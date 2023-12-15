package joao.tamassia.CrudCliente.repository;

import joao.tamassia.CrudCliente.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
