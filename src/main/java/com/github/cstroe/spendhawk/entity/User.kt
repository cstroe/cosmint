package com.github.cstroe.spendhawk.entity

import javax.persistence.*

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
data class User (
    @Id @Column @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    val name: String,

    @OneToMany(mappedBy = "user", targetEntity = Account::class)
    var accounts: MutableList<Account>? = ArrayList()
)