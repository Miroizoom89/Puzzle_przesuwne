package Slider;

import sac.graph.AStar;
import sac.graph.GraphSearchAlgorithm;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Slider extends GraphStateImpl {

    public static final int n = 3;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;
    private static String[] MOVE_NAMES = new String[] { "L", "R", "U", "D"};

    private byte[][] board = null;
    private int zeroI;
    private int zeroJ;

    public Slider() {
        board = new byte[n][n];
        byte k = 0;
        for(int i = 0; i<n; i++)
            for(int j=0;j<n;j++)
                board[i][j] = k++;
        zeroI = 0;
        zeroJ = 0;
    }

    public Slider(Slider toCopy) {
        board = new byte[n][n];
        byte k = 0;
        for(int i = 0; i<n; i++)
            for(int j=0;j<n;j++)
                board[i][j] = toCopy.board[i][j];
        zeroI = toCopy.zeroI;
        zeroJ = toCopy.zeroJ;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(board[i][j]);
                if (j < n - 1)
                    sb.append(",");
            }
            if (i < n - 1)
                sb.append("\n");
        }
        return sb.toString();
    }

    public boolean makeMove(int direction) {
        boolean moveMade = false;
        switch (direction) {
            case LEFT:
                if(zeroJ > 0) {
                    board[zeroI][zeroJ] = board[zeroI][zeroJ-1];
                    board[zeroI][zeroJ - 1] = 0;
                    zeroJ--;
                    moveMade = true;
                }
                break;
            case RIGHT:
                if(zeroJ < n-1) {
                    board[zeroI][zeroJ] = board[zeroI][zeroJ+1];
                    board[zeroI][zeroJ + 1] = 0;
                    zeroJ++;
                    moveMade = true;
                }
                break;
            case UP:
                if(zeroI > 0) {
                    board[zeroI][zeroJ] = board[zeroI-1][zeroJ];
                    board[zeroI-1][zeroJ] = 0;
                    zeroI--;
                    moveMade = true;
                }
                break;
            case DOWN:
                if(zeroI < n-1) {
                    board[zeroI][zeroJ] = board[zeroI+1][zeroJ];
                    board[zeroI+1][zeroJ] = 0;
                    zeroI++;
                    moveMade = true;
                }
                break;
        }
        return moveMade;
    }

    public void shuffle(int howMany, Long seed) {
        Random r = seed != null ? new Random(seed) : new Random();
        for(int i = 0; i < howMany; i++) {
            while(!this.makeMove(r.nextInt() % 4));
        }
    }

    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> children = new ArrayList<GraphState>();
        for(int direction = 0; direction < 4; direction++) {
            Slider child = new Slider(this);
            if(child.makeMove(direction)) {
                children.add(child);
                child.setMoveName(MOVE_NAMES[direction]);
            }

        }
        return children;
    }

    @Override
    public boolean isSolution() {
        byte k = 0;
        for(int i = 0; i<n; i++)
            for(int j=0;j<n;j++)
                if(board[i][j] != k++)
                    return false;
        return true;
    }

    @Override
    public int hashCode() {
        byte[] linear = new byte[n * n];
        int k = 0;
        for (int i =0; i< n; i++)
            for (int j = 0; j < n; j++)
                linear[k++] = board[i][j];
        return Arrays.hashCode(linear);
    }

    public static void main(String[] args) {
        int testCasesCount = 100;
        int shuffleCount = 1000;

        int totalTimeMisplaced = 0;
        int totalClosedMisplaced = 0;
        int totalOpenMisplaced = 0;

        int totalTimeManhattan = 0;
        int totalClosedManhattan = 0;
        int totalOpenManhattan = 0;

        for (int i = 0; i < testCasesCount; i++) {
            Slider slider = new Slider();
            GraphSearchAlgorithm algo = new AStar(slider);
            slider.shuffle(shuffleCount, null);

            Slider.setHFunction(new MisplacedTilesHeur());
            algo.execute();

            totalTimeMisplaced += algo.getDurationTime();
            totalClosedMisplaced += algo.getClosedStatesCount();
            totalOpenMisplaced += algo.getOpenSet().size();

            Slider.setHFunction(new ManhattanHeuristics());
            algo.execute();

            totalTimeManhattan += algo.getDurationTime();
            totalClosedManhattan += algo.getClosedStatesCount();
            totalOpenManhattan += algo.getOpenSet().size();
        }

        System.out.println("MISPLACED:");
        System.out.println("\tTotal time:" + totalTimeMisplaced);
        System.out.println("\tTotal closed:" + totalClosedMisplaced);
        System.out.println("\tTotal open:" + totalOpenMisplaced);
        System.out.println("\tAvg time:" + totalTimeMisplaced / testCasesCount);
        System.out.println("\tAvg closed:" + totalClosedMisplaced / testCasesCount);
        System.out.println("\tAvg open:" + totalOpenMisplaced / testCasesCount);

        System.out.println("MANHATTAN:");
        System.out.println("\tTotal time:" + totalTimeManhattan);
        System.out.println("\tTotal closed:" + totalClosedManhattan);
        System.out.println("\tTotal open:" + totalOpenManhattan);
        System.out.println("\tAvg time:" + totalTimeManhattan / testCasesCount);
        System.out.println("\tAvg closed:" + totalClosedManhattan / testCasesCount);
        System.out.println("\tAvg open:" + totalOpenManhattan / testCasesCount);
//        slider.shuffle(100, new Long(1234));
//        System.out.println(slider);
//
//        GraphSearchAlgorithm algo = new AStar(slider);
//        Slider.setHFunction(new ManhattanHeuristics());
//        algo.execute();
//        GraphState solution = algo.getSolutions().get(0);
//
//        System.out.println("Time: " + algo.getDurationTime());
//        System.out.println("Closed: " + algo.getClosedStatesCount());
//        System.out.println("Open: " + algo.getOpenSet().size());
////        System.out.println("Solution: " + solution);
////        System.out.println("Solution path-len: " + solution.getG());
//        System.out.println("path: " + solution.getMovesAlongPath());
//        System.out.println("");
//        Slider.setHFunction(new MisplacedTilesHeur());
//        algo.execute();
//        solution = algo.getSolutions().get(0);
//
//        System.out.println("Time: " + algo.getDurationTime());
//        System.out.println("Closed: " + algo.getClosedStatesCount());
//        System.out.println("Open: " + algo.getOpenSet().size());
////        System.out.println("Solution: " + solution);
////        System.out.println("Solution path-len: " + solution.getG());
//        System.out.println("path: " + solution.getMovesAlongPath());
    }

    public byte[][] getBoard() {
        return board;
    }
}
