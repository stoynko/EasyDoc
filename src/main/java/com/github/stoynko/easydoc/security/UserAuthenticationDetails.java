package com.github.stoynko.easydoc.security;

import com.github.stoynko.easydoc.user.model.AccountAuthority;
import com.github.stoynko.easydoc.user.model.AccountRole;
import com.github.stoynko.easydoc.user.model.AccountStatus;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static com.github.stoynko.easydoc.user.model.AccountStatus.ACTIVE;
import static com.github.stoynko.easydoc.user.model.AccountStatus.EMAIL_UNVERIFIED;
import static com.github.stoynko.easydoc.user.model.AccountStatus.INCOMPLETE;
import static com.github.stoynko.easydoc.user.model.AccountStatus.SUSPENDED;

@Setter
@Getter
@AllArgsConstructor
public class UserAuthenticationDetails implements UserDetails{

    private UUID id;

    private String emailAddress;

    private String passwordHash;

    private AccountStatus accountStatus;

    private AccountRole role;

    private Set<AccountAuthority> accountAuthorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
        authorities.add(new SimpleGrantedAuthority("STATUS_" + this.accountStatus.name()));

        if (authorities != null) {
            for (AccountAuthority authority : accountAuthorities) {
                authorities.add(new SimpleGrantedAuthority(authority.name()));
            }
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.emailAddress;
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountStatus == ACTIVE || accountStatus == EMAIL_UNVERIFIED || accountStatus == INCOMPLETE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountStatus != SUSPENDED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return accountStatus != SUSPENDED;
    }

    @Override
    public boolean isEnabled() {
        return accountStatus == ACTIVE || accountStatus == EMAIL_UNVERIFIED || accountStatus == INCOMPLETE;
    }
}

