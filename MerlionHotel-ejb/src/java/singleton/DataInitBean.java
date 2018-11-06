/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singleton;

import entity.Employee;
import Enum.EmployeeTypeEnum;
import entity.Partner;
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
        System.out.println("\n\nhwwkjlelkjejlkekjlewrkjl    "+l.equals(0L)+"         afnjekljefkjefwwefkjfewkj\n\n");
        if (l.equals(0L)) {
            initApplication();
        }
    }
    public void initApplication() {
        EmployeeTypeEnum type = EmployeeTypeEnum.SystemAdministrator;
        Employee e = new Employee("1", "1", type, "name");
        Partner p = new Partner("1", "1");
        RoomType rt = new RoomType("Super", "lala", 200, "Big", 4, "Toilets", 1, 100);
        RoomType rt2 = new RoomType("Max", "lala2", 200, "Med", 4, "Toilet", 2, 50);
        em.persist(rt);
        em.persist(rt2);
        em.persist(e);
        em.persist(p);
        em.flush();
        System.out.println("New SysAdmin and Partner and 2 room types with 100 and 50 rooms avail created with Username: 1 and Password: 1 and SysAdminID = "+e.getId());
    }
    
    
}
