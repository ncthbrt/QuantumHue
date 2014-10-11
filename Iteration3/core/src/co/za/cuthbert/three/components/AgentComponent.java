package co.za.cuthbert.three.components;

import co.za.cuthbert.three.Config;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import java.util.List;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class AgentComponent extends Component implements Pool.Poolable {
    private final DVector2 position;
    private float between =0; //How far between target and previous node agent is
    public float between(){
        return between;
    }
    public void between(float between){
        this.between=between;
    }
    private DVector2 next;
    private List<DVector2> path =null;
    public static final float movementSpeed=0.7f;
    public float radius=Config.TILE_SIZE/2.2f;

    public AgentComponent(){
        position=new DVector2();
    }

    public Vector2 position(){
        if(next ==null){
            return new Vector2(position.x(),position.y());
        }else{
            float posX= Interpolation.linear.apply(position.x(), next.x(), between);//TODO Verify that this interpolation method is symmetrical
            float posY= Interpolation.linear.apply(position.y(), next.y(), between);
            return new Vector2(posX,posY);
        }
    }

    public void shift(int deltaX, int deltaY){
        position.set(position.x()+deltaX,position.y()+deltaY);
        if(next!=null) {
            next.set(next.x() + deltaX, next.y() + deltaY);
        }
        if(path!=null) {
            for (DVector2 node : path) {
                node.set(node.x() + deltaX, node.y() + deltaY);
            }
        }
    }

    public DVector2 currentTile(){
        return position;
    }

    public DVector2 nextTile(){
        return next;
    }

    public void nextTile(DVector2 nextTile){
        this.next=nextTile;
    }



    public void position(DVector2 position){
        this.position.set(position.x(), position.y());
    }

    public List<DVector2> path(){
        return path;
    }


    public boolean followingPath(){
        return path!=null;
    }
    public void path(List<DVector2> path){
            this.path = path;
            if (next != null && path.get(0).equals(next)) {
                    between = 1 - between;
                    DVector2 temp = new DVector2(position.x(), position.y());
                    position.set(next.x(), next.y());
                    next = temp;
            }else if(path.size()>0){
                next=path.remove(0);
                if(path.isEmpty()){
                    this.path=null;
                }
            }

    }
    public void advance(float deltaTime){
        if(next !=null){
            float delta=movementSpeed*deltaTime;
            between+=delta;
            if(between>=1){
                position.set(next.x(), next.y());
                next=null;
                if(path!=null) {
                    next = path.remove(0);
                    if (path.isEmpty()) {
                        path = null;
                    }
                }
                between=0f;
            }
        }
    }

    @Override
    public void reset() {
        position.set(0,0);
    }
}
