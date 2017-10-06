package uia.sketch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

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
import uia.sketch.model.xml.LayerType;
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

    private PhotoFile pf;

    private JSlider phZoomSlider;

    private JButton phZoomButton;

    private JSlider gridWidthSlider;

    private JButton gridWidthUpButton;

    private JButton gridWidthDownButton;

    private JSlider gridDegreeSlider;

    private JButton gridDegreeButton;

    private JButton colorButton;

    private JButton dragButton;

    private JRadioButton dragPhotoButton;

    private JRadioButton drag1Button;

    private JRadioButton drag2Button;

    private JRadioButton drag3Button;

    private JRadioButton drag4Button;

    private JToggleButton visible1Button;

    private JToggleButton visible2Button;

    private JToggleButton visible3Button;

    private JToggleButton visible4Button;

    private boolean allowApply;

    /**
     * Constructor.
     */
    public ControlPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(222, 565));

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
            this.mainFrame.getPhotoPanel().getSelectedLayerDrawer().setWidth(value);
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
            this.mainFrame.getPhotoPanel().getSelectedLayerDrawer().setWidth(value);
            apply();
        });
        add(this.gridWidthDownButton);

        this.gridWidthSlider = new JSlider();
        this.gridWidthSlider.setMinorTickSpacing(1);
        this.gridWidthSlider.setSnapToTicks(true);
        this.gridWidthSlider.setPaintLabels(true);
        this.gridWidthSlider.setMinimum(20);
        this.gridWidthSlider.setMaximum(420);
        this.gridWidthSlider.setBounds(10, 227, 200, 23);
        this.gridWidthSlider.addChangeListener(evt -> {
            this.mainFrame.getPhotoPanel().getSelectedLayerDrawer().setWidth(this.gridWidthSlider.getValue());
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
            this.mainFrame.getPhotoPanel().getSelectedLayerDrawer().setDegree(0);
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
            this.mainFrame.getPhotoPanel().getSelectedLayerDrawer().setDegree(this.gridDegreeSlider.getValue());
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
                    this.mainFrame.getPhotoPanel().getSelectedLayerDrawer().getLineColor());
            if (newColor != null) {
                this.colorButton.setBackground(newColor);
                this.mainFrame.getPhotoPanel().getSelectedLayerDrawer().setLineColor(newColor);
                apply();
            }
        });
        add(this.colorButton);

        // Drag
        JLabel dragModeLabel = new JLabel(Resources.getString("drag.Name"));
        dragModeLabel.setIcon(Resources.getImageIcon("drag.png"));
        dragModeLabel.setBounds(10, 82, 90, 15);
        add(dragModeLabel);

        this.dragButton = new JButton("");
        this.dragButton.setToolTipText("Reset center point to (0,0).");
        this.dragButton.setIcon(Resources.getImageIcon("cancel.png"));
        this.dragButton.setEnabled(false);
        this.dragButton.setBounds(194, 82, 16, 16);
        this.dragButton.addActionListener(evt -> {
            LayerDrawer layerDrawer = this.mainFrame.getPhotoPanel().getSelectedLayerDrawer();
            layerDrawer.setOffset(new Point(layerDrawer.getWidth() + 0, layerDrawer.getWidth() + 0));
            apply();
        });
        add(this.dragButton);

        this.dragPhotoButton = new JRadioButton(Resources.getString("drag.Photo"));
        this.dragPhotoButton.setSelected(true);
        this.dragPhotoButton.setEnabled(false);
        this.dragPhotoButton.setBounds(10, 103, 90, 23);
        this.dragPhotoButton.addActionListener(evt -> dragModeChanged());
        add(this.dragPhotoButton);

        this.drag1Button = new JRadioButton(Resources.getString("drag.Grid1"));
        this.drag1Button.setEnabled(false);
        this.drag1Button.setBounds(10, 128, 90, 23);
        this.drag1Button.addActionListener(evt -> dragModeChanged());
        add(this.drag1Button);

        this.drag2Button = new JRadioButton(Resources.getString("drag.Grid2"));
        this.drag2Button.setEnabled(false);
        this.drag2Button.setBounds(114, 128, 90, 23);
        this.drag2Button.addActionListener(evt -> dragModeChanged());
        add(this.drag2Button);

        this.drag3Button = new JRadioButton(Resources.getString("drag.Circle"));
        this.drag3Button.setEnabled(false);
        this.drag3Button.setBounds(10, 153, 90, 23);
        this.drag3Button.addActionListener(evt -> dragModeChanged());
        add(this.drag3Button);

        this.drag4Button = new JRadioButton(Resources.getString("drag.Triangle"));
        this.drag4Button.setEnabled(false);
        this.drag4Button.setBounds(114, 153, 90, 23);
        this.drag4Button.addActionListener(evt -> dragModeChanged());
        add(this.drag4Button);

        ButtonGroup bg = new ButtonGroup();
        bg.add(this.dragPhotoButton);
        bg.add(this.drag1Button);
        bg.add(this.drag2Button);
        bg.add(this.drag3Button);
        bg.add(this.drag4Button);

        // Visible
        JLabel visibleLabel = new JLabel(Resources.getString("grid.Visible"));
        visibleLabel.setIcon(Resources.getImageIcon("visible.png"));
        visibleLabel.setBounds(10, 388, 90, 15);
        add(visibleLabel);

        this.visible1Button = new JToggleButton(Resources.getString("grid.First"));
        this.visible1Button.setIcon(Resources.getImageIcon("p1.png"));
        this.visible1Button.setBounds(10, 413, 96, 23);
        this.visible1Button.addActionListener(evt -> {
            this.mainFrame.getPhotoPanel().getLayerDrawer("GRID1").setEnabled(this.visible1Button.isSelected());
            apply();
        });
        add(this.visible1Button);

        this.visible2Button = new JToggleButton(Resources.getString("grid.Second"));
        this.visible2Button.setIcon(Resources.getImageIcon("p2.png"));
        this.visible2Button.setBounds(114, 413, 96, 23);
        this.visible2Button.addActionListener(evt -> {
            this.mainFrame.getPhotoPanel().getLayerDrawer("GRID2").setEnabled(this.visible2Button.isSelected());
            apply();
        });
        add(this.visible2Button);

        this.visible3Button = new JToggleButton(Resources.getString("grid.Circle"));
        this.visible3Button.setIcon(Resources.getImageIcon("p3.png"));
        this.visible3Button.setBounds(10, 444, 96, 23);
        this.visible3Button.addActionListener(evt -> {
            this.mainFrame.getPhotoPanel().getLayerDrawer("CIRCLE").setEnabled(this.visible3Button.isSelected());
            apply();
        });
        add(this.visible3Button);

        this.visible4Button = new JToggleButton(Resources.getString("grid.Triangle"));
        this.visible4Button.setIcon(Resources.getImageIcon("p4.png"));
        this.visible4Button.setBounds(114, 444, 96, 23);
        this.visible4Button.addActionListener(evt -> {
            this.mainFrame.getPhotoPanel().getLayerDrawer("TRIANGLE").setEnabled(this.visible4Button.isSelected());
            apply();
        });
        add(this.visible4Button);

        setEnabled(false);
    }

    /**
     * Get application frame.
     * @return Application frame.
     */
    public SketchBoardFrame getMainFrame() {
        return this.mainFrame;
    }

    /**
     * Set application frame.
     * @param mainFrame Application frame.
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

        this.dragButton.setEnabled(enabled);
        this.dragPhotoButton.setEnabled(enabled);
        this.drag1Button.setEnabled(enabled);
        this.drag2Button.setEnabled(enabled);
        this.drag3Button.setEnabled(enabled);
        this.drag4Button.setEnabled(enabled);

        this.gridDegreeSlider.setEnabled(enabled);
        this.gridDegreeButton.setEnabled(enabled);

        this.gridWidthSlider.setEnabled(enabled);
        this.gridWidthUpButton.setEnabled(enabled);
        this.gridWidthDownButton.setEnabled(enabled);

        this.colorButton.setEnabled(enabled);

        this.visible1Button.setEnabled(enabled);
        this.visible2Button.setEnabled(enabled);
        this.visible3Button.setEnabled(enabled);
        this.visible4Button.setEnabled(enabled);

        this.allowApply = true;

        this.dragPhotoButton.setSelected(true);
        enableGridFocus(false);
    }

    /**
     * Reset
     */
    public void reset() {
        this.dragPhotoButton.setSelected(true);
        this.visible1Button.setSelected(true);
        this.visible2Button.setSelected(false);
        this.visible3Button.setSelected(false);
        this.visible4Button.setSelected(false);
        this.phZoomSlider.setValue(10);
        this.gridDegreeSlider.setValue(0);

        load(null);
        enableGridFocus(false);
    }

    /**
     * Load a photo.
     * @param pf Photo file.
     */
    public void loadAsync(final PhotoFile pf) {
        new Thread(() -> load(pf)).start();
    }

    /**
     * Load a photo.
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

        List<LayerType> lts = photo.getLayers().getLayer();
        JToggleButton[] visibleButtons = new JToggleButton[] {
                this.visible1Button,
                this.visible2Button,
                this.visible3Button,
                this.visible4Button
        };

        for (int i = 0; i < lts.size(); i++) {
            LayerType lt = lts.get(i);
            LayerDrawer drawer = this.mainFrame.getPhotoPanel().getLayerDrawer(lt.getName());
            drawer.apply(lt);
            visibleButtons[i].setSelected(drawer.isEnabled());
        }

        if ("GRID1".equalsIgnoreCase(photo.getDragTarget())) {
            this.drag1Button.setSelected(true);
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.GRID1);
            this.mainFrame.getPhotoPanel().selectLayerDrawer("GRID1");
        }
        else if ("GRID2".equalsIgnoreCase(photo.getDragTarget())) {
            this.drag2Button.setSelected(true);
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.GRID2);
            this.mainFrame.getPhotoPanel().selectLayerDrawer("GRID2");
        }
        else if ("CIRCLE".equalsIgnoreCase(photo.getDragTarget())) {
            this.drag3Button.setSelected(true);
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.CIRCLE);
            this.mainFrame.getPhotoPanel().selectLayerDrawer("CIRCLE");
        }
        else if ("TRIANGLE".equalsIgnoreCase(photo.getDragTarget())) {
            this.drag4Button.setSelected(true);
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.TRIANGLE);
            this.mainFrame.getPhotoPanel().selectLayerDrawer("TRIANGLE");
        }
        else {
            this.dragPhotoButton.setSelected(true);
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.PHOTO);
        }

        applyFrom(this.mainFrame.getPhotoPanel().getSelectedLayerDrawer());

        enableGridFocus(!this.dragPhotoButton.isSelected());

        this.allowApply = true;

        this.mainFrame.getPhotoPanel().repaint();

    }

    private void applyFrom(LayerDrawer drawer) {
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
            if (this.drag1Button.isSelected()) {
                this.pf.config.setDragTarget("GRID1");
            }
            else if (this.drag2Button.isSelected()) {
                this.pf.config.setDragTarget("GRID2");
            }
            else if (this.drag3Button.isSelected()) {
                this.pf.config.setDragTarget("CIRCLE");
            }
            else if (this.drag4Button.isSelected()) {
                this.pf.config.setDragTarget("TRIANGLE");
            }
            else {
                this.pf.config.setDragTarget("PHOTO");
            }

            List<LayerType> lts = this.pf.config.getLayers().getLayer();
            for (int i = 0; i < lts.size(); i++) {
                LayerType lt = lts.get(i);
                LayerDrawer drawer = this.mainFrame.getPhotoPanel().getLayerDrawer(lt.getName());
                applyTo(lt, drawer);
            }
        }
    }

    private void applyTo(LayerType target, LayerDrawer source) {
        target.setName(source.getLayerName());
        target.setDegree(source.getDegree());
        target.setWidth(source.getWidth());
        target.setLineColor(SketchBookTypeHelper.toColorString(source.getLineColor()));
        target.setOffset(SketchBookTypeHelper.toPointString(source.getOffset()));
        target.setEnabled(source.isEnabled());
    }

    private void dragModeChanged() {
        enableGridFocus(!this.dragPhotoButton.isSelected());
        if (this.drag1Button.isSelected()) {
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.GRID1);
            this.mainFrame.getPhotoPanel().selectLayerDrawer("GRID1");
            this.mainFrame.getPhotoPanel().getLayerDrawer("GRID1").setEnabled(true);
            this.visible1Button.setSelected(true);
            applyFrom(this.mainFrame.getPhotoPanel().getLayerDrawer("GRID1"));
        }
        else if (this.drag2Button.isSelected()) {
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.GRID2);
            this.mainFrame.getPhotoPanel().selectLayerDrawer("GRID2");
            this.mainFrame.getPhotoPanel().getLayerDrawer("GRID2").setEnabled(true);
            this.visible2Button.setSelected(true);
            applyFrom(this.mainFrame.getPhotoPanel().getLayerDrawer("GRID2"));
        }
        else if (this.drag3Button.isSelected()) {
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.CIRCLE);
            this.mainFrame.getPhotoPanel().selectLayerDrawer("CIRCLE");
            this.mainFrame.getPhotoPanel().getLayerDrawer("CIRCLE").setEnabled(true);
            this.visible3Button.setSelected(true);
            applyFrom(this.mainFrame.getPhotoPanel().getLayerDrawer("CIRCLE"));
        }
        else if (this.drag4Button.isSelected()) {
            this.mainFrame.getPhotoPanel().setDragMode(DragTargetType.TRIANGLE);
            this.mainFrame.getPhotoPanel().selectLayerDrawer("TRIANGLE");
            this.mainFrame.getPhotoPanel().getLayerDrawer("TRIANGLE").setEnabled(true);
            this.visible4Button.setSelected(true);
            applyFrom(this.mainFrame.getPhotoPanel().getLayerDrawer("TRIANGLE"));
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
        p.getLayers().getLayer().stream().forEach(g -> {
            System.out.println(String.format("  degree:%-3s, color:%-15s, offset:%-7s, width:%-3s, enabled:%s",
                    g.getDegree(),
                    g.getLineColor(),
                    g.getOffset(),
                    g.getWidth(),
                    g.isEnabled()));
        });
    }
}
