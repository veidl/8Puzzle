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

    private static int[][] ez = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}};

    private static int[][] goalState = {
            {4, 5, 6},
            {1, 2, 3},
            {7, 8, 0}};

    public static void main(String[] args) {

        int initH = calculateHeuristic(ez);
        int g = 0;

        State initState = new State(ez, g, initH);

//        g++;
//        State bestMove = move(initState, g);
        while (!FINISHED) {
            g++;
            initState = move(initState, g);

            if (g == 20)
                FINISHED = true;
//            queue.forEach(state -> System.out.println(state.toString()));

        }
    }


    private static State move(State state, int g) {
        int[][] grid = state.grid;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    int x = i;
                    int y = j;
                    //find tiles which can move to x y --> 8,6
                    //move one calcHeuristic --> then decide which move is better --> return state with lower f
                }
            }
        }

        return queue.peek(); //The best move in this graph depth
    }

    private static void addToQueue(State state) {
        if (state != null) {
            printState(state.grid);
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
