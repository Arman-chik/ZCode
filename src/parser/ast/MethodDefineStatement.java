package parser.ast;
import java.util.List;
public class MethodDefineStatement implements Statement {

    public String name;
    public List<String> argNames;
    public Statement body;


    public MethodDefineStatement(String name, List<String> argNames, Statement body) {
        this.name = name;
        this.argNames = argNames;
        this.body = body;
    }

    @Override public void execute() {} // регистрация происходт в ClassDefineStatement


    @Override public void accept(Visitor v) {
        v.visit(this);
    }
}