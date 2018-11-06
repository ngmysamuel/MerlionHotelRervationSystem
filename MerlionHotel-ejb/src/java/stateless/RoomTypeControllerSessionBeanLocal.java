/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author samue
 */
@Local
public interface RoomTypeControllerSessionBeanLocal {
    public Boolean editAndCreateRoomInventoryIfNecessary(RoomType rt, LocalDate date, Integer numOfRooms) throws ReservationNotFoundException;
    public List<RoomType> getRoomTypes();
}
