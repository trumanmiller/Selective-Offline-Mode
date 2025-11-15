package jmeow.selectiveofflinemode.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashMap;

public class SelectiveOfflineModeConfig {
    public static final String MOD_ID = "selective-offline-mode";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve(MOD_ID)
            .resolve("selective-offline-mode.json");

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();


    /**
     *
     * @return HashMap of names to dates read from CONFIG_PATH
     */
    public static HashMap<String, Date> readConfig() {
        if (!Files.exists(CONFIG_PATH)) {
            return new HashMap<>();
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            HashMap<String, Date> map = GSON.fromJson(
                    reader,
                    new TypeToken<HashMap<String, Date>>() {
                    }.getType()
            );
            return map != null ? new HashMap<>(map) : new HashMap<>();

        } catch (IOException | JsonSyntaxException e) {
            LOGGER.error("an exception was thrown trying to read config file at {} {}", CONFIG_PATH, e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * prunes outdated names from hashmap and saves to config, creating parents if needed
     *
     * @param map HashMap of names to dates to persist to config file
     */

    public static void saveConfig(HashMap<String, Date> map) {


        Date currentTime = new Date();
        HashMap<String, Date> prunedNames = new HashMap<>();

        map.forEach((name, date) -> {
            if (date.after(currentTime)) {
                prunedNames.put(name, date);
            }
        });

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                GSON.toJson(prunedNames, writer);
            }
        } catch (IOException e) {
            LOGGER.error("an exception was thrown trying to write config file at {} {}", CONFIG_PATH, e.getMessage());
        }
    }
}
