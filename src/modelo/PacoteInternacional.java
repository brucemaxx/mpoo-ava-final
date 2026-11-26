package modelo;

import excecao.DadoInvalidoException;

public class PacoteInternacional extends Pacote {
    private double taxaEmbarque;

    public PacoteInternacional(double precoBase, int vagas, double taxaEmbarque) {
        super(precoBase, vagas);
        setTaxaEmbarque(taxaEmbarque);
    }

    public PacoteInternacional(double precoBase, int vagas) {
        this(precoBase, vagas, 350.0);
    }

    public double getTaxaEmbarque() {
        return taxaEmbarque;
    }

    public void setTaxaEmbarque(double taxaEmbarque) {
        if (taxaEmbarque < 0) {
            throw new DadoInvalidoException("A taxa de embarque nao pode ser negativa.");
        }
        this.taxaEmbarque = taxaEmbarque;
    }

    @Override
    public double calcularPreco() {
        return precoBase + taxaEmbarque;
    }
}
