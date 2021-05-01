package at.fhcampuswien.veidl.eightpuzzle.Etwas;

import at.fhcampuswien.veidl.eightpuzzle.Main;

import java.util.Comparator;
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

        this.startingNode = new Node(this.randomPuzzle, 0, calcHeuristic(this.randomPuzzle));
        this.queue = new PriorityQueue<>(Comparator.comparing(Node::calcF));
    }

    public void run() {
        int[] emptyCor = getEmptySpaceCor(startingNode);
        int g = 0;

        moveLeft(startingNode, emptyCor[0], emptyCor[1], g);
        resetEnv();

        moveRight(startingNode, emptyCor[0], emptyCor[1], g);
        resetEnv();

        moveTop(startingNode, emptyCor[0], emptyCor[1], g);
        resetEnv();

        moveBottom(startingNode, emptyCor[0], emptyCor[1], g);
        resetEnv();


        System.out.println("p");
        printNode(this.queue.peek().getNodeGrid());

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
        this.startingNode = new Node(this.randomPuzzle, 0, calcHeuristic(this.randomPuzzle));
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
        int[][] grid = startingNode.getNodeGrid();
        if (x - 1 < 0)
            return;

        int left = grid[y][x - 1];
        grid[y][x - 1] = grid[y][x];
        grid[y][x] = left;
        System.out.println(calcHeuristic(grid));
        Node node = new Node(grid, g, calcHeuristic(grid));
        printNode(node.getNodeGrid());
        addToQueue(node);
    }

    private void moveRight(Node startingNode, int x, int y, int g) {
        int[][] grid = startingNode.getNodeGrid();

        if (x + 1 > 2)
            return;

        int right = grid[y][x + 1];
        grid[y][x + 1] = grid[y][x];
        grid[y][x] = right;

        System.out.println(calcHeuristic(grid));
        Node node = new Node(grid, g, calcHeuristic(grid));
        printNode(node.getNodeGrid());
        addToQueue(node);
    }

    private void moveTop(Node startingNode, int x, int y, int g) {
        int[][] grid = startingNode.getNodeGrid();
        if (y - 1 < 0)
            return;

        int top = grid[y - 1][x];
        grid[y - 1][x] = grid[y][x];
        grid[y][x] = top;
        System.out.println(calcHeuristic(grid));
        printNode(grid);
        Node node = new Node(grid, g, calcHeuristic(grid));
        addToQueue(node);
    }

    private void moveBottom(Node startingNode, int x, int y, int g) {
        int[][] grid = startingNode.getNodeGrid();
        if (y + 1 > 2)
            return;

        int bottom = grid[y + 1][x];
        grid[y + 1][x] = grid[y][x];
        grid[y][x] = bottom;
        System.out.println(calcHeuristic(grid));
        printNode(grid);
        Node node = new Node(grid, g, calcHeuristic(grid));
        addToQueue(node);
    }


    private int calcHeuristic(int[][] nodeGrid) {
        int misplacedCount = 0;
        for (int y = 0; y < nodeGrid.length; y++) {
            for (int x = 0; x < nodeGrid.length; x++) {
                if (this.goalPuzzle[y][x] != nodeGrid[y][x])
                    misplacedCount++;
            }
        }
        return misplacedCount;
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
