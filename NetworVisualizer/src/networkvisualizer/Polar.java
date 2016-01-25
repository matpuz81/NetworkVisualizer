/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package networkvisualizer;

import java.awt.Point;

/**
 *
 * @author chef
 */
public class Polar {
    
    private double angle;
    private double distance;
    
    public Polar(Point p1, Point p2, double zoom)
    {
        angle = getAngle(p1,p2);
        distance = getDistance(p1,p2,zoom);
    }
    
    public Polar(double angle, double distance)
    {
        this.angle=angle;
        this.distance=distance;
    }    
    public static Polar getPolar(Point p1, Point p2, double zoom)
    {
        return new Polar(getAngle(p1,p2),getDistance(p1,p2,zoom));
    }
    
    public static double getAngle(Point p1 , Point p2)
    {
        return 180+Math.toDegrees(Math.atan2((p1.x-p2.x),(p1.y-p2.y)));
    }
    
    public static double getDistance(Point p1, Point p2, double zoom)
    {
        return Math.sqrt(Math.pow((p1.x-p2.x), 2)+Math.pow((p1.y-p2.y), 2))*zoom;   
    }
    
    public Point getPoint()
    {
        return getPoint(angle,distance);
    }
    
    public static Point getPoint(double angle, double distance)
    {
        return new Point((int)(Math.sin(Math.toRadians(angle))*distance),(int)(Math.cos(Math.toRadians(angle))*distance));
    }
    
    public Point getPoint(Point ref, double zoom)
    {
        Point p = new Point();
        p.x = ref.x + (int)(Math.sin(Math.toRadians(angle))*distance/zoom);
        p.y = ref.y + (int)(Math.cos(Math.toRadians(angle))*distance/zoom);
        return p;
    }
    
    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
    
    public double getDistance() {
        return distance;
    }
    
    public void setDistance(double distance) {
        this.distance = distance;
    }
}
