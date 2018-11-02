/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partnerseclient;

import artifacts.Reservation;
import artifacts.ReservationLineItem;
import artifacts.ReservationNotFoundException_Exception;
import artifacts.RoomType;
import artifacts.ViewAllReservations;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author samue
 */
public class MainApp {

    Scanner sc = new Scanner(System.in);

    public void run() {
        System.out.println("username: password password: name");
        System.out.println("Login status is: " + login("1", "1"));

        while (true) {
            System.out.println("What do you want to do? \n1. Create Reservation\n2. Search Rooms\n3. View All Reservations\n4. View a Paticular Reservation\n5. Exit");
            int selection = sc.nextInt();
            if (selection == 1) {
                makeReservation();
            } else if (selection == 2) {
                searchRooms();
            } else if (selection == 3) {
                viewAllReservations();
            } else if (selection == 4) {
                viewAParticularReservation();
            } else if (selection == 5) {
                break;
            } else {
                System.out.println("what?");
            }
        }
    }
    
    public void viewAllReservations() {
        List<Reservation> ls = new ArrayList<>();
        ls = viewAllReservations_1();
        System.out.println(ls);
    }
    
    public void viewAParticularReservation() {
        System.out.println("What is the reservation ID that you want to see?");
        Long id = sc.nextLong();
        Reservation r = new Reservation();
        try {
            r = viewReservationDetails(id);
        } catch (ReservationNotFoundException_Exception e) {
            System.out.println("Oops");
            return;
        }
        System.out.println(r.getPrice());
        System.out.println(r.getDateEnd());
        System.out.println(r.getDateStart());
    }
    
    private void searchRooms() {
        System.out.println("Type in the date you want to start. DD/MM/YYYY");
        String startString = sc.next();
        System.out.println("Type in the date you want to end. DD/MM/YYYY");
        String endString = sc.next();
        List<Boolean> ls1 = searchRoom(startString, endString);
        List<RoomType> ls2 = getRoomType();
        System.out.println("Result of Room Search with the dates given: ");
        int i = 0;
        System.out.println("size of ls1 is " + ls1.size() + " and ls2 is " + ls2.size());
        for (RoomType rt : ls2) {
            System.out.println("Room Type: " + rt.getName() + "-> " + ls1.get(i));
            ++i;
        }
    }

    private void makeReservation() {
        System.out.println("Type in the date you want to start. DD/MM/YYYY");
        String startString = sc.next();
        System.out.println("Type in the date you want to end. DD/MM/YYYY");
        String endString = sc.next();
        System.out.println("What is the guest id?");
        Long guestId = sc.nextLong();
        System.out.println("What is partner id?");
        Long partnerId = sc.nextLong();
        System.out.println("What room type?");
        List<RoomType> ls = getRoomType();
        for (int i = 1; i <= ls.size(); i++) {
            System.out.println(i + ". " + ls.get(i - 1));
        }
        int j = sc.nextInt();
        RoomType rt = ls.get(j - 1);
        System.out.println(rt.getInitialRoomAvailability() + "xvii");
        System.out.println("How many rooms?");
        int k = sc.nextInt();
        ReservationLineItem rli = new ReservationLineItem();
        rli.setNumberOfRooms(k);
        rli.setRoomType(rt);
        List<ReservationLineItem> list = new ArrayList<ReservationLineItem>();
        list.add(rli);
        try {
            createReservation(startString, endString, guestId, partnerId, list);
        } catch (ReservationNotFoundException_Exception e) {
            System.out.println("Oh no, reservation is not ok");
            System.out.println(e);
        }
    }

    private static boolean login(java.lang.String arg0, java.lang.String arg1) {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.login(arg0, arg1);
    }

    private static java.util.List<artifacts.RoomType> getRoomType() {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.getRoomType();
    }

    private static Long createReservation(java.lang.String arg0, java.lang.String arg1, java.lang.Long arg2, java.lang.Long arg3, java.util.List<artifacts.ReservationLineItem> arg4) throws ReservationNotFoundException_Exception {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.createReservation(arg0, arg1, arg2, arg3, arg4);
    }

    private static java.util.List<java.lang.Boolean> searchRoom(java.lang.String arg0, java.lang.String arg1) {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.searchRoom(arg0, arg1);
    }

    private static Reservation viewReservationDetails(java.lang.Long arg0) throws ReservationNotFoundException_Exception {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.viewReservationDetails(arg0);
    }

    private static java.util.List<artifacts.Reservation> viewAllReservations_1() {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.viewAllReservations();
    }

}
