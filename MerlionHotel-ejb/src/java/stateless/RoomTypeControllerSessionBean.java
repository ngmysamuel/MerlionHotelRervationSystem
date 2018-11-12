/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Room;
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
import util.exception.ReservationNotFoundException;
import util.exception.StillInUseException;

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

    @Override
    public Boolean editAndCreateRoomInventoryIfNecessary(RoomType rt, LocalDate date, Integer numOfRooms) throws ReservationNotFoundException {
        Query q = em.createQuery("SELECT ri FROM RoomInventory ri WHERE ri.date = :date AND ri.rt= :rt");
        q.setParameter("date", date);
        q.setParameter("rt", rt);
        if (q.getResultList().isEmpty()) {
            RoomInventory ri = new RoomInventory();
            ri.setDate(date);
            ri.setRt(rt);
            ri.setRoomAvail(rt.getInitialRoomAvailability());
            ri.setRoomCountForAllocation(rt.getInitialRoomAvailability());
            if (ri.getRoomAvail() < numOfRooms) {
                eJBContext.setRollbackOnly();
                throw new ReservationNotFoundException();
            } else {
                ri.setRoomAvail(ri.getRoomAvail() - numOfRooms);
                em.persist(ri);
                return true;
            }
        } else {
            RoomInventory ri = (RoomInventory) q.getResultList().get(0);
            if (ri.getRoomAvail() < numOfRooms) {
                eJBContext.setRollbackOnly();
                throw new ReservationNotFoundException();
            }
            ri.setRoomAvail(ri.getRoomAvail() - numOfRooms);
            return true;
        }
    }

    public List<RoomType> getRoomTypes() {
        Query q = em.createQuery("SELECT rt FROM RoomType rt");
        return q.getResultList();
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public void create(String bed, String name, String amenities, int capacity, String description, int grade, int roomSize) {
        RoomType newrt = new RoomType(name, description, roomSize, bed, capacity, amenities, grade);
        newrt.setIsEnabled(true);
        manageGrade(grade);
        em.persist(newrt);
    }

    public void update(String bed, String name, String amenities, String capacity, String description, String grade, String roomSize, int initialRoomAvail, Long roomTypeId) {
        RoomType rt = em.find(RoomType.class, roomTypeId);
        if (bed.length() != 0) {
            rt.setBed(bed);
        }
        if (name.length() != 0) {
            rt.setName(name);
        }
        if (capacity.length() != 0) {
            rt.setCapacity(Integer.valueOf(capacity));
        }
        if (description.length() != 0) {
            rt.setDescription(description);
        }
        if (grade.length() != 0) {
            manageGrade(Integer.valueOf(grade));
            rt.setGrade(Integer.valueOf(grade));
        }
        if (roomSize.length() != 0) {
            rt.setRoomSize(Integer.valueOf(roomSize));
        }
        if (initialRoomAvail != -1) {
            rt.setInitialRoomAvailability(initialRoomAvail);
        }
    }

    private void manageGrade(Integer grade) {
        Query q = em.createQuery("select rt from RoomType rt where rt.grade >= :grade");
        q.setParameter("grade", grade);
        List<RoomType> ls = q.getResultList();
        for (RoomType rt : ls) {
            rt.setGrade(rt.getGrade()+1);
        }
        em.flush();
    }
    
    public void delete(Long id) throws StillInUseException {
        RoomType rt = em.find(RoomType.class, id);
        List<Room> ls = rt.getRooms();
        if (ls.isEmpty()) {
            em.remove(rt);
        } else {
            rt.setIsEnabled(false);
            throw new StillInUseException();
        }
    }
}
