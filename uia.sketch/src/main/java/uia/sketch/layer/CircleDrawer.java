package uia.sketch.layer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import uia.sketch.LayerDrawer;
import uia.sketch.PhotoPanel;
import uia.sketch.model.SketchBookTypeHelper;
import uia.sketch.model.xml.LayerType;

/**
 * Grid drawer.
 *
 * @author Kyle K. Lin
 *
 */
public class CircleDrawer implements LayerDrawer {

    private String layerName;

    private LayerType layerType;

    private PhotoPanel panel;

    private int width;

    private Color lineColor;

    private Point offset;

    private boolean enabled;

    private int degree;

    /**
     * Constructor.
     */
    public CircleDrawer() {
        this(100, Color.gray, true);
    }

    /**
     * Constructor.
     * @param width Width.
     * @param lineColor Line color.
     * @param degree Degree.
     * @param enabled Enabled or not.
     */
    public CircleDrawer(int width, Color lineColor, boolean enabled) {
        this.width = width;
        this.lineColor = lineColor;
        this.offset = new Point(180, 180);
        this.enabled = enabled;
    }

    @Override
    public void reset() {
        this.width = 100;
        this.lineColor = Color.gray;
        this.offset = new Point(0, 0);
        this.enabled = true;
    }

    @Override
    public void apply(LayerType layerType) {
        this.layerType = layerType;
        setLayerName(layerType.getName());
        setDegree(layerType.getDegree());
        setEnabled(layerType.isEnabled());
        setWidth(layerType.getWidth());
        setOffset(SketchBookTypeHelper.toPoint(layerType.getOffset()));
        setLineColor(SketchBookTypeHelper.toColor(layerType.getLineColor()));
    }

    @Override
    public String getLayerName() {
        return this.layerName;
    }

    @Override
    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        repaint();
    }

    @Override
    public int getDegree() {
        return this.degree;
    }

    @Override
    public void setDegree(int degree) {
        this.degree = degree;
        repaint();
    }

    @Override
    public Point getOffset() {
        return this.offset;
    }

    @Override
    public void setOffset(Point offset) {
        if (offset == null || this.offset.equals(offset)) {
            return;
        }
        this.offset = offset;
        this.layerType.setOffset(SketchBookTypeHelper.toPointString(offset));
        repaint();
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public void setWidth(int width) {
        if (this.width == width) {
            return;
        }
        this.width = width;
        repaint();
    }

    @Override
    public Color getLineColor() {
        return this.lineColor;
    }

    @Override
    public void setLineColor(Color lineColor) {
        if (this.lineColor.equals(lineColor)) {
            return;
        }
        this.lineColor = lineColor;
        repaint();
    }

    @Override
    public PhotoPanel getPhotoPanel() {
        return this.panel;
    }

    @Override
    public void setPhotoPanel(PhotoPanel panel) {
        this.panel = panel;
    }

    @Override
    public void setHorizontal(int horizontal) {
    }

    @Override
    public int getHorizontal() {
        return -1;
    }

    @Override
    public void setVertical(int vertical) {
    }

    @Override
    public int getVertical() {
        return -1;
    }

    @Override
    public void paint(Graphics g) {
        if (this.panel == null || !this.enabled) {
            return;
        }

        Ellipse2D.Double circle = new Ellipse2D.Double();
        circle.width = this.width * 2;
        circle.height = this.width * 2;
        circle.x = this.offset.getX() - this.width;
        circle.y = this.offset.getY() - this.width;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(this.lineColor);
        g2d.draw(circle);
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fill(circle);
        g2d.setColor(this.lineColor);

        g2d.translate(this.offset.x, this.offset.y);
        g2d.rotate(Math.toRadians(this.degree));
        g2d.drawLine(0, -this.width - 10, 0, this.width + 10);
        g2d.drawLine(-this.width - 10, 0, this.width + 10, 0);
        g2d.rotate(Math.toRadians(-this.degree));
        g2d.translate(-this.offset.x, -this.offset.y);
    }

    private void repaint() {
        if (this.panel != null) {
            this.panel.repaint();
        }
    }
}
