package lib;
import java.util.List;
import java.util.Map;
import parser.ast.Statement;

public class ClassDefinition {
    public final String name;
    public final List<String> fields;
    public final Map<String, MethodDef> methods = new java.util.HashMap<>();

    public ClassDefinition(String name, List<String> fields) {
        this.name = name;
        this.fields = fields;
    }

    public static class MethodDef {
        public final List<String> params;
        public final Statement body;
        public final boolean isConstructor;

        public MethodDef(List<String> params, Statement body, boolean isConstructor) {
            this.params = params;
            this.body = body;
            this.isConstructor = isConstructor;
        }
    }
}