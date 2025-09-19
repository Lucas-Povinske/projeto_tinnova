package br.com.tinnova.multiplos;

/**
 * Soma dos múltiplos de 3 ou 5 abaixo de um limite X (exclusivo).
 */
public class Multiplos {

    // Implementação matemática (O(1))
    public static long somaAte(int limite) {
        if (limite <= 0) return 0L;
        long s3 = somaProgressao(limite, 3);
        long s5 = somaProgressao(limite, 5);
        long s15 = somaProgressao(limite, 15);
        return s3 + s5 - s15; // Para evitar contar os múltiplos de 15 duas vezes
    }

    private static long somaProgressao(int limite, int k) {
        long m = (limite - 1L) / k;   // Quantidade de múltiplos de k menores que o limite
        return k * m * (m + 1) / 2;            // k * (1 + 2 + ... + m)
    }

    // Método main para demonstração
    public static void main(String[] args) {
        // Exemplo de uso baseado no desafio
        int[] xs = {10, 16, 30, 0, 2, 100};

        for (int x : xs) {
            long r = Multiplos.somaAte(x);
            System.out.printf("X = %d → Soma = %d%n", x, r);
        }
    }
}