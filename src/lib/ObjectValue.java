package lib;
import java.util.HashMap;
import java.util.Map;

public class ObjectValue implements Value {
    private final String className;
    private final Map<String, Value> fields = new HashMap<>();
    private ClassDefinition definition; // ссылка на определение класса

    public ObjectValue(String className, ClassDefinition definition) {
        this.className = className;
        this.definition = definition;
    }


    public void setField(String name, Value val) {
        fields.put(name, val);
    }

    public Value getField(String name) {
        if (!fields.containsKey(name)) {
            throw new RuntimeException("Поле '" + name + "' не найдено в " + className);
        }

        return fields.get(name);
    }

    public ClassDefinition getDefinition() {
        return definition;
    }

    @Override public double asNumber() {
        throw new RuntimeException("Объект не может быть приведён к числу");
    }


    @Override public String asString() {
        return "Object[" + className + "]";
    }
}