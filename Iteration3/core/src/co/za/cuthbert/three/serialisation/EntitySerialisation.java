package co.za.cuthbert.three.serialisation;

import co.za.cuthbert.three.TileType;
import co.za.cuthbert.three.components.ColourComponent;
import co.za.cuthbert.three.components.DVector3;
import co.za.cuthbert.three.components.PortComponent;
import co.za.cuthbert.three.components.TileTypeComponent;
import co.za.cuthbert.three.components.interfaces.AColourComponent;
import co.za.cuthbert.three.components.interfaces.ADVector3;
import co.za.cuthbert.three.components.interfaces.ANamedComponent;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;


/** This class is to serialise and deserialise the entities pertinent to the level design,
 *  it will not store transient information, other than position, and hence is not suitable for
 *  saving and loading games
 * Copyright Nick Cuthbert, 2014.
 */
public class EntitySerialisation implements JsonSerializer<Entity>, JsonDeserializer<Entity>{

    private static final HashMap<Class<? extends ANamedComponent>, JsonCustomSerializer> serialisers=new HashMap<Class<? extends ANamedComponent>, JsonCustomSerializer>();
    private static final HashMap<String,JsonCustomDeserializer<? extends ANamedComponent>> deserialisers=new HashMap<String, JsonCustomDeserializer<? extends ANamedComponent>>();


    private static final ComponentMapper<TileTypeComponent> tileTypeMapper=ComponentMapper.getFor(TileTypeComponent.class);

    public static final String TYPE_LABEL="type";
    public static final String TILE_TYPE_LABEL="type";
    public static final String COMPONENT_LABEL="component";
    private static final String COMPONENT_ARRAY_LABEL="components";
    public EntitySerialisation(){
        ColourComponentSerialisation colourComponentSerialisation = new ColourComponentSerialisation();
        registerCustomSerialisationMapping(ColourComponent.TYPE_NAME, AColourComponent.class, colourComponentSerialisation, colourComponentSerialisation);

        DVector3Serialisation dVector3Serialisation = new DVector3Serialisation();
        registerCustomSerialisationMapping(DVector3.TYPE_NAME, ADVector3.class, dVector3Serialisation, dVector3Serialisation);
    }


    private void registerCustomSerialisationMapping(String typeName, Class<? extends ANamedComponent> serialisationType, JsonCustomSerializer serializer, JsonCustomDeserializer<? extends ANamedComponent> deserializer){
        serialisers.put(serialisationType,serializer);
        deserialisers.put(typeName,deserializer);
    }




    @Override
    public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Entity entity=new Entity();

        JsonObject jsonEntity=json.getAsJsonObject();
        if(jsonEntity.has(TILE_TYPE_LABEL)) {
            TileType type = TileType.findMatching(jsonEntity.get(TYPE_LABEL).getAsInt());
            TileTypeComponent tileTypeComponent = new TileTypeComponent(type);
            entity.add(tileTypeComponent);
        }
        JsonArray components=jsonEntity.getAsJsonArray(COMPONENT_ARRAY_LABEL);

        for(int i=0; i<components.size(); ++i){
            JsonObject jsonComponent=components.get(i).getAsJsonObject();
            String componentTypeName=jsonComponent.get(TYPE_LABEL).getAsString();
            if(deserialisers.containsKey(componentTypeName)){
                Component component=deserialisers.get(componentTypeName).deserialize(jsonComponent.get(COMPONENT_ARRAY_LABEL), context);
                entity.add(component);
            }
        }
        return entity;
    }

    @Override
    public JsonElement serialize(Entity tile, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonEntity = new JsonObject();
            JsonArray components = new JsonArray();
            if(TileType.isTile(tile)) {
                TileTypeComponent tileType = tileTypeMapper.get(tile);
                jsonEntity.addProperty(TILE_TYPE_LABEL, tileType.tileType().getCode());
            }
            ImmutableArray<Component> tileComponents = tile.getComponents();
            for (int j = 0; j < tileComponents.size(); ++j) {
                if (tileComponents.get(j) instanceof ANamedComponent) {
                    ANamedComponent namedComponent = (ANamedComponent) tileComponents.get(j);
                    if (serialisers.containsKey(namedComponent.getClass())) {
                        String componentName = namedComponent.getComponentName();
                        JsonObject component = new JsonObject();
                        component.addProperty(TYPE_LABEL, componentName);
                        component.add(COMPONENT_LABEL, serialisers.get(namedComponent.getClass()).serialize(namedComponent, context));
                        components.add(component);
                    }
                }
                jsonEntity.add(COMPONENT_ARRAY_LABEL, components);

            }
            return jsonEntity;
    }
}
