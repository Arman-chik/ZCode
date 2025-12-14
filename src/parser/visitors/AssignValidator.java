package parser.visitors;

import lib.Variables;
import parser.ast.*;

public class AssignValidator extends AbstractVisitor {

    @Override
    public void visit(AssignmentStatement s) {
        s.expression.accept(this);
        if (Variables.isExists(s.variable)) {
            throw new RuntimeException("Нельзя присвоить значение константе");
        }
    }


}
