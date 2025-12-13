package parser.ast;
//доступ
import lib.ArrayValue;
import lib.StringValue;
import lib.Value;
import lib.Variables;

import java.util.List;

public final class ArrayAccessExpression implements Expression {

    public final String variable;
    public final List<Expression> indices;

    public ArrayAccessExpression(String variable, List<Expression> indices) {
        this.variable = variable;
        this.indices = indices;
    }

    @Override
    public Value eval() {
        Value value = Variables.get(variable);
        if (value instanceof StringValue && indices.size() == 1) {
            return ((StringValue) value).get(lastIndex());
        }



        return getArray().get(lastIndex());
    }

    public ArrayValue getArray() {
        ArrayValue array = consumeArray(Variables.get(variable));

        int last = indices.size() - 1;
        for (int i = 0; i < last; i++) {
            array = consumeArray( array.get(index(i)) );
        }
        return array;
    } //arr[1]

    public int lastIndex() {
        return index(indices.size() - 1);
    }

    private int index(int index) {
        return (int) indices.get(index).eval().asNumber();
    }

    private ArrayValue consumeArray(Value value) {
        if (value instanceof ArrayValue) {
            return (ArrayValue) value;
        } else {
            throw new RuntimeException("Ожидался массив");
        }
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public String toString() {
        return variable + indices;
    }


}