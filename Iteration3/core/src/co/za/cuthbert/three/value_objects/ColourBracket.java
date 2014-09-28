package co.za.cuthbert.three.value_objects;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourBracket implements Pool.Poolable {
    public DiscreteColour colour;
    private float position;

    public float position() {
        return position;
    }

    public void position(float position) {
        this.position = position;
    }

    public ColourBracket() {
        colour = DiscreteColour.ALPHA;
        position = 0;
    }

    public ColourBracket(DiscreteColour colour) {
        this();
        this.colour=colour;
    }

    public ColourBracket(DiscreteColour colour, float position) {
        this();
        this.colour=(colour);
        this.position=position;
    }

    @Override
    public void reset() {
        colour=DiscreteColour.ALPHA;
        position = 0;
    }

    public void advance(float delta) {
        position += delta;
    }
}
