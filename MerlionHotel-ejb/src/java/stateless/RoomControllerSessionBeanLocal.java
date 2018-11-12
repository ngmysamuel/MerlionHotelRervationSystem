/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Local;
import util.exception.StillInUseException;

/**
 *
 * @author samue
 */
@Local
public interface RoomControllerSessionBeanLocal {
    public void create(Integer roomNum, String status, Long roomTypeId);
    public void update(Long roomNum, String status, Long roomTypeId);
    public List<Room> viewAllRooms();
    public void deleteRoom(Long id) throws StillInUseException;
}
