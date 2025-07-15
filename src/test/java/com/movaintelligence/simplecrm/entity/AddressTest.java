package com.movaintelligence.simplecrm.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {
    @Test
    void testGettersSetters() {
        Address addr = new Address();
        addr.setId(2L);
        addr.setStreet("Jl. Mawar");
        addr.setCity("Jakarta");
        addr.setProvince("DKI Jakarta");
        addr.setCountry("Indonesia");
        addr.setCustomerId(1L);

        assertEquals(2L, addr.getId());
        assertEquals("Jl. Mawar", addr.getStreet());
        assertEquals("Jakarta", addr.getCity());
        assertEquals("DKI Jakarta", addr.getProvince());
        assertEquals("Indonesia", addr.getCountry());
        assertEquals(1L, addr.getCustomerId());
    }
}

