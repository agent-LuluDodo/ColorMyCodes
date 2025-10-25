package de.luludodo.colormycodes.config.serializer;

import com.google.gson.*;
import de.luludodo.colormycodes.config.JsonMapConfig;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * The serializer for {@link JsonMapConfig}.
 * Adds some basic versioning, so that I can update the config layout down the road.
 *
 * @param <K> The key class
 * @param <V> The value class
 */
@SuppressWarnings("unused")
public abstract class MapSerializer<K, V> implements JsonDeserializer<HashMap<K, V>> {
    private int version;

    /**
     * Used to set the current version by the {@link JsonMapConfig} initializer.
     *
     * @param version The current version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * This can differ from {@code fromVersion} if the config loaded was saved with a different version of the mod.
     *
     * @return The current version.
     */
    public int getVersion() {
        return version;
    }

    /**
     * Deserializes a {@link JsonObject} to a {@link HashMap}.
     * <p>
     * Wraps around {@link #deserializeContent(JsonElement, int, Type, JsonDeserializationContext)} to add versioning.
     *
     * @param jsonElement The element to deserialize
     * @param type The type to deserialize to
     * @param jsonDeserializationContext The current context
     *
     * @return The deserialized {@link HashMap}.
     *
     * @throws JsonParseException If something went wrong while parsing the json.
     */
    @Override
    public final HashMap<K, V> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        return deserializeContent(json.get("content"), json.get("version").getAsInt(), type, jsonDeserializationContext);
    }

    /**
     * @param jsonElement The element to deserialize
     * @param fromVersion The version the element was serialized with
     * @param type The type to deserialize to
     * @param jsonDeserializationContext The current context
     *
     * @return The deserialized {@link HashMap}.
     *
     * @throws JsonParseException If something went wrong while parsing the json.
     */
    public abstract HashMap<K, V> deserializeContent(JsonElement jsonElement, int fromVersion, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException;
}
