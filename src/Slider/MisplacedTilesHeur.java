package Slider;

import sac.State;
import sac.StateFunction;

public class MisplacedTilesHeur  extends StateFunction {
    @Override
    public double calculate(State state) {
        Slider slider = (Slider) state;
        byte k = 0;
        int h = 0;
        for(int i = 0; i<Slider.n; i++)
            for(int j=0;j<Slider.n;j++) {
                if ((slider.getBoard()[i][j] != 0) && (slider.getBoard()[i][j] != k))
                    h++;
                k++;
            }
        return h;
    }
}
