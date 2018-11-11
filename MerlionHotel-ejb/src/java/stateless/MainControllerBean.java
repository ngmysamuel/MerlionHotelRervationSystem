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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
        System.out.println("main controller bean is called");
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
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void timer() {
        ExceptionReport er = new ExceptionReport();
        er.setDate(LocalDate.now());
        em.persist(er);
        em.flush();
        List<RoomType> sortedListOfRT = sortRoomTypeAsc();
        LocalDate date = LocalDate.now();
        Query q = em.createQuery("SELECT r FROM Reservation r WHERE r.dateStart = :date");
        q.setParameter("date", date);
        List<Reservation> todayCheckInList = q.getResultList();
        for (int i = 0; i < sortedListOfRT.size(); i++) { //for each type of roomType
            for (Reservation r : todayCheckInList) { //for each reservation checking in today
                List<ReservationLineItem> listOfReservationLineItems = r.getReservationLineItems();
                LocalDate dateEnd = r.getDateEnd();
                LocalDate dateStart = r.getDateStart();
                for (ReservationLineItem rli : listOfReservationLineItems) { //for each line item in the reservation
                    RoomType rt = rli.getRoomType();
                    Integer numOfRooms = rli.getNumberOfRooms();
                    if (rli.getRoomType().getGrade() == sortedListOfRT.get(i).getGrade()) {
                        Boolean b1 = allocateRooms(rt, numOfRooms, dateStart, dateEnd, rli.getId());
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
                }
            }
        }    
    }
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private Boolean allocateRooms(RoomType rt, Integer numOfRooms, LocalDate dateStart, LocalDate dateEnd, Long id) {
        while (!dateStart.isAfter(dateEnd)) {
            RoomInventory ri = new RoomInventory();
            try {
                ri = roomInventorySessionBean.retrieveRoomInventory(dateStart, rt);
            } catch (RoomInventoryNotFound e) {
                System.out.println("\n\n\nOops No such reservation found for that date\n\n\n");
                return false;
            }
            if (ri.getRoomCountForAllocation() < numOfRooms) {
            //if (0 < numOfRooms) {    
                eJBContext.setRollbackOnly();
                return false;
            } else {
                setRoomsAllocated(id, ri);
            }
            dateStart = dateStart.plusDays(1);
        }
        return true;
    }

    private boolean scanForUpgrades(LocalDate dateStart, LocalDate dateEnd, RoomType rt, Integer numOfRooms, List<RoomType> sortedListOfRT, Long id) { //return true if the room can be upgraded
        boolean b1 = false;
        RoomType rtIndiv = new RoomType();
        for (int i = 1; i < sortedListOfRT.size(); i++) {
            Integer ind = rt.getGrade() - 1 - i;
            if (ind < 0) {
                break;
            }
            rtIndiv = sortedListOfRT.get(ind);
            b1 = allocateRooms(rtIndiv, numOfRooms, dateStart, dateEnd, id);
            if (b1) {
                return b1;
            }
        }
        return false;
    }

    private void setRoomsAllocated(Long id, RoomInventory ri) {
        ReservationLineItem rli = em.find(ReservationLineItem.class, id);
        int numOfRooms = rli.getNumberOfRooms();
        ri.setRoomCountForAllocation(ri.getRoomCountForAllocation() - numOfRooms);
        Query q = em.createQuery("SELECT r FROM Room r WHERE r.status = :status");
        q.setParameter("status", "Available");
        List<Room> ls = q.getResultList();
        for (int i = 0; i < numOfRooms; i++) {
            List<Room> ls2 = rli.getAllocatedRooms();
            ls2.add(ls.get(i));
            rli.setAllocatedRooms(ls2);
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
