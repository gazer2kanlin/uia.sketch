package uia.sketch.layer;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
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
public class PerspectiveDrawer implements LayerDrawer {

    private String layerName;

    private LayerType layerType;

    private PhotoPanel panel;

    private int width;

    private Color lineColor;

    private Point zeroPoint;

    private boolean enabled;

    private Integer boundY;

    private Integer boundX;

    private int degree;

    /**
     * Constructor.
     */
    public PerspectiveDrawer() {
        this(100, Color.gray, true);
    }

    /**
     * Constructor.
     * @param width Width.
     * @param lineColor Line color.
     * @param degree Degree.
     * @param enabled Enabled or not.
     */
    public PerspectiveDrawer(int width, Color lineColor, boolean enabled) {
        this.width = width;
        this.lineColor = lineColor;
        this.zeroPoint = new Point(400, 480);
        this.enabled = enabled;
    }

    @Override
    public void reset() {
        this.width = 100;
        this.lineColor = Color.gray;
        this.zeroPoint = new Point(0, 0);
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

    /**
     * Set enabled or not.
     * @param enabled
     */
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
        return this.zeroPoint;
    }

    @Override
    public void setOffset(Point offset) {
        if (offset == null || this.zeroPoint.equals(offset)) {
            return;
        }
        this.zeroPoint = offset;
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
    public void doubleClick(int x, int y) {
    }

    @Override
    public void paint(Graphics g) {
        if (this.panel == null || !this.enabled) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.gray);
        if (this.boundY != null) {
            g2d.drawLine(0, this.boundY, this.panel.getWidth(), this.boundY);
        }
        if (this.boundX != null) {
            g2d.drawLine(this.boundX, 0, this.boundX, this.panel.getHeight());
        }

        g2d.translate(this.zeroPoint.x, this.zeroPoint.y);
        g2d.rotate(Math.toRadians(this.degree));
        g2d.setColor(this.lineColor);

        int left = -this.zeroPoint.x;
        int right = this.panel.getWidth() - this.zeroPoint.x;
        int top = -this.zeroPoint.y;
        int bottom = this.panel.getHeight() - this.zeroPoint.y;

        if (this.boundX != null) {
            left = this.zeroPoint.x > this.boundX ? this.boundX - this.zeroPoint.x : -this.zeroPoint.x;
            right = this.zeroPoint.x < this.boundX ? this.boundX - this.zeroPoint.x : this.panel.getWidth() - this.zeroPoint.x;

        }
        if (this.boundY != null) {
            top = this.zeroPoint.y > this.boundY ? this.boundY - this.zeroPoint.y : -this.zeroPoint.y;
            bottom = this.zeroPoint.y < this.boundY ? this.boundY - this.zeroPoint.y : this.panel.getHeight() - this.zeroPoint.y;
        }

        Color contrastColor = LayerDrawer.contrastColor(this.lineColor);

        int w = this.width;
        if (this.boundY != null) {
            int y = this.boundY - this.zeroPoint.y;

            Polygon p1 = new Polygon();
            p1.addPoint(0, 0);
            p1.addPoint(left, y);
            p1.addPoint(right, y);
            p1.addPoint(0, 0);
            g2d.setColor(new Color(this.lineColor.getRed(), this.lineColor.getGreen(), this.lineColor.getBlue(), 80));
            g2d.fillPolygon(p1);

            g2d.setColor(this.lineColor);
            g2d.setPaint(new GradientPaint(0, 0, this.lineColor, 0, y, contrastColor));
            g2d.drawLine(0, 0, 0, y);
            while (w < this.panel.getWidth()) {
                if (-w >= left) {
                    g2d.setPaint(new GradientPaint(0, 0, this.lineColor, -w, y, contrastColor));
                    g2d.drawLine(0, 0, -w, y);
                }
                if (w <= right) {
                    g2d.setPaint(new GradientPaint(0, 0, this.lineColor, w, y, contrastColor));
                    g2d.drawLine(0, 0, w, y);
                }
                w += this.width;
            }
        }

        w = this.width;
        if (this.boundX != null) {
            int x = this.boundX - this.zeroPoint.x;

            Polygon p1 = new Polygon();
            p1.addPoint(0, 0);
            p1.addPoint(x, top);
            p1.addPoint(x, bottom);
            p1.addPoint(0, 0);
            g2d.setColor(new Color(this.lineColor.getRed(), this.lineColor.getGreen(), this.lineColor.getBlue(), 32));
            g2d.fillPolygon(p1);

            g2d.setColor(this.lineColor);
            g2d.setPaint(new GradientPaint(0, 0, this.lineColor, x, 0, contrastColor));
            g2d.drawLine(0, 0, x, 0);
            while (w < this.panel.getHeight()) {
                if (-w >= top) {
                    g2d.setPaint(new GradientPaint(0, 0, this.lineColor, x, -w, contrastColor));
                    g2d.drawLine(0, 0, x, -w);
                }
                if (w <= bottom) {
                    g2d.setPaint(new GradientPaint(0, 0, this.lineColor, x, w, contrastColor));
                    g2d.drawLine(0, 0, x, w);
                }
                w += this.width;
            }
        }

        g2d.translate(-this.zeroPoint.x, -this.zeroPoint.y);
    }

    private void repaint() {
        if (this.panel != null) {
            this.panel.repaint();
        }
    }
}
