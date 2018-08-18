package uia.sketch.layer;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

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
public class GridDrawer implements LayerDrawer {

    private String layerName;

    private LayerType layerType;

    private PhotoPanel panel;

    private int width;

    private Color lineColor;

    private Point offset;

    private int degree;

    private boolean enabled;

    /**
     * Constructor.
     */
    public GridDrawer() {
        this(40, Color.gray, 0, true);
    }

    /**
     * Constructor.
     * @param width Width.
     * @param lineColor Line color.
     * @param degree Degree.
     * @param enabled Enabled or not.
     */
    public GridDrawer(int width, Color lineColor, int degree, boolean enabled) {
        this.width = width;
        this.lineColor = lineColor;
        this.offset = new Point(0, 0);
        this.degree = degree;
        this.enabled = enabled;
    }

    @Override
    public void reset() {
        this.width = 40;
        this.lineColor = Color.gray;
        this.offset = new Point(0, 0);
        this.degree = 0;
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
    public int getDegree() {
        return this.degree;
    }

    @Override
    public void setDegree(int degree) {
        int temp = degree % 45;
        if (temp == this.degree) {
            return;
        }
        this.degree = temp;
        repaint();
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

    /**
     * Set width.
     * @param width Width.
     */
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

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point offset = this.offset;
        g2d.translate(offset.x, offset.y);

        int left = -offset.x - this.panel.getWidth();
        int top = -offset.y - this.panel.getHeight();
        int right = left + 3 * this.panel.getWidth();
        int bottom = top + 3 * this.panel.getHeight();

        int x0 = (int) Math.ceil((double) left / this.width) * this.width;
        int y0 = (int) Math.ceil((double) top / this.width) * this.width;

        g2d.rotate(Math.toRadians(this.degree));

        g2d.setColor(this.lineColor);
        while (x0 < right) {
            g2d.drawLine(x0, top, x0, bottom);
            x0 += this.width;
        }

        while (y0 < bottom) {
            g2d.drawLine(left, y0, right, y0);
            y0 += this.width;
        }

        // g.fillRect(-2, -2, 5, 5);
        int hw = this.width / 2;
        Color contrastColor = LayerDrawer.contrastColor(this.lineColor);
        g2d.setPaint(new GradientPaint(-hw, 0, this.lineColor, 0, 0, contrastColor));
        g2d.drawLine(-this.width, 0, 0, 0);
        g2d.setPaint(new GradientPaint(0, 0, contrastColor, hw, 0, this.lineColor));
        g2d.drawLine(0, 0, this.width, 0);
        g2d.setPaint(new GradientPaint(0, -hw, this.lineColor, 0, 0, contrastColor));
        g2d.drawLine(0, -this.width, 0, 0);
        g2d.setPaint(new GradientPaint(0, 0, contrastColor, 0, hw, this.lineColor));
        g2d.drawLine(0, 0, 0, this.width);

        //g.setColor(contrastColor(this.lineColor));
        //g.drawLine(-5, 0, 5, 0);
        //g.drawLine(0, -5, 0, 5);

        g2d.rotate(Math.toRadians(-this.degree));
        g2d.translate(-offset.x, -offset.y);
    }

    private void repaint() {
        if (this.panel != null) {
            this.panel.repaint();
        }
    }
}
