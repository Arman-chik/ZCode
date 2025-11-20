package parser.ast;

import lib.*;

import java.util.ArrayList;
import java.util.List;

public class FunctionalExpression implements Expression{

    private String name;
    private List<Expression> arguments;

    public FunctionalExpression(String name) {
        this.name = name;
        arguments = new ArrayList<>();
    }

    public FunctionalExpression(String name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public void addArgument(Expression arg) {
        arguments.add(arg);
    }

    @Override
    public Value eval() {
        final int size = arguments.size();
        final Value[] values = new Value[size];
        for (int i = 0; i < size; i++) {
            values[i] = arguments.get(i).eval();
        }

        Function function = Functions.get(name);
        if (function instanceof CallFunction) {
            CallFunction userFunction = (CallFunction) function;

            if (size != userFunction.getArgsCount()) {
                throw new RuntimeException("Количество аргументов не совпадает");
            }

            Variables.push();
            for (int i = 0; i < size; i++) {
                Variables.set(userFunction.getArgsName(i), values[i]);
            }
            Value result = userFunction.execute(values);
            Variables.pop();
            return result;
        }

        return function.execute(values);
    }

    @Override
    public String toString() {
        return name + "(" + arguments.toString() + ")";
    }
}
