import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BMHAlgorithm bmhAlgorithm = new BMHAlgorithm();
        bmhAlgorithm.run();
    }
}

class BMHAlgorithm {
    public void run() {
        Scanner scanner = new Scanner(System.in);

        // Solicita ao usuário escolher a quantidade de textos
        System.out.print("Escolha a quantidade de textos: ");
        int numTexts = scanner.nextInt();

        for (int i = 0; i < numTexts; i++) {
            // Solicita ao usuário escolher o tamanho do texto
            System.out.print("Escolha o tamanho do texto " + (i + 1) + " (500, 1000, 1500, 2000, 3000): ");
            int textSize = getUserSelectedTextSize(scanner);

            // Solicita ao usuário inserir o padrão a ser buscado
            System.out.print("Insira o padrão a ser buscado no texto " + (i + 1) + ": ");
            String pattern = scanner.next();

            // Gera um texto com o tamanho especificado
            String text = generateLoremIpsum(textSize);

            // Executa o algoritmo 10 vezes para obter um comportamento médio
            long totalTime = 0;
            int numExecutions = 10;

            for (int j = 0; j < numExecutions; j++) {
                long startTime = System.nanoTime();
                List<Integer> indices = bmhSearchAll(text, pattern);
                long endTime = System.nanoTime();

                // Calcula o tempo de execução em milissegundos
                long executionTime = (endTime - startTime) / 1000000;
                totalTime += executionTime;

                // Exibe os índices encontrados e o tempo de execução
                System.out.println("Texto " + (i + 1) + " de " + textSize + " caracteres - Execução " + (j + 1) +
                        " - Índices encontrados: " + indices + " - Tempo de execução: " + executionTime + " ms");
            }

            // Calcula e exibe o tempo médio de execução
            long averageTime = totalTime / numExecutions;
            System.out.println("Tempo médio de execução para texto " + (i + 1) + " de " + textSize +
                    " caracteres: " + averageTime + " ms");
        }

        scanner.close();
    }

    // Implementação do algoritmo BMH para encontrar todas as ocorrências
    private static List<Integer> bmhSearchAll(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();

        int textSize = text.length();
        int patternSize = pattern.length();

        if (patternSize == 0 || patternSize > textSize) {
            return indices;
        }

        int[] badCharTable = buildBadCharTable(pattern);

        int i = 0;
        while (i <= textSize - patternSize) {
            int skip = 0;
            for (int j = patternSize - 1; j >= 0; j--) {
                char textChar = text.charAt(i + j);
                char patternChar = pattern.charAt(j);

                if (textChar != patternChar) {
                    skip = Math.max(1, j - badCharTable[textChar]);
                    break;
                }
            }

            if (skip == 0) {
                indices.add(i); // Padrão encontrado
                i++;
            } else {
                i += skip;
            }
        }

        return indices;
    }

    // Constrói a tabela de caracteres ruins para o BMH
    private static int[] buildBadCharTable(String pattern) {
        int[] badCharTable = new int[256];
        Arrays.fill(badCharTable, pattern.length());

        for (int i = 0; i < pattern.length() - 1; i++) {
            badCharTable[pattern.charAt(i)] = pattern.length() - 1 - i;
        }

        return badCharTable;
    }

    // Gera texto "Lorem Ipsum" com o tamanho especificado
    private static String generateLoremIpsum(int size) {
        StringBuilder loremIpsum = new StringBuilder();
        String loremIpsumText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ";

        while (loremIpsum.length() < size) {
            loremIpsum.append(loremIpsumText);
        }

        return loremIpsum.substring(0, size);
    }

    // Solicita ao usuário escolher o tamanho do texto
    private static int getUserSelectedTextSize(Scanner scanner) {
        int selectedSize;
        do {
            
            selectedSize = scanner.nextInt();
        } while (!isValidTextSize(selectedSize));

        return selectedSize;
    }

    // Verifica se o tamanho escolhido é válido
    private static boolean isValidTextSize(int size) {
        int[] validSizes = {500, 1000, 1500, 2000, 3000};
        for (int validSize : validSizes) {
            if (size == validSize) {
                return true;
            }
        }
        return false;
    }
}
