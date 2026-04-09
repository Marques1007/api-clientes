 package br.com.cotiinformatica.api_clientes.entities;

        import com.fasterxml.jackson.annotation.JsonIgnore;
        import com.fasterxml.jackson.annotation.JsonPropertyOrder;
        import lombok.Getter;
        import lombok.Setter;

        @Setter
@Getter
        @JsonPropertyOrder({
                "id",
                "logradouro",
                "numero",
                "complemento",
                "bairro",
                "cidade",
                "cep",
                "uf"
        })

        public class Endereco {

    private Integer id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    @JsonIgnore
    private Cliente cliente;
}

