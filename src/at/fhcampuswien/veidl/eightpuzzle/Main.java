package at.fhcampuswien.veidl.eightpuzzle;

import java.util.*;

public class Main {
    private static final PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparing(State::getF));
    private static final ArrayList<State> visitedNodes = new ArrayList<>();

//        private static final int[][] initialState = {{1, 2, 3}, {0, 4, 6}, {7, 5, 8}};
//    private static final int[][] goalState = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};

//    private static final int[][] initialState = {{1, 2, 3}, {8, 0, 4}, {7, 6, 5}};
//    private static final int[][] goalState = {{2, 8, 1}, {4, 6, 3}, {0, 7, 5}};


    private static final int[][] initialState = {{8, 6, 7}, {2, 5, 4}, {3, 0, 1}};
    private static final int[][] goalState = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};

    public static void main(String[] args) {
        if (!isSolvable(initialState)) {
            throw new IllegalArgumentException("Not solvable");
        }
        queue.add(new State(initialState, 0, 0));

        System.out.println("Search with Manhatten Distance");
        search(0, 10000);

        queue.clear();
        queue.add(new State(initialState, 0, 0));
        System.out.println("Search with Misplaced Tiles");
        search(1, 10000);

        queue.clear();
        queue.add(new State(initialState, 0, 0));
        System.out.println("Search with Weighted Heuristic");
        search(2, 30000);
    }

    private static void search(int searchAlgorithm, int maxIteration) {
        int iteration = 0;
        int nodesGenerated = 0;
        visitedNodes.clear();

        long startTime = System.nanoTime();
        while (iteration <= maxIteration) {
            ArrayList<State> childNodes;
            State topOfNodes = queue.poll();
//            System.out.println("getting topNode: with G=" + topOfNodes.g + " H=" + topOfNodes.h + " F=" + topOfNodes.getF());
//            printNode(topOfNodes);
            visitedNodes.add(topOfNodes);
            if (Arrays.deepEquals(topOfNodes.grid, goalState)) {
                System.out.println("Execution took: " + (System.nanoTime() - startTime) / 1000000 + "ms");
                System.out.println("Visited Notes: " + (visitedNodes.size() - 1));
                System.out.println("Nodes Generated: " + (nodesGenerated));
                System.out.println("Branching Factor: " + ((((double) nodesGenerated) / (double) (visitedNodes.size() - 1))));
                System.out.println("Puzzle solved in " + topOfNodes.g + " steps");
                printNode(topOfNodes);
                break;
            } else {
                childNodes = childNodes(topOfNodes, searchAlgorithm);
                for (State child : childNodes) {
                    if (!containsinChild(child)) {
                        nodesGenerated++;
                        queue.add(child);
                    }
                }
            }
            iteration++;
        }
    }

    private static void printNode(State state) {
        for (int i = 0; i < state.grid.length; i++) {
            for (int j = 0; j < state.grid.length; j++) {
                System.out.print(state.grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static boolean containsinChild(State parent) {
        int[][] childGrid = parent.grid;
        for (State child : visitedNodes) {
            if (Arrays.deepEquals(child.grid, childGrid))
                return true;
        }
        return false;
    }

    private static ArrayList<State> childNodes(State currentState, int searchAlgorithm) {
        ArrayList<State> toReturn = new ArrayList<>();
        int[][] grid = currentState.grid;
        int x = -1;
        int y = -1;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    x = i;
                    y = j;
                }
            }
        }
        // up
        if (x - 1 >= 0) {
            int[][] workingGrid = copyGrid(currentState.getGrid());
            int top = workingGrid[x - 1][y];
            workingGrid[x - 1][y] = workingGrid[x][y];
            workingGrid[x][y] = top;
            toReturn.add(new State(workingGrid, currentState.g + 1, heuristic(searchAlgorithm, workingGrid)));
        }
        // down
        if (x + 1 <= grid.length - 1) {
            int[][] workingGrid = copyGrid(currentState.getGrid());
            int down = workingGrid[x + 1][y];
            workingGrid[x + 1][y] = workingGrid[x][y];
            workingGrid[x][y] = down;

            toReturn.add(new State(workingGrid, currentState.g + 1, heuristic(searchAlgorithm, workingGrid)));
        }
        // left
        if (y - 1 >= 0) {
            int[][] workingGrid = copyGrid(currentState.getGrid());
            int left = workingGrid[x][y - 1];
            workingGrid[x][y - 1] = workingGrid[x][y];
            workingGrid[x][y] = left;

            toReturn.add(new State(workingGrid, currentState.g + 1, heuristic(searchAlgorithm, workingGrid)));
        }
        // right
        if (y + 1 <= grid.length - 1) {
            int[][] workingGrid = copyGrid(currentState.getGrid());
            int right = workingGrid[x][y + 1];
            workingGrid[x][y + 1] = workingGrid[x][y];
            workingGrid[x][y] = right;
            toReturn.add(new State(workingGrid, currentState.g + 1, heuristic(searchAlgorithm, workingGrid)));
        }

        return toReturn;
    }

    private static int heuristic(int searchAlgorithm, int[][] workingGrid) {
        int h = 0;

        if (searchAlgorithm == 0) {
            h = calculateMisplacedHeuristic(workingGrid);
        } else if (searchAlgorithm == 1) {
            h = calculateMisplacedHeuristic(workingGrid);
        } else {
            h = (int) (calcManhattanHeuristic(workingGrid) * 0.97 + calculateMisplacedHeuristic(workingGrid) * 0.03);
        }

        return h;
    }

    private static int[][] copyGrid(int[][] grid) {
        int[][] newGrid = new int[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, newGrid[i], 0, grid.length);
        }
        return newGrid;
    }

    private static int calculateMisplacedHeuristic(int[][] grid) {
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

    private static int calcManhattanHeuristic(int[][] nodeGrid) {
        int h = 0;
        for (int i = 0; i < nodeGrid.length; i++) {
            for (int j = 0; j < nodeGrid.length; j++) {
                int target = nodeGrid[i][j];
                for (int k = 0; k < nodeGrid.length; k++) {
                    for (int l = 0; l < nodeGrid.length; l++) {
                        if (goalState[k][l] == target) {
                            h += Math.abs(k - i) + Math.abs(l - j);
                        }
                    }
                }
            }
        }
        return h;
    }

    private static boolean isSolvable(int[][] grid) {
        int inversionCounter = 0;
        List<Integer> invList = new ArrayList<>();

        for (int i = 0; i < initialState.length; i++) {
            for (int j = 0; j < initialState.length; j++) {
                invList.add(grid[i][j]);
            }
        }
        for (int i = 0; i < invList.size(); i++) {
            for (Integer integer : invList) {
                if (invList.get(i) > 0 && integer > 0 && invList.get(i) > integer) {
                    inversionCounter++;
                }
            }
        }
        return inversionCounter % 2 == 0;
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
    }
}
