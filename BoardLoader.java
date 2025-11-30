package lab.pkg9;

import java.io.*;

public class BoardLoader {

    // Load a 9x9 Sudoku board from CSV file
    public static int[][] loadCsv(String path) throws Exception {
        int[][] board = new int[9][9];
        BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        int r = 0;

        while ((line = br.readLine()) != null && r < 9) {
            String[] parts = line.split(",");
            if (parts.length != 9)
                throw new Exception("CSV rows must have 9 numbers.");

            for (int c = 0; c < 9; c++)
                board[r][c] = Integer.parseInt(parts[c].trim());

            r++;
        }

        if (r != 9)
            throw new Exception("CSV must have 9 rows.");

        br.close();
        return board;
    }
}
