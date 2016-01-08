package com.deepwallgames.quantumhue;

import com.badlogic.ashley.core.Component;
import com.deepwallgames.quantumhue.value_objects.Colour;

import java.util.ArrayList;

/**
 * Created by nick on 23/11/2015.
 */
public interface IFunction  extends Component{
    boolean[] inputMask=new boolean[8];
    boolean[] outputMask=new boolean[8];
    Colour calculate(ArrayList<Colour> inputs, ArrayList<Colour> outputs);
}
