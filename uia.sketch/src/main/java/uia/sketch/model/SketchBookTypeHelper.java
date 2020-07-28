package uia.sketch.model;

import java.awt.Color;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import uia.sketch.model.xml.LayerListType;
import uia.sketch.model.xml.LayerType;
import uia.sketch.model.xml.ObjectFactory;
import uia.sketch.model.xml.PhotoListType;
import uia.sketch.model.xml.PhotoType;
import uia.sketch.model.xml.SketchBookType;

/**
 *
 * @author Kyle K. Lin
 *
 */
public class SketchBookTypeHelper {

    static Unmarshaller UNMARSHALLER;

    public static SketchBookType createBook() {
        SketchBookType book = new SketchBookType();
        book.setPhotoList(new PhotoListType());
        return book;
    }

    public static PhotoType createPhoto(File file) {
        PhotoType photo = new PhotoType();
        photo.setPath(file == null ? null : "photo/" + file.getName());
        // photo.setPath(file == null ? null : file.getAbsolutePath());
        photo.setName(file == null ? "Undefined" : file.getName());
        photo.setOffset("0,0");
        photo.setZoom(1.0d);
        photo.setViewWidth(100);
        photo.setViewHeight(100);
        photo.setDragTarget("PHOTO");
        photo.setLayers(new LayerListType());
        photo.getLayers().getLayer().add(createLayer("GRID1", true, "60,60,60", 40));
        photo.getLayers().getLayer().add(createLayer("GRID2", false, "180,180,180", 100));
        photo.getLayers().getLayer().add(createLayer("CIRCLE", false, "180,180,180", 100));
        photo.getLayers().getLayer().add(createLayer("TRIANGLE", false, "180,180,180", 100));
        photo.getLayers().getLayer().add(createLayer("PERSPECTIVE", false, "180,180,180", 100));
        return photo;

    }

    public static LayerType createLayer(String name, boolean enabled, String color, int width) {
        LayerType layer = new LayerType();
        layer.setName(name);
        layer.setDegree(0);
        layer.setEnabled(enabled);
        layer.setLineColor(color);
        layer.setOffset("0,0");
        layer.setWidth(width);
        return layer;
    }

    public static void save(SketchBookType book, File file) throws Exception {
        ObjectFactory of = new ObjectFactory();

        JAXBContext jaxbContext = JAXBContext.newInstance(SketchBookType.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(of.createSketchBook(book), file);
    }

    public static SketchBookType load(File file) throws Exception {
    	return load(new FileInputStream(file));
    }

    public static SketchBookType load(InputStream stream) throws Exception {
        if (UNMARSHALLER == null) {
            initial();
        }

        // Create the XMLReader
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();

        // The filter class to set the correct namespace
        XMLFilterImpl xmlFilter = new XMLNamespaceFilter(reader);
        reader.setContentHandler(UNMARSHALLER.getUnmarshallerHandler());

        SAXSource source = new SAXSource(xmlFilter, new InputSource(stream));

        @SuppressWarnings("unchecked")
        JAXBElement<SketchBookType> elem = (JAXBElement<SketchBookType>) UNMARSHALLER.unmarshal(source);
        return elem.getValue();
    }

    public static SketchBookType load(String content) throws Exception {
        if (UNMARSHALLER == null) {
            initial();
        }

        // Create the XMLReader
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();

        // The filter class to set the correct namespace
        XMLFilterImpl xmlFilter = new XMLNamespaceFilter(reader);
        reader.setContentHandler(UNMARSHALLER.getUnmarshallerHandler());

        InputStream inStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
        SAXSource source = new SAXSource(xmlFilter, new InputSource(inStream));

        @SuppressWarnings("unchecked")
        JAXBElement<SketchBookType> elem = (JAXBElement<SketchBookType>) UNMARSHALLER.unmarshal(source);
        return elem.getValue();
    }

    public static Color toColor(String color) {
        String[] rgb = color.split(",");
        return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
    }

    public static String toColorString(Color color) {
        return String.format("%s,%s,%s", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Point toPoint(String point) {
        String[] xy = point.split(",");
        return new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
    }

    public static String toPointString(Point point) {
        return String.format("%s,%s", point.x, point.y);
    }

    static void initial() throws Exception {
        try {
            JAXBContext jc = JAXBContext.newInstance("uia.sketch.model.xml");
            UNMARSHALLER = jc.createUnmarshaller();
        }
        catch (JAXBException ex) {
            UNMARSHALLER = null;
            throw ex;
        }
    }

    static class XMLNamespaceFilter extends XMLFilterImpl {

        public XMLNamespaceFilter(XMLReader reader) {
            super(reader);
        }

        @Override
        public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
            super.startElement("http://sketch.uia/model/xml", localName, qName, attributes);
        }
    }
}
