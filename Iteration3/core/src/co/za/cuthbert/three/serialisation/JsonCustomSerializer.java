package co.za.cuthbert.three.serialisation;

import co.za.cuthbert.three.components.interfaces.ADVector3;
import co.za.cuthbert.three.components.interfaces.ANamedComponent;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public interface JsonCustomSerializer {
    public JsonElement serialize(ANamedComponent src, JsonSerializationContext context);
}
