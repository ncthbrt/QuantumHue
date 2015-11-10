package com.deepwallgames.quantumhue.value_objects;

/**
 * Created by CTHNI_000 on 2014-09-17.
 */
public enum DRotation {
    ZERO(Rotation.ZERO),
    FOURTY_FIVE(new Rotation((float)Math.toRadians(45))),
    NINETY(Rotation.NINETY),
    ONE_THIRTY_FIVE(new Rotation((float)Math.toRadians(135))),
    ONE_EIGHTY(Rotation.ONE_EIGHTY),
    TWO_TWENTY_FIVE(new Rotation((float)Math.toRadians(225))),
    TWO_SEVENTY(Rotation.TWO_SEVENTY);

    DRotation(Rotation rotation){
        this.rotation=rotation;
    }
    public final Rotation rotation;
}
