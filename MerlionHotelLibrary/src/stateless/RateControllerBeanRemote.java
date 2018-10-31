/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Rate;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.ejb.Remote;

/**
 *
 * @author Asus
 */
@Remote
public interface RateControllerBeanRemote {

    Rate retrieveRate(LocalDate date);

    BigDecimal countRate(LocalDate dateStart, LocalDate dateEnd);


    
}
