package com.deepwallgames.quantumhue.value_objects;

/**
 * Created by CTHNI_000 on 2014-09-17.
 */
public enum DRotation {
    ZERO(Rotation.ZERO),
    FORTY_FIVE(new Rotation((float)Math.toRadians(45))),
    NINETY(Rotation.NINETY),
    ONE_THIRTY_FIVE(new Rotation((float)Math.toRadians(135))),
    ONE_EIGHTY(Rotation.ONE_EIGHTY),
    TWO_TWENTY_FIVE(new Rotation((float)Math.toRadians(225))),
    TWO_SEVENTY(Rotation.TWO_SEVENTY),
    THREE_FIFTEEN(new Rotation((float)Math.toRadians(315)));

    DRotation(Rotation rotation){
        this.rotation=rotation;
        this.angle=(float)Math.toDegrees(rotation.angle());
    }

    public final Rotation rotation;
    public final float angle;
    public static DRotation fromAngle(float angle){
        System.out.println("Angle: "+angle);
        if(angle<0){
            angle=360+angle;
        }
        System.out.println("Adjusted Angle: "+angle);
        return (DRotation.values()[(Math.round(angle/45))%DRotation.values().length]);
    }
}
