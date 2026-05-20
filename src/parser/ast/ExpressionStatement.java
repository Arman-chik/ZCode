package parser.ast;

public class ExpressionStatement implements Statement {
    public Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }


    @Override
    public void execute() {
        expression.eval();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public String toString() {
        return expression.toString();
    }
}