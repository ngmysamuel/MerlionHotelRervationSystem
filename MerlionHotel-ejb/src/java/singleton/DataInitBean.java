/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singleton;

import entity.Employee;
import Enum.EmployeeTypeEnum;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author samue
 */
@Singleton
@LocalBean
@Startup
public class DataInitBean {

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    @PostConstruct
    public void postConstruct() {
        Query q = em.createQuery("SELECT COUNT(e) FROM Employee e");
        List<Object> ls = q.getResultList();
        Long l = (Long) ls.get(0);
        System.out.println(l);
        System.out.println("\n\nhwwkjlelkjejlkekjlewrkjl    " + l.equals(0L) + "         afnjekljefkjefwwefkjfewkj\n\n");
        if (l.equals(0L)) {
            initApplication();
        }
    }

    public void initApplication() {
        EmployeeTypeEnum type1 = EmployeeTypeEnum.SystemAdministrator;
        EmployeeTypeEnum type2 = EmployeeTypeEnum.OperationManager;
        Employee e1 = new Employee("1", "1", type1, "name");
        Employee e2 = new Employee("2", "2", type2, "john");
        Partner p = new Partner("1", "1");
        Guest g = new Guest("blabla", "02020", "DDJD");
        RoomType rt1 = new RoomType("Super", "Bath with big bed", 234, "Big Bed", 5, "Bath", 1);
        rt1.setInitialRoomAvailability(4);
        rt1.setIsEnabled(true);
        RoomType rt2 = new RoomType("Max", "Shower with med bed", 123, "Med", 4, "Shower", 2);
        rt2.setInitialRoomAvailability(2);
        rt2.setIsEnabled(true);
        RoomType rt3 = new RoomType("Deluxe", "Bath tub with super bed", 345, "super", 6, "Bath tub", 3);
        rt3.setInitialRoomAvailability(2);
        rt3.setIsEnabled(true);
        Room r1 = new Room(21, "Available", rt1);
        Room r2 = new Room(22, "Available", rt1);
        Room r3 = new Room(23, "Available", rt1);
        Room r4 = new Room(24, "Available", rt1);
        Room r5 = new Room(31, "Available", rt2);
        Room r6 = new Room(32, "Available", rt2);
        Room r7 = new Room(41, "Available", rt3);
        Room r8 = new Room(42, "Available", rt3);
        List<Room> ls1 = new ArrayList<>();
        ls1.add(r1);
        ls1.add(r2);
        ls1.add(r3);
        ls1.add(r4);
        rt1.setRooms(ls1);
        ls1.clear();
        ls1.add(r5);
        ls1.add(r6);
        rt2.setRooms(ls1);
        ls1.clear();
        ls1.add(r7);
        ls1.add(r8);
        rt3.setRooms(ls1);
        em.persist(e1);
        em.persist(e2);
        em.persist(p);
        em.persist(g);
        em.persist(rt1);
        em.persist(rt2);
        em.persist(rt3);
        em.persist(r1);
        em.persist(r2);
        em.persist(r3);
        em.persist(r4);
        em.persist(r5);
        em.persist(r6);
        em.persist(r7);
        em.persist(r8);
        em.flush();
    }

}
