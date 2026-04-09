package br.com.cotiinformatica.api_clientes.services;

import br.com.cotiinformatica.api_clientes.dtos.ClienteRequest;
import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.entities.Endereco;
import br.com.cotiinformatica.api_clientes.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

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

        //verificar se tem pelo menos um endereco
        if (request.enderecos() == null || request.enderecos().length == 0) {
            throw new IllegalArgumentException("O cliente deve ter pelo menos 1 endereço.");
        }

        //
 //        var clienteRepository = new ClienteRepository();
        if(clienteRepository.cpfExistente(request.cpf())) {
          throw new IllegalArgumentException(("O CPF já está cadastrado."));
        }

        var cliente = new Cliente();
        cliente.setEnderecos(new ArrayList<>()); // instanciando lista de endereços

        cliente.setNome(request.nome());
        cliente.setCpf(request.cpf());

        for (var item : request.enderecos()) {

            var endereco = new Endereco();

            endereco.setLogradouro(item.logradouro());
            endereco.setNumero(item.numero());
            endereco.setComplemento(item.complemento());
            endereco.setBairro(item.bairro());
            endereco.setCidade(item.cidade());
            endereco.setUf(item.uf());
            endereco.setCep(item.cep());

            cliente.getEnderecos().add(endereco);

        }

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
//        var clienteRepository = new ClienteRepository();
        // depois que criou o autowired lá no inicio da classe
        var lista = clienteRepository.listar(nome);

        // retornar a lista de clientes
        return lista;

    }
/*
exclusao de cliente
 */
public void excluirCliente(Integer id) throws Exception {

    var result = clienteRepository.excluir(id);

    if (!result) {        //if not result

        throw new IllegalArgumentException(("Nenhum cliente excluido"));



    }



}


}


