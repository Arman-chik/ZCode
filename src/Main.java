import parser.Lexer;
import parser.Parser;
import parser.Token;
import parser.ast.Statement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("program.txt")), "UTF-8");
        List<Token> tokens = new Lexer(input).tokenize();
        for (Token token : tokens) {
            System.out.println(token);
        }


        Statement program = new Parser(tokens).parse();
        System.out.println(program.toString());
        program.execute();
    }
}