import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int currentTokenIndex;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    public void parse() {
        parseOrganization();
        if (currentTokenIndex < tokens.size()) {
            throw new RuntimeException("Error de sintaxis: Tokens adicionales encontrados.");
        }
    }

    private void parseOrganization() {
        match(TokenType.ORGANIZATION);
        match(TokenType.KEY);
        match(TokenType.NAME);
        match(TokenType.KEY);
        match(TokenType.IDENTIFIER);
        match(TokenType.KEY);
        match(TokenType.DESCRIPTION);
        matchString();
        parseGroups();
        match(TokenType.KEY);
    }

    private void parseGroups() {
        if (lookahead(TokenType.GROUP)) {
            parseGroup();
            parseGroups();
        } // Epsilon

    }

    private void parseGroup() {
        match(TokenType.GROUP);
        parsePublications();
    }

    private void parsePublications() {
        if (lookahead(TokenType.PUBLICATION)) {
            parsePublication();
            parsePublications();
        }
        // Epsilon
    }

    private void parsePublication() {
        match(TokenType.PUBLICATION);
        match(TokenType.KEY);
        match(TokenType.TITLE);
        matchString();
        match(TokenType.CONTENT);
        matchString();
        match(TokenType.DATEP);
        matchString();
        match(TokenType.STATE);
        matchString();
        parseComents();
        match(TokenType.KEY);
    }

    private void parseComents() {
        if (lookahead(TokenType.COMENT)) {
            parseComent();
            parseComents();
        }
        // Epsilon
    }

    private void parseComent() {
        match(TokenType.COMENT);
        match(TokenType.KEY);
        match(TokenType.CONTENT);
        matchString();
        match(TokenType.DATEC);
        matchString();
        match(TokenType.KEY);
    }

    private boolean lookahead(TokenType expected) {
        if (currentTokenIndex < tokens.size()) {
            return tokens.get(currentTokenIndex).getType() == expected;
        }
        return false;
    }

    private void match(TokenType expected) {
        if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).getType() == expected) {
            currentTokenIndex++;
        } else {
            throw new RuntimeException("Error de sintaxis: se esperaba " + expected+" y se encontró "+tokens.get(currentTokenIndex));
        }
    }
    private void matchString(){
        match(TokenType.KEY);
        match(TokenType.STRING);
        match(TokenType.KEY);
    }
}

