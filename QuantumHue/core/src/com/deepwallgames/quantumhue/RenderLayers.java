package com.deepwallgames.quantumhue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by nick_000 on 08/11/2015.
 */
public class RenderLayers implements Collection<FrameBuffer> {

    @Override
    public int size() {
        return layerBuffers.size;
    }

    @Override
    public boolean isEmpty() {
        return layerBuffers.size==0;
    }

    @Override
    public boolean contains(Object o) {
        if(o.getClass()==FrameBuffer.class){
            for(FrameBuffer buffer:layerBuffers){
                if(buffer.equals(o)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterator<FrameBuffer> iterator() {
        return new Array.ArrayIterator<FrameBuffer>(layerBuffers);
    }

    @Override
    public Object[] toArray() {
        return layerBuffers.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return a;
    }

    @Override
    public boolean add(FrameBuffer buffer) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends FrameBuffer> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
    }

    public enum LevelEditorLayer {UTILITY, GLOW_EFFECT, BASE}

    private final Array<FrameBuffer> layerBuffers;

    public RenderLayers(){
        layerBuffers=new Array<FrameBuffer>(LevelEditorLayer.values().length);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public <E extends Enum<E>> FrameBuffer get(E layer){
            return layerBuffers.get(layer.ordinal());
    }

    public void resize(int width, int height){
        for(int i=0; i< LevelEditorLayer.values().length; ++i){
            layerBuffers.add(new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false));
        }
    }
}
