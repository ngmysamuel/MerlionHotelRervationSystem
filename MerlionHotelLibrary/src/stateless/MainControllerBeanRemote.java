/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Employee;
import Enum.EmployeeTypeEnum;
import entity.Partner;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author samue
 */
@Remote
public interface MainControllerBeanRemote {
    public boolean doLogin(String username, String password);
    public EmployeeTypeEnum getEmployeeTypeEnum(String username);
    public Employee createEmployee(String username, String password, EmployeeTypeEnum employeeType);
    public List<Employee> viewEmployees();
    public Partner createPartner(String emp, String manager, String username);
    public List<Partner> viewPartners();
    public void timer();
}
