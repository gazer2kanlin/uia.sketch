package uia.sketch.model;

import java.io.File;

import org.junit.Test;

import uia.sketch.model.xml.GridType;
import uia.sketch.model.xml.ObjectFactory;
import uia.sketch.model.xml.SketchBookType;

public class SketchBookTypeTest {

    @Test
    public void testObjectFactory() {
        ObjectFactory of = new ObjectFactory();
        SketchBookType book = of.createSketchBookType();
        System.out.println(book.getPhotoList());
        GridType grid = of.createGridType();
        System.out.println(grid.getWidth());
    }

    @Test
    public void testSample() throws Exception {
        SketchBookType book = SketchBookTypeHelper.load(new File(SketchBookTypeTest.class.getResource("sample.xml").toURI()));
        book.getPhotoList().getPhoto().stream().forEach(p -> {
            System.out.println(String.format("%-20s, zoom:%-4s, offset:%-7s, %s",
                    p.getName(),
                    p.getZoom(),
                    p.getOffset(),
                    p.getPath()));
            p.getGrids().getGrid().stream().forEach(g -> {
                System.out.println(String.format("  degree:%-3s, color:%-15s, offset:%-7s, width:%-3s, enabled:%s",
                        g.getDegree(),
                        g.getLineColor(),
                        g.getOffset(),
                        g.getWidth(),
                        g.isEnabled()));
            });
        });
    }
}
