package blue.made.angleserver.network;

import blue.made.angleserver.Game;
import blue.made.angleserver.Player;
import blue.made.angleserver.network.packet.in.I01PreConnect;
import blue.made.angleserver.network.packet.out.O01ServerInfo;
import blue.made.angleserver.network.packet.out.O20TerrainMeta;
import blue.made.angleserver.network.packet.out.OPacket;
import io.netty.channel.Channel;

import java.net.SocketAddress;

/**
 * Created by Sam Sartor on 3/8/16.
 */
public class Client {
    private Channel channel;
    public String name = null;
    public Player player = null;
    public boolean selfChecked = false;

    public Client(Channel channel) {
        this.channel = channel;
    }

    public void onConnect() {
        send(new O01ServerInfo());
    }

    public void onActivate(I01PreConnect info) {
        name = info.name;
        Game.INSTANCE.active.add(this);
        send(new O20TerrainMeta(Game.INSTANCE.world, false));

    }

    public void onJoin() {

    }

    public void onDisconnect() {
        Game.INSTANCE.active.remove(this);
        System.out.printf("Client \"%s\" has disconnected.%n", getName());
    }

    public SocketAddress getAddress() {
        return channel.localAddress();
    }

    public void kick() {
        channel.disconnect();
    }

    public void send(OPacket pack) {
        channel.writeAndFlush(pack);
    }

    public void queuePacket(OPacket pack) {
        channel.write(pack);
    }

    public void send() {
        channel.flush();
    }

    public String getName() {
        if (name != null) return name;
        return getAddress().toString();
    }

    public boolean isConnected() {
        return channel.isActive();
    }
}
