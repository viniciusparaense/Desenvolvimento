package br.com.processos.Controller;


import static br.com.processos.Controller.UsuarioBean.recuperarUID;
import br.com.processos.Modelo.Protocolo;
import br.com.processos.Modelo.Solicitante;
import br.com.processos.Modelo.Tipo_protocolo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

@ManagedBean
public class ProtocoloBean extends Protocolo implements validação {

    private String solcitante;
    private String tipo;
    private String usuario;
    private String email;
    private String telefone;
    private String endereco;
    private String status_novo;
    private Solicitante so;
    private UsuarioBean ub;
    private Tipo_protocolo tp;
    private SolicitanteBean sob;

    private ArrayList<ProtocoloBean> protocolo = new ArrayList<ProtocoloBean>();
    Protocolo proto = new Protocolo();

    
     public void cProcesso(String telefone, int tipo, Date data, int id_solicitante) {
         
         System.out.println("Entrou no método cProcesso");
         
         String dataf = new SimpleDateFormat("dd/MM/yyyy").format(data);
             
        System.out.println("telefone :"+telefone);
        System.out.println("tipo :"+tipo);
        System.out.println("data :"+dataf);
        
        SolicitanteBean solbean = new SolicitanteBean();

        if(id_solicitante == 0){
          
            this.setId_solicitante(solbean.recuperarSID(this.getTelefone()));
            
        }else{
        this.setId_solicitante(id_solicitante);
        }
          
//        this.setId_tipo(recuperarTID());        
        this.setId_usuario(recuperarUID(null));

        PreparedStatement stmt = null;
        Connection con = FabricaConexao.getConnection();
        String query = "INSERT INTO PROTOCOLO (DATA,TIPO_ID,SOLICITANTE_ID,USUARIO_ID,STATUS) VALUES(?,?,?,?,?)";
        try {

            stmt = con.prepareStatement(query);

            stmt.setString(1, dataf);
            stmt.setInt(2, tipo);
            stmt.setInt(3, this.getId_solicitante());
            stmt.setInt(4, this.getId_usuario());
            this.setStatus("Aguardando Encaminhamento");
            stmt.setString(5, this.getStatus());
            stmt.execute();

            msgSucesso();

            System.out.println(this.getData());
            System.out.println("Cadastrou!");

        } catch (SQLException ex) {

            System.err.println("Não cadastrou");
            Logger.getLogger(ProtocoloBean.class.getName()).log(Level.SEVERE, null, ex);
            msgErro(ex);

        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);

        }

    }
    
    public List<ProtocoloBean> gerarLista() {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = FabricaConexao.getConnection();
        String query = "SELECT P.ID_PROTOCOLO, P.DATA, T.DESCRICAO, S.NOME, U.NOME, P.STATUS\n"
                + "FROM PROTOCOLO P, TIPO_PROTOCOLO T, SOLICITANTE S, USUARIO U\n"
                + "WHERE P.TIPO_ID=T.ID_TIPO AND P.SOLICITANTE_ID=S.ID_SOLICITANTE AND P.USUARIO_ID=U.ID_USUARIO ORDER BY P.ID_PROTOCOLO";
        ProtocoloBean prot;

        try {
            stmt = con.prepareStatement(query);
            stmt.executeQuery();
            rs = stmt.executeQuery();

            while (rs.next()) {

                prot = new ProtocoloBean();

                prot.setId_protocolo(rs.getInt("P.ID_PROTOCOLO"));
                prot.setData(rs.getString("P.DATA"));
                prot.setTipo(rs.getString("T.DESCRICAO"));
                prot.setSolcitante(rs.getString("S.NOME"));
                prot.setUsuario(rs.getString("U.NOME"));
                prot.setStatus(rs.getString("P.STATUS"));

                if (prot.getStatus().equals("A espera do solicitante")) {

                    FacesContext context = FacesContext.getCurrentInstance();

                    context.addMessage(null, new FacesMessage("Atenção!", "Comunique o solicitante para a retirada do protocolo:"
                            +prot.getId_protocolo()));

                }

                this.getProtocolo().add(prot);

            }

            return this.getProtocolo();

        } catch (SQLException e) {
            Logger.getLogger(ProtocoloBean.class.getName()).log(Level.SEVERE, null, e);
            return null;

        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);
            FabricaConexao.fecharRs(rs);

        }

    }

    public String consultaProtocolo() {

        Connection con = FabricaConexao.getConnection();
        String query = "SELECT P.ID_PROTOCOLO, P.DATA, T.DESCRICAO, S.NOME, U.NOME, P.STATUS\n"
                + "FROM PROTOCOLO P, TIPO_PROTOCOLO T, SOLICITANTE S, USUARIO U\n"
                + "WHERE P.TIPO_ID=T.ID_TIPO AND P.SOLICITANTE_ID=S.ID_SOLICITANTE AND P.USUARIO_ID=U.ID_USUARIO"
                + " AND P.ID_PROTOCOLO = ? ORDER BY P.ID_PROTOCOLO";
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement(query);
            // stmt.setString(1, this.login);
            //stmt.setString(2, this.senha);
            stmt.setInt(1, proto.getId_protocolo());
            stmt.executeQuery();
            rs = stmt.executeQuery();

            while (rs.next()) {

                proto.setId_protocolo(rs.getInt("P.ID_PROTOCOLO"));
                proto.setData(rs.getString("P.DATA"));
                this.setTipo(rs.getString("T.DESCRICAO"));
                this.setSolcitante(rs.getString("S.NOME"));
                this.setUsuario(rs.getString("U.NOME"));
                proto.setStatus(rs.getString("P.STATUS"));

            }

            if (proto.getStatus().isEmpty()) {
                proto = null;

                return "/localizar/l_processo.xhtml";

            }

            return "/localizar/l_processo.xhtml";

        } catch (Exception e) {
            proto.setId_protocolo(0);
            proto.setData(null);
            this.setTipo("");
            this.setSolcitante("");
            this.setUsuario("");
            proto.setStatus("");

            FacesContext context = FacesContext.getCurrentInstance();

            context.addMessage(null, new FacesMessage("Atençao!", "Processo Nao Localizado!"));

            System.err.println("Erro=: " + e.getMessage());

            return "/localizar/l_processo.xhtml";

        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);
            FabricaConexao.fecharRs(rs);

        }

    }
   
    public void atualizar(RowEditEvent event) {
        ProtocoloBean pb = (ProtocoloBean) event.getObject();
        ProtocoloBean prot = new ProtocoloBean();

        if (pb.getStatus().equals(prot.getStatus_novo())) {

        } else {

            Connection con = FabricaConexao.getConnection();
            String query = "UPDATE PROTOCOLO SET USUARIO_ID = ?, STATUS = ? WHERE ID_PROTOCOLO = ?";
            PreparedStatement stmt = null;

            try {

                stmt = con.prepareStatement(query);
                stmt.setInt(1, recuperarUID(pb.getUsuario()));
                stmt.setString(2, pb.getStatus());
                stmt.setInt(3, pb.getId_protocolo());
                stmt.executeUpdate();

                System.out.println("Alterado com Sucesso!");
                System.out.println(pb.getStatus());
                System.out.println(pb.getId_protocolo());

            } catch (SQLException e) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, e);
                try {
                    con.rollback();

                } catch (Exception ex) {
                    Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, e);
                }

            } finally {
                FabricaConexao.fecharCon(con);
                FabricaConexao.fecharStmt((PreparedStatement) stmt);
            }

        }

    }

    public String deletarProcesso(Integer id) {

        ProtocoloBean prot = new ProtocoloBean();

        Connection con = FabricaConexao.getConnection();
        String query = "DELETE FROM PROTOCOLO WHERE ID_PROTOCOLO = ?";
        PreparedStatement stmt = null;
        prot.setId_protocolo(id);
        try {

            stmt = con.prepareStatement(query);
            stmt.setInt(1, prot.getId_protocolo());
            stmt.executeUpdate();

            this.getProtocolo().clear();

            System.out.println("Deletado com Sucesso!");
            System.out.println(prot.getId_protocolo());

            return "/localizar/listar_processo.xhtml";

        } catch (SQLException e) {

            Logger.getLogger(ProtocoloBean.class.getName()).log(Level.SEVERE, null, e);
            return null;

        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt((PreparedStatement) stmt);
        }

    }

    public void msgSucesso() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage("Successful", "Cadastrado com Sucesso!"));

    }

    public void msgErro(SQLException ex) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage("Erro", "Erro ao Cadastrar Usario :" + ex));
    }

//    public static void main(String[] args) {
//  }      
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSolcitante() {
        return solcitante;
    }

    public void setSolcitante(String solcitante) {
        this.solcitante = solcitante;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Solicitante getSo() {
        return so;
    }

    public void setSo(Solicitante so) {
        this.so = so;
    }

    public Tipo_protocolo getTp() {
        return tp;
    }

    public void setTp(Tipo_protocolo tp) {
        this.tp = tp;
    }

    public ArrayList<ProtocoloBean> getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(ArrayList<ProtocoloBean> protocolo) {
        this.protocolo = protocolo;
    }

    public SolicitanteBean getSob() {
        return sob;
    }

    public void setSob(SolicitanteBean sob) {
        this.sob = sob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public UsuarioBean getUb() {
        return ub;
    }

    public void setUb(UsuarioBean ub) {
        this.ub = ub;
    }

    public String getStatus_novo() {
        return status_novo;
    }

    public void setStatus_novo(String status_novo) {
        this.status_novo = "A espera do Solicitante";
    }

    public Protocolo getProto() {
        return proto;
    }

    public void setProto(Protocolo proto) {
        this.proto = proto;
    }

}
