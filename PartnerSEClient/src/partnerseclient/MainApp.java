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
import artifacts.ReservationLineItem;
import artifacts.Room;

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
                break;
            }
        }

        while (true) {
            
            
            System.out.println("What do you want to do? \n1. Create Reservation\n2. Search Rooms\n3. View All Reservations\n4. View a Paticular Reservation\n5. Exit\n6. Print roomType\n7. Wrong Search\n8. Wrong reservation");
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
        for (Reservation r : ls) {
            List<String> lsString = getStartAndEndDate(r.getId());
            System.out.print("Start date is "+lsString.get(0));
            System.out.print("    End date is "+lsString.get(1));
            System.out.println("     Reservation ID "+r.getId()+": ");
        }
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
        List<String> lsString = getStartAndEndDate(r.getId());
        System.out.print("Start date is "+lsString.get(0));
        System.out.print("    End date is "+lsString.get(1));
        System.out.println("     Reservation ID: "+r.getId());
        List<ReservationLineItem> ls = r.getReservationLineItems();
        System.out.println("      Price is "+r.getPrice());
//System.out.println("ReservationLineItems ls is "+ls);        
        for (ReservationLineItem rli : ls) {
            System.out.print("RoomType booked is "+rli.getRoomType().getName());
            System.out.println();
            List<Room> ls2 = rli.getAllocatedRooms();
            for (Room rm : ls2) {
                System.out.print("Allocated Room is "+rm.getNumber()+" and roomType is "+rm.getType().getName());
                System.out.print("      ");
            }
            System.out.println("\n\n");
        }
        System.out.println("");
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
            System.out.println(e.getMessage());
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

    private static java.util.List<java.lang.String> getStartAndEndDate(java.lang.Long arg0) {
        artifacts.PartnerReservationWebService_Service service = new artifacts.PartnerReservationWebService_Service();
        artifacts.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.getStartAndEndDate(arg0);
    }

    

    

    

    








}
