/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Room;
import entity.RoomInventory;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.StillInUseException;

/**
 *
 * @author samue
 */
@Local
public interface RoomControllerSessionBeanLocal {
    public boolean create(Integer roomNum, String status, Long roomTypeId);
    public void update(Long roomNum, String status, Long roomTypeId);
    public List<Room> viewAllRooms();
    public void deleteRoom(Long id) throws StillInUseException;
    public Boolean allocateRooms(RoomType rt, Integer numOfRooms, LocalDate dateStart, LocalDate dateEnd, Long id);
    public boolean setRoomsAllocated(Long id, RoomType rt);
}
