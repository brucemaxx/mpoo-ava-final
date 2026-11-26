# Agencia de Viagens - Trabalho AV2

## Integrantes

- [Nome completo do Integrante 1]
- [Nome completo do Integrante 2]

Tema: T11 - Agencia de viagens.

## Compilar e executar

```bash
javac -d bin $(find src -name "*.java")
java -cp bin app.Main
```

No PowerShell do Windows, use:

```powershell
Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName } | javac -d bin @args
java -cp bin app.Main
```

## Regras de negocio

1. Um pacote sem vagas nao pode ser vendido.
2. Um pacote internacional exige passaporte cadastrado no cliente.
3. Um cliente comum pode manter no maximo 1 venda aberta; um ClienteVip pode manter ate 5.
4. Clientes comuns podem parcelar em ate 6 vezes; clientes VIP em ate 12 vezes.
5. Nao e permitido adicionar servicos, finalizar ou cancelar uma venda ja encerrada.
6. Nao e permitido remover cliente ou pacote que participa de uma venda aberta.

## Mapa de conceitos

| Conceito | Onde (arquivo / classe / metodo) | Por que foi usado |
|---|---|---|
| Encapsulamento e validacoes | Classes em `modelo/`, setters com `DadoInvalidoException` | Impede estados invalidos nos objetos do dominio. |
| Construtores sobrecarregados / `this(...)` | `Pessoa`, `Cliente`, `ClienteVip`, `Pacote`, `PacoteInternacional`, `VendaPacote` | Reutiliza construtores completos com valores padrao. |
| Sobrecarga | `Agencia.buscarCliente(int)` e `buscarCliente(String)`; `buscarPacote(int)` e `buscarPacote(String)` | Permite buscar por codigo ou nome/tipo. |
| `static` | Contadores em `Pessoa`, `Pacote` e `VendaPacote` | Gera codigos automaticos para os objetos. |
| Constante `public static final` | `ClienteVip.LIMITE_PARCELAS`, `Cruzeiro.PRECO_POR_NOITE`, `Agencia.LIMITE_*` | Centraliza limites e valores fixos. |
| `toString()` com `@Override` | Classes de dominio em `modelo/` | Exibe clientes, pacotes e vendas de forma legivel. |
| Associacao | `VendaPacote.cliente` e `VendaPacote.pacote` | A venda conhece o cliente e o pacote envolvidos. |
| Agregacao | `Agencia` guarda repositorios de clientes e pacotes | Os objetos existem independentemente da Agencia. |
| Composicao | `VendaPacote.adicionarServico()` cria `new ServicoOpcional()` | O servico opcional existe somente dentro da venda. |
| Pessoa abstrata / 3 niveis | `Pessoa -> Cliente -> ClienteVip` | Representa cliente comum e VIP. |
| `super(...)` e atributo protegido | Construtores das hierarquias e `ClienteVip.percentualDesconto` | Reutiliza inicializacao e expõe desconto necessario a subclasse. |
| Metodo concreto sobrescrito com `super` | `Cliente.toString()` chama `super.toString()`; `ClienteVip.toString()` reaproveita o cliente | Evita duplicacao na representacao textual. |
| Classe abstrata de recurso | `Pacote` com `calcularPreco()` abstrato | Define um contrato comum para os tipos de pacote. |
| Polimorfismo | `PacoteNacional`, `PacoteInternacional`, `Cruzeiro` em `List<Pacote>` | Cada subclassa calcula o preco de forma propria. |
| `instanceof` + cast | `VendaPacote.calcularTotal()` e `Agencia.abrirVenda()` | Aplica desconto VIP e regras adicionais no cliente especial. |
| Interfaces | `Cancelavel` e `Cobravel` | Separam cancelamento e calculo de valor. |
| Classe com duas interfaces | `VendaPacote implements Cancelavel, Cobravel` | A venda pode ser cancelada e cobrada. |
| Interface usada como tipo | `List<Cobravel>` em `Agencia.calcularFaturamento()` | Soma vendas finalizadas independente da hierarquia concreta. |
| Excecao checked | `PacoteIndisponivelException`, `OperacaoNaoEncontradaException`, `ClienteNaoEncontradoException` usadas com `throws` | Obriga o chamador a tratar operacoes inexistentes e indisponiveis. |
| Excecao unchecked | `DadoInvalidoException` em setters e regras | Valida dados sem obrigar tratamento em cada chamada. |
| `try/catch/finally` | `lerInteiro`/`lerDouble` usam catch; `Main.main()` usa finally para fechar `Scanner` | Mensagens amigaveis e fechamento garantido do recurso. |
| `List` | Repositorio e `VendaPacote.servicos` | Guarda colecoes polimorficas e itens compostos. |
| `Set` | `Agencia.documentosCadastrados` | Impede documentos repetidos. |
| `Map` | `clientesPorDocumento` e `contagem` do relatorio | Busca direta e contagem de vendas por cliente. |
| Classe generica propria | `Repositorio<T>` usada com `Cliente`, `Pacote` e `VendaPacote` | Centraliza armazenamento de tipos diferentes com seguranca de tipos. |
