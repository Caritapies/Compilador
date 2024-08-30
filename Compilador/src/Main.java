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

        public static void main(String[] args) {
            String input = """
            ORGANIZATION NAME{%UCO%}DESCRIPTION{"Universidad"}
            GROUP PUBLICATION TITLE{"Reunión General"}CONTENT{"hoy habrá reunión a las 4"}DATEP{"07/08/2023"}STATE{"Activa"}
            COMENT CONTENT{"Allá estaré"}DATEC{"14/08/2023"}
            
            """;
            Lexer lexer = new Lexer(input);
            List<Token> tokens = lexer.tokenize();

            for (Token token : tokens) {
                System.out.println(token);
            }


        }
}


