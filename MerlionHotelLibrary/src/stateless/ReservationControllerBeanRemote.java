/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import Enum.ReservationTypeEnum;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.ReservationLineItem;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Asus
 */
@Remote
public interface ReservationControllerBeanRemote {

    Reservation retrieveGuestReservationDetails(String guestEmail, LocalDate dateStart, LocalDate dateEnd) throws ReservationNotFoundException;

    Reservation createGuestReservation(LocalDate dateStart, LocalDate dateEnd, ReservationTypeEnum type, Guest guest, List<ReservationLineItem> rooms);

    Reservation retrievePartnerReservationDetails(String partner, LocalDate dateStart, LocalDate dateEnd) throws ReservationNotFoundException;
    
    Reservation createPartnerReservation(LocalDate dateStart, LocalDate dateEnd, entity.Guest guest, entity.Partner partner, List<ReservationLineItem> rooms) throws ReservationNotFoundException;
}
