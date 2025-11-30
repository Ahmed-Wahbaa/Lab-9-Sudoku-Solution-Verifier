package lab.pkg9;

import java.util.*;

public class ColsChecker extends Thread {
    private final int[][] board;
    private final List<String> errors;

    public ColsChecker(int[][] board, List<String> errors) {
        this.board = board;
        this.errors = errors;
    }

    @Override
    public void run() {
        // Check each column for duplicates
        for (int c = 0; c < 9; c++) {
            Map<Integer, List<Integer>> map = new HashMap<>();
            for (int r = 0; r < 9; r++)
                map.computeIfAbsent(board[r][c], k -> new ArrayList<>()).add(r + 1);

            synchronized (errors) {
                for (var e : map.entrySet())
                    if (e.getValue().size() > 1)
                        errors.add("COL " + (c + 1) + ", #" + e.getKey() + ", " + e.getValue());
            }
        }
    }
}
