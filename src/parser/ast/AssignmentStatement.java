package parser.ast;

import lib.Value;
import lib.Variables;

public class AssignmentStatement implements Statement{

    private String variable;
    private Expression expression;



    public AssignmentStatement(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }


    @Override
    public void execute() {
        Value result = expression.eval();
        Variables.set(variable, result);
    }


    @Override
    public String toString() {
        return String.format("%s = %s", variable, expression);
    }
}
