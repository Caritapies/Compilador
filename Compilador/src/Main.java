import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
    /*
    Grammar

<ORGANIZATION> ::= ORGANIZATION {NAME{%id%}DESCRIPTION{"str"}<GROUPS>}

<GROUPS> ::= <GROUP><GROUPS>
           | Epsilon

<GROUP> ::= GROUP <PUBLICATIONS>

<PUBLICATIONS> ::= <PUBLICATION> <PUBLICATIONS>
                | Epsilon

<PUBLICATION> ::= PUBLICATION { TITLE{"str"}CONTENT{"str"}DATEP{"str"}STATE{"str"}<COMENTS>}

<COMENTS> ::= <COMENT><COMENTS>
            | Epsilon

<COMENT> ::= COMENT {CONTENT{"str"}DATEC{"str"}}
*/

    public static void main(String[] args) throws IOException {

        String input = readFile();

        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);

        try {
            parser.parse();
            System.out.println("La entrada es sintácticamente correcta.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    private static String readFile() throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/Usuario/Desktop/Uni/8vo semestre/Compiladores/Compilador/Compilador/src/input.txt"))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        }
    }

}


