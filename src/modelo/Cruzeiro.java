package modelo;

import excecao.DadoInvalidoException;

public class Cruzeiro extends Pacote {
    private int noites;
    public static final double PRECO_POR_NOITE = 450.0;

    public Cruzeiro(int noites, int vagas) {
        super(PRECO_POR_NOITE * noites, vagas);
        setNoites(noites);
    }

    public Cruzeiro(int noites) {
        this(noites, 20);
    }

    public int getNoites() {
        return noites;
    }

    public void setNoites(int noites) {
        if (noites < 1 || noites > 30) {
            throw new DadoInvalidoException("O cruzeiro deve ter entre 1 e 30 noites.");
        }
        this.noites = noites;
    }

    @Override
    public double calcularPreco() {
        return PRECO_POR_NOITE * noites;
    }
}
