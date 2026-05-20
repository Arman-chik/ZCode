package parser.ast;
import lib.*;
import parser.ast.ReturnStatement;
import java.util.List;

public class ObjectCreationExpression implements Expression {
    public String className;
    public List<Expression> args;


    public ObjectCreationExpression(String className, List<Expression> args) {
        this.className = className; this.args = args;
    }


    @Override public Value eval() {
        ClassDefinition def = ClassRegistry.get(className);
        ObjectValue obj = new ObjectValue(className, def);

        // Инициализация полей нулями
        for (String f : def.fields) {
            obj.setField(f, NumberValue.ZERO);
        }


        // Поиск и выполнение конструктора
        ClassDefinition.MethodDef ctor = def.methods.get(className);

        if (ctor != null) {
            // Аналогично методам, аргументы конструктора вычисляем ДО смены this
            Value[] argValues = new Value[args.size()];
            for (int i = 0; i < args.size(); i++) {
                argValues[i] = args.get(i).eval();
            }

            try {
                Variables.push();
                Variables.set("this", obj);

                for (int i = 0; i < ctor.params.size(); i++) {
                    Variables.set(ctor.params.get(i), argValues[i]);
                }

                ctor.body.execute();
            } catch (ReturnStatement rt) {
                /* конструктор не возвращает значение */
            }
            finally {
                Variables.pop();
            }
        }
        return obj;
    }


    @Override public void accept(Visitor v) {
        v.visit(this);
    }
}