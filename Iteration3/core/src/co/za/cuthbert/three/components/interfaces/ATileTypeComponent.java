package co.za.cuthbert.three.components.interfaces;

import co.za.cuthbert.three.TileType;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public abstract class ATileTypeComponent extends ANamedComponent {
    public abstract void tileType(TileType tileType);
    public abstract TileType tileType();
}
