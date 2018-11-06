/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author samue
 */
@Entity
public class RoomInventory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private LocalDate date;

    private Integer roomCountForAllocation;

    private Integer roomAvail;

    @ManyToOne
    private RoomType rt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoomInventory)) {
            return false;
        }
        RoomInventory other = (RoomInventory) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomInventory[ id=" + getId() + " ]";
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getRoomAvail() {
        return roomAvail;
    }

    public void setRoomAvail(Integer roomAvail) {
        this.roomAvail = roomAvail;
    }

    public RoomType getRt() {
        return rt;
    }

    public void setRt(RoomType rt) {
        this.rt = rt;
    }

    public Integer getRoomCountForAllocation() {
        return roomCountForAllocation;
    }

    public void setRoomCountForAllocation(Integer roomCountForAllocation) {
        this.roomCountForAllocation = roomCountForAllocation;
    }
}
