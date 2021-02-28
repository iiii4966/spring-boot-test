package com.example.demo.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "auth_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column()
    private String password;

    @Column(name = "is_superuser")
    private final boolean isSuperUser = false;

    @Column(name = "first_name")
    private final String firstName = "";

    @Column(name = "last_name")
    private final String lastName = "";

    @Column(name = "email")
    private final String email = "";

    @Column(name = "is_staff")
    private boolean isStaff = false;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "date_joined", updatable = false)
    @CreationTimestamp
    private Date dateJoined;

    @Column(name = "certification_id")
    private final String certificationID = "";

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Profile profile;

    @Transient
    private boolean isAnonymous = false;

    public static Account createNew (String username, String password){
        var user = new Account();
        user.username = username;
        user.password = password;
        return user;
    }

    public static Account createAnonymous() {
        var anonymous = new Account();
        anonymous.id = 0L;
        anonymous.username="Anonymous";
        anonymous.isAnonymous=true;
        anonymous.isActive=false;
        anonymous.isStaff=false;
        return anonymous;
    }
}
