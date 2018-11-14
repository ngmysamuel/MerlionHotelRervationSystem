/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import Enum.ReservationTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javafx.util.Pair;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Asus
 */
@Entity
public class Reservation implements Serializable {



    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private LocalDateTime reservationDateTime;
    @Column(nullable=false)
    private LocalDate dateStart;
    @Column(nullable=false)
    private LocalDate dateEnd;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private ReservationTypeEnum type;
    
    @JoinColumn(nullable = false)
    @OneToMany(mappedBy = "reservation")
    private List<ReservationLineItem> reservationLineItems;
    
    @ManyToOne
    private Guest guest;
    @ManyToOne
    private Partner partner;
    @Column(nullable = false)
    private BigDecimal price;
    
    public Reservation() {
    }

    //For Walk-in or Online Reservation
    public Reservation(LocalDateTime reservationDateTime, LocalDate dateStart, LocalDate dateEnd, ReservationTypeEnum type, Guest guest, BigDecimal price) {
        this.reservationDateTime = reservationDateTime;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.type = type;
        this.guest = guest;
        this.price = price;
    }

    //For Partner reservation
    public Reservation(LocalDateTime reservationDateTime, LocalDate dateStart, LocalDate dateEnd, Guest guest, Partner partner, BigDecimal price) {
        this.reservationDateTime = reservationDateTime;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.type = ReservationTypeEnum.Partner;
        this.guest = guest;
        this.partner = partner;
        this.price = price;
    }
    
    public Reservation(LocalDateTime reservationDateTime, LocalDate dateStart, LocalDate dateEnd, List<ReservationLineItem> rooms, Guest guest, Partner partner, BigDecimal price) {
        this.reservationDateTime = reservationDateTime;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.type = ReservationTypeEnum.Partner;
        this.reservationLineItems = reservationLineItems;
        this.guest = guest;
        this.partner = partner;
        this.price = price;
    }
    
    public ReservationTypeEnum getType() {
        return type;
    }

    public void setType(ReservationTypeEnum type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public List<ReservationLineItem> getReservationLineItems() {
        return reservationLineItems;
    }

    public void setReservationLineItems(List<ReservationLineItem> reservationLineItems) {
        this.reservationLineItems = reservationLineItems;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationEntity[ id=" + id + " ]";
    }
    
}
