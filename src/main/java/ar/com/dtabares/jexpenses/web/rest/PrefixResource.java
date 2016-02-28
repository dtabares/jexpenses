package ar.com.dtabares.jexpenses.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.com.dtabares.jexpenses.domain.Prefix;
import ar.com.dtabares.jexpenses.repository.PrefixRepository;
import ar.com.dtabares.jexpenses.web.rest.util.HeaderUtil;
import ar.com.dtabares.jexpenses.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Prefix.
 */
@RestController
@RequestMapping("/api")
public class PrefixResource {

    private final Logger log = LoggerFactory.getLogger(PrefixResource.class);
        
    @Inject
    private PrefixRepository prefixRepository;
    
    /**
     * POST  /prefixs -> Create a new prefix.
     */
    @RequestMapping(value = "/prefixs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prefix> createPrefix(@Valid @RequestBody Prefix prefix) throws URISyntaxException {
        log.debug("REST request to save Prefix : {}", prefix);
        if (prefix.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("prefix", "idexists", "A new prefix cannot already have an ID")).body(null);
        }
        Prefix result = prefixRepository.save(prefix);
        return ResponseEntity.created(new URI("/api/prefixs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("prefix", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prefixs -> Updates an existing prefix.
     */
    @RequestMapping(value = "/prefixs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prefix> updatePrefix(@Valid @RequestBody Prefix prefix) throws URISyntaxException {
        log.debug("REST request to update Prefix : {}", prefix);
        if (prefix.getId() == null) {
            return createPrefix(prefix);
        }
        Prefix result = prefixRepository.save(prefix);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("prefix", prefix.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prefixs -> get all the prefixs.
     */
    @RequestMapping(value = "/prefixs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Prefix>> getAllPrefixs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Prefixs");
        Page<Prefix> page = prefixRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prefixs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prefixs/:id -> get the "id" prefix.
     */
    @RequestMapping(value = "/prefixs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prefix> getPrefix(@PathVariable Long id) {
        log.debug("REST request to get Prefix : {}", id);
        Prefix prefix = prefixRepository.findOne(id);
        return Optional.ofNullable(prefix)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prefixs/:id -> delete the "id" prefix.
     */
    @RequestMapping(value = "/prefixs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePrefix(@PathVariable Long id) {
        log.debug("REST request to delete Prefix : {}", id);
        prefixRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("prefix", id.toString())).build();
    }
}
