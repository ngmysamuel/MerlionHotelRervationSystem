/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Employee;
import Enum.EmployeeTypeEnum;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author samue
 */
@Local
public interface EmployeeControllerBeanLocal {
    public Employee create(String username, String password, EmployeeTypeEnum employeeTypeEnum);
    public List<Employee> viewAll();
}
