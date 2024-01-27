/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Iterator;

public class PointSET {

    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        this.points = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points) {
            StdDraw.point(point.x(), point.y());
        }
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        Iterator<Point2D> pointItertor = points.iterator();
        ArrayList<Point2D> pointsInRect = new ArrayList<>();

        pointItertor.forEachRemaining(point -> {
            if (rect.contains(point)) {
                pointsInRect.add(point);
            }
        });

        return pointsInRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        Iterator<Point2D> pointIterator = points.iterator();
        final Point2D[] nearest = { null };

        pointIterator.forEachRemaining(point -> {
            if (nearest[0] == null || point.distanceSquaredTo(p) < nearest[0].distanceSquaredTo(
                    p)) {
                nearest[0] = point;
            }
        });

        return nearest[0];
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        In in = new In(args[0]);
        PointSET setOfPoints = new PointSET();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            // System.out.println(p);
            Point2D point = new Point2D(x, y);
            setOfPoints.insert(point);
        }

        Point2D testPoint = new Point2D(.81, .30);
        // StdDraw.clear();
        // StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(.01);
        // setOfPoints.draw();

        // System.out.println(setOfPoints.isEmpty());
        System.out.println("nearest point: " + setOfPoints.nearest(testPoint));
    }
}