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
import entity.RoomType;
import java.time.LocalDate;
import java.util.ArrayList;
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
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.type = :partner");
        q.setParameter("partner", ReservationTypeEnum.Partner);
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

    public Long createReservation(LocalDate dateStart, LocalDate dateEnd, Long guestId, Long partnerId, List<ReservationLineItem> rooms) throws ReservationNotFoundException {
        try {
            Reservation r = reservationControllerBean.createPartnerReservation(dateStart, dateEnd, guestId, partnerId, rooms);
            return r.getId();
        } catch (ReservationNotFoundException e) {
            throw e;
        }
    }

    public List<Boolean> searchRooms(LocalDate dateStart, LocalDate dateEnd) {
        int i = 0;
        LocalDate dateStartTemp = dateStart;
        System.out.println("V");
        List<Boolean> bo = new ArrayList<>();
        List<RoomType> ls = roomTypeControllerSessionBean.getRoomTypes();
        for (RoomType rt : ls) {
            while (!dateStartTemp.isAfter(dateEnd)) {
                ++i;
                if (!roomInventorySessionBean.isItFull(dateStart, rt)) {
                    bo.add(false);
                    break;
                } else if (roomInventorySessionBean.isItFull(dateEnd, rt)&& (dateStartTemp.isEqual(dateEnd))) {
                    bo.add(true);
                }
                dateStartTemp = dateStartTemp.plusDays(1);
            }
            dateStartTemp = dateStart;
        }
        return bo;
    }

}
