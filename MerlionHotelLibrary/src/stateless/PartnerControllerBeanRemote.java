/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author samue
 */
@Remote
public interface PartnerControllerBeanRemote {
    public List<Boolean> searchRooms(LocalDate dateStart, LocalDate dateEnd);
}
