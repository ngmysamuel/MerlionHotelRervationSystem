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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;
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

    @Override //for creaation of reservations
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
                System.out.println("not enough rooms\n");
                throw new ReservationNotFoundException("not enough rooms");
            } else {
//                ri.setRoomAvail(ri.getRoomAvail() - numOfRooms);
                em.persist(ri);
                return true;
            }
        } else {
            RoomInventory ri = (RoomInventory) q.getResultList().get(0);
            if (ri.getRoomAvail()<numOfRooms) {
                System.out.println("not enough rooms\n");
                throw new ReservationNotFoundException("not enough rooms");
            }
//            ri.setRoomAvail(ri.getRoomAvail() - numOfRooms);
            return true;
        }
    }
    
    //for timer to check
    public boolean timerChecker(RoomType rt, LocalDate date, Integer numOfRooms) {
        int roomsAvail;
        Query q1 = em.createQuery("select r from Room r where r.status = :status and r.type = :type");
        q1.setParameter("status", "Available");
        q1.setParameter("type", rt);
        roomsAvail = q1.getResultList().size();   
        if (roomsAvail < numOfRooms ) {
            return false;
        }

        return true;
    }

    public List<RoomType> getRoomTypes() {
        Query q = em.createQuery("SELECT rt FROM RoomType rt");
        List<RoomType> rts = q.getResultList();
        for(RoomType rt: rts){
            rt.getId();
        }
        return rts;
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public void create(String bed, String name, String amenities, int capacity, String description, int grade, int roomSize) {
        RoomType newrt = new RoomType(name, description, roomSize, bed, capacity, amenities, grade, null);
        newrt.setIsEnabled(true);
        manageGrade(grade, grade);
        em.persist(newrt);
    }

    public void update(String bed, String name, String amenities, String capacity, String description, String grade, String roomSize, int initialRoomAvail, Long roomTypeId, String b) {
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
            manageGrade(Integer.valueOf(grade), rt.getGrade());
            rt.setGrade(Integer.valueOf(grade));
        }
        if (roomSize.length() != 0) {
            rt.setRoomSize(Integer.valueOf(roomSize));
        }
        if (initialRoomAvail != -1) {
            rt.setInitialRoomAvailability(initialRoomAvail);
        }
        if (b.length() != 0) {
            rt.setIsEnabled(Boolean.valueOf(b));
        }
    }

    private void manageGrade(Integer newGrade, Integer oldGrade) { //is called from update() and create()
        if (newGrade <= oldGrade) { //new grade is 1 and old grade is 3. 1 2 3 -> 2 3 4 -> 2 3 1 || From 2 to nonexistant 1, it cannot work if I didn't have a prior grade. Because I will be selecting >= 1, I will be getting a null value
            Query q = em.createQuery("select rt from RoomType rt where rt.grade >= :newGrade");
            q.setParameter("newGrade", newGrade);
            List<RoomType> ls = q.getResultList();
            for (RoomType rt : ls) {
                rt.setGrade(rt.getGrade() + 1);
                em.flush();
            }
        } else if (newGrade > oldGrade) { //new grade is 3 and old grade 1. 1 2 3 -> 0 1 2  -> 3 1 2 
            Query q = em.createQuery("select rt from RoomType rt where rt.grade >= :oldGrade");
            q.setParameter("oldGrade", oldGrade);
            List<RoomType> ls = q.getResultList();
            for (RoomType rt : ls) {
                rt.setGrade(rt.getGrade() - 1);
                em.flush();
            }
        } else {
            
            }
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
    
    public RoomType retrieveRoomType(String name) throws RoomTypeNotFoundException{
        Query q = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :inName");
        q.setParameter("inName", name);
        try{
            return (RoomType) q.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex){
            throw new RoomTypeNotFoundException("Room type " + name + " not available!");
        }
    }
}
