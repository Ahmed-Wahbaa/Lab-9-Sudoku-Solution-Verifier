package lab.pkg9;

import java.util.*;

public class SingleColChecker extends Thread {
    private final int[][] board;
    private final List<String> errors;
    private final int col;

    public SingleColChecker(int[][] board, List<String> errors, int col) {
        this.board = board;
        this.errors = errors;
        this.col = col;
    }

    @Override
    public void run() {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int r = 0; r < 9; r++)
            map.computeIfAbsent(board[r][col], k -> new ArrayList<>()).add(r + 1);

        synchronized (errors) {
            for (var e : map.entrySet())
                if (e.getValue().size() > 1)
                    errors.add("COL " + (col + 1) + ", #" + e.getKey() + ", " + e.getValue());
        }
    }
}
