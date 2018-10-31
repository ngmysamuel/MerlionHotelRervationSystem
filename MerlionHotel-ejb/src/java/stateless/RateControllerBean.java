/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Rate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Asus
 */
@Stateless
public class RateControllerBean implements RateControllerBeanRemote, RateControllerBeanLocal {

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;


    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Rate retrieveRate(LocalDate date) {
        Query q = em.createQuery("SELECT r FROM Rate r WHERE r.type = :inType");
        q.setParameter("inDate", date);
        q.setParameter("inType", "Normal");
        List<Rate> normalRates;
        normalRates = q.getResultList();
        Rate normal = normalRates.get(0);
        for(Rate rate: normalRates){
            if(normal != null && -1 == rate.getPrice().compareTo(normal.getPrice())){
                normal = rate;
            }
        }

        q = em.createQuery("SELECT r FROM Rate r WHERE r.type = :inType AND r.dateStart <= :inDate AND r.dateEnd > :inDate");
        q.setParameter("inDate", date);
        q.setParameter("inType", "Promotion");
        List<Rate> promoRates;
        promoRates = q.getResultList();
        Rate promo = promoRates.get(0);
        for(Rate rate: promoRates){
            if(promo != null && -1 == rate.getPrice().compareTo(promo.getPrice())){
                promo = rate;
            }
        }
        
        
        q = em.createQuery("SELECT r FROM Rate r WHERE r.type = :inType AND r.dateStart <= :inDate AND r.dateEnd > :inDate");
        q.setParameter("inDate", date);
        q.setParameter("inType", "Peak");
        List<Rate> peakRates;
        peakRates = q.getResultList();
        Rate peak = peakRates.get(0);
        for(Rate rate: peakRates){
            if(peak != null && rate.getPrice().compareTo(peak.getPrice()) == -1){
                peak = rate;
            }
        }
        
        if(normal == null){
            if(peak != null){
                return peak;
            } else if(promo != null){
                return promo;
            }
        } else if (promo != null){
            return promo;
        } else if (peak != null){
            return peak;
        } else {
            return normal;
        }
        return null;
    }

    @Override
    public BigDecimal countRate(LocalDate dateStart, LocalDate dateEnd) {
        BigDecimal total = new BigDecimal(0);
        while(dateStart.isBefore(dateEnd)){
            total.add(retrieveRate(dateStart).getPrice());
            dateStart.plusDays(1);
        }
        return total;
    }

    

    
}
