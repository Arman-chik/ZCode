package parser.ast;
//создан е
import lib.*;

import java.util.List;

public class ArrayExpression implements Expression{


    public List<Expression> elements;


    public ArrayExpression(List<Expression> elements) {
        this.elements = elements;
    }

    @Override
    public Value eval() {
        int size = elements.size();
        ArrayValue array = new ArrayValue(size);

        for (int i = 0; i < size; i++) {
            array.set(i, elements.get(i).eval());
        }
        return array;
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public String toString() {
        return elements.toString();
    }
}
