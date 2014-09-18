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

    public ColourVector(List<ColourBracket> line) {
        this.line = new ArrayList<ColourBracket>();
        this.line.addAll(line);
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


    public static ColourVector reverseVector(ColourVector vector){
        List<ColourBracket> line = new ArrayList<ColourBracket>();
        ColourBracket first=vector.line.get(vector.line.size()-1);
        line.add(new ColourBracket(first.colour, 0));
        for (int i=vector.line.size()-1; i>0; i++){
            Colour colour=vector.line.get(i-1).colour;
            ColourBracket bracket=new ColourBracket(colour,1f-vector.line.get(i).position());
            line.add(bracket);
        }
        return new ColourVector(line);
    }

    public static ColourVector mergeVectors(ColourVector fromVector, ColourVector toVector){
        int i=0;
        int j=0;
        ColourVector revToVector=reverseVector(toVector);
        List<ColourBracket> combinedLine = new ArrayList<ColourBracket>();
        combinedLine.add(new ColourBracket(Colour.add(fromVector.line.get(i).colour, revToVector.line.get(j).colour)));
        while(i<fromVector.line.size()-1 && j<revToVector.line.size()-1){
            float position;
            if(fromVector.line.get(i+1).position()==revToVector.line.get(j+1).position()){
                ++i;
                ++j;
                position=fromVector.line.get(i).position();
            }
            else if(fromVector.line.get(i+1).position()<revToVector.line.get(j+1).position()){
                i++;
                position=fromVector.line.get(i).position();
            }else{
                j++;
                position=revToVector.line.get(j).position();
            }
            combinedLine.add(new ColourBracket(Colour.add(fromVector.line.get(i).colour, revToVector.line.get(j).colour),position));
        }
        while (i<fromVector.line.size()-1){
            ++i;
            float position=fromVector.line.get(i).position();
            combinedLine.add(new ColourBracket(Colour.add(fromVector.line.get(i).colour, revToVector.line.get(j).colour),position));
        }
        while (j<revToVector.line.size()-1){
            ++j;
            float position=revToVector.line.get(j).position();
            combinedLine.add(new ColourBracket(Colour.add(fromVector.line.get(i).colour, revToVector.line.get(j).colour),position));
        }

        return new ColourVector(combinedLine);

    }

    @Override
    public void reset() {
        line.clear();
        line.add(new ColourBracket());
    }
}
