import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Objects;
import java.util.stream.Stream;

public class Solver {
    private static class SearchNode implements Comparable<SearchNode> {
        private final int movesToReach;
        private final Board board;
        private final SearchNode previousNode;
        private final int priority;

        public SearchNode(int movesToReach, Board board, SearchNode previousNode) {
            this.previousNode = previousNode;
            this.board = board;
            this.movesToReach = movesToReach;
            this.priority = movesToReach + board.manhattan();
        }

        public int getMovesToReach() {
            return movesToReach;
        }

        public Board getBoard() {
            return board;
        }

        public SearchNode getPreviousNode() {
            return previousNode;
        }

        @Override
        public int compareTo(SearchNode searchNode) {
            return this.priority - searchNode.priority;
        }
    }
    private final SearchNode solutionNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<SearchNode> boards = new MinPQ<>();
        boards.insert(new SearchNode(0, initial, null));

        MinPQ<SearchNode> twinBoards = new MinPQ<>();
        twinBoards.insert(new SearchNode(0, initial.twin(), null));

        while (!boards.min().getBoard().isGoal() && !twinBoards.min().getBoard().isGoal()) {
            addNeighbors(boards, boards.delMin());
            addNeighbors(twinBoards, twinBoards.delMin());
        }

        if (boards.min().getBoard().isGoal()) {
            solutionNode = boards.min();
        } else {
            solutionNode = null;
        }
    }

    private void addNeighbors(MinPQ<SearchNode> searchNodes, SearchNode currentNode) {
        for (Board board : currentNode.getBoard().neighbors()) {
            if (currentNode.previousNode != null && board.equals(currentNode.previousNode.getBoard())) continue;
            searchNodes.insert(new SearchNode(currentNode.movesToReach + 1, board, currentNode));
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solutionNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? solutionNode.movesToReach : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Stack<Board> sequence = new Stack<>();
        Stream.iterate(solutionNode, Objects::nonNull, sn -> sn.previousNode)
                .map(SearchNode::getBoard)
                .forEach(sequence::push);

        return sequence;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}