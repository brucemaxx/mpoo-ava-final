package modelo;

import excecao.DadoInvalidoException;
import interfaces.Cobravel;

public class ServicoOpcional implements Cobravel {
    private String descricao;
    private double valor;

    public ServicoOpcional(String descricao, double valor) {
        setDescricao(descricao);
        setValor(valor);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new DadoInvalidoException("A descricao do servico nao pode ser vazia.");
        }
        this.descricao = descricao.trim();
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if (valor <= 0) {
            throw new DadoInvalidoException("O valor do servico deve ser maior que zero.");
        }
        this.valor = valor;
    }

    @Override
    public double calcularTotal() {
        return valor;
    }

    @Override
    public String toString() {
        return descricao + " - R$ " + String.format("%.2f", valor);
    }
}
