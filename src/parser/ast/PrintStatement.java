package parser.ast;

public class PrintStatement implements Statement {

    public Expression expression;


    public PrintStatement(Expression expression) {
        this.expression = expression;
    }


    @Override
    public void execute() {
        System.out.print(expression.eval());
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "print " + expression;
    }
}
