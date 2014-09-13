package co.za.cuthbert.three.components;

import co.za.cuthbert.three.components.interfaces.AColourComponent;
import co.za.cuthbert.three.data.Colour;
import com.badlogic.gdx.utils.Pool;


/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourComponent extends AColourComponent implements Pool.Poolable {
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

    public static final String TYPE_NAME="colour";
    @Override
    public String getComponentName() {
        return TYPE_NAME;
    }
}

