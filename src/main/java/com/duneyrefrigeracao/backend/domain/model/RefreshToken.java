package com.duneyrefrigeracao.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT",nullable = false)
    private String token;
    @Column(nullable = false)
    private String refreshToken;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar expirationDate;
    @Column(nullable = false)
    private boolean isAvailable;
}
