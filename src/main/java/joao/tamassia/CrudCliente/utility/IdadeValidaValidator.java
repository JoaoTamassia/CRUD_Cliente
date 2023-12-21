package joao.tamassia.CrudCliente.utility;

import java.time.LocalDate;

public class IdadeValidaValidator{

    public static boolean isValid(LocalDate dataNascimento) {
        return dataNascimento.isBefore(LocalDate.now()) && dataNascimento.isAfter(LocalDate.of(1900, 1, 1));
    }

}