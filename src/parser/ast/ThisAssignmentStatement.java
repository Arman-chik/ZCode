package parser.ast;
import lib.Value;
import lib.Variables;
import lib.ObjectValue;

public class ThisAssignmentStatement implements Statement {
    public String fieldName;
    public Expression expression;

    public ThisAssignmentStatement(String fieldName, Expression expression) {
        this.fieldName = fieldName;
        this.expression = expression;
    }

    @Override
    public void execute() {
        Value thisValue = Variables.get("this");
        if (!(thisValue instanceof ObjectValue)) {
            throw new RuntimeException("'this' доступен только внутри методов класса");
        }
        ObjectValue obj = (ObjectValue) thisValue;
        obj.setField(fieldName, expression.eval());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "this." + fieldName + " = " + expression;
    }
}