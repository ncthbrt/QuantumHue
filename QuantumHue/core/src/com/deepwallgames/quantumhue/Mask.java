package com.deepwallgames.quantumhue;

import com.deepwallgames.quantumhue.value_objects.DRotation;
import com.deepwallgames.quantumhue.value_objects.Rotation;

/**
 * Created by nick on 23/11/2015.
 */
public class Mask {
    private boolean [] mask=new boolean[8];
    public boolean masked(int portnumber){
        if(portnumber<4){
            return mask[portnumber];
        }
        else if(portnumber<9){
            return mask[portnumber-1];
        }
        throw new ArrayIndexOutOfBoundsException("Mask has only 9 slots");
    }

    public Mask(){}
    public Mask(boolean []mask){
        if(mask.length==8) {
            this.mask =mask;
        }
    }


    public Mask rotate(DRotation rotation){
        Rotation realRot=rotation.rotation;
        boolean[] newMask=new boolean[8];
        for(int i=-1; i<=1; ++i){
            for(int j=-1; j<=1; ++j){
                if(j!=0 || i!=0){
                    int x=Math.round(realRot.x(i,j));
                    int y=Math.round(realRot.x(i,j));
                    int index=(y+1)*3+(x+1);
                    if(index<4){
                        newMask[index]=true;
                    }else{
                        newMask[--index]=true;
                    }
                }
            }
        }
        return new Mask(newMask);
    }
}
