package de.luludodo.colormycodes.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.luludodo.colormycodes.config.serializer.MapSerializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * A json-based config.
 * The config is standalone except for the {@link MapSerializer}.
 * Logs use the name {@code Lulu/JsonMapConfig}.
 *
 * @param <K> The key class
 * @param <V> The value class
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public abstract class JsonMapConfig<K,V> {
    private static final Logger LOG = LoggerFactory.getLogger("Lulu/JsonMapConfig");

    /**
     * This exception is thrown if the config which is being read contains nothing.
     * This usually indicates a failure while previously saving the file.
     */
    public static class EmptyFileException extends InvalidJsonException {
        public EmptyFileException() {
            super();
        }
        public EmptyFileException(String message) {
            super(message);
        }
        public EmptyFileException(Throwable cause) {
            super(cause);
        }
        public EmptyFileException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * This exception is thrown if the config which is being read contains malformed json.
     * This shouldn't happen unless the file corrupted or was edited manually.
     */
    public static class InvalidJsonException extends IOException {
        public InvalidJsonException() {
            super();
        }
        public InvalidJsonException(String message) {
            super(message);
        }
        public InvalidJsonException(Throwable cause) {
            super(cause);
        }
        public InvalidJsonException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private final Gson gson;
    private final File file;
    private final String filename;
    private final String[] oldFilenames;
    private final Type type;
    private boolean loaded = false;
    protected Map<K, V> content;
    /**
     * The constructor for JsonMapConfig.
     * This is used to do one-time configurations such as the filename.
     *
     * @param filename The filename which is used to save and read the config
     * @param serializer The serializer which is used to parse the json
     * @param oldFilenames A list of old filenames which will be moved to the new location and replace the current file if found
     */
    public JsonMapConfig(String filename, int version, MapSerializer<K, V> serializer, String... oldFilenames) {
        this(filename, version, serializer, true, oldFilenames);
    }

    /**
     * The constructor for JsonMapConfig.
     * This is used to do one-time configurations such as the filename.
     *
     * @param filename The filename which is used to save and read the config
     * @param load If the config should be loaded by the construction (using {@link JsonMapConfig#reload()}
     * @param serializer The serializer which is used to parse the json
     * @param oldFilenames A list of old filenames which will be moved to the new location and replace the current file if found
     */
    public JsonMapConfig(String filename, int version, MapSerializer<K, V> serializer, boolean load, String... oldFilenames) {
        this.filename = Path.of(filename).toString();
        serializer.setVersion(version);
        this.oldFilenames = oldFilenames;
        type = new TypeToken<Map<K, V>>(){}.getType();
        gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(type, serializer).create();
        file = FabricLoader.getInstance().getConfigDir().resolve(filename + ".json").toFile();

        if (load) {
            reload();
        }
    }

    /**
     * @return The {@link Gson} instance used by this {@link JsonMapConfig}.
     */
    protected Gson getGson() {
        return gson;
    }

    /**
     * @return The {@link File} under which the config gets saved.
     */
    protected File getFile() {
        return file;
    }

    /**
     * @return The {@code filename} under which the config is saved.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @return Old {@code filenames} which the config tries to load.
     */
    public String[] getOldFilenames() {
        return oldFilenames;
    }

    /**
     * Returns the {@link Type} used for the {@link Gson} instance.
     * @return The {@link Type} used for the {@link Gson} instance.
     */
    protected Type getType() {
        return type;
    }

    /**
     * @return If the config has been loaded.
     */
    public boolean isLoaded() {
        return loaded;
    }

    private void tryRestoreOldSettings() {
        if (getFile().exists()) return;
        for (String oldFilename : getOldFilenames()) {
            File oldFile = FabricLoader.getInstance().getConfigDir().resolve(oldFilename + ".json").toFile();
            if (!oldFile.exists()) continue;
            try {
                getFile().getParentFile().mkdirs();
                FileUtils.moveFile(oldFile, getFile());
                LOG.info("Restored old settings from {}.json to {}.json!", oldFilename, getFilename());
                break;
            } catch (IOException e) {
                LOG.error("Cannot restore old settings for {}.json from {}.json!", getFilename(), oldFilename, e);
            }
        }
    }

    private Map<K, V> read(File file) throws IOException {
        String contentToParse = FileUtils.readFileToString(file, Charset.defaultCharset());
        if (contentToParse.isBlank()) {
            throw new EmptyFileException();
        }

        try {
            return getGson().fromJson(contentToParse, getType());
        } catch (Exception e) {
            throw new InvalidJsonException(e);
        }
    }

    /**
     * @return The defaults for this config.
     */
    protected abstract String getDefaultResource();

    /**
     * Reloads the config.
     * Tries to restore old configs first and will override the current config if one is found.
     * Logs errors in case of failure.
     */
    public boolean reload() {
        tryRestoreOldSettings();
        boolean successful = false;
        try {
            content = read(getFile());
            LOG.info("Loaded {}.json!", getFilename());
            successful = true;
        } catch (EmptyFileException e) {
            LOG.error("Couldn't read config {}.json, because the file is empty!", getFilename(), e);
            content = new HashMap<>();
        } catch (InvalidJsonException e) {
            LOG.error("Couldn't parse config {}.json!", getFilename(), e);
            content = new HashMap<>();
        } catch (NoSuchFileException | FileNotFoundException e) {
            LOG.warn("Couldn't find config {}.json!", getFilename(), e); // Warning since it could be the first time launching
            content = new HashMap<>();
            saveDefaults();
        } catch (IOException e) {
            LOG.error("Couldn't read config {}.json!", getFilename(), e);
            content = new HashMap<>();
        }
        LOG.info("Content: {} entries", content.size());
        loaded = true;
        return successful;
    }

    /**
     * Saves the config to the file specified in the initializer.
     * Logs errors in case of failure.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean saveDefaults() {
        try {
            if (!getFile().exists()) {
                getFile().getParentFile().mkdirs();
                getFile().createNewFile();
            }
        } catch (IOException e) {
            LOG.error("Couldn't create config {}.json!", getFilename(), e);
            return false;
        }
        try (FileWriter writer = new FileWriter(getFile(), false)) {
            try (InputStream input = JsonMapConfig.class.getResourceAsStream(getDefaultResource())) {
                if (input == null)
                    throw new IllegalStateException("Couldn't read default resource from " + getDefaultResource() + "!");

                int c;
                while ((c = input.read()) != -1) {
                    writer.write(c);
                }
            }
            LOG.info("Saved config {}.json!", getFilename());
            return true;
        } catch (IOException e) {
            LOG.error("Couldn't save config {}.json!", getFilename(), e);
            return false;
        }
    }

    /**
     * Deletes the config and any empty parent directories up to the fabric config directory.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean delete() {
        if (!getFile().exists()) {
            LOG.info("Can't delete config {}.json because it doesn't exist!", getFilename());
            return true;
        }

        if (!getFile().delete()) {
            LOG.warn("Failed to delete config {}.json!", getFilename());
            return false;
        }
        LOG.info("Deleted config {}.json!", getFilename());
        Path path = getFile().toPath().getParent();
        Path configDir = FabricLoader.getInstance().getConfigDir();
        while (path.startsWith(configDir) && !path.equals(configDir)) {
            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
                if (!dirStream.iterator().hasNext()) {
                    path.toFile().delete();
                    LOG.info("Deleted empty parent directory {}!", configDir.relativize(path));
                    path = path.getParent();
                } else {
                    break;
                }
            } catch (IOException e) {
                LOG.error("Couldn't read directory data while deleting config {}.json!", getFilename(), e);
                return false;
            }
        }
        return true;
    }

    /**
     * Gets a value from the config using a key.
     *
     * @param key The key
     *
     * @return The value
     */
    public V get(K key) {
        return content.get(key);
    }

    /**
     * Checks if the config contains a specific key.
     *
     * @param key The key
     *
     * @return If the config contains said key
     */
    public boolean contains(K key) {
        return content.containsKey(key);
    }

    /**
     * @return All keys present in this config.
     */
    public Set<K> options() {
        return content.keySet();
    }

    /**
     * Does the specified action for every entry in the config.
     *
     * @param action The action
     */
    public void forEach(BiConsumer<K, V> action) {
        content.forEach(action);
    }
}
