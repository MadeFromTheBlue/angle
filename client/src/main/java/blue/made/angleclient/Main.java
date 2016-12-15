package blue.made.angleclient;

import blue.made.angleclient.network.Network;
import blue.made.angleclient.ui.UI;
import blue.made.bcf.BCFWriter;

/**
 * Created by Sam Sartor on 5/25/2016.
 */
public class Main {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "16127"));

    public static void main(String[] args) throws Exception {
        String name = "test2d";

        Game.INSTANCE.start();

        // Connect the network
        Network net = new Network(HOST, PORT);
        net.connect();
        Game.INSTANCE.net = net;

        // Connect the UI
        UI ui = new UI();
        ui.show(Game.INSTANCE);
        Game.INSTANCE.ui = ui;

        net.send(data -> {
            BCFWriter bcf = new BCFWriter(data);
            BCFWriter.Map map = bcf.startMap();
            map.writeName("name");
            map.write(name);
            map.end();
            return 0x01;
        });

        net.send(data -> {
            BCFWriter bcf = new BCFWriter(data);
            BCFWriter.Map map = bcf.startMap();
            map.writeName("what");
            map.write("terrain_chunk");
            map.writeName("range");
            BCFWriter.Map range = map.startMap();
            range.writeName("x0");
            range.write(Integer.MIN_VALUE);
            range.writeName("x1");
            range.write(Integer.MAX_VALUE);
            range.writeName("y0");
            range.write(Integer.MIN_VALUE);
            range.writeName("y1");
            range.write(Integer.MAX_VALUE);
            range.end();
            map.end();
            return 0x02;
        });
    }
}
