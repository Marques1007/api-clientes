package br.com.cotiinformatica.api_clientes.controllers;

import br.com.cotiinformatica.api_clientes.services.ClienteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController //essa classe é um controlador de API
@RequestMapping ("/api/cliente")
public class ClienteController {
    /*
    HTTP POST / API/CLiente/criar
    Operação ===> para cadastrar um cliente
     */
    @PostMapping("Criar")
    public String criar(@RequestParam String nome, @RequestParam String cpf ) {

        try {

            var clienteService = new ClienteService();
            clienteService.cadastrarCliente(nome, cpf);

            return "CLiente " + nome + ", cadastrado com sucesso!";

        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

}
