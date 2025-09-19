package br.com.tinnova.fatorial;

import java.math.BigInteger;

/**
 * Classe para calcular o fatorial de um número n (n!).
 * Utiliza BigInteger para suportar valores grandes.
 */
public class Fatorial {

    // Calcula o fatorial n! iterativamente
    public static BigInteger calcular(int n) {
        // Validação do número
        if (n < 0) {
            throw new IllegalArgumentException("n deve ser >= 0");
        }
        BigInteger res = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            res = res.multiply(BigInteger.valueOf(i));
        }
        return res;
    }

    // Método main para demonstração
    public static void main(String[] args) {
        // Exemplo de uso baseado no desafio
        int[] casos = {0,1,2,3,4,5,6,7};

        for (int caso : casos) {
            BigInteger f = Fatorial.calcular(caso);
            System.out.printf("%d! = %s%n", caso, f);
        }
    }
}