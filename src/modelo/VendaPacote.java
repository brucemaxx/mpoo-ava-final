package modelo;

import java.util.ArrayList;
import java.util.List;

import excecao.DadoInvalidoException;
import interfaces.Cancelavel;
import interfaces.Cobravel;

public class VendaPacote implements Cancelavel, Cobravel {
    private static int proximoCodigo = 1;
    private int codigo;
    private Cliente cliente;
    private Pacote pacote;
    private List<ServicoOpcional> servicos;
    private int parcelas;
    private String situacao;
    private double totalFinal;

    public VendaPacote(Cliente cliente, Pacote pacote, int parcelas) {
        this.codigo = proximoCodigo++;
        if (cliente == null || pacote == null) {
            throw new DadoInvalidoException("Cliente e pacote sao obrigatorios.");
        }
        this.cliente = cliente;
        this.pacote = pacote;
        setParcelas(parcelas);
        this.servicos = new ArrayList<ServicoOpcional>();
        this.situacao = "ABERTA";
        this.totalFinal = 0.0;
    }

    public VendaPacote(Cliente cliente, Pacote pacote) {
        this(cliente, pacote, 1);
    }

    public int getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Pacote getPacote() {
        return pacote;
    }

    public List<ServicoOpcional> getServicos() {
        return servicos;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        int limite = (cliente instanceof ClienteVip)
                ? ClienteVip.LIMITE_PARCELAS : 6;
        if (parcelas < 1 || parcelas > limite) {
            throw new DadoInvalidoException(
                    "O numero de parcelas deve ficar entre 1 e " + limite + ".");
        }
        this.parcelas = parcelas;
    }

    public String getSituacao() {
        return situacao;
    }

    public boolean estaAberta() {
        return "ABERTA".equals(situacao);
    }

    public void adicionarServico(String descricao, double valor) {
        if (!estaAberta()) {
            throw new DadoInvalidoException("Nao e possivel adicionar servicos a uma venda encerrada.");
        }
        servicos.add(new ServicoOpcional(descricao, valor));
    }

    public double calcularSubtotal() {
        double subtotal = pacote.calcularPreco();
        for (ServicoOpcional servico : servicos) {
            subtotal += servico.calcularTotal();
        }
        return subtotal;
    }

    @Override
    public double calcularTotal() {
        if ("FINALIZADA".equals(situacao)) {
            return totalFinal;
        }
        double subtotal = calcularSubtotal();
        if (cliente instanceof ClienteVip) {
            ClienteVip vip = (ClienteVip) cliente;
            return vip.aplicarDesconto(subtotal);
        }
        return subtotal;
    }

    public double finalizar() {
        if (!estaAberta()) {
            throw new DadoInvalidoException("A venda " + codigo + " ja foi encerrada.");
        }
        totalFinal = calcularTotal();
        situacao = "FINALIZADA";
        return totalFinal;
    }

    @Override
    public void cancelar() {
        if (!estaAberta()) {
            throw new DadoInvalidoException("A venda " + codigo + " ja foi encerrada.");
        }
        pacote.liberarVaga();
        situacao = "CANCELADA";
    }

    @Override
    public String toString() {
        return "[" + codigo + "] cliente: " + cliente.getNome()
                + " | pacote: " + pacote.getCodigo()
                + " | " + situacao
                + " | valor: R$ " + String.format("%.2f", calcularTotal());
    }
}
