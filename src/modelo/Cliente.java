package modelo;

import excecao.DadoInvalidoException;

public class Cliente extends Pessoa {
    private String passaporte;

    public Cliente(String nome, String documento, String passaporte) {
        super(nome, documento);
        setPassaporte(passaporte);
    }

    public Cliente(String nome, String documento) {
        this(nome, documento, "");
    }

    public String getPassaporte() {
        return passaporte;
    }

    public void setPassaporte(String passaporte) {
        if (passaporte == null) {
            throw new DadoInvalidoException("O passaporte nao pode ser nulo.");
        }
        this.passaporte = passaporte.trim();
    }

    public boolean temPassaporte() {
        return passaporte != null && !passaporte.trim().isEmpty();
    }

    @Override
    public String toString() {
        return super.toString() + " | comum";
    }
}
