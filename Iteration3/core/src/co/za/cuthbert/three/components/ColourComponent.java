package co.za.cuthbert.three.components;


import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourComponent extends Component implements Pool.Poolable {
    private DiscreteColour colour;

    public void colour(DiscreteColour colour) {
        this.colour = colour;
    }

    public DiscreteColour colour() {
        return colour;
    }

    public ColourComponent() {
        colour = DiscreteColour.ALPHA;
    }

    @Override
    public void reset() {
        colour=DiscreteColour.ALPHA;
    }
}

