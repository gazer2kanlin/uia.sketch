package uia.sketch;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public final class Resources {

    public static Locale LOCALE = Locale.TAIWAN;

    public static String TITLE = "";

    public static String getString(String key) {
        return ResourceBundle.getBundle("i18n/sketch", LOCALE).getString(key);
    }

    public static URL getImageURL(String pngName) {
        return Resources.class.getResource("/images/" + pngName);
    }

    public static ImageIcon getImageIcon(String pngName) {
        return new ImageIcon(Resources.class.getResource("/images/" + pngName));
    }

    private Resources() {
    }
}
