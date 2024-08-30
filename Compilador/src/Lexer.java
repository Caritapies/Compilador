import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int pos;

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char current = input.charAt(pos);

            if (Character.isWhitespace(current)) {
                pos++;
                continue;
            }
            if (current == '{' || current == '}') {
                tokens.add(new Token(TokenType.KEY, Character.toString(current)));
                pos++;
                continue;
            }

            if (current == '%') {
                tokens.add(new Token(TokenType.IDENTIFIER, extractIdentifier()));
                continue;
            }
            if (current == '"') {
                tokens.add(new Token(TokenType.DESCRIPTION, extractString()));
                continue;
            }

            String word = extractWord();
            switch (word) {
                case "ORGANIZATION" -> tokens.add(new Token(TokenType.ORGANIZATION, word));
                case "NAME" -> tokens.add(new Token(TokenType.IDENTIFIER, word));
                case "DESCRIPTION" -> tokens.add(new Token(TokenType.DESCRIPTION, word));
                case "GROUP" -> tokens.add(new Token(TokenType.GROUP, word));
                case "PUBLICATION" -> tokens.add(new Token(TokenType.PUBLICATION, word));
                case "TITLE" -> tokens.add(new Token(TokenType.TITLE, word));
                case "CONTENT" -> tokens.add(new Token(TokenType.CONTENT, word));
                case "DATEP" -> tokens.add(new Token(TokenType.DATEP, word));
                case "STATE" -> tokens.add(new Token(TokenType.STATE, word));
                case "COMENT" -> tokens.add(new Token(TokenType.COMENT, word));
                case "DATEC" -> tokens.add(new Token(TokenType.DATEC, word));
                default -> tokens.add(new Token(TokenType.UNKNOWN, word));
            }
        }

        return tokens;
    }

    private String extractIdentifier() {
        StringBuilder result = new StringBuilder();
        pos++;
        while (pos < input.length() && input.charAt(pos) != '%') {
            result.append(input.charAt(pos));
            pos++;
        }
        pos++;
        return result.toString();
    }

    private String extractString() {
        StringBuilder result = new StringBuilder();
        pos++;
        while (pos < input.length() && input.charAt(pos) != '"') {
            result.append(input.charAt(pos));
            pos++;
        }
        pos++;
        return result.toString();
    }

    private String extractWord() {
        StringBuilder result = new StringBuilder();
        while (pos < input.length() && Character.isLetter(input.charAt(pos))) {
            result.append(input.charAt(pos));
            pos++;
        }
        return result.toString();
    }
}
