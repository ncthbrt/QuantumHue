package com.deepwallgames.quantumhue.value_objects;


import java.io.Serializable;

public class Rotation implements Serializable {


    /**
     * This class provides services for discrete rotations
     */
    public static final Rotation ZERO = new Rotation(0, 1, 0, 0, 1);//Identity Matrix
    public static final Rotation NINETY = new Rotation((float) Math.PI / 2f, 0, -1, 1, 0); //90 degree clockwise rotation matrix
    public static final Rotation ONE_EIGHTY = new Rotation((float) Math.PI, -1, 0, 0, -1);//180 Degree rotation Matrix
    public static final Rotation TWO_SEVENTY = new Rotation(((float) Math.PI * (1.5f)), 0, 1, -1, 0);//270 Degree rotation Matrix
    public static final Rotation NULL = new Rotation(0, 0, 0, 0, 0); //Null Matrix

    /**
     * m1 is the first row and column of the transformation, n2 is the last
     */
    private Rotation(float angle, float m0, float m1, float n0, float n1) {
        _angle = angle;
        _rotationMatrix = new float[2][2];
        _rotationMatrix[0][0] = m0;
        _rotationMatrix[0][1] = m1;
        _rotationMatrix[1][0] = n0;
        _rotationMatrix[1][1] = n1;
    }

    public Rotation(float angle) {
        this(angle, (float) Math.cos(angle), (float) -Math.sin(angle), (float) Math.sin(angle), (float) Math.cos(angle));
    }


    private final float[][] _rotationMatrix;

    private final float _angle;

    public Rotation() {
        this(0);
    }

    public float angle() {
        return _angle;
    }

    public float x(float x, float y) {
        return (x * _rotationMatrix[0][0] + y * _rotationMatrix[0][1]);
    }

    public float y(int x, int y) {
        return (x * _rotationMatrix[1][0] + y * _rotationMatrix[1][1]);
    }


}
