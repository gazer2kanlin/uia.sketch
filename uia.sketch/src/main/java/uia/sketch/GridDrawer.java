package uia.sketch;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

/**
 * Grid drawer.
 *
 * @author Kyle K. Lin
 *
 */
public class GridDrawer {

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

    public void reset() {
        this.width = 40;
        this.lineColor = Color.gray;
        this.offset = new Point(0, 0);
        this.degree = 0;
        this.enabled = true;
    }

    /**
     * Get the degree.
     * @return Degree.
     */
    public int getDegree() {
        return this.degree;
    }

    /**
     * Set the degree between -45 and 45.
     * @param degree
     */
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
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Set enabled or not.
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        repaint();
    }

    /**
     * Get offset.
     * @return Offest.
     */
    public Point getOffset() {
        return this.offset;
    }

    /**
     * Set offset.
     * @param offset Offset.
     */
    public void setOffset(Point offset) {
        if (offset == null || this.offset.equals(offset)) {
            return;
        }
        this.offset = offset;
        repaint();
    }

    /**
     * Get width.
     * @return Width.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Set width.
     * @param width Width.
     */
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
    public Color getLineColor() {
        return this.lineColor;
    }

    /**
     * Set line color.
     * @param lineColor Line color.
     */
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
    public PhotoPanel getPhotoPanel() {
        return this.panel;
    }

    /**
     * Set photo panel.
     * @param panel Photo panel.
     */
    public void setPhotoPanel(PhotoPanel panel) {
        this.panel = panel;
    }

    /**
     * Paint grid.
     * @param g Graphics from photo panel.
     */
    void paint(Graphics g) {
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

        double r = Math.toRadians(this.degree);
        g2d.rotate(r);

        g.setColor(this.lineColor);
        while (x0 < right) {
            g.drawLine(x0, top, x0, bottom);
            x0 += this.width;
        }

        while (y0 < bottom) {
            g.drawLine(left, y0, right, y0);
            y0 += this.width;
        }

        // g.fillRect(-2, -2, 5, 5);
        int hw = this.width / 2;
        Color contrastColor = contrastColor(this.lineColor);
        g2d.setPaint(new GradientPaint(-hw, 0, this.lineColor, 0, 0, contrastColor));
        g.drawLine(-this.width, 0, 0, 0);
        g2d.setPaint(new GradientPaint(0, 0, contrastColor, hw, 0, this.lineColor));
        g.drawLine(0, 0, this.width, 0);
        g2d.setPaint(new GradientPaint(0, -hw, this.lineColor, 0, 0, contrastColor));
        g.drawLine(0, -this.width, 0, 0);
        g2d.setPaint(new GradientPaint(0, 0, contrastColor, 0, hw, this.lineColor));
        g.drawLine(0, 0, 0, this.width);

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

    private static Color contrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }
}
