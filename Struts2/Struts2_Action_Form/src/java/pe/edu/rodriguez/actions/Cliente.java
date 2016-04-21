/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.rodriguez.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import pe.edu.rodriguez.entity.ClienteBean;

/**
 *
 * @author Elvis
 */
public class Cliente extends ActionSupport implements ModelDriven<ClienteBean>{

    private ClienteBean cliente = new ClienteBean();
    
    @Override
    public ClienteBean getModel() {
        return cliente;
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }
    
}
