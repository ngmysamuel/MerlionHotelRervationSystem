/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author samue
 */
@Stateless
public class PartnerControllerBean implements PartnerControllerBeanRemote, PartnerControllerBeanLocal {

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

    public Partner create(String emp, String manager, String username) {
        System.out.println("Partner controller bean is called");
        Partner part = new Partner(emp, manager, username);
        em.persist(part);
        em.flush();
        return part;
    }

    public List<Partner> viewAll() {
        Query q = em.createQuery("SELECT p FROM Partner p");
        return q.getResultList();
    }

}
