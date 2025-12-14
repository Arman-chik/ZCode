package parser.visitors;

import lib.CallFunction;
import lib.Functions;
import parser.ast.*;

public class FunctionsAdder extends AbstractVisitor {

    @Override
    public void visit(FunctionDefineStatement s) {
        s.body.accept(this);
        s.execute();
    }

}
