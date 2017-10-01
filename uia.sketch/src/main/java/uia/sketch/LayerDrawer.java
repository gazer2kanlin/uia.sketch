package uia.sketch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public interface LayerDrawer {

    public PhotoPanel getPhotoPanel();

    public void setPhotoPanel(PhotoPanel panel);

    public Point getOffset();

    public void setOffset(Point offset);

    public int getWidth();

    public void setWidth(int width);

    public int getDegree();

    public void setDegree(int degree);

    public Color getLineColor();

    public void setLineColor(Color lineColor);

    public void paint(Graphics g);

    public void setEnabled(boolean enabled);

    public boolean isEnabled();
}
