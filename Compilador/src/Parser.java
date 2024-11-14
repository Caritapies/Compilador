import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {
    private final List<Token> tokens;
    private int currentTokenIndex;
    private final Set<String> titulosPublicacion = new HashSet<>();

    private static final String ORGANIZATION_START = "<html><head><title>Organization Report</title><meta charset='utf-8'></head><body>";
    private static final String ORGANIZATION_END = "</body></html>";
    private static final String GROUP_START = "<div class='group'><h2>Group</h2><table border='1'><tr><th>Title</th><th>Content</th><th>Date</th><th>Status</th></tr>";
    private static final String GROUP_END = "</table></div><hr>";
    private static final String ERROR_SYNTAX = "Error de sintaxis en línea ";
    private static final String COLUMN = ", columna ";

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    public void parse(StringBuilder outputHtml) {
        outputHtml.append(ORGANIZATION_START);
        parseOrganization(outputHtml);
        outputHtml.append(ORGANIZATION_END);

        if (currentTokenIndex < tokens.size()) {
            throw new RuntimeException("Error de sintaxis: Tokens adicionales encontrados.");
        }
    }

    private void parseOrganization(StringBuilder outputHtml) {
        match(TokenType.ORGANIZATION);
        match(TokenType.KEY);
        match(TokenType.NAME);
        match(TokenType.KEY);
        match(TokenType.IDENTIFIER);
        outputHtml.append("<h1>Organization: ").append(getLexeme()).append("</h1>");
        match(TokenType.KEY);
        match(TokenType.DESCRIPTION);
        matchString(outputHtml, "<p><strong>Description:</strong> ", "</p><hr>");
        parseGroups(outputHtml);
        match(TokenType.KEY);
    }

    private void parseGroups(StringBuilder outputHtml) {
        while (lookahead(TokenType.GROUP)) {
            parseGroup(outputHtml);
        }
    }

    private void parseGroup(StringBuilder outputHtml) {
        match(TokenType.GROUP);
        outputHtml.append(GROUP_START);
        parsePublications(outputHtml);
        outputHtml.append(GROUP_END);
    }

    private void parsePublications(StringBuilder outputHtml) {
        while (lookahead(TokenType.PUBLICATION)) {
            parsePublication(outputHtml);
        }
    }

    private void parsePublication(StringBuilder outputHtml) {
        match(TokenType.PUBLICATION);
        match(TokenType.KEY);

        // Procesar y validar el título
        match(TokenType.TITLE);
        String title = matchAndReturnString(outputHtml, "<tr><td><strong>", "</strong></td>");
        validateUniqueTitle(title);

        // Procesar contenido
        match(TokenType.CONTENT);
        matchString(outputHtml, "<td>", "</td>");

        // Procesar y validar la fecha de publicación
        match(TokenType.DATEP);
        String dateP = matchAndReturnString(outputHtml, "<td>", "</td>");
        validateDate(dateP, "DATEP");

        // Procesar estado
        match(TokenType.STATE);
        matchString(outputHtml, "<td>", "</td></tr>");

        parseComents(outputHtml);
        match(TokenType.KEY);
    }

    private void parseComents(StringBuilder outputHtml) {
        while (lookahead(TokenType.COMENT)) {
            parseComent(outputHtml);
        }
    }

    private void parseComent(StringBuilder outputHtml) {
        match(TokenType.COMENT);
        match(TokenType.KEY);

        // Procesar comentario
        match(TokenType.CONTENT);
        matchString(outputHtml, "<tr><td colspan='4'><strong>Comment:</strong> ", "</td></tr>");

        // Procesar y validar fecha del comentario
        match(TokenType.DATEC);
        String dateC = matchAndReturnString(outputHtml, "<tr><td colspan='4'><p><strong>Date:</strong> ", "</p></td></tr>");
        validateDate(dateC, "DATEC");

        match(TokenType.KEY);
    }

    private boolean lookahead(TokenType expected) {
        return currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).getType() == expected;
    }

    private void match(TokenType expected) {
        if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).getType() == expected) {
            currentTokenIndex++;
        } else {
            Token token = tokens.get(currentTokenIndex);
            throw new RuntimeException(ERROR_SYNTAX + token.getLine() + COLUMN + token.getColumn() +
                    ": se esperaba " + expected + " y se encontró " + token.getLexeme());
        }
    }

    private void matchString(StringBuilder outputHtml, String prefix, String suffix) {
        match(TokenType.KEY);
        outputHtml.append(prefix);
        match(TokenType.STRING);
        outputHtml.append(getLexeme());
        match(TokenType.KEY);
        outputHtml.append(suffix);
    }

    private String matchAndReturnString(StringBuilder outputHtml, String prefix, String suffix) {
        match(TokenType.KEY);
        outputHtml.append(prefix);
        match(TokenType.STRING);
        String lexeme = getLexeme();
        outputHtml.append(lexeme);
        match(TokenType.KEY);
        outputHtml.append(suffix);
        return lexeme;
    }

    private String getLexeme() {
        return tokens.get(currentTokenIndex - 1).getLexeme();
    }

    private void validateUniqueTitle(String title) {
        if (titulosPublicacion.contains(title)) {
            System.out.println("Advertencia: Título duplicado en PUBLICATION: " + title);
        } else {
            titulosPublicacion.add(title);
        }
    }

    private void validateDate(String date, String dateType) {
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            System.out.println("Advertencia: Fecha en " + dateType + " no válida: " + date);
        }
    }
}
