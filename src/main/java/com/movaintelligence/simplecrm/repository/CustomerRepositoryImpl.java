package com.movaintelligence.simplecrm.repository;

import com.movaintelligence.simplecrm.entity.Customer;
import com.movaintelligence.simplecrm.entity.Address;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {
    private final Connection connection;

    public CustomerRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Customer save(Customer customer) {
        String sql = "INSERT INTO CUSTOMER (nama, type, status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getType().name());
            ps.setString(3, customer.getStatus().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    customer.setId(rs.getLong(1));
                }
            }
            // Save addresses if any
            if (customer.getAddresses() != null) {
                for (Address addr : customer.getAddresses()) {
                    saveAddress(addr, customer.getId());
                }
            }
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAddress(Address address, Long customerId) throws SQLException {
        String sql = "INSERT INTO ADDRESS (street, city, province, country, customer_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, address.getStreet());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getProvince());
            ps.setString(4, address.getCountry());
            ps.setLong(5, customerId);
            ps.executeUpdate();
        }
    }

    @Override
    public Customer update(Customer customer) {
        String sql = "UPDATE CUSTOMER SET nama=?, type=?, status=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getType().name());
            ps.setString(3, customer.getStatus().name());
            ps.setLong(4, customer.getId());
            ps.executeUpdate();
            // For simplicity, delete all addresses and re-insert
            deleteAddressesByCustomerId(customer.getId());
            if (customer.getAddresses() != null) {
                for (Address addr : customer.getAddresses()) {
                    saveAddress(addr, customer.getId());
                }
            }
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteAddressesByCustomerId(Long customerId) throws SQLException {
        String sql = "DELETE FROM ADDRESS WHERE customer_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            deleteAddressesByCustomerId(id);
            String sql = "DELETE FROM CUSTOMER WHERE id=?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer findById(Long id) {
        String sql = "SELECT * FROM CUSTOMER WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer customer = mapCustomer(rs);
                    customer.setAddresses(findAddressesByCustomerId(id));
                    return customer;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private List<Address> findAddressesByCustomerId(Long customerId) throws SQLException {
        String sql = "SELECT * FROM ADDRESS WHERE customer_id=?";
        List<Address> addresses = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Address addr = new Address();
                    addr.setId(rs.getLong("id"));
                    addr.setStreet(rs.getString("street"));
                    addr.setCity(rs.getString("city"));
                    addr.setProvince(rs.getString("province"));
                    addr.setCountry(rs.getString("country"));
                    addr.setCustomerId(rs.getLong("customer_id"));
                    addresses.add(addr);
                }
            }
        }
        return addresses;
    }

    @Override
    public List<Customer> findAll(int offset, int limit, String sortBy, boolean asc) {
        String sql = "SELECT * FROM CUSTOMER ORDER BY " + sortBy + (asc ? " ASC" : " DESC") + " LIMIT ? OFFSET ?";
        List<Customer> customers = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Customer customer = mapCustomer(rs);
                    customer.setAddresses(findAddressesByCustomerId(customer.getId()));
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    @Override
    public List<Customer> search(String name, String city, String province, String country, int offset, int limit, String sortBy, boolean asc) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT c.* FROM CUSTOMER c LEFT JOIN ADDRESS a ON c.id=a.customer_id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            sql.append(" AND c.nama LIKE ?");
            params.add("%" + name + "%");
        }
        if (city != null && !city.isEmpty()) {
            sql.append(" AND a.city LIKE ?");
            params.add("%" + city + "%");
        }
        if (province != null && !province.isEmpty()) {
            sql.append(" AND a.province LIKE ?");
            params.add("%" + province + "%");
        }
        if (country != null && !country.isEmpty()) {
            sql.append(" AND a.country LIKE ?");
            params.add("%" + country + "%");
        }
        sql.append(" ORDER BY c.").append(sortBy).append(asc ? " ASC" : " DESC");
        sql.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);
        List<Customer> customers = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Customer customer = mapCustomer(rs);
                    customer.setAddresses(findAddressesByCustomerId(customer.getId()));
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM CUSTOMER";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int countSearch(String name, String city, String province, String country) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT c.id) FROM CUSTOMER c LEFT JOIN ADDRESS a ON c.id=a.customer_id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            sql.append(" AND c.nama LIKE ?");
            params.add("%" + name + "%");
        }
        if (city != null && !city.isEmpty()) {
            sql.append(" AND a.city LIKE ?");
            params.add("%" + city + "%");
        }
        if (province != null && !province.isEmpty()) {
            sql.append(" AND a.province LIKE ?");
            params.add("%" + province + "%");
        }
        if (country != null && !country.isEmpty()) {
            sql.append(" AND a.country LIKE ?");
            params.add("%" + country + "%");
        }
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private Customer mapCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setName(rs.getString("nama"));
        customer.setType(Customer.Type.valueOf(rs.getString("type")));
        customer.setStatus(Customer.Status.valueOf(rs.getString("status")));
        return customer;
    }
}

