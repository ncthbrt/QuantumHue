package com.deepwallgames.quantumhue.components;

import com.deepwallgames.quantumhue.value_objects.Colour;
import com.deepwallgames.quantumhue.value_objects.DiscreteColour;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class PortComponent implements Pool.Poolable, Component {
    private final Colour[] incomingPortColours;
    private final Colour[] outgoingPortColours;
    private byte portMask;
    private byte groundDirection;


    public static int portNumber(int relativeX, int relativeY) {
        return (3 * (relativeY + 1) + (relativeX + 1));
    }

    public static int adjacentPortNumber(int portNumber) {
        return 8 - portNumber;
    }

    public byte groundDirection(){
        return this.groundDirection;
    }

    public boolean groundDirection(int port){
        if(port>4){
            --port;
        }
        return (groundDirection& 1<<portMask)>0;
    }

    public void resetGroundDirection(){
        groundDirection=0;
    }

    public void groundDirection(int port, boolean yes){
        if(port>4){
            --port;
        }
        groundDirection=(byte)((groundDirection & ((byte)~(1<<portMask)))|((yes?1:0)<<port));
    }

    public PortComponent() {
        incomingPortColours = new Colour[8];
        outgoingPortColours = new Colour[8];
        portMask = 0;
        groundDirection=0;
        reset();
    }

    public boolean portMask(int port){
        if(port>4){
            return (portMask&(1<<(port-1)))>0;
        }else{
            return (portMask&(1<<port))>0;
        }
    }

    public byte portMask() {
        return portMask;
    }

    public void mask(byte portMask) {
        this.portMask=portMask;
        for (int i = 0; i < 8; ++i) {
            if ((portMask&(1<<i))>0){
                incomingPortColours[i]=DiscreteColour.ALPHA.toColour();
            }
        }
    }


    public void mask(int port, boolean active) {
        if(port>4){
            this.portMask=(byte)((this.portMask&((byte)~(1<<(port-1))))|((active?1:0)<<(port-1)));
            if (!active) {
                incomingPortColours[port-1]=DiscreteColour.ALPHA.toColour();
            }
        }else{
            this.portMask=(byte)((this.portMask&((byte)~(1<<port)))|((active?1:0)<<(port)));
            if (!active) {
                incomingPortColours[port]=DiscreteColour.ALPHA.toColour();
            }
        }
    }

    public Colour[] incomingPortColours() {
        return incomingPortColours;
    }

    public void incomingPortColour(int port, Colour colour){
        mask(port,true);
        if(port>4) {
            this.incomingPortColours[port-1] =(colour);
        }else{
            this.incomingPortColours[port]=(colour);
        }
    }

    public Colour incomingPortColour(int port) {
        if(port>4) {
            return incomingPortColours[port-1];
        }else{
            return incomingPortColours[port];
        }
    }

    public void incomingPortColours(Colour[] outgoingPortColours) {
        System.arraycopy(outgoingPortColours, 0, this.incomingPortColours, 0, outgoingPortColours.length);
    }

    public Colour[] outgoingPortColours() {
        return outgoingPortColours;
    }

    public void outgoingPortColour(int port, Colour colour) {
        if(port>4) {
            this.outgoingPortColours[port-1]=(colour);
        }else{
            this.outgoingPortColours[port]=(colour);
        }
    }

    public Colour outgoingPortColour(int port) {
        if(port>4){
            return outgoingPortColours[port-1];
        }else{
            return outgoingPortColours[port];
        }
    }

    public void outgoingPortColours(Colour[] outgoingPortColours) {
        System.arraycopy(outgoingPortColours, 0, this.outgoingPortColours, 0, outgoingPortColours.length);
    }



    public void outgoingPortColours(Colour colour) {
        for (int i = 0; i < outgoingPortColours.length; ++i) {
            this.outgoingPortColours[i] = colour;
        }
    }

    @Override
    public void reset() {
        for (int i = 0; i < incomingPortColours.length; i++) {
            this.incomingPortColours[i] = DiscreteColour.ALPHA.toColour();
            this.outgoingPortColours[i] = DiscreteColour.ALPHA.toColour();
        }
        this.portMask = 0;
        this.groundDirection=0;
    }
}
