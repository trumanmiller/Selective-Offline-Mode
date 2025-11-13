package jmeow.selectiveofflinemode;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NameExpiry {
    public static Map<String, Date> names = new HashMap<>();

    public static void addName(String name) {
        names.put(name, Date.from(Instant.now().plus(Duration.ofSeconds(60))));
    }

    public static void addName(String name, Long duration) {
        names.put(name, Date.from(Instant.now().plus(Duration.ofSeconds(duration))));
    }
}
