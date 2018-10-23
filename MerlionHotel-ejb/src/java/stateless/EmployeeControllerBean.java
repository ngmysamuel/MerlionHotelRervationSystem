/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import Entity.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author samue
 */
@Stateless
public class EmployeeControllerBean implements EmployeeControllerBeanRemote, EmployeeControllerBeanLocal {

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;
    
    public Long getIdAndAddOne(Long id) {
        Employee emp1 = em.find(Employee.class, id);
        return emp1.getId()+1;
    }

    public Long create() {
        Employee emp = new Employee();
        em.persist(emp);
        em.flush();
        return emp.getId();
    }
}
