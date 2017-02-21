package uia.sketch;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class StatusPanel extends JPanel {

    private static final long serialVersionUID = -5230333084025698687L;

    private JLabel fileNameLabel;

    public StatusPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new EmptyBorder(4, 6, 4, 4));
        setBackground(UIManager.getColor("Button.background"));

        this.fileNameLabel = new JLabel("No file is selected");
        add(this.fileNameLabel);
    }

    public void setFileName(String fileName) {
        this.fileNameLabel.setText(fileName);
    }
}
