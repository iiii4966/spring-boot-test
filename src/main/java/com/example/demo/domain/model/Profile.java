package com.example.demo.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "account_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nick;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private Account account;

    public void setAccount(Account account) {
        this.account = account;
    }

    public static Profile createNew(String nick, Account account) {
        if (account == null) {
            throw new NullPointerException("Account not null");
        }
        var profile = new Profile();
        profile.nick = nick;
        profile.account = account;
        return profile;
    }
}
