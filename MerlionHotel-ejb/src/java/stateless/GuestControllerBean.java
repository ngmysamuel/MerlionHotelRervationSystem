/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Guest;
import entity.RegisteredGuest;
import entity.Reservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.GuestAlreadyExistException;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Asus
 */
@Stateless
public class GuestControllerBean implements GuestControllerBeanRemote, GuestControllerBeanLocal {

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

    @Override
    public Guest createRegisteredGuest(String email, String password, String telephone, String passport) throws GuestAlreadyExistException {
        try {
            RegisteredGuest newGuest = new RegisteredGuest(email, password, telephone, passport);
            em.persist(newGuest);
            em.flush();
            newGuest.getId();
            return newGuest;
        } catch (EntityExistsException ex){
            throw new GuestAlreadyExistException("Guest with similar email or passport exists");
        }
    }

    @Override
    public void addReservation(Reservation reservation, entity.Guest guest) {
        List<Reservation> list = guest.getReservations();
        list.add(reservation);
        guest.setReservations(list);
    }

    @Override
    public Guest guestLogin(String email, String password) throws InvalidLoginCredentialException {
        try{
            Guest guest = retrieveGuestByEmail(email);
            Query query = em.createQuery("SELECT r.password FROM RegisteredGuest r WHERE r.id = :inGuest");
            query.setParameter("inGuest", guest.getId());
            if(password.equals(query.getSingleResult())){
                guest.getId();
                return guest;
            } else {
                throw new InvalidLoginCredentialException("Guest does not exist or invalid password");
            }
        } catch (GuestNotFoundException ex) {
            throw new InvalidLoginCredentialException("Guest does not exist or invalid password");
        }
    }

    @Override
    public Guest retrieveGuestByEmail(String email) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.email = :inEmail");
        query.setParameter("inEmail", email);
        
        try
        {
            return (Guest)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new GuestNotFoundException("Guest Email " + email + " does not exist!");
        }
    }
    
    @Override
    public Guest retrieveGuestByPassport(String passport) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.passportNumber = :inPassport");
        query.setParameter("inPassport", passport);
        
        try
        {
            return (Guest)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new GuestNotFoundException("Guest Passport " + passport + " does not exist!");
        }
    }
}
