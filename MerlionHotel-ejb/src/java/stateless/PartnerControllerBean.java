/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import Enum.ReservationTypeEnum;
import entity.Partner;
import entity.Reservation;
import entity.ReservationLineItem;
import entity.RoomInventory;
import entity.RoomType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    private MainControllerBeanLocal mainControllerBean;

    @EJB
    private RoomInventorySessionBeanLocal roomInventorySessionBean;

    @EJB
    private RoomTypeControllerSessionBeanLocal roomTypeControllerSessionBean;

    @EJB
    private ReservationControllerBeanLocal reservationControllerBean;

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

    public Partner create(String emp, String manager, String username) {
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
System.out.println("I am in PartnerControllerBean");        
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.type = :partner");
        q.setParameter("partner", ReservationTypeEnum.Partner);
        return q.getResultList();
    }

    public Reservation viewReservationDetails(Long id) throws ReservationNotFoundException {
System.out.println("I am in PartnerControllerBean");
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.id = :id");
        q.setParameter("id", id);
        Reservation r = new Reservation();
        r = (Reservation) q.getSingleResult();
        r.getReservationLineItems().size();
System.out.println("PartnerControllerBean the reservation is "+r);
System.out.println("PartnerControllerBean the reservation line items is "+r.getReservationLineItems());
        for (ReservationLineItem rli : r.getReservationLineItems()) {
            rli.getAllocatedRooms().size();
System.out.println("PartnerControllerBean the allocated rooms is "+rli.getAllocatedRooms());
            rli.getReservation();
            rli.getRoomType();
        }
        return r;
    }

    public Reservation viewReservationDetails(Long partner, LocalDate dateStart, LocalDate dateEnd) throws ReservationNotFoundException {
        try {
            return reservationControllerBean.retrievePartnerReservationDetails(partner, dateStart, dateEnd);
        } catch (Throwable e) {
            throw e;
        }
    }

    public Long createReservation(LocalDate dateStart, LocalDate dateEnd, Long guestId, Long partnerId, List<ReservationLineItem> rooms) throws ReservationNotFoundException {
        try {
            Reservation r = reservationControllerBean.createPartnerReservation(dateStart, dateEnd, guestId, partnerId, rooms);
            return r.getId();
        } catch (ReservationNotFoundException e) {
            throw e;
        }
    }
    
    public void printRoomType() {
        RoomType roomType = em.find(RoomType.class, (long) 1);
        
        RoomInventory ri = new RoomInventory();
        ri.setDate(LocalDate.now());
        ri.setRt(roomType);
        ri.setRoomCountForAllocation(789);
        ri.setRoomAvail(789);
        em.persist(ri);
        em.flush();
//        Query q = em.createQuery("SELECT ri FROM RoomInventory ri WHERE ri.date = :date");
//        q.setParameter("date", LocalDate.now());
//        //q.setParameter("roomType", 2);
//        System.out.println(q.getResultList());
//        List<RoomInventory> ls = q.getResultList();
//        RoomInventory ri = ls.get(0);
//        System.out.println(ri.getRt());
    }

    @Override
    public List<Boolean> searchRooms(LocalDate dateStart, LocalDate dateEnd) {
        RoomType roomType = em.find(RoomType.class, (long) 1);
        
        RoomInventory ri = new RoomInventory();
        ri.setDate(LocalDate.now());
        ri.setRt(roomType);
        ri.setRoomCountForAllocation(123);
        ri.setRoomAvail(123);
        em.persist(ri);
        em.flush();
        int i = 0;
        LocalDate dateStartTemp = dateStart;
        List<Boolean> bo = new ArrayList<>();
        List<RoomType> ls = roomTypeControllerSessionBean.getRoomTypes();
        for (RoomType rt : ls) {
System.out.println("partnerControllerBean rt.getGrade() is "+rt.getGrade());
            while (!dateStartTemp.isAfter(dateEnd)) { //bo is an array of which room types has enough space to accomodate all the days given
                ++i;
                boolean full = roomInventorySessionBean.isItFull(dateStartTemp, rt);
                if (!full) { //this evaluates to true but is then negated to false
                    bo.add(false);
                    break;
                } else if (full&& (dateStartTemp.isEqual(dateEnd))) {
                    bo.add(true);
                }
                dateStartTemp = dateStartTemp.plusDays(1);
            }
            dateStartTemp = dateStart;
        }
//        
//        bo.add(Boolean.TRUE);
//        bo.add(Boolean.TRUE);
        return bo;
    }
    
    @Override
    public List<Boolean> search (LocalDate dateStart, LocalDate dateEnd) {
//        RoomType roomType = em.find(RoomType.class, (long) 1);
//        
//        RoomInventory ri = new RoomInventory();
//        ri.setDate(LocalDate.now());
//        ri.setRt(roomType);
//        ri.setRoomCountForAllocation(123);
//        ri.setRoomAvail(123);
//        em.persist(ri);
//        em.flush();
//System.out.println("XV id is "+ri.getId());

        int i = 0;
        LocalDate dateStartTemp = dateStart;
        List<Boolean> bo = new ArrayList<>();
        List<RoomType> ls = mainControllerBean.sortRoomTypeAsc();
        boolean full = true;
        for (RoomType rt : ls) {
            while (dateStartTemp.compareTo(dateEnd) < 0) { //bo is an array of which room types has enough space to accomodate all the days given
                ++i;
                full = roomInventorySessionBean.isItFull(dateStartTemp, rt);
                if (!full) { //this evaluates to true but is then negated to false
                    bo.add(false);
                    break;
                } else if (full && (dateStartTemp.isEqual(dateEnd.minusDays(1)))) {
                    bo.add(true);
                }
                dateStartTemp = dateStartTemp.plusDays(1);
            }
            dateStartTemp = dateStart;
        }
        return bo;
    }

}
