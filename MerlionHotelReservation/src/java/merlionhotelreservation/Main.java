/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionhotelreservation;

import javax.ejb.EJB;
import stateless.GuestControllerBeanRemote;
import stateless.MainControllerBeanRemote;
import stateless.PartnerControllerBeanRemote;
import stateless.RateControllerBeanRemote;
import stateless.ReservationControllerBeanRemote;
import stateless.RoomInventorySessionBeanRemote;
import stateless.RoomTypeControllerSessionBeanRemote;

/**
 *
 * @author Asus
 */
public class Main {

    @EJB(name = "PartnerControllerBeanRemote")
    private static PartnerControllerBeanRemote partnerControllerBeanRemote;

    @EJB(name = "MainControllerBeanRemote")
    private static MainControllerBeanRemote mainControllerBeanRemote;

    

    @EJB(name = "ReservationControllerBeanRemote")
    private static ReservationControllerBeanRemote reservationControllerBeanRemote;

    @EJB(name = "GuestControllerBeanRemote")
    private static GuestControllerBeanRemote guestControllerBeanRemote;
    

    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println(guestControllerBeanRemote);
        MainApp main = new MainApp(guestControllerBeanRemote, reservationControllerBeanRemote, mainControllerBeanRemote, partnerControllerBeanRemote);
        main.runApp();
    }
    
}
