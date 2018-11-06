/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import Enum.EmployeeTypeEnum;
import entity.Employee;
import entity.ExceptionReport;
import entity.Partner;
import entity.Reservation;
import entity.ReservationLineItem;
import entity.Room;
import entity.RoomInventory;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomInventoryNotFound;

/**
 *
 * @author samue
 */
@Stateless
public class MainControllerBean implements MainControllerBeanRemote, MainControllerBeanLocal {

    @EJB
    private RoomInventorySessionBeanLocal roomInventorySessionBean;

    @EJB
    private PartnerControllerBeanLocal partnerControllerBean;

    @EJB
    private EmployeeControllerBeanLocal employeeControllerBean;

    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

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
        System.out.println("main controller bean is called");
        return partnerControllerBean.create(emp, manager, username);
    }

    public List<Partner> viewPartners() {
        return partnerControllerBean.viewAll();
    }

    //Allocate Rooms
    //I need to find the roomTypes with grade the highest first. Allocate them. Change the room status. Then move one the roomsTypes 
    //second highest grade. 
    
    public void timer() {
        ExceptionReport er = new ExceptionReport();
        er.setDate(LocalDate.now());
        List<RoomType> sortedListOfRT = sortRoomTypeAsc();
        LocalDate date = LocalDate.now();
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.startDate = :date");
        q.setParameter("date", date);
        List<Reservation> todayCheckInList = q.getResultList();
        for (int i = 1; i <= sortedListOfRT.size(); i++) {
            for (Reservation r : todayCheckInList) {
                List<ReservationLineItem> listOfReservationLineItems = r.getReservationLineItems();
                for (ReservationLineItem rli : listOfReservationLineItems) {
                    if (rli.getRoomType().getGrade() == sortedListOfRT.get(i).getGrade()) {
                        allocateRooms(rli, er);
                    }
                }
            }
        }
        em.persist(er);
    }

    private boolean scanForUpgrades(ReservationLineItem rli, ExceptionReport er) { //return true if the room can be upgraded
        RoomType rt = rli.getRoomType();
        Integer grade = rt.getGrade();
        if (grade == 1) {
            return false;
        }
        --grade;
        Query q = em.createQuery("SELECT ri FROM RoomInventory ri WHERE ri.rt.grade = :grade");
        q.setParameter("grade", grade);
        RoomInventory ri = (RoomInventory) q.getResultList().get(0);
        if (ri.getRoomCountForAllocation() >= rli.getNumberOfRooms()) {
            setRoomsAllocated(rli, ri);
            return true;
        }
        return false;
    }

    private void allocateRooms(ReservationLineItem rli, ExceptionReport er) {
        Integer numOfRooms = rli.getNumberOfRooms();
        RoomType rt = rli.getRoomType();
        RoomInventory ri = new RoomInventory();
        try {
            ri = roomInventorySessionBean.retrieveRoomInventory(LocalDate.now(), rt);
        } catch (RoomInventoryNotFound e) {
            System.out.println("\n\n\nOops No such reservation found for that date\n\n\n");
            return;
        }
        if (ri.getRoomCountForAllocation() < numOfRooms) {
            List<String> ls = er.getExceptions();
            if (scanForUpgrades(rli, er)) {
                ls.add("Upgraded. Booked Room Type: " + rt + "\nBooked Num of Rooms: " + numOfRooms + "\nCurrent Avail Rooms: " + ri.getRoomAvail() + "\nReservationID: " + rli.getReservation().getId());
                er.setExceptions(ls);
                System.out.println("\n\n\nOops not enough rooms\n\n\n");
            } else {
                ls.add("No upgrade. Booked Room Type: " + rt + "\nBooked Num of Rooms: " + numOfRooms + "\nCurrent Avail Rooms: " + ri.getRoomAvail() + "\nReservationID: " + rli.getReservation().getId());
                er.setExceptions(ls);
                System.out.println("\n\n\nOops not enough rooms\n\n\n");
            }
        } else {
            setRoomsAllocated(rli, ri);

        }
    }

    private void setRoomsAllocated(ReservationLineItem rli, RoomInventory ri) {
        int numOfRooms = rli.getNumberOfRooms();
        ri.setRoomCountForAllocation(ri.getRoomCountForAllocation() - numOfRooms);
        for (int i = 0; i < numOfRooms; i++) {
            Query q = em.createQuery("SELECT r FROM Room r WHERE r.status = :status");
            q.setParameter("status", "Available");
            List<Room> ls = q.getResultList();
            rli.getAllocatedRooms().add(ls.get(i));
            rli.setAllocatedRooms(rli.getAllocatedRooms());
            ls.get(i).setStatus("Occupied");
        }
    }   

    private List<RoomType> sortRoomTypeAsc() {
        Query que = em.createQuery("SELECT rt FROM RoomType rt");
        List<RoomType> lis = que.getResultList();
        for (int i = 0; i < lis.size(); i++) {
            for (int j = i; j < lis.size(); j++) {
                RoomType rt = lis.get(i);
                RoomType rt2 = lis.get(j);
                if (rt.getGrade() > rt.getGrade()) {
                    RoomType rtTemp = rt2;
                    rt2 = rt;
                    rt = rtTemp;
                }
            }
        }
        return lis;
    }
}
