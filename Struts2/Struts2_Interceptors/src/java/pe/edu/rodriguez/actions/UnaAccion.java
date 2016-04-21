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
public class UnaAccion extends ActionSupport {

    @Override
    public String execute() throws Exception {
        System.out.println("UnaAccion - se ejecuta el metodo execute()");
        return SUCCESS;
    }
    
}
