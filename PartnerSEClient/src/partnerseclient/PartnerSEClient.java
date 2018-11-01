/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partnerseclient;

import java.io.ObjectStreamConstants;

/**
 *
 * @author samue
 */
public class PartnerSEClient {

    
    public static void main(String[] args) {
        System.out.println("username: password password: name");
        System.out.println(login("password", "name"));
    }

    private static boolean login(java.lang.String arg0, java.lang.String arg1) {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.login(arg0, arg1);
    }
    
}
