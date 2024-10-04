import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int pos;
    private int line;
    private int column;

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
        this.line = 1;
        this.column = 0;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        int columnPhoto;

        while (pos < input.length()) {
            char current = input.charAt(pos);
            if(current == '\n'){
                line++;
                column = 0;
            }

            if (Character.isWhitespace(current)) {
                pos++;
                column++;
                continue;
            }
            if (current == '{' || current == '}') {
                column++;
                columnPhoto = column;
                tokens.add(new Token(TokenType.KEY, Character.toString(current),line, columnPhoto));
                pos++;
                continue;
            }

            if (current == '%') {
                column++;
                columnPhoto = column;
                tokens.add(new Token(TokenType.IDENTIFIER, extractIdentifier(),line, columnPhoto));
                continue;
            }
            if (current == '"') {
                column++;
                columnPhoto = column;
                tokens.add(new Token(TokenType.STRING, extractString(),line, columnPhoto));
                continue;
            }

            String word = extractWord();
            switch (word) {
                case "ORGANIZATION" -> tokens.add(new Token(TokenType.ORGANIZATION, word,line, column));
                case "NAME" -> tokens.add(new Token(TokenType.NAME, word,line, column));
                case "DESCRIPTION" -> tokens.add(new Token(TokenType.DESCRIPTION, word,line, column));
                case "GROUP" -> tokens.add(new Token(TokenType.GROUP, word,line, column));
                case "PUBLICATION" -> tokens.add(new Token(TokenType.PUBLICATION, word,line, column));
                case "TITLE" -> tokens.add(new Token(TokenType.TITLE, word,line, column));
                case "CONTENT" -> tokens.add(new Token(TokenType.CONTENT, word,line, column));
                case "DATEP" -> tokens.add(new Token(TokenType.DATEP, word,line, column));
                case "STATE" -> tokens.add(new Token(TokenType.STATE, word,line, column));
                case "COMENT" -> tokens.add(new Token(TokenType.COMENT, word,line, column));
                case "DATEC" -> tokens.add(new Token(TokenType.DATEC, word,line, column));
                default -> tokens.add(new Token(TokenType.UNKNOWN, word,line, column));
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
            column++;
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
            column++;
        }
        pos++;
        return result.toString();
    }

    private String extractWord() {
        StringBuilder result = new StringBuilder();
        while (pos < input.length() && Character.isLetter(input.charAt(pos))) {
            result.append(input.charAt(pos));
            pos++;
            column++;
        }
        return result.toString();
    }
}
