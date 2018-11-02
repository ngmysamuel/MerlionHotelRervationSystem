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

/**
 *
 * @author samue
 */
@Local
public interface RoomTypeControllerSessionBeanLocal {
    public void editAndCreateRoomInventoryIfNecessary(RoomType rt, LocalDate date, Integer numOfRooms);
    public List<RoomType> getRoomTypes();
}
