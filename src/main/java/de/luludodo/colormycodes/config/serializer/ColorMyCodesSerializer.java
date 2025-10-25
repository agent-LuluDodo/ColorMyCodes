package de.luludodo.colormycodes.config.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.luludodo.colormycodes.formatting.ConfigFormatting;

import java.lang.reflect.Type;
import java.util.HashMap;

public class ColorMyCodesSerializer extends MapSerializer<String, ConfigFormatting> {

    @Override
    public HashMap<String, ConfigFormatting> deserializeContent(JsonElement jsonElement, int fromVersion, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serializeContent(HashMap<String, ConfigFormatting> config, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }
}
