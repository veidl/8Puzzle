package at.fhcampuswien.veidl.eightpuzzle.Etwas;

public class Node {
    int[][] nodeGrid;
    int g;
    int h;

    public Node(int[][] nodeGrid, int g, int h) {
        this.nodeGrid = nodeGrid;
        this.g = g;
        this.h = h;
    }

    public void setNodeGrid(int[][] nodeGrid) {
        this.nodeGrid = nodeGrid;
    }

    public int[][] getNodeGrid() {
        return nodeGrid;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public int calcF() {
        return g + h;
    }


}
