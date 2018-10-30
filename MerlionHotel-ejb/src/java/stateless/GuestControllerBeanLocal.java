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

    RegisteredGuest createRegisteredGuest(String email, String password, String telephone, String passport) throws GuestAlreadyExistException;

    void addReservation(Reservation reservation, entity.Guest guest);

    RegisteredGuest guestLogin(String email, String password) throws InvalidLoginCredentialException;

    RegisteredGuest retrieveGuestByEmail(String email) throws GuestNotFoundException;
    
}
