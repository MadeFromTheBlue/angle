package blue.made.angleserver.network;

import blue.made.angleserver.network.packet.PacketManager;
import blue.made.angleserver.network.packet.in.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by Sam Sartor on 5/25/2016.
 */
public class Network {
	public int PORT = 16127;
	public PacketManager packs = new PacketManager();

	public void start() {
		try {
			registerPacks();
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new NetServerInitializer(this));
			b.bind(PORT).sync().channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public void registerPacks() throws NoSuchMethodException {
		packs.register(0x01, new I01PreConnect.Loader());
		packs.register(0x02, new I02Request.Loader());
		packs.register(0x04, new I04Ping.Loader());
		packs.register(0x60, new I60Action.Loader());
	}
}
