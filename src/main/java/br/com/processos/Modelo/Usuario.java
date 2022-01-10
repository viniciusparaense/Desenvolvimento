package br.com.processos.Modelo;

public abstract class Usuario{

    private String login;
    private String Senha;
    private String nome;
    private String palavra_passe;
    private Integer id_usuario;

       public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String Senha) {
        this.Senha = Senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPalavra_passe() {
        return palavra_passe;
    }

      public void setPalavra_passe(String palavra_passe) {
        this.palavra_passe = palavra_passe;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

   
}
