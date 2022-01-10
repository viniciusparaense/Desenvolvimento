package br.com.processos.Controller;

import br.com.processos.Modelo.Solicitante;
import br.com.processos.Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "soBean")
public class SolicitanteBean extends Solicitante {

    private Date date;
    private Integer tipo;

    public String cadastrarSolicitante() {

        FacesContext context = FacesContext.getCurrentInstance();

        PreparedStatement stmt = null;
        Connection con = FabricaConexao.getConnection();
        String query = "INSERT INTO SOLICITANTE (NOME,EMAIL,TELEFONE,ENDERECO) VALUES(?,?,?,?)";

        try {

            stmt = con.prepareStatement(query);
            stmt.setString(1, this.getNome());
            stmt.setString(2, this.getEmail());
            stmt.setString(3, this.getTelefone());
            stmt.setString(4, this.getEndereco());
            stmt.executeUpdate();

            context.addMessage(null, new FacesMessage("Mensagem:",
                    "Solicitante Cadastrado! " + this.getNome()));

            return "/cadastrar/abrir_processo.xhtml";
            
            
        } catch (SQLException ex) {

            Logger.getLogger(ProtocoloBean.class
                    .getName()).log(Level.SEVERE, null, ex);

            context.addMessage(null, new FacesMessage("Atenção:",
                    "Solicitante não cadastrado! " + this.getNome()));
            
            return "";

        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);

        }

    }

    public Integer recuperarSID(String telefone) {

        Connection con = FabricaConexao.getConnection();
        String query = "SELECT ID_SOLICITANTE FROM SOLICITANTE WHERE TELEFONE = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement(query);
            stmt.setString(1, telefone);
            stmt.executeQuery();
            rs = stmt.executeQuery();

            while (rs.next()) {

                this.setId_solicitante(rs.getInt("ID_SOLICITANTE"));
            }

            return this.getId_solicitante();

        } catch (Exception e) {
            Logger.getLogger(Usuario.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);
            FabricaConexao.fecharRs(rs);

        }

    }

    public String retornarEmail(String destinatario) {

        Connection con = FabricaConexao.getConnection();
        String query = "SELECT EMAIL FROM SOLICITANTE WHERE NOME = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement(query);
            stmt.setString(1, destinatario);
            stmt.executeQuery();
            rs = stmt.executeQuery();

            while (rs.next()) {

                this.setEmail(rs.getString("EMAIL"));

            }

            if (this.getEmail() == null) {

                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Solicitante nao encontrado: ", this.getNome()));

                return "";
            } else {

                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage(null, new FacesMessage(FacesMessage.FACES_MESSAGES, "Email Enviado para: " + destinatario));
                return this.getEmail();
            }

        } catch (Exception e) {

            Logger.getLogger(Usuario.class
                    .getName()).log(Level.SEVERE, null, e);
            return "";

        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);
            FabricaConexao.fecharRs(rs);

        }

    }

    public void retornarSolicitante() {

        FacesContext context = FacesContext.getCurrentInstance();

        Connection con = FabricaConexao.getConnection();
        String query = "SELECT ID_SOLICITANTE,NOME ,EMAIL ,TELEFONE ,ENDERECO "
                + "FROM SOLICITANTE WHERE NOME = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement(query);
            stmt.setString(1, this.getParametro());
            stmt.executeQuery();
            rs = stmt.executeQuery();

            while (rs.next()) {

                this.setId_solicitante(rs.getInt("ID_SOLICITANTE"));
                this.setNome(rs.getString("NOME"));
                this.setEmail(rs.getString("EMAIL"));
                this.setTelefone(rs.getString("TELEFONE"));
                this.setEndereco(rs.getString("ENDERECO"));

            }

            if (this.getId_solicitante() == null) {

                System.out.println("Entrou no IF");
                context.addMessage(null, new FacesMessage("Mensagem:",
                        "Solicitante não encontrado: " + this.getParametro()));
                this.setParametro("");
                this.setNome("");
                this.setEndereco("");
                this.setTelefone("");
                this.setEmail("");
                this.setNome("");

            } else {

                context.addMessage(null, new FacesMessage("Mensagem:",
                        "Solicitante encontrado: " + this.getParametro()));

                ProtocoloBean protobean = new ProtocoloBean();

                protobean.cProcesso(this.getTelefone(), this.getTipo(), this.getDate(), this.getId_solicitante());

            }

        } catch (SQLException e) {

//            context.addMessage(null, new FacesMessage("Mensagem:",
//                    "Solicitante não encontrado!" + this.getNome()));
            this.setId_solicitante(0);
            this.setNome("");
            this.setEndereco("");
            this.setTelefone("");
            this.setEmail("");
            this.setNome("");
        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);
            FabricaConexao.fecharRs(rs);

        }
//
    }

    public void onDateSelect(SelectEvent event) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selecionada", format.format(event.getObject())));
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
}
