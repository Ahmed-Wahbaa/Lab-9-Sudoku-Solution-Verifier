package lab.pkg9;

import java.util.*;
import java.io.*;

public class SudokuVerifier {

    private static final int N = 9; // the Sudoku board size
    private int[][] board = new int[N][N]; // The board
    private final List<String> errors = Collections.synchronizedList(new ArrayList<>()); // Thread-safe list to store errors

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java SudokuVerifier <csv-file> <mode(0|3|27|all)>");
            return;
        }

        String csvPath = args[0];
        String modeArg = args[1].toLowerCase();

        SudokuVerifier verifier = new SudokuVerifier();
        verifier.board = BoardLoader.loadCsv(csvPath); // Load Sudoku board from CSV

        // Run all modes or single mode
        if (modeArg.equals("all")) {
            verifier.runAllModes();
        } else {
            int mode = Integer.parseInt(modeArg);
            verifier.runSingleMode(mode);
        }
    }

    // Run one mode and show execution time
    private void runSingleMode(int mode) throws InterruptedException {
        long start = System.nanoTime();
        runMode(mode); // Run the chosen mode
        long end = System.nanoTime();

        printResults(); // Print validation results

        long ms = (end - start) / 1_000_000;
        System.out.println("\nExecution Time: " + ms + " ms");
    }

    // Run all modes and show execution time comparison
    private void runAllModes() throws InterruptedException {
        Map<Integer, Long> time = new LinkedHashMap<>();

        // Run mode 0
        errors.clear();
        long t0 = measure(() -> { try { runMode(0); } catch (Exception ignored) {} });
        time.put(0, t0);

        // Run mode 3
        errors.clear();
        long t3 = measure(() -> { try { runMode(3); } catch (Exception ignored) {} });
        time.put(3, t3);

        // Run mode 27
        errors.clear();
        long t27 = measure(() -> { try { runMode(27); } catch (Exception ignored) {} });
        time.put(27, t27);

        // Print comparison table
        System.out.println("\n================ Execution Time Comparison ================");
        System.out.printf("%-8s | %-10s\n", "Mode", "Time (ms)");
        System.out.println("----------------------------------------");
        for (var e : time.entrySet()) {
            System.out.printf("%-8d | %-10d\n", e.getKey(), e.getValue());
        }
        System.out.println("===========================================================\n");

        // Print validation from last run
        printResults();
    }

    // Helper to measure execution time
    private long measure(Runnable r) {
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        return (end - start) / 1_000_000;
    }

    // Run a specific mode
    private void runMode(int mode) throws InterruptedException {
        errors.clear();
        CheckerFactory factory = new CheckerFactory(board, errors);

        if (mode == 0) {
            // Sequential execution of main checkers
            Thread rows = factory.createRowsChecker();
            Thread cols = factory.createColsChecker();
            Thread boxes = factory.createBoxesChecker();

            rows.start(); rows.join(); // Run and wait for completion
            cols.start(); cols.join();
            boxes.start(); boxes.join();
        } 
        else if (mode == 3) {
            // Parallel execution of main checkers
            Thread t1 = factory.createRowsChecker();
            Thread t2 = factory.createColsChecker();
            Thread t3 = factory.createBoxesChecker();
            t1.start(); t2.start(); t3.start();
            t1.join(); t2.join(); t3.join();
        } 
        else if (mode == 27) {
            // Parallel execution of all single row/col/box checkers
            List<Thread> threads = new ArrayList<>();
            for (int r = 0; r < N; r++) threads.add(factory.createSingleRowChecker(r));
            for (int c = 0; c < N; c++) threads.add(factory.createSingleColChecker(c));
            for (int b = 0; b < N; b++) threads.add(factory.createSingleBoxChecker(b));
            for (Thread t : threads) t.start();
            for (Thread t : threads) t.join();
        }
    }

    // Print validation results
    private void printResults() {
        if (errors.isEmpty()) {
            System.out.println("VALID");
            return;
        }

        System.out.println("INVALID\n");

        // Separate errors into rows, cols, and boxes
        List<String> rowE = new ArrayList<>();
        List<String> colE = new ArrayList<>();
        List<String> boxE = new ArrayList<>();

        synchronized (errors) {
            for (String e : errors) {
                if (e.startsWith("ROW")) rowE.add(e);
                else if (e.startsWith("COL")) colE.add(e);
                else boxE.add(e);
            }
        }

        rowE.forEach(System.out::println);
        System.out.println("----------------------------------------");
        colE.forEach(System.out::println);
        System.out.println("----------------------------------------");
        boxE.forEach(System.out::println);
    }
}
