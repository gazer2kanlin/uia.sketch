package uia.sketch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.FontUIResource;

/**
 *
 * @author Kyle K. Lin
 *
 */
public class SketchBoardFrame extends JFrame {

    private static final long serialVersionUID = -7449421668829108966L;

    private static final String VER = "0.0.1";

    private FileNaviPanel filePanel;

    private PhotoPanel photoPanel;

    private ControlPanel controlPanel;

    private StatusPanel statusPanel;

    private File lastFile;

    private JMenuItem newMenuItem;

    private JMenuItem openMenuItem;

    private JMenuItem saveMenuItem;

    private JMenuItem saveAsMenuItem;

    private JMenuItem addMenuItem;

    private JMenuItem deleteMenuItem;

    private JMenuItem clearMenuItem;

    private JMenuItem undoMenuItem;

    private JMenuItem redoMenuItem;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                Locale.setDefault(new Locale(args[0]));
                Resources.LOCALE = Locale.getDefault();
            }

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            String fontName = "Monospace";
            int fontSize = 12;
            if (Resources.LOCALE != Locale.TAIWAN && Resources.LOCALE != Locale.TRADITIONAL_CHINESE) {
                fontName = "Tahoma";
                FontUIResource f = new FontUIResource(new Font(fontName, Font.PLAIN, fontSize));
                Enumeration<Object> keys = UIManager.getDefaults().keys();
                while (keys.hasMoreElements()) {
                    Object key = keys.nextElement();
                    Object value = UIManager.get(key);
                    if (value instanceof FontUIResource) {
                        UIManager.put(key, f);
                    }
                }
            }
        }
        catch (Exception ex) {
        }

        Resources.TITLE = Resources.getString("app.Name");

        new SketchBoardFrame().setVisible(true);
    }

    /**
     *
     */
    public SketchBoardFrame() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(Resources.getImageURL("sketch_96.png")));
        setSize(1000, 720);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(Resources.TITLE + " - " + Resources.getString("text.NoName"));
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                windowLoaded();
            }
        });

        JSplitPane splitPane = new JSplitPane();
        getContentPane().add(splitPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        rightPanel.setLayout(new BorderLayout(0, 0));
        splitPane.setRightComponent(rightPanel);

        // Photo Panel
        this.photoPanel = new PhotoPanel();
        this.photoPanel.setBackground(UIManager.getColor("Button.light"));
        this.photoPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        rightPanel.add(this.photoPanel);

        // Control Panel
        this.controlPanel = new ControlPanel();
        rightPanel.add(this.controlPanel, BorderLayout.EAST);

        // Status Panel
        this.statusPanel = new StatusPanel();
        getContentPane().add(this.statusPanel, BorderLayout.SOUTH);

        // File Navigator Panel
        this.filePanel = new FileNaviPanel();
        this.filePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        splitPane.setLeftComponent(this.filePanel);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menu Bar> File
        JMenu fileMenu = new JMenu(Resources.getString("menu.File"));
        menuBar.add(fileMenu);

        this.newMenuItem = new JMenuItem(Resources.getString("menu.New"));
        this.newMenuItem.addActionListener(evt -> {
            this.controlPanel.reset();
            this.photoPanel.reset();
            this.filePanel.newSketchBook();
        });
        fileMenu.add(this.newMenuItem);

        this.openMenuItem = new JMenuItem(Resources.getString("menu.Open"));
        this.openMenuItem.addActionListener(evt -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getAbsolutePath().toLowerCase().endsWith(".xml");
                }

                @Override
                public String getDescription() {
                    return "Sketch book (*.xml)";
                }

            });
            fc.setCurrentDirectory(this.lastFile);
            int returnVal = fc.showOpenDialog(SketchBoardFrame.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                this.lastFile = fc.getSelectedFile();
                this.filePanel.openSketchBook(this.lastFile);
            }

        });
        fileMenu.add(this.openMenuItem);

        this.saveMenuItem = new JMenuItem(Resources.getString("menu.Save"));
        this.saveMenuItem.addActionListener(evt -> {
            if (this.lastFile == null) {
                saveAs();
            }
            else {
                this.filePanel.saveSketchBook(this.lastFile);
            }
        });
        fileMenu.add(this.saveMenuItem);

        this.saveAsMenuItem = new JMenuItem(Resources.getString("menu.SaveAs"));
        this.saveAsMenuItem.addActionListener(evt -> saveAs());
        fileMenu.add(this.saveAsMenuItem);
        fileMenu.addSeparator();

        this.addMenuItem = new JMenuItem(Resources.getString("menu.Add"));
        this.addMenuItem.addActionListener(evt -> this.filePanel.addPhoto());
        fileMenu.add(this.addMenuItem);

        this.deleteMenuItem = new JMenuItem(Resources.getString("menu.Delete"));
        this.deleteMenuItem.addActionListener(evt -> this.filePanel.deletePhoto());
        fileMenu.add(this.deleteMenuItem);

        this.clearMenuItem = new JMenuItem(Resources.getString("menu.Clear"));
        this.clearMenuItem.addActionListener(evt -> {
            this.filePanel.clearPhotos();
        });
        fileMenu.add(this.clearMenuItem);
        fileMenu.addSeparator();

        JMenuItem exitMenuItem = new JMenuItem(Resources.getString("menu.Exit"));
        exitMenuItem.addActionListener(evt -> SketchBoardFrame.this.dispose());
        fileMenu.add(exitMenuItem);

        JMenu editMenu = new JMenu(Resources.getString("menu.Edit"));
        menuBar.add(editMenu);

        this.undoMenuItem = new JMenuItem(Resources.getString("menu.Undo"));
        this.undoMenuItem.setEnabled(false);
        editMenu.add(this.undoMenuItem);

        this.redoMenuItem = new JMenuItem(Resources.getString("menu.Redo"));
        this.redoMenuItem.setEnabled(false);
        editMenu.add(this.redoMenuItem);

        // Menu Bar> Help
        JMenu helpMenu = new JMenu(Resources.getString("menu.Help"));
        menuBar.add(helpMenu);

        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Resources.getImageURL("sketch_48.png")));

        JMenuItem aboutMenuItem = new JMenuItem(Resources.getString("menu.About"));
        aboutMenuItem.addActionListener(evt -> JOptionPane.showMessageDialog(
                SketchBoardFrame.this,
                "Grids for Sketch Composition                                  \nVersion: " + VER + "\nAuthor: Kyle K. Lin",
                Resources.getString("menu.About"),
                JOptionPane.INFORMATION_MESSAGE,
                icon));
        helpMenu.add(aboutMenuItem);

    }

    public ControlPanel getControlPanel() {
        return this.controlPanel;
    }

    public StatusPanel getStatusPanel() {
        return this.statusPanel;
    }

    public PhotoPanel getPhotoPanel() {
        return this.photoPanel;
    }

    public FileNaviPanel getFileNaviPanel() {
        return this.filePanel;
    }

    private void saveAs() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(this.lastFile);
        fc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getAbsolutePath().toLowerCase().endsWith(".xml");
            }

            @Override
            public String getDescription() {
                return "Sketch book (*.xml)";
            }

        });
        int returnVal = fc.showSaveDialog(SketchBoardFrame.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            this.lastFile = fc.getSelectedFile();
            this.filePanel.saveSketchBook(this.lastFile);
        }
    }

    private void windowLoaded() {
        try {
            this.controlPanel.setMainFrame(this);
            this.filePanel.setMainFrame(this);
        }
        catch (Exception ex) {

        }
    }
}
