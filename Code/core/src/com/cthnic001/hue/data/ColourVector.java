package com.cthnic001.hue.data;

import com.cthnic001.hue.components.Poolable;
import com.cthnic001.hue.data.Colour;
import com.cthnic001.hue.data.ColourBracket;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourVector implements Poolable {
    public List<ColourBracket> line;

    public ColourVector() {
        line = new ArrayList<ColourBracket>();
        line.add(new ColourBracket());
    }

    public void advance(float delta, Colour portColour) {
        if (!portColour.equals(line.get(0).colour)) {
            line.add(0, new ColourBracket(portColour));
        }
        for (int i = 1; i < line.size(); i++) {
            line.get(i).advance(delta);
            if (line.get(i).position() >= 1f) {
                line.remove(i--);
            }
        }
    }

    @Override
    public void reset() {
        line.clear();
        line.add(new ColourBracket());
    }
}
