/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import Enum.RateTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Asus
 */
@Entity
public class Rate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RateTypeEnum type;
    @Column(nullable = false)
    private BigDecimal price;
    @Column
    private LocalDate dateStart;
    @Column
    private LocalDate dateEnd;
    @Column(nullable = false, length = 8)
    private String status;
    @ManyToOne
    private RoomType roomType;

    public String getStatus() {
        return status;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Rate() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Normal and Published rate
    public Rate(RateTypeEnum type, RoomType roomType, String name, BigDecimal price) {
        this.type = type;
        this.price = price;
        this.name = name;
        this.status = "active";
        this.roomType = roomType;
    }

    // Peak and Promotion rate
    public Rate(RateTypeEnum type, RoomType roomType, String name, BigDecimal price, LocalDate dateStart, LocalDate dateEnd) {
        this.type = type;
        this.price = price;
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.status = "active";
        this.roomType = roomType;
    }
    
    

    public RateTypeEnum getType() {
        return type;
    }

    public void setType(RateTypeEnum type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
        if (!(object instanceof Rate)) {
            return false;
        }
        Rate other = (Rate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RateEntity[ id=" + id + " ]";
    }
    
}
