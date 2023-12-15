package joao.tamassia.CrudCliente.controller;

import joao.tamassia.CrudCliente.entities.Cliente;
import joao.tamassia.CrudCliente.exceptions.ClienteNotFoundException;
import joao.tamassia.CrudCliente.repository.ClienteRepository;
import joao.tamassia.CrudCliente.utility.ValidaCPF;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> obterTodosClientes() {
        List<Cliente> clientes = repository.findAll();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obterClientePorId(@PathVariable Integer id) {
        Cliente cliente = repository.findById(id).orElse(null);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Cliente> adicionarCliente(@RequestBody Cliente cliente) {
        if (!ValidaCPF.isCPF(cliente.getCpf())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Cliente> clienteExistente = repository.findByCpf(cliente.getCpf());
        if (clienteExistente.isPresent()) {
            // Cliente com CPF já existe
            return new ResponseEntity<>(HttpStatus.CONFLICT); // ou outro status HTTP apropriado
        }

        Cliente novoCliente = repository.save(cliente);
        return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Integer id, @RequestBody Cliente cliente) {
        Cliente clienteAtualizado = repository.findById(id).map(clienteExistente -> {
            clienteExistente.setNome(cliente.getNome());
            clienteExistente.setCpf(cliente.getCpf());
            clienteExistente.setDataNascimento(cliente.getDataNascimento());
            clienteExistente.setEmail(cliente.getEmail());
            return repository.save(clienteExistente);
        }).orElse(null);
        return new ResponseEntity<>(clienteAtualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerCliente(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ClienteNotFoundException("Cadastro não encontrado para o ID: " + id);
        }
    }





}
