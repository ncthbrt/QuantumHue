package com.deepwallgames.quantumhue.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class PixelMaskFactory {


    public PixelMaskFactory(){
    }


    public PixelMask getPixelMask(String resourceName){
        int index=resourceName.lastIndexOf('.');
        String bitmaskName=resourceName.substring(0,index)+"_bitmask.cim";
        FileHandle bitmaskHandle= Gdx.files.external(bitmaskName);
        PixelMask mask;
        if(bitmaskHandle.exists()){
            return toPixelMask(PixmapIO.readCIM(bitmaskHandle));
        }
        else{
            return exportMask(resourceName);
        }
    }

    public  Pixmap getPixmap(String resourceName){
        Pixmap pixmap=new Pixmap(Gdx.files.internal(resourceName));
        Pixmap.Format format = Pixmap.Format.RGBA8888;
        if (pixmap.getFormat()!=format) {
            Pixmap tmp = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), format);
            tmp.drawPixmap(pixmap, 0, 0); //copy pix to tmp
            pixmap.dispose();
            return tmp;
        }
        return pixmap;
    }


    public PixelMask toPixelMask(Pixmap maskPixmap){
        PixelMask mask=new PixelMask(maskPixmap.getWidth(),maskPixmap.getHeight());
        for(int j=0; j<maskPixmap.getHeight(); j++){
            for(int i=0; i<maskPixmap.getWidth(); i++){
                int value=maskPixmap.getPixel(i,j);
                int alpha = ((value & 0xff));
                if(alpha>200){
                    mask.set(i,j,true);
                }else {
                    mask.set(i,j,false);
                }
            }
        }
        maskPixmap.dispose();
        return mask;
    }


    public PixelMask exportMask(String resourceName){
        int index=resourceName.lastIndexOf('.');
        String bitmaskName=resourceName.substring(0, index)+"_bitmask.cim";
        FileHandle bitmaskHandle= Gdx.files.local(bitmaskName);
        Pixmap pixmap=new Pixmap(Gdx.files.internal(resourceName));
        Pixmap maskPixmap=convertToBitmask(pixmap);
        PixmapIO.writeCIM(bitmaskHandle, maskPixmap);
        return toPixelMask(maskPixmap);
    }

    public Pixmap convertToBitmask(Pixmap old){
        Pixmap.Format format = Pixmap.Format.Alpha;
        if (old.getFormat()!=format) {
            Pixmap tmp = new Pixmap(old.getWidth(), old.getHeight(), format);
            tmp.drawPixmap(old, 0, 0); //copy pix to tmp
            old.dispose();
            return tmp;
        }
        return old;
    }

}
