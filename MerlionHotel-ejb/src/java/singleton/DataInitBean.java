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
        Employee e = new Employee("sys", "pass", type, "name");
        Partner p = new Partner("partner", "pass");
        Guest g = new Guest("guest", "02020", "DDJD");
        RegisteredGuest rg = new RegisteredGuest("registered", "pass", "0811", "passport");
        Employee om = new Employee("om", "pass", EmployeeTypeEnum.OperationManager, "om");
        Employee sales = new Employee("sales", "pass", EmployeeTypeEnum.SalesManager, "sales");
        Employee gr = new Employee("gr", "pass", EmployeeTypeEnum.GuestRelationOfficer, "gr");
        em.persist(e);
        em.persist(p);
        em.persist(g);
        em.persist(rg);
        em.persist(om);
        em.persist(sales);
        em.persist(gr);
        RoomType deluxe = new RoomType("Deluxe Room", "The standard room for everyone", 32, "Queen", 2, "Shower, AC, Wi-Fi", 5, 5);
        RoomType premiere = new RoomType("Premiere Room", "For business travellers", 32, "King", 2, "Shower, AC, Wi-Fi", 4, 5);
        RoomType family = new RoomType("Family Room", "For families with kids", 60, "1 Queen 2 Twins", 4, "Shower, AC, Wi-Fi", 3, 5);
        RoomType junior = new RoomType("Junior Suite", "Larger room", 75, "1 King 1 Queen", 4, "Shower, AC, Wi-Fi", 2, 5);
        RoomType grand = new RoomType("Grand Suite", "Larger room", 100, "1 King 1 Queen", 4, "Shower, AC, Wi-Fi", 1, 5);
        em.persist(deluxe);
        em.persist(premiere);
        em.persist(family);
        em.persist(junior);
        em.persist(grand);
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
//        Room r27 = new Room(27, "Available", rt2);
//        Room r28 = new Room(28, "Available", rt2);
//        Room r29 = new Room(29, "Available", rt2);
//        Room r30 = new Room(30, "Available", rt2);
//        Room r31 = new Room(31, "Available", rt2);
//        Room r32 = new Room(32, "Available", rt2);
//        Room r33 = new Room(33, "Available", rt2);
//        Room r34 = new Room(34, "Available", rt2);
//        Room r35 = new Room(35, "Available", rt2);
//        Room r36 = new Room(36, "Available", rt2);
//        Room r37 = new Room(37, "Available", rt2);
//        Room r38 = new Room(38, "Available", rt2);
//        Room r39 = new Room(39, "Available", rt2);
//        Room r40 = new Room(40, "Available", rt2);
//        Room r41 = new Room(41, "Available", rt2);
//        Room r42 = new Room(42, "Available", rt2);
//        Room r43 = new Room(43, "Available", rt2);
//        Room r44 = new Room(44, "Available", rt2);
//        Room r45 = new Room(45, "Available", rt2);
//        Room r46 = new Room(46, "Available", rt2);
//        Room r47 = new Room(47, "Available", rt2);
//        Room r48 = new Room(48, "Available", rt2);
//        Room r49 = new Room(49, "Available", rt2);
//        Room r50 = new Room(50, "Available", rt2);
//        Room r51 = new Room(51, "Available", rt2);
//        Room r52 = new Room(52, "Available", rt2);
//        Room r53 = new Room(53, "Available", rt2);
//        Room r54 = new Room(54, "Available", rt2);
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
//        em.persist(r27);
//        em.persist(r28);
//        em.persist(r29);
//        em.persist(r30);
//        em.persist(r31);
//        em.persist(r32);
//        em.persist(r33);
//        em.persist(r34);
//        em.persist(r35);
//        em.persist(r36);
//        em.persist(r37);
//        em.persist(r38);
//        em.persist(r39);
//        em.persist(r40);
//        em.persist(r41);
//        em.persist(r42);
//        em.persist(r43);
//        em.persist(r44);
//        em.persist(r45);
//        em.persist(r46);
//        em.persist(r47);
//        em.persist(r48);
//        em.persist(r49);
//        em.persist(r50);
//        em.persist(r51);
//        em.persist(r52);
//        em.persist(r53);
//        em.persist(r54);
//        Room r5 = new Room(5, "Available", rt1);
//        Room r6 = new Room(6, "Available", rt1);
//        Room r7 = new Room(7, "Available", rt1);
//        Room r8 = new Room(8, "Available", rt1);
//        Room r9 = new Room(9, "Available", rt1);
//        Room r10 = new Room(10, "Available", rt1);
//        Room r11 = new Room(11, "Available", rt1);
//        Room r12 = new Room(12, "Available", rt1);
//        Room r13 = new Room(13, "Available", rt1);
//        Room r14 = new Room(14, "Available", rt1);
//        Room r15 = new Room(15, "Available", rt1);
//        Room r16 = new Room(16, "Available", rt1);
//        Room r17 = new Room(17, "Available", rt1);
//        Room r18 = new Room(18, "Available", rt1);
//        Room r19 = new Room(19, "Available", rt1);
//        Room r20 = new Room(20, "Available", rt1);
//        Room r21 = new Room(21, "Available", rt1);
//        Room r22 = new Room(22, "Available", rt1);
//        Room r23 = new Room(23, "Available", rt1);
//        Room r24 = new Room(24, "Available", rt1);
//        Room r25 = new Room(25, "Available", rt1);
//        Room r26 = new Room(26, "Available", rt1);
//        Room r27 = new Room(27, "Available", rt1);
//        Room r28 = new Room(28, "Available", rt1);
//        Room r29 = new Room(29, "Available", rt1);
//        Room r30 = new Room(30, "Available", rt1);
//        Room r31 = new Room(31, "Available", rt1);
//        Room r32 = new Room(32, "Available", rt1);
//        Room r33 = new Room(33, "Available", rt1);
//        Room r34 = new Room(34, "Available", rt1);
//        Room r35 = new Room(35, "Available", rt1);
//        Room r36 = new Room(36, "Available", rt1);
//        Room r37 = new Room(37, "Available", rt1);
//        Room r38 = new Room(38, "Available", rt1);
//        Room r39 = new Room(39, "Available", rt1);
//        Room r40 = new Room(40, "Available", rt1);
//        Room r41 = new Room(41, "Available", rt1);
//        Room r42 = new Room(42, "Available", rt1);
//        Room r43 = new Room(43, "Available", rt1);
//        Room r44 = new Room(44, "Available", rt1);
//        Room r45 = new Room(45, "Available", rt1);
//        Room r46 = new Room(46, "Available", rt1);
//        Room r47 = new Room(47, "Available", rt1);
//        Room r48 = new Room(48, "Available", rt1);
//        Room r49 = new Room(49, "Available", rt1);
//        Room r50 = new Room(50, "Available", rt1);
//        Room r51 = new Room(51, "Available", rt1);
//        Room r52 = new Room(52, "Available", rt1);
//        Room r53 = new Room(53, "Available", rt1);
//        Room r54 = new Room(54, "Available", rt1);
//        em.persist(r5);
//        em.persist(r6);
//        em.persist(r7);
//        em.persist(r8);
//        em.persist(r9);
//        em.persist(r10);
//        em.persist(r11);
//        em.persist(r12);
//        em.persist(r13);
//        em.persist(r14);
//        em.persist(r15);
//        em.persist(r16);
//        em.persist(r17);
//        em.persist(r18);
//        em.persist(r19);
//        em.persist(r20);
//        em.persist(r21);
//        em.persist(r22);
//        em.persist(r23);
//        em.persist(r24);
//        em.persist(r25);
//        em.persist(r26);
//        em.persist(r27);
//        em.persist(r28);
//        em.persist(r29);
//        em.persist(r30);
//        em.persist(r31);
//        em.persist(r32);
//        em.persist(r33);
//        em.persist(r34);
//        em.persist(r35);
//        em.persist(r36);
//        em.persist(r37);
//        em.persist(r38);
//        em.persist(r39);
//        em.persist(r40);
//        em.persist(r41);
//        em.persist(r42);
//        em.persist(r43);
//        em.persist(r44);
//        em.persist(r45);
//        em.persist(r46);
//        em.persist(r47);
//        em.persist(r48);
//        em.persist(r49);
//        em.persist(r50);
//        em.persist(r51);
//        em.persist(r52);
//        em.persist(r53);
//        em.persist(r54);
        
        em.flush();
        System.out.println("New SysAdmin and Partner and 2 room types with 100 and 50 rooms avail created with Username: 1 and Password: 1 and SysAdminID = " + e.getId());
    }

}
