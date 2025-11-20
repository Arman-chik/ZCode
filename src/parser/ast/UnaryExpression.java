package parser.ast;


import lib.NumberValue;
import lib.Value;


public class UnaryExpression implements Expression{

    private Expression expr1;
    private char operation;

    public UnaryExpression(char operation, Expression expr1) {
        this.operation = operation;
        this.expr1 = expr1;
    }

    @Override
    public Value eval() {
        switch (operation) {
            case '-': return new NumberValue(-expr1.eval().asNumber());
            case '+':
            default:
                return expr1.eval();
        }
    }

    @Override
    public String toString() {
        return String.format("%c %s", operation, expr1);
    }
}
