import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Board {
    private final short[][] tiles;
    private short size;
    private Location zeroLocation;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.size = (short) tiles.length;
        this.tiles = new short[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.tiles[i][j] = (short) tiles[i][j];
    }

    private Board(short[][] tiles) {
        this.tiles = tiles;
        this.size = (short) tiles.length;
    }

    // string representation of this board
    public String toString() {
        return IntStream.range(0, size)
                .mapToObj(i -> IntStream.range(0, size)
                        .mapToObj(j -> Integer.toString(tiles[i][j]))
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n", size + "\n", ""));
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        final int[] correctValue = {1}; // This makes it immutable

        return Arrays.stream(tiles)
                .flatMapToInt(row -> Arrays.stream(convertShortArrayToInt(row)))
                .filter(tile -> tile != correctValue[0]++ && tile != 0)
                .map(tile -> 1)
                .sum();
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDistance = 0;
        for (int x = 0, correctValue = 1; x < tiles.length; x++) {
            for (int y = 0; y < tiles.length; y++) {
                int tile = tiles[x][y];
                if (tile == correctValue++ || tile == 0) continue;
                // Calculate distance to the correct location and subtract from current location
                int solutionX = x - (tile - 1) / tiles.length;
                int solutionY = y - (tile - 1) % tiles.length;

                manhattanDistance += Math.abs(solutionY) + Math.abs(solutionX);
            }
        }
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        return Arrays.deepEquals(tiles, ((Board) y).tiles);
    }
    private Board swap(Location loc1, Location loc2) {
        short[][] tilesCopy = new short[size][size];
        // Copy the array
        for (int i = 0; i < size; i++) {
            System.arraycopy(tiles[i], 0, tilesCopy[i], 0, size);
        }

        tilesCopy[loc1.x][loc1.y] = tilesCopy[loc2.x][loc2.y];
        tilesCopy[loc2.x][loc2.y] = tiles[loc1.x][loc1.y];
        return new Board(tilesCopy);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] == 0) this.zeroLocation = new Location(i, j);
            }
        }

        Predicate<Location> isValidNeighbor = neighbor -> neighbor.x >= 0 && neighbor.x < size && neighbor.y >= 0 && neighbor.y < size;

        return Stream.of(
                        new Location(zeroLocation.x- 1, zeroLocation.y),
                        new Location(zeroLocation.x + 1, zeroLocation.y),
                        new Location(zeroLocation.x, zeroLocation.y - 1),
                        new Location(zeroLocation.x, zeroLocation.y + 1)
                ).filter(isValidNeighbor)
                .map(neighbor -> swap(zeroLocation, neighbor))
                .collect(Collectors.toList());
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Location zeroLocation = IntStream.range(0, size)
                .boxed()
                .flatMap(i -> IntStream.range(0, size)
                        .mapToObj(j -> new Location(i, j)))
                .filter(location -> tiles[location.x][location.y] == 0)
                .findFirst()
                .orElse(new Location(0, 0));

        if (zeroLocation.x == 0) {
            return swap(new Location(1, 0), new Location(1, 1));
        }
        return swap(new Location(0, 0), new Location(0, 1));
    }

    private int[] convertShortArrayToInt(short[] array) {
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }

        return newArray;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        short[][] startingBoard = new short[][]{
                {0, 1, 3},
                {4, 2, 5},
                {7, 8, 6}
        };
        Board board = new Board(startingBoard);

        System.out.println(board);
        System.out.println("Hamming Distance: " + board.hamming());
        System.out.println("Manhattan Distance: " + board.manhattan());

        for (Board board2 : board.neighbors())
            System.out.println(board2);
    }
}