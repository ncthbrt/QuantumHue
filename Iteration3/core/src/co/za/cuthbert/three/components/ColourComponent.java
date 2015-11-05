package co.za.cuthbert.three.components;


import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourComponent implements Pool.Poolable, Component {
    private Colour colour;

    public void colour(Colour colour) {
        this.colour = colour;
    }

    public Colour colour() {
        return colour;
    }

    public ColourComponent() {
        colour = DiscreteColour.ALPHA.toColour();
    }

    @Override
    public void reset() {
        colour=DiscreteColour.ALPHA.toColour();
    }
}

