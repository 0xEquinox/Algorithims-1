/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

class RGB {
    public final int R, G, B;

    public RGB(int color) {
        this.R = (color >> 16) & 0xFF;
        this.G = (color >> 8) & 0xFF;
        this.B = color & 0xFF;
    }
}

public class SeamCarver {
    private Picture picture;
    private double[][] energy;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();

        Picture copy = new Picture(picture.width(), picture.height());
        for (int i = 0; i < copy.height(); i++) {
            for (int j = 0; j < copy.width(); j++) {
                copy.setRGB(j, i, picture.getRGB(j, i));
            }
        }

        this.picture = copy;
        this.energy = new double[picture.height()][picture.width()];
        populateEnergyGrid();
    }

    private void populateEnergyGrid() {
        // Create a new energy grid
        double[][] newEnergy = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                newEnergy[i][j] = energy(j, i);
            }
        }
        this.energy = newEnergy;
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    private double calculateGradient(RGB first, RGB second) {
        int redXDelta = first.R > second.R ? first.R - second.R : second.R - first.R;
        int greenXDelta = first.G > second.G ? first.G - second.G : second.G - first.G;
        int blueXDelta = first.B > second.B ? first.B - second.B : second.B - first.B;

        return Math.pow(redXDelta, 2) + Math.pow(greenXDelta, 2) + Math.pow(blueXDelta, 2);
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height())
            throw new IllegalArgumentException();
        if (x < 1 || y < 1 || x >= picture.width() - 1 || y >= picture.height() - 1)
            return 1000.0;

        RGB right = new RGB(picture.getRGB(x + 1, y));
        RGB left = new RGB(picture.getRGB(x - 1, y));
        RGB up = new RGB(picture.getRGB(x, y + 1));
        RGB down = new RGB(picture.getRGB(x, y - 1));

        double xGradient = calculateGradient(left, right);
        double yGradient = calculateGradient(up, down);

        double energy = Math.sqrt(xGradient + yGradient);

        return energy;
    }

    private int[] getOneDimensionalImagePath(int width, int height) {
        if (height == 1) {
            return new int[] { 0 }; // All energy values are the same
        }
        int[] path = new int[height];
        if (width == 1) {
            for (int i = 0; i < height; i++) {
                path[i] = 0;
            }
        }

        return path;
    }

    private int[] findSeam(double[][] energy) {
        double[][] distTo = new double[energy.length][energy[0].length];
        int width = distTo[0].length;
        int height = distTo.length;

        // If height or width is 1, no need to compute anything else
        if (width == 1 || height == 1) return getOneDimensionalImagePath(width, height);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                double originalEnergy = energy[row][col];
                // First row stays the same
                if (row == 0 || row == 1 || col == 0 || col == width - 1) {
                    distTo[row][col] = originalEnergy;
                    continue;
                }
                // For all other rows find the three spaces above (or two if the edge is on the side)

                double left = col != 1 ? distTo[row - 1][col - 1] : Double.POSITIVE_INFINITY;
                double right = col < width - 2 ? distTo[row - 1][col + 1] :
                               Double.POSITIVE_INFINITY;
                double center = col != width - 1 ? distTo[row - 1][col] : Double.POSITIVE_INFINITY;

                double lowest = Math.min(Math.min(left, right), center);

                distTo[row][col] = lowest + originalEnergy;
            }
        }

        // Find the x coordinate for the shortest seam
        int lowestEnergyXVal = 1;
        for (int i = 1; i < width - 1; i++) {
            if (distTo[height - 2][i] < distTo[height - 2][lowestEnergyXVal]) {
                lowestEnergyXVal = i;
            }
        }

        int[] path = new int[height];
        // Reclaculate the path for the shortest seam so it can be removed
        for (int row = distTo.length - 1, col = lowestEnergyXVal, pathIndex = distTo.length - 1;
             row > 0; row--) {
            // First element is just the lowestEnergyXVal
            if (row == distTo.length - 1) {
                path[pathIndex--] = col;
            }

            // Find the above three nodes and find the lowest number
            double left = col != 1 ? distTo[row - 1][col - 1] : Double.POSITIVE_INFINITY;
            double right = col < width - 2 ? distTo[row - 1][col + 1] : Double.POSITIVE_INFINITY;
            double center = col != width - 1 ? distTo[row - 1][col] : Double.POSITIVE_INFINITY;

            double lowest = Math.min(Math.min(left, right), center);

            if (lowest == left) {
                path[pathIndex--] = --col;
            }
            else if (lowest == center) {
                path[pathIndex--] = col;
            }
            else if (lowest == right) {
                path[pathIndex--] = ++col;
            }
        }
        return path;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] horizontalImage = new double[width()][height()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                horizontalImage[col][row] = energy[row][col];
            }
        }

        return findSeam(horizontalImage);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(
                energy
        ); // Find seam is basically just find vertical seam, but it takes a parameter so it can be generalized for the horiztonal seams as well
    }

    private boolean isValidSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                return false;
            }
        }
        return true;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width() || !isValidSeam(seam))
            throw new IllegalArgumentException();

        Picture newImage = new Picture(width(), height() - 1);

        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height() - 1; row++) {
                if (seam[col] < 0 || seam[col] >= height()) throw new IllegalArgumentException();
                if (row < seam[col]) {
                    newImage.set(col, row, picture.get(col, row));
                }
                else {
                    newImage.set(col, row, picture.get(col, row + 1));
                }
            }
        }
        this.picture = new Picture(newImage);
        populateEnergyGrid();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height() || !isValidSeam(seam))
            throw new IllegalArgumentException();

        Picture newImage = new Picture(width() - 1, height());
        for (int row = 0, index = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (seam[row] < 0 || seam[row] >= width()) throw new IllegalArgumentException();
                if (col == seam[row]) continue;
                newImage.setRGB(index++, row, picture.getRGB(col, row));
            }
            index = 0;
        }

        this.picture = newImage;
        populateEnergyGrid();
    }

    //  unit testing (optional Cree Elders Get Emotional When I Speak Their Dying Language

    public static void main(String[] args) {
        SeamCarver seamCarver = new SeamCarver(
                new Picture("1x8.png"));

        PrintEnergy.main(new String[] { "1x8.png" });
        Arrays.stream(seamCarver.findVerticalSeam()).forEach(StdOut::println);

    }
}