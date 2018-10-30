/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Guest;
import entity.RegisteredGuest;
import entity.Reservation;
import javax.ejb.Remote;
import util.exception.GuestAlreadyExistException;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Asus
 */
@Remote
public interface GuestControllerBeanRemote {

    public RegisteredGuest createRegisteredGuest(String email, String password, String telephone, String passport) throws GuestAlreadyExistException;

    public void addReservation(Reservation reservation, Guest guest);

    public RegisteredGuest guestLogin(String email, String password) throws InvalidLoginCredentialException;

    public RegisteredGuest retrieveGuestByEmail(String email) throws GuestNotFoundException;
    
}
