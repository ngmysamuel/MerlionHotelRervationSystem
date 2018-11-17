/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionhotelreservation;

import entity.Guest;
import entity.Reservation;
import entity.ReservationLineItem;
import entity.RoomInventory;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import stateless.GuestControllerBeanRemote;
import stateless.MainControllerBeanRemote;
import stateless.PartnerControllerBeanRemote;
import stateless.ReservationControllerBeanRemote;
import util.exception.GuestAlreadyExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoAvailableRoomsException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Asus
 */
public class MainApp {
    
    Scanner sc = new Scanner(System.in);
    private GuestControllerBeanRemote guestControllerBeanRemote;
    private ReservationControllerBeanRemote reservationControllerBeanRemote;
    private MainControllerBeanRemote mainControllerBeanRemote;
    private PartnerControllerBeanRemote partnerControllerBeanRemote;
    private Long currentGuest;

    public MainApp(GuestControllerBeanRemote guestControllerBeanRemote, ReservationControllerBeanRemote reservationControllerBeanRemote , MainControllerBeanRemote mainControllerBeanRemote, PartnerControllerBeanRemote partnerControllerBeanRemote) {
        this.guestControllerBeanRemote = guestControllerBeanRemote;
        this.reservationControllerBeanRemote = reservationControllerBeanRemote;
        this.mainControllerBeanRemote = mainControllerBeanRemote;
        this.partnerControllerBeanRemote = partnerControllerBeanRemote;
    }
    
    public void runApp(){
        while(true){
            System.out.println("***Merlion Hotel Reservation System***");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Search Rooms");
            System.out.println("4: Exit");

            switch(sc.nextInt()){
                case 1: doLogin(); break;
                case 2: doRegister(); break;
                case 3: searchRooms(); break;
                case 4: return;
            }
        }
    }
    
    private void doLogin(){
        System.out.print("Email>");
        String email = sc.next();
        System.out.print("Password>");
        String password = sc.next();
        sc.nextLine();
        try {
            currentGuest = guestControllerBeanRemote.guestLogin(email, password).getId();
            mainMenu();
        } catch (InvalidLoginCredentialException ex) {
            System.out.println("Incorrect email or password");
        }
        
    }
    
    private void mainMenu(){
        while(true){
            System.out.println("***Merlion Hotel Reservation System***");
            System.out.println("***Logged In***");
            System.out.println("1: Search Rooms");
            System.out.println("2: Reserve Rooms");
            System.out.println("3: View My Reservation Details");
            System.out.println("4: View All My Reservations");
            System.out.println("5: Logout");
            
            switch(sc.nextInt()){
                case 1: searchRooms(); break;
                case 2: reserveRooms(); break;
                case 3: 
                    viewMyReservationDetails();
                    sc.nextLine();
                    System.out.println("Press enter to continue");
                    sc.nextLine();
                    break;
                case 4: 
                    viewAllMyReservations(); 
                    sc.nextLine();
                    System.out.println("Press enter to continue");
                    sc.nextLine();
                    break;
                case 5: return;
            }
        }
    }
    
    private void doRegister(){
        System.out.println("***Registration***");
        System.out.print("Email>");
        String email = sc.next();
        System.out.print("Password>");
        String password = sc.next();
        System.out.print("Telephone>");
        String telephone = sc.next();
        System.out.print("Passport Number>");
        String passport = sc.next();
                
        try{
            currentGuest = guestControllerBeanRemote.createRegisteredGuest(email, password, telephone, passport).getId();
            mainMenu();
        } catch (GuestAlreadyExistException ex){
            System.out.println("Your email or passport number has been used.");
        }
    }
    
    private void searchRooms(){
//        System.out.print("Check-in Date(YYYY-MM-DD)>");
//        String start = sc.next();
//        System.out.print("Check-out Date(YYYY-MM-DD)>");
//        String end = sc.next();
//        LocalDate startDate = LocalDate.parse(start);
//        LocalDate endDate = LocalDate.parse(end);
//        try{
//            List<Pair<RoomInventory, BigDecimal>> rooms = mainControllerBeanRemote.searchRooms(startDate, endDate);
//            System.out.println("Available rooms");
//            int i = 1;
//            for(Pair<RoomInventory, BigDecimal> room: rooms){
//                System.out.println(i + ". " + room.getKey().getRt().getName());
//                System.out.println("  Price: " + room.getValue());
//                i++;
//            }
//            return rooms;
//        }catch(NoAvailableRoomsException ex){
//            System.out.println("No rooms avilable for the given date.");
//        }
//        return null;
        
        System.out.print("Check-in Date(YYYY-MM-DD)>");
        String start = sc.next();
        System.out.print("Check-out Date(YYYY-MM-DD)>");
        String end = sc.next();
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<Boolean> ls1 = this.partnerControllerBeanRemote.search(startDate, endDate, "1");
        List<RoomType> ls2 = this.mainControllerBeanRemote.sortRoomTypeAsc();
        System.out.println("Result of Room Search with the dates given: \n");
        int i = 0;
        for (RoomType rt : ls2) {
            System.out.println("Room Type: " + rt.getName() + "-> " + ls1.get(i));
            ++i;
        }
        System.out.println("");
    }
    
    private void reserveRooms(){
        System.out.println("***Reserve Rooms***");
        searchRooms();
        List<RoomType> roomTypes = this.mainControllerBeanRemote.sortRoomTypeAsc();

        System.out.println("Enter number of rooms booked for each type");
        List<ReservationLineItem> rlis = new ArrayList<>();
        for(RoomType room: roomTypes){
            System.out.print(room.getName() + "> ");
            int n = sc.nextInt();
            rlis.add(new ReservationLineItem(n , room));
        }
        System.out.println("Re-confirm stay dates");
        System.out.print("Check-in Date(YYYY-MM-DD)>");
        String start = sc.next();
        System.out.print("Check-out Date(YYYY-MM-DD)>");
        String end = sc.next();
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        try {
            mainControllerBeanRemote.reserveGuestRooms(currentGuest, startDate, endDate, rlis);
            System.out.println("Reservation has been made.");
        } catch (ReservationNotFoundException ex) {
            System.out.println("Reservation failed!");
        }

    }
    
    private void viewMyReservationDetails(){
        System.out.print("Check-in Date(YYYY-MM-DD)>");
        String start = sc.next();
        System.out.print("Check-out Date(YYYY-MM-DD)>");
        String end = sc.next();
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        try {
            Reservation reservation = reservationControllerBeanRemote.retrieveGuestReservationDetails(currentGuest, startDate, endDate);
            List<ReservationLineItem> rlis = reservation.getReservationLineItems();
            for(ReservationLineItem rli : rlis){
                System.out.println("Room type: " + rli.getRoomType().getName());
                System.out.println("Number of rooms: " + rli.getNumberOfRooms());
            }
            System.out.println("Price: " + reservation.getPrice());
        } catch(ReservationNotFoundException ex){
            System.out.println("Reservation not found!");
        }
    }
    
    private void viewAllMyReservations(){
        System.out.println("***Your Reservations***");
        List<Reservation> reservations = reservationControllerBeanRemote.retrieveAllGuestReservations(currentGuest);
        int i = 1;
        if(reservations.isEmpty()){
            System.out.println("***You don't have any reservations yet***");
        }
        for(Reservation reservation: reservations){
            System.out.println(i + ". Check-in Date " + reservation.getDateStart());
            System.out.println("   Check-out Date " + reservation.getDateEnd());
            i++;
        }
    }
    
    
}
