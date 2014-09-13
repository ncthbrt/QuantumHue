package co.za.cuthbert.three.serialisation;

import co.za.cuthbert.three.components.ColourComponent;
import co.za.cuthbert.three.components.interfaces.AColourComponent;
import co.za.cuthbert.three.components.interfaces.ANamedComponent;
import co.za.cuthbert.three.data.Colour;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public class ColourComponentSerialisation implements JsonCustomDeserializer<ColourComponent> , JsonCustomSerializer{


    private static final String COLOUR_LABEL ="colour";
    @Override
    public JsonElement serialize(ANamedComponent src, JsonSerializationContext context) {
        if(src instanceof AColourComponent ){
            JsonObject jsonComponent = new JsonObject();
            jsonComponent.addProperty(COLOUR_LABEL, ((AColourComponent)(src)).colour().toRGBA());
            return jsonComponent;
        }
        return null;
    }

    @Override
    public ColourComponent deserialize(JsonElement json, JsonDeserializationContext context) throws JsonParseException {
        int jsonColour=json.getAsJsonObject().get(COLOUR_LABEL).getAsInt();
        Colour colour=new Colour(jsonColour);
        ColourComponent returnable=new ColourComponent();
        returnable.colour().set(colour);
        return returnable;
    }
}
