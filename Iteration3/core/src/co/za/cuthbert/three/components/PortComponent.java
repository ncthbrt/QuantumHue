package co.za.cuthbert.three.components;


import co.za.cuthbert.three.listeners.Level;
import co.za.cuthbert.three.data.Colour;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.utils.Pool;

/**
 * The wire port component lets
 * Copyright Nick Cuthbert, 2014.
 */
public class PortComponent extends Component implements Pool.Poolable {
    private boolean cullable =true;

    public boolean cullable(){
        return cullable;
    }

    public void cullable(boolean cullable){
        this.cullable=cullable;
    }


    private final PortComponent[] neighboringPorts;
    private final Colour[] portColours;
    private final boolean[] ports;

    public PortComponent() {
        neighboringPorts = new PortComponent[9];
        portColours = new Colour[9];
        ports = new boolean[9];
        for (int i = 0; i < 9; i++) {
            portColours[i] = new Colour();
            ports[i] = false;
            neighboringPorts[i] = null;
        }
        reset();
    }


    public PortComponent(Level level, int x, int y, int z) {
        this();
        setNeighboringPorts(level, x, y, z);
    }


    public boolean[] getPortMap() {
        boolean[] map = new boolean[9];
        System.arraycopy(ports, 0, map, 0, 9);
        return map;
    }


    public Colour[] getIncomingPortColours() {
        Colour[] incoming = new Colour[9];
        for (int port = 0; port < 9; port++) {
            if (port != 4) {
                incoming[port] = getIncomingPortColour(port);
            }
        }
        return incoming;
    }

    public Colour getIncomingPortColour(int port) {
        if (ports[port]) {
            if (port != 4) {
                return neighboringPorts[port].portColours[8 - port];
            }
        }
        return new Colour();
    }


    public void setOutgoingPortColours(Colour[] portColours) {
        System.arraycopy(portColours, 0, this.portColours, 0, 9);
    }

    public void setOutgoingPortColours(Colour portColour) {
        for(int i=0; i<9; ++i){
            this.portColours[i]=portColour;
        }
    }

    public void setNeighboringPorts(Level level, int x, int y, int z) {
        ComponentMapper<PortComponent> portMapper = ComponentMapper.getFor(PortComponent.class);
        for (int j = -1; j <= 1; ++j) {
            for (int i = -1; i <= 1; ++i) {
                if (j != 0 || i != 0) {
                    if (x + i >= 0 && y + j >= 0 && x + i < level.width() && y + j < level.height()) {
                        Entity entity = level.get(x + i, y + j, z);
                        if (entity != null && portMapper.has(entity)) {
                            PortComponent port = portMapper.get(entity);
                            neighboringPorts[(j + 1) * 3 + i + 1] = port;
                            ports[(j + 1) * 3 + i + 1] = true;
                        } else {
                            ports[(j + 1) * 3 + i + 1] = false;
                            neighboringPorts[(j + 1) * 3 + i + 1] = null;
                        }
                    }
                }
            }
        }
    }

    public void cullPorts() {
        if (ports[5] && ports[2] && neighboringPorts[5].neighboringPorts[1] == neighboringPorts[2]) {
            if(neighboringPorts[2].cullable()) {
                ports[2] = false;
            }
        }
        if (ports[5] && ports[8] && neighboringPorts[5].neighboringPorts[7] == neighboringPorts[8]) {
            if(neighboringPorts[8].cullable()) {
                ports[8] = false;
            }
        }
        if (ports[3] && ports[0] && neighboringPorts[3].neighboringPorts[1] == neighboringPorts[0]) {
            if(neighboringPorts[0].cullable()) {
                ports[0] = false;
            }
        }
        if (ports[3] && ports[6] && neighboringPorts[3].neighboringPorts[7] == neighboringPorts[6]) {
            if(neighboringPorts[6].cullable()) {
                ports[6] = false;
            }
        }
        if (ports[1] && ports[0] && neighboringPorts[1].neighboringPorts[3] == neighboringPorts[0]) {
            if(neighboringPorts[0].cullable()) {
                ports[0] = false;
            }
        }
        if (ports[1] && ports[2] && neighboringPorts[1].neighboringPorts[5] == neighboringPorts[2]) {
            if(neighboringPorts[2].cullable()) {
                ports[2] = false;
            }
        }
        if (ports[7] && ports[6] && neighboringPorts[7].neighboringPorts[3] == neighboringPorts[6]) {
            if(neighboringPorts[6].cullable()) {
                ports[6] = false;
            }
        }
        if (ports[7] && ports[8] && neighboringPorts[7].neighboringPorts[5] == neighboringPorts[8]) {
            if(neighboringPorts[8].cullable()) {
                ports[8] = false;
            }
        }
    }

    public void sanitisePorts() {
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                if (!ports[i]) {
                    neighboringPorts[i] = null;
                }
            }
        }
    }


    @Override
    public void reset() {
        for (int i = 0; i < 9; i++) {
            this.neighboringPorts[i] = null;
            this.portColours[i] = new Colour();
            this.ports[i] = false;
        }
    }
}
