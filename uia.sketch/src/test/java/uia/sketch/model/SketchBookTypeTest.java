package uia.sketch.model;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import uia.sketch.model.xml.SketchBookType;

public class SketchBookTypeTest {

    @Test
    public void testSample() throws Exception {
        SketchBookType book = SketchBookTypeHelper.load(new File(SketchBookTypeTest.class.getResource("sample.xml").toURI()));
        Assert.assertEquals(1, book.getX());
        Assert.assertEquals(2, book.getY());
        Assert.assertEquals(400, book.getWidth());
        Assert.assertEquals(300, book.getHeight());
        Assert.assertEquals(2, book.getPhotoList().getPhoto().size());

        book.getPhotoList().getPhoto().stream().forEach(p -> {
            System.out.println(String.format("%-4s, zoom:%-4s, offset:%-5s, %-20s, drag:%s, view(%s,%s)",
                    p.getName(),
                    p.getZoom(),
                    p.getOffset(),
                    p.getPath(),
                    p.getDragTarget(),
                    p.getViewWidth(),
                    p.getViewHeight()));
            p.getLayers().getLayer().stream().forEach(g -> {
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
