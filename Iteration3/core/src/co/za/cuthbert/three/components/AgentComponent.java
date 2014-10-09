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
    private List<DVector2> path =null;
    private static final float movementSpeed=0.3f;
    public float radius= Config.TILE_SIZE/1.8f;
    public boolean alive=true;

    public AgentComponent(){
        position=new DVector2();
    }

    public Vector2 position(){
        if(path ==null){
            return new Vector2(position.x(),position.y());
        }else{
            float posX= Interpolation.pow3In.apply(position.x(), path.get(0).x(), between);
            float posY= Interpolation.pow3In.apply(position.y(), path.get(0).y(), between);
            return new Vector2(posX,posY);
        }
    }

    public void position(DVector2 position){
        this.position.set(position.x(), position.y());
    }

    public void assignRoute(List<DVector2> route){
        this.path =route;
    }

    public List<DVector2> path(){
        return path;
    }

    public void advance(float deltaTime){
        if(path !=null){
            float delta=movementSpeed*deltaTime;
            between+=delta;
            if(between>1){
                position.set(path.get(0).x(), path.get(0).y());
                path.remove(0);
                if(path.isEmpty()){
                    path =null;
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
