package uia.sketch;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public final class Resources {

    static Locale LOCALE = Locale.TAIWAN;

    static String TITLE = "";

    static String getString(String key) {
        return ResourceBundle.getBundle("i18n/sketch", LOCALE).getString(key);
    }

    static URL getImageURL(String pngName) {
        return Resources.class.getResource("/images/" + pngName);
    }

    static ImageIcon getImageIcon(String pngName) {
        return new ImageIcon(Resources.class.getResource("/images/" + pngName));
    }

    private Resources() {

    }
}
