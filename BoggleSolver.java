/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;

class Cord {
    public int dx;
    public int dy;

    public Cord(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}

public class BoggleSolver {
    private final Trie dictionary;
    private final Cord[] neighbors;
    private final ArrayList<String> validWords;

    public BoggleSolver(String[] dictionary) {
        this.dictionary = new Trie();
        for (String word : dictionary) {
            this.dictionary.add(word);
        }

        // Populate neighbors
        this.neighbors = new Cord[] {
                new Cord(-1, -1),
                new Cord(-1, 0),
                new Cord(-1, 1),
                new Cord(0, -1),
                new Cord(0, 1),
                new Cord(1, -1),
                new Cord(1, 0),
                new Cord(1, 1),
                };

        this.validWords = new ArrayList<>();
    }

    private boolean isValidCord(int x, int y, int xBound, int yBound) {
        return !(x < 0 || x >= xBound || y < 0 || y >= yBound);
    }

    private ArrayList<Cord> getNeighbors(int row, int col, int xBound, int yBound) {
        ArrayList<Cord> neighbors = new ArrayList<>();

        for (Cord cord : this.neighbors) {
            int nextRow = row + cord.dx;
            int nextCol = col + cord.dy;
            if (isValidCord(nextRow, nextCol, xBound, yBound)) {
                neighbors.add(new Cord(nextRow, nextCol));
            }
        }

        return neighbors;
    }

    // Convert 2d coordinate into 1 d
    private int twoToOneDConvert(int row, int col, int cols) {
        return row + col * cols;
    }

    private void dfs(int row, int col, ArrayList<Integer> visitedPath, String current,
                     BoggleBoard board) {
        char letter = board.getLetter(row, col);

        visitedPath.add(twoToOneDConvert(row, col, board.cols()));

        current += letter;
        // Qu edge case
        if (letter == 'Q') current += 'U';

        if (current.length() > 2 && !validWords.contains(current) && dictionary.contains(
                new StringBuilder(current)))
            validWords.add(current);

        if (!dictionary.hasKeysWithPrefix(new StringBuilder(current))) return; // it is complete

        ArrayList<Cord> neighbors = getNeighbors(row, col, board.rows(), board.cols());
        for (Cord neighbor : neighbors) {
            if (!visitedPath.contains(twoToOneDConvert(neighbor.dx, neighbor.dy, board.cols()))) {
                dfs(neighbor.dx, neighbor.dy, (ArrayList<Integer>) visitedPath.clone(), current,
                    board);
            }
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                dfs(row, col, new ArrayList<>(), "", board);
            }
        }
        return validWords;
    }

    public int scoreOf(String word) {
        if (word.length() < 3) throw new IllegalArgumentException();
        if (word.length() == 3 || word.length() == 4) return 1;
        else if (word.length() == 5) return 2;
        else if (word.length() == 6) return 3;
        else if (word.length() == 7) return 5;
        else return 11;
    }
}
