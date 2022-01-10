package br.com.processos.Modelo;

import java.util.Date;

public class Protocolo {
    
    public Protocolo(){
    }
            
    
    private int id_protocolo;
    private String data;
    private int id_solicitante;
    private int id_tipo;
    private int id_usuario;
    private String status;

    
    public int getId_protocolo() {
        return id_protocolo;
    }
   
    public void setId_protocolo(int id_protocolo) {
        this.id_protocolo = id_protocolo;
    }
   
      
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId_solicitante() {
        return id_solicitante;
    }

    public void setId_solicitante(int id_solicitante) {
        this.id_solicitante = id_solicitante;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
