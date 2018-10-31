/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Guest;
import entity.RegisteredGuest;
import entity.Reservation;
import javax.ejb.Local;
import util.exception.GuestAlreadyExistException;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Asus
 */
@Local
public interface GuestControllerBeanLocal {

    Guest createRegisteredGuest(String email, String password, String telephone, String passport) throws GuestAlreadyExistException;

    void addReservation(Reservation reservation, entity.Guest guest);

    Guest guestLogin(String email, String password) throws InvalidLoginCredentialException;

    Guest retrieveGuestByEmail(String email) throws GuestNotFoundException;
    
}
