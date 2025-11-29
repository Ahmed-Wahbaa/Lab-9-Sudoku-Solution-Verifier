package lab.pkg9;

import java.util.*;

public class RowsChecker extends Thread {
    private final int[][] board;
    private final List<String> errors;

    public RowsChecker(int[][] board, List<String> errors) {
        this.board = board;
        this.errors = errors;
    }

    @Override
    public void run() {
        // Check each row for duplicates
        for (int r = 0; r < 9; r++) {
            Map<Integer, List<Integer>> map = new HashMap<>();
            for (int c = 0; c < 9; c++)
                map.computeIfAbsent(board[r][c], k -> new ArrayList<>()).add(c + 1);

            synchronized (errors) { // thread-safe add
                for (var e : map.entrySet())
                    if (e.getValue().size() > 1)
                        errors.add("ROW " + (r + 1) + ", #" + e.getKey() + ", " + e.getValue());
            }
        }
    }
}
