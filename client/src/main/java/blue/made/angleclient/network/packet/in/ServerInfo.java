package blue.made.angleclient.network.packet.in;

import blue.made.angleclient.network.packet.IPacket;

import java.awt.image.BufferedImage;

/**
 * Created by Sam Sartor on 5/25/2016.
 */
public class ServerInfo implements IPacket {
    public final String name;
    public final String desc;
    public final BufferedImage icon;
    public final short major;
    public final short minor;
    public final short patch;

    public ServerInfo(String name, String desc, BufferedImage icon, short major, short minor, short patch) {
        this.name = name;
        this.desc = desc;
        this.icon = icon;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    private String getMajorString() {
        switch (major) {
            case -2:
                return "PREALPHA-";
            case -1:
                return "ALPHA-";
            case 0:
                return "BETA-";
            default:
                return major + ".";
        }
    }

    @Override
    public void onProcess() {
        System.out.printf("%s : %s (%s%d.%d)%n", name, desc, getMajorString(), minor, patch);
    }
}
