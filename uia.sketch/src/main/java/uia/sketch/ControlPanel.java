package uia.sketch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

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

    private JRadioButton drag5Button;

    private ButtonGroup dragGroup;

    private JToggleButton visible1Button;

    private JToggleButton visible2Button;

    private JToggleButton visible3Button;

    private JToggleButton visible4Button;

    private JToggleButton visible5Button;

    private boolean allowApply;

    /**
     * Constructor.
     */
    public ControlPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(222, 652));

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

        // Drag
        JLabel dragModeLabel = new JLabel(Resources.getString("drag.Name"));
        dragModeLabel.setIcon(Resources.getImageIcon("drag.png"));
        dragModeLabel.setBounds(10, 78, 90, 15);
        add(dragModeLabel);

        this.dragButton = new JButton("");
        this.dragButton.setToolTipText("Reset center point to (0,0).");
        this.dragButton.setIcon(Resources.getImageIcon("cancel.png"));
        this.dragButton.setEnabled(false);
        this.dragButton.setBounds(194, 78, 16, 16);
        this.dragButton.addActionListener(evt -> {
            LayerDrawer layerDrawer = this.mainFrame.getPhotoPanel().getSelectedLayerDrawer();
            layerDrawer.setOffset(new Point(layerDrawer.getWidth() + 0, layerDrawer.getWidth() + 0));
            apply();
        });
        add(this.dragButton);

        this.dragPhotoButton = new JRadioButton(Resources.getString("drag.Photo"));
        this.dragPhotoButton.setActionCommand("PHOTO");
        this.dragPhotoButton.setSelected(true);
        this.dragPhotoButton.setEnabled(false);
        this.dragPhotoButton.setBounds(10, 99, 145, 25);
        this.dragPhotoButton.addActionListener(evt -> dragModeChanged());
        add(this.dragPhotoButton);

        this.drag1Button = new JRadioButton(Resources.getString("drag.Grid1"));
        this.drag1Button.setActionCommand("GRID1");
        this.drag1Button.setEnabled(false);
        this.drag1Button.setBounds(10, 124, 145, 25);
        this.drag1Button.addActionListener(evt -> dragModeChanged());
        add(this.drag1Button);

        this.drag2Button = new JRadioButton(Resources.getString("drag.Grid2"));
        this.drag2Button.setActionCommand("GRID2");
        this.drag2Button.setEnabled(false);
        this.drag2Button.setBounds(10, 151, 145, 25);
        this.drag2Button.addActionListener(evt -> dragModeChanged());
        add(this.drag2Button);

        this.drag3Button = new JRadioButton(Resources.getString("drag.Circle"));
        this.drag3Button.setActionCommand("CIRCLE");
        this.drag3Button.setEnabled(false);
        this.drag3Button.setBounds(10, 176, 145, 25);
        this.drag3Button.addActionListener(evt -> dragModeChanged());
        add(this.drag3Button);

        this.drag4Button = new JRadioButton(Resources.getString("drag.Triangle"));
        this.drag4Button.setActionCommand("TRIANGLE");
        this.drag4Button.setEnabled(false);
        this.drag4Button.setBounds(10, 201, 145, 25);
        this.drag4Button.addActionListener(evt -> dragModeChanged());
        add(this.drag4Button);

        this.drag5Button = new JRadioButton(Resources.getString("drag.Perspective"));
        this.drag5Button.setActionCommand("PERSPECTIVE");
        this.drag5Button.setEnabled(false);
        this.drag5Button.setBounds(10, 226, 145, 23);
        this.drag5Button.addActionListener(evt -> dragModeChanged());
        add(this.drag5Button);

        this.dragGroup = new ButtonGroup();
        this.dragGroup.add(this.dragPhotoButton);
        this.dragGroup.add(this.drag1Button);
        this.dragGroup.add(this.drag2Button);
        this.dragGroup.add(this.drag3Button);
        this.dragGroup.add(this.drag4Button);
        this.dragGroup.add(this.drag5Button);

        // Grid> Width
        JLabel gridWidthLabel = new JLabel(Resources.getString("grid.Width"));
        gridWidthLabel.setIcon(Resources.getImageIcon("grid.png"));
        gridWidthLabel.setBounds(10, 269, 90, 15);
        add(gridWidthLabel);

        this.gridWidthUpButton = new JButton("");
        this.gridWidthUpButton.setIcon(Resources.getImageIcon("zoom_p.png"));
        this.gridWidthUpButton.setToolTipText("Zoom in");
        this.gridWidthUpButton.setBounds(176, 269, 16, 16);
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
        this.gridWidthDownButton.setBounds(194, 269, 16, 16);
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
        this.gridWidthSlider.setBounds(10, 294, 200, 23);
        this.gridWidthSlider.addChangeListener(evt -> {
            this.mainFrame.getPhotoPanel().getSelectedLayerDrawer().setWidth(this.gridWidthSlider.getValue());
            apply();
        });
        add(this.gridWidthSlider);

        // Grid> Degree
        JLabel gridDegreeLabel = new JLabel(Resources.getString("grid.Degree"));
        gridDegreeLabel.setIcon(Resources.getImageIcon("line_degree.png"));
        gridDegreeLabel.setBounds(10, 336, 90, 15);
        add(gridDegreeLabel);

        this.gridDegreeButton = new JButton("");
        this.gridDegreeButton.setIcon(Resources.getImageIcon("cancel.png"));
        this.gridDegreeButton.setToolTipText("Reset degree to zero.");
        this.gridDegreeButton.setBounds(194, 336, 16, 16);
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
        this.gridDegreeSlider.setBounds(10, 361, 200, 23);
        this.gridDegreeSlider.addChangeListener(evt -> {
            this.mainFrame.getPhotoPanel().getSelectedLayerDrawer().setDegree(this.gridDegreeSlider.getValue());
            apply();
        });
        add(this.gridDegreeSlider);

        // Grid> Line Color
        JLabel colorLabel = new JLabel(Resources.getString("grid.LineColor"));
        colorLabel.setIcon(Resources.getImageIcon("line_color.png"));
        colorLabel.setBounds(10, 394, 128, 15);
        add(colorLabel);

        this.colorButton = new JButton("");
        this.colorButton.setIcon(Resources.getImageIcon("color-picker.png"));
        this.colorButton.setToolTipText("Choose grid line color");
        this.colorButton.setOpaque(true);
        this.colorButton.setBounds(10, 419, 23, 23);
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

        this.visible1Button = new JToggleButton();
        this.visible1Button.setBounds(185, 124, 25, 25);
        this.visible1Button.setIcon(Resources.getImageIcon("layer_off.png"));
        this.visible1Button.setSelectedIcon(Resources.getImageIcon("p1.png"));
        this.visible1Button.addActionListener(evt -> {
            this.mainFrame.getPhotoPanel().getLayerDrawer("GRID1").setEnabled(this.visible1Button.isSelected());
            apply();
        });
        add(this.visible1Button);

        this.visible2Button = new JToggleButton();
        this.visible2Button.setBounds(185, 149, 25, 25);
        this.visible2Button.setIcon(Resources.getImageIcon("layer_off.png"));
        this.visible2Button.setSelectedIcon(Resources.getImageIcon("p2.png"));
        this.visible2Button.addActionListener(evt -> {
            this.mainFrame.getPhotoPanel().getLayerDrawer("GRID2").setEnabled(this.visible2Button.isSelected());
            apply();
        });
        add(this.visible2Button);

        this.visible3Button = new JToggleButton();
        this.visible3Button.setBounds(185, 174, 25, 25);
        this.visible3Button.setIcon(Resources.getImageIcon("layer_off.png"));
        this.visible3Button.setSelectedIcon(Resources.getImageIcon("p3.png"));
        this.visible3Button.addActionListener(evt -> {
            this.mainFrame.getPhotoPanel().getLayerDrawer("CIRCLE").setEnabled(this.visible3Button.isSelected());
            apply();
        });
        add(this.visible3Button);

        this.visible4Button = new JToggleButton();
        this.visible4Button.setBounds(185, 199, 25, 25);
        this.visible4Button.setIcon(Resources.getImageIcon("layer_off.png"));
        this.visible4Button.setSelectedIcon(Resources.getImageIcon("p4.png"));
        this.visible4Button.addActionListener(evt -> {
            this.mainFrame.getPhotoPanel().getLayerDrawer("TRIANGLE").setEnabled(this.visible4Button.isSelected());
            apply();
        });
        add(this.visible4Button);

        this.visible5Button = new JToggleButton();
        this.visible5Button.setBounds(185, 224, 25, 25);
        this.visible5Button.setIcon(Resources.getImageIcon("layer_off.png"));
        this.visible5Button.setSelectedIcon(Resources.getImageIcon("layer_on.png"));
        this.visible5Button.addActionListener(evt -> {
            this.mainFrame.getPhotoPanel().getLayerDrawer("PERSPECTIVE").setEnabled(this.visible5Button.isSelected());
            apply();
        });
        add(this.visible5Button);

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
        Enumeration<AbstractButton> bs = this.dragGroup.getElements();
        while (bs.hasMoreElements()) {
            bs.nextElement().setEnabled(enabled);
        }

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
        this.visible5Button.setEnabled(enabled);

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
        this.visible5Button.setSelected(false);
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
                this.visible4Button,
                this.visible5Button
        };

        for (int i = 0; i < lts.size(); i++) {
            LayerType lt = lts.get(i);
            LayerDrawer drawer = this.mainFrame.getPhotoPanel().getLayerDrawer(lt.getName());
            if (drawer != null) {
                drawer.apply(lt);
                visibleButtons[i].setSelected(drawer.isEnabled());
            }
        }

        String dragTarget = photo.getDragTarget().toUpperCase();
        if ("GRID1".equals(dragTarget)) {
            this.drag1Button.setSelected(true);
        }
        else if ("GRID2".equals(dragTarget)) {
            this.drag2Button.setSelected(true);
        }
        else if ("CIRCLE".equals(dragTarget)) {
            this.drag3Button.setSelected(true);
        }
        else if ("TRIANGLE".equals(dragTarget)) {
            this.drag4Button.setSelected(true);
        }
        else if ("PERSPECTIVE".equals(dragTarget)) {
            this.drag5Button.setSelected(true);
        }
        else {
            this.dragPhotoButton.setSelected(true);
        }
        this.mainFrame.getPhotoPanel().setDragMode(dragTarget);
        this.mainFrame.getPhotoPanel().selectLayerDrawer(dragTarget);

        applyFrom(this.mainFrame.getPhotoPanel().getSelectedLayerDrawer());

        enableGridFocus(!this.dragPhotoButton.isSelected());

        this.allowApply = true;

        this.mainFrame.getPhotoPanel().repaint();

    }

    private void applyFrom(LayerDrawer drawer) {
        if (drawer != null) {
            this.gridWidthSlider.setValue(drawer.getWidth());
            this.gridDegreeSlider.setValue(drawer.getDegree());
            this.colorButton.setBackground(drawer.getLineColor());
        }
    }

    private void apply() {
        if (this.pf == null) {
            return;
        }

        if (this.allowApply) {
            this.pf.config.setZoom(this.phZoomSlider.getValue() / 10d);
            this.pf.config.setViewWidth(this.mainFrame.getPhotoPanel().getViewWidth());
            this.pf.config.setViewHeight(this.mainFrame.getPhotoPanel().getViewHeight());
            this.pf.config.setDragTarget(this.dragGroup.getSelection().getActionCommand());

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

        String dragTarget = this.dragGroup.getSelection().getActionCommand();
        this.mainFrame.getPhotoPanel().setDragMode(dragTarget);
        this.mainFrame.getPhotoPanel().selectLayerDrawer(dragTarget);
        applyFrom(this.mainFrame.getPhotoPanel().getLayerDrawer(dragTarget));

        if (this.drag1Button.isSelected()) {
            this.visible1Button.setSelected(true);
        }
        else if (this.drag2Button.isSelected()) {
            this.visible2Button.setSelected(true);
        }
        else if (this.drag3Button.isSelected()) {
            this.visible3Button.setSelected(true);
        }
        else if (this.drag4Button.isSelected()) {
            this.visible4Button.setSelected(true);
        }
        else if (this.drag5Button.isSelected()) {
            this.visible5Button.setSelected(true);
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
