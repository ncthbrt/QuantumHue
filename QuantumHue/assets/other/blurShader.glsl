#version 100
#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
//"in" attributes from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;

//declare uniforms
uniform sampler2D u_texture;
uniform float blur;
uniform vec2 dir;

void main() {

    //this will be our RGBA sum
    vec3 sum = vec3(0.0);

    //our original texcoord for this fragment
    vec2 tc = vTexCoord;

    //the amount to blur, i.e. how far off center to sample from
    //1.0 -> blur by one pixel
    //2.0 -> blur by two pixels, etc.

    //the direction of our blur
    //(1.0, 0.0) -> x-axis blur
    //(0.0, 1.0) -> y-axis blur
    float hstep = dir.x;
    float vstep = dir.y;

    //apply blurring, using a 9-tap filter with predefined gaussian weights

    vec4 v1= texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep));
    vec4 v2=texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep));
    vec4 v3=texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep));
    vec4 v4= texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep));

    vec4 v5= texture2D(u_texture, vec2(tc.x, tc.y));

    vec4 v6= texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep));
    vec4 v7= texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep));
    vec4 v8= texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep));
    vec4 v9= texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep));

    sum += v1.rgb*v1.a* 0.0162162162;
    sum += v2.rgb*v2.a* 0.0540540541;
    sum += v3.rgb*v3.a* 0.1216216216;
    sum += v4.rgb*v4.a* 0.1945945946;

    sum += v5.rgb*v5.a * 0.2270270270;

    sum += v6.rgb*v6.a * 0.1945945946;
    sum += v7.rgb*v7.a * 0.1216216216;
    sum += v8.rgb*v8.a* 0.0540540541;
    sum += v9.rgb*v9.a * 0.0162162162;
    float a=(v1.a+v9.a)*0.0162162162+(v2.a+v8.a)*0.0540540541+(v3.a+v7.a)*0.1216216216+(v4.a+v6.a)*0.1945945946+0.2270270270*v5.a;

    gl_FragColor = vec4(sum,a*4.0);
}