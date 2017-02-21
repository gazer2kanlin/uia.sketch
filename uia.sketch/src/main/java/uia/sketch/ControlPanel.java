package uia.sketch;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import uia.sketch.PhotoPanel.DragTargetType;
import uia.sketch.model.SketchBookTypeHelper;
import uia.sketch.model.xml.GridType;
import uia.sketch.model.xml.PhotoType;

/**
 *
 *
 * @author Kyle K. Lin
 *
 */
public class ControlPanel extends JPanel {

    private static final long serialVersionUID = 4623087604711068100L;

    private SketchBoardFrame mainFrame;

    // private File lastFile;

    private PhotoFile pf;

    // private JButton phFileButton;

    private JSlider phZoomSlider;

    private JButton phZoomButton;

    private JSlider gridWidthSlider;

    private JButton gridWidthUpButton;

    private JButton gridWidthDownButton;

    private JSlider gridDegreeSlider;

    private JButton gridDegreeButton;

    private JButton colorButton;

    private JRadioButton dragPhotoButton;

    private JRadioButton dragGrid1Button;

    private JRadioButton dragGrid2Button;

    private JToggleButton visible1Button;

    private JToggleButton visible2Button;

    private boolean allowApply;

    /**
     * Constructor.
     */
    public ControlPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(222, 565));

        // Photo
        /**
        this.phFileButton = new JButton(Constants.getString("photo.Select"));
        this.phFileButton.setIcon(Constants.getImageIcon("file.png"));
        this.phFileButton.setBounds(10, 10, 200, 23);
        this.phFileButton.setVisible(false);
        this.phFileButton.addActionListener(evt -> {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(this.lastFile);
            int returnVal = fc.showOpenDialog(ControlPanel.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                this.lastFile = fc.getSelectedFile();
                this.mainFrame.getPhotoPanel().loadImage(this.lastFile);
                this.mainFrame.getStatusPanel().setFileName(this.lastFile.getAbsolutePath());
                this.phZoomSlider.setValue(10);

                this.pf.config.setName(this.lastFile.getName());
                this.pf.config.setPath(this.lastFile.getAbsolutePath());
                this.pf.config.setZoom(1.0);
                this.pf.config.setViewWidth(this.mainFrame.getPhotoPanel().getViewWidth());
                this.pf.config.setViewHeight(this.mainFrame.getPhotoPanel().getViewHeight());
            }

        });
        add(this.phFileButton);
        */

        // Zoom
        JLabel phZoomLabel = new JLabel(Resources.getString("photo.Zoom"));
        phZoomLabel.setIcon(Resources.getImageIcon("zoom.png"));
        phZoomLabel.setBounds(10, 10, 90, 15);
        add(phZoomLabel);

        this.phZoomButton = new JButton("");
        this.phZoomButton.setIcon(Resources.getImageIcon("cancel.png"));
        this.phZoomButton.setToolTipText("Reset zoom to 1:1");
        this.phZoomButton.setBounds(194, 10, 16, 16);
        this.phZoomButton.addActionListener(evt -> {
            this.phZoomSlider.setValue(10);
            this.mainFrame.getPhotoPanel().reloadImage();
            apply();
        });
        add(this.phZoomButton);

        this.phZoomSlider = new JSlider();
        this.phZoomSlider.setValue(10);
        this.phZoomSlider.setSnapToTicks(true);
        this.phZoomSlider.setPaintLabels(true);
        this.phZoomSlider.setMinorTickSpacing(1);
        this.phZoomSlider.setMinimum(1);
        this.phZoomSlider.setBounds(10, 35, 200, 23);
        this.phZoomSlider.addChangeListener(evt -> {
            this.mainFrame.getPhotoPanel().setZoom(this.phZoomSlider.getValue() / 10.0d);
            apply();
        });
        add(this.phZoomSlider);

        // Grid> Width
        JLabel gridWidthLabel = new JLabel(Resources.getString("grid.Width"));
        gridWidthLabel.setIcon(Resources.getImageIcon("grid.png"));
        gridWidthLabel.setBounds(10, 202, 90, 15);
        add(gridWidthLabel);

        this.gridWidthUpButton = new JButton("");
        this.gridWidthUpButton.setIcon(Resources.getImageIcon("zoom_p.png"));
        this.gridWidthUpButton.setToolTipText("Zoom in");
        this.gridWidthUpButton.setBounds(176, 202, 16, 16);
        this.gridWidthUpButton.addActionListener(evt -> {
            int value = this.gridWidthSlider.getValue() * 2;
            this.gridWidthSlider.setValue(value);
            this.mainFrame.getPhotoPanel().getSelectedGridDrawer().setWidth(value);
            apply();
        });
        add(this.gridWidthUpButton);

        this.gridWidthDownButton = new JButton("");
        this.gridWidthDownButton.setIcon(Resources.getImageIcon("zoom_m.png"));
        this.gridWidthDownButton.setToolTipText("Zoom out");
        this.gridWidthDownButton.setBounds(194, 202, 16, 16);
        this.gridWidthDownButton.addActionListener(evt -> {
            int value = this.gridWidthSlider.getValue() / 2;
            this.gridWidthSlider.setValue(value);
            this.mainFrame.getPhotoPanel().getSelectedGridDrawer().setWidth(value);
            apply();
        });
        add(this.gridWidthDownButton);

        this.gridWidthSlider = new JSlider();
        this.gridWidthSlider.setMinorTickSpacing(1);
        this.gridWidthSlider.setSnapToTicks(true);
        this.gridWidthSlider.setPaintLabels(true);
        this.gridWidthSlider.setMinimum(20);
        this.gridWidthSlider.setMaximum(320);
        this.gridWidthSlider.setBounds(10, 227, 200, 23);
        this.gridWidthSlider.addChangeListener(evt -> {
            this.mainFrame.getPhotoPanel().getSelectedGridDrawer().setWidth(this.gridWidthSlider.getValue());
            apply();
        });
        add(this.gridWidthSlider);

        // Grid> Degree
        JLabel gridDegreeLabel = new JLabel(Resources.getString("grid.Degree"));
        gridDegreeLabel.setIcon(Resources.getImageIcon("line_degree.png"));
        gridDegreeLabel.setBounds(10, 260, 90, 15);
        add(gridDegreeLabel);

        this.gridDegreeButton = new JButton("");
        this.gridDegreeButton.setIcon(Resources.getImageIcon("cancel.png"));
        this.gridDegreeButton.setToolTipText("Reset degree to zero.");
        this.gridDegreeButton.setBounds(194, 260, 16, 16);
        this.gridDegreeButton.addActionListener(evt -> {
            this.gridDegreeSlider.setValue(0);
            this.mainFrame.getPhotoPanel().getSelectedGridDrawer().setDegree(0);
            apply();
        });
        add(this.gridDegreeButton);

        this.gridDegreeSlider = new JSlider();
        this.gridDegreeSlider.setMinorTickSpacing(1);
        this.gridDegreeSlider.setSnapToTicks(true);
        this.gridDegreeSlider.setPaintLabels(true);
        this.gridDegreeSlider.setValue(0);
        this.gridDegreeSlider.setMinimum(-45);
        this.gridDegreeSlider.setMaximum(45);
        this.gridDegreeSlider.setBounds(10, 285, 200, 23);
        this.gridDegreeSlider.addChangeListener(evt -> {
            this.mainFrame.getPhotoPanel().getSelectedGridDrawer().setDegree(this.gridDegreeSlider.getValue());
            apply();
        });
        add(this.gridDegreeSlider);

        // Grid> Line Color
        JLabel colorLabel = new JLabel(Resources.getString("grid.LineColor"));
        colorLabel.setIcon(Resources.getImageIcon("line_color.png"));
        colorLabel.setBounds(10, 318, 128, 15);
        add(colorLabel);

        this.colorButton = new JButton("");
        this.colorButton.setIcon(Resources.getImageIcon("color-picker.png"));
        this.colorButton.setToolTipText("Choose grid line color");
        this.colorButton.setOpaque(true);
        this.colorButton.setBounds(10, 343, 23, 23);
        this.colorButton.addActionListener(evt -> {
            Color newColor = JColorChooser.showDialog(
                    ControlPanel.this,
                    "Choose grid line color",
                    this.mainFrame.getPhotoPanel().getSelectedGridDrawer().getLineColor());
            if (newColor != null) {
                this.colorButton.setBackground(newColor);
                this.mainFrame.getPhotoPanel().getSelectedGridDrawer().setLineColor(newColor);;
                apply();
            }
        });
        add(this.colorButton);

        // Drag
        JLabel dragModeLabel = new JLabel(Resources.getString("drag.Name"));
        dragModeLabel.setIcon(Resources.getImageIcon("drag.png"));
        dragModeLabel.setBounds(10, 87, 90, 15);
        add(dragModeLabel);

        this.dragPhotoButton = new JRadioButton(Resources.getString("drag.Photo"));
        this.dragPhotoButton.setSelected(true);
        this.dragPhotoButton.setEnabled(false);
        this.dragPhotoButton.setBounds(10, 108, 90, 23);
        this.dragPhotoButton.addActionListener(evt -> dragModeChanged());
        add(this.dragPhotoButton);

        this.dragGrid1Button = new JRadioButton(Resources.getString("drag.Grid1"));
        this.dragGrid1Button.setBounds(10, 133, 90, 23);
        this.dragGrid1Button.addActionListener(evt -> dragModeChanged());
        add(this.dragGrid1Button);

        this.dragGrid2Button = new JRadioButton(Resources.getString("drag.Grid2"));
        this.dragGrid2Button.setEnabled(false);
        this.dragGrid2Button.setBounds(114, 133, 90, 23);
        this.dragGrid2Button.addActionListener(evt -> dragModeChanged());
        add(this.dragGrid2Button);

        ButtonGroup bg = new ButtonGroup();
        bg.add(this.dragPhotoButton);
        bg.add(this.dragGrid1Button);
        bg.add(this.dragGrid2Button);

        // Visible
        JLabel visibleLabel = new JLabel(Resources.getString("grid.Visible"));
        visibleLabel.setIcon(Resources.getImageIcon("visible.png"));
        visibleLabel.setBounds(10, 388, 90, 15);
        add(visibleLabel);

        this.visible1Button = new JToggleButton(Resources.getString("grid.First"));
        this.visible1Button.setSelected(true);
        this.visible1Button.setIcon(Resources.getImageIcon("p1.png"));
        this.visible1Button.setBounds(10, 413, 96, 23);
        this.visible1Button.addActionListener(evt -> {
            this.mainFrame.getPhotoPanel().getFirstGridDrawer().setEnabled(this.visible1Button.isSelected());
            apply();
        });
        add(this.visible1Button);

        this.visible2Button = new JToggleButton(Resources.getString("grid.Second"));
        this.visible2Button.setIcon(Resources.getImageIcon("p2.png"));
        this.visible2Button.setBounds(114, 413, 96, 23);
        this.visible2Button.addActionListener(evt -> {
            this.mainFrame.getPhotoPanel().getSecondGridDrawer().setEnabled(this.visible2Button.isSelected());
            apply();
        });
        add(this.visible2Button);

        setEnabled(false);
    }

    /**
     * Get photo panel.
     * @return Photo panel.
     */
    public SketchBoardFrame getMainFrame() {
        return this.mainFrame;
    }

    /**
     * Set photo panel.
     * @param photoPanel Photo panel.
     */
    public void setMainFrame(SketchBoardFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        //this.phFileButton.setEnabled(enabled);
        this.phZoomSlider.setEnabled(enabled);
        this.phZoomButton.setEnabled(enabled);

        this.dragPhotoButton.setEnabled(enabled);
        this.dragGrid1Button.setEnabled(enabled);
        this.dragGrid2Button.setEnabled(enabled);

        this.gridDegreeSlider.setEnabled(enabled);
        this.gridDegreeButton.setEnabled(enabled);

        this.gridWidthSlider.setEnabled(enabled);
        this.gridWidthUpButton.setEnabled(enabled);
        this.gridWidthDownButton.setEnabled(enabled);

        this.colorButton.setEnabled(enabled);

        this.visible1Button.setEnabled(enabled);
        this.visible2Button.setEnabled(enabled);

        this.allowApply = true;

        this.dragPhotoButton.setSelected(true);
        enableGridFocus(false);
    }

    public void reset() {
        this.dragPhotoButton.setSelected(true);
        this.visible1Button.setSelected(true);
        this.visible2Button.setSelected(false);
        this.phZoomSlider.setValue(10);
        this.gridDegreeSlider.setValue(0);

        load(null);
        enableGridFocus(false);
    }

    public void loadAsync(final PhotoFile pf) {
        new Thread(() -> load(pf)).start();
    }

    /**
     * Load photo file.
     * @param pf Photo file.
     */
    public synchronized void load(PhotoFile pf) {
        this.pf = pf;
        if (this.pf == null) {
            this.mainFrame.getPhotoPanel().reset();
            setEnabled(false);
            return;
        }
        setEnabled(true);

        PhotoType photo = pf.config;

        this.allowApply = false;

        this.mainFrame.getPhotoPanel().loadImage(pf);
        this.phZoomSlider.setValue((int) (10 * photo.getZoom()));

        GridType g1 = photo.getGrids().getGrid().get(0);
        GridDrawer gd1 = this.mainFrame.getPhotoPanel().getFirstGridDrawer();
        applyTo(gd1, g1);
        this.visible1Button.setSelected(g1.isEnabled());

        GridType g2 = photo.getGrids().getGrid().get(1);
        GridDrawer gd2 = this.mainFrame.getPhotoPanel().getSecondGridDrawer();
        applyTo(gd2, g2);
        this.visible2Button.setSelected(g2.isEnabled());

        if ("GRID1".equalsIgnoreCase(photo.getDragTarget())) {
            this.dragGrid1Button.setSelected(true);
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.GRID1);
            this.mainFrame.getPhotoPanel().selectFirstGridDrawer();
        }
        else if ("GRID2".equalsIgnoreCase(photo.getDragTarget())) {
            this.dragGrid2Button.setSelected(true);
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.GRID2);
            this.mainFrame.getPhotoPanel().selectSecondGridDrawer();
        }
        else {
            this.dragPhotoButton.setSelected(true);
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.PHOTO);
        }

        applyFrom(this.mainFrame.getPhotoPanel().getSelectedGridDrawer());

        enableGridFocus(!this.dragPhotoButton.isSelected());

        this.allowApply = true;

        this.mainFrame.getPhotoPanel().repaint();

    }

    private void applyFrom(GridDrawer drawer) {
        this.gridWidthSlider.setValue(drawer.getWidth());
        this.gridDegreeSlider.setValue(drawer.getDegree());
        this.colorButton.setBackground(drawer.getLineColor());
    }

    private void apply() {
        if (this.pf == null) {
            return;
        }

        if (this.allowApply) {
            this.pf.config.setZoom(this.phZoomSlider.getValue() / 10d);
            this.pf.config.setViewWidth(this.mainFrame.getPhotoPanel().getViewWidth());
            this.pf.config.setViewHeight(this.mainFrame.getPhotoPanel().getViewHeight());
            if (this.dragGrid1Button.isSelected()) {
                this.pf.config.setDragTarget("GRID1");
            }
            else if (this.dragGrid2Button.isSelected()) {
                this.pf.config.setDragTarget("GRID2");
            }
            else {
                this.pf.config.setDragTarget("PHOTO");
            }

            GridType g1 = this.pf.config.getGrids().getGrid().get(0);
            GridDrawer drawer1 = this.mainFrame.getPhotoPanel().getFirstGridDrawer();
            applyTo(g1, drawer1);

            GridType g2 = this.pf.config.getGrids().getGrid().get(1);
            GridDrawer drawer2 = this.mainFrame.getPhotoPanel().getSecondGridDrawer();
            applyTo(g2, drawer2);
        }
    }

    private void applyTo(GridDrawer drawer, GridType grid) {
        drawer.setDegree(grid.getDegree());
        drawer.setEnabled(grid.isEnabled());
        drawer.setWidth(grid.getWidth());
        drawer.setOffset(SketchBookTypeHelper.toPoint(grid.getOffset()));
        drawer.setLineColor(SketchBookTypeHelper.toColor(grid.getLineColor()));
    }

    private void applyTo(GridType grid, GridDrawer drawer) {
        grid.setDegree(drawer.getDegree());
        grid.setWidth(drawer.getWidth());
        grid.setLineColor(SketchBookTypeHelper.toColorString(drawer.getLineColor()));
        grid.setOffset(SketchBookTypeHelper.toPointString(drawer.getOffset()));
        grid.setEnabled(drawer.isEnabled());
    }

    private void dragModeChanged() {
        enableGridFocus(!this.dragPhotoButton.isSelected());
        if (this.dragGrid1Button.isSelected()) {
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.GRID1);
            this.mainFrame.getPhotoPanel().selectFirstGridDrawer();
            this.mainFrame.getPhotoPanel().getFirstGridDrawer().setEnabled(true);
            this.visible1Button.setSelected(true);
            applyFrom(this.mainFrame.getPhotoPanel().getFirstGridDrawer());
        }
        else if (this.dragGrid2Button.isSelected()) {
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.GRID2);
            this.mainFrame.getPhotoPanel().selectSecondGridDrawer();
            this.mainFrame.getPhotoPanel().getSecondGridDrawer().setEnabled(true);
            this.visible2Button.setSelected(true);
            applyFrom(this.mainFrame.getPhotoPanel().getSecondGridDrawer());
        }
        else {
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.PHOTO);
        }
        apply();
    }

    private void enableGridFocus(boolean enabled) {
        this.gridWidthUpButton.setEnabled(enabled);
        this.gridWidthDownButton.setEnabled(enabled);
        this.gridWidthSlider.setEnabled(enabled);

        this.gridDegreeSlider.setEnabled(enabled);
        this.gridDegreeButton.setEnabled(enabled);

        this.colorButton.setEnabled(enabled);
    }

    @SuppressWarnings("unused")
    private void println(PhotoType p) {
        System.out.println(String.format("%-20s, zoom:%-4s(%-4s,%-4s), offset:%-7s, %s, drag:%s",
                p.getName(),
                p.getZoom(),
                p.getViewWidth(),
                p.getViewHeight(),
                p.getOffset(),
                p.getPath(),
                p.getDragTarget()));
        p.getGrids().getGrid().stream().forEach(g -> {
            System.out.println(String.format("  degree:%-3s, color:%-15s, offset:%-7s, width:%-3s, enabled:%s",
                    g.getDegree(),
                    g.getLineColor(),
                    g.getOffset(),
                    g.getWidth(),
                    g.isEnabled()));
        });
    }
}
