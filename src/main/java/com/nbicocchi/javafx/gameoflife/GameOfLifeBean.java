package com.nbicocchi.javafx.gameoflife;

import java.util.random.RandomGenerator;

public class GameOfLifeBean {
    int rows;
    int cols;
    int[][] grid;

    public GameOfLifeBean(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols];
    }

    public int getRows() {
        return rows;
    }


    public int getCols() {
        return cols;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void init(RandomGenerator rnd) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = rnd.nextInt(2);
            }
        }
    }

    public void evolve() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int n = countNeighbors(r, c);
                if (n > 3 || n < 2) {
                    grid[r][c] = 0;
                } else if (n == 3) {
                    grid[r][c] = 1;
                }
            }
        }
    }

    private int countNeighbors(int x, int y) {
        int startX = Math.max(x - 1, 0);
        int startY = Math.max(y - 1, 0);
        int endX = Math.min(x + 1, cols - 1);
        int endY = Math.min(y + 1, rows - 1);

        int sum = -grid[x][y];
        for (int r = startY; r <= endY; r++) {
            for (int c = startX; c <= endX; c++) {
                sum += grid[r][c];
            }
        }
        return sum;
    }
}
