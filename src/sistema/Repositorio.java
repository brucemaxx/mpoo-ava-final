package sistema;

import java.util.ArrayList;
import java.util.List;

public class Repositorio<T> {
    private List<T> itens;

    public Repositorio() {
        itens = new ArrayList<T>();
    }

    public void adicionar(T item) {
        itens.add(item);
    }

    public boolean remover(T item) {
        return itens.remove(item);
    }

    public List<T> listar() {
        return itens;
    }

    public int tamanho() {
        return itens.size();
    }
}
