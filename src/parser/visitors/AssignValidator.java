package parser.visitors;

import lib.Variables;
import parser.ast.*;

public class AssignValidator extends AbstractVisitor {

    @Override
    public void visit(AssignmentStatement s) {
        if ("this".equals(s.variable)) {
            throw new RuntimeException("Нельзя присваивать значение ключевому слову 'this'");
        }
        s.expression.accept(this);
        if (Variables.isExists(s.variable)) {
            throw new RuntimeException("Нельзя присвоить значение константе");
        }
    }


}
