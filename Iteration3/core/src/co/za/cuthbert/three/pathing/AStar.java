package co.za.cuthbert.three.pathing;

import co.za.cuthbert.three.Level;
import co.za.cuthbert.three.components.ColourComponent;
import co.za.cuthbert.three.components.DVector2;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class AStar implements Pool.Poolable{
    private Level level;
    private PriorityQueue<PathfindingNode> openList;
    private HashMap<DVector2,PathfindingNode> closedList;

    private static final ComponentMapper<ColourComponent> colourComponentMapper= ComponentMapper.getFor(ColourComponent.class);

    public AStar(Level level){
        this.level=level;

    }

    public PathfindingNode route(DVector2 start, DVector2 end, Entity agent){
        if(start.equals(end) || ){

            return new PathfindingNode(0,0,null);
        }
        openList=new PriorityQueue<PathfindingNode>();
        closedList=new HashMap<DVector2, PathfindingNode>();
        openList.offer(new PathfindingNode(heuristic(start,end),0, null));
        return route2(start,  end, agent);
    }

    private PathfindingNode route2(DVector2 start, DVector2 end, Entity agent) {
        PathfindingNode node = openList.poll();
        for (int j = -1; j <= 1; j++) {
            for (int i = -1; i <= 1; i++) {
                if (!(i==0 && j==0)){

                }

            }
        }
    }

    private float heuristic(DVector2 position,DVector2 target){
        DVector2 dist=DVector2.delta(target,position);
        return DVector2.chebyshevDistance(dist);
    }




    @Override
    public void reset() {
        openList.clear();
        closedList.clear();
    }
}
