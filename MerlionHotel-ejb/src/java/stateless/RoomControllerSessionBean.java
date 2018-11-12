/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.StillInUseException;

/**
 *
 * @author samue
 */
@Stateless
public class RoomControllerSessionBean implements RoomControllerSessionBeanRemote, RoomControllerSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

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
    
}
