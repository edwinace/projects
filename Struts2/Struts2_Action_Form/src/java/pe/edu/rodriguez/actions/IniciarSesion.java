/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.rodriguez.actions;

import com.opensymphony.xwork2.ActionSupport;

/**
 *
 * @author Elvis
 */
public class IniciarSesion extends ActionSupport {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }    
    
}
