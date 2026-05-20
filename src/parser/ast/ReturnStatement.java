package parser.ast;

import lib.NumberValue;
import lib.Value;

public class ReturnStatement extends RuntimeException implements Statement{

    public Expression expression;
    public Value result;


    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }


    public Value getResult() {
        return result;
    }

    @Override
    public void execute() {
        if (expression != null) {
            result = expression.eval();
        } else {
            result = NumberValue.ZERO;  // return без значения = возвращаем 0
        }
        throw this;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "return " + (expression != null ? expression : "");
    }
}
