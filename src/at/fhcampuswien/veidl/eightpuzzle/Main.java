package at.fhcampuswien.veidl.eightpuzzle;

import java.util.*;

public class Main {
    // best heuristic first
    private static final PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparing(State::getF));
    private static final ArrayList<State> visitedNodes = new ArrayList<>();

    private static final int[][] goalState = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};

    public static void main(String[] args) {
        System.out.println("Generating random start state... Done!");
        handleInput();
    }

    /***
     * Search method
     * Used for executing the search with a given algorithm and max iterations
     * In order to measure the run time of the search, a timer starts before entering the loop
     * Every node positioned at the peek of the queue is a visited node. The priority queue is used to sort all nodes by using a custom comparator.
     * The comparator uses f(n) with f(n) = g(n) + h(n) to determine the position of the node in the queue. A smaller f(n) dictates a higher priority in the queue. Using the
     * poll() function provided by the queue we can take out the element with the highest priority.
     * Visited nodes are those which have the smallest f(n).
     * Expanded nodes on the other hand are those who are opened but then dismissed since f(n) wasn't low enough to pursue
     * The branching factor is calculated by using the expanded nodes and visited nodes and gives us a number between 1-4, stating how many nodes expanded
     * on average during one iteration
     * @param searchAlgorithm
     * Number between 0-2 stating the heuristic in use
     * 0 ... Manhattan Distance
     * 1 ... Misplaced Tiles
     * 2 ... Weighted Combination of both
     * Number of iteration the search should proceed
     */
    private static void search(int[][] input, int searchAlgorithm) {
        // to avoid high runtimes for bad heuristics
        int nodesGenerated = 0;
        visitedNodes.clear();
        State e = new State(input, 0, 0);
        queue.add(e);

        System.out.println("Puzzle to solve:");
        printNode(e);

        if (isNotSolvable(input)) {
            System.out.println("This puzzle is not solvable:");
            printNode(e);
            return;
        } else {
            System.out.println("Puzzle is solvable.. continuing..");
        }

        long startTime = System.nanoTime();
        while (true) {
            ArrayList<State> childNodes;
            // get first node in queue
            State topOfNodes = queue.poll();
            // when taking out from queue, the node has been touched
            visitedNodes.add(topOfNodes);
            // compare grids of current node and goal
            if (Arrays.deepEquals(topOfNodes.grid, goalState)) {
                System.out.println("Execution took: " + (System.nanoTime() - startTime) / 1000000 + "ms");
                calculateMemory();
                System.out.println("Visited Notes: " + (visitedNodes.size() - 1));
                System.out.println("Nodes Generated: " + (nodesGenerated));
                System.out.println("Branching Factor: " + ((((double) nodesGenerated) / (double) (visitedNodes.size() - 1))));
                System.out.println("Puzzle solved in " + topOfNodes.g + " steps");
                printNode(topOfNodes);
                break;
            } else {
                // generate possible childs
                childNodes = childNodes(topOfNodes, searchAlgorithm);
                for (State child : childNodes) {
                    // check if child has been visited before
                    if (!containsInChild(child)) {
                        nodesGenerated++;
                        queue.add(child);
                    }
                }
            }

        }
    }

    /***
     * Print method
     * Used for printing any given state on the console
     * @param state
     * State object which should be printed
     */
    private static void printNode(State state) {
        for (int i = 0; i < state.grid.length; i++) {
            for (int j = 0; j < state.grid.length; j++) {
                System.out.print(state.grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /***
     * Contains child method
     * Used for checking if a given parent state contains any child from visited nodes
     * @param parent
     * Given parent state used for comparing with the child from visited nodes
     * @return
     * Whether a child exits in visited nodes
     */
    private static boolean containsInChild(State parent) {
        int[][] childGrid = parent.grid;
        for (State child : visitedNodes) {
            if (Arrays.deepEquals(child.grid, childGrid))
                return true;
        }
        return false;
    }

    /***
     * Child nodes method
     * Used for calculating child nodes of a given parent state and search algorithm
     * Find out x and y of the empty space, in our case the empty space is marked with  0
     * Before making any move, check if the given move is allowed. Meaning if the move is still in bound of the given array
     * then and only then make the move accordingly
     * Allowed moves are: Up, Down, Left and Right. If a move is valid then depending on which move is executed the tile is replaced with the empty space
     * After making the move, the child is added to the childrenStates Arraylist
     * @param parentState
     * Given parent state which is used to calculated the children of
     * @param searchAlgorithm
     * Given search algorithm (0, 1, 2)
     * @return
     * ArrayList of childNodes of the given parent
     */
    private static ArrayList<State> childNodes(State parentState, int searchAlgorithm) {
        ArrayList<State> childrenStates = new ArrayList<>();
        int[][] grid = parentState.grid;
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
            int[][] workingGrid = copyGrid(parentState.getGrid());
            int top = workingGrid[x - 1][y];
            workingGrid[x - 1][y] = workingGrid[x][y];
            workingGrid[x][y] = top;
            childrenStates.add(new State(workingGrid, parentState.g + 1, heuristic(searchAlgorithm, workingGrid)));
        }
        // down
        if (x + 1 <= grid.length - 1) {
            int[][] workingGrid = copyGrid(parentState.getGrid());
            int down = workingGrid[x + 1][y];
            workingGrid[x + 1][y] = workingGrid[x][y];
            workingGrid[x][y] = down;

            childrenStates.add(new State(workingGrid, parentState.g + 1, heuristic(searchAlgorithm, workingGrid)));
        }
        // left
        if (y - 1 >= 0) {
            int[][] workingGrid = copyGrid(parentState.getGrid());
            int left = workingGrid[x][y - 1];
            workingGrid[x][y - 1] = workingGrid[x][y];
            workingGrid[x][y] = left;

            childrenStates.add(new State(workingGrid, parentState.g + 1, heuristic(searchAlgorithm, workingGrid)));
        }
        // right
        if (y + 1 <= grid.length - 1) {
            int[][] workingGrid = copyGrid(parentState.getGrid());
            int right = workingGrid[x][y + 1];
            workingGrid[x][y + 1] = workingGrid[x][y];
            workingGrid[x][y] = right;
            childrenStates.add(new State(workingGrid, parentState.g + 1, heuristic(searchAlgorithm, workingGrid)));
        }

        return childrenStates;
    }

    /***
     * Heuristic method
     * Used for calculating the heuristic of a given working grid
     * Depending on the selection of the search algorithm, the heuristic is calculated accordingly
     * The third option is a weighted heuristic combining misplaced tiles and manhattan distance. Since the manhattan distance performs better than the number of
     * misplaced tiles, the weight is higher. We choose the combination 97% - Manhattan and 3% - Misplaced. Any kind of small deviation of these numbers doesn't
     * change the outcome by a big factor. Only changing the weights drastically f.e 40% - 60% would result into a better/worse solution. By observation having a bigger weight
     * on misplaced tiles results into a longer and sub optimal solution
     * @param searchAlgorithm
     * Given search algorithm (0, 1, 2)
     * @param workingGrid
     * Given working grid of child state
     * @return
     * returns h(n) of f(n) = g(n) + h(n)
     */
    private static int heuristic(int searchAlgorithm, int[][] workingGrid) {
        int h = 0;

        if (searchAlgorithm == 0) {
            h = calcManhattanHeuristic(workingGrid);
        } else if (searchAlgorithm == 1) {
            h = calculateMisplacedHeuristic(workingGrid);
        } else {
            h = (int) (calcManhattanHeuristic(workingGrid) * 0.97 + calculateMisplacedHeuristic(workingGrid) * 0.03);
        }

        return h;
    }

    /***
     * Copy method
     * Used for copying the content of a given grid into a new one
     * @param grid
     * Given Grid
     * @return
     * Returns copied grid
     */
    private static int[][] copyGrid(int[][] grid) {
        int[][] newGrid = new int[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, newGrid[i], 0, grid.length);
        }
        return newGrid;
    }

    /***
     * Calculate misplaced heuristic
     * Used for calculating the heuristic using the number of misplaced tiles in the given grid
     * Every tile which is not in its goal position is counted as a misplaced tile. That number increased if the given grid
     * is further away from the goal state and decreased if the given grid is getting closer to the goal state
     * @param grid
     * Given grid
     * @return
     * Returns h(n) of misplaced tiles
     */
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

    /***
     * Calculate manhattan heuristic
     * Used for calculating the heuristic using the manhattan distance in the given grid
     * Manhattan distance formula for given points
     * Point_1: x1, y1
     * Point_2: x2, y2
     * m = |x1 - x2| + |y1 - y2|
     * The heuristic is a sum of the manhattan distance of every tile in the given grid. A smaller sum indicates that the grid
     * is closer to the goal state. A bigger sum indicates that the grid is further away from the goal state
     * @param grid
     * Given grid, which is used to calculate the heuristic of
     * @return
     * Returns h(n) of manhattan distance
     */
    private static int calcManhattanHeuristic(int[][] grid) {
        int h = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                int target = grid[i][j];
                for (int k = 0; k < grid.length; k++) {
                    for (int l = 0; l < grid.length; l++) {
                        if (goalState[k][l] == target) {
                            h += Math.abs(k - i) + Math.abs(l - j);
                        }
                    }
                }
            }
        }
        return h;
    }

    /***
     * Is solvable method
     *
     * https://www.geeksforgeeks.org/check-instance-8-puzzle-solvable/
     *
     * Used for calculating the number of inversions of the given grid
     * If the number of inversions is odd, the grid is not solvable
     * If the number of inversion is even, the grid is solvable
     * An inversion occurs when a number at position 0 of the list, is greater than any following number in the list
     * Example list: 8,1,2,6,4,7,5,3
     * The number 8 is positioned at 0, the number of inversions is 7
     * The number 6 is positioned at 3, the number of inversions is 3
     * The number 7 is positioned at 5, the number of inversions is 2
     * The number 5 is positioned at 6, the number of inversions is 1
     * @param grid
     * Given grid for which the inversions are calculated
     * @return whether its solvable or not
     */
    private static boolean isNotSolvable(int[][] grid) {
        int inversionCounter = 0;

        for (int i = 0; i < 3 - 1; i++)
            for (int j = i + 1; j < 3; j++)
                if (grid[j][i] > 0 &&
                        grid[j][i] > grid[i][j])
                    inversionCounter++;

        return inversionCounter % 2 == 0;
    }

    /**
     * shuffles 2D array
     * https://stackoverflow.com/questions/20190110/2d-int-array-shuffle
     *
     * @param a
     * @return
     */
    static int[][] shuffle(int[][] a) {
        Random random = new Random();

        for (int i = a.length - 1; i > 0; i--) {
            for (int j = a[i].length - 1; j > 0; j--) {
                int m = random.nextInt(i + 1);
                int n = random.nextInt(j + 1);

                int temp = a[i][j];
                a[i][j] = a[m][n];
                a[m][n] = temp;
            }
        }
        return a;
    }

    /**
     * handles user interaction
     */
    private static void handleInput() {
        int[][] randomStartState = shuffle(copyGrid(goalState));
        while (true) {
            System.out.println("######## 8 - Puzzle Solver Menu ########");
            System.out.println("0 ... Search with Manhatten");
            System.out.println("1 ... Search with Euklidian");
            System.out.println("2 ... Search with Weighted");
            System.out.println("3 ... generate random new Puzzle");
            System.out.println("4 ... enter custom puzzle");
            System.out.println("########################################");

            Scanner scanner = new Scanner(System.in);

            try {
                int i = scanner.nextInt();

                switch (i) {
                    case 0:
                        search(randomStartState, 0);
                        break;
                    case 1:
                        search(randomStartState, 1);
                        break;
                    case 2:
                        search(randomStartState, 2);
                        break;
                    case 3:
                        randomStartState = shuffle(copyGrid(goalState));
                        System.out.println("Generation of new random state is done. Enter a Search algorithm");
                        break;
                    case 4:
                        randomStartState = handleCustomInput();
                    default:
                        // try again
                }
            } catch (Exception e) {
                System.out.println("Please provide a valid input.\n");
            }
        }
    }

    /**
     * Define your custom puzzle
     * @return custom 8-puzzle
     */
    private static int[][] handleCustomInput() {
        System.out.println("Enter new Puzzle like: '012345678'");
        Scanner scanner = new Scanner(System.in);

        String s = scanner.nextLine();

        int[][] temp = new int[3][3];


        if (s.length() == 9) {
            String[] split = s.split("");

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // find the appropriate index for a 3x3 puzzle
                    temp[i][j] = Integer.parseInt(split[3 * i + j]);
                }
            }
        } else {
            System.out.println("You have to enter 9 values only.");
        }

        printNode(new State(temp, 0, 0));

        return temp;
    }

    private static void calculateMemory() {
        Runtime rt = Runtime.getRuntime();

        rt.gc();
        long memory = rt.totalMemory() - rt.freeMemory();

        System.out.println("Used memory in bytes: " + memory);
        System.out.println("Used memory in Megabytes: " + memory / (1024L * 1024L));
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

        /***
         * Get f method
         * Used for calculating f(n) = g(n) + h(n)
         * @return
         * Returns f(n)
         */
        private int getF() {
            return this.g + this.h;
        }

        /***
         * Get grid method
         * Used for return the grid of this state
         * @return
         * Returns grid
         */
        public int[][] getGrid() {
            return grid;
        }
    }
}
