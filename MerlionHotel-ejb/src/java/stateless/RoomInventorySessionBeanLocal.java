/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.RoomInventory;
import entity.RoomType;
import java.time.LocalDate;
import javax.ejb.Local;
import util.exception.RoomInventoryNotFound;

/**
 *
 * @author samue
 */
@Local
public interface RoomInventorySessionBeanLocal {
    public RoomInventory retrieveRoomInventory(LocalDate date, RoomType rt) throws RoomInventoryNotFound;
    public boolean isItFull(LocalDate date, RoomType roomType);

    public RoomInventory retrieveLeastRoomInventory(LocalDate dateStart, LocalDate dateEnd, RoomType rt) throws RoomInventoryNotFound;
}
