package ar.com.dtabares.jexpenses.repository;

import ar.com.dtabares.jexpenses.domain.Entry;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Entry entity.
 */
public interface EntryRepository extends JpaRepository<Entry,Long> {

    @Query("select entry from Entry entry where entry.user.login = ?#{principal.username}")
    List<Entry> findByUserIsCurrentUser();

}
