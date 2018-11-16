    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.ReservationLineItem;
import entity.Room;
import entity.RoomInventory;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ReservationNotFoundException;
import util.exception.RoomInventoryNotFound;
import util.exception.StillInUseException;

/**
 *
 * @author samue
 */
@Stateless
public class RoomControllerSessionBean implements RoomControllerSessionBeanRemote, RoomControllerSessionBeanLocal {

    @EJB
    private RoomTypeControllerSessionBeanLocal roomTypeControllerSessionBean;

    @EJB
    private RoomInventorySessionBeanLocal roomInventorySessionBean;

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

    @Resource
    EJBContext eJBContext;

    public RoomControllerSessionBean() {
    }

    public boolean create(Integer roomNum, String status, Long roomTypeId) {
System.out.println("stateless.RoomControllerSessionBean.create()");
        RoomType type = em.find(RoomType.class, roomTypeId);
        Room newRoom = new Room(roomNum, status, type);
        Query q = em.createQuery("select r from Room r where r.number = :number");
        q.setParameter("number", roomNum);
        List<Room> lsRm = q.getResultList();
System.out.println(lsRm);
        if (lsRm.isEmpty()) {
            em.persist(newRoom);
System.out.println("new room persisted.");
            List<Room> ls = type.getRooms();
            ls.add(newRoom);
            type.setRooms(ls);
            return true;
        }
        return false;
    }

    public void update(Long roomNum, String status, Long roomTypeId) {
        Query q = em.createQuery("select r from Room r where r.number = :roomNum");
        q.setParameter("roomNum", roomNum);
        Room r = (Room) q.getSingleResult();
        r.setStatus(status);
        RoomType rt = new RoomType();
        if (roomTypeId.equals(new Long("-1"))) {
            return;
        } else {
            rt = em.find(RoomType.class, roomTypeId);
            r.setType(rt);
        }
    }

    public List<Room> viewAllRooms() {
        return em.createQuery("select r from Room r").getResultList();
    }

    public void deleteRoom(Long roomNum) throws StillInUseException {
        Query q = em.createQuery("select r from Room r where r.number = :roomNum");
        q.setParameter("roomNum", roomNum);
        Room r = (Room) q.getSingleResult();
        Long id = r.getId();
        List<ReservationLineItem> lsRli1 = r.getReservationLineItems();
        for (ReservationLineItem rli : lsRli1) {
            if (rli.getReservation().getDateEnd().isAfter(LocalDate.now())) {
                r.setStatus("Unavailable");
                throw new StillInUseException();
            }
        }

        RoomType rt = r.getType();
        List<Room> ls = rt.getRooms();
        for (int i = 0; i < ls.size(); i++) {
            Room r2 = ls.get(i);
            if (r2.getId().equals(id)) {
                ls.remove(r2);
            }
        }
        rt.setRooms(ls);
        em.remove(r);
    }

    public Boolean allocateRooms(RoomType rt, Integer numOfRooms, LocalDate dateStart, LocalDate dateEnd, Long id) {
        RoomInventory ri = new RoomInventory();
        if (!roomTypeControllerSessionBean.timerChecker(rt, dateStart, numOfRooms)) {
            return false;
        }
        
        boolean b = setRoomsAllocated(id, rt);
        if (!b) {
            //if (0 < numOfRooms) {    
            //eJBContext.setRollbackOnly();
            return false;
        }
        dateStart = dateStart.plusDays(1);

        return true;
    }

    public boolean setRoomsAllocated(Long id, RoomType rt) {
        ReservationLineItem rli = em.find(ReservationLineItem.class, id);
        rli.getAllocatedRooms().size();
        int numOfRooms = rli.getNumberOfRooms();
//        ri.setRoomCountForAllocation(ri.getRoomCountForAllocation() - numOfRooms);
        Query q = em.createQuery("SELECT r FROM Room r WHERE r.status = :status and r.type = :roomType");
        q.setParameter("status", "Available");
        q.setParameter("roomType", rt);
        List<Room> ls = q.getResultList();
        if (ls.size() < numOfRooms) {
            return false;
        } else {
            for (int i = 0; i < numOfRooms; i++) {
                Room r = ls.get(i);
                List<Room> ls2 = rli.getAllocatedRooms();
                ls2.add(r);
                rli.setAllocatedRooms(ls2);
                r.setStatus("Occupied");
                List<ReservationLineItem> ls3 = r.getReservationLineItems();
                ls3.add(rli);
                r.setReservationLineItems(ls3);
                em.flush();
            }
        }
        return true;
    }

}
