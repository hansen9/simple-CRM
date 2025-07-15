package com.movaintelligence.simplecrm.entity;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    void testGettersSetters() {
        Customer c = new Customer();
        c.setId(1L);
        c.setName("Test Name");
        c.setType(Customer.Type.PERORANGAN);
        c.setStatus(Customer.Status.ACTIVE);
        List<Address> addresses = new ArrayList<>();
        Address addr = new Address();
        addr.setStreet("Jl. Test");
        addresses.add(addr);
        c.setAddresses(addresses);

        assertEquals(1L, c.getId());
        assertEquals("Test Name", c.getName());
        assertEquals(Customer.Type.PERORANGAN, c.getType());
        assertEquals(Customer.Status.ACTIVE, c.getStatus());
        assertEquals(1, c.getAddresses().size());
        assertEquals("Jl. Test", c.getAddresses().get(0).getStreet());
    }
}

