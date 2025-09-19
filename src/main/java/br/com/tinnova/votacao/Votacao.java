package br.com.tinnova.votacao;

/**
 * Classe para calcular percentuais de votos em uma eleição.
 * Recebe o total de eleitores, votos válidos, brancos e nulos.
 * Fornece métodos para calcular os percentuais correspondentes.
 */
public class Votacao {

    // Variáveis
    private int total;
    private int validos;
    private int brancos;
    private int nulos;

    // Construtor
    public Votacao(int total, int validos, int brancos, int nulos) {
        // Validações - Realizado para garantir a integridade dos dados
        if (total < 0 || validos < 0 || brancos < 0 || nulos < 0) {
            throw new IllegalArgumentException("Nenhum valor pode ser negativo.");
        }
        if (validos + brancos + nulos != total) {
            throw new IllegalArgumentException(
                "A soma (válidos + brancos + nulos) deve ser igual ao total.");
        }
        this.total = total;
        this.validos = validos;
        this.brancos = brancos;
        this.nulos = nulos;
    }

    // Métodos para calcular os percentuais
    public double percentualValidos() { return validos * 100.0 / total; }
    public double percentualBrancos() { return brancos * 100.0 / total; }
    public double percentualNulos()   { return nulos   * 100.0 / total; }

    // Método main para demonstração
    public static void main(String[] args) {
        // Exemplo de uso baseado no desafio
        Votacao v = new Votacao(1000, 800, 150, 50);

        System.out.println("Resultados:");
        System.out.printf("Porcentagem de votos válidos: %.2f%%%n", v.percentualValidos());
        System.out.printf("Porcentagem de votos brancos: %.2f%%%n", v.percentualBrancos());
        System.out.printf("Porcentagem de votos nulos: %.2f%%%n", v.percentualNulos());
    }
}