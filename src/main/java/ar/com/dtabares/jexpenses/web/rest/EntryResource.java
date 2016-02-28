package ar.com.dtabares.jexpenses.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.com.dtabares.jexpenses.domain.Entry;
import ar.com.dtabares.jexpenses.repository.EntryRepository;
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
 * REST controller for managing Entry.
 */
@RestController
@RequestMapping("/api")
public class EntryResource {

    private final Logger log = LoggerFactory.getLogger(EntryResource.class);
        
    @Inject
    private EntryRepository entryRepository;
    
    /**
     * POST  /entrys -> Create a new entry.
     */
    @RequestMapping(value = "/entrys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Entry> createEntry(@Valid @RequestBody Entry entry) throws URISyntaxException {
        log.debug("REST request to save Entry : {}", entry);
        if (entry.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("entry", "idexists", "A new entry cannot already have an ID")).body(null);
        }
        Entry result = entryRepository.save(entry);
        return ResponseEntity.created(new URI("/api/entrys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("entry", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entrys -> Updates an existing entry.
     */
    @RequestMapping(value = "/entrys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Entry> updateEntry(@Valid @RequestBody Entry entry) throws URISyntaxException {
        log.debug("REST request to update Entry : {}", entry);
        if (entry.getId() == null) {
            return createEntry(entry);
        }
        Entry result = entryRepository.save(entry);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("entry", entry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entrys -> get all the entrys.
     */
    @RequestMapping(value = "/entrys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Entry>> getAllEntrys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Entrys");
        Page<Entry> page = entryRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/entrys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /entrys/:id -> get the "id" entry.
     */
    @RequestMapping(value = "/entrys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Entry> getEntry(@PathVariable Long id) {
        log.debug("REST request to get Entry : {}", id);
        Entry entry = entryRepository.findOne(id);
        return Optional.ofNullable(entry)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /entrys/:id -> delete the "id" entry.
     */
    @RequestMapping(value = "/entrys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        log.debug("REST request to delete Entry : {}", id);
        entryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("entry", id.toString())).build();
    }
}
