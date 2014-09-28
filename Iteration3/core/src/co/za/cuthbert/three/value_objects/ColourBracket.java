package co.za.cuthbert.three.value_objects;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourBracket implements Pool.Poolable {
    public final Colour colour;
    private float position;

    public float position() {
        return position;
    }

    public void position(float position) {
        this.position = position;
    }

    public ColourBracket() {
        colour = new Colour();
        position = 0;
    }

    public ColourBracket(Colour colour) {
        this();
        colour.set(colour);
    }

    public ColourBracket(Colour colour, float position) {
        this();
        colour.set(colour);
        this.position=position;
    }

    @Override
    public void reset() {
        colour.reset();
        position = 0;
    }

    public void advance(float delta) {
        position += delta;
    }
}
