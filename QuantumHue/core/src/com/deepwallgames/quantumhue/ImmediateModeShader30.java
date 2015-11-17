package com.deepwallgames.quantumhue;

/**
 * Created by nick on 12/11/2015.
 */
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/** Immediate mode rendering class for GLES 2.0. The renderer will allow you to specify vertices on the fly and provides a default
 * shader for (unlit) rendering.</p> *
 *
 * @author mzechner */
public class ImmediateModeShader30 {

    static private String createVertexShader (boolean hasNormals, boolean hasColors, int numTexCoords) {
        String prefix="#version 100\n";
        String shader = prefix +
                "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
                + (hasNormals ? "attribute vec3 " + ShaderProgram.NORMAL_ATTRIBUTE + ";\n" : "")
                + (hasColors ? "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" : "");

        for (int i = 0; i < numTexCoords; i++) {
            shader += "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + i + ";\n";
        }

        shader += "uniform mat4 u_projModelView;\n";
        shader += (hasColors ? "varying vec4 v_col;\n" : "");

        for (int i = 0; i < numTexCoords; i++) {
            shader += "varying vec2 v_tex" + i + ";\n";
        }

        shader += "void main() {\n" + "   gl_Position = u_projModelView * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
                + (hasColors ? "   v_col = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" : "");

        for (int i = 0; i < numTexCoords; i++) {
            shader += "   v_tex" + i + " = " + ShaderProgram.TEXCOORD_ATTRIBUTE + i + ";\n";
        }
        shader += "   gl_PointSize = 1.0;\n";
        shader += "}\n";
        return shader;
    }

    static private String createFragmentShader (boolean hasNormals, boolean hasColors, int numTexCoords) {
        String prefix= "#version 100\n";
        String shader = prefix +
                 "#ifdef GL_ES\n" + "precision mediump float;\n" + "#endif\n";

        if (hasColors) shader += "varying vec4 v_col;\n";
        for (int i = 0; i < numTexCoords; i++) {
            shader += "varying vec2 v_tex" + i + ";\n";
            shader += "uniform sampler2D u_sampler" + i + ";\n";
        }

        shader += "void main() {\n" + "   gl_FragColor = " + (hasColors ? "v_col" : "vec4(1, 1, 1, 1)");

        if (numTexCoords > 0) shader += " * ";

        for (int i = 0; i < numTexCoords; i++) {
            if (i == numTexCoords - 1) {
                shader += " texture2D(u_sampler" + i + ",  v_tex" + i + ")";
            } else {
                shader += " texture2D(u_sampler" + i + ",  v_tex" + i + ") *";
            }
        }

        shader += ";\n}";
        return shader;
    }

    /** Returns a new instance of the default shader used by SpriteBatch for GL2 when no shader is specified. */
    static public ShaderProgram createDefaultShader (boolean hasNormals, boolean hasColors, int numTexCoords) {
        String vertexShader = createVertexShader(hasNormals, hasColors, numTexCoords);
        String fragmentShader = createFragmentShader(hasNormals, hasColors, numTexCoords);
        ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);
        if (program.isCompiled() == false) throw new IllegalArgumentException("Error compiling shapeRenderer shader: " + program.getLog());
        return program;
    }
}
