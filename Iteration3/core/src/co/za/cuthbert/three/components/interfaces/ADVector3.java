package co.za.cuthbert.three.components.interfaces;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public abstract class ADVector3 extends ANamedComponent {
    public abstract int x();
    public abstract int y();
    public abstract int z();

    public abstract void x(int x);
    public abstract void y(int y);
    public abstract void z(int z);
    public abstract void set(int x, int y, int z);
}
