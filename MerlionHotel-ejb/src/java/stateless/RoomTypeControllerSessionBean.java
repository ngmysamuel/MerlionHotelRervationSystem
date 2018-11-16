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
import javax.ejb.EJB;
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

    @EJB
    private MainControllerBeanLocal mainControllerBean;

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
            if (ri.getRoomAvail() < numOfRooms) {
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
        if (roomsAvail < numOfRooms) {
            return false;
        }

        return true;
    }

    public List<RoomType> getRoomTypes() {
        Query q = em.createQuery("SELECT rt FROM RoomType rt");
        List<RoomType> rts = q.getResultList();
        for (RoomType rt : rts) {
            rt.getId();
        }
        return rts;
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public void create(String bed, String name, String amenities, int capacity, String description, int grade, int roomSize) {
        RoomType newrt = new RoomType(name, description, roomSize, bed, capacity, amenities, grade, roomSize);
        newrt.setIsEnabled(true);
        createGrade(grade);
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
            List<RoomType> ls = updateGrade1(Integer.valueOf(grade), rt.getGrade());
            updateGrade2(Integer.valueOf(grade), rt.getGrade(), ls);
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

    private void updateGrade2(Integer newGrade, Integer currentGrade, List<RoomType> ls) { //is called from update() and create()
System.out.println("manage grade is called");
        if (newGrade <= currentGrade) { //new grade is 1 and old grade is 3. 1 2 3 -> 2 3 4 -> 2 3 1 || From 2 to nonexistant 1, it cannot work if I didn't have a prior grade. Because I will be selecting >= 1, I will be getting a null value
            for (int i = ls.size() - 1; i >= 0; i--) {
                RoomType rt = ls.get(i);
                if (rt.getGrade() == currentGrade) {
                    rt.setGrade(Integer.MAX_VALUE);
                    em.flush();
                    continue;
                }
                if (rt.getGrade() < currentGrade) {
                    rt.setGrade(rt.getGrade() + 1);
                    em.flush();
                }
            }
        } else if (newGrade > currentGrade) { 
System.out.println("pruned list is "+ls);
System.out.println("curren grade is "+currentGrade);
            for (int i = 0; i < ls.size(); i++) {
                RoomType rt = ls.get(i);
System.out.println("rt grade now is "+rt.getGrade());
                if (rt.getGrade() == currentGrade) {
                    rt.setGrade(Integer.MAX_VALUE);
                    em.flush();
                    continue;
                }
                if (rt.getGrade() > currentGrade) {
                    rt.setGrade(rt.getGrade() - 1);
                    em.flush();
                }
            }
        } else {

        }
    }
    
    private void createGrade(Integer grade) {
        List<RoomType> ls = mainControllerBean.sortRoomTypeAsc();
        if (grade > ls.size()) {
            return;
        }
        ls = updateGrade1(grade, ls.size());
System.out.println("New grade is "+grade);
System.out.println(ls);
        for (int i = ls.size()-1; i >= 0; i--) {
            RoomType rt = ls.get(i);
System.out.println("rt.getGrade before is "+rt.getGrade());
            rt.setGrade(rt.getGrade()+1);
            em.flush();
System.out.println("rt.getGrade after is "+rt.getGrade());
        }
    }
    
    private List<RoomType> updateGrade1(Integer newGrade, Integer currentGrade) { //this method prunes away the access
        List<RoomType> ls = mainControllerBean.sortRoomTypeAsc();
        List<RoomType> ls1 = mainControllerBean.sortRoomTypeAsc();
        if (newGrade >= currentGrade) {
            for (int i = 0; i < ls.size(); i++) {
                System.out.println("i is "+i);
                RoomType rt1 = ls.get(i);
                if ((rt1.getGrade() < currentGrade) || (rt1.getGrade() > newGrade)) {
                    ls1.remove(rt1);
                }
            }
        } else if (newGrade < currentGrade) {
            for (int i = 0; i < ls.size(); i++) {
                RoomType rt1 = ls.get(i);
                if ((rt1.getGrade() < newGrade) || (rt1.getGrade() > currentGrade)) {
                    ls1.remove(rt1);
                }
            }
        }
        return ls1;
    }

    public void delete(Long id) throws StillInUseException {
        RoomType rt = em.find(RoomType.class, id);
        List<Room> ls = rt.getRooms();
        Integer grade = rt.getGrade();

        if (ls.isEmpty()) {
            em.remove(rt);
            List<RoomType> ls2 = mainControllerBean.sortRoomTypeAsc();
            List<RoomType> ls3 = mainControllerBean.sortRoomTypeAsc();
            for (int i = 0; i < ls2.size(); i++) {
                RoomType rt2 = ls2.get(i);
                if (rt2.getGrade() <= grade) {
                    ls3.remove(rt2);
                }
            }
            for (RoomType rt2 : ls3) {
                rt2.setGrade(rt2.getGrade() - 1);
                em.flush();
            }
        } else {
            rt.setIsEnabled(false);
            throw new StillInUseException();
        }
    }

    public RoomType retrieveRoomType(String name) throws RoomTypeNotFoundException {
        Query q = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :inName");
        q.setParameter("inName", name);
        try {
            return (RoomType) q.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("Room type " + name + " not available!");
        }
    }
}
