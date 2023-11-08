import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment [] segments;
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.NullPointerException("points is null");
        }

        checkNull(points);

        ArrayList<LineSegment> segmentsList = new ArrayList<>();
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);

        checkDuplicates(pointsCopy);

        for (int i = 0; i < (pointsCopy.length - 3); ++i) {
            for (int j = i + 1; j < (pointsCopy.length - 2); ++j) {
                for (int k = j + 1; k < (pointsCopy.length - 1); ++k) {
                    for (int l = k + 1; l < (pointsCopy.length); ++l) {
                        if (checkEquivalencies(pointsCopy[i], pointsCopy[j], pointsCopy[l]) && checkEquivalencies(pointsCopy[i], pointsCopy[j], pointsCopy[k])) {
                            LineSegment tempLineSegment = new LineSegment(pointsCopy[i], pointsCopy[l]);

                            if (!segmentsList.contains(tempLineSegment)) {
                                segmentsList.add(tempLineSegment);
                            }
                        }
                    }
                }
            }
        }
        segments = segmentsList.toArray(new LineSegment[segmentsList.size()]);

    }

    private void checkNull(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) {
                throw new java.lang.NullPointerException("Something is null :(");
            }
        }
    }
    private void checkDuplicates(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicates found");
            }
        }
    }

    private boolean checkEquivalencies(Point i, Point j, Point c) {
        return i.slopeTo(j) == i.slopeTo(c);
    }

    public int numberOfSegments() {
        return segments.length;
    }
    public LineSegment[] segments()  {
        return segments;
    }
}
