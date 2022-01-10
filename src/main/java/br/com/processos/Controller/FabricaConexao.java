package br.com.processos.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FabricaConexao {

    public static Connection getConnection() {

        final String USER = "root";
        final String PASS = "root";
        final String DRIVER = "com.mysql.jdbc.Driver";
        final String URL = "jdbc:mysql://localhost:3306/TESTE";

        Connection con = null;
        try {
            Class.forName(DRIVER);
//                DriverManager.setLoginTimeout(1);
            con = DriverManager.getConnection(URL, USER, PASS);

            return con;
        } catch (Exception e) {
            Logger.getLogger(FabricaConexao.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }

    public static void fecharCon(Connection con) {

        try {
            if (con != null) {
                con.close();
            }

        } catch (Exception e) {

            Logger.getLogger(FabricaConexao.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public static void fecharStmt(PreparedStatement stmt) {

        try {
            if (stmt != null) {
                stmt.close();
            }

        } catch (Exception e) {
            Logger.getLogger(FabricaConexao.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public static void fecharRs(ResultSet rs) {

        try {
            if (rs != null) {
                rs.close();
            }

        } catch (Exception e) {
            Logger.getLogger(FabricaConexao.class.getName()).log(Level.SEVERE, null, e);
        }

    }
 
}
