package blue.made.angleserver.network;

import blue.made.angleserver.network.packet.in.IPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * Created by Sam Sartor on 5/25/2016.
 */
public class NetServerHandler extends SimpleChannelInboundHandler<IPacket> {
    public static final AttributeKey<Client> clientAttribute = AttributeKey.newInstance("client");

    public final Network net;
    public Client client;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    protected NetServerHandler(Network net) {
        super();
        this.net = net;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, IPacket msg) throws Exception {
        msg.onProcessed();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.onDisconnect();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        client = new Client(ctx.channel());
        ctx.channel().attr(clientAttribute).set(client);

        client.onConnect();
    }
}
