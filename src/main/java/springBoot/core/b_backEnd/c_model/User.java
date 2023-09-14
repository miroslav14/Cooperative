package springBoot.core.b_backEnd.c_model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import springBoot.core.c_config.a_security.Role;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Users")
@ApiModel(description = "This is entity transferred into the table of users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "This is primary key of the User model object which is of type long")
    private Long id;

    @NonNull
    private String password;

    @NonNull
    private String username;

    @NonNull
    private String firstname;

    @NonNull
    private String lastname;


    @Column(name="accountnonexpired")
    private boolean accountNonExpired;

    @Column(name="accountnonlocked")
    private boolean accountNonLocked;

    @Column(name="credentialsnonexpired")
    private boolean credentialsNonExpired;

    private boolean enabled;

    private Set<String> authorities = new HashSet<>();

    public boolean add_GrantedAuthority(Role role){
        if(process_hasGrantedAutority(role)){
            return false;
        }
        return process_add_GrantedAutority(role);
    }

    private boolean process_add_GrantedAutority(Role role) {
        this.authorities.add(role.name());
        return process_hasGrantedAutority(role);
    }

    public boolean hasGrantedAutority(Role role){
        return process_hasGrantedAutority(role);
    }

    private boolean process_hasGrantedAutority(Role role) {

        if(this.authorities == null || this.authorities.size() == 0){
            return false;
        }

        for (String authority : authorities){
            if (role.name().equals(authority)){
                return true;
            }
        }
        return false;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return process_getAuthorities();
    }
    private Collection<? extends GrantedAuthority> process_getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList();
        if (this.authorities != null && this.authorities.size() > 0) {
            this.authorities.forEach(authority -> simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(authority)));
        }
        return simpleGrantedAuthorityList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
