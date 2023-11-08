import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] segments;
    public FastCollinearPoints(Point[] points) {
        checkNull(points);

        Point[] pointsCopyOne = Arrays.copyOf(points, points.length);
        Point[] pointsCopyTwo = Arrays.copyOf(points, points.length);
        ArrayList<LineSegment> segmentsList = new ArrayList<>();

        Arrays.sort(pointsCopyTwo);

        checkDuplicates(pointsCopyTwo);
        for (Point origin : pointsCopyTwo) {
            Arrays.sort(pointsCopyOne);
            Arrays.sort(pointsCopyOne, origin.slopeOrder());

            int count = 1;
            Point lineBeginning = null;
            for (int j = 0; j < pointsCopyOne.length - 1; j++) {
                if (pointsCopyOne[j].slopeTo(origin) == pointsCopyOne[j + 1].slopeTo(origin)) {
                    count += 1;
                    if (count == 2) {
                        lineBeginning = pointsCopyOne[j];
                        count += 1;
                    } else if (count >= 4 && j + 1 == pointsCopyOne.length - 1) {
                        if (lineBeginning.compareTo(origin) > 0) {
                            segmentsList.add(new LineSegment(origin, pointsCopyOne[j + 1]));
                        }
                        count = 1;
                    }
                } else if (count >= 4) {
                    if (lineBeginning.compareTo(origin) > 0) {
                        segmentsList.add(new LineSegment(origin, pointsCopyOne[j]));
                    }
                    count = 1;
                } else {
                    count = 1;
                }
            }
        }
        segments = segmentsList.toArray(new LineSegment[0]);
    }

    /**
     * Returns the number of segments
     *
     * @return number of segments containing 4 or more points
     */
    public int numberOfSegments()
    {
        return segments.length;
    }

    /**
     * Returns array of segments that were discovered in the given points array
     *
     * @return array of discovered segments
     */
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, numberOfSegments());
    }

    private void checkDuplicates(Point[] points) {
        for (int i = 0; i < points.length - 1; ++i) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new java.lang.IllegalArgumentException("There are duplicates");
            }
        }
    }

    private void checkNull(Point[] points) {
        for (Point point : points) {
            if (point == null) {
                throw new NullPointerException("Something is null :(");
            }
        }
    }
}
