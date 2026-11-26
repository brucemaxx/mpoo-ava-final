package excecao;

public class OperacaoNaoEncontradaException extends Exception {
    public OperacaoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
