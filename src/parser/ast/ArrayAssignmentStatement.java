package parser.ast;
//присваивание

import lib.Value;

public final class ArrayAssignmentStatement implements Statement {

    private ArrayAccessExpression array;
    private Expression expression;

    public ArrayAssignmentStatement(ArrayAccessExpression array, Expression expression) {
        this.array = array;
        this.expression = expression;
    }

    @Override
    public void execute() {
        Value value = array.getArray();
        if (value instanceof lib.StringValue) {
            throw new RuntimeException("Строки неизменяемы, нельзя изменить символ по индексу");
        }
        ((lib.ArrayValue) value).set(array.lastIndex(), expression.eval());


        //array.getArray().set(array.lastIndex(), expression.eval());
    }

    @Override
    public String toString() {
        return String.format("%s = %s", array, expression);
    }
}
