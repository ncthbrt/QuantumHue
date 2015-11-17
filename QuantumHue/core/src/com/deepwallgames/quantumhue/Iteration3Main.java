package com.deepwallgames.quantumhue;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.deepwallgames.quantumhue.components.*;
import com.deepwallgames.quantumhue.value_objects.Colour;
import com.deepwallgames.quantumhue.value_objects.HSVColour;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Iteration3Main extends Game {
    SpriteBatch batch;
    private static TextureAtlas atlas;
    private static final float colourCycleTime = 30f;
    private static HSVColour currentColour = new HSVColour(0, 0.81f, 0.7f, 1f);

    public static Colour currentColour() {
        return currentColour.toRGB();
    }

    private static TitleScreen titleScreen;
    private static Iteration3Main main;

    public static TextureAtlas textureAtlas() {
        return atlas;
    }

//    public static ShaderProgram defaultShader;
    @Override
    public void create() {
        ShaderProgram defaultShader=createDefaultShader();

        batch = new SpriteBatch(1000,defaultShader);


        atlas = new TextureAtlas(Gdx.files.internal("global.atlas"));
        titleScreen = new TitleScreen(this, atlas,batch);
        main = this;
        setScreen(titleScreen);
    }


    static public ShaderProgram createDefaultShader () {
        String prefix="#version 100\n";
        String vertexShader = prefix+
                "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_color.a = v_color.a * (255.0/254.0);\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader =
                prefix
                +"#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying LOWP vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
                + "}";

        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }


    @Override
    public void render() {
        float nextHue = currentColour.hue() + Gdx.graphics.getDeltaTime() / colourCycleTime;
        nextHue = nextHue > 1 ? 0 : nextHue;
        currentColour.hue(nextHue);
        Gdx.gl.glClearColor(0, 0, 0, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }
}
