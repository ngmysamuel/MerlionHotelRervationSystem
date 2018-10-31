/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import Enum.ReservationTypeEnum;
import entity.Reservation;
import entity.ReservationLineItem;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Asus
 */
@Local
public interface ReservationControllerBeanLocal {

    Reservation retrieveGuestReservationDetails(Long guestId, LocalDate dateStart, LocalDate dateEnd) throws ReservationNotFoundException;

    Reservation createGuestReservation(LocalDate dateStart, LocalDate dateEnd, ReservationTypeEnum type, java.lang.Long guestId, List<ReservationLineItem> rooms);

    List<Reservation> retrieveAllReservationsForToday(LocalDate currentDate);

    Reservation retrievePartnerReservationDetails(java.lang.Long partnerId, LocalDate dateStart, LocalDate dateEnd) throws ReservationNotFoundException;

    Reservation createPartnerReservation(LocalDate dateStart, LocalDate dateEnd, java.lang.Long guestId, java.lang.Long partnerId, List<ReservationLineItem> rooms) throws ReservationNotFoundException;

    List<Reservation> retrieveAllGuestReservations(long guestId);
    
}
