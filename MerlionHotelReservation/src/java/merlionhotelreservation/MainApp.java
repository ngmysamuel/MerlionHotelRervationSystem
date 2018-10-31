/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionhotelreservation;

import entity.Guest;
import entity.Reservation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import stateless.GuestControllerBeanRemote;
import stateless.ReservationControllerBeanRemote;
import util.exception.GuestAlreadyExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Asus
 */
public class MainApp {
    
    Scanner sc = new Scanner(System.in);
    private GuestControllerBeanRemote guestControllerBeanRemote;
    private ReservationControllerBeanRemote reservationControllerBeanRemote;
    private Guest currentGuest;

    public MainApp(GuestControllerBeanRemote guestControllerBeanRemote, ReservationControllerBeanRemote reservationControllerBeanRemote) {
        this.guestControllerBeanRemote = guestControllerBeanRemote;
        this.reservationControllerBeanRemote = reservationControllerBeanRemote;
    }
    
    public void runApp(){
        while(true){
            System.out.println("***Merlion Hotel Reservation System***");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Search Rooms");
            System.out.println("4: Exit");

            switch(sc.nextInt()){
                case 1: doLogin();
                case 2: doRegister();
                case 3: searchRooms();
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
            currentGuest = guestControllerBeanRemote.guestLogin(email, password);
        } catch (InvalidLoginCredentialException ex) {
            System.out.println("Incorrect email or password");
        }
        
    }
    
    private void mainMenu(){
        while(true){
            System.out.println("***Merlion Hotel Reservation System***");
            System.out.println("***Logged In***");
            System.out.println("1: Search Rooms");
            System.out.println("2: View My Reservation Details");
            System.out.println("3: View All My Reservations");
            System.out.println("4: Logout");
            System.out.println("5: Exit");

            switch(sc.nextInt()){
                case 1: searchRooms();
                case 2: viewMyReservationDetails();
                case 3: viewAllMyReservations();
                case 4: return;
                case 5: System.exit(1);
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
            currentGuest = guestControllerBeanRemote.createRegisteredGuest(email, password, telephone, passport);
            mainMenu();
        } catch (GuestAlreadyExistException ex){
            System.out.println("Your email or passport number has been used.");
        }
    }
    
    private void searchRooms(){
        System.out.println("Use case not supported yet!");
    }
    
    private void viewMyReservationDetails(){
        System.out.print("Check-in Date(YYYY-MM-DD)>");
        String start = sc.next();
        System.out.print("Check-out Date(YYYY-MM-DD)>");
        String end = sc.next();
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        try {
            Reservation reservation = reservationControllerBeanRemote.retrieveGuestReservationDetails(currentGuest.getEmail(), startDate, endDate);
            System.out.println(reservation);
        } catch(ReservationNotFoundException ex){
            System.out.println("Reservation not found!");
        }
    }
    
    private void viewAllMyReservations(){
        System.out.println("***Your Reservations***");
        List<Reservation> reservations = currentGuest.getReservations();
        int i = 1;
        for(Reservation reservation: reservations){
            System.out.println(i + ". Check-in Date" + reservation.getDateStart());
            System.out.println("   Check-out Date" + reservation.getDateEnd());
            i++;
        }
    }
    
    
}
