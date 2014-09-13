package co.za.cuthbert.three.serialisation;

import co.za.cuthbert.three.components.ColourComponent;
import co.za.cuthbert.three.components.interfaces.ANamedComponent;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public interface JsonCustomDeserializer<T extends ANamedComponent> {
    public T deserialize(JsonElement json, JsonDeserializationContext context);
}
