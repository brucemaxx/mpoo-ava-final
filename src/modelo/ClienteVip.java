package modelo;

import excecao.DadoInvalidoException;

public class ClienteVip extends Cliente {
    protected double percentualDesconto;
    public static final int LIMITE_PARCELAS = 12;

    public ClienteVip(String nome, String documento, String passaporte, double percentualDesconto) {
        super(nome, documento, passaporte);
        setPercentualDesconto(percentualDesconto);
    }

    public ClienteVip(String nome, String documento, String passaporte) {
        this(nome, documento, passaporte, 8.0);
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(double percentualDesconto) {
        if (percentualDesconto <= 0 || percentualDesconto > 100) {
            throw new DadoInvalidoException("O desconto do cliente VIP deve ficar entre 1 e 100.");
        }
        this.percentualDesconto = percentualDesconto;
    }

    public double aplicarDesconto(double valor) {
        return valor * (1.0 - percentualDesconto / 100.0);
    }

    @Override
    public String toString() {
        return super.toString().replace(" | comum", "") + " | VIP (" + percentualDesconto + "%)";
    }
}
