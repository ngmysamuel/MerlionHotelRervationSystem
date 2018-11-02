package webService;

import entity.Partner;
import entity.Reservation;
import entity.ReservationLineItem;
import entity.RoomInventory;
import entity.RoomType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import stateless.PartnerControllerBeanLocal;
import stateless.RoomTypeControllerSessionBeanLocal;
import util.exception.ReservationNotFoundException;

@WebService(serviceName = "PartnerReservationWebService")
@Stateless
public class PartnerReservationWebService {

    @EJB
    private RoomTypeControllerSessionBeanLocal roomTypeControllerSessionBean;

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
            Partner p = r.getPartner();
            p.setReservations(null);
        }
        return ls;
    }

    public Reservation viewReservationDetails(Long id) throws ReservationNotFoundException {
        try {
            return partnerControllerBean.viewReservationDetails(id);
        } catch (ReservationNotFoundException e) {
            throw e;
        }
    }

    public List<RoomType> getRoomType() {
        List<RoomType> ls = roomTypeControllerSessionBean.getRoomTypes();
        for (RoomType rt : ls) {
            List<RoomInventory> ls2 = rt.getRoomInventory();
            for (RoomInventory ri : ls2) {
                ri.setRt(null);
            }
        }
        return ls;
    }

    public Long createReservation(String startString, String endString, Long guestId, Long partnerId, List<ReservationLineItem> rooms) throws ReservationNotFoundException {
        try {
            LocalDate dateStart = LocalDate.parse(startString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate dateEnd = LocalDate.parse(endString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.println(rooms.get(0).getRoomType().getInitialRoomAvailability());
            return partnerControllerBean.createReservation(dateStart, dateEnd, guestId, partnerId, rooms);
        } catch (ReservationNotFoundException e) {
            throw e;
        }
    }

    public List<Boolean> searchRoom(String startString, String endString) {
        System.out.println("VI");
        LocalDate dateStart = LocalDate.parse(startString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate dateEnd = LocalDate.parse(endString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println("IV");
        return partnerControllerBean.searchRooms(dateStart, dateEnd);
    }
}
