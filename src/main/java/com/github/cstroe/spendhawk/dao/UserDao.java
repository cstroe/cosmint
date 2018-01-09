package com.github.cstroe.spendhawk.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * A user of the application.
 */

@Entity
@Table(name = "userinfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDao {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "user")
    private List<AccountDao> accounts;
}
