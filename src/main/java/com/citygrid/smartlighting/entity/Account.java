package com.citygrid.smartlighting.entity;

import jakarta.persistence.*;

/**
 * A single entry in the Chart of Accounts.
 * type decides which financial statement it appears on:
 *  ASSET / LIABILITY  -> Balance Sheet
 *  INCOME / EXPENSE   -> Profit & Loss
 */
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    public Account() {}

    public Account(String code, String name, AccountType type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }
}
