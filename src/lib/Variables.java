package lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class Variables {


    private static Map<String, Value> variables;
    private static Stack<Map<String, Value>> stack;



    static {
        stack = new Stack<>();
//        variables = new HashMap<>();
//        variables.put("PI", new NumberValue(Math.PI));
//        variables.put("pi", new NumberValue(Math.PI));
//        variables.put("ПИ", new NumberValue(Math.PI));
//        variables.put("пи", new NumberValue(Math.PI));
//        variables.put("E", new NumberValue(Math.E));
//        variables.put("e", new NumberValue(Math.E));
//        variables.put("GOLDEN_RATIO", new NumberValue(1.628));


        variables = new ConcurrentHashMap<>();
        variables.put("true", NumberValue.ONE);
        variables.put("false", NumberValue.ZERO);
    }

    public static void push() {
        stack.push(new ConcurrentHashMap<>(variables));
    }


    public static void pop() {
      variables = stack.pop();
    }

    public static boolean isExists(String key) {
        return variables.containsKey(key);
    }

    public static Value get(String key) {
        if (!isExists(key)) {
            return NumberValue.ZERO;
        }
        return variables.get(key);
    }

    public static void set(String key, Value value) {
        variables.put(key, value);
    }
}
