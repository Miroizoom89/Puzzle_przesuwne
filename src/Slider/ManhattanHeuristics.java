package Slider;

import sac.State;
import sac.StateFunction;

public class ManhattanHeuristics extends StateFunction {
    @Override
    public double calculate(State state) {
        Slider slider = (Slider) state;
        int currentValue;
        int h = 0;
        for(int i = 0; i<Slider.n; i++)
            for(int j=0;j<Slider.n;j++) {
                currentValue = slider.getBoard()[i][j];
                if (currentValue == 0)
                    continue;
                h += Math.abs(i - (currentValue / Slider.n)) + Math.abs(j - (currentValue % Slider.n));
            }
        return h;
    }
}
