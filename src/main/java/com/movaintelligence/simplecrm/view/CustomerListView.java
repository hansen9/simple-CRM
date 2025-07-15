package com.movaintelligence.simplecrm.view;

import com.movaintelligence.simplecrm.entity.Customer;
import com.movaintelligence.simplecrm.repository.CustomerRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerListView extends JFrame {
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField searchNameField;
    private JTextField searchCityField;
    private JTextField searchProvinceField;
    private JTextField searchCountryField;
    private JButton searchButton;
    private JButton nextPageButton;
    private JButton prevPageButton;
    private JButton createButton;
    private JLabel pageLabel;
    private int currentPage = 1;
    private int pageSize = 50;
    private int totalRows = 0;

    private final CustomerRepository customerRepository;

    public CustomerListView(CustomerRepository repo) {

        customerRepository = repo;

        setTitle("Customer List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Name:"));
        searchNameField = new JTextField(10);
        searchPanel.add(searchNameField);
        searchPanel.add(new JLabel("City:"));
        searchCityField = new JTextField(10);
        searchPanel.add(searchCityField);
        searchPanel.add(new JLabel("Province:"));
        searchProvinceField = new JTextField(10);
        searchPanel.add(searchProvinceField);
        searchPanel.add(new JLabel("Country:"));
        searchCountryField = new JTextField(10);
        searchPanel.add(searchCountryField);
        searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        // Add Create button
        createButton = new JButton("Create");
        searchPanel.add(createButton);

        add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Name", "City", "Province", "Country", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        // Pagination panel
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        prevPageButton = new JButton("Previous");
        nextPageButton = new JButton("Next");
        pageLabel = new JLabel("Page 1");
        paginationPanel.add(prevPageButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextPageButton);
        add(paginationPanel, BorderLayout.SOUTH);

        // Event listeners (dummy for now)
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Implement search and reload table
            }
        });
        nextPageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Implement next page
            }
        });
        prevPageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Implement previous page
            }
        });
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createButtonActionPerformed(e);
            }
        });
    }

    private void createButtonActionPerformed(ActionEvent e) {
        CustomerCreateDialog dialog = new CustomerCreateDialog(CustomerListView.this, customerRepository);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadCustomers();
        }
    }

    // Load data from repository, no parameters needed
    public void loadCustomers() {
        tableModel.setRowCount(0);
        int offset = (currentPage - 1) * pageSize;

        List<Customer> customers = customerRepository.findAll(offset, pageSize, "nama", true);
        int totalRows = customerRepository.countAll();

        String addressRow;
        for (Customer c : customers) {
            if (c.getAddresses() == null || c.getAddresses().isEmpty())
                addressRow = "- No Address -";
            else
                addressRow = c.getAddresses().getFirst().getStreet() + ", " +
                              c.getAddresses().getFirst().getCity() + ", " +
                              c.getAddresses().getFirst().getProvince() + ", " +
                              c.getAddresses().getFirst().getCountry();
            tableModel.addRow(new Object[]{
                    c.getName(),
                    addressRow,
                    c.getStatus()
            });
        }
        this.totalRows = totalRows;
        pageLabel.setText("Page " + currentPage);
    }
}
