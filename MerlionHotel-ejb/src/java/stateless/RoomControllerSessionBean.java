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
   

    public void create(Integer roomNum, String status, Long roomTypeId) {
        RoomType type = em.find(RoomType.class, roomTypeId);
        Room newRoom = new Room(roomNum, status, type);
        em.persist(newRoom);
        List<Room> ls = type.getRooms();
        ls.add(newRoom);
        type.setRooms(ls);
    }

    public void update(Long roomNum, String status, Long roomTypeId) {
        Query q = em.createQuery("select r from Room r where r.number = :roomNum");
        q.setParameter("roomNum", roomNum);
        Room r = (Room) q.getSingleResult();
        r.setStatus(status);
        if (roomTypeId.equals(new Long ("-1"))) {
            
        } else {
            RoomType rt = em.find(RoomType.class, roomTypeId);
            r.setType(rt);
        }
    }
    
    public List<Room> viewAllRooms() {
        return em.createQuery("select r from room r").getResultList();
    }
    
    public void deleteRoom(Long roomNum) throws StillInUseException {
        Query q = em.createQuery("select r from Room r where r.number = :roomNum");
        q.setParameter("roomNum", roomNum);
        Room r = (Room) q.getSingleResult();
        Long id = r.getId();
        
        if (!r.getReservationLineItems().isEmpty()) {
            r.setStatus("Unavailable");
            throw new StillInUseException();
        } else {
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
    }
    
    
    public Boolean allocateRooms(RoomType rt, Integer numOfRooms, LocalDate dateStart, LocalDate dateEnd, Long id) {
        while (!dateStart.isAfter(dateEnd)) {
            RoomInventory ri = new RoomInventory();
            try {
                try {
                    roomTypeControllerSessionBean.editAndCreateRoomInventoryIfNecessary(rt, dateStart, numOfRooms);
                } catch (ReservationNotFoundException ex) {
                    return false;
                }
                ri = roomInventorySessionBean.retrieveRoomInventory(dateStart, rt);
System.out.println("In allocateRooms() where roomInventory date is "+ri.getDate()+" roomType is "+ri.getRt().getName());
            } catch (RoomInventoryNotFound e) {
                return false;
            }
            boolean b = setRoomsAllocated(id, ri);
            if (!b) {
            //if (0 < numOfRooms) {    
               //eJBContext.setRollbackOnly();
System.out.println("after rolling back");
                return false;
            } 
            dateStart = dateStart.plusDays(1);
        }
        return true;
    }
    
    public boolean setRoomsAllocated(Long id, RoomInventory ri) {
System.out.println("I am in setRoomsAllocated()");
        ReservationLineItem rli = em.find(ReservationLineItem.class, id);
        rli.getAllocatedRooms().size();
System.out.println("after just em.find: the allocated rooms for rli is "+rli.getAllocatedRooms());
        int numOfRooms = rli.getNumberOfRooms();
        RoomType rt = ri.getRt();
//        ri.setRoomCountForAllocation(ri.getRoomCountForAllocation() - numOfRooms);
        Query q = em.createQuery("SELECT r FROM Room r WHERE r.status = :status and r.type = :roomType");
        q.setParameter("status", "Available");
        q.setParameter("roomType", rt);
        List<Room> ls = q.getResultList();
        if (ls.size() < numOfRooms) {
System.out.println("There is lesser available rooms where \n"+ls+"\n than there is the num of rooms we need for the RLI: "+numOfRooms);
            return false;
        } else {
System.out.println("There is enough available rooms where \n"+ls+"\n The num of rooms we need for the RLI: "+numOfRooms);
            for (int i = 0; i < numOfRooms; i++) {
System.out.println("i = "+i);
                Room r = ls.get(i);
                List<Room> ls2 = rli.getAllocatedRooms();
                ls2.add(r);
System.out.println("ls2, after .add(r) is "+ls2);
                rli.setAllocatedRooms(ls2);
System.out.println("within the for loop of setRoomsAllocated: the allocated rooms for rli is "+rli.getAllocatedRooms());
                r.setStatus("Occupied");
                List<ReservationLineItem> ls3 = r.getReservationLineItems();
                ls3.add(rli);
                r.setReservationLineItems(ls3);
                em.flush();
            }
        } //rt: roomType||rli: reservationlineitem||ls: list of available rooms of the roomtype||ls2: rli's list of allocated rooms
          //r: a room that i randomly get from the list of avail rooms.||ls3: r's rli-s 
System.out.println("rli allocated rooms is "+rli.getAllocatedRooms());
        return true;
    }
    
}
