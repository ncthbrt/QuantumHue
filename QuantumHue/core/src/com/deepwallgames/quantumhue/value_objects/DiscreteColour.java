package com.deepwallgames.quantumhue.value_objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public enum DiscreteColour {
    RED(true,false,false),
    YELLOW(true,true,false),
    GREEN(false,true,false),
    CYAN(false,true, true),
    BLUE(false,false,true),
    MAGENTA(true,false,true),
    WHITE(true,true,true),
    ALPHA(false,false,false);

    public final boolean red, green, blue;
    DiscreteColour(boolean red, boolean green, boolean blue){
        this.red=red;
        this.green=green;
        this.blue=blue;
    }

    public static DiscreteColour add(DiscreteColour colour1,DiscreteColour colour2) {
         boolean red=colour1.red|colour2.red;
         boolean green=colour1.green|colour2.green;
         boolean blue=colour1.blue|colour2.blue;
         return map(red,green,blue);
    }

    public static DiscreteColour subtract(DiscreteColour colour1,DiscreteColour colour2) {
       boolean red=false;
       if(colour1.red){
           red= !colour2.red;
       }

       boolean green=false;
       if(colour1.green){
            green= !colour2.green;
       }

        boolean blue=false;
        if(colour1.blue){
            blue= !colour2.blue;
        }
        return map(red,green,blue);
    }

    public Colour toColour() {
        if(red ||green || blue) {
            return new Colour(red ? 255 : 0, green ? 255 : 0, blue ? 255 : 0, 255);
        }else{
            return new Colour(0,0,0,0);
        }
    }

    public final List<Colour> toComponentColours() {
        List<Colour> colours=new ArrayList<Colour>();
        if(red) {
            colours.add(new Colour(255,0,0,255));
        }
        if(green) {
            colours.add(new Colour(0,255,0,255));
        }
        if(blue) {
            colours.add(new Colour(0,0,255,255));
        }
        return colours;
    }

    public static DiscreteColour map(boolean red, boolean green,boolean blue){
        for(DiscreteColour discreteColour: DiscreteColour.values()){
            if(discreteColour.red==red && discreteColour.green==green && discreteColour.blue==blue){
                return discreteColour;
            }
        }
        return null;
    }

}
