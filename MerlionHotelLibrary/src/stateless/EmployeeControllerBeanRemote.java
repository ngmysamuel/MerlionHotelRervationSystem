/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import Enum.EmployeeTypeEnum;
import javax.ejb.Remote;

/**
 *
 * @author samue
 */
@Remote
public interface EmployeeControllerBeanRemote {
    public Long getIdAndAddOne(Long id);
    
}
