package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.util.Collection;
import java.util.Optional;

/**
 * A user has:
 * <ul>
 *     <li>A unique database id.</li>
 *     <li>A user name.  Used for identification in the system.</li>
 *     <li>Many accounts.</li>
 *     <li>Many expense categories.</li>
 * </ul>
 */
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(exclude = {"name", "accounts"})
public class User {
    @Id
    @Column
    private Integer id;

    @Column
    private String name;

    @OneToMany(mappedBy = "user")
    private Collection<Account> accounts;
}
