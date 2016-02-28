package ar.com.dtabares.jexpenses.web.rest;

import ar.com.dtabares.jexpenses.Application;
import ar.com.dtabares.jexpenses.domain.Prefix;
import ar.com.dtabares.jexpenses.repository.PrefixRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PrefixResource REST controller.
 *
 * @see PrefixResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PrefixResourceIntTest {


    private static final Float DEFAULT_PREFIX = 1F;
    private static final Float UPDATED_PREFIX = 2F;

    @Inject
    private PrefixRepository prefixRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPrefixMockMvc;

    private Prefix prefix;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrefixResource prefixResource = new PrefixResource();
        ReflectionTestUtils.setField(prefixResource, "prefixRepository", prefixRepository);
        this.restPrefixMockMvc = MockMvcBuilders.standaloneSetup(prefixResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        prefix = new Prefix();
        prefix.setPrefix(DEFAULT_PREFIX);
    }

    @Test
    @Transactional
    public void createPrefix() throws Exception {
        int databaseSizeBeforeCreate = prefixRepository.findAll().size();

        // Create the Prefix

        restPrefixMockMvc.perform(post("/api/prefixs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prefix)))
                .andExpect(status().isCreated());

        // Validate the Prefix in the database
        List<Prefix> prefixs = prefixRepository.findAll();
        assertThat(prefixs).hasSize(databaseSizeBeforeCreate + 1);
        Prefix testPrefix = prefixs.get(prefixs.size() - 1);
        assertThat(testPrefix.getPrefix()).isEqualTo(DEFAULT_PREFIX);
    }

    @Test
    @Transactional
    public void checkPrefixIsRequired() throws Exception {
        int databaseSizeBeforeTest = prefixRepository.findAll().size();
        // set the field null
        prefix.setPrefix(null);

        // Create the Prefix, which fails.

        restPrefixMockMvc.perform(post("/api/prefixs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prefix)))
                .andExpect(status().isBadRequest());

        List<Prefix> prefixs = prefixRepository.findAll();
        assertThat(prefixs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrefixs() throws Exception {
        // Initialize the database
        prefixRepository.saveAndFlush(prefix);

        // Get all the prefixs
        restPrefixMockMvc.perform(get("/api/prefixs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(prefix.getId().intValue())))
                .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX.doubleValue())));
    }

    @Test
    @Transactional
    public void getPrefix() throws Exception {
        // Initialize the database
        prefixRepository.saveAndFlush(prefix);

        // Get the prefix
        restPrefixMockMvc.perform(get("/api/prefixs/{id}", prefix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(prefix.getId().intValue()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPrefix() throws Exception {
        // Get the prefix
        restPrefixMockMvc.perform(get("/api/prefixs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrefix() throws Exception {
        // Initialize the database
        prefixRepository.saveAndFlush(prefix);

		int databaseSizeBeforeUpdate = prefixRepository.findAll().size();

        // Update the prefix
        prefix.setPrefix(UPDATED_PREFIX);

        restPrefixMockMvc.perform(put("/api/prefixs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prefix)))
                .andExpect(status().isOk());

        // Validate the Prefix in the database
        List<Prefix> prefixs = prefixRepository.findAll();
        assertThat(prefixs).hasSize(databaseSizeBeforeUpdate);
        Prefix testPrefix = prefixs.get(prefixs.size() - 1);
        assertThat(testPrefix.getPrefix()).isEqualTo(UPDATED_PREFIX);
    }

    @Test
    @Transactional
    public void deletePrefix() throws Exception {
        // Initialize the database
        prefixRepository.saveAndFlush(prefix);

		int databaseSizeBeforeDelete = prefixRepository.findAll().size();

        // Get the prefix
        restPrefixMockMvc.perform(delete("/api/prefixs/{id}", prefix.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Prefix> prefixs = prefixRepository.findAll();
        assertThat(prefixs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
