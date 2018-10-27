/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;


import Enum.EmployeeTypeEnum;
import entity.Employee;
import entity.Partner;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author samue
 */

@Stateless
public class MainControllerBean implements MainControllerBeanRemote, MainControllerBeanLocal {

    @EJB
    private PartnerControllerBeanLocal partnerControllerBean;

    @EJB
    private EmployeeControllerBeanLocal employeeControllerBean;

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;
    
    public boolean doLogin(String username, String password) {
        Query q = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
        q.setParameter("username", username);
        List<Employee> ls = q.getResultList();
        if (ls.isEmpty()) {
            return false;
        } 
        Employee e = ls.get(0);
        if (e.getPassword().equals(password)){
            return true;
        }
        return false;
    }

    public EmployeeTypeEnum getEmployeeTypeEnum(String username) {
        System.out.println("I have been clled to get the enum");
        Query q = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
        System.out.println("query created");
        q.setParameter("username", username);
        System.out.println("Will try to get result list");
        List<Employee> ls = q.getResultList();
        System.out.println("got ersult list");
        Employee e = ls.get(0);
        System.out.println("we got the first index");
        return e.getEmployeeType();
    }
    
    public Employee createEmployee(String username, String password, EmployeeTypeEnum employeeType) {
        return employeeControllerBean.create(username, password, employeeType);
    }
    
    public List<Employee> viewEmployees() {
        return employeeControllerBean.viewAll();
    }
    
    public Partner createPartner(String emp, String manager, String username) {
        System.out.println("main controller bean is called");
        return partnerControllerBean.create(emp, manager, username);
    }

    public List<Partner> viewPartners() {
        return partnerControllerBean.viewAll();
    }
}
