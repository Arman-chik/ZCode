import parser.Lexer;
import parser.Parser;
import parser.Token;
import parser.ast.Statement;
import parser.visitors.AssignValidator;
import parser.visitors.FunctionsAdder;
import parser.visitors.VariablePrinter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
public static void main(String[] args) throws IOException {
    String input = new String(Files.readAllBytes(Paths.get("tests_files/PongGame.txt")), "UTF-8");
    List<Token> tokens = new Lexer(input).tokenize();
    for (Token token : tokens) {
        System.out.println(token);
    }

    // tests_files/SnakeGame.txt
    // tests_files/PainterApp.txt
    // tests_files/PongGame.txt

    Statement program = new Parser(tokens).parse();
    System.out.println(program.toString());
    program.accept(new FunctionsAdder());
    // program.accept(new VariablePrinter()); // Вывод переменных задействованых в программе.
    program.accept(new AssignValidator()); // Для запрета присваивания значений константам.
    program.execute();
    }
}