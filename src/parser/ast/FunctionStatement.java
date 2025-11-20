package parser.ast;

public class FunctionStatement implements Statement{

    private FunctionalExpression function;


    public FunctionStatement(FunctionalExpression function) {
        this.function = function;
    }

    @Override
    public void execute() {
        function.eval();
    }


    @Override
    public String toString() {
        return function.toString();
    }
}
