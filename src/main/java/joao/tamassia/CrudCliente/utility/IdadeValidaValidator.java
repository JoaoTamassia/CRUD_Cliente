package joao.tamassia.CrudCliente.utility;

import java.time.LocalDate;
import java.time.Period;

public class IdadeValidaValidator {

    private static final int IDADE_MAXIMA = 150;

    public static boolean isValid(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            return false; // ou ajuste conforme necessário
        }

        LocalDate hoje = LocalDate.now();

        // Calcula a idade do cliente
        Period periodo = Period.between(dataNascimento, hoje);
        int idade = periodo.getYears();

        // Verifica se a idade é menor que 150 e maior que 0
        return idade <= IDADE_MAXIMA && idade > 0;
    }
}
