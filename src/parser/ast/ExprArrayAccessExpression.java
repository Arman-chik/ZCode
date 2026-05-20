package parser.ast;
import lib.ArrayValue;
import lib.Value;
import java.util.List;

public class ExprArrayAccessExpression implements Expression {
    public Expression baseExpr;
    public List<Expression> indices;


    public ExprArrayAccessExpression(Expression baseExpr, List<Expression> indices) {
        this.baseExpr = baseExpr;
        this.indices = indices;
    }


    @Override public Value eval() {
        Value val = baseExpr.eval();
        if (!(val instanceof ArrayValue)) {
            throw new RuntimeException("Ожидается массив перед индексацией");
        }

        ArrayValue array = (ArrayValue) val;
        for (int i = 0; i < indices.size() - 1; i++) {
            array = (ArrayValue) array.get((int) indices.get(i).eval().asNumber());
        }

        return array.get((int) indices.get(indices.size() - 1).eval().asNumber());
    }


    @Override public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}