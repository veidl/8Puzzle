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

//    private static int[][] ez = {
//            {1, 2, 3},
//            {4, 0, 6},
//            {7, 5, 8}};
//
//    private static int[][] goalState = {
//            {1, 2, 3},
//            {0, 4, 6},
//            {7, 5, 8}};

    private static int[][] ez = {
            {1, 2, 3},
            {0, 4, 6},
            {7, 5, 8}};

    private static int[][] goalState = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}};


    public static void main(String[] args) {

        int initH = calculateHeuristic(ez);
        int g = 0;

        State initState = new State(ez, g, initH);
//        State best = move(initState, g);
//        printState(best.grid);
        while (!FINISHED) {
            g++;
            State best = move(initState, g);
            if (g == 1) {
//                printState(best.grid);
                FINISHED = true;
            }
        }
    }


    private static State move(State state, int g) {
        int[][] grid = state.grid;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[y][x] == 0) {

                    //Save current state somewhere after moving the xero
                    State left = moveLeft(grid, x, y, g);
                    printState(grid);
                    addToQueue(left);

                    State right = moveRight(grid, x, y, g);
                    printState(grid);
                    addToQueue(right);

                    State bottom = moveBottom(grid, x, y, g);
                    printState(grid);
                    addToQueue(bottom);

                    State top = moveTop(grid, x, y, g);
                    printState(grid);
                    addToQueue(top);
                    break;
//                    queue.forEach(state1 -> System.out.println(state));


                }
            }
        }
        return queue.peek(); //return the best move in this q
    }


    private static State moveLeft(int[][] grid, int x, int y, int g) {
        if (x - 1 < 0)
            return null;

        int left = grid[y][x - 1];
        grid[y][x - 1] = grid[y][x];
        grid[y][x] = left;

        return new State(grid, g, calculateHeuristic(grid));
    }

    private static State moveRight(int[][] grid, int x, int y, int g) {
        if (x + 1 > 2)
            return null;

        int right = grid[y][x + 1];
        grid[y][x + 1] = grid[y][x];
        grid[y][x] = right;

//        System.out.println(calculateHeuristic(grid));
        return new State(grid, g, calculateHeuristic(grid));
    }

    private static State moveTop(int[][] grid, int x, int y, int g) {
        if (y - 1 < 0)
            return null;

        int top = grid[y - 1][x];
        grid[y - 1][x] = grid[y][x];
        grid[y][x] = top;

        return new State(grid, g, calculateHeuristic(grid));
    }

    private static State moveBottom(int[][] grid, int x, int y, int g) {
        if (y + 1 > 2)
            return null;

        int bottom = grid[y + 1][x];
        grid[y + 1][x] = grid[y][x];
        grid[y][x] = bottom;

        return new State(grid, g, calculateHeuristic(grid));
    }


    private static boolean isOutOfBound(int x, int y) {
        if (x - 1 < 0 || x + 1 > 2 || y + 1 > 2 || y - 1 < 0) {
            return true;
        }
        return false;
    }

    private static void addToQueue(State state) {
        if (state != null) {
//            printState(state.grid);
            queue.add(state);
        }
    }


    private static void printState(int[][] currGrid) {
        for (int i = 0; i < currGrid.length; i++) {
            for (int j = 0; j < currGrid.length; j++) {
                System.out.print(currGrid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    private static int calculateHeuristic(int[][] grid) {
        int misplaced = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] != goalState[i][j]) {
                    misplaced++;
                }
            }
        }
        return misplaced;
    }


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
