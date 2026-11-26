package modelo;

import excecao.DadoInvalidoException;

public abstract class Pessoa {
    private static int proximoCodigo = 1;
    private int codigo;
    private String nome;
    private String documento;

    public Pessoa(String nome, String documento) {
        this.codigo = proximoCodigo++;
        setNome(nome);
        setDocumento(documento);
    }

    public Pessoa() {
        this("Sem nome", "00000000000");
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new DadoInvalidoException("O nome deve ter pelo menos 3 caracteres.");
        }
        this.nome = nome.trim();
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        if (documento == null || documento.trim().length() < 5) {
            throw new DadoInvalidoException("O documento deve ter pelo menos 5 caracteres.");
        }
        this.documento = documento.trim();
    }

    @Override
    public String toString() {
        return "[" + codigo + "] " + nome + " | documento: " + documento;
    }
}
