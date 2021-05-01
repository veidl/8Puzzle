package at.fhcampuswien.veidl.eightpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Main {


    // sorted by F(n)
    private static PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparing(State::getF));
    private static ArrayList<State> visitedNodes = new ArrayList<>();

    private static boolean FINISHED = false;

    private static int[][] ez = {{1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}};

    private static int[][] goalState = {{4, 5, 6}, {1, 2, 3}, {7, 8, 0}};

    public static void main(String[] args) {
        // write your code here

        queue.add(new State(ez, 0, 0));

        int g = 0;

        while (!FINISHED) {
            move(queue.peek(), g);
            g++;

            if (g > 10)
                FINISHED = true;

            queue.forEach(state -> System.out.println(state.toString()));

        }
    }


    private static void move(State state, int g) {
        int[][] grid = state.grid;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    int x = i;
                    int y = j;

                    State left = moveLeft(grid, x, y, g);
//                    State right = moveRight(grid, x, y, g);
//                    State up = moveUp(grid, x, y, g);
//                    State down = moveDown(grid, x, y, g);


                    addToQueue(left);
//                    addToQueue(right);
//                    addToQueue(up);
//                    addToQueue(down);

                    //find tiles which can move to x y --> 8,6
                    //move one calcHeuristic --> then decide which move is better --> return state with lower f


                }
            }
        }
    }

    private static void addToQueue(State state) {
        if (state != null) {
            queue.add(state);
        }
    }

    private static State moveLeft(int[][] grid, int x, int y, int g) {
        //check outOfBound
        if (!checkOutOfBounds(grid, x, y)) {
            return null;
        }
//        System.out.println();
        // swapped left
        grid[x][y] = grid[x - 1][y];
        grid[x - 1][y] = 0;

        return new State(grid, calculateHeuristic(grid), g);
    }

    private static State moveRight(int[][] grid, int x, int y, int g) {
        //check outOfBound
        if (checkOutOfBounds(grid, x, y)) {
            return null;
        }

        // swapped left
        grid[x][y] = grid[x + 1][y];
        grid[x + 1][y] = 0;

        return new State(grid, calculateHeuristic(grid), g);
    }

    private static State moveUp(int[][] grid, int x, int y, int g) {
        //check outOfBound
        if (checkOutOfBounds(grid, x, y)) {
            return null;
        }

        // swapped left
        grid[x][y] = grid[x][y + 1];
        grid[x][y + 1] = 0;

        return new State(grid, calculateHeuristic(grid), g);
    }

    private static State moveDown(int[][] grid, int x, int y, int g) {
        //check outOfBound
        if (checkOutOfBounds(grid, x, y)) {
            return null;
        }

        // swapped left
        grid[x][y] = grid[x][y - 1];
        grid[x][y - 1] = 0;

        return new State(grid, calculateHeuristic(grid), g);
    }

    private static boolean checkOutOfBounds(int[][] grid, int x, int y) {
        return (x + 1 < grid.length - 1 || y + 1 < grid.length - 1 || x - 1 > 0 || y - 1 > 0);
    }

    /**
     * misplaced tiles
     *
     * @param
     * @return
     */
    private static int calculateHeuristic(int[][] grid) {

        // current position
        int misplaced = 0;
        // check every tile for mismatch with goalState
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] != goalState[i][j]) {
                    misplaced++;
                }
            }
        }

        return misplaced;
    }


    // grid + g + h
    private static class State {
        private int[][] grid;
        private int g;
        private int h;

        public State(int[][] grid, int g, int h) {
            this.grid = grid;
            this.g = g;
            this.h = h;
        }

        private int getF() {
            return this.g + this.h;
        }

        public int[][] getGrid() {
            return grid;
        }

        public void setGrid(int[][] grid) {
            this.grid = grid;
        }

        public int getG() {
            return g;
        }

        public void setG(int g) {
            this.g = g;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        @Override
        public String toString() {
            return "State{" +
                    "grid=" + Arrays.toString(grid) +
                    ", g=" + g +
                    ", h=" + h +
                    '}';
        }
    }

}
