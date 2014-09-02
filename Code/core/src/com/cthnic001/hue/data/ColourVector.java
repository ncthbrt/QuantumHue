package com.cthnic001.hue.data;

import com.cthnic001.hue.components.Poolable;
import com.cthnic001.hue.data.Colour;
import com.cthnic001.hue.data.ColourBracket;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourVector implements Poolable {
    public List<ColourBracket> line;

    public ColourVector() {
        line = new ArrayList<ColourBracket>();
        line.add(new ColourBracket());
    }

    public void advance(float delta, Colour portColour) {
        if (!portColour.equals(line.get(0).colour)) {
            line.add(0, new ColourBracket(portColour));
        }
        for (int i = 1; i < line.size(); i++) {
            line.get(i).advance(delta);
            if (line.get(i).position() >= 1f) {
                line.remove(i--);
            }
        }
    }

    @Override
    public void reset() {
        line.clear();
        line.add(new ColourBracket());
    }

    private static ColourVector flip(ColourVector vector){
        ColourVector flippedCopy=new ColourVector();
        float position=0;
        for (int i=vector.line.size()-1; i>=0; i--){
            ColourBracket bracket=new ColourBracket(vector.line.get(i).colour,position);
            flippedCopy.line.add(bracket);
            position=1-vector.line.get(i).position();

        }
        return flippedCopy;
    }

    private static ColourVector merge(ColourVector from,ColourVector to){
        to=flip(to);
        int left=0,right=0;
        ColourVector result=new ColourVector();
        result.line.set(0,new ColourBracket(Colour.add(from.line.get(0).colour,from.line.get(0).colour),0));
        while(left<from.line.size()||right<to.line.size())
            if(from.line.get(left+1).position()<(to.line.get(right+1).position())){
                result.line.add(new ColourBracket(Colour.add(from.line.get(left+1).colour,from.line.get(right).colour),from.line.get(left+1).position()));
                left++;
            }else {
                result.line.add(new ColourBracket(Colour.add(to.line.get(left).colour,from.line.get(right+1).colour),to.line.get(right+1).position()));
                right++;
            }
        return result;
    }
}
