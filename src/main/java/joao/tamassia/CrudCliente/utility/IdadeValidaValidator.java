package joao.tamassia.CrudCliente.utility;

import java.time.LocalDate;
import java.time.Period;

public class IdadeValidaValidator {

    private static final int IDADE_MAXIMA = 150;

    public static boolean isValid(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            return false;
        }

        LocalDate hoje = LocalDate.now();

        Period periodo = Period.between(dataNascimento, hoje);
        int idade = periodo.getYears();

        return idade <= IDADE_MAXIMA && idade > 0;
    }
}
