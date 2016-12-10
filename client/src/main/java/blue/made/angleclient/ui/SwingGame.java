package blue.made.angleclient.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * Created by Sam Sartor on 6/5/2016.
 */
public class SwingGame {
    private class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            AffineTransform t = new AffineTransform(g2.getTransform());
            t.concatenate(getDrawToScreen());

            for (Consumer<Graphics2D> p : painters) {
                g2.setTransform(t);
                p.accept(g2);
            }
        }
    }

    public JFrame frame;
    public DrawPanel panel;
    private LinkedList<Consumer<Graphics2D>> painters = new LinkedList<>();

    private boolean camfill = true;
    private double camx = 500;
    private double camy = 500;
    private double camscale = 500;

    public double minx;
    public double miny;
    public double maxx;
    public double maxy;
    protected double drawScale = 1;

    private double mousex;
    private double mousey;
    private double mouseu;
    private double mousev;
    private boolean mousein = false;

    public Consumer<Object> onMouseMove = o -> {
    };

    public java.util.List<MouseAdapter> adapters = new LinkedList<>();

    public void openUI() {
        updateCamBounds();
        frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pane = new JPanel();
        frame.setContentPane(pane);
        pane.setBackground(Color.BLACK);
        pane.setLayout(new BorderLayout());

        panel = new DrawPanel();
        pane.add(panel, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(500, 500));

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateCamBounds();
            }
        });
        frame.pack();
        frame.setVisible(true);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                adapters.forEach(a -> a.mouseClicked(e));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                adapters.forEach(a -> a.mousePressed(e));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                adapters.forEach(a -> a.mouseReleased(e));
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                adapters.forEach(a -> a.mouseWheelMoved(e));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseu = e.getX();
                mousev = e.getY();
                recalcMouse();
                onMouseMove.accept(null);
                adapters.forEach(a -> a.mouseDragged(e));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseu = e.getX();
                mousev = e.getY();
                recalcMouse();
                onMouseMove.accept(null);
                adapters.forEach(a -> a.mouseMoved(e));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mousein = false;
                mousex = Double.NaN;
                mousey = Double.NaN;
                onMouseMove.accept(null);
                adapters.forEach(a -> a.mouseExited(e));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                mousein = true;
                onMouseMove.accept(null);
                adapters.forEach(a -> a.mouseEntered(e));
            }
        };

        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
        panel.addMouseWheelListener(mouseAdapter);

        SwingUtilities.invokeLater(() -> panel.requestFocus());
    }

    public AffineTransform getDrawToScreen() {
        AffineTransform t = new AffineTransform();

        double xscale = panel.getWidth() / (maxx - minx);
        double yscale = panel.getHeight() / (maxy - miny);

        t.scale(xscale / drawScale, yscale / drawScale);
        t.translate(-minx * drawScale, -miny * drawScale);

        return t;
    }

    public AffineTransform getWorldToScreen() {
        AffineTransform t = new AffineTransform();

        double xscale = panel.getWidth() / (maxx - minx);
        double yscale = panel.getHeight() / (maxy - miny);

        t.scale(xscale, yscale);
        t.translate(-minx, -miny);

        return t;
    }

    public AffineTransform getScreenToWorld() {
        AffineTransform t = new AffineTransform();

        double xscale = (maxx - minx) / panel.getWidth();
        double yscale = (maxy - miny) / panel.getHeight();

        t.translate(minx, miny);
        t.scale(xscale, yscale);

        return t;
    }

    public void closeUI() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public double getMouseX() {
        return mousex;
    }

    public double getMouseY() {
        return mousey;
    }

    public boolean isMouseIn() {
        return mousein;
    }

    private void recalcMouse() {
        if (panel != null) {
            AffineTransform t = getScreenToWorld();
            Point2D m = new Point2D.Double();
            m.setLocation(mouseu, mousev);
            Point2D w = new Point2D.Double();
            t.transform(m, w);
            mousex = w.getX();
            mousey = w.getY();
            onMouseMove.accept(null);
        }
    }


    public void setCamera(double x, double y, double scale, boolean fill, double drawScale) {
        camx = x;
        camy = y;
        camscale = scale * .5;
        camfill = fill;
        this.drawScale = drawScale;
        updateCamBounds();
    }

    private void updateCamBounds() {
        double scalew = 1;
        double scaleh = 1;
        if (panel != null) scaleh = (double) panel.getHeight() / panel.getWidth();
        if (camfill == scaleh > 1) {
            scalew = 1 / scaleh;
            scaleh = 1;
        }
        minx = camx - camscale * scalew;
        maxx = camx + camscale * scalew;
        miny = camy - camscale * scaleh;
        maxy = camy + camscale * scaleh;
        recalcMouse();
        notifyRedraw();
    }

    void setKeyEvent(String name, KeyStroke key, Consumer<ActionEvent> action) {
        panel.getInputMap().put(key, name);
        panel.getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.accept(e);
            }
        });
    }

    public double getDrawScale() {
        return drawScale;
    }

    public void notifyRedraw() {
        if (panel != null) panel.repaint();
    }

    public void addPainter(Consumer<Graphics2D> painter) {
        painters.add(painter);
    }

    public void addMouse(MouseAdapter a) {
        adapters.add(a);
    }
}
