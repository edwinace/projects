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
public class LargoProcesoAction extends ActionSupport {

    @Override
    public String execute() throws Exception {
        for (int i = 0; i < 100000; i++) {
            System.out.println(i);
        }
        return SUCCESS;
    }
    
}
