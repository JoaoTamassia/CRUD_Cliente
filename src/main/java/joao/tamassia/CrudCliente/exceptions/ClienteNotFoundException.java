package joao.tamassia.CrudCliente.exceptions;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(String mensagem) {
        super(mensagem);
    }
}