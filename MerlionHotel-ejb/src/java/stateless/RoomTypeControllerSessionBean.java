/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.RoomInventory;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author samue
 */
@Stateless
public class RoomTypeControllerSessionBean implements RoomTypeControllerSessionBeanRemote, RoomTypeControllerSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;
    
    @Resource
    private EJBContext eJBContext;

    public void editAndCreateRoomInventoryIfNecessary(RoomType rt, LocalDate date, Integer numOfRooms) {
        Query q = em.createQuery("SELECT ri FROM RoomInventory ri WHERE ri.date = :date AND ri.rt = :rt");
        q.setParameter("date", date);
        q.setParameter("rt", rt);
        if (q.getResultList().isEmpty()) {
            RoomInventory ri = new RoomInventory();
            ri.setDate(date);
            ri.setRt(rt);
            ri.setRoomAvail(rt.getInitialRoomAvailability());
            if (ri.getRoomAvail() < numOfRooms) {
                eJBContext.setRollbackOnly();
                return;
            } else {
                ri.setRoomAvail(ri.getRoomAvail()-numOfRooms);
                em.persist(ri);
            }
        } else {
            RoomInventory ri = (RoomInventory) q.getResultList().get(0);
            if (ri.getRoomAvail() < numOfRooms) {
                eJBContext.setRollbackOnly();
            }
            ri.setRoomAvail(ri.getRoomAvail()-numOfRooms);
        }
    }
    
    public List<RoomType> getRoomTypes() {
        Query q = em.createQuery("SELECT rt FROM RoomType rt");
        return q.getResultList();
    }
    
    public void persist(Object object) {
        em.persist(object);
    }
}
