package br.com.cotiinformatica.api_clientes.services;

import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.repositories.ClienteRepository;

public class ClienteService {

    /*
    metodo para desenvolver as regras de negocios
    para cadastrar os clientes
     */
    public void cadastrarCliente(String nome, String cpf) throws Exception {

        // verificar se o nome está preenchido
        // if (nome != null && nome.trim().length()) { // != diferente &&=e
        if (nome == null || nome.trim().length() < 6) { // ||=ou
            throw new IllegalArgumentException(("""
                    O nome do cliente é obrigatório e deve ter pelo menos 6 caracteres.!);"""));
        }

        //
        if (cpf == null) { // todo validar o formato do CPF!
            throw new IllegalArgumentException(("O CPF do cliente é obrigatorio."));
        }

        //
        var clienteRepository = new ClienteRepository();
        if(clienteRepository.cpfExistente(cpf)) {
          throw new IllegalArgumentException(("O CPF já está cadastrado."));
        }

        var cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpf(cpf);

        // salvando o cliente no banco de dados
        clienteRepository.inserir(cliente);

    }

}


