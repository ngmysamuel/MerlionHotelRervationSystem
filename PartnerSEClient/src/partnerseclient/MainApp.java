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

import artifacts.ReservationLineItem;
import artifacts.Room;
import java.math.BigDecimal;
import java.util.InputMismatchException;

/**
 *
 * @author samue
 */
public class MainApp {

    Scanner sc = new Scanner(System.in);

    public void run() {
        
        while (true) {
             System.out.println("Press 1 for login. Press any other key to exit.");
            int sel = sc.nextInt();
            if (sel != 1) {
                return;
            }
            
            System.out.println("What is you username?");
            String username = sc.next();
            System.out.println("What is your password?");
            String password = sc.next();
            if (!login(username, password)) {
                System.out.println("Wrong details.");
                continue;
            } else {
                operations();
            }
        }

        
    }
    
    public void operations() {
        while (true) {
            int selection = 100;
            System.out.println("What do you want to do? \n1. Create Reservation\n2. Search Rooms\n3. View All Reservations\n4. View a Paticular Reservation\n5. Exit");
            try {
                selection = sc.nextInt();
            } catch (InputMismatchException exception) {
                System.out.println(exception);
                sc.next();
            }
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
            } /*else if (selection == 6) {
                printRoomType();
            } else if (selection == 7) {
                sear();
            } /*else if (selection ==8) {
                makeReservation();
            }*/ else {
                System.out.println("what?");
            }

        }
    }
    
//    public void sear() {
//        System.out.println("Type in the date you want to start. DD/MM/YYYY");
//        String startString = sc.next();
//        System.out.println("Type in the date you want to end. DD/MM/YYYY");
//        String endString = sc.next();
//        search(startString, endString, "1");
//    }
    
    public void viewAllReservations() {
        List<Reservation> ls = new ArrayList<>();
        ls = viewAllReservations_1();
        for (Reservation r : ls) {
            List<String> lsString = getStartAndEndDate(r.getId());
            String s = String.format("Reservation ID is %-3d Start date is %-15s End date is %-15s", r.getId(), lsString.get(0), lsString.get(1));
            System.out.println(s);
        }
    }
    
    public void viewAParticularReservation() {
        System.out.println("What is the reservation ID that you want to see?");
        Long id = sc.nextLong();
        Reservation r = new Reservation();
        try {
            r = viewReservationDetails(id);
        } catch (ReservationNotFoundException_Exception e) {
            System.out.println("Reservation not found.");
            return;
        }
        List<String> lsString = getStartAndEndDate(r.getId());
        String s = String.format("Reservation ID is %-3d Start date is %-15s End date is %-15s Booked by Guest: %-25s", r.getId(), lsString.get(0), lsString.get(1), r.getGuest().getEmail());
        System.out.println(s);
        List<ReservationLineItem> ls = r.getReservationLineItems();
        System.out.println("Price is "+r.getPrice());  
        for (ReservationLineItem rli : ls) {
            s = String.format("%-3d rooms of RoomType: %-15S booked", rli.getNumberOfRooms(), rli.getRoomType().getName());
            System.out.println(s);
        }
        System.out.println("The guest will be informed of their allocated room upon checking in.");
        System.out.println("");
    }
    
    private void searchRooms() {
        System.out.println("Type in the date you want to start. DD/MM/YYYY");
        String startString = sc.next();
        System.out.println("Type in the date you want to end. DD/MM/YYYY");
        String endString = sc.next();
        System.out.println("How many rooms do you want to book?");
        String numOfRooms = sc.next();
        Integer numOfRoom = Integer.valueOf(numOfRooms);
        List<Boolean> ls1 = search(startString, endString, numOfRooms);
        List<String> ls2 = getRoomType();
        System.out.println("Result of Room Search with the dates given: \n");
        int i = 0;
        for (String rt : ls2) {
            BigDecimal bd = getPrice(startString, endString, rt, numOfRoom);
            if (ls1.get(i)){
                String s = String.format("Room Type: %-20S is possible for %3d rooms. Price is expected to be %-4f", rt, numOfRoom, bd);
                System.out.println(s);
            }
            ++i;
        }
        System.out.println("");
    }

    private void makeReservation2() {
//        Arg4 arg = new Arg4();
        System.out.println("Type in the date you want to start. DD/MM/YYYY");
        String startString = sc.next();
        System.out.println("Type in the date you want to end. DD/MM/YYYY");
        String endString = sc.next();
        System.out.println("What is the guest email?");
        String email = sc.next();
        System.out.println("What is the guest passport number?");
        String passportNumber = sc.next();
        System.out.println("What is the guest telephone?");
        String telephone = sc.next();
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
//System.out.println("ls2 size is "+arg.getEntry().size());
            System.out.println("Do you want to book more? Press 1 for yes and 2 for no");
            int l = sc.nextInt();
            if (l == 2) {
                break;
            }
        }
        try {
            createReservation2(startString, endString, email, passportNumber, telephone, partnerId, lsString, lsInteger);
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

  

    private static java.util.List<java.lang.String> getRoomType() {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.getRoomType();
    }

    

    

    private static java.util.List<java.lang.String> getStartAndEndDate(java.lang.Long arg0) {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.getStartAndEndDate(arg0);
    }

    private static Long createReservation2(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4, java.lang.Long arg5, java.util.List<java.lang.String> arg6, java.util.List<java.lang.Integer> arg7) throws ReservationNotFoundException_Exception {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.createReservation2(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }

    private static java.util.List<java.lang.Boolean> search(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.search(arg0, arg1, arg2);
    }

    private static BigDecimal getPrice(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.Integer arg3) {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.getPrice(arg0, arg1, arg2, arg3);
    }

    

    

    

    

    








}
