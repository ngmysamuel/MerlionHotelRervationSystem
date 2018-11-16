/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import Enum.EmployeeTypeEnum;
import Enum.RateTypeEnum;
import Enum.ReservationTypeEnum;
import com.sun.prism.impl.PrismTrace;
import entity.Employee;
import entity.ExceptionReport;
import entity.Guest;
import entity.Partner;
import entity.Rate;
import entity.Reservation;
import entity.ReservationLineItem;
import entity.Room;
import entity.RoomInventory;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.GuestNotFoundException;
import util.exception.NoAvailableRoomsException;
import util.exception.RateNameNotUniqueException;
import util.exception.RateNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomInventoryNotFound;
import util.exception.RoomNotAllocatedException;
import util.exception.RoomTypeNotFoundException;
import util.exception.StillInUseException;

/**
 *
 * @author samue
 */
@Stateless
public class MainControllerBean implements MainControllerBeanRemote, MainControllerBeanLocal {

    @EJB
    private GuestControllerBeanLocal guestControllerBean;

    @EJB
    private ReservationControllerBeanLocal reservationControllerBean;

    @EJB
    private RateControllerBeanLocal rateControllerBean;

    @EJB
    private RoomControllerSessionBeanLocal roomControllerSessionBean;

    @EJB
    private RoomTypeControllerSessionBeanLocal roomTypeControllerSessionBean;

    @EJB
    private RoomInventorySessionBeanLocal roomInventorySessionBean;

    @EJB
    private PartnerControllerBeanLocal partnerControllerBean;

    @EJB
    private EmployeeControllerBeanLocal employeeControllerBean;
    
    

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

    @Resource
    private EJBContext eJBContext;

    public boolean doLogin(String username, String password) {
        Query q = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
        q.setParameter("username", username);
        List<Employee> ls = q.getResultList();
        if (ls.isEmpty()) {
            return false;
        }
        Employee e = ls.get(0);
        if (e.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public EmployeeTypeEnum getEmployeeTypeEnum(String username) {
        Query q = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
        q.setParameter("username", username);
        List<Employee> ls = q.getResultList();
        Employee e = ls.get(0);
        return e.getEmployeeType();
    }

    public Employee createEmployee(String username, String password, EmployeeTypeEnum employeeType) {
        return employeeControllerBean.create(username, password, employeeType);
    }

    public List<Employee> viewEmployees() {
        return employeeControllerBean.viewAll();
    }

    public Partner createPartner(String emp, String manager, String username) {
        return partnerControllerBean.create(emp, manager, username);
    }

    public List<Partner> viewPartners() {
        return partnerControllerBean.viewAll();
    }
    
    public ExceptionReport viewExceptionReport(LocalDate date) {
        Query q = em.createQuery("select er from ExceptionReport er where er.date = :date");
        q.setParameter("date", date);
        return (ExceptionReport) q.getSingleResult();
    }
    
    public void createRoomType(String bed, String name, String amenities, int capacity, String description, int grade, int roomSize) {
        roomTypeControllerSessionBean.create(bed, name, amenities, capacity, description, grade, roomSize);
    }
    
    public List<RoomType> viewAllRoomTypes() {
        return roomTypeControllerSessionBean.getRoomTypes();
    }
    
    public RoomType viewSpecificRoomType(String name) throws NoResultException {
        Query q = em.createQuery("select rt from RoomType rt where rt.name = :name");
        q.setParameter("name", name);
System.out.println("name is "+name);
System.out.println("About to execute query");
RoomType r = new RoomType();
try {
        r = (RoomType) q.getSingleResult();
} catch (NoResultException e) {
    return null;
}
        return r;
    }
    
    public void updateRoomType(String bed, String name, String amenities, String capacity, String description, String grade, String roomSize, int initialRoomAvail, Long roomTypeId, String b) {
        roomTypeControllerSessionBean.update(bed, name, amenities, capacity, description, grade, roomSize, initialRoomAvail, roomTypeId,b);
    }
    
    public void updateRomType(int num, Long id) {
        RoomType rt = em.find(RoomType.class, id);
        if (rt.getInitialRoomAvailability() != null) {
            num += rt.getInitialRoomAvailability();
        }
        String bed = "";
        String name = "";
        String amenities = "";
        String capacity = "";
        String descriptio = "";
        String grade = "";
        String roomSize = "";
        String b = "";
        roomTypeControllerSessionBean.update(bed, name, amenities, capacity, descriptio, grade, roomSize, num, id, b);
    }
    
    public List<RoomType> retrieveRoomTypes(){
        return this.roomTypeControllerSessionBean.getRoomTypes();
    }
    
    public void deleteRoomType(Long id) throws StillInUseException {
        try {
            roomTypeControllerSessionBean.delete(id);
        } catch (StillInUseException ex) {
            throw ex;
        }
    }
    
    public void createRoom(Integer roomNum, String status, Long roomTypeId) {
        roomControllerSessionBean.create(roomNum, status, roomTypeId);
    }
    
    public void updateRoom(Long roomNum, String status, Long roomTypeId) {
        roomControllerSessionBean.update(roomNum, status, roomTypeId);
    }
    
    public List<Room> viewRooms() {
        return roomControllerSessionBean.viewAllRooms();
    }
    
    public void deleteRoom(Long roomNum) throws StillInUseException {
        try {
            roomControllerSessionBean.deleteRoom(roomNum);
        } catch (StillInUseException ex) {
            throw ex;
        }
    }
    
    
    
    public void persistEr() {
        ExceptionReport er = new ExceptionReport();
        er.setDate(LocalDate.now());
        em.persist(er);
    }

    //Allocate Rooms
    //I need to find the roomTypes with grade the highest first. Allocate them. Change the room status. Then move one the roomsTypes 
    //second highest grade. 
    //Problem: I need to check through the entire length of the reservation if there will enough space. 
    //Both in the allocateRoom() and scanForUpgrades()
    @Schedule(hour = "2")
    public void timer() {
System.out.println("I have called timer()");
        guestCheckout();
        reconcileRoomAvailability();
        ExceptionReport er = new ExceptionReport();
        er.setDate(LocalDate.now());
        em.persist(er);
        em.flush();
        List<RoomType> sortedListOfRT = sortRoomTypeAsc();
        LocalDate date = LocalDate.now();
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.dateStart = :date");
        q.setParameter("date", date);
        List<Reservation> todayCheckInList = q.getResultList();
        for (RoomType rtMaster : sortedListOfRT) { //for each type of roomType
            for (Reservation r : todayCheckInList) { //for each reservation checking in today
                em.refresh(r);
                r.getReservationLineItems().size();
                List<ReservationLineItem> listOfReservationLineItems = r.getReservationLineItems();
                LocalDate dateEnd = r.getDateEnd();
                LocalDate dateStart = r.getDateStart();             
                for (ReservationLineItem rli : listOfReservationLineItems) { //for each line item in the reservation
                    RoomType rt = rli.getRoomType();
                    Integer numOfRooms = rli.getNumberOfRooms();
                    if (rtMaster.getGrade() == rt.getGrade()) {
                        Boolean b1 = roomControllerSessionBean.allocateRooms(rt, numOfRooms, dateStart, dateEnd, rli.getId()); //allocating across the whole duration
                        if (b1) {
                            continue; //continue on to the next rli
                        } else {
                            Boolean b2 = scanForUpgrades(dateStart, dateEnd, rt, numOfRooms, sortedListOfRT, rli.getId());
                            List<String> ls = er.getExceptions();
                            if (b2) {
                                ls.add("Upgraded. Booked Room Type: " + rli.getRoomType() + "\nBooked Num of Rooms: " + rli.getNumberOfRooms() + "\nReservationID: " + r.getId());
                                er.setExceptions(ls);
                                System.out.println("\n\n\nOops not enough rooms\n\n\n");
                            } else {
                                ls.add("No upgrade. Booked Room Type: " + rt + "\nBooked Num of Rooms: " + numOfRooms + "\nReservationID: " + r.getId());
                                er.setExceptions(ls);
                                System.out.println("\n\n\nOops not enough rooms\n\n\n");
                            }
                        }
                    }
                    reconcileRoomAvailability();
                }
            }
        }    
        //reconcileRoomAvailability();
    }
    
    private void guestCheckout() {
        Query q = em.createQuery("select r from Reservation r where r.dateEnd = :date");
        q.setParameter("date", LocalDate.now());
        List<Reservation> ls = q.getResultList();
        for (Reservation r : ls) {
            List<ReservationLineItem> ls2 = r.getReservationLineItems();
            for (ReservationLineItem rli : ls2) {
                List<Room> ls3 = rli.getAllocatedRooms();
                for (Room rm : ls3) {
                    rm.setStatus("Available");
                }
            }
        }
    }
    
    private void reconcileRoomAvailability() {
        List<RoomType> l = roomTypeControllerSessionBean.getRoomTypes();
        Query q = em.createQuery("select ri from RoomInventory ri where ri.date = :date");
        q.setParameter("date", LocalDate.now());
        List<RoomInventory> ls = q.getResultList();
        int i;
        for (RoomInventory ri : ls) {
            q = em.createQuery("select r from Room r where r.type = :type and r.status = :status");
            q.setParameter("status", "Available");
            q.setParameter("type", ri.getRt());
            i = q.getResultList().size();
            ri.setRoomAvail(i);
            RoomType rt = ri.getRt();
            l.remove(rt);
        }
        if (!l.isEmpty()) { //if l is not empty, ie if there is still room types that do not have a room inventory
            for (RoomType roomType : l) {
                roomType.getRoomInventory().size();
System.out.println("REconcile()1. Room Type's room inventroy is "+roomType.getRoomInventory());  
            }
            for (RoomType roomType : l) {
                RoomInventory ri = new RoomInventory();
                ri.setRt(roomType); //REMEBER TO SET THE OTHER SIDE
                ri.setDate(LocalDate.now());
                q = em.createQuery("select r from Room r where r.type = :type and r.status = :status");
                q.setParameter("status", "Available");
                q.setParameter("type", roomType);
                i = q.getResultList().size();
                ri.setRoomAvail(i);
                ri.setRoomCountForAllocation(roomType.getInitialRoomAvailability());
System.out.println("REconcile()2. Room Type's room inventroy is "+roomType.getRoomInventory());  
                List<RoomInventory> ls1 = roomType.getRoomInventory();
                ls1.add(ri);
                roomType.setRoomInventory(ls1);
System.out.println("REconcile()3. Room Type's room inventroy is "+roomType.getRoomInventory());                
                em.persist(ri);
            }
        }
        em.flush();
    }
    
    private boolean scanForUpgrades(LocalDate dateStart, LocalDate dateEnd, RoomType rt, Integer numOfRooms, List<RoomType> sortedListOfRT, Long id) { //return true if the room can be upgraded
//        for (int i = rt.getGrade(); i > 1; i--) {
//            Query q = em.createQuery("select rt from RoomType rt where rt.grade = :grade");
//            q.setParameter("grade", i);
//            RoomType rtToCheck = (RoomType) q.getSingleResult();
//            if (!roomControllerSessionBean.allocateRooms(rtToCheck, numOfRooms, dateStart, dateEnd, id)) {
//                return false;
//            }
//        }
        Integer i = rt.getGrade()-1;
        if (i <= 0) {
            return false;
        }
        Query q = em.createQuery("select rt from RoomType rt where rt.grade = :grade");
        q.setParameter("grade", i);
        RoomType rtToCheck = (RoomType) q.getSingleResult();
System.out.println("rtToCheck is "+rtToCheck.getName()); 
        return roomControllerSessionBean.allocateRooms(rtToCheck, numOfRooms, dateStart, dateEnd, id);
    }

//    private boolean setRoomsAllocated(Long id, RoomInventory ri) {
//        ReservationLineItem rli = em.find(ReservationLineItem.class, id);
//        int numOfRooms = rli.getNumberOfRooms();
////        ri.setRoomCountForAllocation(ri.getRoomCountForAllocation() - numOfRooms);
//        Query q = em.createQuery("SELECT r FROM Room r WHERE r.status = :status");
//        q.setParameter("status", "Available");
//        List<Room> ls = q.getResultList();
//        if (ls.size() < numOfRooms) {
//            return false;
//        } else {
//            for (int i = 0; i < numOfRooms; i++) {
//                Room r = ls.get(i);
//                List<Room> ls2 = rli.getAllocatedRooms();
//                ls2.add(r);
//                rli.setAllocatedRooms(ls2);
//                r.setStatus("Occupied");
//                List<ReservationLineItem> ls3 = r.getReservationLineItems();
//                ls3.add(rli);
//                r.setReservationLineItems(ls3);
//            }
//        }
//        return true;
//    }

    public List<RoomType> sortRoomTypeAsc() {
        Query que = em.createQuery("SELECT rt FROM RoomType rt");
        List<RoomType> lis = que.getResultList();
        for (int i = 0; i < lis.size(); i++) {
            for (int j = i+1; j < lis.size(); j++) {
                RoomType rt = lis.get(i);
                RoomType rt2 = lis.get(j);
                if (rt.getGrade() > rt2.getGrade()) {
                    Collections.swap(lis, i, j);
                }
            }
        }
        return lis;
    }
    
    
    public Rate createRate(String roomTypeName, String name, RateTypeEnum rateType, BigDecimal price) throws RoomTypeNotFoundException{
        RoomType roomType = roomTypeControllerSessionBean.retrieveRoomType(roomTypeName);
        Rate rate = rateControllerBean.createRate(name, roomType, rateType, price);
        return rate;
    }
    
    
    public Rate createRate(String roomTypeName, String name, RateTypeEnum rateType, BigDecimal price, LocalDate dateStart, LocalDate dateEnd) throws RoomTypeNotFoundException{
        RoomType roomType = roomTypeControllerSessionBean.retrieveRoomType(roomTypeName);
        Rate rate = rateControllerBean.createRate(name, roomType, rateType, price, dateStart, dateEnd);
        return rate;
    }
    
    
    public Rate viewRate(String roomTypeName) throws RateNotFoundException{
        return rateControllerBean.retrieveRate(roomTypeName);
    }
    
    
    public void updateRate(Long rateId, String rateName, String roomTypeName, RateTypeEnum rateType, BigDecimal price, LocalDate dateStart, LocalDate dateEnd) throws RateNameNotUniqueException, RoomTypeNotFoundException{
        RoomType roomType = roomTypeControllerSessionBean.retrieveRoomType(roomTypeName);
        rateControllerBean.updateRate(rateId, rateName, rateType, price, dateStart, dateEnd, roomType);
    }
    
    
    public void updateRate(Long rateId, String rateName, String roomTypeName, RateTypeEnum rateType, BigDecimal price) throws RateNameNotUniqueException, RoomTypeNotFoundException{
        RoomType roomType = roomTypeControllerSessionBean.retrieveRoomType(roomTypeName);
        rateControllerBean.updateRate(rateId, rateName, rateType, price, roomType);
    }
    
    
    public void deleteRate(Long rateId){
        rateControllerBean.deleteRate(rateId);
    }
    
    
    public List<Rate> viewAllRates() throws RateNotFoundException{
        return rateControllerBean.retrieveAllRates();
    }
    
    
    public List<Pair<RoomInventory, BigDecimal>> searchRooms(LocalDate dateStart, LocalDate dateEnd) throws NoAvailableRoomsException{
        List<RoomType> roomTypes = roomTypeControllerSessionBean.getRoomTypes();
        List<Pair<RoomInventory, BigDecimal>> values = null;
        for(RoomType rt: roomTypes){
            boolean full = false;
            LocalDate date = dateStart;
            while(date.compareTo(dateEnd) == -1){
                full = !this.roomInventorySessionBean.isItFull(dateStart, rt);
                date = date.plusDays(1);
            }
            if(!full){
                try {
                    values.add(new Pair(this.roomInventorySessionBean.retrieveRoomInventory(dateStart, rt), this.rateControllerBean.countRate(dateStart, dateEnd, rt)));
                } catch (RoomInventoryNotFound ex) {
                    Logger.getLogger(MainControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if(values == null){
            throw new NoAvailableRoomsException();
        }
        return values;
    }
    

    @Override
    public void reserveGuestRooms(Long guestId, LocalDate dateStart, LocalDate dateEnd, List<ReservationLineItem> rooms) throws ReservationNotFoundException{

        reservationControllerBean.createGuestReservation(dateStart, dateEnd, ReservationTypeEnum.Online, guestId, rooms);
    }
    
    @Override
    public void reserveGuestRooms(Guest guest, LocalDate dateStart, LocalDate dateEnd, List<ReservationLineItem> rooms) throws ReservationNotFoundException{
        try{
            guestControllerBean.retrieveGuestByPassport(guest.getPassportNumber());
            guest = guestControllerBean.retrieveGuestByEmail(guest.getEmail());
        } catch(GuestNotFoundException ex){
            em.persist(guest);
            em.flush();
        }
        reservationControllerBean.createWalkinReservation(dateStart, dateEnd, ReservationTypeEnum.Online, guest.getId(), rooms);
    }
    
    public List<Integer> retrieveAllocatedRooms(String passport) throws GuestNotFoundException, ReservationNotFoundException, RoomNotAllocatedException{
        Guest guest = this.guestControllerBean.retrieveGuestByPassport(passport);
        Reservation reservation = reservationControllerBean.retrieveCheckInReservation(guest.getId());
        List<Integer> roomNums = new ArrayList<>();
        List<ReservationLineItem> rlis = reservation.getReservationLineItems();
        for(ReservationLineItem rli: rlis){
            List<Room> rooms= rli.getAllocatedRooms();
            try{
                for(Room room: rooms){
                    roomNums.add(room.getNumber());
                }
            } catch(NullPointerException ex) {
                throw new RoomNotAllocatedException();
            }
        }
        guest.setStatus("Checked In");
        return roomNums;
    }
    
    @Override
    public void checkOut(String passport) throws GuestNotFoundException{
        Guest guest = this.guestControllerBean.retrieveGuestByPassport(passport);
        guest.setStatus("idle");
    }
}
