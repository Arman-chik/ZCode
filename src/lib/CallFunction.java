package lib;

import parser.ast.ReturnStatement;
import parser.ast.Statement;

import java.util.List;

public class CallFunction implements Function {

    private List<String> argNames;
    private Statement body;


    public CallFunction(List<String> argNames, Statement body) {
        this.argNames = argNames;
        this.body = body;
    }

    public int getArgsCount() {
        return argNames.size();
    }


    public String getArgsName(int index) {
        if (index < 0 || index >= getArgsCount()) {
            return "";
        }

        return argNames.get(index);
    }

    @Override
    public Value execute(Value... args) {
        try {
            Variables.push();
            for (int i = 0; i < args.length; i++) {
                Variables.set(argNames.get(i), args[i]);
            }
            body.execute();
            Variables.pop();
            return NumberValue.ZERO;
        } catch (ReturnStatement rt) {
            Variables.pop();
            return rt.getResult();
        }
    }
}
