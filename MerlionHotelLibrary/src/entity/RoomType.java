/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Asus
 */
@Entity
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer roomSize;
    @Column(nullable = false)
    private String bed;
    @Column(nullable = false)
    private Integer capacity;
    @Column(nullable = false)
    private String amenities;
    @Column(nullable = false, unique = true)
    private Integer grade;
    @Column(nullable = false)
    private Integer initialRoomAvailability;
    
    @OneToMany(mappedBy = "type")
    private List<Room> rooms;
    
    @OneToMany
    //@JoinColumn(nullable = false)
    private List<Rate> rates;
    
    @OneToMany(mappedBy = "roomType")
    private List<ReservationLineItem> reservationLineItems;
    
    @OneToMany(mappedBy = "rt")
    private List<RoomInventory> roomInventory = new ArrayList<RoomInventory>();

    public RoomType() {
    }

    public RoomType(String name, Integer initialRoomAvailability) {
        this.name = name;
        this.initialRoomAvailability = initialRoomAvailability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Integer roomSize) {
        this.roomSize = roomSize;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
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
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomTypeEntity[ id=" + id + " ]";
    }
    
    //@XmlTransient
    public List<RoomInventory> getRoomInventory() {
        return roomInventory;
    }

    public void setRoomInventory(List<RoomInventory> roomInventory) {
        this.roomInventory = roomInventory;
    }

    public Integer getInitialRoomAvailability() {
        return initialRoomAvailability;
    }

    public void setInitialRoomAvailability(Integer initialRoomAvailability) {
        this.initialRoomAvailability = initialRoomAvailability;
    }
    
}
