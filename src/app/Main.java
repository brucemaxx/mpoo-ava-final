package app;

import java.util.Map;
import java.util.Scanner;

import excecao.DadoInvalidoException;
import modelo.Cliente;
import modelo.ClienteVip;
import modelo.Cruzeiro;
import modelo.Pacote;
import modelo.PacoteInternacional;
import modelo.PacoteNacional;
import modelo.ServicoOpcional;
import modelo.VendaPacote;
import sistema.Agencia;

public class Main {
    public static void main(String[] args) {
        Agencia agencia = new Agencia();
        agencia.carregarDadosIniciais();
        Scanner sc = new Scanner(System.in);
        try {
            int opcao;
            do {
                System.out.println();
                System.out.println("===== AGENCIA DE VIAGENS =====");
                System.out.println("1 - Clientes");
                System.out.println("2 - Pacotes");
                System.out.println("3 - Vendas");
                System.out.println("4 - Relatorios");
                System.out.println("0 - Sair");
                opcao = lerInteiro(sc, "Opcao: ");
                switch (opcao) {
                    case 1:
                        menuClientes(sc, agencia);
                        break;
                    case 2:
                        menuPacotes(sc, agencia);
                        break;
                    case 3:
                        menuVendas(sc, agencia);
                        break;
                    case 4:
                        menuRelatorios(sc, agencia);
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opcao invalida!");
                        break;
                }
            } while (opcao != 0);
        } finally {
            sc.close();
        }
    }

    private static int lerInteiro(Scanner sc, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero valido.");
            }
        }
    }

    private static double lerDouble(Scanner sc, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero valido.");
            }
        }
    }

    private static void menuClientes(Scanner sc, Agencia agencia) {
        int opcao;
        do {
            System.out.println();
            System.out.println("----- CLIENTES -----");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Listar clientes");
            System.out.println("3 - Buscar cliente");
            System.out.println("4 - Remover cliente");
            System.out.println("0 - Voltar");
            opcao = lerInteiro(sc, "Opcao: ");
            switch (opcao) {
                case 1:
                    try {
                        int tipo = lerInteiro(sc, "Tipo (1 - Comum, 2 - VIP): ");
                        System.out.print("Nome: ");
                        String nome = sc.nextLine();
                        System.out.print("Documento: ");
                        String documento = sc.nextLine();
                        String passaporte = "";
                        if (tipo == 2) {
                            System.out.print("Passaporte: ");
                            passaporte = sc.nextLine();
                        } else {
                            System.out.print("Passaporte (opcional): ");
                            passaporte = sc.nextLine();
                        }
                        boolean especial = tipo == 2;
                        if (tipo != 1 && tipo != 2) {
                            throw new DadoInvalidoException("Tipo de cliente invalido.");
                        }
                        Cliente cliente = agencia.cadastrarCliente(
                                nome, documento, passaporte, especial);
                        System.out.println("Cliente cadastrado!");
                        System.out.println("Codigo: " + cliente.getCodigo());
                    } catch (DadoInvalidoException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 2:
                    if (agencia.getRepositorioClientes().tamanho() == 0) {
                        System.out.println("Nenhum cliente cadastrado.");
                    } else {
                        for (Cliente cliente : agencia.getRepositorioClientes().listar()) {
                            System.out.println(cliente);
                        }
                    }
                    break;
                case 3:
                    try {
                        int tipoBusca = lerInteiro(sc, "Buscar por (1 - Codigo, 2 - Nome): ");
                        if (tipoBusca == 1) {
                            int codigo = lerInteiro(sc, "Codigo: ");
                            System.out.println(agencia.buscarCliente(codigo));
                        } else if (tipoBusca == 2) {
                            System.out.print("Nome: ");
                            System.out.println(agencia.buscarCliente(sc.nextLine()));
                        } else {
                            System.out.println("Opcao invalida!");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        int codigo = lerInteiro(sc, "Codigo do cliente: ");
                        agencia.removerCliente(codigo);
                        System.out.println("Cliente removido!");
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }
        } while (opcao != 0);
    }

    private static void menuPacotes(Scanner sc, Agencia agencia) {
        int opcao;
        do {
            System.out.println();
            System.out.println("----- PACOTES -----");
            System.out.println("1 - Cadastrar pacote");
            System.out.println("2 - Listar pacotes");
            System.out.println("3 - Buscar pacote");
            System.out.println("4 - Remover pacote");
            System.out.println("0 - Voltar");
            opcao = lerInteiro(sc, "Opcao: ");
            switch (opcao) {
                case 1:
                    try {
                        int tipo = lerInteiro(sc,
                                "Tipo (1 - Nacional, 2 - Internacional, 3 - Cruzeiro): ");
                        Pacote pacote = null;
                        int vagas = lerInteiro(sc, "Numero de vagas: ");
                        if (tipo == 1) {
                            double preco = lerDouble(sc, "Preco: ");
                            pacote = new PacoteNacional(preco, vagas);
                        } else if (tipo == 2) {
                            double preco = lerDouble(sc, "Preco: ");
                            double taxa = lerDouble(sc, "Taxa de embarque: ");
                            pacote = new PacoteInternacional(preco, vagas, taxa);
                        } else if (tipo == 3) {
                            int noites = lerInteiro(sc, "Quantidade de noites: ");
                            pacote = new Cruzeiro(noites, vagas);
                        } else {
                            throw new DadoInvalidoException("Tipo de pacote invalido.");
                        }
                        agencia.cadastrarPacote(pacote);
                        System.out.println("Pacote cadastrado!");
                        System.out.println("Codigo: " + pacote.getCodigo());
                    } catch (DadoInvalidoException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 2:
                    if (agencia.getRepositorioPacotes().tamanho() == 0) {
                        System.out.println("Nenhum pacote cadastrado.");
                    } else {
                        for (Pacote pacote : agencia.getRepositorioPacotes().listar()) {
                            System.out.println(pacote);
                        }
                    }
                    break;
                case 3:
                    int tipoBusca = lerInteiro(sc, "Buscar por (1 - Codigo, 2 - Tipo): ");
                    Pacote encontrado;
                    if (tipoBusca == 1) {
                        encontrado = agencia.buscarPacote(
                                lerInteiro(sc, "Codigo: "));
                    } else {
                        System.out.print("Tipo: ");
                        encontrado = agencia.buscarPacote(sc.nextLine());
                    }
                    System.out.println(encontrado == null
                            ? "Pacote nao encontrado." : encontrado);
                    break;
                case 4:
                    try {
                        int codigo = lerInteiro(sc, "Codigo do pacote: ");
                        agencia.removerPacote(codigo);
                        System.out.println("Pacote removido!");
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }
        } while (opcao != 0);
    }

    private static void menuVendas(Scanner sc, Agencia agencia) {
        int opcao;
        do {
            System.out.println();
            System.out.println("----- VENDAS -----");
            System.out.println("1 - Abrir venda");
            System.out.println("2 - Adicionar servico opcional");
            System.out.println("3 - Finalizar venda");
            System.out.println("4 - Cancelar venda");
            System.out.println("5 - Listar vendas");
            System.out.println("0 - Voltar");
            opcao = lerInteiro(sc, "Opcao: ");
            switch (opcao) {
                case 1:
                    try {
                        int codigoCliente = lerInteiro(sc, "Codigo do cliente: ");
                        int codigoPacote = lerInteiro(sc, "Codigo do pacote: ");
                        int parcelas = lerInteiro(sc, "Numero de parcelas: ");
                        VendaPacote venda = agencia.abrirVenda(
                                codigoCliente, codigoPacote, parcelas);
                        System.out.println("Venda " + venda.getCodigo() + " aberta!");
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        int codigoVenda = lerInteiro(sc, "Codigo da venda: ");
                        VendaPacote venda = agencia.buscarVenda(codigoVenda);
                        System.out.print("Descricao: ");
                        String descricao = sc.nextLine();
                        double valor = lerDouble(sc, "Valor: ");
                        venda.adicionarServico(descricao, valor);
                        System.out.println("Servico adicionado!");
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        int codigoVenda = lerInteiro(sc, "Codigo da venda: ");
                        VendaPacote venda = agencia.buscarVenda(codigoVenda);
                        double subtotal = venda.calcularSubtotal();
                        double total = agencia.finalizarVenda(codigoVenda);
                        System.out.println("--- Venda " + venda.getCodigo()
                                + " | " + venda.getCliente().getNome() + " ---");
                        System.out.println("Pacote: R$ "
                                + String.format("%.2f", venda.getPacote().calcularPreco()));
                        for (ServicoOpcional servico : venda.getServicos()) {
                            System.out.println(servico);
                        }
                        System.out.println("Subtotal: R$ "
                                + String.format("%.2f", subtotal));
                        if (venda.getCliente() instanceof ClienteVip) {
                            ClienteVip vip = (ClienteVip) venda.getCliente();
                            double desconto = subtotal - total;
                            System.out.println("Desconto VIP (" + vip.getPercentualDesconto()
                                    + "%): -R$ " + String.format("%.2f", desconto));
                        }
                        System.out.println("Parcelas: " + venda.getParcelas());
                        System.out.println("TOTAL: R$ "
                                + String.format("%.2f", total));
                        System.out.println("Venda finalizada!");
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        int codigoVenda = lerInteiro(sc, "Codigo da venda: ");
                        agencia.cancelarVenda(codigoVenda);
                        System.out.println("Venda cancelada!");
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 5:
                    if (agencia.getRepositorioVendas().tamanho() == 0) {
                        System.out.println("Nenhuma venda cadastrada.");
                    } else {
                        for (VendaPacote venda : agencia.getRepositorioVendas().listar()) {
                            System.out.println(venda);
                        }
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }
        } while (opcao != 0);
    }

    private static void menuRelatorios(Scanner sc, Agencia agencia) {
        int opcao;
        do {
            System.out.println();
            System.out.println("----- RELATORIOS -----");
            System.out.println("1 - Faturamento total");
            System.out.println("2 - Pacotes disponiveis");
            System.out.println("3 - Vendas por cliente");
            System.out.println("0 - Voltar");
            opcao = lerInteiro(sc, "Opcao: ");
            switch (opcao) {
                case 1:
                    System.out.println("Faturamento total: R$ "
                            + String.format("%.2f", agencia.calcularFaturamento()));
                    break;
                case 2:
                    boolean encontrou = false;
                    for (Pacote pacote : agencia.getRepositorioPacotes().listar()) {
                        if (pacote.isDisponivel()) {
                            System.out.println(pacote);
                            encontrou = true;
                        }
                    }
                    if (!encontrou) {
                        System.out.println("Nenhum pacote disponivel.");
                    }
                    break;
                case 3:
                    Map<String, Integer> contagem = agencia.contarVendasPorCliente();
                    if (contagem.isEmpty()) {
                        System.out.println("Nenhuma venda registrada.");
                    } else {
                        for (Map.Entry<String, Integer> item : contagem.entrySet()) {
                            System.out.println(item.getKey() + ": " + item.getValue()
                                    + " operacao(oes)");
                        }
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }
        } while (opcao != 0);
    }
}
