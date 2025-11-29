package lab.pkg9;

import java.util.*;

public class SingleBoxChecker extends Thread {
    private final int[][] board;
    private final List<String> errors;
    private final int box;

    public SingleBoxChecker(int[][] board, List<String> errors, int box) {
        this.board = board;
        this.errors = errors;
        this.box = box;
    }

    @Override
    public void run() {
        int boxRow = box / 3;
        int boxCol = box % 3;
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
                    errors.add("BOX " + (box + 1) + ", #" + e.getKey() + ", " + e.getValue());
        }
    }
}
