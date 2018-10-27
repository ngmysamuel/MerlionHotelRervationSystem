/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javafx.util.Pair;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

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
    @ManyToMany
    private List<Room> allocatedRooms;
    @Column(nullable = false)
    private List<Pair<RoomType, Integer>> roomsReserved;
    @ManyToOne
    @JoinColumn(nullable=false)
    private Guest guest;
    @ManyToOne
    private Partner partner;

    public Reservation() {
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

    public List<Room> getAllocatedRooms() {
        return allocatedRooms;
    }

    public void setAllocatedRooms(List<Room> allocatedRooms) {
        this.allocatedRooms = allocatedRooms;
    }

    public List<Pair<RoomType, Integer>> getRoomsReserved() {
        return roomsReserved;
    }

    public void setRoomsReserved(List<Pair<RoomType, Integer>> roomsReserved) {
        this.roomsReserved = roomsReserved;
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
