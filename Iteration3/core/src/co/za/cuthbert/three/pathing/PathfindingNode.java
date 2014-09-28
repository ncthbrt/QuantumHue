package co.za.cuthbert.three.pathing;

import com.badlogic.ashley.core.Entity;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class PathfindingNode implements Comparable<PathfindingNode>{
    Entity tile;
    private final float heuristicCost;
    private float cost;


    public PathfindingNode(float heuristicCost, float cost,PathfindingNode predecessor){
        this.cost=cost;
        this.predecessor=predecessor;
        this.heuristicCost=heuristicCost;
    }

    private PathfindingNode predecessor=null;
    public PathfindingNode predecessor(){
        return predecessor;
    }

    public float estimatedCost(){
        return heuristicCost+cost;
    }
    public float currentCost(){
        return cost;
    }

    public void adjust(float cost, PathfindingNode predecessor){
        this.cost=cost;
        this.predecessor=predecessor;
    }

    public float heuristCost(){
        return heuristicCost;
    }


    @Override
    public boolean equals(Object obj) {
        PathfindingNode otherNode;
            if (obj instanceof PathfindingNode){
                otherNode=(PathfindingNode)obj;
            }else{
                return false;
            }

            if (compareTo(otherNode)!=0){
                return false;
            }else {
                return true;
            }
    }

    @Override
    public int compareTo(PathfindingNode other) {
        if(estimatedCost()>other.estimatedCost()){
            return 1;
        }else if(estimatedCost()<other.estimatedCost()){
            return -1;
        }
        return 0;
    }
}
