/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singleton;

import entity.Employee;
import Enum.EmployeeTypeEnum;
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
        em.persist(e);
        em.flush();
        System.out.println("New SysAdmin created with Username: 1 and Password: 1 and ID = "+e.getId());
    }
}
