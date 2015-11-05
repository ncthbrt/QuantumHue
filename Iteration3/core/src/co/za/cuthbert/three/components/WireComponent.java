package co.za.cuthbert.three.components;

import co.za.cuthbert.three.value_objects.Colour;
import co.za.cuthbert.three.value_objects.ColourVector;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class WireComponent implements Pool.Poolable, Component {

    private final ColourVector toNexus[];
    private final ColourVector fromNexus[];
    private Colour[] incomingPortColours;

    public WireComponent() {
        toNexus = new ColourVector[9];
        fromNexus = new ColourVector[9];
        incomingPortColours = new Colour[9];
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                toNexus[i] = new ColourVector();
                fromNexus[i] = new ColourVector();
                incomingPortColours[i] = DiscreteColour.ALPHA.toColour();
            }
        }
    }

//    public DiscreteColour colourAtPoint(float between,DRotation rotation){
//        Vector2 delta=new Vector2(point.x-tileLocation.x(), point.y-tileLocation.y());
//
//        if(delta.x>0){
//            float gradient=delta.y/delta.x;
//
//        }else if(delta.x<0){
//            float gradient=delta.y/delta.x;
//        }else{
//            if(delta.y<0){
//
//            }else if(delta.y>0){
//
//            }else{
//                return outgoingNexusColour(1);
//            }
//        }
//    }

    @Override
    public void reset() {
        for (ColourVector vector : toNexus) {
            if (vector != null)
                vector.reset();
        }
        for (ColourVector vector : fromNexus) {
            if (vector != null)
                vector.reset();
        }
        for (Colour colour : incomingPortColours) {
            if (colour != null) {
                colour = DiscreteColour.ALPHA.toColour();
            }
        }
    }

    public void incomingPortColours(Colour[] colours) {
        for (int i = 0; i < 9; ++i) {
            if (i != 4 && colours[i] != null) {
                incomingPortColours[i] = (colours[i]);
            }
        }
    }

    public void incomingPortColour(int port, Colour colour) {
        incomingPortColours[port] = (colour);
    }

    public Colour outgoingColour(int port) {
        return fromNexus[port].last().colour;
    }

    public Colour[] outgoingColours() {
        Colour[] out = new Colour[9];
        for (int i = 0; i < 9; ++i) {
            if (i != 4) {
                out[i] = outgoingColour(i);
            } else {
                out[i] = DiscreteColour.ALPHA.toColour();
            }
        }
        return out;
    }

    public void advance(float delta) {
        int portCount=0;
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                if(incomingPortColours[i]!=null) {
                    toNexus[i].advance(delta, incomingPortColours[i]);
                    ++portCount;
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                Colour feedColour;
                if(portCount>1){
                    feedColour=outgoingNexusColour(i);
                }else{
                    feedColour=DiscreteColour.ALPHA.toColour();
                }
                fromNexus[i].advance(delta,feedColour);
            }
        }

    }


    private Colour outgoingNexusColour(int port) {
        Colour outgoingNexusColour = DiscreteColour.ALPHA.toColour();
        for (int i = 0; i < 9; i++) {
            if (i != 4 && i != port) {
                Colour colour = toNexus[i].last().colour;
                outgoingNexusColour = Colour.add(outgoingNexusColour, colour);
            }
        }
        return outgoingNexusColour;
    }
    private Colour outgoingNexusColour() {
        return outgoingNexusColour(-1);
    }

    public ColourVector resultantVector(int port) {
        ColourVector from = fromNexus[port];
        ColourVector to = toNexus[port];
        return ColourVector.mergeVectors(from, to);
    }

    public Colour[] incomingPortColours() {
        return incomingPortColours;
    }


}
