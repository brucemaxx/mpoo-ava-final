package sistema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import excecao.ClienteNaoEncontradoException;
import excecao.DadoInvalidoException;
import excecao.OperacaoNaoEncontradaException;
import excecao.PacoteIndisponivelException;
import interfaces.Cobravel;
import modelo.Cliente;
import modelo.ClienteVip;
import modelo.Cruzeiro;
import modelo.Pacote;
import modelo.PacoteInternacional;
import modelo.PacoteNacional;
import modelo.VendaPacote;

public class Agencia {
    public static final int LIMITE_VENDAS_ABERTAS_COMUM = 1;
    public static final int LIMITE_VENDAS_ABERTAS_VIP = 5;

    private Repositorio<Cliente> clientes;
    private Repositorio<Pacote> pacotes;
    private Repositorio<VendaPacote> vendas;
    private Map<String, Cliente> clientesPorDocumento;
    private Set<String> documentosCadastrados;

    public Agencia() {
        clientes = new Repositorio<Cliente>();
        pacotes = new Repositorio<Pacote>();
        vendas = new Repositorio<VendaPacote>();
        clientesPorDocumento = new HashMap<String, Cliente>();
        documentosCadastrados = new HashSet<String>();
    }

    public void carregarDadosIniciais() {
        if (clientes.tamanho() > 0 || pacotes.tamanho() > 0 || vendas.tamanho() > 0) {
            return;
        }
        cadastrarCliente("Ana Souza", "12345678900", "", false);
        cadastrarCliente("Bruno Lima", "98765432100", "P123456", true);
        cadastrarCliente("Carla Dias", "11122233344", "P998877", false);
        cadastrarPacote(new PacoteNacional(2400.0, 8));
        cadastrarPacote(new PacoteInternacional(5200.0, 5, 350.0));
        cadastrarPacote(new Cruzeiro(7, 12));
        try {
            VendaPacote venda = abrirVenda(1, 1, 4);
            venda.adicionarServico("Seguro viagem", 180.0);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Cliente cadastrarCliente(String nome, String documento, String passaporte,
                                    boolean especial) {
        String chave = documento == null ? "" : documento.trim();
        if (documentosCadastrados.contains(chave)) {
            throw new DadoInvalidoException("Documento ja cadastrado.");
        }
        Cliente cliente;
        if (especial) {
            cliente = new ClienteVip(nome, documento, passaporte);
        } else {
            cliente = new Cliente(nome, documento, passaporte);
        }
        clientes.adicionar(cliente);
        clientesPorDocumento.put(chave, cliente);
        documentosCadastrados.add(chave);
        return cliente;
    }

    public Pacote cadastrarPacote(Pacote pacote) {
        if (pacote == null) {
            throw new DadoInvalidoException("O pacote nao pode ser nulo.");
        }
        pacotes.adicionar(pacote);
        return pacote;
    }

    public Repositorio<Cliente> getRepositorioClientes() {
        return clientes;
    }

    public Repositorio<Pacote> getRepositorioPacotes() {
        return pacotes;
    }

    public Repositorio<VendaPacote> getRepositorioVendas() {
        return vendas;
    }

    public Cliente buscarCliente(int codigo) throws ClienteNaoEncontradoException {
        for (Cliente cliente : clientes.listar()) {
            if (cliente.getCodigo() == codigo) {
                return cliente;
            }
        }
        throw new ClienteNaoEncontradoException("Cliente nao encontrado.");
    }

    public Cliente buscarCliente(String nome) throws ClienteNaoEncontradoException {
        for (Cliente cliente : clientes.listar()) {
            if (cliente.getNome().equalsIgnoreCase(nome)) {
                return cliente;
            }
        }
        throw new ClienteNaoEncontradoException("Cliente nao encontrado.");
    }

    public Pacote buscarPacote(int codigo) {
        for (Pacote pacote : pacotes.listar()) {
            if (pacote.getCodigo() == codigo) {
                return pacote;
            }
        }
        return null;
    }

    public Pacote buscarPacote(String nome) {
        for (Pacote pacote : pacotes.listar()) {
            if (pacote.getClass().getSimpleName().equalsIgnoreCase(nome)) {
                return pacote;
            }
        }
        return null;
    }

    public VendaPacote buscarVenda(int codigo) throws OperacaoNaoEncontradaException {
        for (VendaPacote venda : vendas.listar()) {
            if (venda.getCodigo() == codigo) {
                return venda;
            }
        }
        throw new OperacaoNaoEncontradaException("Venda nao encontrada.");
    }

    public VendaPacote abrirVenda(int codigoCliente, int codigoPacote, int parcelas)
            throws PacoteIndisponivelException, ClienteNaoEncontradoException,
                   OperacaoNaoEncontradaException {
        Cliente cliente = buscarCliente(codigoCliente);
        Pacote pacote = buscarPacote(codigoPacote);
        if (pacote == null) {
            throw new OperacaoNaoEncontradaException("Pacote nao encontrado.");
        }
        if (!pacote.isDisponivel()) {
            throw new PacoteIndisponivelException("Pacote " + codigoPacote + " sem vagas.");
        }
        if (pacote instanceof PacoteInternacional && !cliente.temPassaporte()) {
            throw new PacoteIndisponivelException(
                    "Pacote internacional exige passaporte cadastrado.");
        }
        int abertas = contarVendasAbertas(cliente);
        int limite = (cliente instanceof ClienteVip)
                ? LIMITE_VENDAS_ABERTAS_VIP : LIMITE_VENDAS_ABERTAS_COMUM;
        if (abertas >= limite) {
            throw new PacoteIndisponivelException(
                    "O cliente atingiu o limite de vendas abertas.");
        }
        VendaPacote venda = new VendaPacote(cliente, pacote, parcelas);
        vendas.adicionar(venda);
        pacote.venderVaga();
        return venda;
    }

    private int contarVendasAbertas(Cliente cliente) {
        int quantidade = 0;
        for (VendaPacote venda : vendas.listar()) {
            if (venda.estaAberta() && venda.getCliente() == cliente) {
                quantidade++;
            }
        }
        return quantidade;
    }

    public void cancelarVenda(int codigo) throws Exception {
        VendaPacote venda = buscarVenda(codigo);
        venda.cancelar();
    }

    public double finalizarVenda(int codigo) throws Exception {
        VendaPacote venda = buscarVenda(codigo);
        return venda.finalizar();
    }

    public void removerCliente(int codigo) throws Exception {
        Cliente cliente = buscarCliente(codigo);
        for (VendaPacote venda : vendas.listar()) {
            if (venda.estaAberta() && venda.getCliente() == cliente) {
                throw new DadoInvalidoException(
                        "Cliente possui venda aberta e nao pode ser removido.");
            }
        }
        clientes.remover(cliente);
        clientesPorDocumento.remove(cliente.getDocumento());
        documentosCadastrados.remove(cliente.getDocumento());
    }

    public void removerPacote(int codigo) throws Exception {
        Pacote pacote = buscarPacote(codigo);
        if (pacote == null) {
            throw new OperacaoNaoEncontradaException("Pacote nao encontrado.");
        }
        for (VendaPacote venda : vendas.listar()) {
            if (venda.estaAberta() && venda.getPacote() == pacote) {
                throw new DadoInvalidoException(
                        "Pacote possui venda aberta e nao pode ser removido.");
            }
        }
        pacotes.remover(pacote);
    }

    public double calcularFaturamento() {
        List<Cobravel> cobraveis = new ArrayList<Cobravel>();
        for (VendaPacote venda : vendas.listar()) {
            if ("FINALIZADA".equals(venda.getSituacao())) {
                cobraveis.add(venda);
            }
        }
        double total = 0.0;
        for (Cobravel cobravel : cobraveis) {
            total += cobravel.calcularTotal();
        }
        return total;
    }

    public Map<String, Integer> contarVendasPorCliente() {
        Map<String, Integer> contagem = new HashMap<String, Integer>();
        for (VendaPacote venda : vendas.listar()) {
            String nome = venda.getCliente().getNome();
            Integer quantidade = contagem.get(nome);
            if (quantidade == null) {
                contagem.put(nome, 1);
            } else {
                contagem.put(nome, quantidade + 1);
            }
        }
        return contagem;
    }
}
