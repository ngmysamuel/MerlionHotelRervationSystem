/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import Enum.ReservationTypeEnum;
import entity.Guest;
import entity.Reservation;
import entity.ReservationLineItem;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Asus
 */
@Stateless
public class ReservationControllerBean implements ReservationControllerBeanRemote, ReservationControllerBeanLocal {

    @EJB(name = "GuestControllerBeanLocal")
    private GuestControllerBeanLocal guestControllerBeanLocal;

    @EJB(name = "RateControllerBeanLocal")
    private RateControllerBeanLocal rateControllerBeanLocal;

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Reservation retrieveGuestReservationDetails(String guestEmail, LocalDate dateStart, LocalDate dateEnd) throws ReservationNotFoundException {
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.guest.email = :inEmail AND r.dateStart = :inDateStart AND r.dateEnd = :inDateEnd");
        q.setParameter("inEmail", guestEmail);
        q.setParameter("inDateStart", dateStart);
        q.setParameter("inDateEnd", dateEnd);
        
        try{
            return (Reservation) q.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex){
            throw new ReservationNotFoundException("Reservation of " + guestEmail + " for " + dateStart + " to " + dateEnd + " not found.");
        }
    }

    @Override
    public Reservation createGuestReservation(LocalDate dateStart, LocalDate dateEnd, ReservationTypeEnum type, Guest guest, List<ReservationLineItem> rooms) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        BigDecimal price = rateControllerBeanLocal.countRate(dateStart, dateEnd);
        Reservation newReservation = new Reservation(currentDateTime, dateStart, dateEnd, type, rooms, guest, price);
        em.persist(newReservation);
        em.flush();
        return newReservation;
    }

    @Override
    public List<Reservation> retrieveAllReservationsForToday(LocalDate currentDate) {
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.dateStart <= :inDate AND r.dateEnd > :inDate");
        q.setParameter("inDate", currentDate);
        return (List<Reservation>) q.getResultList();
    }

    @Override
    public Reservation retrievePartnerReservationDetails(String partner, LocalDate dateStart, LocalDate dateEnd) throws ReservationNotFoundException {
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.username = :inUsername AND r.dateStart = :inDateStart AND r.dateEnd = :inDateEnd");
        q.setParameter("inUsername", partner);
        q.setParameter("inDateStart", dateStart);
        q.setParameter("inDateEnd", dateEnd);
        
        try{
            return (Reservation) q.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex){
            throw new ReservationNotFoundException("Reservation of " + partner + " for " + dateStart + " to " + dateEnd + " not found.");
        }
    }

    @Override
    public Reservation createPartnerReservation(LocalDate dateStart, LocalDate dateEnd, entity.Guest guest, entity.Partner partner, List<ReservationLineItem> rooms) throws ReservationNotFoundException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        BigDecimal price = rateControllerBeanLocal.countRate(dateStart, dateEnd);
        Reservation newReservation = new Reservation(currentDateTime, dateStart, dateEnd, rooms, guest, partner, price);
        em.persist(newReservation);
        em.flush();
        return newReservation;
    }
    
}
