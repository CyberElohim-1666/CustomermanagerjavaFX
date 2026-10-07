import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

/**
 * A small desktop application for the ICT261 Customer Manager classroom lab.
 * Customer records are stored in memory while the application is open.
 */
public class CustomerManagerApp {
    private static final String[] PROVINCES = {
        "Central", "Copperbelt", "Eastern", "Luapula", "Lusaka",
        "Muchinga", "Northern", "North-Western", "Southern", "Western"
    };

    private final JFrame frame;
    private final JTextField nameField;
    private final JComboBox<String> provinceBox;
    private final CustomerTableModel customerTableModel;
    private final JTable customerTable;
    private final JLabel statusLabel;

    public CustomerManagerApp() {
        frame = new JFrame("Customer Manager");
        nameField = new JTextField(24);
        provinceBox = new JComboBox<>(PROVINCES);
        customerTableModel = new CustomerTableModel();
        customerTable = new JTable(customerTableModel);
        statusLabel = new JLabel("Enter a customer name and choose a province.");

        createAndShowGui();
    }

    private void createAndShowGui() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        content.add(createFormPanel(), BorderLayout.NORTH);
        content.add(createTablePanel(), BorderLayout.CENTER);
        content.add(createStatusPanel(), BorderLayout.SOUTH);

        frame.setContentPane(content);
        frame.setMinimumSize(new Dimension(620, 440));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> nameField.requestFocusInWindow());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem clearItem = new JMenuItem("Clear form");
        clearItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        clearItem.addActionListener(event -> clearForm());

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        closeItem.addActionListener(event -> frame.dispose());

        fileMenu.add(clearItem);
        fileMenu.addSeparator();
        fileMenu.add(closeItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem howToUseItem = new JMenuItem("How to use");
        howToUseItem.addActionListener(event -> showHelp());
        helpMenu.add(howToUseItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer details"));

        JLabel nameLabel = new JLabel("Customer name:");
        nameLabel.setLabelFor(nameField);
        JLabel provinceLabel = new JLabel("Province:");
        provinceLabel.setLabelFor(provinceBox);

        provinceBox.setSelectedIndex(-1);
        provinceBox.setToolTipText("Choose the customer's province");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.anchor = GridBagConstraints.WEST;

        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(nameLabel, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.NONE;
        formPanel.add(provinceLabel, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(provinceBox, constraints);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton saveButton = new JButton("Save customer");
        JButton clearButton = new JButton("Clear form");
        JButton deleteButton = new JButton("Delete selected");

        saveButton.addActionListener(event -> saveCustomer());
        clearButton.addActionListener(event -> clearForm());
        deleteButton.addActionListener(event -> deleteSelectedCustomer());
        frame.getRootPane().setDefaultButton(saveButton);

        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(deleteButton);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(buttonPanel, constraints);

        return formPanel;
    }

    private JScrollPane createTablePanel() {
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setRowHeight(24);
        customerTable.setFillsViewportHeight(true);
        customerTable.setAutoCreateRowSorter(false);
        customerTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(180);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Saved customers"));
        scrollPane.setPreferredSize(new Dimension(580, 220));
        return scrollPane;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 2, 0, 2));
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        return statusPanel;
    }

    private void saveCustomer() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("Enter the customer name.");
            nameField.requestFocusInWindow();
            return;
        }

        String province = (String) provinceBox.getSelectedItem();
        if (province == null) {
            showError("Choose a province.");
            provinceBox.requestFocusInWindow();
            return;
        }

        customerTableModel.addCustomer(new Customer(name, province));
        int newRow = customerTableModel.getRowCount() - 1;
        customerTable.setRowSelectionInterval(newRow, newRow);
        customerTable.scrollRectToVisible(customerTable.getCellRect(newRow, 0, true));

        nameField.setText("");
        provinceBox.setSelectedIndex(-1);
        showSuccess("Customer saved.");
        nameField.requestFocusInWindow();
    }

    private void deleteSelectedCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Select a customer before deleting.");
            customerTable.requestFocusInWindow();
            return;
        }

        Customer selectedCustomer = customerTableModel.getCustomerAt(selectedRow);
        String message = "Delete the selected customer?\n\n"
                + "Name: " + selectedCustomer.getName() + "\n"
                + "Province: " + selectedCustomer.getProvince();
        int choice = JOptionPane.showConfirmDialog(
                frame,
                message,
                "Confirm deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            customerTableModel.removeCustomer(selectedRow);
            showSuccess("Customer deleted.");
            customerTable.requestFocusInWindow();
        } else {
            showStatus("Deletion cancelled.", new Color(70, 70, 70));
        }
    }

    private void clearForm() {
        nameField.setText("");
        provinceBox.setSelectedIndex(-1);
        showStatus("Form cleared.", new Color(70, 70, 70));
        nameField.requestFocusInWindow();
    }

    private void showHelp() {
        JOptionPane.showMessageDialog(
                frame,
                "1. Enter a customer name.\n"
                        + "2. Choose a province.\n"
                        + "3. Press Enter or click Save customer.\n"
                        + "4. Select a table row before deleting.\n\n"
                        + "Customer records are stored only while the application is open.",
                "Customer Manager help",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        showStatus(message, new Color(185, 28, 28));
    }

    private void showSuccess(String message) {
        showStatus(message, new Color(4, 120, 87));
    }

    private void showStatus(String message, Color colour) {
        statusLabel.setText(message);
        statusLabel.setForeground(colour);
    }

    public static void main(String[] args) {
        if (args.length == 1 && "--smoke-test".equals(args[0])) {
            runSmokeTest();
            return;
        }
        SwingUtilities.invokeLater(CustomerManagerApp::new);
    }

    private static void runSmokeTest() {
        CustomerTableModel model = new CustomerTableModel();
        Customer firstCustomer = new Customer("Mary Banda", "Central");
        Customer secondCustomer = new Customer("John Phiri", "Lusaka");

        model.addCustomer(firstCustomer);
        model.addCustomer(secondCustomer);
        check(model.getRowCount() == 2, "Customers were not added to the table model.");
        check("Mary Banda".equals(model.getValueAt(0, 0)), "The name column is incorrect.");
        check("Lusaka".equals(model.getValueAt(1, 1)), "The province column is incorrect.");

        model.removeCustomer(0);
        check(model.getRowCount() == 1, "The selected customer was not deleted.");
        check(secondCustomer == model.getCustomerAt(0), "The remaining customer is incorrect.");

        System.out.println("Customer Manager smoke test passed.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Represents one customer in the Customer Manager application.
     */
    private static class Customer {
        private final String name;
        private final String province;

        Customer(String name, String province) {
            this.name = name;
            this.province = province;
        }

        public String getName() {
            return name;
        }

        public String getProvince() {
            return province;
        }
    }

    /**
     * Connects the in-memory customer list to the JTable.
     */
    private static class CustomerTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;

        private final String[] columnNames = {"Customer name", "Province"};
        private final ArrayList<Customer> customers = new ArrayList<>();

        @Override
        public int getRowCount() {
            return customers.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Customer customer = customers.get(rowIndex);
            if (columnIndex == 0) {
                return customer.getName();
            }
            return customer.getProvince();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public void addCustomer(Customer customer) {
            int row = customers.size();
            customers.add(customer);
            fireTableRowsInserted(row, row);
        }

        public Customer getCustomerAt(int row) {
            return customers.get(row);
        }

        public void removeCustomer(int row) {
            customers.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }
}
