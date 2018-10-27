/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author samue
 */
@Local
public interface PartnerControllerBeanLocal {
    public Partner create(String emp, String manager, String username);
    public List<Partner> viewAll();
}
