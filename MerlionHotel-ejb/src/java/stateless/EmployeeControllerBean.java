/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Employee;
import Enum.EmployeeTypeEnum;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
    public Employee create(String username, String password, EmployeeTypeEnum employeeTypeEnum) {
        Employee emp = new Employee(username, password, employeeTypeEnum, "name");
        em.persist(emp);
        em.flush();
        return emp;
    }
    
    public List<Employee> viewAll() {
        Query q = em.createQuery("SELECT e FROM Employee e");
        return q.getResultList();
    }
}
