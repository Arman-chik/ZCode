package parser.ast;

import lib.ObjectValue;
import lib.Value;
import lib.Variables;

public class MemberAssignmentStatement implements Statement {
    public Expression target;      // объект
    public String fieldName;       // имя поля
    public Expression expression;  // значение для присваивания

    public MemberAssignmentStatement(Expression target, String fieldName, Expression expression) {
        this.target = target;
        this.fieldName = fieldName;
        this.expression = expression;
    }

    @Override
    public void execute() {
        Value objVal = target.eval();

        if (!(objVal instanceof ObjectValue)) {
            throw new RuntimeException("Ожидается объект перед '.' в присваивании: " + fieldName);
        }

        ((ObjectValue) objVal).setField(fieldName, expression.eval());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public String toString() {
        return target + "." + fieldName + " = " + expression;
    }
}