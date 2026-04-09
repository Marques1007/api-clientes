package br.com.cotiinformatica.api_clientes.repositories;

import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.entities.Endereco;
import br.com.cotiinformatica.api_clientes.factories.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class ClienteRepository {

    @Autowired
    private ConnectionFactory connectionFactory;

    /*
    metodo para verificar se um CPF ja esta cadastrado no banco de clientes
     */
    public boolean cpfExistente(String cpf) throws Exception {

        try (var connection = connectionFactory.getConnection()) {
            var statement = connection.prepareStatement("""
                    SELECT COUNT(*) AS QTD FROM CLIENTES WHERE CPF = ?""");
            statement.setString(1, cpf);
            var result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("QTD") == 1;
            }

            return false;


        }



    }

    /*
    metodo para retornar uma lista de banco de dados através do nome informado
     */
    public List<Cliente> listar(String nome) throws Exception {

        try (var connection = connectionFactory.getConnection()) {

            var sql = """
SELECT
	c.ID AS IDCLIENTE,
	c.NOME,
	c.CPF,
	e.ID AS IDENDERECO,
	e.LOGRADOURO,
	e.NUMERO,
	e.COMPLEMENTO,
	e.BAIRRO,
	e.CIDADE,
	e.UF,
	e.CEP 
FROM CLIENTES c 
LEFT JOIN ENDERECOS e 
ON c.ID = e.CLIENTE_ID  
WHERE c.NOME ILIKE ? AND STATUS = 1
ORDER BY c.NOME                    
                    """;
            var statement = connection.prepareStatement (sql);
            //COMO ESTAVA
//            var statement = connection.prepareStatement
//                    (("SELECT * FROM CLIENTES c INNER JOIN ENDERECOS e ON c.ID = e.CLIENTE_ID WHERE NOME ILIKE ? ORDER BY NOME"));

            statement.setString(1, "%" + nome + "%");
            var result = statement.executeQuery();

            var lista = new ArrayList<Cliente>();
            var map = new HashMap<Integer, Cliente>();  //ajudar a capturar cada cliente individualmente

            while (result.next()) { //percorrendo cada registro

                //capturando o id do cliente no banco de dados
                var clienteId = result.getInt("IDCLIENTE");

                Cliente cliente; //objeto cliente sem iniciaalizar com nada

                if (map.containsKey(clienteId)) { //VERIFICA SE O CLIENTE JA FOI LIDO
                    cliente = map.get(clienteId);
                }
                else {
                    cliente = new Cliente();

                    cliente.setId(result.getInt("IDCLIENTE"));
                    cliente.setNome(result.getString("NOME"));
                    cliente.setCpf(result.getString("CPF"));
                    cliente.setEnderecos(new ArrayList<>());

                    map.put(clienteId, cliente);


                    lista.add(cliente);
                }

                //se houver enderecos vamos adiocionar na lista
                var enderecoId = result.getObject("IDENDERECO");
                if (enderecoId != null) { //verifica se tem endereco

                    var endereco = new Endereco();
                    endereco.setId(result.getInt("IDENDERECO"));
                    endereco.setLogradouro(result.getString("LOGRADOURO"));
                    endereco.setNumero(result.getString("NUMERO"));
                    endereco.setComplemento(result.getString("COMPLEMENTO"));
                    endereco.setBairro(result.getString("BAIRRO"));
                    endereco.setCidade(result.getString("CIDADE"));
                    endereco.setUf(result.getString("UF"));
                    endereco.setCep(result.getString("CEP"));

                    cliente.getEnderecos().add(endereco);

                }

                //paginar essa consulta

// retirou para tratar endereco
//                var cliente = new Cliente(); //criando um objeto cliente
//                cliente.setId(result.getInt("c.id"));
//                cliente.setNome(result.getString("c.nome"));
//                cliente.setCpf(result.getString("c.cpf"));
//                lista.add(cliente);

            }
            return lista;
        }
    }

    /*
    metodo para inserir um cliente no banco de dados
     */
    public void inserir (Cliente cliente) throws Exception {

        try (var connection = connectionFactory.getConnection()) {

            //setar para não fazer o autocommit
            connection.setAutoCommit(false);

            //inserindo o cliente e capturando o ID auto incremento
            var statement = connection.prepareStatement("""
                    INSERT INTO CLIENTES(NOME, CPF) 
                    VALUES (?,?)""", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getCpf());
            statement.execute();

//capturando o ID do cliente gerado no insert do banco de dados
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                cliente.setId(generatedKeys.getInt(1));
            }
            //verificar se o cliente tem endereco
            if (cliente.getEnderecos() != null) {
                //percorrer cada endereco
                for(var endereco : cliente.getEnderecos()) {

                    //nao colocou o var aqui porque já está definido acima
                    statement = connection.prepareStatement("""
                    INSERT INTO ENDERECOS(LOGRADOURO,
                                          NUMERO,
                                          COMPLEMENTO,
                                          BAIRRO,
                                          CIDADE,
                                          UF,
                                          CEP,
                                          CLIENTE_ID)
                    VALUES (?,?,?,?,?,?,?,?)
                              """);
                    statement.setString(1, endereco.getLogradouro());
                    statement.setString(2, endereco.getNumero());
                    statement.setString(3, endereco.getComplemento());
                    statement.setString(4, endereco.getBairro());
                    statement.setString(5, endereco.getCidade());
                    statement.setString(6, endereco.getUf());
                    statement.setString(7, endereco.getCep());
                    statement.setInt(8, cliente.getId());
                    statement.execute();
                }
            }
            connection.commit(); //confirmando a transacao
        }
    }
    /*
    METODO PARA EXCLUIR logicamente o cliente
     */
    public boolean excluir(Integer id) throws Exception {

        try (var connection = connectionFactory.getConnection()) {


            var sql = """
                 UPDATE CLIENTES
                    SET 
                    STATUS = 0,
                    DATAHORAEXCLUSAO = CURRENT_TIMESTAMP
                    WHERE ID = ?
                    AND STATUS = 1
                    """;

            var statement = connection.prepareStatement (sql);
            statement.setInt(1, id);
            var result = statement.executeUpdate();

            return result > 0;

        }


    }
}
