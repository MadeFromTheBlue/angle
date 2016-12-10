package blue.made.angleserver.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by Sam Sartor on 5/25/2016.
 */
public class NetServerInitializer extends ChannelInitializer<SocketChannel> {
    public final Network net;

    public NetServerInitializer(Network net) {
        this.net = net;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipe = ch.pipeline();

        pipe.addLast(net.packs.newEncoder());
        pipe.addLast(net.packs.newDecoder());
        pipe.addLast(new NetServerHandler(net));
    }
}
