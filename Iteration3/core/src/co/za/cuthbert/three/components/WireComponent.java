package co.za.cuthbert.three.components;

import co.za.cuthbert.three.data.Colour;
import co.za.cuthbert.three.data.ColourVector;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class WireComponent extends Component implements Pool.Poolable{

    public final ColourVector toNexus[];
    public final ColourVector fromNexus[];
    private Colour[] incomingPortColours;
    public WireComponent() {
        toNexus = new ColourVector[9];
        incomingPortColours = new Colour[9];
        fromNexus = new ColourVector[9];
        for (int i = 0; i < 9; i++) {
            if(i!=4) {
                toNexus[i] = new ColourVector();
                fromNexus[i] = new ColourVector();
                incomingPortColours[i] = new Colour();
            }
        }
    }

    public Colour getOutgoingNexusColour() {
        Colour finalColour = new Colour();
        for (int i = 0; i < toNexus.length; i++) {
            if(i!=4) {
                ColourVector vector = toNexus[i];
                Colour colour = vector.line.get(vector.line.size() - 1).colour;
                finalColour.add(colour);
            }
        }
        return finalColour;
    }

    public Colour[] getOutgoingPortColours() {
        Colour[] outgoingPortColours = new Colour[9];
        for (int i = 0; i < fromNexus.length; i++) {
            if(i!=4) {
                outgoingPortColours[i] = fromNexus[i].line.get(fromNexus[i].line.size() - 1).colour;
            }
        }
        return outgoingPortColours;
    }

    public ColourVector getResultantVector(int port){
        ColourVector from=fromNexus[port];
        ColourVector to=toNexus[port];
        return ColourVector.mergeVectors(from, to);
    }

    public void advance(float delta) {
        Colour outgoingNexusColour = getOutgoingNexusColour();
        for (int i = 0; i < toNexus.length; i++) {
            if(i!=4) {
                toNexus[i].advance(delta, incomingPortColours[i]);
            }
        }
        for (int i = 0; i < fromNexus.length; i++) {
            if(i!=4) {
                Colour feedColour = Colour.subtract(outgoingNexusColour, (toNexus[i].line.get(toNexus[i].line.size() - 1).colour));
                fromNexus[i].advance(delta, feedColour);
            }
        }
    }



    public void setIncomingPortColours(Colour[] colours) {
        System.arraycopy(colours, 0, incomingPortColours, 0, incomingPortColours.length);
    }


    @Override
    public void reset() {
        for (ColourVector vector : toNexus) {
            if(vector!=null)
            vector.reset();
        }
        for (ColourVector vector : fromNexus) {
            if(vector!=null)
            vector.reset();
        }

        for (Colour incomingPortColour : incomingPortColours) {
            if(incomingPortColour!=null)
            incomingPortColour.reset();
        }
    }
}
