package parser.ast;

import lib.Value;
import lib.NumberValue;
import lib.StringValue;
import lib.ObjectValue;
import lib.ArrayValue;

public class ConditionalExpression implements Expression{
    public static enum Operator {
        EQUALS("=="),
        NOT_EQUALS("!="),
        LT("<"),
        LTEQ("<="),
        GT(">"),
        GTEQ(">="),
        AND("&&"),
        OR("||");

        private String name;
        Operator(String name) { this.name = name; }
        public String getName() { return name; }
    }
    public Expression expr1, expr2;
    public Operator operation;

    public ConditionalExpression(Operator operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public Value eval() {
        Value value1 = expr1.eval();
        switch (operation) {
            case AND: return NumberValue.fromBoolean((value1.asNumber() != 0) && (expr2.eval().asNumber() != 0));
            case OR:  return NumberValue.fromBoolean((value1.asNumber() != 0) || (expr2.eval().asNumber() != 0));
        }
        Value value2 = expr2.eval();
        boolean result;

        switch (operation) {
            case EQUALS:
                if (value1 instanceof NumberValue && value2 instanceof NumberValue) {
                    result = value1.asNumber() == value2.asNumber();
                } else if (value1 instanceof StringValue && value2 instanceof StringValue) {
                    result = value1.asString().equals(value2.asString());
                } else if (value1 instanceof ObjectValue && value2 instanceof ObjectValue) {
                    result = value1 == value2; // Сравнение по ссылке
                } else if (value1 instanceof ArrayValue && value2 instanceof ArrayValue) {
                    result = value1 == value2;
                } else {
                    result = false; // Разные типы не равны
                }
                break;
            case NOT_EQUALS:
                if (value1 instanceof NumberValue && value2 instanceof NumberValue) {
                    result = value1.asNumber() != value2.asNumber();
                } else if (value1 instanceof StringValue && value2 instanceof StringValue) {
                    result = !value1.asString().equals(value2.asString());
                } else if (value1 instanceof ObjectValue && value2 instanceof ObjectValue) {
                    result = value1 != value2;
                } else if (value1 instanceof ArrayValue && value2 instanceof ArrayValue) {
                    result = value1 != value2;
                } else {
                    result = true; // Разные типы всегда не равны
                }
                break;
            case LT: result = value1.asNumber() < value2.asNumber(); break;
            case LTEQ: result = value1.asNumber() <= value2.asNumber(); break;
            case GT: result = value1.asNumber() > value2.asNumber(); break;
            case GTEQ: result = value1.asNumber() >= value2.asNumber(); break;
            default:
                throw new RuntimeException("Operation " + operation + " is not supported");
        }
        return NumberValue.fromBoolean(result);
    }


    @Override public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override public String toString() {
        return String.format("%s %s %s", expr1, operation.getName(), expr2);
    }
}