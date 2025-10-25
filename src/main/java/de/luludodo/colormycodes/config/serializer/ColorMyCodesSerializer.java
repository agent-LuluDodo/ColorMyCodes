package de.luludodo.colormycodes.config.serializer;

import com.google.gson.*;
import de.luludodo.colormycodes.formatting.ConfigFormatting;

import java.lang.reflect.Type;
import java.util.HashMap;

public class ColorMyCodesSerializer extends MapSerializer<String, ConfigFormatting> {

    @Override
    public HashMap<String, ConfigFormatting> deserializeContent(JsonElement jsonElement, int fromVersion, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (fromVersion != getVersion())
            throw new JsonParseException("Unsupported config version v" + fromVersion + ", expected version v" + getVersion());

        HashMap<String, ConfigFormatting> map = new HashMap<>();

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (String name : jsonObject.keySet()) {
            JsonObject value = jsonObject.getAsJsonObject(name);

            ConfigFormatting formatting;
            if (!value.has("code")) {
                formatting = new ConfigFormatting(name);
            } else {
                String codeStr = value.get("code").getAsString();

                if (codeStr.length() != 1)
                    throw new JsonParseException("Invalid 'code' for " + name + ": " + codeStr + " (expected singular character)");

                char code = codeStr.charAt(0);

                if (value.has("color")) {
                    String color = value.get("color").getAsString();
                    if (!color.matches("#[0-9A-F]{6}")) {
                        throw new JsonParseException("Invalid 'color' for " + name + ": " + color);
                    }

                    int colorValue = Integer.parseInt(color.substring(1), 16);

                    formatting = new ConfigFormatting(name, code, colorValue);
                } else if (value.has("modifier")) {
                    boolean modifier = value.get("modifier").getAsBoolean();

                    formatting = new ConfigFormatting(name, code, modifier);
                } else {
                    throw new JsonParseException("Expected either 'color' or 'modifier' to be defined if 'code' is defined!");
                }
            }

            map.put(name, formatting);
        }

        return map;
    }
}
