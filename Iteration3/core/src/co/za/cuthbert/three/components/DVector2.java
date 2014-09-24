package co.za.cuthbert.three.components;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * A class to store discrete coordinates in three dimensions. Especially useful for tile based games
 * Copyright Nick Cuthbert, 2014.
 */
public class DVector2 extends Component implements Pool.Poolable{
    public static final String TYPE_NAME="discrete-position";
    public String getComponentName() {
        return TYPE_NAME;
    }
    private int x, y;

    public DVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public DVector2() {
        reset();
    }


    public int x() {
        return x;
    }


    public int y() {
        return y;
    }


    public void x(int x) {
        this.x=x;
    }


    public void y(int y) {
        this.y=y;
    }


    public void set(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public void reset() {
        this.x = 0;
        this.y = 0;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DVector2) {
            DVector2 other = (DVector2) obj;
            return x == other.x && y == other.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return x + y * 17;
    }
}