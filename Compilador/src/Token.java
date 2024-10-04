public class Token {
    private final TokenType type;
    private final String lexeme;
    private final int line;
    private final int column;


    public Token(TokenType type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    public TokenType getType() {
        return type;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", lexeme='" + lexeme + '\'' + '}';
    }
}

