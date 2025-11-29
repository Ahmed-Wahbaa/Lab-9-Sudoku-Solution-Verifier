package lab.pkg9;

import java.util.*;

public class BoxesChecker extends Thread {
    private final int[][] board;
    private final List<String> errors;

    public BoxesChecker(int[][] board, List<String> errors) {
        this.board = board;
        this.errors = errors;
    }

    @Override
    public void run() {
        // Check each 3x3 box for duplicates
        for (int b = 0; b < 9; b++) {
            int boxRow = b / 3;
            int boxCol = b % 3;
            Map<Integer, List<Integer>> map = new HashMap<>();
            int pos = 0;

            for (int dr = 0; dr < 3; dr++)
                for (int dc = 0; dc < 3; dc++) {
                    pos++;
                    int r = boxRow * 3 + dr;
                    int c = boxCol * 3 + dc;
                    map.computeIfAbsent(board[r][c], k -> new ArrayList<>()).add(pos);
                }

            synchronized (errors) {
                for (var e : map.entrySet())
                    if (e.getValue().size() > 1)
                        errors.add("BOX " + (b + 1) + ", #" + e.getKey() + ", " + e.getValue());
            }
        }
    }
}
