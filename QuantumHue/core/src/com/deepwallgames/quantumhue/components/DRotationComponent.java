package com.deepwallgames.quantumhue.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.deepwallgames.quantumhue.value_objects.DRotation;



/**
 * Created by nick on 21/11/2015.
 */
public class DRotationComponent implements Pool.Poolable, Component {
    public DRotationComponent(){}
    public DRotation rotation;
    public float angle(){
        return rotation.angle;
    }
    public DRotationComponent(DRotation rotation){
        this.rotation=rotation;
    }
    public void reset() {
        rotation=DRotation.ZERO;
    }
}
