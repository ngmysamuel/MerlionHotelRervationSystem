/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partnerseclient;

import artifacts.Reservation;
import artifacts.ReservationNotFoundException_Exception;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import artifacts.CreateReservation.Arg4;
import artifacts.CreateReservation.Arg4.Entry;

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
            System.out.println("What do you want to do? \n1. Create Reservation\n2. Search Rooms\n3. View All Reservations\n4. View a Paticular Reservation\n5. Exit\n6. Print roomType\n7. Search\n8. Wrong reservation");
            int selection = sc.nextInt();
            if (selection == 1) {
                makeReservation2();
            } else if (selection == 2) {
                searchRooms();
            } else if (selection == 3) {
                viewAllReservations();
            } else if (selection == 4) {
                viewAParticularReservation();
            } else if (selection == 5) {
                break;
            } else if (selection == 6) {
                printRoomType();
            } else if (selection == 7) {
                sear();
            } else if (selection ==8) {
                makeReservation();
            } else {
                System.out.println("what?");
            }
        }
    }
    
    public void sear() {
        System.out.println("Type in the date you want to start. DD/MM/YYYY");
        String startString = sc.next();
        System.out.println("Type in the date you want to end. DD/MM/YYYY");
        String endString = sc.next();
        search(startString, endString);
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
        System.out.println(r.getReservationLineItems());
    }
    
    private void searchRooms() {
        System.out.println("Type in the date you want to start. DD/MM/YYYY");
        String startString = sc.next();
        System.out.println("Type in the date you want to end. DD/MM/YYYY");
        String endString = sc.next();
        List<Boolean> ls1 = search(startString, endString);
        List<String> ls2 = getRoomType();
        System.out.println("Result of Room Search with the dates given: \n");
        int i = 0;
        for (String rt : ls2) {
            System.out.println("Room Type: " + rt + "-> " + ls1.get(i));
            ++i;
        }
        System.out.println("");
    }

    private void makeReservation() {
        Arg4 arg = new Arg4();
        System.out.println("Type in the date you want to start. DD/MM/YYYY");
        String startString = sc.next();
        System.out.println("Type in the date you want to end. DD/MM/YYYY");
        String endString = sc.next();
        System.out.println("What is the guest id?");
        Long guestId = sc.nextLong();
        System.out.println("What is partner id?");
        Long partnerId = sc.nextLong();
        List<String> ls = getRoomType();
        String rt = "";
        while (true) {
            System.out.println("What room type?");
            if (ls.size() > 0) {
                for (int i = 1; i <= ls.size(); i++) {
                    System.out.println(i + ". " + ls.get(i - 1));
                }
                int j = sc.nextInt();
                rt = ls.get(j - 1);
            } else {
                System.out.println("There are no room types initiated yet. Please look for the staff to create some rooms for some room types. So sorry.");
                return;
            }
            System.out.println("How many rooms?");
            Integer k = sc.nextInt();
            Entry e = new Entry();
            e.setKey(rt);
            e.setValue(k);
            arg.getEntry().add(e);
System.out.println("ls2 size is "+arg.getEntry().size());
            System.out.println("Do you want to book more? Press 1 for yes and 2 for no");
            int l = sc.nextInt();
            if (l == 2) {
                break;
            }
        }
        try {
            createReservation(startString, endString, guestId, partnerId, arg);
            System.out.println("STOTTTOTO");
        } catch (ReservationNotFoundException_Exception e) {
            System.out.println("Oh no, reservation is not ok");
            System.out.println(e);
        }
    }

    private void makeReservation2() {
        Arg4 arg = new Arg4();
        System.out.println("Type in the date you want to start. DD/MM/YYYY");
        String startString = sc.next();
        System.out.println("Type in the date you want to end. DD/MM/YYYY");
        String endString = sc.next();
        System.out.println("What is the guest id?");
        Long guestId = sc.nextLong();
        System.out.println("What is partner id?");
        Long partnerId = sc.nextLong();
        List<String> lsString = new ArrayList<>();
        List<Integer> lsInteger = new ArrayList<>();
        List<String> ls = getRoomType();
        String rt = "";
        while (true) {
            System.out.println("What room type?");
            if (ls.size() > 0) {
                for (int i = 1; i <= ls.size(); i++) {
                    System.out.println(i + ". " + ls.get(i - 1));
                }
                int j = sc.nextInt();
                rt = ls.get(j - 1);
            } else {
                System.out.println("There are no room types initiated yet. Please look for the staff to create some rooms for some room types. So sorry.");
                return;
            }
            System.out.println("How many rooms?");
            Integer k = sc.nextInt();
            lsString.add(rt);
            lsInteger.add(k);
System.out.println("ls2 size is "+arg.getEntry().size());
            System.out.println("Do you want to book more? Press 1 for yes and 2 for no");
            int l = sc.nextInt();
            if (l == 2) {
                break;
            }
        }
        try {
            createReservation2(startString, endString, guestId, partnerId, lsString, lsInteger);
            System.out.println("STOTTTOTO");
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

    private static void printRoomType() {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        port.printRoomType();
    }

    private static java.util.List<java.lang.Boolean> search(java.lang.String arg0, java.lang.String arg1) {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.search(arg0, arg1);
    }

    private static java.util.List<java.lang.String> getRoomType() {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.getRoomType();
    }

    private static Long createReservation(java.lang.String arg0, java.lang.String arg1, java.lang.Long arg2, java.lang.Long arg3, artifacts.CreateReservation.Arg4 arg4) throws ReservationNotFoundException_Exception {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.createReservation(arg0, arg1, arg2, arg3, arg4);
    }

    private static Long createReservation2(java.lang.String arg0, java.lang.String arg1, java.lang.Long arg2, java.lang.Long arg3, java.util.List<java.lang.String> arg4, java.util.List<java.lang.Integer> arg5) throws ReservationNotFoundException_Exception {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.createReservation2(arg0, arg1, arg2, arg3, arg4, arg5);
    }








}
