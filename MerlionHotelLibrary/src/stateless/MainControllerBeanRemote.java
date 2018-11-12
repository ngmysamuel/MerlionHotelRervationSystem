/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Employee;
import Enum.EmployeeTypeEnum;
import entity.ExceptionReport;
import entity.Partner;
import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.StillInUseException;

/**
 *
 * @author samue
 */
@Remote
public interface MainControllerBeanRemote {
    public boolean doLogin(String username, String password);
    public EmployeeTypeEnum getEmployeeTypeEnum(String username);
    public Employee createEmployee(String username, String password, EmployeeTypeEnum employeeType);
    public List<Employee> viewEmployees();
    public Partner createPartner(String emp, String manager, String username);
    public List<Partner> viewPartners();
    public void timer();
    public ExceptionReport viewExceptionReport(LocalDate date);
    public void persistEr();
    public List<RoomType> viewAllRoomTypes();
    public RoomType viewSpecificRoomType(String name);
    public void createRoomType(String bed, String name, String amenities, int capacity, String description, int grade, int roomSize);
    public void updateRoomType(String bed, String name, String amenities, String capacity, String description, String grade, String roomSize, int initialRoomAvail, Long roomTypeId);
    public void updateRomType(int num, Long id);
    public void deleteRoomType(Long id) throws StillInUseException ;
    public void createRoom(Integer roomNum, String status, Long roomTypeId);
    public void updateRoom(Long roomNum, String status, Long roomTypeId);
    public void deleteRoom(Long roomNum) throws StillInUseException;
    public List<Room> viewRooms();
}
