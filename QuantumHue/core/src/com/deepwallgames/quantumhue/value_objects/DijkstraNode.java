package com.deepwallgames.quantumhue.value_objects;


import com.badlogic.ashley.core.Entity;


/**
 * Created by nick_000 on 09/11/2015.
 */
public class DijkstraNode  implements Comparable<DijkstraNode>{
    public Entity entity;
    public float distanceToGround;
    public DijkstraNode(Entity entity, float distanceToGround){
        this.entity=entity;
        this.distanceToGround=distanceToGround;

    }
    @Override
    public int compareTo(DijkstraNode o) {
        return Float.compare(distanceToGround, o.distanceToGround);
    }

    @Override
    public int hashCode() {
        return entity.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DijkstraNode && entity.equals(((DijkstraNode) obj).entity);
    }
}
