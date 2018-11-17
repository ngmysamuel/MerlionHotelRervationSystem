/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singleton;

import entity.Employee;
import Enum.EmployeeTypeEnum;
import Enum.RateTypeEnum;
import entity.Guest;
import entity.Partner;
import java.util.ArrayList;
import entity.Rate;
import entity.RegisteredGuest;
import entity.Room;
import entity.RoomType;
import java.math.BigDecimal;
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

        EmployeeTypeEnum type = EmployeeTypeEnum.SystemAdministrator;
        Employee e = new Employee("sysadmin", "pass", type, "name");
        Partner p = new Partner("1", "1");
        Guest g = new Guest("guest", "02020", "DDJD");
        RegisteredGuest rg = new RegisteredGuest("registered", "pass", "0811", "passport");
        Employee om = new Employee("opsmanager", "pass", EmployeeTypeEnum.OperationManager, "om");
        Employee sales = new Employee("sales", "pass", EmployeeTypeEnum.SalesManager, "sales");
        Employee gr = new Employee("gr", "pass", EmployeeTypeEnum.GuestRelationOfficer, "gr");

        RoomType deluxe = new RoomType("Deluxe Room", "The standard room for everyone", 32, "Queen", 2, "Shower, AC, Wi-Fi", 5, 5);
        deluxe.setIsEnabled(true);
        RoomType premiere = new RoomType("Premiere Room", "For business travellers", 32, "King", 2, "Shower, AC, Wi-Fi", 4, 5);
        premiere.setIsEnabled(true);
        RoomType family = new RoomType("Family Room", "For families with kids", 60, "1 Queen 2 Twins", 4, "Shower, AC, Wi-Fi", 3, 5);
        family.setIsEnabled(true);
        RoomType junior = new RoomType("Junior Suite", "Larger room", 75, "1 King 1 Queen", 4, "Shower, AC, Wi-Fi", 2, 5);
        junior.setIsEnabled(true);
        RoomType grand = new RoomType("Grand Suite", "Larger room", 100, "1 King 1 Queen", 4, "Shower, AC, Wi-Fi", 1, 5);
        grand.setIsEnabled(true);

        Room r1 = new Room(100, "Available", deluxe);
        Room r2 = new Room(101, "Available", deluxe);
        Room r3 = new Room(102, "Available", deluxe);
        Room r5 = new Room(5, "Available", deluxe);
        Room r6 = new Room(6, "Available", deluxe);

        Room r7 = new Room(7, "Available", premiere);
        Room r8 = new Room(8, "Available", premiere);
        Room r9 = new Room(9, "Available", premiere);
        Room r10 = new Room(10, "Available", premiere);
        Room r11 = new Room(11, "Available", premiere);

        Room r12 = new Room(12, "Available", family);
        Room r13 = new Room(13, "Available", family);
        Room r14 = new Room(14, "Available", family);
        Room r15 = new Room(15, "Available", family);
        Room r16 = new Room(16, "Available", family);

        Room r17 = new Room(17, "Available", junior);
        Room r18 = new Room(18, "Available", junior);
        Room r19 = new Room(19, "Available", junior);
        Room r20 = new Room(20, "Available", junior);
        Room r21 = new Room(21, "Available", junior);

        Room r22 = new Room(22, "Available", grand);
        Room r23 = new Room(23, "Available", grand);
        Room r24 = new Room(24, "Available", grand);
        Room r25 = new Room(25, "Available", grand);
        Room r26 = new Room(26, "Available", grand);

        List<Room> ls1 = new ArrayList<>();
        ls1.add(r1);
        ls1.add(r2);
        ls1.add(r3);
        ls1.add(r5);
        ls1.add(r6);
        deluxe.setRooms(ls1);
        ls1.clear();
        ls1.add(r7);
        ls1.add(r8);
        ls1.add(r9);
        ls1.add(r10);
        ls1.add(r11);
        premiere.setRooms(ls1);
        ls1.clear();
        ls1.add(r12);
        ls1.add(r13);
        ls1.add(r14);
        ls1.add(r15);
        ls1.add(r16);
        family.setRooms(ls1);
        ls1.clear();
        ls1.add(r17);
        ls1.add(r18);
        ls1.add(r19);
        ls1.add(r20);
        ls1.add(r21);
        junior.setRooms(ls1);
        ls1.clear();
        ls1.add(r22);
        ls1.add(r23);
        ls1.add(r24);
        ls1.add(r25);
        ls1.add(r26);
        grand.setRooms(ls1);

        em.persist(e);
        em.persist(p);
        em.persist(g);
        em.persist(rg);
        em.persist(om);
        em.persist(sales);
        em.persist(gr);

        em.persist(deluxe);
        em.persist(premiere);
        em.persist(family);
        em.persist(junior);
        em.persist(grand);

        em.persist(r1);
        em.persist(r2);
        em.persist(r3);
        em.persist(r5);
        em.persist(r6);
        em.persist(r7);
        em.persist(r8);
        em.persist(r9);
        em.persist(r10);
        em.persist(r11);
        em.persist(r12);
        em.persist(r13);
        em.persist(r14);
        em.persist(r15);
        em.persist(r16);
        em.persist(r17);
        em.persist(r18);
        em.persist(r19);
        em.persist(r20);
        em.persist(r21);
        em.persist(r22);
        em.persist(r23);
        em.persist(r24);
        em.persist(r25);
        em.persist(r26);
        
        Rate dn = new Rate(RateTypeEnum.Normal, deluxe, "Deluxe Normal", new BigDecimal(50));
        Rate dp = new Rate(RateTypeEnum.Published, deluxe, "Deluxe Published", new BigDecimal(65));
        Rate pn = new Rate(RateTypeEnum.Normal, premiere, "Premiere Normal", new BigDecimal(70));
        Rate pp = new Rate(RateTypeEnum.Published, premiere, "Premiere Published", new BigDecimal(90));
        Rate fn = new Rate(RateTypeEnum.Normal, family, "Family Normal", new BigDecimal(110));
        Rate fp = new Rate(RateTypeEnum.Published, family, "Family Published", new BigDecimal(135));
        Rate jn = new Rate(RateTypeEnum.Normal, junior, "Junior Normal", new BigDecimal(150));
        Rate jp = new Rate(RateTypeEnum.Published, junior, "Junior Published", new BigDecimal(180));
        Rate gn = new Rate(RateTypeEnum.Normal, grand, "Grand Normal", new BigDecimal(200));
        Rate gp = new Rate(RateTypeEnum.Published, grand, "Grand Published", new BigDecimal(250));
        
        List<Rate> lsRate = new ArrayList<>();
        lsRate.add(dn);
        lsRate.add(dp);
        deluxe.setRates(lsRate);
        lsRate.clear();
        lsRate.add(pn);
        lsRate.add(pp);
        premiere.setRates(lsRate);
        lsRate.clear();
        lsRate.add(fn);
        lsRate.add(fp);
        family.setRates(lsRate);
        lsRate.clear();
        lsRate.add(jn);
        lsRate.add(jp);
        junior.setRates(lsRate);
        lsRate.clear();
        lsRate.add(gn);
        lsRate.add(gp);
        grand.setRates(lsRate);
        
        
        em.persist(dn);
        em.persist(dp);
        em.persist(pn);
        em.persist(pp);
        em.persist(fn);
        em.persist(fp);
        em.persist(jn);
        em.persist(jp);
        em.persist(gn);
        em.persist(gp);

        em.flush();
    }

}
