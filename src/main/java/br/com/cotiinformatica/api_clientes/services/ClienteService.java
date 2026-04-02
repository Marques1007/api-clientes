package br.com.cotiinformatica.api_clientes.services;

import br.com.cotiinformatica.api_clientes.dtos.ClienteRequest;
import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.repositories.ClienteRepository;

import java.util.List;

public class ClienteService {

    /*
    metodo para desenvolver as regras de negocios
    para cadastrar os clientes
     */
    public void cadastrarCliente(ClienteRequest request) throws Exception {

        // verificar se o nome está preenchido
        // if (nome != null && nome.trim().length()) { // != diferente &&=e
        if (request.nome() == null || request.nome().trim().length() < 6) { // ||=ou
            throw new IllegalArgumentException(("""
                    O nome do cliente é obrigatório e deve ter pelo menos 6 caracteres.!);"""));
        }

        //
        if (request.cpf() == null) { // todo validar o formato do CPF!
            throw new IllegalArgumentException(("O CPF do cliente é obrigatorio."));
        }

        //
        var clienteRepository = new ClienteRepository();
        if(clienteRepository.cpfExistente(request.cpf())) {
          throw new IllegalArgumentException(("O CPF já está cadastrado."));
        }

        var cliente = new Cliente();
        cliente.setNome(request.nome());
        cliente.setCpf(request.cpf());

        // salvando o cliente no banco de dados
        clienteRepository.inserir(cliente);

    }

    /*
    metodo para consultar a pesquisa de cliente por nome
     */
    public List<Cliente> pesquisarClientes(String nome) throws Exception {

        //verificar se o nome do cliente tem pelo menos 5 caracteres
        if (nome == null || nome.trim().length() < 5) {
            throw new IllegalArgumentException("O nome do cliente deve ter pelo menos 5 caracteres.");
        }

        //consultar os cliente no banco de dados
        var clienteRepository = new ClienteRepository();
        var lista = clienteRepository.listar(nome);

        // retornar a lista de clientes
        return lista;

    }

}


