package modelo;

public class PacoteNacional extends Pacote {
    public PacoteNacional(double precoBase, int vagas) {
        super(precoBase, vagas);
    }

    public PacoteNacional(double precoBase) {
        super(precoBase);
    }

    @Override
    public double calcularPreco() {
        return precoBase;
    }
}
