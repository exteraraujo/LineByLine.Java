import java.util.Map;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * O CodeAnalyzer é o "cérebro" do projeto LineByLine.
 * Versão 3.0: Carrega as regras de um arquivo externo (rules.txt),
 * tornando a base de conhecimento facilmente expansível.
 */
public class CodeAnalyzer {

    // O nome do arquivo de onde vamos carregar as regras
    private static final String RULES_FILE = "rules.txt";
    // Nosso separador customizado
    private static final String SEPARATOR = ";;;";

    private Map<Pattern, String> ruleBase;

    /**
     * Construtor da classe.
     * Ao ser criado, o analisador inicializa o Map e chama
     * o novo método para carregar as regras do arquivo.
     */
    public CodeAnalyzer() {
        this.ruleBase = new LinkedHashMap<>();
        // Trocamos o 'initializeRules()' por 'loadRulesFromFile()'
        loadRulesFromFile();
    }

    /**
     * Novo método!
     * Lê o arquivo 'rules.txt' do 'classpath' (mesma pasta dos .class)
     * e popula o 'ruleBase'.
     */
    private void loadRulesFromFile() {
        System.out.println("[LineByLine] Carregando base de regras de " + RULES_FILE + "...");

        try (
            // 1. Pega o arquivo 'rules.txt' como um 'fluxo' de dados
            InputStream is = CodeAnalyzer.class.getClassLoader().getResourceAsStream(RULES_FILE);
            
            // 2. Cria um 'leitor' para esse fluxo, especificando o formato (UTF-8)
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            
            // 3. Cria um 'leitor de buffer' (mais eficiente) para ler linha por linha
            BufferedReader reader = new BufferedReader(isr)
        ) {
            String line;
            // 4. Lê cada linha do arquivo até o fim (quando 'line' for 'null')
            while ((line = reader.readLine()) != null) {
                // Ignora linhas vazias ou que sejam comentários no arquivo de regras
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // 5. Divide a linha no nosso separador
                // O '2' significa "divida em no máximo 2 partes",
                // para o caso da explicação ter um ';'.
                String[] parts = line.split(SEPARATOR, 2);

                if (parts.length == 2) {
                    try {
                        // 6. Parte 0 é o Regex, Parte 1 é a Explicação
                        String regex = parts[0];
                        String explanation = parts[1];
                        
                        // 7. Compila o Regex e adiciona ao nosso Map
                        this.ruleBase.put(Pattern.compile(regex), explanation);

                    } catch (PatternSyntaxException e) {
                        // Se o Regex no arquivo .txt estiver escrito errado
                        System.err.println("[Erro de Regra] Regex mal formatado: " + parts[0]);
                    }
                }
            }
            System.out.println("[LineByLine] Base de regras carregada com sucesso.");

        } catch (IOException e) {
            // Se o 'rules.txt' não for encontrado ou der erro de leitura
            System.err.println("[Erro Fatal] Não foi possível carregar o arquivo de regras: " + RULES_FILE);
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Se 'getResourceAsStream' não achar o arquivo
            System.err.println("[Erro Fatal] Arquivo " + RULES_FILE + " não encontrado no classpath.");
        }
    }

    /**
     * O método de análise continua O MESMO.
     * Ele não se importa 'como' o ruleBase foi preenchido
     * (seja por código ou por arquivo). Isso é um bom design!
     */
    public String analyzeLine(String line) {
        String trimmedLine = line.trim();

        if (trimmedLine.isEmpty()) {
            return "";
        }

        for (Pattern pattern : ruleBase.keySet()) {
            Matcher matcher = pattern.matcher(trimmedLine);
            if (matcher.find()) {
                return ruleBase.get(pattern);
            }
        }

        return "--> (Linha recebida. Nenhuma explicação simples encontrada para este padrão.)";
    }
}