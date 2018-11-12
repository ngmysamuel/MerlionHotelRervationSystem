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
import entity.Room;
import entity.RoomType;
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
        Employee e = new Employee("1", "1", type, "name");
        Partner p = new Partner("1", "1");
        Guest g = new Guest("blabla", "02020", "DDJD");
//        RoomType rt1 = new RoomType("Super", "lala", 200, "Big", 4, "Toilets", 1, 50);
//        RoomType rt2 = new RoomType("Max", "lala2", 200, "Med", 4, "Toilet", 2, 50);
//        em.persist(rt1);
//        em.persist(rt2);
//        Room r1 = new Room(100, "Available", rt2);
//        Room r2 = new Room(101, "Available", rt2);
//        Room r3 = new Room(102, "Available", rt2);
//        Room r5 = new Room(5, "Available", rt2);
//        Room r6 = new Room(6, "Available", rt2);
//        Room r7 = new Room(7, "Available", rt2);
//        Room r8 = new Room(8, "Available", rt2);
//        Room r9 = new Room(9, "Available", rt2);
//        Room r10 = new Room(10, "Available", rt2);
//        Room r11 = new Room(11, "Available", rt2);
//        Room r12 = new Room(12, "Available", rt2);
//        Room r13 = new Room(13, "Available", rt2);
//        Room r14 = new Room(14, "Available", rt2);
//        Room r15 = new Room(15, "Available", rt2);
//        Room r16 = new Room(16, "Available", rt2);
//        Room r17 = new Room(17, "Available", rt2);
//        Room r18 = new Room(18, "Available", rt2);
//        Room r19 = new Room(19, "Available", rt2);
//        Room r20 = new Room(20, "Available", rt2);
//        Room r21 = new Room(21, "Available", rt2);
//        Room r22 = new Room(22, "Available", rt2);
//        Room r23 = new Room(23, "Available", rt2);
//        Room r24 = new Room(24, "Available", rt2);
//        Room r25 = new Room(25, "Available", rt2);
//        Room r26 = new Room(26, "Available", rt2);
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
//        em.persist(r1);
//        em.persist(r2);
//        em.persist(r3);
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
        em.persist(e);
        em.persist(p);
        em.persist(g);
        em.flush();
        System.out.println("New SysAdmin and Partner and 2 room types with 100 and 50 rooms avail created with Username: 1 and Password: 1 and SysAdminID = " + e.getId());
    }

}
