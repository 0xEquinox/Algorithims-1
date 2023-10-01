import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF quickUnionUF;
    private final boolean[][] grid;
    private int openSpaces = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n was less than or equal to 0");

        this.grid = new boolean[n][n];
        this.quickUnionUF = new WeightedQuickUnionUF((n * n) + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) return;

        isException(row - 1, col - 1);
        this.openSpaces += 1;

        grid[row - 1][col - 1] = true;

        /*
        This code checks each surrounding square so if we have a grid:
        | 1 | 2 | 3 |
        | 1 | 1 | 6 |
        | 7 | 8 | 9 |

        and we open space 6 on the grid, now we check up, down, left, right, and find that 5 is also open, so it will union 6 to 5.
        This will give us a new grid:
        | 1 | 2 | 3 |
        | 1 | 1 | 1 |
        | 7 | 8 | 9 |
         */
        int truePosition = getOneDPosition(row, col);
        if ((row - 1) != 0 && isOpen(row - 1, col)) {
            quickUnionUF.union(truePosition, getOneDPosition(row - 1,  col));
        } else if ((row - 1) == 0) {
            quickUnionUF.union(truePosition, grid.length * grid.length);
        }
        if ((row - 1) != (grid.length - 1) && isOpen(row + 1, col)) {
            quickUnionUF.union(truePosition, getOneDPosition(row + 1, col));
        } else if ((row - 1) == grid.length - 1) {
            quickUnionUF.union(truePosition, (grid.length * grid.length) + 1);
        }
        if ((col - 1) != grid.length - 1 && isOpen(row, col + 1)) {
            quickUnionUF.union(truePosition, getOneDPosition(row, col + 1));
        }
        if ((col - 1) != 0 && isOpen(row, col - 1)) {
            quickUnionUF.union(truePosition, getOneDPosition(row, col - 1));
        }
    }

    private int getOneDPosition(int row, int col) {
        return (row - 1) * grid.length + (col - 1);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        isException(row - 1, col - 1);
        return this.grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        isException(row - 1, col - 1);

        return quickUnionUF.find(getOneDPosition(row, col)) == quickUnionUF.find(grid.length * grid.length);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSpaces;
    }

    // does the system percolate?
    /*
    This method should connect the elements in the first row that are open to the top and bottom master element and then check if the top and bottom master elements are connected.
    For example if we have this grid:
            | 0 |
        | 1 | 2 | 3 |
        | 1 | 1 | 6 |
        | 7 | 1 | 9 |
            | 10 |
     It will connect top and bottom ones:
            | 0 |
        | 0 | 2 | 3 |
        | 1 | 1 | 6 |
        | 7 | 1 | 9 |
            | 8 |

     Then it will check if the two master nodes, which they are, so it will return true the system percolates
     */
    public boolean percolates() {
        int len = grid.length;
        return quickUnionUF.find(len * len) == quickUnionUF.find((len * len) + 1);
    }

    private void isException(int row, int col) {
        if (row < 0 || col < 0 || row >= grid.length || col >= grid.length) {
            throw new IllegalArgumentException("Given row: " + row + ", or col: " + col + " is outside the bounds");
        }
    }

    public void debugPrint() {
        for (int row = 1; row <= grid.length; row++) {
            for (int col = 1; col <= grid.length; col++) {
                if (isFull(row, col)) System.out.print("* ");
                else if (isOpen(row, col)) System.out.print("T ");
                else System.out.print("F ");
            }
            System.out.println();
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);

        percolation.open(1, 1);
        percolation.open(2, 1);
        percolation.open(3, 1);
        percolation.open(2, 3);
        percolation.open(3, 3);
        percolation.debugPrint();
    }
}
