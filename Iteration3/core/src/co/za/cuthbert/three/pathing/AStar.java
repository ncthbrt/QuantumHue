package co.za.cuthbert.three.pathing;

import co.za.cuthbert.three.Level;
import co.za.cuthbert.three.components.ColourComponent;
import co.za.cuthbert.three.components.DVector2;
import co.za.cuthbert.three.components.Traversable;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;
import java.util.LinkedList;
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

    /**This gets the route and processes it so that it is in a better form
     * @param start
     * @param end
     * @param agent
     * @return
     */
    private LinkedList<DVector2> route(DVector2 start, DVector2 end, Entity agent){
        PathfindingNode endNode=revRoute(start,end,agent);
        if(end==null){
            return new LinkedList<DVector2>();
        }else{
            LinkedList<DVector2> result=new LinkedList<DVector2>();

            PathfindingNode currentNode=endNode;
            while(!currentNode.position.equals(start)){
                result.addFirst(currentNode.position);
                currentNode=currentNode.predecessor();

            }
            result.addFirst(currentNode.position);
            return result;
        }
    }

    /**This method returns the route from the start to the end, however this route is returned backwards.
     *
     * @param start
     * @param end
     * @param agent
     * @return
     */
    private PathfindingNode revRoute(DVector2 start, DVector2 end, Entity agent){
        if(start.equals(end)){
            return new PathfindingNode(start,0,0,null);
        }
        openList=new PriorityQueue<PathfindingNode>();
        closedList=new HashMap<DVector2, PathfindingNode>();

        openList.offer(new PathfindingNode(start,heuristic(start, end),0, null));
        return revRoute2(end, agent);
    }



    private PathfindingNode revRoute2(DVector2 end, Entity agent) {
        PathfindingNode node = openList.poll();
        for (int j = -1; j <= 1; j++) {
            for (int i = -1; i <= 1; i++) {
                if (!(i == 0 && j == 0) && node.position.x() + i >= 0 && node.position.x() + i < level.width() && node.position.y() + j >= 0 && node.position.y() + j < level.height()) {
                    Entity tile=level.get(node.position.x()+i,node.position.y()+j);
                    if(tile!=null) {
                        ImmutableArray<Component> components = tile.getComponents();
                        for (int k = 0; k < components.size(); ++k) {
                            if (components.get(k) instanceof Traversable) {
                                Traversable traversable = (Traversable) components.get(k);
                                if (traversable.traversable(agent)) {
                                    float movementCost = traversable.movementCost(agent);
                                    DVector2 pos = new DVector2(node.position.x() + i, node.position.y() + j);
                                    PathfindingNode newNode = new PathfindingNode(pos,heuristic(pos, end),movementCost+node.currentCost(),node);
                                    if(!closedList.containsKey(pos)){
                                        openList.offer(newNode);
                                    }else if(closedList.get(pos).compareTo(newNode)>0){
                                        closedList.remove(pos);
                                        openList.offer(newNode);
                                    }

                                }
                                break;
                            }
                        }

                    }
                }

            }
        }
        closedList.put(node.position,node);
        if(openList.size()==0){
            return null;
        }else if(openList.peek().heuristCost()<1){
            return openList.poll();
        }else {
            return revRoute2(end,agent);
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
