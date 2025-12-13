package parser.ast;

import lib.Functions;
import lib.CallFunction;

import java.util.List;

public class FunctionDefineStatement implements Statement {

    public String name;
    public List<String> argNames;
    public Statement body;


    public FunctionDefineStatement(String name, List<String> argNames, Statement body) {
        this.name = name;
        this.argNames = argNames;
        this.body = body;
    }

    @Override
    public void execute() {
        Functions.set(name, new CallFunction(argNames, body));
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "def (" + argNames.toString() + ") " + body.toString();
    }
}
