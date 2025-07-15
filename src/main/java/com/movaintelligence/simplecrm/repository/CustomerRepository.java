package com.movaintelligence.simplecrm.repository;

import com.movaintelligence.simplecrm.entity.Customer;
import java.util.List;

public interface CustomerRepository {
    Customer save(Customer customer);
    Customer update(Customer customer);
    boolean delete(Long id);
    Customer findById(Long id);
    List<Customer> findAll(int offset, int limit, String sortBy, boolean asc);
    List<Customer> search(String name, String city, String province, String country, int offset, int limit, String sortBy, boolean asc);
    int countAll();
    int countSearch(String name, String city, String province, String country);
}

