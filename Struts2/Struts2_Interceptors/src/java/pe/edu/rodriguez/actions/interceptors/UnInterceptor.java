/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.rodriguez.actions.interceptors;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 *
 * @author Elvis
 */
public class UnInterceptor implements Interceptor {

    @Override
    public void destroy() {
        System.out.println("Se detruye el interceptor (UnInterceptor)");
    }

    @Override
    public void init() {
        System.out.println("Se inicializa el interceptor (UnInterceptor)");
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        System.out.println("Antes de ActionInvocation");
        String result = invocation.invoke();
        System.out.println("contiene [result]: " + result);
        System.out.println("Despues de ActionInvocation");
        
        return result;
    }
    
}
