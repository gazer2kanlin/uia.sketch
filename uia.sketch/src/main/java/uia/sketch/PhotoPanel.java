package uia.sketch;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import uia.sketch.model.SketchBookTypeHelper;

/**
 * Photo panel.
 *
 * @author Kyle K. Lin
 *
 */
public class PhotoPanel extends JPanel {

    /**
     * Drag target.
     *
     * @author Kyle K. Lin
     *
     */
    public enum DragTargetType {
        PHOTO, GRID1, GRID2, CIRCLE, TRIANGLE
    }

    private static final long serialVersionUID = -8559394957522314281L;

    private PhotoFile pf;

    private File file;

    private BufferedImage origImage;

    private Image backgroundImage;

    private LayerDrawer selectedLayerDrawer;

    private LinkedHashMap<String, LayerDrawer> layerDrawers;

    private DragTargetType dragTarget;

    private Point offset;

    private double zoom;

    private int viewWidth;

    private int viewHeight;

    private SketchBoardFrame mainFrame;

    /**
     * Constructor.
     */
    public PhotoPanel() {
        this.layerDrawers = new LinkedHashMap<String, LayerDrawer>();

        this.dragTarget = DragTargetType.PHOTO;
        this.offset = new Point(0, 0);
        this.zoom = 1.0d;

        GridDrawer grid1LayerDrawer = new GridDrawer();
        grid1LayerDrawer.setLayerName("GRID1");
        grid1LayerDrawer.setPhotoPanel(this);
        this.layerDrawers.put(grid1LayerDrawer.getLayerName(), grid1LayerDrawer);

        GridDrawer grid2LayerDrawer = new GridDrawer(100, new Color(200, 200, 200), 0, false);
        grid2LayerDrawer.setLayerName("GRID2");
        grid2LayerDrawer.setPhotoPanel(this);
        this.layerDrawers.put(grid2LayerDrawer.getLayerName(), grid2LayerDrawer);

        CircleDrawer circleDrawer = new CircleDrawer(100, new Color(180, 180, 180), false);
        circleDrawer.setLayerName("CIRCLE");
        circleDrawer.setPhotoPanel(this);
        this.layerDrawers.put(circleDrawer.getLayerName(), circleDrawer);

        TriangleDrawer triDrawer = new TriangleDrawer(100, new Color(180, 180, 180), 0, false);
        triDrawer.setLayerName("TRIANGLE");
        triDrawer.setPhotoPanel(this);
        this.layerDrawers.put(triDrawer.getLayerName(), triDrawer);

        this.selectedLayerDrawer = grid1LayerDrawer;

        GridMotionListener listener = new GridMotionListener();

        setSize(900, 800);
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public SketchBoardFrame getMainFrame() {
        return this.mainFrame;
    }

    public void setMainFrame(SketchBoardFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Reload image.
     * @return
     */
    public boolean reloadImage() {
        return this.file == null ? false : loadImage(this.file);
    }

    /**
     * Reset.
     */
    public void reset() {
        this.file = null;
        this.origImage = null;
        this.backgroundImage = null;

        boolean enabled = true;
        for (LayerDrawer layerDrawer : this.layerDrawers.values()) {
            layerDrawer.reset();
            layerDrawer.setEnabled(enabled);
            enabled = false;
        }
        repaint();
    }

    /**
     * Load new image file.
     * @param fileName File with full path.
     * @return Success of not.
     */
    public boolean loadImage(PhotoFile pf) {
        this.pf = pf;
        try {
            loadImage(new File(pf.config.getPath()));
            setOffset(SketchBookTypeHelper.toPoint(pf.config.getOffset()));
            setZoom(pf.config.getZoom(), pf.config.getViewWidth(), pf.config.getViewHeight());
            return true;
        }
        catch (Exception ex) {
            return loadImage((File) null);
        }
    }

    /**
     * Get width of view.
     * @return Width.
     */
    public int getViewWidth() {
        return this.viewWidth;
    }

    /**
     * Get height of view.
     * @return Height.
     */
    public int getViewHeight() {
        return this.viewHeight;
    }

    /**
     * Load new image file.
     * @param file Image file.
     * @return Success of not.
     */
    public boolean loadImage(File file) {
        try {
            this.file = file;
            this.origImage = ImageIO.read(this.file);
            this.zoom = 1.0d;

            double ph = (double) getHeight() / this.origImage.getHeight();
            double pw = (double) getWidth() / this.origImage.getWidth();
            double zoomFix = Math.min(ph, pw);
            this.viewWidth = (int) (this.origImage.getWidth() * zoomFix);
            this.viewHeight = (int) (this.origImage.getHeight() * zoomFix);
            this.backgroundImage = this.origImage.getScaledInstance(this.viewWidth, this.viewHeight, Image.SCALE_SMOOTH);
            return true;
        }
        catch (Exception ex) {
            this.file = null;
            this.origImage = null;
            this.backgroundImage = null;
            return false;
        }
        finally {
            this.offset = new Point(0, 0);
            this.zoom = 1.0d;
            repaint();
        }
    }

    /**
     * Get offset
     * @return Offset.
     */
    public Point getOffset() {
        return this.offset;
    }

    /**
     * Set offset.
     * @param offset Offset.
     */
    public void setOffset(Point offset) {
        if (this.offset.equals(offset)) {
            return;
        }
        this.offset = offset;
        if (this.pf != null && this.pf.config != null) {
            this.pf.config.setOffset(SketchBookTypeHelper.toPointString(this.offset));
        }
        repaint();
    }

    /**
     * Get zoom.
     * @return Zoom.
     */
    public double getZoom() {
        return this.zoom;
    }

    /**
     * Set zoom.
     * @param zoom Zoom.
     */
    public void setZoom(double zoom) {
        if (this.origImage == null || this.zoom == zoom) {
            return;
        }
        this.zoom = zoom;
        this.backgroundImage = this.origImage.getScaledInstance(
                (int) (this.viewWidth * this.zoom),
                (int) (this.viewHeight * this.zoom),
                Image.SCALE_SMOOTH);
        repaint();
    }

    /**
     * Set zoom and view.
     * @param zoom Zoom.
     * @param viewWidth Width of view.
     * @param viewHeight Height of view.
     */
    public void setZoom(double zoom, int viewWidth, int viewHeight) {
        if (this.origImage == null || this.zoom == zoom) {
            return;
        }
        this.zoom = zoom;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.backgroundImage = this.origImage.getScaledInstance(
                (int) (this.viewWidth * this.zoom),
                (int) (this.viewHeight * this.zoom),
                Image.SCALE_SMOOTH);
        repaint();
    }

    /**
     * Get drag mode.
     * @return Drag mode.
     */
    public DragTargetType getDragMode() {
        return this.dragTarget;
    }

    /**
     * Set drag mode.
     * @param dragMode Drag mode.
     */
    public void setDragMode(DragTargetType dragMode) {
        this.dragTarget = dragMode;
    }

    public LayerDrawer getLayerDrawer(String layerName) {
        return this.layerDrawers.get(layerName);
    }

    /**
     * Get selected grid drawer.
     * @return Grid drawer.
     */
    public LayerDrawer getSelectedLayerDrawer() {
        return this.selectedLayerDrawer;
    }

    public void selectLayerDrawer(String layerName) {
        LayerDrawer layerDrawer = this.layerDrawers.get(layerName);
        if (layerDrawer != null) {
            this.selectedLayerDrawer = layerDrawer;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.backgroundImage, this.offset.x, this.offset.y, this);
        for (LayerDrawer layerDrawer : this.layerDrawers.values()) {
            layerDrawer.paint(g);
        }
    }

    /**
     * Grid motion listener implementation.
     *
     * @author Kyle K. Lin
     *
     */
    class GridMotionListener extends MouseAdapter implements MouseMotionListener {

        private Point pt;

        private int x;

        private int y;

        @Override
        public void mousePressed(MouseEvent evt) {
            if (PhotoPanel.this.selectedLayerDrawer == null) {
                return;
            }

            this.pt = evt.getPoint();
            if (PhotoPanel.this.dragTarget != DragTargetType.PHOTO) {
                this.x = PhotoPanel.this.selectedLayerDrawer.getOffset().x;
                this.y = PhotoPanel.this.selectedLayerDrawer.getOffset().y;
            }
            else {
                this.x = PhotoPanel.this.offset.x;
                this.y = PhotoPanel.this.offset.y;
            }
            PhotoPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            this.pt = null;
            PhotoPanel.this.setCursor(Cursor.getDefaultCursor());
        }

        @Override
        public void mouseDragged(MouseEvent evt) {
            if (this.pt == null || PhotoPanel.this.selectedLayerDrawer == null) {
                return;
            }

            int ox = (this.x + evt.getX() - this.pt.x);
            int oy = (this.y + evt.getY() - this.pt.y);

            Point pt = new Point(ox, oy);
            if (PhotoPanel.this.dragTarget != DragTargetType.PHOTO) {
                PhotoPanel.this.selectedLayerDrawer.setOffset(pt);
            }
            else {
                PhotoPanel.this.setOffset(pt);
            }
        }
    }
}
