package com.deepwallgames.quantumhue.value_objects;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class HSVColour {
    private float h,s, v,a;
    public HSVColour(float h, float s, float v, float a){
        this.h=h;
        this.s=s;
        this.v =v;
        this.a=a;
    }

    public HSVColour(){
        this.h=0;
        this.s=0;
        this.v =0;
        this.a=0;
    }
    public HSVColour(HSVColour hsvColour){
        h=hsvColour.h;
        s=hsvColour.s;
        v=hsvColour.v;
        a=hsvColour.a;
    }

    public HSVColour(Colour colour){
       HSVColour hsvColour=colour.toHSV();
        h=hsvColour.h;
        s=hsvColour.s;
        v=hsvColour.v;
        a=hsvColour.a;
    }

    public float hue(){
        return h;
    }
    public void hue(float hue){
        this.h=hue;
    }

    public float saturation(){
        return s;
    }
    public void saturation(float saturation){
        this.s=saturation;
    }

    public float value(){
        return v;
    }
    public void value(float value){
        this.v=value;
    }
    public float alpha(){
        return a;
    }

    public void alpha(float alpha){
        this.a=alpha;
    }

    public void add(HSVColour colour) {
        this.h+=colour.h;
        if(h>1){
            h=1;
        }
        this.s+=colour.s;
        if(s>1){
            s=1;
        }
        this.v+=colour.v;
        if(v>1){
            v=1;
        }
        this.a+=colour.a;
        if(a>1){
            a=1;
        }
    }

    public void subtract(HSVColour colour) {
        this.h-=colour.h;
        if(h<0){
            h=0;
        }
        this.s-=colour.s;
        if(s<0){
            s=0;
        }
        this.v-=colour.v;
        if(v<0){
            v=0;
        }
        this.a-=colour.a;
        if(a<0){
            a=0;
        }
    }

    public static HSVColour add(HSVColour hsvColour1, HSVColour hsvColour2){
        HSVColour copy=new HSVColour(hsvColour1);
        copy.add(hsvColour2);
        return copy;
    }


    public static HSVColour subtract(HSVColour hsvColour1, HSVColour hsvColour2){
        HSVColour copy=new HSVColour(hsvColour1);
        copy.subtract(hsvColour2);
        return copy;
    }



    //Formula for conversion sourced from easyRGB.com
    public Colour toRGB(){
        float red,green, blue;
        int alpha=(int)(a*255);
        if(s==0){
            return new Colour((int)(v *255),(int)(v *255),(int)(v *255),alpha);
        }else {
            float hTemp;
            if(hue()>=1){//Hue must be less than 1
                hTemp=(hue()%1)*6;
            }else {
                hTemp = hue() * 6;
            }
            int i=(int)hTemp;//Break up h into six groupings
            float p1=value()*(1-saturation());
            float p2=value()*(1-saturation()*(hTemp-i));
            float p3=value()*(1-saturation()*(1-(hTemp-i)));
            switch (i){
                case 0:
                    red=value();
                    green=p3;
                    blue=p1;
                    break;
                case 1:
                    red=p2;
                    green=value();
                    blue=p1;
                    break;
                case 2:
                    red=p1;
                    green=value();
                    blue=p3;
                    break;
                case 3:
                    red=p1;
                    green=p2;
                    blue=value();
                    break;
                case 4:
                    red=p3;
                    green=p1;
                    blue=value();
                    break;
                default:
                    red=value();
                    green=p1;
                    blue=p2;
                    break;
            }
        }
        return new Colour((int)(red *255),(int)(green *255),(int)(blue *255),alpha);
    }

}
