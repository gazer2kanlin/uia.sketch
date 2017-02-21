package uia.sketch;

import uia.sketch.model.xml.PhotoType;

/**
 *
 * @author Kyle K. Lin
 *
 */
public class PhotoFile {

    public final PhotoType config;

    /**
     *
     * @param photo
     */
    public PhotoFile(PhotoType config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return " " + this.config.getName();
    }
}
