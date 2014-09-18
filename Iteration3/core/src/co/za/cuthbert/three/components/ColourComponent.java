package co.za.cuthbert.three.components;


import co.za.cuthbert.three.value_objects.Colour;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourComponent extends Component implements Pool.Poolable {
    private final Colour colour;

    public Colour colour() {
        return colour;
    }

    public ColourComponent() {
        colour = new Colour();
    }

    @Override
    public void reset() {
        colour.reset();
    }
}

