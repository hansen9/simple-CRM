package com.movaintelligence.simplecrm.repository;

import com.movaintelligence.simplecrm.entity.Address;
import com.movaintelligence.simplecrm.entity.Customer;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryImplTest {
    private static Connection conn;
    private static CustomerRepository repo;

    @BeforeAll
    static void setup() throws Exception {
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE CUSTOMER (id BIGINT AUTO_INCREMENT PRIMARY KEY, nama VARCHAR(255), type VARCHAR(20), status VARCHAR(20))");
            st.execute("CREATE TABLE ADDRESS (id BIGINT AUTO_INCREMENT PRIMARY KEY, street VARCHAR(255), city VARCHAR(100), province VARCHAR(100), country VARCHAR(100), customer_id BIGINT, FOREIGN KEY (customer_id) REFERENCES CUSTOMER(id) ON DELETE CASCADE)");
        }
        repo = new CustomerRepositoryImpl(conn);
    }

    @AfterAll
    static void tearDown() throws Exception {
        conn.close();
    }

    @Test
    void testSaveAndFindById() {
        Customer c = new Customer();
        c.setName("John Doe");
        c.setType(Customer.Type.PERORANGAN);
        c.setStatus(Customer.Status.SEED);
        Address addr = new Address();
        addr.setStreet("Jl. Mawar");
        addr.setCity("Jakarta");
        addr.setProvince("DKI Jakarta");
        addr.setCountry("Indonesia");
        c.setAddresses(List.of(addr));
        Customer saved = repo.save(c);
        assertNotNull(saved.getId());
        Customer found = repo.findById(saved.getId());
        assertEquals("John Doe", found.getName());
        assertEquals(1, found.getAddresses().size());
        assertEquals("Jakarta", found.getAddresses().get(0).getCity());
    }

    @Test
    void testUpdate() {
        Customer c = new Customer();
        c.setName("Jane Doe");
        c.setType(Customer.Type.PERUSAHAAN);
        c.setStatus(Customer.Status.LEAD);
        Address addr = new Address();
        addr.setStreet("Jl. Melati");
        addr.setCity("Bandung");
        addr.setProvince("Jawa Barat");
        addr.setCountry("Indonesia");
        c.setAddresses(List.of(addr));
        Customer saved = repo.save(c);
        saved.setName("Jane Updated");
        saved.setStatus(Customer.Status.ACTIVE);
        Address newAddr = new Address();
        newAddr.setStreet("Jl. Kenanga");
        newAddr.setCity("Surabaya");
        newAddr.setProvince("Jawa Timur");
        newAddr.setCountry("Indonesia");
        saved.setAddresses(List.of(newAddr));
        repo.update(saved);
        Customer found = repo.findById(saved.getId());
        assertEquals("Jane Updated", found.getName());
        assertEquals(Customer.Status.ACTIVE, found.getStatus());
        assertEquals("Surabaya", found.getAddresses().get(0).getCity());
    }

    @Test
    void testDelete() {
        Customer c = new Customer();
        c.setName("Delete Me");
        c.setType(Customer.Type.PERORANGAN);
        c.setStatus(Customer.Status.SEED);
        Address addr = new Address();
        addr.setStreet("Jl. Delete");
        addr.setCity("Depok");
        addr.setProvince("Jawa Barat");
        addr.setCountry("Indonesia");
        c.setAddresses(List.of(addr));
        Customer saved = repo.save(c);
        boolean deleted = repo.delete(saved.getId());
        assertTrue(deleted);
        assertNull(repo.findById(saved.getId()));
    }

    @Test
    void testFindAllAndCount() {
        int before = repo.countAll();
        Customer c = new Customer();
        c.setName("Bulk Test");
        c.setType(Customer.Type.PERUSAHAAN);
        c.setStatus(Customer.Status.NURTURE);
        Address addr = new Address();
        addr.setStreet("Jl. Bulk");
        addr.setCity("Semarang");
        addr.setProvince("Jawa Tengah");
        addr.setCountry("Indonesia");
        c.setAddresses(List.of(addr));
        repo.save(c);
        List<Customer> all = repo.findAll(0, 10, "nama", true);
        assertTrue(all.size() >= 1);
        assertTrue(repo.countAll() > before);
    }

    @Test
    void testSearchAndCountSearch() {
        Customer c = new Customer();
        c.setName("SearchTest");
        c.setType(Customer.Type.PERORANGAN);
        c.setStatus(Customer.Status.LEAD);
        Address addr = new Address();
        addr.setStreet("Jl. Search");
        addr.setCity("Yogyakarta");
        addr.setProvince("DI Yogyakarta");
        addr.setCountry("Indonesia");
        c.setAddresses(List.of(addr));
        repo.save(c);
        List<Customer> found = repo.search("SearchTest", "Yogyakarta", "DI Yogyakarta", "Indonesia", 0, 10, "nama", true);
        assertFalse(found.isEmpty());
        assertEquals("SearchTest", found.get(0).getName());
        assertTrue(repo.countSearch("SearchTest", "Yogyakarta", "DI Yogyakarta", "Indonesia") > 0);
    }
}

