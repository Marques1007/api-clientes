package br.com.cotiinformatica.api_clientes.controllers;

import br.com.cotiinformatica.api_clientes.dtos.ClienteRequest;
import br.com.cotiinformatica.api_clientes.services.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //essa classe é um controlador de API
@RequestMapping ("/api/cliente")
public class ClienteController {
    /*
    HTTP POST / API/CLiente/criar
    Operação ===> para cadastrar um cliente
     */
    @PostMapping("Criar")
    public ResponseEntity<String> criar(@RequestBody ClienteRequest request) {

        try {

            var clienteService = new ClienteService();
            clienteService.cadastrarCliente(request);

            return ResponseEntity.status(201).body ("CLiente " + request.nome() + ", cadastrado com sucesso!");
        }

        catch(IllegalArgumentException e) {
            //http 400 - bad request
            return ResponseEntity.status(400).body (e.getMessage());
        }

        // catch genérico tem que ser o último
        catch (Exception e) {
            //HTTP 500 internal server error
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /*
       HTTP GET /api/cliente/consulttar
       Operação na API para consultar os clientes
    */
    @GetMapping("consultar")
    //?=esse endpoint pode retornar qualquer coisa, no caso mensagem e lista
    public ResponseEntity<?> consultar(@RequestParam String nome) {
        try {
            var clienteService = new ClienteService();
            var lista = clienteService.pesquisarClientes(nome);

            //HTTP 200 - OK
            return ResponseEntity.status(200).body(lista);
        }
        catch (IllegalArgumentException e) {
            //HTTP 400 - BAD REQUEST
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch(Exception e){
            //HTTP 500 - INTERNAL SERVER ERROR
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

