package blue.made.angleclient.network;

import blue.made.angleclient.network.packet.IPacket;
import blue.made.angleclient.network.packet.OPacket;
import blue.made.angleclient.network.packet.PacketManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Sam Sartor on 5/26/2016.
 */
public class Network {
    class NetClientInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipe = ch.pipeline();

            pipe.addLast(PacketManager.makeDecoder());
            pipe.addLast(PacketManager.makeEncoder());

            pipe.addLast(new NetClientHandler());
        }
    }

    class NetClientHandler extends SimpleChannelInboundHandler<IPacket> {
        @Override
        protected void messageReceived(ChannelHandlerContext ctx, IPacket msg) throws Exception {
            msg.onProcess();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            onDisconnect();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
        }
    }

    public final String host;
    public final int port;
    private EventLoopGroup group;
    private Channel ch;
    private ChannelFuture lastWriteFuture = null;

    public Network(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws Exception {
        if (ch == null) {
            group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new NetClientInitializer());
            ch = b.connect(host, port).sync().channel();
        }
    }

    public void send(OPacket packet) {
        if (ch != null) {
            lastWriteFuture = ch.writeAndFlush(packet);
        }
    }

    public void disconnect() {
        if (ch != null) {
            System.out.println("Disconnecting...");
            if (lastWriteFuture != null) {
                try {
                    lastWriteFuture.sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ch.disconnect();
            ch = null;
        }
    }

    private void onDisconnect() {
        group.shutdownGracefully();
    }
}
