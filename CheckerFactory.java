package lab.pkg9;

import java.util.*;

public class CheckerFactory {

    private final int[][] board;
    private final List<String> errors;

    public CheckerFactory(int[][] board, List<String> errors) {
        this.board = board;
        this.errors = errors;
    }

    // Create main checkers
    public Thread createRowsChecker() { return new RowsChecker(board, errors); }
    public Thread createColsChecker() { return new ColsChecker(board, errors); }
    public Thread createBoxesChecker() { return new BoxesChecker(board, errors); }

    // Create single row/col/box checkers
    public Thread createSingleRowChecker(int r) { return new SingleRowChecker(board, errors, r); }
    public Thread createSingleColChecker(int c) { return new SingleColChecker(board, errors, c); }
    public Thread createSingleBoxChecker(int b) { return new SingleBoxChecker(board, errors, b); }
}
