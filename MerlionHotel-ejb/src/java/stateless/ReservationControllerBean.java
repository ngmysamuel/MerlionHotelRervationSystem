/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import Enum.ReservationTypeEnum;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.ReservationLineItem;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @EJB
    private RoomTypeControllerSessionBeanLocal roomTypeControllerSessionBean;

    @EJB
    private RateControllerBeanLocal rateControllerBeanLocal;

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;
    
    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Reservation retrieveGuestReservationDetails(Long guestId, LocalDate dateStart, LocalDate dateEnd) throws ReservationNotFoundException {
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.guest.id = :inId AND r.dateStart = :inDateStart AND r.dateEnd = :inDateEnd");
        q.setParameter("inId", guestId);
        q.setParameter("inDateStart", dateStart);
        q.setParameter("inDateEnd", dateEnd);
        
        try{
            Reservation r = (Reservation) q.getSingleResult();
            List<ReservationLineItem> rlis = r.getReservationLineItems();
            for(ReservationLineItem rli: rlis){
                rli.getRoomType().getId();
            }
            return r;
        } catch (NoResultException | NonUniqueResultException ex){
            throw new ReservationNotFoundException("Reservation of " + guestId + " for " + dateStart + " to " + dateEnd + " not found.");
        }
    }

    @Override
    public Reservation createGuestReservation(LocalDate dateStart, LocalDate dateEnd, ReservationTypeEnum type, java.lang.Long guestId, List<ReservationLineItem> rooms) throws ReservationNotFoundException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        BigDecimal price = new BigDecimal(0); 
        for(int i = 0; i < rooms.size(); i++){
            price.add(rateControllerBeanLocal.countRate(dateStart, dateEnd, rooms.get(i).getRoomType()));
        }
        Guest guest = em.find(Guest.class, guestId);
        Reservation newReservation = new Reservation(currentDateTime, dateStart, dateEnd, type, guest, price);
        em.persist(newReservation);
        
        LocalDate dateStartTemp = dateStart;
        for (ReservationLineItem rli : rooms) { //for each room line item
            RoomType rt = rli.getRoomType();
            Integer numOfRooms = rli.getNumberOfRooms();
            while (!dateStartTemp.isAfter(dateEnd)) { //for each day booked
                try {
                roomTypeControllerSessionBean.editAndCreateRoomInventoryIfNecessary(rt, dateStartTemp, numOfRooms);
                } catch (ReservationNotFoundException e) {
                    throw e;
                }
                dateStartTemp = dateStartTemp.plusDays(1);//the next day
            }
            dateStartTemp = dateStart;//the next room line item
            rli.setReservation(newReservation);
            em.persist(rli);
        }
        newReservation.setReservationLineItems(rooms);
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
    public Reservation retrievePartnerReservationDetails(java.lang.Long partnerId, LocalDate dateStart, LocalDate dateEnd) throws ReservationNotFoundException {
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.id = :inId AND r.dateStart = :inDateStart AND r.dateEnd = :inDateEnd");
        q.setParameter("inId", partnerId);
        q.setParameter("inDateStart", dateStart);
        q.setParameter("inDateEnd", dateEnd);
        
        try{
            return (Reservation) q.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex){
            throw new ReservationNotFoundException("Reservation of " + partnerId + " for " + dateStart + " to " + dateEnd + " not found.");
        }
    }

    @Override
    public Reservation createPartnerReservation(LocalDate dateStart, LocalDate dateEnd, Long guestId, Long partnerId, List<ReservationLineItem> rooms) throws ReservationNotFoundException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate oneYearOnDateTime = currentDateTime.plus(1, ChronoUnit.YEARS).toLocalDate();
        if (dateStart.isAfter(oneYearOnDateTime)) {
            throw new ReservationNotFoundException("Date reserved is too far ahead");
        }
        BigDecimal price = new BigDecimal(1);
        Guest guest = em.find(Guest.class, guestId);
        Partner partner = em.find(Partner.class, partnerId);
        
        Reservation newReservation = new Reservation(currentDateTime, dateStart, dateEnd, guest, partner, price);
        em.persist(newReservation);
        
        LocalDate dateStartTemp = dateStart;
        for (ReservationLineItem rli : rooms) { //for each room line item
            RoomType rt = rli.getRoomType();
            Integer numOfRooms = rli.getNumberOfRooms();
            while (!dateStartTemp.isAfter(dateEnd)) { //for each day booked
                try {
                roomTypeControllerSessionBean.editAndCreateRoomInventoryIfNecessary(rt, dateStartTemp, numOfRooms);
                } catch (ReservationNotFoundException e) {
                    throw e;
                }
                dateStartTemp = dateStartTemp.plusDays(1);//the next day
            }
            dateStartTemp = dateStart;//the next room line item
            rli.setReservation(newReservation);
            em.persist(rli);
        }
        newReservation.setReservationLineItems(rooms);
        return newReservation;
    }

    @Override
    public List<Reservation> retrieveAllGuestReservations(long guestId) {
        Guest guest = em.find(Guest.class, guestId);
        List<Reservation> reservations = guest.getReservations();
        for(Reservation reservation: reservations){
            reservation.getId();
            List<ReservationLineItem> rlis = reservation.getReservationLineItems();
            for(ReservationLineItem rli: rlis){
                RoomType roomType = rli.getRoomType();
                roomType.getName();
            }
        }
        return reservations;
    }
    
    @Override
    public Reservation retrieveCheckInReservation(long guestId) throws ReservationNotFoundException{
        Guest guest = em.find(Guest.class, guestId);
        List<Reservation> reservations = guest.getReservations();
        for(Reservation reservation: reservations){
            if(reservation.getDateStart().equals(LocalDate.now())){
                return reservation;
            }
        }
        throw new ReservationNotFoundException();
    }
}
