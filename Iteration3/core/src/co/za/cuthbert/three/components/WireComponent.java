package co.za.cuthbert.three.components;

import co.za.cuthbert.three.value_objects.ColourVector;
import co.za.cuthbert.three.value_objects.DiscreteColour;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class WireComponent extends Component implements Pool.Poolable {

    private final ColourVector toNexus[];
    private final ColourVector fromNexus[];
    private DiscreteColour[] incomingPortColours;

    public WireComponent() {
        toNexus = new ColourVector[9];
        fromNexus = new ColourVector[9];
        incomingPortColours = new DiscreteColour[9];
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                toNexus[i] = new ColourVector();
                fromNexus[i] = new ColourVector();
                incomingPortColours[i] = DiscreteColour.ALPHA;
            }
        }
    }

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
        for (DiscreteColour colour : incomingPortColours) {
            if (colour != null) {
                colour = DiscreteColour.ALPHA;
            }
        }
    }

    public void incomingPortColours(DiscreteColour[] colours) {
        for (int i = 0; i < 9; ++i) {
            if (i != 4 && colours[i] != null) {
                incomingPortColours[i] = (colours[i]);
            }
        }
    }

    public void incomingPortColour(int port, DiscreteColour colour) {
        incomingPortColours[port] = (colour);
    }

    public DiscreteColour outgoingColour(int port) {
        return fromNexus[port].last().colour;
    }

    public DiscreteColour[] outgoingColours() {
        DiscreteColour[] out = new DiscreteColour[9];
        for (int i = 0; i < 9; ++i) {
            if (i != 4) {
                out[i] = outgoingColour(i);
            } else {
                out[i] = DiscreteColour.ALPHA;
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
                DiscreteColour feedColour;
                if(portCount>1){
                    feedColour=outgoingNexusColour(i);
                }else{
                    feedColour=DiscreteColour.ALPHA;
                }
                fromNexus[i].advance(delta,feedColour);
            }
        }

    }


    private DiscreteColour outgoingNexusColour(int port) {
        DiscreteColour outgoingNexusColour = DiscreteColour.ALPHA;
        for (int i = 0; i < 9; i++) {
            if (i != 4 && i != port) {
                DiscreteColour colour = toNexus[i].last().colour;
                outgoingNexusColour = DiscreteColour.add(outgoingNexusColour, colour);
            }
        }
        return outgoingNexusColour;
    }


    public ColourVector resultantVector(int port) {
        ColourVector from = fromNexus[port];
        ColourVector to = toNexus[port];
        return ColourVector.mergeVectors(from, to);
    }

    public DiscreteColour[] incomingPortColours() {
        return incomingPortColours;
    }


}
