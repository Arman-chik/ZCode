package parser.ast;

import lib.ArrayValue;
import lib.ObjectValue;
import lib.Value;
import lib.Variables;
import java.util.List;

public class ThisArrayAssignmentStatement implements Statement {
    public String fieldName;
    public List<Expression> indices;
    public Expression expression;

    public ThisArrayAssignmentStatement(String fieldName, List<Expression> indices, Expression expression) {
        this.fieldName = fieldName;
        this.indices = indices;
        this.expression = expression;
    }

    @Override
    public void execute() {
        Value thisVal = Variables.get("this");
        if (!(thisVal instanceof ObjectValue)) {
            throw new RuntimeException("'this' доступен только внутри методов класса");
        }

        Value arrVal = ((ObjectValue) thisVal).getField(fieldName);
        if (!(arrVal instanceof ArrayValue)) {
            throw new RuntimeException("Поле '" + fieldName + "' не является массивом");
        }

        ArrayValue array = (ArrayValue) arrVal;
        // Спускаемся до нужного уровня вложенности (например, bricks[i])
        for (int i = 0; i < indices.size() - 1; i++) {
            int idx = (int) indices.get(i).eval().asNumber();
            Value next = array.get(idx);
            if (!(next instanceof ArrayValue)) {
                throw new RuntimeException("Ожидается массив на уровне вложенности " + i);
            }
            array = (ArrayValue) next;
        }

        // Присваиваем в последний индекс
        int lastIdx = (int) indices.get(indices.size() - 1).eval().asNumber();
        array.set(lastIdx, expression.eval());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "this." + fieldName + "[" + indices + "] = " + expression;
    }
}