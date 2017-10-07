package uia.sketch;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import uia.sketch.model.SketchBookTypeHelper;
import uia.sketch.model.xml.PhotoType;
import uia.sketch.model.xml.SketchBookType;

public class FileNaviPanel extends JPanel {

    private static final long serialVersionUID = -8661701906134024400L;

    private File lastFile;

    private JList<PhotoFile> fileList;

    private PhotoFile selectedFile;

    private DefaultListModel<PhotoFile> photoModel;

    private SketchBoardFrame mainFrame;

    public FileNaviPanel() {
        setPreferredSize(new Dimension(200, 300));
        setLayout(new BorderLayout(0, 0));

        this.photoModel = new DefaultListModel<PhotoFile>();
        this.fileList = new JList<PhotoFile>();
        this.fileList.setBackground(UIManager.getColor("Button.background"));
        this.fileList.setModel(this.photoModel);
        this.fileList.addListSelectionListener(evt -> {
            this.selectedFile = this.fileList.getSelectedValue();

            if (this.selectedFile == null || this.selectedFile.config == null) {
                this.mainFrame.getStatusPanel().setFileName("");
            }
            else {
                this.mainFrame.getStatusPanel().setFileName(this.selectedFile.config.getPath());
            }

            this.mainFrame.getControlPanel().loadAsync(this.selectedFile);
        });
        this.fileList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
                    changeName();
                }
            }
        });
        add(this.fileList, BorderLayout.CENTER);
    }

    public SketchBoardFrame getMainFrame() {
        return this.mainFrame;
    }

    public void setMainFrame(SketchBoardFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void newSketchBook() {
        this.photoModel.clear();
        this.mainFrame.setTitle(Resources.TITLE + " - " + Resources.getString("text.NoName"));

    }

    public void openSketchBook(File file) {
        try {
            this.photoModel.clear();
            this.mainFrame.setTitle(Resources.TITLE + " - " + file.getAbsolutePath());

            SketchBookType book = SketchBookTypeHelper.load(file);
            // location
            this.mainFrame.setLocation(book.getX(), book.getY());
            // size
            this.mainFrame.setSize(new Dimension(book.getWidth(), book.getHeight()));
            // photo list
            book.getPhotoList().getPhoto().stream().forEach(p -> {
                PhotoFile pf = new PhotoFile(p);
                this.photoModel.addElement(pf);
            });
            if (this.photoModel.size() > 0) {
                this.fileList.setSelectedIndex(0);
            }
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, file.getAbsolutePath() + " can't be opened.", "Sketch book", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveSketchBook(File file) {
        try {
            if (!file.getName().toLowerCase().endsWith(".xml")) {
                file = new File(file.getAbsolutePath() + ".xml");
            }

            SketchBookType book = SketchBookTypeHelper.createBook();
            // window size
            book.setHeight(this.mainFrame.getSize().height);
            book.setWidth(this.mainFrame.getSize().width);
            // photo list
            for (int i = 0, n = this.photoModel.getSize(); i < n; i++) {
                PhotoType photo = this.photoModel.get(i).config;
                book.getPhotoList().getPhoto().add(photo);
            }
            SketchBookTypeHelper.save(book, file);
            this.mainFrame.setTitle(Resources.TITLE + " - " + file.getAbsolutePath());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, file.getAbsolutePath() + " can't be saved.", "Sketch book", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addPhoto() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(this.lastFile);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            this.lastFile = fc.getSelectedFile();

            PhotoType photo = SketchBookTypeHelper.createPhoto(this.lastFile);
            PhotoFile pf = new PhotoFile(photo);
            this.photoModel.addElement(pf);
            this.fileList.setSelectedValue(pf, true);
        }
    }

    public void deletePhoto() {
        this.photoModel.removeElement(this.fileList.getSelectedValue());
        if (this.photoModel.size() > 0) {
            this.fileList.setSelectedValue(this.photoModel.firstElement(), true);
        }
    }

    public void clearPhotos() {
        this.photoModel.clear();
        this.fileList.setSelectedIndex(-1);
    }

    private void changeName() {
        Object aliasName = JOptionPane.showInputDialog(
                getMainFrame(),
                "Name",
                this.selectedFile.config.getName());
        if (aliasName != null) {
            this.selectedFile.config.setName(aliasName.toString());
        }
    }
}
