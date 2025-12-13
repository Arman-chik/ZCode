package parser.ast;

public class FunctionStatement implements Statement{

    public FunctionalExpression function;


    public FunctionStatement(FunctionalExpression function) {
        this.function = function;
    }

    @Override
    public void execute() {
        function.eval();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public String toString() {
        return function.toString();
    }
}
