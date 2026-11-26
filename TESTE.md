# Passo a passo para testar Boas Viagens

Este roteiro cobre os 13 passos da atividade e os principais pontos de OO.

## 1. Preparar e executar

Abra o PowerShell na pasta do projeto:

```powershell
cd "C:\Users\bruce\.copilot\chats\2026-11-25\fuzzy-fortnight-28d90554"
```

Compile:

```powershell
New-Item -ItemType Directory -Force bin | Out-Null
$arquivos = Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName }
javac -d bin $arquivos
```

Execute:

```powershell
java -cp bin app.Main
```

Se aparecer `===== AGENCIA DE VIAGENS =====`, o sistema iniciou corretamente.

## 2. Confirmar os dados iniciais

No menu principal:

1. Digite `2`.
2. Digite `2`.

Devem aparecer três tipos de pacote:

- `PacoteNacional`
- `PacoteInternacional`
- `Cruzeiro`

Ao voltar, o sistema já deve ter três clientes e uma venda inicial. Os dados podem ser conferidos em:

1. Menu principal: `1`
2. Clientes: `2`

Depois:

1. Digite `0` no submenu.
2. Digite `0` no menu principal.

---

## 3. Testar cliente com dado inválido

No menu principal:

1. Digite `1`.
2. Digite `1`.
3. Escolha `1` para cliente comum.
4. Digite um nome vazio ou com menos de três caracteres.
5. Digite um documento válido.

Resultado esperado:

```text
Erro: O nome deve ter pelo menos 3 caracteres.
```

O cliente não deve ser cadastrado.

## 4. Testar documento repetido

No submenu Clientes:

1. Digite `1`.
2. Escolha `1`.
3. Informe um novo nome.
4. Informe `12345678900`, usado pelo cliente inicial Ana Souza.

Resultado esperado:

```text
Erro: Documento ja cadastrado.
```

## 5. Cadastrar cliente VIP

No submenu Clientes:

1. Digite `1`.
2. Escolha `2` para VIP.
3. Informe um nome novo.
4. Informe um documento novo.
5. Informe um passaporte novo.

Resultado esperado:

```text
Cliente cadastrado!
Codigo: 4
```

Anote o código mostrado. Se o programa já tiver sido executado repetidamente em outra sessão, o código não será necessariamente `4`, mas o sistema atribui o próximo código automaticamente.

## 6. Cadastrar pacote novo

No menu principal:

1. Digite `2`.
2. Digite `1`.
3. Escolha `1` para pacote nacional.
4. Informe uma vaga positiva, por exemplo `10`.
5. Informe um preço positivo, por exemplo `3000`.

Resultado esperado:

```text
Pacote cadastrado!
Codigo: 4
```

Repita o cadastro para testar os três tipos:

- Nacional: vagas e preço.
- Internacional: vagas, preço e taxa de embarque.
- Cruzeiro: vagas e quantidade de noites.

## 7. Buscar por código e por nome/tipo

### Buscar cliente

No submenu Clientes:

1. Digite `3`.
2. Digite `1`.
3. Informe o código de um cliente.

Depois:

1. Digite `3`.
2. Digite `2`.
3. Informe o nome exato de um cliente.

Teste também um código ou nome inexistente e confirme a mensagem amigável.

### Buscar pacote

No submenu Pacotes:

1. Digite `3`.
2. Digite `1`.
3. Informe o código de um pacote.

Depois:

1. Digite `3`.
2. Digite `2`.
3. Informe `PacoteNacional`, `PacoteInternacional` ou `Cruzeiro`.

## 8. Testar pacote sem vagas

Primeiro cadastre um pacote nacional com `0` vagas.

Depois abra uma venda:

1. Menu principal: `3`
2. Venda: `1`
3. Informe o código de um cliente.
4. Informe o código do pacote sem vagas.
5. Informe o número de parcelas.

Resultado esperado:

```text
Erro: Pacote X sem vagas.
```

## 9. Testar venda válida

No submenu Vendas:

1. Digite `1`.
2. Informe o código do novo cliente VIP.
3. Informe o código de um pacote nacional disponível.
4. Informe um número válido de parcelas, por exemplo `12`.

Resultado esperado:

```text
Venda X aberta!
```

## 10. Adicionar dois serviços opcionais

Na venda recém-aberta:

1. Digite `2`.
2. Informe o código da venda.
3. Informe `Seguro viagem`.
4. Informe `180`.

Repita:

1. Digite `2`.
2. Informe a venda.
3. Informe `Passeio`.
4. Informe `250`.

Resultado esperado em ambos:

```text
Servico adicionado!
```

## 11. Finalizar venda

No submenu Vendas:

1. Digite `3`.
2. Informe o código da venda aberta.

Confirme:

- O subtotal.
- O desconto de 8% para o cliente VIP.
- O valor final.
- A situação `FINALIZADA`.

Exemplo de cálculo:

```text
Pacote nacional de R$ 3000,00
+ Seguro viagem de R$ 180,00
+ Passeio de R$ 250,00
Subtotal = R$ 3430,00
Desconto VIP de 8% = R$ 274,40
TOTAL = R$ 3155,60
```

Para o seu caso, o pacote e os serviços podem ter outros valores.

## 12. Tentar cancelar venda finalizada

No submenu Vendas:

1. Digite `4`.
2. Informe o código da venda finalizada.

Resultado esperado:

```text
Erro: A venda X ja foi encerrada.
```

## 13. Tentar remover pacote em uso

Abra outra venda válida e não a finalize.

Depois:

1. Menu principal: `2`
2. Pacotes: `4`
3. Informe o código desse pacote.

Resultado esperado:

```text
Erro: Pacote possui venda aberta e nao pode ser removido.
```

## 14. Testar remoção de cliente em uso

Com a venda aberta do passo anterior:

1. Menu principal: `1`
2. Clientes: `4`
3. Informe o código desse cliente.

Resultado esperado:

```text
Erro: Cliente possui venda aberta e nao pode ser removido.
```

## 15. Testar venda com pacote internacional

Cadastre ou selecione um `PacoteInternacional`.

Tente abrir a venda para um cliente comum sem passaporte.

Resultado esperado:

```text
Erro: Pacote internacional exige passaporte cadastrado.
```

Depois use o cliente VIP, que possui passaporte, e confirme que a venda abre.

## 16. Testar limite de vendas abertas

Cadastre um cliente comum.

Abra uma venda para esse cliente e não finalize.

Tente abrir outra venda para o mesmo cliente.

Resultado esperado:

```text
Erro: O cliente atingiu o limite de vendas abertas.
```

Um cliente VIP pode ter até cinco vendas abertas.

## 17. Mostrar os três relatórios

No menu principal:

1. Digite `4`.

### Faturamento total

1. Digite `1`.

Resultado esperado: soma apenas das vendas com situação `FINALIZADA`.

### Pacotes disponíveis

1. Digite `2`.

Resultado esperado: pacotes com vagas disponíveis.

### Vendas por cliente

1. Digite `3`.

Resultado esperado: uma linha com o nome e a quantidade de vendas de cada cliente.

## 18. Testar entrada inválida

Quando o programa pedir um número:

1. Digite `abc`.

Resultado esperado:

```text
Digite um numero valido.
```

2. No menu principal, digite `9`.

Resultado esperado:

```text
Opcao invalida!
```

O menu deve continuar sendo exibido.

## 19. Testar a saída

No menu principal:

1. Digite `0`.

Resultado esperado:

```text
Saindo...
```

O programa deve terminar sem erro.

## 20. Checklist rápido

- [ ] Compila sem erros.
- [ ] Dados iniciais carregados.
- [ ] Os três tipos de pacote aparecem.
- [ ] Nome ou documento inválido é recusado.
- [ ] Documento repetido é recusado.
- [ ] Cliente VIP recebe 8% de desconto.
- [ ] Pacote sem vagas é recusado.
- [ ] Pacote internacional exige passaporte.
- [ ] Serviços são criados dentro da venda.
- [ ] Venda é finalizada e o pacote libera a vaga.
- [ ] Venda finalizada não pode ser cancelada.
- [ ] Cliente ou pacote em venda aberta não pode ser removido.
- [ ] Os três relatórios funcionam.
- [ ] Letra no lugar de número não encerra o programa.
- [ ] Opção inexistente não encerra o programa.
- [ ] Programa sai com a opção `0`.
