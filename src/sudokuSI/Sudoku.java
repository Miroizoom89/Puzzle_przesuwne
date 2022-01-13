package sudokuSI;

import sac.graph.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sudoku extends GraphStateImpl {
    public static final int n = 3;
    public static final int n2 = n * n;

    private byte[][] board;
    private int zeros = n2 * n2;

    public Sudoku() {
        board = new byte[n2][n2];

        for (int i = 0; i< n2; i++){
            for (int j =0; j < n2; j++) {
                board[i][j]=0;
            }
        }
    }

    public Sudoku(Sudoku s) {
        board = new byte[n2][n2];
        for (int i = 0; i< n2; i++){
            for (int j =0; j < n2; j++) {
                board[i][j] = s.board[i][j];
            }
        }
        zeros = s.zeros;
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
//        txt.append("-".repeat(n2 * 2));
//        txt.append("\n");

        for (int i = 0; i< n2; i++){
            for (int j =0; j < n2; j++) {
                if((j+1) % n == 0)
                    txt.append(board[i][j]).append("|");
                else if(j == 0)
                    txt.append("|").append(board[i][j]).append(" ");
                else
                    txt.append(board[i][j]).append(" ");
            }
            if((i+1) % n == 0) {
                txt.append("\n");
//                txt.append("-".repeat(n2 * 2));
            }

            txt.append("\n");
        }
        return txt.toString();
    }

    public boolean isLegal() {
        byte[] rowGroup = new byte[n2];
        byte[] columnGroup = new byte[n2];
        // rows
        for (int i = 0; i< n2; i++){
            for (int j = 0; j < n2; j++) {
                rowGroup[j] = board[i][j];
                columnGroup[j] = board[j][i];
            }
            if(!checkBlock(rowGroup) || !checkBlock(columnGroup))
                return false;
        }

        for (int i = 0; i< n; i++){
            for (int j = 0; j < n; j++) {
                int z = 0;
                for (int k = 0; k< n; k++){
                    for (int m = 0; m < n; m++) {
                        rowGroup[z++] = board[k + i * n][m + j * n];
                    }
                }
                if(!checkBlock(rowGroup))
                    return false;
            }
        }

        return true;
    }

    private void refreshZeros() {
        zeros = 0;
        for (int i = 0; i< n2; i++){
            for (int j = 0; j < n2; j++) {
                if(board[i][j]==0)
                    zeros++;
            }
        }
    }

    private boolean checkBlock(byte[] arr) {
        boolean[] v = new boolean[n2 + 1];

        for (byte val : arr) {
            if(val == 0)
                continue;
            if(v[val])
                return false;
            v[val] = true;
        }
        return true;
    }

    public void fromStringN3(String txt) {
        int k = 0;
        for (int i = 0; i< n2; i++){
            for (int j = 0; j < n2; j++) {
                board[i][j] = Byte.parseByte(txt.substring(k, k+1));
                k++;
            }
        }
        refreshZeros();
    }

    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> children = new ArrayList<>();

        int i = 0;
        int j = 0;

        lookingForZero:
        for(;i < n2; i++)
            for(j = 0;j < n2; j++)
                if(board[i][j] == 0)
                    break lookingForZero;

        if(i == n2)
            return children;

        for(int k = 0; k < n2; k++) {
            Sudoku child = new Sudoku(this);
            child.board[i][j] = (byte)(k+1);
            if(child.isLegal()) {
                children.add(child);
                child.zeros--;
            }
        }
        return children;
    }

    @Override
    public int hashCode() {
        byte[] linearSudoku = new byte[n2 * n2];
        int k = 0;
        for (int i =0; i< n2; i++)
            for (int j = 0; j < n2; j++)
                linearSudoku[k++] = board[i][j];
        return Arrays.hashCode(linearSudoku);
//        return this.toString().hashCode();
    }

    @Override
    public boolean isSolution() {
        return ((zeros == 0) && isLegal());
    }

    public double getZeros() {
        return this.zeros;
    }

    public static void main(String[] args) {
        // "000000085000210009960080100500800016000000000890006007009070052300054000480000000";
//        String sudokuAsTxt = "000000085000210009960080100500800016000000000890006007009070052300054000480000000";
//        String sudokuAsTxt = "003020600900305001001806400008102900700000008006708200002609500800203009005010300";
        String sudokuAsTxt = "000020600900305001001800400008102900000000008006708200002609500800203009005010300";
        Sudoku s = new Sudoku();
        s.fromStringN3(sudokuAsTxt);
        System.out.println(s);

        GraphSearchConfigurator conf = new GraphSearchConfigurator();
        conf.setWantedNumberOfSolutions(Integer.MAX_VALUE);
        GraphSearchAlgorithm algo = new BestFirstSearch(s, conf);
        Sudoku.setHFunction(new EmptyCellsHeuristics());
        algo.execute();
        List<GraphState> solutions = algo.getSolutions();
        for (GraphState solution: solutions) {
            System.out.println(solution);
        }
        System.out.println("Time: " + algo.getDurationTime());
        System.out.println("Closed: " + algo.getClosedStatesCount());
        System.out.println("Open: " + algo.getOpenSet().size());
        System.out.println("Solutions: " + algo.getSolutions().size());
    }
}
