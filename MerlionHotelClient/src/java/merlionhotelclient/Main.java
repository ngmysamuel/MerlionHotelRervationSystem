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

    @EJB(name = "PartnerControllerBeanRemote")
    private static PartnerControllerBeanRemote partnerControllerBeanRemote;

    @EJB
    private static MainControllerBeanRemote mainControllerBean;

    public static void main(String[] args) {
        MainApp mainApp = new MainApp();
        System.out.println(mainControllerBean);
        mainApp.run(mainControllerBean, partnerControllerBeanRemote);
    }
    
}
