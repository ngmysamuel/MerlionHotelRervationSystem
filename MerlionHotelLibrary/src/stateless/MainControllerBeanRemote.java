/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Employee;
import Enum.EmployeeTypeEnum;
import Enum.RateTypeEnum;
import entity.ExceptionReport;
import entity.Partner;
import entity.Rate;
import entity.ReservationLineItem;
import entity.Room;
import entity.RoomInventory;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Remote;
import util.exception.NoAvailableRoomsException;
import util.exception.RateNameNotUniqueException;
import util.exception.RateNotFoundException;
import util.exception.RoomTypeNotFoundException;
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

    public Rate createRate(String roomTypeName, String name, RateTypeEnum rateType, BigDecimal price, LocalDate dateStart, LocalDate dateEnd) throws RoomTypeNotFoundException;

    public Rate createRate(String roomTypeName, String name, RateTypeEnum rateType, BigDecimal price) throws RoomTypeNotFoundException;

    public Rate viewRate(String roomTypeName) throws RateNotFoundException;

    public void updateRate(Long rateId, String rateName, String roomTypeName, RateTypeEnum rateType, BigDecimal price, LocalDate dateStart, LocalDate dateEnd) throws RateNameNotUniqueException, RoomTypeNotFoundException;

    public List<Rate> viewAllRates() throws RateNotFoundException;

    public void deleteRate(Long rateId);

    public void updateRate(Long rateId, String rateName, String roomTypeName, RateTypeEnum rateType, BigDecimal price) throws RateNameNotUniqueException, RoomTypeNotFoundException;

    public List<Pair<RoomInventory, BigDecimal>> searchRooms(LocalDate dateStart, LocalDate dateEnd) throws NoAvailableRoomsException;

    public void reserveGuestRooms(Long guestId, LocalDate dateStart, LocalDate dateEnd, List<ReservationLineItem> rooms);
}
