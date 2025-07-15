package com.movaintelligence.simplecrm.view;

import com.movaintelligence.simplecrm.entity.Customer;
import com.movaintelligence.simplecrm.entity.Address;
import com.movaintelligence.simplecrm.repository.CustomerRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class CustomerCreateDialog extends JDialog {
    private boolean saved = false;
    private JTextField nameField;
    private JComboBox<Customer.Type> typeCombo;
    private JComboBox<Customer.Status> statusCombo;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField provinceField;
    private JTextField countryField;
    private final CustomerRepository customerRepository;

    public CustomerCreateDialog(Frame owner, CustomerRepository repo) {
        super(owner, "Create Customer", true);
        this.customerRepository = repo;
        setSize(400, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        formPanel.add(new JLabel("Type:"));
        typeCombo = new JComboBox<>(Customer.Type.values());
        formPanel.add(typeCombo);
        formPanel.add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(Customer.Status.values());
        formPanel.add(statusCombo);
        formPanel.add(new JLabel("Street:"));
        streetField = new JTextField();
        formPanel.add(streetField);
        formPanel.add(new JLabel("City:"));
        cityField = new JTextField();
        formPanel.add(cityField);
        formPanel.add(new JLabel("Province:"));
        provinceField = new JTextField();
        formPanel.add(provinceField);
        formPanel.add(new JLabel("Country:"));
        countryField = new JTextField();
        formPanel.add(countryField);
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener((ActionEvent e) -> onSave());
        cancelButton.addActionListener((ActionEvent e) -> onCancel());
    }

    private void onSave() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.");
            return;
        }
        Customer customer = new Customer();
        customer.setName(name);
        customer.setType((Customer.Type) typeCombo.getSelectedItem());
        customer.setStatus((Customer.Status) statusCombo.getSelectedItem());
        Address address = new Address();
        address.setStreet(streetField.getText().trim());
        address.setCity(cityField.getText().trim());
        address.setProvince(provinceField.getText().trim());
        address.setCountry(countryField.getText().trim());
        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(address);
        customer.setAddresses(addresses);
        customerRepository.save(customer);
        saved = true;
        dispose();
    }

    private void onCancel() {
        saved = false;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
}

