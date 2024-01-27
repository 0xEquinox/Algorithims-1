/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;

class Node {
    private int height;
    private Point2D point;
    private Node left;
    private Node right;
    private RectHV rect;

    public Node(Point2D point, int height, RectHV rect) {
        this.setPoint(point);
        this.setHeight(height);
        this.setRect(rect);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Point2D getPoint() {
        return point;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public RectHV getRect() {
        return rect;
    }

    public void setRect(RectHV rect) {
        this.rect = rect;
    }
}

public class KdTree {
    private Node root;
    private int size = 0;
    private static final double TREE_X_BOUND = 1.0;
    private static final double TREE_Y_BOUND = 1.0;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        root = insert(root, point, 0, 0.0, 0.0, TREE_X_BOUND, TREE_Y_BOUND);
    }

    public Point2D nearest(Point2D point) {
        if (isEmpty()) return null;

        return nearest(root, point, null);
    }

    public boolean contains(Point2D p) {
        return search(root, p, 0);
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> pointsInRect = new ArrayList<>();
        Stack<Node> nodesToVisit = new Stack<>();

        if (root != null)
            nodesToVisit.push(root);

        nodesToVisit.forEach(current -> {
            assert current != null;
            if (rect.intersects(current.getRect()) && rect.contains(current.getPoint())) {
                pointsInRect.add(current.getPoint());
            }
            nodesToVisit.push(current.getLeft());
            nodesToVisit.push(current.getRight());
        });

        return pointsInRect;
    }

    // Utility method for finding points, not necessary, but it simplifies some things
    private boolean search(Node node, Point2D point, int height) {
        if (node == null) {
            return false;
        }

        double compare = compare(node.getPoint(), point, height);
        if (compare > 0) {
            return search(node.getLeft(), point, height + 1);
        }
        else if (compare < 0) {
            return search(node.getRight(), point, height + 1);
        }

        return true;
    }

    private Point2D nearest(Node current, Point2D point, Point2D nearest) {
        if (current == null || (nearest != null && nearest.distanceSquaredTo(point) <= current.getRect().distanceSquaredTo(point))) {
            return nearest;
        }
        if (nearest == null || current.getPoint().distanceSquaredTo(point) < nearest.distanceSquaredTo(point)) {
            nearest = current.getPoint();
        }
        if (current.getRight() != null && current.getRight().getRect().contains(point)) {
            nearest = nearest(current.getRight(), point, nearest);
            nearest = nearest(current.getLeft(), point, nearest);
        }
        else {
            nearest = nearest(current.getLeft(), point, nearest);
            nearest = nearest(current.getRight(), point, nearest);
        }
        return nearest;
    }

    // Some credit to Maksim for helping me out here :)
    private Node insert(Node node, Point2D point, int height, double xmin, double ymin, double xmax, double ymax) {
        if (node == null) {
            size++;
            return new Node(point, height, new RectHV(xmin, ymin, xmax, ymax));
        }

        boolean splitVert = height % 2 == 0;
        node.setRect(new RectHV(xmin, ymin, xmax, ymax));

        int cmp = compare(point, node.getPoint(), height);
        if (cmp < 0) {
            if (splitVert) {
                node.setLeft(insert(node.getLeft(), point, height + 1, xmin, ymin, node.getPoint().x(), ymax));
            }
            else {
                node.setLeft(insert(node.getLeft(), point, height + 1, xmin, ymin, xmax, node.getPoint().y()));
            }
        }
        else if (cmp > 0) {
            if (splitVert) {
                node.setRight(insert(node.getRight(), point, height + 1, node.getPoint().x(), ymin, xmax, ymax));
            }
            else {
                node.setRight(insert(node.getRight(), point, height + 1, xmin, node.getPoint().y(), xmax, ymax));
            }
        }

        return node;
    }

    public void draw() {

    }

    // Utility method for comparing two points to each other
    private int compare(Point2D p1, Point2D p2, int height) {
        boolean isHorizontalLine = height % 2 == 0;

        double[] axis1 = { p1.x(), p2.x() };
        double[] axis2 = { p1.y(), p2.y() };

        double[] mainAxis = isHorizontalLine ? axis1 : axis2;
        double[] altAxis = !isHorizontalLine ? axis1 : axis2;

        int comp = Double.compare(mainAxis[0], mainAxis[1]);
        int altComp = Double.compare(altAxis[0], altAxis[1]);

        return comp == 0 ? altComp : comp;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        KdTree setOfPoints = new KdTree();

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
