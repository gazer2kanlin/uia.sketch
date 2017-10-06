package uia.sketch;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import uia.sketch.model.SketchBookTypeHelper;
import uia.sketch.model.xml.LayerType;

/**
 * Grid drawer.
 *
 * @author Kyle K. Lin
 *
 */
public class TriangleDrawer implements LayerDrawer {

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
    public TriangleDrawer() {
        this(40, Color.gray, 0, true);
    }

    /**
     * Constructor.
     * @param width Width.
     * @param lineColor Line color.
     * @param degree Degree.
     * @param enabled Enabled or not.
     */
    public TriangleDrawer(int width, Color lineColor, int degree, boolean enabled) {
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

    /**
     * Get the degree.
     * @return Degree.
     */
    @Override
    public int getDegree() {
        return this.degree;
    }

    /**
     * Set the degree between -45 and 45.
     * @param degree
     */
    @Override
    public void setDegree(int degree) {
        int temp = degree % 45;
        if (temp == this.degree) {
            return;
        }
        this.degree = temp;
        repaint();
    }

    /**
     * Get enabled or not.
     * @return Enabled.
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Set enabled or not.
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        repaint();
    }

    /**
     * Get offset.
     * @return Offest.
     */
    @Override
    public Point getOffset() {
        return this.offset;
    }

    /**
     * Set offset.
     * @param offset Offset.
     */
    @Override
    public void setOffset(Point offset) {
        if (offset == null || this.offset.equals(offset)) {
            return;
        }
        this.offset = offset;
        this.layerType.setOffset(SketchBookTypeHelper.toPointString(offset));
        repaint();
    }

    /**
     * Get width.
     * @return Width.
     */
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

    /**
     * Get line color.
     * @return Line color.
     */
    @Override
    public Color getLineColor() {
        return this.lineColor;
    }

    /**
     * Set line color.
     * @param lineColor Line color.
     */
    @Override
    public void setLineColor(Color lineColor) {
        if (this.lineColor.equals(lineColor)) {
            return;
        }
        this.lineColor = lineColor;
        repaint();
    }

    /**
     * Get photo panel.
     * @return Photo panel.
     */
    @Override
    public PhotoPanel getPhotoPanel() {
        return this.panel;
    }

    /**
     * Set photo panel.
     * @param panel Photo panel.
     */
    @Override
    public void setPhotoPanel(PhotoPanel panel) {
        this.panel = panel;
    }

    /**
     * Paint grid.
     * @param g Graphics from photo panel.
     */
    @Override
    public void paint(Graphics g) {
        if (this.panel == null || !this.enabled) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point offset = this.offset;
        g2d.translate(offset.x, offset.y);

        double r = Math.toRadians(this.degree);
        g2d.rotate(r);
        g2d.setColor(this.lineColor);

        int left = -offset.x - 3 * this.panel.getWidth();
        int top = -offset.y - 3 * this.panel.getHeight();
        int right = left + 7 * this.panel.getWidth();
        int bottom = top + 7 * this.panel.getHeight();

        int y0 = (int) Math.ceil((double) top / this.width) * this.width;
        int y = y0;
        while (y < bottom) {
            g2d.drawLine(left, y, right, y);
            y += this.width;
        }

        g2d.rotate(Math.toRadians(60));
        y = y0;
        while (y < bottom) {
            g2d.drawLine(left, y, right, y);
            y += this.width;
        }
        g2d.rotate(Math.toRadians(-120));
        y = y0;
        while (y < bottom) {
            g2d.drawLine(left, y, right, y);
            y += this.width;
        }
        g2d.rotate(Math.toRadians(60));

        int hw = this.width / 2;
        Color contrastColor = contrastColor(this.lineColor);
        g2d.setPaint(new GradientPaint(-hw, 0, this.lineColor, 0, 0, contrastColor));
        g2d.drawLine(-this.width, 0, 0, 0);
        g2d.setPaint(new GradientPaint(0, 0, contrastColor, hw, 0, this.lineColor));
        g2d.drawLine(0, 0, this.width, 0);
        g2d.rotate(Math.toRadians(60));
        g2d.setPaint(new GradientPaint(-hw, 0, this.lineColor, 0, 0, contrastColor));
        g2d.drawLine(-this.width, 0, 0, 0);
        g2d.setPaint(new GradientPaint(0, 0, contrastColor, hw, 0, this.lineColor));
        g2d.drawLine(0, 0, this.width, 0);
        g2d.rotate(Math.toRadians(-120));
        g2d.setPaint(new GradientPaint(-hw, 0, this.lineColor, 0, 0, contrastColor));
        g2d.drawLine(-this.width, 0, 0, 0);
        g2d.setPaint(new GradientPaint(0, 0, contrastColor, hw, 0, this.lineColor));
        g2d.drawLine(0, 0, this.width, 0);
        g2d.rotate(Math.toRadians(60));

        g2d.rotate(Math.toRadians(-this.degree));
        g2d.translate(-offset.x, -offset.y);
    }

    private void repaint() {
        if (this.panel != null) {
            this.panel.repaint();
        }
    }

    private static Color contrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }
}
