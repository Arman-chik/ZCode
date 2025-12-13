package parser.ast;

public class ForStatement implements Statement{

    public Statement initialization;
    public Expression termination;  // условие завершение цикла
    public Statement increment;
    public Statement statement;


    public ForStatement(Statement initialization, Expression termination, Statement increment, Statement statement) {
        this.initialization = initialization;
        this.termination = termination;
        this.increment = increment;
        this.statement = statement;
    }



    @Override
    public void execute() {
        for (initialization.execute(); termination.eval().asNumber() != 0; increment.execute()) {
            try {
                statement.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                // continue;
            }
        }
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "for " + initialization + "; " + termination + "; " + increment + " " + statement;
    }
}
