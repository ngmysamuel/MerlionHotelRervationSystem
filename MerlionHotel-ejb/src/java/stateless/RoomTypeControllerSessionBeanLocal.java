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
import util.exception.StillInUseException;

/**
 *
 * @author samue
 */
@Local
public interface RoomTypeControllerSessionBeanLocal {
    public Boolean editAndCreateRoomInventoryIfNecessary(RoomType rt, LocalDate date, Integer numOfRooms) throws ReservationNotFoundException;
    public List<RoomType> getRoomTypes();
    public void create(String bed, String name, String amenities, int capacity, String description, int grade, int roomSize);
    public void update(String bed, String name, String amenities, String capacity, String description, String grade, String roomSize, int initialRoomAvail, Long roomTypeId, String b);
    public void delete(Long id) throws StillInUseException;
}
