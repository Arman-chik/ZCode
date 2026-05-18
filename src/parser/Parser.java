package parser;

import parser.ast.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static Token EOF = new Token(TokenType.EOF, "", -1, -1);
    private List<Token> tokens;
    private int pos;
    private int size;


    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
    }

    public Statement parse() {
        BlockStatement result = new BlockStatement();

        while (!match(TokenType.EOF)) {
            result.add(statement());
        }

        return result;
    }



    private Statement block() {
        BlockStatement block = new BlockStatement();
        consume(TokenType.LBRACE);
        while (!match(TokenType.RBRACE)) {
            block.add(statement());
        }
        return block;
    }


    private Statement statementOrBlock() {
        if (get(0).getType() == TokenType.LBRACE) {
            return block();
        }

        return statement();
    }


    private Statement statement() {
        if (match(TokenType.CLASS)) return classDeclaration();
        if (match(TokenType.PRINT)) return new PrintStatement(expression());
        if (match(TokenType.IF)) return ifElse();
        if (match(TokenType.WHILE)) return whileStatement();
        if (match(TokenType.DO)) return doWhileStatement();
        if (match(TokenType.BREAK)) return new BreakStatement();
        if (match(TokenType.CONTINUE)) return new ContinueStatement();
        if (match(TokenType.RETURN)) {
            // return может быть без выражения (просто выход из функции)
            if (lookMatch(0, TokenType.RBRACE) || lookMatch(0, TokenType.EOF) ||
                    lookMatch(0, TokenType.ELSE) || lookMatch(0, TokenType.WHILE) ||
                    lookMatch(0, TokenType.FOR) || lookMatch(0, TokenType.IF)) {
                return new ReturnStatement(null);
            }
            return new ReturnStatement(expression());
        }
        if (match(TokenType.USE)) return new UseStatement(expression());
        if (match(TokenType.FOR)) return forStatement();
        if (match(TokenType.DEF)) return functionDefine();


        // Проверки на присваивания
        // 1. var = expr
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.EQ))
            return assignmentStatement();

        // 2. obj.field = expr (например, ball.dy = -ball.dy)
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.DOT) &&
                lookMatch(2, TokenType.WORD) && lookMatch(3, TokenType.EQ))
            return assignmentStatement();

        // 3. this.field = expr (например, this.x = x) ← ВАШ СЛУЧАЙ [9 18]
        if (lookMatch(0, TokenType.THIS) && lookMatch(1, TokenType.DOT) &&
                lookMatch(2, TokenType.WORD) && lookMatch(3, TokenType.EQ))
            return assignmentStatement();

        // 4. arr[idx] = expr
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LBRACKET))
            return assignmentStatement();

        // 5. this.arr[i][j] = expr
        if (lookMatch(0, TokenType.THIS) && lookMatch(1, TokenType.DOT) &&
                lookMatch(2, TokenType.WORD) && lookMatch(3, TokenType.LBRACKET)) {

            int scan = 3;  // начинаем с первого '['
            int depth = 0;
            boolean isAssignment = false;

            while (pos + scan < size) {
                TokenType t = get(scan).getType();
                if (t == TokenType.LBRACKET) depth++;
                else if (t == TokenType.RBRACKET) depth--;
                else if (t == TokenType.EQ && depth == 0) {
                    isAssignment = true;
                    break;  // Если нашли '=' вне скобок это присаивание
                }
                else if (t == TokenType.DOT || t == TokenType.LPAREN || t == TokenType.EOF) {
                    break;  // Это вызов метода
                }
                scan++;
            }
            if (isAssignment) return assignmentStatement();
        }
        // 6. this.obj.field = expr (например, this.ball.dy = -this.ball.dy)
        if (lookMatch(0, TokenType.THIS) && lookMatch(1, TokenType.DOT) &&
                lookMatch(2, TokenType.WORD) && lookMatch(3, TokenType.DOT) &&
                lookMatch(4, TokenType.WORD) && lookMatch(5, TokenType.EQ))
            return assignmentStatement();

        // Если ни одно присваивание не распознано то возвращаем обычное выражение
        return new ExpressionStatement(expression());
    }

    private Statement assignmentStatement() {
        // 1. var = expr
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.EQ)) {
            String variable = consume(TokenType.WORD).getText();
            consume(TokenType.EQ);
            return new AssignmentStatement(variable, expression());
        }
        // 2. this.field = expr
        if (lookMatch(0, TokenType.THIS) && lookMatch(1, TokenType.DOT) && lookMatch(2, TokenType.WORD) && lookMatch(3, TokenType.EQ)) {
            consume(TokenType.THIS); consume(TokenType.DOT);
            String fieldName = consume(TokenType.WORD).getText();
            consume(TokenType.EQ);
            return new ThisAssignmentStatement(fieldName, expression());
        }
        // 3. arr[idx] = expr
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LBRACKET)) {
            ArrayAccessExpression array = (ArrayAccessExpression) element();
            consume(TokenType.EQ);
            return new ArrayAssignmentStatement(array, expression());
        }
        // 4. obj.field = expr
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.DOT) && lookMatch(2, TokenType.WORD) && lookMatch(3, TokenType.EQ)) {
            Expression target = new VariableExpression(consume(TokenType.WORD).getText());
            consume(TokenType.DOT);
            String fieldName = consume(TokenType.WORD).getText();
            consume(TokenType.EQ);
            return new MemberAssignmentStatement(target, fieldName, expression());
        }
        // 5. this.arr[idx] = expr
        if (lookMatch(0, TokenType.THIS) && lookMatch(1, TokenType.DOT) && lookMatch(2, TokenType.WORD) && lookMatch(3, TokenType.LBRACKET)) {
            consume(TokenType.THIS); consume(TokenType.DOT);
            String fieldName = consume(TokenType.WORD).getText();
            List<Expression> indices = new ArrayList<>();
            while (lookMatch(0, TokenType.LBRACKET)) {
                consume(TokenType.LBRACKET);
                indices.add(expression());
                consume(TokenType.RBRACKET);
            }
            consume(TokenType.EQ);
            return new ThisArrayAssignmentStatement(fieldName, indices, expression());
        }

        // 6. this.obj.field = expr
        if (lookMatch(0, TokenType.THIS) && lookMatch(1, TokenType.DOT) &&
                lookMatch(2, TokenType.WORD) && lookMatch(3, TokenType.DOT) &&
                lookMatch(4, TokenType.WORD) && lookMatch(5, TokenType.EQ)) {

            consume(TokenType.THIS); consume(TokenType.DOT);
            String objField = consume(TokenType.WORD).getText();  // например, "ball"
            consume(TokenType.DOT);
            String fieldName = consume(TokenType.WORD).getText();  // например, "dy"
            consume(TokenType.EQ);

            Expression target = new MemberAccessExpression(
                    new VariableExpression("this"),
                    objField,
                    null  // null = это поле, а не вызов метода
            );

            return new MemberAssignmentStatement(target, fieldName, expression());
        }

        throw new ParseException("Неизвестный оператор: " + get(0));
    }

    private Statement classDeclaration() {
        String name = consume(TokenType.WORD).getText();
        List<String> fields = new java.util.ArrayList<>();
        List<MethodDefineStatement> methods = new java.util.ArrayList<>();

        consume(TokenType.LBRACE);
        while (!lookMatch(0, TokenType.RBRACE)) {
            if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LPAREN)) {
                methods.add(methodDeclaration());
            } else {
                fields.add(consume(TokenType.WORD).getText());
            }
        }
        consume(TokenType.RBRACE); // закрывающая скобка
        return new ClassDefineStatement(name, fields, methods);
    }


    private MethodDefineStatement methodDeclaration() {
        String name = consume(TokenType.WORD).getText();
        consume(TokenType.LPAREN);
        List<String> params = new java.util.ArrayList<>();

        if (!match(TokenType.RPAREN)) { // если не пустой список параметров
            do {
                params.add(consume(TokenType.WORD).getText());
            } while (match(TokenType.COM)); // запятая обязательна между параметрами
            consume(TokenType.RPAREN); // закрывающая скобка
        }

        Statement body = statementOrBlock();
        return new MethodDefineStatement(name, params, body);
    }


    private Statement ifElse() {
        Expression condition = expression();
        Statement ifStatement = statementOrBlock();
        Statement elseStatement;
        if (match(TokenType.ELSE)) {
            elseStatement = statementOrBlock();
        } else {
            elseStatement= null;
        }
        return new IfStatement(condition, ifStatement, elseStatement);
    }




    private Statement whileStatement() {
        Expression condition = expression();
        Statement statement = statementOrBlock();

        return new WhileStatement(condition, statement);
    }



    private Statement doWhileStatement() {
        Statement statement = statementOrBlock();
        consume(TokenType.WHILE);
        Expression condition = expression();

        return new DoWhileStatement(condition, statement);
    }


    private Statement forStatement() {
        match(TokenType.LPAREN); // необязательные скобки
        Statement initialization = assignmentStatement();
        if (!match(TokenType.COM) && !match(TokenType.COMMA)) {
            throw new ParseException("Ожидался разделитель ',' или ';' после инициализации в for");
        }

        Expression termination = expression();

        if (!match(TokenType.COM) && !match(TokenType.COMMA)) {
            throw new ParseException("Ожидался разделитель ',' или ';' после условия в for");
        }

        Statement increment = assignmentStatement();
        match(TokenType.RPAREN); // необязательные скобки
        Statement statement = statementOrBlock();

        return new ForStatement(initialization, termination, increment, statement);
    }


    private FunctionDefineStatement functionDefine() {
        String name = consume(TokenType.WORD).getText();
        consume(TokenType.LPAREN);
        List<String> argNames = new ArrayList<>();

        while (!match(TokenType.RPAREN)) {
            argNames.add(consume(TokenType.WORD).getText());
            match(TokenType.COM);
        }


        Statement body = statementOrBlock();

        return new FunctionDefineStatement(name, argNames, body);
    }


    private FunctionalExpression function() {
        String name = consume(TokenType.WORD).getText();
        consume(TokenType.LPAREN);
        FunctionalExpression function = new FunctionalExpression(name);

        while (!match(TokenType.RPAREN)) {
            function.addArgument(expression());
            match(TokenType.COM);
        }

        return function;
    }


    private Expression array() {
        consume(TokenType.LBRACKET);
        final List<Expression> elements = new ArrayList<>();
        while (!match(TokenType.RBRACKET)) {
            elements.add(expression());
            match(TokenType.COM);
        }
        return new ArrayExpression(elements);
    }


    private Expression element() {
        String variable = consume(TokenType.WORD).getText();
        List<Expression> indices = new ArrayList<>();

        do {
            consume(TokenType.LBRACKET);
            indices.add(expression());
            consume(TokenType.RBRACKET);
        } while(lookMatch(0, TokenType.LBRACKET));

        return new ArrayAccessExpression(variable, indices);
    }





    private Expression expression() {
        return ternary();
    }


    private Expression ternary() {
        Expression result = logicalOr();

        if (match(TokenType.QUESTION)) {
            final Expression trueExpr = expression();
            consume(TokenType.COLON);
            final Expression falseExpr = expression();
            return new TernaryExpression(result, trueExpr, falseExpr);
        }

        return result;
    }


    private Expression logicalOr() {
        Expression result = logicalAnd();

        while (true) {
            if (match(TokenType.BARBAR)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd());
                continue;
            }
            break;
        }

        if (match(TokenType.BARBAR)) {
            return new ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd());
        }

        return result;
    }


    private Expression logicalAnd() {
        Expression result = bitwiseOr();

        while (true) {
            if (match(TokenType.AMPAMP)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.AND, result, bitwiseOr());
                continue;
            }
            break;
        }


        return result;
    }

    private Expression bitwiseOr() {
        Expression expression = bitwiseXor();

        while (true) {
            if (match(TokenType.BAR)) {
                expression = new BinaryExpression(BinaryExpression.Operator.OR, expression, bitwiseXor());
                continue;
            }
            break;
        }

        return expression;
    }

    private Expression bitwiseXor() {
        Expression expression = bitwiseAnd();

        while (true) {
            if (match(TokenType.CARET)) {
                expression = new BinaryExpression(BinaryExpression.Operator.XOR, expression, bitwiseAnd());
                continue;
            }
            break;
        }

        return expression;
    }

    private Expression bitwiseAnd() {
        Expression expression = equality();

        while (true) {
            if (match(TokenType.AMP)) {
                expression = new BinaryExpression(BinaryExpression.Operator.AND, expression, equality());
                continue;
            }
            break;
        }

        return expression;
    }


    private Expression equality() {
        Expression result = conditional();

        if (match(TokenType.EQEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.EQUALS, result, conditional());

        }
        if (match(TokenType.EXCLEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, result, conditional());
        }

        return result;
    }


    private Expression conditional() {
        Expression result = shift();

        while (true) {
            if (match(TokenType.LT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LT, result, shift());
                continue;
            }
            if (match(TokenType.LTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LTEQ, result, shift());
                continue;
            }
            if (match(TokenType.GT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GT, result, shift());
                continue;
            }
            if (match(TokenType.GTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GTEQ, result, shift());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression shift() {
        Expression expression = additive();

        while (true) {
            if (match(TokenType.LTLT)) {
                expression = new BinaryExpression(BinaryExpression.Operator.LSHIFT, expression, additive());
                continue;
            }
            if (match(TokenType.GTGT)) {
                expression = new BinaryExpression(BinaryExpression.Operator.RSHIFT, expression, additive());
                continue;
            }
            if (match(TokenType.GTGTGT)) {
                expression = new BinaryExpression(BinaryExpression.Operator.URSHIFT, expression, additive());
                continue;
            }
            break;
        }

        return expression;
    }


    private Expression additive() {
        Expression result = multiplicative();

        while (true) {
            if (match(TokenType.PLUS)) {
                result = new BinaryExpression(BinaryExpression.Operator.ADD, result, multiplicative());
                continue;
            }
            if (match(TokenType.MINUS)) {
                result = new BinaryExpression(BinaryExpression.Operator.SUBTRACT, result, multiplicative());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression multiplicative() {
        Expression result = unary();

        while (true) {
            if (match(TokenType.STAR)) {
                result = new BinaryExpression(BinaryExpression.Operator.MULTIPLY, result, unary());
                continue;
            }
            if (match(TokenType.SLASH)) {
                result = new BinaryExpression(BinaryExpression.Operator.DIVIDE, result, unary());
                continue;
            }
            if (match(TokenType.PERCENT)) {
                result = new BinaryExpression(BinaryExpression.Operator.REMAINDER, result, unary());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression unary() {
        if (match(TokenType.MINUS)) {
            return new UnaryExpression(UnaryExpression.Operator.NEGATE, postfix());
        }
        if (match(TokenType.EXCL)) {
            return new UnaryExpression(UnaryExpression.Operator.NOT, postfix());
        }
        if (match(TokenType.TILDE)) {
            return new UnaryExpression(UnaryExpression.Operator.COMPLEMENT, postfix());
        }
        if (match(TokenType.PLUS)) {
            //return new UnaryExpression('+', primary());
            return postfix();
        }
        return postfix();
    }

    private Expression primary() {
        Token current = get(0);
        if (match(TokenType.NUMBER)) {
            return new ValueExpression(Double.parseDouble(current.getText()));
        }
        if (match(TokenType.HEX_NUMBER)) {
            return new ValueExpression(Long.parseLong(current.getText(), 16));
        }
        if (match(TokenType.NEW)) {
            String cls = consume(TokenType.WORD).getText();
            consume(TokenType.LPAREN);
            List<Expression> args = new java.util.ArrayList<>();
            while (!match(TokenType.RPAREN)) {
                args.add(expression());
                match(TokenType.COM);
            }
            return new ObjectCreationExpression(cls, args);
        }
        if (match(TokenType.THIS)) {
            return new VariableExpression("this");
        }
        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LPAREN) {
            return function();
        }
        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LBRACKET) {
            return element();
        }
        if (lookMatch(0, TokenType.LBRACKET)) {
            return array();
        }
        if (match(TokenType.WORD)) {
            return new VariableExpression(current.getText());
        }
        if (match(TokenType.TEXT)) {
            return new ValueExpression(current.getText());
        }

        if (match(TokenType.LPAREN)) {
            Expression result = expression();
            match(TokenType.RPAREN);
            return result;
        }
        throw new ParseException("Неизвестное выражение: " + current);
    }


    private Expression postfix() {
        Expression expr = primary();
        while (true) {
            if (match(TokenType.DOT)) {
                String name = consume(TokenType.WORD).getText();
                List<Expression> args = null;
                if (match(TokenType.LPAREN)) {
                    args = new ArrayList<>();
                    while (!match(TokenType.RPAREN)) {
                        args.add(expression());
                        match(TokenType.COM);
                    }
                }
                expr = new MemberAccessExpression(expr, name, args);
            } else if (lookMatch(0, TokenType.LBRACKET)) {
                List<Expression> indices = new ArrayList<>();
                while (lookMatch(0, TokenType.LBRACKET)) {
                    consume(TokenType.LBRACKET);
                    indices.add(expression());
                    consume(TokenType.RBRACKET);
                }
                expr = new ExprArrayAccessExpression(expr, indices);
            } else {
                break;
            }
        }
        return expr;
    }





    private Token consume(TokenType type) {
        Token current = get(0);
        if (type != current.getType()) {
            throw new ParseException("Токен " + current + "не соответствует токену " +  type);
        }

        pos++;
        return current;
    }


    private boolean match(TokenType type) {
        Token current = get(0);
        if (type != current.getType()) {
            return false;
        }

        pos++;
        return true;
    }

    private boolean lookMatch(int pos, TokenType type) {
        return get(pos).getType() == type;
    }

    private Token get(int relativePosition) {
        int position = pos + relativePosition;
        if (position >= size) {
            return EOF;
        }

        return tokens.get(position);
    }
}
