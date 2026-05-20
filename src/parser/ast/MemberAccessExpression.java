package parser.ast;
import lib.*;
import parser.ast.ReturnStatement;
import java.util.List;

public class MemberAccessExpression implements Expression {
    public Expression target;
    public String name;

    public List<Expression> callArgs; // null если это поле, не null если вызов метода

    public MemberAccessExpression(Expression target, String name, List<Expression> callArgs) {
        this.target = target;
        this.name = name;
        this.callArgs = callArgs;
    }

    @Override public Value eval() {
        Value objVal = target.eval();

        if (!(objVal instanceof ObjectValue)) {
            throw new RuntimeException("Ожидается объект перед '.'");
        }

        ObjectValue obj = (ObjectValue) objVal;

        if (callArgs == null) { // это поле
            return obj.getField(name);
        } else { // вызов метода
            ClassDefinition.MethodDef method = obj.getDefinition().methods.get(name);
            if (method == null) throw new RuntimeException("Метод " + name + " не найден");

            // Вычисляем аргументы в текщей обл. видимости, перед сменой this
            Value[] argValues = new Value[callArgs.size()];
            for (int i = 0; i < callArgs.size(); i++) {
                argValues[i] = callArgs.get(i).eval();
            }

            try {
                Variables.push();
                Variables.set("this", obj);

                for (int i = 0; i < method.params.size(); i++) {
                    Variables.set(method.params.get(i), argValues[i]);
                }
                method.body.execute();
                Variables.pop();

                return NumberValue.ZERO;
            } catch (ReturnStatement rt) {
                Variables.pop();
                return rt.getResult();
            }
        }
    }


    @Override public void accept(Visitor v) {
        v.visit(this);
    }
}