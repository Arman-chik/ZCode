package parser.ast;
import lib.ClassDefinition;
import lib.ClassRegistry;
import java.util.List;

public class ClassDefineStatement implements Statement {
    public String name;
    public List<String> fields;
    public List<MethodDefineStatement> methods;

    public ClassDefineStatement(String name, List<String> fields, List<MethodDefineStatement> methods) {
        this.name = name;
        this.fields = fields;
        this.methods = methods;
    }

    @Override public void execute() {
        ClassDefinition def = new ClassDefinition(name, fields);

        for (MethodDefineStatement m : methods) {
            def.methods.put(m.name, new ClassDefinition.MethodDef(m.argNames, m.body, m.name.equals(name)));
        }

        ClassRegistry.register(def);
    }


    @Override public void accept(Visitor v) {
        v.visit(this);
    }
}