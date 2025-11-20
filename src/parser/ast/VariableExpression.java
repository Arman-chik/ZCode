package parser.ast;

import lib.Value;
import lib.Variables;

public class VariableExpression implements Expression{

    private String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        if (!Variables.isExists(name)) {
            throw new RuntimeException("Константы не существует");
        }
        return Variables.get(name);
    }


    @Override
    public String toString() {
        return String.format("%s", name, Variables.get(name));
    }
}
