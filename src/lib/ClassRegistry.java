package lib;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassRegistry {
    private static final Map<String, ClassDefinition> classes = new ConcurrentHashMap<>();


    public static void register(ClassDefinition def) {
        classes.put(def.name, def);
    }

    public static ClassDefinition get(String name) {
        if (!classes.containsKey(name)) {
            throw new RuntimeException("Класс " + name + " не найден");
        }

        return classes.get(name);
    }
}