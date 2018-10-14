package com.wallethubex.ex.loganalysis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ACCESS_LOG")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "date", nullable = false)
    public LocalDateTime date;

    @Column(name = "IP_ADDRESS", nullable = false)
    public String ipAddress;

    public AccessLog(LocalDateTime date, String ipAddress) {
        this.date = date;
        this.ipAddress = ipAddress;
    }
}