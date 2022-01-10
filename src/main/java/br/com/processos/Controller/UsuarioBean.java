package br.com.processos.Controller;

import br.com.processos.Controller.FabricaConexao;
import br.com.processos.Modelo.Usuario;
import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name = "usuarioBean")
public class UsuarioBean extends Usuario {

    private static String user;
    private String senha1;
    private String login1;
    private String palavra_passe1;
    
    
    public String Sessao(){
      
    if(this.getUser().isEmpty()){
    
    return "login.xhtml";
        
    }else{
    
    return "/cadastrar/abrir_processo.xhtml";
    }
    
    }

    public void cadastrar(String nome, String login, String senha, String tipo, String palavra) {

        System.out.println("Tipo: " + tipo);

        PreparedStatement stmt = null;
        Connection con = FabricaConexao.getConnection();
        String query = "INSERT INTO USUARIO (NOME,LOGIN,SENHA,TIPO,PALAVRA) VALUES(?,?,?,?,?)";

        try {

            stmt = con.prepareStatement(query);

            stmt.setString(1, nome);
            stmt.setString(2, login);
            stmt.setString(3, senha);
            stmt.setString(4, tipo);
            stmt.setString(5, palavra);
            stmt.execute();

            System.out.println("Usuário Cadastrado!");

        } catch (SQLException ex) {
            try {
                System.err.println("Usuário não Cadastrado!");
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
                con.rollback();
            } catch (Exception e) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);

            }

        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);

        }

    }

    public static void deletar(String nome) {

        PreparedStatement stmt = null;
        Connection con = FabricaConexao.getConnection();
        String query = "DELETE FROM USUARIO WHERE NOME LIKE'" + nome + "'";

        try {

            stmt = con.prepareStatement(query);
            stmt.executeUpdate();

            System.out.println("Usuário Deletado!");

        } catch (SQLException ex) {

            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);

        }

    }

    public String alterar() {

        Connection con = FabricaConexao.getConnection();
        String query = "UPDATE USUARIO SET SENHA = ? WHERE LOGIN = ?";
        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement(query);
            stmt.setString(1, this.getSenha());
            stmt.setString(2, this.getLogin());
            stmt.executeUpdate();

            return "/login.xhtml";

        } catch (SQLException e) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, e);
            try {
                con.rollback();

            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, e);
            }
            return "";
        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt((PreparedStatement) stmt);
        }

    }

    public String autenticacao() {

        Connection con = FabricaConexao.getConnection();
        String query = "SELECT LOGIN , SENHA FROM USUARIO WHERE LOGIN = ? AND SENHA = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement(query);
            stmt.setString(1, getLogin());
            stmt.setString(2, getSenha());
            stmt.executeQuery();
            rs = stmt.executeQuery();

            while (rs.next()) {

                setUser(rs.getString("login"));
                setSenha1(rs.getString("senha"));

            }

            if (this.getUser().equals(this.getLogin()) && this.getSenha1().equals(this.getSenha())) {

                return "home/home.xhtml";

            } else {
                System.out.println("Usuário ou Senha inválidos!");
                return null;
            }

        } catch (Exception e) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);
            FabricaConexao.fecharRs(rs);

        }

    }

    public String esquecer_senha() {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = FabricaConexao.getConnection();
        String query = "SELECT LOGIN, PALAVRA_PASSE FROM USUARIO WHERE LOGIN = ? AND PALAVRA_PASSE = ?";

        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, getLogin());
            stmt.setString(2, getPalavra_passe());
            stmt.executeQuery();
            rs = stmt.executeQuery();

            while (rs.next()) {
                this.setLogin1(rs.getString("LOGIN"));
                this.setPalavra_passe1(rs.getString("PALAVRA_PASSE"));

            }

            if (this.getLogin().equals(this.getLogin1()) && this.getPalavra_passe().equals(this.getPalavra_passe1())) {
                System.err.println("Usuário ou palavra encontrado!");
                return "alterar_senha.xhtml";
            } else {
                System.err.println("Usuário ou palavra não encontrado!");
                return null;
            }

        } catch (SQLException e) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);
            FabricaConexao.fecharRs(rs);

        }

    }
    
    public static Integer recuperarUID(String nome){
    
       Connection con = FabricaConexao.getConnection();
        String query = "SELECT ID_USUARIO FROM USUARIO WHERE LOGIN = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        UsuarioBean usuarioBean;
        ProtocoloBean prot;

        try {

            usuarioBean = new UsuarioBean();
           
            stmt = con.prepareStatement(query);
            if(nome==null){
            stmt.setString(1, usuarioBean.getUser());
            }else{
            stmt.setString(1, nome);
            }
            
            stmt.executeQuery();
            rs = stmt.executeQuery();

            while (rs.next()) {
                
                usuarioBean.setId_usuario(rs.getInt("ID_USUARIO"));
            }

            return usuarioBean.getId_usuario();
            
        } catch (Exception e) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, e);
 
            return null;
            
        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);
            FabricaConexao.fecharRs(rs);

        }
        
       
    
    }
    
    public Integer recuperarNome(String nome){
    
        Connection con = FabricaConexao.getConnection();
        String query = "SELECT ID_USUARIO FROM USUARIO WHERE LOGIN = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        UsuarioBean usuarioBean;
        

        try {

            usuarioBean = new UsuarioBean();
           usuarioBean.setLogin(nome);
            stmt = con.prepareStatement(query);
            stmt.setString(1, usuarioBean.getLogin());
            stmt.executeQuery();
            rs = stmt.executeQuery();

            while (rs.next()) {
                
                usuarioBean.setId_usuario(rs.getInt("ID_USUARIO"));
            }

            return usuarioBean.getId_usuario();
            
        } catch (Exception e) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, e);
 
            return null;
            
        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);
            FabricaConexao.fecharRs(rs);

        }
     
    
    }
    
    public String atalho(){
    
        return "/home/home.xhtml";
    
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSenha1() {
        return senha1;
    }

    public void setSenha1(String senha1) {
        this.senha1 = senha1;
    }

    public String getLogin1() {
        return login1;
    }

    public void setLogin1(String login1) {
        this.login1 = login1;
    }

    public String getPalavra_passe1() {
        return palavra_passe1;
    }

    public void setPalavra_passe1(String palavra_passe1) {
        this.palavra_passe1 = palavra_passe1;
    }

//    public static void main(String arg[]) {
//        
//        String conectou = null, senha = "TESTE", login = "TESTE1";
//        
//        
//        conectou = autenticacao(login, senha);
//        
//        if(conectou==null){
//            
//            System.err.println("Não conectou: "+conectou);
//            
//        }else{
//        
//            System.out.println("Conectou: "+conectou);
//        }
//        
//    }
    /**
     * @return the ulogin
     */
}
