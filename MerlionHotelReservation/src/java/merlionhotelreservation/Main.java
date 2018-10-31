/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionhotelreservation;

import javax.ejb.EJB;
import stateless.GuestControllerBeanRemote;
import stateless.ReservationControllerBeanRemote;

/**
 *
 * @author Asus
 */
public class Main {

    @EJB(name = "ReservationControllerBeanRemote")
    private static ReservationControllerBeanRemote reservationControllerBeanRemote;

    @EJB(name = "GuestControllerBeanRemote")
    private static GuestControllerBeanRemote guestControllerBeanRemote;
    

    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println(guestControllerBeanRemote);
        MainApp main = new MainApp(guestControllerBeanRemote, reservationControllerBeanRemote);
        main.runApp();
    }
    
}
