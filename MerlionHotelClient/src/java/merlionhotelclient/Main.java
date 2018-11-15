/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionhotelclient;

import javax.ejb.EJB;
import stateless.MainControllerBeanRemote;
import stateless.PartnerControllerBeanRemote;

/**
 *
 * @author samue
 */
public class Main {

    @EJB
    private static MainControllerBeanRemote mainControllerBean;

    @EJB
    private static PartnerControllerBeanRemote partnerControllerBeanRemote;


    public static void main(String[] args) {
        MainApp mainApp = new MainApp();
        System.out.println(mainControllerBean);
        System.out.println(partnerControllerBeanRemote);
        mainApp.run(mainControllerBean, partnerControllerBeanRemote);
    }
    
}
