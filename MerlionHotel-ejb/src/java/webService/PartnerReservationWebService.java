package webService;

import entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import stateless.PartnerControllerBeanLocal;
import util.exception.ReservationNotFoundException;

@WebService(serviceName = "PartnerReservationWebService")
@Stateless
public class PartnerReservationWebService {

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
        return partnerControllerBean.viewAllReservations();
    }
    
    public Reservation viewReservationDetails(Long id) throws ReservationNotFoundException {
        try {
            return partnerControllerBean.viewReservationDetails(id);
        } catch (ReservationNotFoundException e) {
            throw e;
        }
    }
    
    //public void viewReservationDetails(Long partner, LocalDate dateStart, LocalDate dateEnd) throws ReservationNotFoundException {
//        try {
//            return partnerControllerBean.viewReservationDetails(partner, dateStart, dateEnd);
//        } catch (Throwable e) {
//            throw e;
//        }
//    }
}
