/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import Enum.RateTypeEnum;
import Enum.ReservationTypeEnum;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.ReservationLineItem;
import entity.RoomInventory;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
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
    
    @Resource 
    EJBContext eJBContext;
    
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

    
    public Reservation createGuestReservation(LocalDate dateStart, LocalDate dateEnd, ReservationTypeEnum type, java.lang.Long guestId, List<ReservationLineItem> rooms) throws ReservationNotFoundException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        BigDecimal price = new BigDecimal(0); 
        for(int i = 0; i < rooms.size(); i++){
            BigDecimal nOfRooms = new BigDecimal(rooms.get(i).getNumberOfRooms());
            BigDecimal rr = rateControllerBeanLocal.countRate(dateStart, dateEnd, rooms.get(i).getRoomType());
            BigDecimal temp = rr.multiply(nOfRooms);
            price = price.add(temp);
        }
        Guest guest = em.find(Guest.class, guestId);
        Reservation newReservation = new Reservation(currentDateTime, dateStart, dateEnd, type, guest, price);
        em.persist(newReservation);
        guest.getReservations().add(newReservation);
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
    public Reservation createWalkinReservation(LocalDate dateStart, LocalDate dateEnd, ReservationTypeEnum type, java.lang.Long guestId, List<ReservationLineItem> rooms) throws ReservationNotFoundException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        BigDecimal price = new BigDecimal(0); 
        for(int i = 0; i < rooms.size(); i++){
            Query q = em.createQuery("SELECT r.price FROM Rate r WHERE r.type = :inType AND r.status <> 'disabled' AND r.roomType.name = :inRoom");
            q.setParameter("inType", RateTypeEnum.Published);
            q.setParameter("inRoom", rooms.get(i).getRoomType().getName());
            BigDecimal rate = (BigDecimal) q.getSingleResult();
            LocalDate date = dateStart;
            while(date.compareTo(dateEnd) < 0){
                BigDecimal nOfRoom = new BigDecimal(rooms.get(i).getNumberOfRooms());
                BigDecimal temp = rate.multiply(nOfRoom);
                price = price.add(temp);
                date = date.plusDays(1);
            }
        }
            
        Guest guest = em.find(Guest.class, guestId);
        Reservation newReservation = new Reservation(currentDateTime, dateStart, dateEnd, type, guest, price);
        em.persist(newReservation);
        guest.getReservations().add(newReservation);
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
        BigDecimal price = new BigDecimal(0);
        for (ReservationLineItem rli : rooms) {
            BigDecimal nOfRooms = new BigDecimal(rli.getNumberOfRooms());
            BigDecimal rr = rateControllerBeanLocal.countRate(dateStart, dateEnd, rli.getRoomType());
            BigDecimal temp = rr.multiply(nOfRooms);
            price = price.add(temp);
        }
        Guest guest = em.find(Guest.class, guestId);
        Partner partner = em.find(Partner.class, partnerId);
        
        if (guest == null || partner == null) {
            throw new ReservationNotFoundException("There is no such guest or partner.");
        }
        
        Reservation newReservation = new Reservation(currentDateTime, dateStart, dateEnd, guest, partner, price);
        em.persist(newReservation);
        
        LocalDate dateStartTemp = dateStart;
        for (ReservationLineItem rli : rooms) { //for each room line item
            RoomType rt = rli.getRoomType();
            Integer numOfRooms = rli.getNumberOfRooms();
            while (!dateStartTemp.isAfter(dateEnd)) { //for each day booked
                try {
                roomTypeControllerSessionBean.editAndCreateRoomInventoryIfNecessary(rt, dateStartTemp, numOfRooms);
                Query q = em.createQuery("select ri from RoomInventory ri where ri.date = :date and ri.rt = :rt");
                q.setParameter("date", dateStartTemp);
                q.setParameter("rt", rt);
                RoomInventory ri = (RoomInventory) q.getSingleResult();
                ri.setRoomAvail(ri.getRoomAvail()-numOfRooms);
                } catch (ReservationNotFoundException e) {
                    eJBContext.setRollbackOnly();
                    throw e;
                }
                dateStartTemp = dateStartTemp.plusDays(1);//the next day
            }
            dateStartTemp = dateStart;//the next room line item
            rli.setReservation(newReservation);
            em.persist(rli);
        }
        List<ReservationLineItem> lsRli = newReservation.getReservationLineItems();
        lsRli.addAll(rooms);
        newReservation.setReservationLineItems(lsRli);
        
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
            if(reservation.getDateStart().compareTo(LocalDate.now()) == 0){
                return reservation;
            }
        }
        throw new ReservationNotFoundException();
    }
}
