package ar.com.dtabares.jexpenses.repository;

import ar.com.dtabares.jexpenses.domain.Prefix;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prefix entity.
 */
public interface PrefixRepository extends JpaRepository<Prefix,Long> {

}
