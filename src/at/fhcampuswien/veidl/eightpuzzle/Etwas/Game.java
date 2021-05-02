package at.fhcampuswien.veidl.eightpuzzle.Etwas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Game {

    private int[][] randomPuzzle;

    private final int[][] goalPuzzle;
    private Node startingNode;
    private final PriorityQueue<Node> queue;

    public Game() {
        this.randomPuzzle = new int[][]{
                {1, 2, 3},
                {0, 4, 6},
                {7, 5, 8}};
//        this.randomPuzzle = new int[][]{
//                {1, 2, 3},
//                {4, 0, 6},
//                {7, 5, 8}};
//        this.randomPuzzle = new int[][]{
//                {1, 2, 3},
//                {4, 5, 6},
//                {7, 0, 8}};
        this.goalPuzzle = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}};

        this.startingNode = new Node(this.randomPuzzle, 0, calcMisplacedHeuristic(this.randomPuzzle));
        this.queue = new PriorityQueue<>(Comparator.comparing(Node::calcF));
    }

    public void run() {
        //Change rand puzzle -> to peek of queue
        //clear queue
        //move stuff and fill queue
        //increase g
        int g = 0;
        if (isSolvable()) {
            while (g < 3) { //find finishing condition
                int[] emptyCor = getEmptySpaceCor(startingNode);
                //g = 1
                moveLeft(startingNode, emptyCor[0], emptyCor[1], g);
                moveRight(startingNode, emptyCor[0], emptyCor[1], g);
                moveTop(startingNode, emptyCor[0], emptyCor[1], g);
                moveBottom(startingNode, emptyCor[0], emptyCor[1], g);

                System.out.println("p");
                printNode(this.queue.peek().getNodeGrid());
                startingNode = this.queue.peek();
                this.queue.clear();
                g++;
            }
        }

    }

    private boolean isSolvable() {
        int inversionCounter = 0;
        List<Integer> invList = getListOfArray();

        for (int i = 0; i < invList.size(); i++) {
            for (int j = 0; j < invList.size(); j++) {
                if (invList.get(i) > 0 && invList.get(j) > 0 && invList.get(i) > invList.get(j))
                    inversionCounter++;
            }
        }
        return inversionCounter % 2 == 0;
    }

    private List<Integer> getListOfArray() {
        List<Integer> invList = new ArrayList<>();
        for (int i = 0; i < randomPuzzle.length; i++) {
            for (int j = 0; j < randomPuzzle.length; j++) {
                invList.add(randomPuzzle[i][j]);
            }
        }
        return invList;
    }

    private int[][] copyGrid(int[][] grid) {
        int[][] newGrid = new int[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                newGrid[i][j] = grid[i][j];
            }
        }
        return newGrid;
    }

    public void resetEnv() { //Dumme scheiß methode
        this.randomPuzzle = new int[][]{
                {1, 2, 3},
                {0, 4, 6},
                {7, 5, 8}};
//        this.randomPuzzle = new int[][]{
//                {1, 2, 3},
//                {4, 0, 6},
//                {7, 5, 8}};
//        this.randomPuzzle = new int[][]{
//                {1, 2, 3},
//                {4, 5, 6},
//                {7, 0, 8}};
        this.startingNode = new Node(this.randomPuzzle, 0, calcMisplacedHeuristic(this.randomPuzzle));
    }

    public void addToQueue(Node node) {
        if (node != null)
            this.queue.add(node);
    }

    public int[] getEmptySpaceCor(Node node) {
        int[][] grid = node.getNodeGrid();
        int[] cor = new int[2];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[y][x] == 0) {
                    cor[0] = x;
                    cor[1] = y;
                }
            }
        }
        return cor;
    }

    private void moveLeft(Node startingNode, int x, int y, int g) {
        int[][] grid = copyGrid(startingNode.getNodeGrid());
        if (x - 1 < 0)
            return;

        int left = grid[y][x - 1];
        grid[y][x - 1] = grid[y][x];
        grid[y][x] = left;
        System.out.println(calcMisplacedHeuristic(grid));
        Node node = new Node(grid, g, calcMisplacedHeuristic(grid));
        printNode(node.getNodeGrid());
        addToQueue(node);
    }

    private void moveRight(Node startingNode, int x, int y, int g) {
        int[][] grid = copyGrid(startingNode.getNodeGrid());

        if (x + 1 > 2)
            return;

        int right = grid[y][x + 1];
        grid[y][x + 1] = grid[y][x];
        grid[y][x] = right;

        System.out.println(calcMisplacedHeuristic(grid));
        Node node = new Node(grid, g, calcMisplacedHeuristic(grid));
        printNode(node.getNodeGrid());
        addToQueue(node);
    }

    private void moveTop(Node startingNode, int x, int y, int g) {
        int[][] grid = copyGrid(startingNode.getNodeGrid());
        if (y - 1 < 0)
            return;

        int top = grid[y - 1][x];
        grid[y - 1][x] = grid[y][x];
        grid[y][x] = top;
        System.out.println(calcMisplacedHeuristic(grid));
        printNode(grid);
        Node node = new Node(grid, g, calcMisplacedHeuristic(grid));
        addToQueue(node);
    }

    private void moveBottom(Node startingNode, int x, int y, int g) {
        int[][] grid = copyGrid(startingNode.getNodeGrid());
        if (y + 1 > 2)
            return;

        int bottom = grid[y + 1][x];
        grid[y + 1][x] = grid[y][x];
        grid[y][x] = bottom;
        System.out.println(calcMisplacedHeuristic(grid));
        printNode(grid);
        Node node = new Node(grid, g, calcMisplacedHeuristic(grid));
        addToQueue(node);
    }


    private int calcMisplacedHeuristic(int[][] nodeGrid) {
        int misplacedCount = 0;
        for (int y = 0; y < nodeGrid.length; y++) {
            for (int x = 0; x < nodeGrid.length; x++) {
                if (this.goalPuzzle[y][x] != nodeGrid[y][x])
                    misplacedCount++;
            }
        }
        return misplacedCount;
    }

    private int calcManhattanHeuristic(int[][] nodeGrid) {
        int distance = 0;
        for (int y = 0; y < nodeGrid.length; y++) {
            for (int x = 0; x < nodeGrid.length; x++) {

                //coordinates of current position in goalState
                int p2X = findPointInGoalPuzzle(nodeGrid[y][x]).get(1);
                int p2Y = findPointInGoalPuzzle(nodeGrid[y][x]).get(0);

                distance += Math.abs(x - p2X) + Math.abs(y - p2Y);

            }
        }
        return distance;
    }

    private List<Integer> findPointInGoalPuzzle(int point) {
        List<Integer> cor = new ArrayList<>();
        for (int i = 0; i < goalPuzzle.length; i++) {
            for (int j = 0; j < goalPuzzle.length; j++) {
                if (this.goalPuzzle[i][j] == point)
                    cor.add(i);
                cor.add(j);
                return cor;
            }
        }
        return cor;
    }

    private void printNode(int[][] currGrid) {
        for (int i = 0; i < currGrid.length; i++) {
            for (int j = 0; j < currGrid.length; j++) {
                System.out.print(currGrid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
