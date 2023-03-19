package com.andreas.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(columnDefinition = "Decimal(10,2) default '0'")
    BigDecimal balance;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

}
