/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import Enum.RateTypeEnum;
import entity.Rate;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.RateNameNotUniqueException;
import util.exception.RateNotFoundException;

/**
 *
 * @author Asus
 */
@Local
public interface RateControllerBeanLocal {

    Rate retrieveRate(LocalDate date, RoomType roomType);

    BigDecimal countRate(LocalDate dateStart, LocalDate dateEnd, RoomType roomType);

    public Rate createRate(String name, RoomType roomType, RateTypeEnum rateType, BigDecimal price);

    public Rate createRate(String name, RoomType roomType, RateTypeEnum rateType, BigDecimal price, LocalDate dateStart, LocalDate dateEnd);

    public Rate retrieveRate(String name) throws RateNotFoundException;

    public void updateRate(Long rateId, String rateName, RateTypeEnum rateType, BigDecimal price, LocalDate dateStart, LocalDate dateEnd, RoomType roomType)throws RateNameNotUniqueException;

    public void deleteRate(Long rateId);

    public void updateRate(Long rateId, String rateName, RateTypeEnum rateType, BigDecimal price, RoomType roomType)throws RateNameNotUniqueException;

    public List<Rate> retrieveAllRates() throws RateNotFoundException;


    
}
