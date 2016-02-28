package ar.com.dtabares.jexpenses.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Prefix.
 */
@Entity
@Table(name = "prefix")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Prefix implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "prefix", nullable = false)
    private Float prefix;
    
    @OneToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrefix() {
        return prefix;
    }
    
    public void setPrefix(Float prefix) {
        this.prefix = prefix;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prefix prefix = (Prefix) o;
        if(prefix.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, prefix.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Prefix{" +
            "id=" + id +
            ", prefix='" + prefix + "'" +
            '}';
    }
}
