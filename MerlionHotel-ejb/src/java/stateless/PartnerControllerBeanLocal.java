/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Partner;
import entity.Reservation;
import entity.ReservationLineItem;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author samue
 */
@Local
public interface PartnerControllerBeanLocal {
    public Partner create(String emp, String manager, String username);
    public List<Partner> viewAll();
    public boolean login(String username, String password);
    public List<Reservation> viewAllReservations();
    public Reservation viewReservationDetails(Long id) throws ReservationNotFoundException;
    public Long createReservation(LocalDate dateStart, LocalDate dateEnd, java.lang.Long guestId, java.lang.Long partnerId, List<ReservationLineItem> rooms) throws ReservationNotFoundException;
    public List<Boolean> searchRooms(LocalDate dateStart, LocalDate dateEnd);

}
