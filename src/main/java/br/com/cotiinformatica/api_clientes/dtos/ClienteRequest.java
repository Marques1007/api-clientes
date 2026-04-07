package br.com.cotiinformatica.api_clientes.dtos;

import java.util.List;

public record ClienteRequest(

        String nome,
        String cpf,
        EnderecoRequest[] enderecos //array de enderecos

) {
}
