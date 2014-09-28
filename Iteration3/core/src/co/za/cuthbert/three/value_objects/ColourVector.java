package co.za.cuthbert.three.value_objects;

import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourVector implements Pool.Poolable {
    public List<ColourBracket> line;

    public ColourVector() {
        line = new ArrayList<ColourBracket>();
        line.add(new ColourBracket());
    }

    public ColourBracket last() {
        return line.get(line.size() - 1);
    }

    public ColourVector(List<ColourBracket> line) {
        this.line = new ArrayList<ColourBracket>();
        this.line.addAll(line);
    }

    public void advance(float delta, DiscreteColour portColour) {
        if (portColour!=(line.get(0).colour)) {
            line.add(0, new ColourBracket(portColour));
        }
        for (int i=1; i<line.size(); ++i){
            line.get(i).advance(delta);
            if (line.get(i).position() > 1f) {
              line.remove(i--);
            }
        }
    }


    public static ColourVector reverseVector(ColourVector vector){
        List<ColourBracket> line = new ArrayList<ColourBracket>();
        float position = 1;
        for (int i = vector.line.size() - 1; i >= 0; i--) {
            ColourBracket bracket = vector.line.get(i);
            line.add(new ColourBracket(bracket.colour, 1f - position));
            position = bracket.position();
        }
        return new ColourVector(line);
    }

    public static ColourVector mergeVectors(ColourVector fromVector, ColourVector toVector){

        ColourVector revToVector=reverseVector(toVector);
        List<ColourBracket> combinedLine = new ArrayList<ColourBracket>();
        if(toVector.line.size()>1) {
//            for(ColourBracket bracket: toVector.line){
//                //System.out.println("Bracket - position: "+bracket.position()+", colour: "+Integer.toHexString(bracket.colour.toRGBA()));
//            }
        }
        combinedLine.add(new ColourBracket(DiscreteColour.add(fromVector.line.get(0).colour, revToVector.line.get(0).colour), 0));
        int j = 0;
        int i = 0;


        while(i<fromVector.line.size()-1 && j<revToVector.line.size()-1){
            float position;

            if(fromVector.line.get(i+1).position()==revToVector.line.get(j+1).position()){
                ++i;
                ++j;
                position=fromVector.line.get(i).position();
            }
            else if(fromVector.line.get(i+1).position()<revToVector.line.get(j+1).position()){
                ++i;
                position=fromVector.line.get(i).position();
            }else{
                ++j;
                position=revToVector.line.get(j).position();
            }
            if (!fromVector.line.get(i).colour.equals(revToVector.line.get(j))) {
                combinedLine.add(new ColourBracket(DiscreteColour.add(fromVector.line.get(i).colour, revToVector.line.get(j).colour),position));
            }
        }
        while (i<fromVector.line.size()-1){
            ++i;
            float position=fromVector.line.get(i).position();
            if (!fromVector.line.get(i).colour.equals(revToVector.line.get(j))) {
                combinedLine.add(new ColourBracket(DiscreteColour.add(fromVector.line.get(i).colour, revToVector.line.get(j).colour),position));
            }
        }
        while (j<revToVector.line.size()-1){
            ++j;
            float position=revToVector.line.get(j).position();
            if (!fromVector.line.get(i).colour.equals(revToVector.line.get(j))) {
                combinedLine.add(new ColourBracket(DiscreteColour.add(fromVector.line.get(i).colour, revToVector.line.get(j).colour), position));
            }
        }

        return new ColourVector(combinedLine);
    }

    @Override
    public void reset() {
        line.clear();
        line.add(new ColourBracket());
    }
}
