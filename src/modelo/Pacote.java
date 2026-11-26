package modelo;

import excecao.DadoInvalidoException;

public abstract class Pacote {
    private static int proximoCodigo = 1;
    private int codigo;
    protected double precoBase;
    private int vagas;
    private boolean disponivel;

    public Pacote(double precoBase, int vagas) {
        this.codigo = proximoCodigo++;
        setPrecoBase(precoBase);
        setVagas(vagas);
        this.disponivel = true;
    }

    public Pacote(double precoBase) {
        this(precoBase, 20);
    }

    public int getCodigo() {
        return codigo;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(double precoBase) {
        if (precoBase <= 0) {
            throw new DadoInvalidoException("O preco do pacote deve ser maior que zero.");
        }
        this.precoBase = precoBase;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        if (vagas < 0) {
            throw new DadoInvalidoException("O numero de vagas nao pode ser negativo.");
        }
        this.vagas = vagas;
        this.disponivel = vagas > 0;
    }

    public boolean isDisponivel() {
        return disponivel && vagas > 0;
    }

    public void venderVaga() {
        if (!isDisponivel()) {
            throw new DadoInvalidoException("Nao ha vagas para este pacote.");
        }
        vagas--;
        disponivel = vagas > 0;
    }

    public void liberarVaga() {
        vagas++;
        disponivel = true;
    }

    public abstract double calcularPreco();

    @Override
    public String toString() {
        return "[" + codigo + "] " + getClass().getSimpleName()
                + " | preco: R$ " + String.format("%.2f", calcularPreco())
                + " | vagas: " + vagas
                + " | " + (isDisponivel() ? "DISPONIVEL" : "ESGOTADO");
    }
}
