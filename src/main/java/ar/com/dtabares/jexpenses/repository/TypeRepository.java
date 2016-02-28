package ar.com.dtabares.jexpenses.repository;

import ar.com.dtabares.jexpenses.domain.Type;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Type entity.
 */
public interface TypeRepository extends JpaRepository<Type,Long> {

}
