package parser.visitors;

import parser.ast.*;

public class VariablePrinter extends AbstractVisitor {

    @Override
    public void visit(AssignmentStatement s) {
        s.expression.accept(this);
        System.out.println(s.variable);
    }


    @Override
    public void visit(VariableExpression s) {
        System.out.println(s.name);
    }

}
