package parser.ast;

public class ForStatement implements Statement{

    private Statement initialization;
    private Expression termination;  // условие завершение цикла
    private Statement increment;
    private Statement statement;


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
    public String toString() {
        return "for " + initialization + "; " + termination + "; " + increment + " " + statement;
    }
}
