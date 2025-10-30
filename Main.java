import java.util.Scanner; // Importa a ferramenta Scanner para ler a entrada do usuário

/**
 * Classe principal do projeto LineByLine.Java.
 * Esta classe é responsável por rodar o aplicativo de console,
 * capturar a entrada do usuário e exibir as explicações.
 */
public class Main {

    /**
     * Método principal (main), o ponto de entrada do nosso programa.
     */
    public static void main(String[] args) {
        
        // 1. Inicialização
        // Cria um Scanner para "ouvir" o que o usuário digita no console
        Scanner scanner = new Scanner(System.in);
        // Cria uma instância do nosso "cérebro" analisador
        CodeAnalyzer analyzer = new CodeAnalyzer();

        // 2. Apresentação
        System.out.println("--- Bem-vindo ao LineByLine.Java ---");
        System.out.println("Aprenda Java entendendo o que o seu código faz.");
        System.out.println("\nDigite seu código Java linha por linha.");
        System.out.println("Digite 'fim' para sair.");
        System.out.println("----------------------------------------");

        // 3. O Loop Principal do Programa
        // 'while (true)' cria um loop que roda para sempre,
        // até que encontremos o comando 'break'.
        while (true) {
            
            // Mostra o cursor '>' para indicar que estamos prontos para receber
            System.out.print("> ");
            
            // Lê a linha inteira que o usuário digitou
            String inputLine = scanner.nextLine();

            // 4. Verificação de Saída
            // Compara a entrada com "fim", ignorando maiúsculas/minúsculas
            if (inputLine.equalsIgnoreCase("fim")) {
                System.out.println("\nSessão encerrada. Bons estudos!");
                break; // Quebra o loop 'while (true)' e encerra o programa
            }

            // 5. Análise e Resposta
            // Envia a linha para o analisador
            String explanation = analyzer.analyzeLine(inputLine);

            // 6. Exibição do Resultado
            // Imprime a linha original do usuário e a explicação lado a lado
            // (Usamos printf para formatar a saída de forma organizada)
            System.out.printf("%-45s %s%n", inputLine, explanation);
        }

        // 7. Encerramento
        // Fecha o scanner para liberar os recursos do sistema
        scanner.close();
    }
}