package webService;

import entity.Partner;
import entity.Reservation;
import entity.ReservationLineItem;
import entity.RoomType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import stateless.PartnerControllerBeanLocal;
import stateless.RoomTypeControllerSessionBeanLocal;
import util.exception.ReservationNotFoundException;

@WebService(serviceName = "PartnerReservationWebService")
@Stateless
public class PartnerReservationWebService {

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
    
    public void printRoomType() {
        partnerControllerBean.printRoomType();
    }

    public List<String> getRoomType() {
        List<RoomType> ls = roomTypeControllerSessionBean.getRoomTypes();
        List<String> ls2 = new ArrayList<>();
        for (RoomType rt : ls) {
            ls2.add(rt.getName());
        }
        return ls2;
    }

    public Long createReservation(String startString, String endString, Long guestId, Long partnerId, /*List<Pair<String, Integer>>*/List<String> rooms) throws ReservationNotFoundException {
            LocalDate dateStart = LocalDate.parse(startString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate dateEnd = LocalDate.parse(endString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            List<ReservationLineItem> rliList = new ArrayList<ReservationLineItem>();
            for (int i = 0; i < rooms.size(); i+=2) {
                String roomtype = rooms.get(i);
                String numofrooms = rooms.get(i+1);
                ReservationLineItem rli = new ReservationLineItem();
                rli.setNumberOfRooms(Integer.parseInt(numofrooms));
                RoomType rt = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :name").setParameter("name", roomtype).getResultList().get(0);
                //rli.setNumberOfRooms(p.getK());
                //RoomType rt = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :name").setParameter("name", p.getKey()).getResultList().get(0);
                rli.setRoomType(rt);
                rliList.add(rli);
System.out.println(rliList.size());     
            }
            try {
                return partnerControllerBean.createReservation(dateStart, dateEnd, guestId, partnerId, rliList);
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
    
    public List<Boolean> search(String startString, String endString) {
        LocalDate dateStart = LocalDate.parse(startString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate dateEnd = LocalDate.parse(endString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return partnerControllerBean.search(dateStart, dateEnd);
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
