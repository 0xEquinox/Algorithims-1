/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;

class Direction {
    public int dx;
    public int dy;

    public Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}

public class BoggleSolver {
    private final Trie dictionary;
    private final Direction[] neighbors;
    private ArrayList<String> validWords;
    private boolean[][] visited;

    public BoggleSolver(String[] dictionary) {
        this.dictionary = new Trie();
        for (String word : dictionary) {
            this.dictionary.add(word);
        }

        // Populate neighbors
        this.neighbors = new Direction[] {
                new Direction(-1, -1),
                new Direction(-1, 0),
                new Direction(-1, 1),
                new Direction(0, -1),
                new Direction(0, 1),
                new Direction(1, -1),
                new Direction(1, 0),
                new Direction(1, 1),
                };

        this.validWords = new ArrayList<>();
    }

    private boolean isValidCord(int x, int y, int xBound, int yBound) {
        return !(x < 0 || x >= xBound || y < 0 || y >= yBound);
    }

    private ArrayList<Direction> getNeighbors(int row, int col, int xBound, int yBound) {
        ArrayList<Direction> tileNeighbors = new ArrayList<>();

        for (Direction cord : this.neighbors) {
            int nextRow = row + cord.dx;
            int nextCol = col + cord.dy;
            if (isValidCord(nextRow, nextCol, xBound, yBound)) {
                tileNeighbors.add(new Direction(nextRow, nextCol));
            }
        }

        return tileNeighbors;
    }

    private void dfs(int row, int col, String current,
                     BoggleBoard board) {

        char letter = board.getLetter(row, col);


        current += letter;
        if (letter == 'Q') current += 'U';

        if (!dictionary.hasKeyWithPrefix(current)) return;

        if (current.length() > 2 && !validWords.contains(current) && dictionary.contains(
                current)) {
            validWords.add(current);
        }

        visited[row][col] = true;
        ArrayList<Direction> tileNeighbors = getNeighbors(row, col, board.rows(), board.cols());
        for (Direction neighbor : tileNeighbors) {
            if (!visited[neighbor.dx][neighbor.dy]) {
                dfs(neighbor.dx, neighbor.dy, current, board);
            }
        }
        visited[row][col] = false;
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.visited = new boolean[board.rows()][board.cols()];
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                dfs(row, col, "", board);
            }
        }

        ArrayList<String> validWordsClone = new ArrayList<>();

        for (int i = 0; i < validWords.size(); i++) {
            validWordsClone.add(validWords.get(i));
        }

        this.validWords = new ArrayList<>();
        return validWordsClone;
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
