package br.com.tinnova.bubble;

import java.util.Arrays;

/**
 * Implementação do algoritmo de ordenação Bubble Sort.
 * Complexidade: O(n^2) no pior caso.
 * Funciona trocando elementos adjacentes fora de ordem, repetidamente,
 * até que o vetor esteja ordenado.
 */
public class BubbleSort {

    // Ordena o vetor usando Bubble Sort
    public static void sort(int[] v) {
        if (v == null || v.length < 2) return;

        int n = v.length;
        boolean trocou;

        // A variável "i" marca quantos elementos finais já estão na posição correta
        for (int i = 0; i < n - 1; i++) {
            trocou = false;

            // O loop continua até n-1-i porque o final já está ordenado nas iterações anteriores
            for (int j = 0; j < n - 1 - i; j++) {
                if (v[j] > v[j + 1]) {
                    int tmp = v[j];
                    v[j] = v[j + 1];
                    v[j + 1] = tmp;
                    trocou = true;
                }
            }
            // Feito para otimização: Se não houve uma troca, significa que já está ordenado
            if (!trocou) break;
        }
    }

    // Método main para demonstração
    public static void main(String[] args) {
        // Exemplo de uso baseado no desafio
        int[] b = {5, 3, 2, 4, 7, 1, 0, 6};

        System.out.println("Entrada : " + Arrays.toString(b));
        BubbleSort.sort(b);
        System.out.println("Ordenado: " + Arrays.toString(b));
    }
}