package br.com.cotiinformatica.api_clientes.factories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class ConnectionFactory {

    @Value("${datasource.host}")
    private String host;

    @Value("${datasource.user}")
    private String user;

    @Value("${datasource.pass}")
    private String pass;

    /*
        Método para retornar a conexão com o banco de dados
     */
//    public static Connection getConnection() throws Exception { estava como static antes
    // de criar as variaveis host,user,pass
    public Connection getConnection() throws Exception {

// eliminou essas informações depois que criou as variaveis
//        var host = "jdbc:postgresql://localhost:5432/bd-api-clientes";
//        var user = "postgres";
//        var pass = "coti";

        return DriverManager.getConnection(host, user, pass);
    }
}

