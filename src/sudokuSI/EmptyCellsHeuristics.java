package sudokuSI;

import sac.State;
import sac.StateFunction;

public class EmptyCellsHeuristics extends StateFunction {
    public double calculate(State state) {
        return ((Sudoku)state).getZeros();
    }

    //    @Override
//    public double calculate(State state) {
//        return super.calculate(state);
//    }
}
