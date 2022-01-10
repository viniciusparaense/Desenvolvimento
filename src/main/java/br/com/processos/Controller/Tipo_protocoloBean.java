package br.com.processos.Controller;

import br.com.processos.Modelo.Tipo_protocolo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "TPBean")
public class Tipo_protocoloBean extends Tipo_protocolo {

    public static Integer recuperarTID() {

        Connection con = FabricaConexao.getConnection();
        String query = "SELECT ID_TIPO FROM TIPO_PROTOCOLO WHERE DESCRICAO = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Tipo_protocoloBean ptipo;      

        try {

            ptipo = new Tipo_protocoloBean();
            stmt = con.prepareStatement(query);
            stmt.setString(1, ptipo.getDescricao());
            stmt.executeQuery();
            rs = stmt.executeQuery();

            while (rs.next()) {
             
                ptipo.setId_protocolo(rs.getInt("ID_TIPO"));
            }
            
            return ptipo.getId_protocolo();

        } catch (Exception e) {
            Logger.getLogger(Tipo_protocoloBean.class.getName()).log(Level.SEVERE, null, e);
            return null;

        } finally {
            FabricaConexao.fecharCon(con);
            FabricaConexao.fecharStmt(stmt);
            FabricaConexao.fecharRs(rs);

        }

    }

}
