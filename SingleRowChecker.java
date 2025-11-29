package lab.pkg9;

import java.util.*;

public class SingleRowChecker extends Thread {
    private final int[][] board;
    private final List<String> errors;
    private final int row;

    public SingleRowChecker(int[][] board, List<String> errors, int row) {
        this.board = board;
        this.errors = errors;
        this.row = row;
    }

    @Override
    public void run() {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int c = 0; c < 9; c++)
            map.computeIfAbsent(board[row][c], k -> new ArrayList<>()).add(c + 1);

        synchronized (errors) {
            for (var e : map.entrySet())
                if (e.getValue().size() > 1)
                    errors.add("ROW " + (row + 1) + ", #" + e.getKey() + ", " + e.getValue());
        }
    }
}
