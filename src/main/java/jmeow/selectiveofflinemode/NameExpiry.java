package jmeow.selectiveofflinemode;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import static jmeow.selectiveofflinemode.config.SelectiveOfflineModeConfig.saveConfig;

public class NameExpiry {
    private static HashMap<String, Date> names = new HashMap<>();

    public static void addName(String name, Long duration) {
        names.put(name, Date.from(Instant.now().plus(Duration.ofSeconds(duration))));
        saveConfig(names);
    }

    public static Boolean isAllowed(String name) {
        return names.containsKey(name) &&
                names.get(name).after(new Date());
    }

    public static void initNames(HashMap<String, Date> newNames) {
        names = newNames;
    }
}
