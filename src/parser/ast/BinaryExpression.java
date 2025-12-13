package parser.ast;

import lib.*;

public class BinaryExpression implements Expression{

    public Expression expr1, expr2;
    public char operation;


    public BinaryExpression(char operation,Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public Value eval() {
        Value value1 = expr1.eval();
        Value value2 = expr2.eval();
        if ((value1 instanceof StringValue) || (value1 instanceof ArrayValue)) {
            String string1 = value1.asString();
            String string2 = value2.asString();

            switch (operation) {
                case '*':{
                    int iterations = (int) value2.asNumber();
                    StringBuilder buffer = new StringBuilder();
                    for (int i = 0; i < iterations; i++) {
                        buffer.append(string1);
                    }
                    return new StringValue(buffer.toString());
                }
                case '+':
                default:
                    return new StringValue(string1 + string2);
            }
        }

        double number1 = expr1.eval().asNumber();
        double number2 = expr2.eval().asNumber();

        switch (operation) {
            case '-': return new NumberValue(number1 - number2);
            case '*': return new NumberValue(number1 * number2);
            case '/': return new NumberValue(number1 / number2);
            case '%': return new NumberValue(number1 % number2);
            case '+':
            default:
                return new NumberValue(number1 + number2);
        }
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("(%s %c %s)", expr1, operation, expr2);
    }
}
