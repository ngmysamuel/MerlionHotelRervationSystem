/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//PROBLEM ROOM COUNT IS NOT CORRECT IN THE ROOM INVENTORY
package stateless;

import entity.RoomInventory;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomInventoryNotFound;

/**
 *
 * @author samue
 */
@Stateless
public class RoomInventorySessionBean implements RoomInventorySessionBeanRemote, RoomInventorySessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public boolean isItFull(LocalDate date, RoomType roomType) { //return true if there is still space. Return false if there isn't space
        //RoomType roomType2 = em.find(RoomType.class, (long) 1);

//        RoomInventory ri = new RoomInventory();
//        ri.setDate(LocalDate.now());
//        ri.setRt(roomType);
//        ri.setRoomCountForAllocation(543);
//        ri.setRoomAvail(543);
//        em.persist(ri);
//        em.flush();
        Query q = em.createQuery("SELECT ri.roomAvail FROM RoomInventory ri WHERE ri.date = :date AND ri.rt.grade = :roomType");
        q.setParameter("date", date);
        q.setParameter("roomType", roomType.getGrade());
        List<Integer> ls = q.getResultList();
        if (ls.isEmpty()) {
            RoomInventory ri = new RoomInventory();
            ri.setDate(date);
            ri.setRt(roomType);
            ri.setRoomCountForAllocation(roomType.getInitialRoomAvailability());
            ri.setRoomAvail(roomType.getInitialRoomAvailability());
            em.persist(ri);
            em.flush();
            List<RoomInventory> roomInventoryList = roomType.getRoomInventory();
            roomInventoryList.add(ri);
            roomType.setRoomInventory(roomInventoryList);
            return true;
        } else {
            Integer roomsAvail = ls.get(0);
            if (roomsAvail <= 0) {
                return false;
            }
        }
        return true;
    }

    private void addRoomInventory(LocalDate date, RoomType roomType) {
        RoomInventory ri2 = new RoomInventory();
        ri2.setDate(date);
        ri2.setRt(roomType);
        ri2.setRoomAvail(roomType.getInitialRoomAvailability());
        ri2.setRoomCountForAllocation(roomType.getInitialRoomAvailability());
        em.persist(ri2);
        em.flush();
        List<RoomInventory> ls2 = roomType.getRoomInventory();
        ls2.add(ri2);
        roomType.setRoomInventory(ls2);
    }

    public RoomInventory retrieveRoomInventory(LocalDate date, RoomType rt) throws RoomInventoryNotFound {
        Query q = em.createQuery("SELECT ri FROM RoomInventory ri WHERE ri.date = :date AND ri.rt.grade = :roomType");
        q.setParameter("date", date);
        q.setParameter("roomType", rt.getGrade());
        try {
            return (RoomInventory) q.getSingleResult();
        } catch (NoResultException e) {
            throw new RoomInventoryNotFound();
        }
    }
    
    @Override
    public RoomInventory retrieveLeastRoomInventory(LocalDate dateStart, LocalDate dateEnd, RoomType rt) throws RoomInventoryNotFound{
        RoomInventory leastRoomInventory = retrieveRoomInventory(dateStart, rt);
        dateStart = dateStart.plusDays(1);
        while(dateStart.compareTo(dateEnd) == -1){
            if(retrieveRoomInventory(dateStart, rt).getRoomAvail() < leastRoomInventory.getRoomAvail()){
                leastRoomInventory = leastRoomInventory;
            }
            dateStart = dateStart.plusDays(1);
        }
        leastRoomInventory.getRt().getName();
        return leastRoomInventory;
    }
}
