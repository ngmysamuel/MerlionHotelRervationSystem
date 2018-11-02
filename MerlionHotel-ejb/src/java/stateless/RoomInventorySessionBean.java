/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.RoomInventory;
import entity.RoomType;
import java.time.LocalDate;
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

    public boolean isItFull(LocalDate date, RoomType roomType) {
        Query q = em.createQuery("SELECT ri.roomAvail FROM RoomInventory ri WHERE ri.date = :date AND ri.rt = :roomType");
        q.setParameter("date", date);
        q.setParameter("roomType", roomType);
        if (q.getResultList().isEmpty()) {
            RoomInventory ri = new RoomInventory();
            ri.setDate(date);
            ri.setRt(roomType);
            em.persist(ri);
            em.flush();
            roomType.getRoomInventory().add(ri);
            em.merge(roomType);
            return true;
        } else {
            Integer avail = (Integer) q.getResultList().get(0);
            if (avail <= 0) {
                return false;
            }
        }
        return true;
    }

    public RoomInventory retrieveRoomInventory(LocalDate date) throws RoomInventoryNotFound {
        Query q = em.createQuery("SELECT ri FROM RoomInventory ri WHERE ri.date = :date");
        q.setParameter("date", date);
        try {
            return (RoomInventory) q.getSingleResult();
        } catch (NoResultException e) {
            throw new RoomInventoryNotFound();
        }
    }
}
