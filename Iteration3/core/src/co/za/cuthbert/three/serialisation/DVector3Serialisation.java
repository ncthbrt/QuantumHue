package co.za.cuthbert.three.serialisation;

import co.za.cuthbert.three.components.DVector3;
import co.za.cuthbert.three.components.interfaces.ADVector3;
import co.za.cuthbert.three.components.interfaces.ANamedComponent;
import com.badlogic.ashley.core.Component;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class DVector3Serialisation implements JsonCustomDeserializer<DVector3> , JsonCustomSerializer{

    @Override
    public DVector3 deserialize(JsonElement json, JsonDeserializationContext context) throws JsonParseException {
        int x=json.getAsJsonObject().get("x").getAsInt();
        int y=json.getAsJsonObject().get("y").getAsInt();
        int z=json.getAsJsonObject().get("z").getAsInt();
        DVector3 vector3=new DVector3(x,y,z);
        return vector3;
    }

    @Override
    public JsonElement serialize(ANamedComponent src, JsonSerializationContext context) {
        if(src instanceof ADVector3) {
            JsonObject object = new JsonObject();
            object.addProperty("x", ((ADVector3)src).x());
            object.addProperty("y", (((ADVector3)src).y()));
            object.addProperty("z", (((ADVector3)src).y()));
            return object;
        }
        return null;
    }
}
