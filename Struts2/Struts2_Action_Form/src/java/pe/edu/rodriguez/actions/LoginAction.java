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
public class LoginAction extends ActionSupport {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    @Override
    public void validate() {
        if("emrodriguez".equals(getUsername())) {
            addActionMessage("Usuario válido");
        } else {
            addActionError("Usuario no exite");
        }
    }
    
}
