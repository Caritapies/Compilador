import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Uso: java Main <nombre_del_archivo>");
            return;
        }

        String input;
        try {
            input = readFile(args[0]);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        Parser parser = new Parser(tokens);

        try {
            StringBuilder outputHtml = new StringBuilder();
            parser.parse(outputHtml);

            // Almacenar el resultado de la traducción en un archivo HTML
            Files.writeString(Paths.get("output.html"), outputHtml.toString());
            System.out.println("La entrada es válida. Traducción almacenada en output.html.");

        } catch (RuntimeException e) {
            System.out.println(e.getMessage()); // Muestra error sintáctico con detalles de fila y columna
        }
    }

    private static String readFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }
            return stringBuilder.toString();
        }
    }
}
