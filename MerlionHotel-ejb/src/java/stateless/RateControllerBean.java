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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.RateNameNotUniqueException;
import util.exception.RateNotFoundException;

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
        Query q = em.createQuery("SELECT r FROM Rate r WHERE r.type = :inType AND r.status <> 'disabled'");
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

        q = em.createQuery("SELECT r FROM Rate r WHERE r.type = :inType AND r.dateStart <= :inDate AND r.dateEnd > :inDate AND r.status <> 'disabled'");
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
        
        
        q = em.createQuery("SELECT r FROM Rate r WHERE r.type = :inType AND r.dateStart <= :inDate AND r.dateEnd > :inDate AND r.status <> 'disabled'");
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
    public Rate retrieveRate(String name) throws RateNotFoundException{
        Query q = em.createQuery("SELECT r FROM Rate r WHERE r.name = :inName");
        q.setParameter("inName", name);
        try{
            Rate r = (Rate) q.getSingleResult();
            if("disabled".equals(r.getStatus())){
                throw new RateNotFoundException(name + " rate doesn't exist");
            }
            return r;
        } catch(NoResultException ex){
            throw new RateNotFoundException(name + " rate doesn't exist");
        }
    }
    
    public Rate retrieveDisabledRate(String name) throws RateNotFoundException{
        Query q = em.createQuery("SELECT r FROM Rate r WHERE r.name = :inName");
        q.setParameter("inName", name);
        try{
            Rate r = (Rate) q.getSingleResult();
            if(!"disabled".equals(r.getStatus())){
                throw new RateNotFoundException(name + " rate doesn't exist");
            }
            return r;
        } catch(NoResultException ex){
            throw new RateNotFoundException(name + " rate doesn't exist");
        }
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

    @Override
    public Rate createRate(String name, RoomType roomType, RateTypeEnum rateType, BigDecimal price){
        try{
            Rate r = retrieveDisabledRate(name);
            r.setRoomType(roomType);
            r.setType(rateType);
            r.setPrice(price);
            r.setStatus("active");
            em.flush();
            return r;
        } catch(RateNotFoundException ex){
            Rate rate = new Rate(rateType, roomType, name, price);
            roomType.getRates().add(rate);
            em.persist(rate);
            em.flush();
            return rate;
        }      
    }
    
    @Override
    public Rate createRate(String name, RoomType roomType, RateTypeEnum rateType, BigDecimal price, LocalDate dateStart, LocalDate dateEnd){
        try{
            Rate r = retrieveDisabledRate(name);
            r.setRoomType(roomType);
            r.setType(rateType);
            r.setPrice(price);
            r.setDateStart(dateStart);
            r.setDateEnd(dateEnd);
            r.setStatus("active");
            em.flush();
            return r;
        } catch(RateNotFoundException ex){
            Rate rate = new Rate(rateType, roomType, name, price, dateStart, dateEnd);
            roomType.getRates().add(rate);
            em.persist(rate);
            em.flush();
            return rate;
        }
    }

    @Override
    public void updateRate(Long rateId, String rateName, RateTypeEnum rateType, BigDecimal price, LocalDate dateStart, LocalDate dateEnd, RoomType roomType)
    throws RateNameNotUniqueException{
        try{
            Rate r = em.find(Rate.class, rateId);
            r.setName(rateName);
            r.setPrice(price);
            r.setType(rateType);
            r.setDateEnd(dateEnd);
            r.setDateStart(dateStart);
            r.setRoomType(roomType);
            em.flush();
        } catch(PersistenceException ex){
            throw new RateNameNotUniqueException("Name has been used!");
        }
    }
    
    @Override
    public void updateRate(Long rateId, String rateName, RateTypeEnum rateType, BigDecimal price, RoomType roomType) throws RateNameNotUniqueException{
        try{
            Rate r = em.find(Rate.class, rateId);
            r.setName(rateName);
            r.setPrice(price);
            r.setType(rateType);
            r.setRoomType(roomType);
        } catch(PersistenceException ex){
            throw new RateNameNotUniqueException("Name has been used!");
        }
    }
    
    @Override
    public void deleteRate(Long rateId){
        Rate r = em.find(Rate.class, rateId);
        r.setStatus("disabled");
    }
    
    @Override
    public List<Rate> retrieveAllRates() throws RateNotFoundException{
        try{
            Query q = em.createQuery("SELECT r FROM Rate r WHERE r.status <> 'disabled'");
            List<Rate> rates =  q.getResultList();
            for(Rate r: rates){
                r.getId();
                r.getRoomType().getName();
            }
            return rates;
        } catch(NullPointerException ex){
            throw new RateNotFoundException("No rates found!");
        }
    }
}
