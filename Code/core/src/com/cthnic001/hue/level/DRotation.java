package com.cthnic001.hue.level;


public enum DRotation {
    /**
     * This class provides services for discrete rotations
     */
    ZERO(0, 1, 0, 0, 1),//Identity Matrix
    NINETY((float) Math.PI / 2f, 0, -1, 1, 0), //90 degree clockwise rotation matrix
    ONE_EIGHTY((float) Math.PI, -1, 0, 0, -1),//180 Degree rotation Matrix
    TWO_SEVENTY(((float) Math.PI * (1.5f)), 0, 1, -1, 0),//270 Degree rotation Matrix
    NULL(0, 0, 0, 0, 0); //Null Matrix

    /**
     * m1 is the first row and column of the transformation, n2 is the last
     */
    DRotation(float angle, int m0, int m1, int n0, int n1) {
        _angle = angle;
        _rotationMatrix = new int[2][2];
        _rotationMatrix[0][0] = m0;
        _rotationMatrix[0][1] = m1;
        _rotationMatrix[1][0] = n0;
        _rotationMatrix[1][1] = n1;
    }

    private final int[][] _rotationMatrix;

    private final float _angle;

    public float angle() {
        return _angle;
    }

    public int x(int x, int y) {
        return (x * _rotationMatrix[0][0] + y * _rotationMatrix[0][1]);
    }

    public int y(int x, int y) {
        return (x * _rotationMatrix[1][0] + y * _rotationMatrix[1][1]);
    }


}
