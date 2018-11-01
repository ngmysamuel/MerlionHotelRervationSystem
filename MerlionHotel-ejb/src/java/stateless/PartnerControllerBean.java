/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Partner;
import entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author samue
 */
@Stateless
public class PartnerControllerBean implements PartnerControllerBeanRemote, PartnerControllerBeanLocal {

    @EJB
    private ReservationControllerBeanLocal reservationControllerBean;

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;
    
    public Partner create(String emp, String manager, String username) {
        System.out.println("Partner controller bean is called");
        Partner part = new Partner("name", "password");
        em.persist(part);
        em.flush();
        return part;
    }

    public List<Partner> viewAll() {
        Query q = em.createQuery("SELECT p FROM Partner p");
        return q.getResultList();
    }
    public boolean login(String username, String password) {
        Query q = em.createQuery("SELECT p FROM Partner p WHERE p.username = :username");
        q.setParameter("username", username);
        List<Partner> ls = q.getResultList();
        Partner p = new Partner();
        if (!ls.isEmpty()) {
            p = ls.get(0);
        }
        if (p.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
    
    public List<Reservation> viewAllReservations() {
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.reservationTypeEnum = ReservationTypeEnum.Partner");
        return q.getResultList();
    }
    
    public Reservation viewReservationDetails(Long id) throws ReservationNotFoundException {
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.id = :id");
        q.setParameter("id", id);
        List<Reservation> ls = q.getResultList();
        if (ls.isEmpty()) {
            throw new ReservationNotFoundException();
        } 
        return ls.get(0);
    }
    
    public Reservation viewReservationDetails(Long partner, LocalDate dateStart, LocalDate dateEnd) throws ReservationNotFoundException {
        try {
            return reservationControllerBean.retrievePartnerReservationDetails(partner, dateStart, dateEnd);
        } catch (Throwable e) {
            throw e;
        }
    }
    
//    public List<Integer> searchRooms(LocalDate dateStart, LocalDate dateEnd) {
//        
//    }

}
