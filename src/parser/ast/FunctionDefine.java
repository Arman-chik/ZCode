package parser.ast;

import lib.Functions;
import lib.CallFunction;

import java.util.List;

public class FunctionDefine implements Statement {

    private String name;
    private List<String> argNames;
    private Statement body;


    public FunctionDefine(String name, List<String> argNames, Statement body) {
        this.name = name;
        this.argNames = argNames;
        this.body = body;
    }

    @Override
    public void execute() {
        Functions.set(name, new CallFunction(argNames, body));
    }


    @Override
    public String toString() {
        return "def (" + argNames.toString() + ") " + body.toString();
    }
}
