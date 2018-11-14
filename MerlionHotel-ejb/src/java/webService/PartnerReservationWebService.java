package webService;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.ReservationLineItem;
import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import stateless.MainControllerBeanLocal;
import stateless.PartnerControllerBeanLocal;
import stateless.RoomTypeControllerSessionBeanLocal;
import util.exception.ReservationNotFoundException;

@WebService(serviceName = "PartnerReservationWebService")
@Stateless
public class PartnerReservationWebService {

    @EJB
    private MainControllerBeanLocal mainControllerBean;

    @EJB
    private RoomTypeControllerSessionBeanLocal roomTypeControllerSessionBean;
    @PersistenceContext(unitName = "MerlionHotel-ejbPU")
    private EntityManager em;

    @EJB
    private PartnerControllerBeanLocal partnerControllerBean;

//    @WebMethod(operationName = "hello")
//    public String hello(@WebParam(name = "name") String txt) {
//        return "Hello " + txt + " !";
//    }
    public boolean login(String username, String password) {
        return partnerControllerBean.login(username, password);
    }

    public List<Reservation> viewAllReservations() {
        List<Reservation> ls = partnerControllerBean.viewAllReservations();
        for (Reservation r : ls) {
            em.detach(r);
            Partner p = r.getPartner();
            p.setReservations(null);
            Guest g = r.getGuest();
            g.setReservations(null);
            List<ReservationLineItem> ls2 = r.getReservationLineItems();
            for (ReservationLineItem rli : ls2) {
                em.detach(rli);
                RoomType rt = rli.getRoomType();
                em.detach(rt);
                rt.setRoomInventory(null);
                rt.setRooms(null);
                rt.setReservationLineItems(null);
                rt.setRates(null);
                List<Room> ls3 = rli.getAllocatedRooms();
                for (Room room : ls3) { //for each room
                    em.detach(room);
                    room.setReservationLineItems(null);
                }
            }
        }

        return ls;
    }

    public Reservation viewReservationDetails(Long id) throws ReservationNotFoundException {
        try {
            Reservation r = partnerControllerBean.viewReservationDetails(id);
            em.detach(r);
            em.flush();
            Partner p = r.getPartner();
            p.setReservations(null);
            Guest g = r.getGuest();
            g.setReservations(null);
            List<ReservationLineItem> ls2 = r.getReservationLineItems();
            for (ReservationLineItem rli : ls2) { //for each reservation line item
                em.detach(rli);
                RoomType rt = rli.getRoomType();
                em.detach(rt);
                rt.setRoomInventory(null);
                rt.setRooms(null);
                rt.setReservationLineItems(null);
                rt.setRates(null);
                rt.setRoomInventory(null);
                rli.getAllocatedRooms().size();
                List<Room> ls3 = rli.getAllocatedRooms();
                for (Room room : ls3) { //for each room
                    em.detach(room);
                    room.getReservationLineItems().size();
                }
                for (Room room : ls3) {
                    room.setReservationLineItems(null);
                }
            }
            return r;
        } catch (ReservationNotFoundException e) {
            throw e;
        }
    }

    public void printRoomType() {
        partnerControllerBean.printRoomType();
    }

    public List<String> getRoomType() {
        List<RoomType> ls = mainControllerBean.sortRoomTypeAsc();
        List<String> ls2 = new ArrayList<>();
        for (RoomType rt : ls) {
            if (rt.getInitialRoomAvailability() == null) {
                continue;
            }
            ls2.add(rt.getName());
        }
        return ls2;
    }

    public Long createReservation(String startString, String endString, Long guestId, Long partnerId, HashMap hm) throws ReservationNotFoundException {
        LocalDate dateStart = LocalDate.parse(startString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate dateEnd = LocalDate.parse(endString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<ReservationLineItem> rliList = new ArrayList<ReservationLineItem>();
        //String[] set;
        List<String> roomTypeList = new ArrayList<>();
        Set<String> set = hm.keySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            roomTypeList.add((String) iter.next());
        }
        System.out.println("hasmap size is " + hm.size());
        for (int i = 0; i < hm.size(); i++) {
            String roomTypeName = roomTypeList.get(i);
            Integer numOfRooms = (Integer) hm.get(roomTypeName);
            ReservationLineItem rli = new ReservationLineItem();
            RoomType rt = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :name").setParameter("name", roomTypeName).getResultList().get(0);
            //Integer numofrooms = hm.get(roomTypeName);
            //rli.setNumberOfRooms(numofrooms); //Integer.parseInt(numofrooms));
            rli.setRoomType(rt);
            rliList.add(rli);
            rli.setNumberOfRooms(numOfRooms);
            System.out.println("number of rli to be booked is: " + rliList.size());
        }
        try {
            return partnerControllerBean.createReservation(dateStart, dateEnd, guestId, partnerId, rliList);
        } catch (ReservationNotFoundException e) {
            throw e;
        }
    }

    public Long createReservation2(String startString, String endString, Long guestId, Long partnerId, List<String> lsString, List<Integer> lsInteger) throws ReservationNotFoundException {
        LocalDate dateStart = LocalDate.parse(startString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate dateEnd = LocalDate.parse(endString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<ReservationLineItem> rliList = new ArrayList<ReservationLineItem>();
        int lsIntegerCounter = 0;
        for (String s : lsString) {
            String roomTypeName = s;
            Integer numOfRooms = lsInteger.get(lsIntegerCounter);
            ReservationLineItem rli = new ReservationLineItem();
            RoomType rt = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :name").setParameter("name", roomTypeName).getResultList().get(0);
            rli.setRoomType(rt);
            rliList.add(rli);
            rli.setNumberOfRooms(numOfRooms);
            ++lsIntegerCounter;
        }
        try {
            return partnerControllerBean.createReservation(dateStart, dateEnd, guestId, partnerId, rliList);
        } catch (ReservationNotFoundException e) {
            throw e;
        }
    }

    public List<Boolean> searchRoom(String startString, String endString) {
        LocalDate dateStart = LocalDate.parse(startString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate dateEnd = LocalDate.parse(endString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return partnerControllerBean.searchRooms(dateStart, dateEnd);
    }

    public List<Boolean> search(String startString, String endString) {
        LocalDate dateStart = LocalDate.parse(startString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate dateEnd = LocalDate.parse(endString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return partnerControllerBean.search(dateStart, dateEnd);
    }

    public List<String> getStartAndEndDate(Long id) {
        List<String> ls = new ArrayList<>();
        Reservation reservation = em.find(Reservation.class, id);
        String startDate = reservation.getDateStart().toString();
        String endDate = reservation.getDateEnd().toString();
        ls.add(startDate);
        ls.add(endDate);
        return ls;
    }
}
