package lib;

import java.util.HashMap;
import java.util.Map;

public class ClassValue implements Value {

    private final String name;
    private final Map<String, Function> methods;

    public ClassValue(String name) {
        this.name = name;
        this.methods = new HashMap<>();
    }


    @Override
    public double asNumber() {
        return 0;
    }

    @Override
    public String asString() {
        return "";
    }
}
