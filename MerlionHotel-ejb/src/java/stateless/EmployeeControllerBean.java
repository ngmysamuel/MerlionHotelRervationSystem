/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Employee;
import Enum.EmployeeTypeEnum;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author samue
 */
@Stateless
public class EmployeeControllerBean implements EmployeeControllerBeanRemote, EmployeeControllerBeanLocal {

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

    ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
    Validator validator = vf.getValidator();

    public Long getIdAndAddOne(Long id) {
        Employee emp1 = em.find(Employee.class, id);
        return emp1.getId() + 1;
    }

    public Employee create(String username, String password, EmployeeTypeEnum employeeTypeEnum) {
        Employee emp = new Employee(username, password, employeeTypeEnum, "name");
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(emp);
        if (constraintViolations.size() > 0) {
            Iterator<ConstraintViolation<Employee>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<Employee> cv = iterator.next();
                System.err.println(cv.getRootBeanClass().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
                System.err.println(cv.getRootBeanClass().getSimpleName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
                return null;
            }
        } else {
            em.persist(emp);
            em.flush();
        }
        return emp;
    }

    public List<Employee> viewAll() {
        Query q = em.createQuery("SELECT e FROM Employee e");
        return q.getResultList();
    }
}
