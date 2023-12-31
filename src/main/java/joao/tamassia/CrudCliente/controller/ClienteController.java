package joao.tamassia.CrudCliente.controller;

import jakarta.validation.Valid;
import joao.tamassia.CrudCliente.entities.Cliente;
import joao.tamassia.CrudCliente.exceptions.ClienteNotFoundException;
import joao.tamassia.CrudCliente.repository.ClienteRepository;
import joao.tamassia.CrudCliente.response.ErrorResponse;
import joao.tamassia.CrudCliente.utility.EmailValidator;
import joao.tamassia.CrudCliente.utility.IdadeValidaValidator;
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
        if (repository.count() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
        List<Cliente> clientes = repository.findAll();
        return new ResponseEntity<>(clientes, HttpStatus.OK);}
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obterClientePorId(@PathVariable Integer id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado para o ID: " + id));
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> adicionarCliente(@RequestBody @Valid Cliente cliente) {
        if (!ValidaCPF.isCPF(cliente.getCpf())) {
            ErrorResponse errorResponse = new ErrorResponse("CPF informado inválido");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (!IdadeValidaValidator.isValid(cliente.getDataNascimento())) {
            ErrorResponse errorResponse = new ErrorResponse("Idade inválida");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if(!EmailValidator.isValid(cliente.getEmail())){
            ErrorResponse errorResponse = new ErrorResponse("Email inválido");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
        }).orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado para o ID: " + id));
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
