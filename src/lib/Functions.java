package lib;


import java.util.HashMap;
import java.util.Map;

public class Functions {


    private static Map<String, Function> functions;


    static {

        functions = new HashMap<>();

    }



    public static boolean isExists(String key) {
        return functions.containsKey(key);
    }

    public static Function get(String key) {
        if (!isExists(key)) {
            throw new RuntimeException("Неизвестная функция " + key);
        }
        return functions.get(key);
    }

    public static void set(String key, Function function) {
        functions.put(key, function);
    }
}
