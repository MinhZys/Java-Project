package page;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ReserveTablePage extends JPanel {
    private JTextField nameField;
    private JTextField phoneField;
    private JButton[] tableButtons;
    private Map<String, String[]> reservationMap;
    private JTable reservationTable;
    private DefaultTableModel tableModel;
    TablePage tablePage;

    private final Color BOOKED_COLOR = Color.RED;
    private final Color AVAILABLE_COLOR = Color.GRAY;
    private final Color OCCUPIED_COLOR = Color.GREEN;

    public ReserveTablePage(TablePage tablePage) {
        this.tablePage = tablePage;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Đặt Bàn", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        inputPanel.add(new JLabel("Tên Khách Hàng:"));
        nameField = new JTextField(15);
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Số Điện Thoại:"));
        phoneField = new JTextField(15);
        inputPanel.add(phoneField);

        JButton editButton = new JButton("Chỉnh Sửa");
        editButton.addActionListener(new EditButtonListener());
        inputPanel.add(editButton);

        JButton statusButton = new JButton("Trạng Thái");
        statusButton.addActionListener(new StatusButtonListener());
        inputPanel.add(statusButton);

        add(inputPanel, BorderLayout.SOUTH);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        tableButtons = new JButton[8];
        reservationMap = new HashMap<>();

        for (int i = 0; i < 8; i++) {
            tableButtons[i] = new JButton("bàn " + (char) ('A' + i));
            tableButtons[i].setBackground(AVAILABLE_COLOR);
            tableButtons[i].setPreferredSize(new Dimension(80, 80));
            tableButtons[i].addActionListener(new TableButtonListener());
            gbc.gridx = i % 4;
            gbc.gridy = i / 4;
            gridPanel.add(tableButtons[i], gbc);
        }

        add(gridPanel, BorderLayout.CENTER);

        // Tạo bảng hiển thị thông tin đặt bàn
        String[] columnNames = {"Bàn", "Khách Hàng", "Số Điện Thoại"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reservationTable = new JTable(tableModel);
        reservationTable.setPreferredScrollableViewportSize(new Dimension(400, 0));
        reservationTable.getSelectionModel().addListSelectionListener(e -> loadSelectedReservation());

        JScrollPane scrollPane = new JScrollPane(reservationTable);
        add(scrollPane, BorderLayout.EAST);
    }

    private class TableButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            String tableName = clickedButton.getText();
            String customerName = nameField.getText().trim();
            String phoneNumber = phoneField.getText().trim();

            if (clickedButton.getBackground().equals(OCCUPIED_COLOR)) {
                JOptionPane.showMessageDialog(null, "Bàn này hiện đang có người ngồi.");
                return;
            }

            if (customerName.isEmpty() || phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập tên và số điện thoại khách hàng.");
                return;
            }

            if (reservationMap.values().stream().anyMatch(info -> info[0].equals(customerName))) {
                JOptionPane.showMessageDialog(null, "Mỗi khách hàng chỉ có thể đặt 1 bàn.");
                return;
            }

            if (clickedButton.getBackground().equals(BOOKED_COLOR)) {
                int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn hủy đặt bàn " + tableName + "?", "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    reservationMap.remove(tableName);
                    clickedButton.setBackground(AVAILABLE_COLOR);
                    removeReservationFromTable(tableName);
                    tablePage.setTableStatus(tableName, AVAILABLE_COLOR);
                    JOptionPane.showMessageDialog(null, "Bàn " + tableName + " đã được hủy đặt.");
                }
            } else {
                reservationMap.put(tableName, new String[]{customerName, phoneNumber});
                clickedButton.setBackground(BOOKED_COLOR);
                addReservationToTable(tableName, customerName, phoneNumber);
                tablePage.setTableStatus(tableName, BOOKED_COLOR);
                JOptionPane.showMessageDialog(null, "Bàn " + tableName + " đã được đặt cho " + customerName);
            }
        }
    }

    private class StatusButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = reservationTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một đặt chỗ để chỉnh sửa trạng thái.");
                return;
            }

            String tableName = (String) tableModel.getValueAt(selectedRow, 0);
            String[] options = {"khách đã vào ", "Không Có Người Ngồi", "Hủy"};
            int choice = JOptionPane.showOptionDialog(null, "Chọn trạng thái của " + tableName,
                    "Chỉnh Sửa Trạng Thái Bàn", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            if (choice == 0) {
                tablePage.setTableStatus(tableName, OCCUPIED_COLOR);
                updateTableStatus(tableName, OCCUPIED_COLOR);
            } else if (choice == 1) {
                tablePage.setTableStatus(tableName, AVAILABLE_COLOR);
                updateTableStatus(tableName, AVAILABLE_COLOR);
            } else if (choice == 2) {
                tablePage.setTableStatus(tableName, BOOKED_COLOR);
                updateTableStatus(tableName, BOOKED_COLOR);
            }
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = reservationTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một đặt chỗ để chỉnh sửa.");
                return;
            }

            String tableName = (String) tableModel.getValueAt(selectedRow, 0);
            String customerName = (String) tableModel.getValueAt(selectedRow, 1);
            String phoneNumber = (String) tableModel.getValueAt(selectedRow, 2);

            EditCustomerDialog dialog = new EditCustomerDialog(customerName, phoneNumber);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                String newCustomerName = dialog.getCustomerName();
                String newPhoneNumber = dialog.getPhoneNumber();

                reservationMap.put(tableName, new String[]{newCustomerName, newPhoneNumber});
                tableModel.setValueAt(newCustomerName, selectedRow, 1);
                tableModel.setValueAt(newPhoneNumber, selectedRow, 2);

                JOptionPane.showMessageDialog(null, "Thông tin khách hàng đã được cập nhật.");
            }
        }
    }

    private void loadSelectedReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow != -1) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            phoneField.setText((String) tableModel.getValueAt(selectedRow, 2));
        }
    }

    private void addReservationToTable(String tableName, String customerName, String phoneNumber) {
        tableModel.addRow(new Object[]{tableName, customerName, phoneNumber});
    }

    private void removeReservationFromTable(String tableName) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(tableName)) {
                tableModel.removeRow(i);
                break;
            }
        }
    }

    public void updateTableStatus(String tableName, Color color) {
        for (JButton button : tableButtons) {
            if (button.getText().equals(tableName)) {
                button.setBackground(color);
                button.setEnabled(color != OCCUPIED_COLOR);
            }
        }
    }
}

class EditCustomerDialog extends JDialog {
    private JTextField customerNameField;
    private JTextField phoneNumberField;
    private boolean confirmed;

    public EditCustomerDialog(String customerName, String phoneNumber) {
        setTitle("Chỉnh Sửa Khách Hàng");
        setModal(true);
        setSize(300, 200);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Tên Khách Hàng:"));
        customerNameField = new JTextField(customerName);
        add(customerNameField);

        add(new JLabel("Số Điện Thoại:"));
        phoneNumberField = new JTextField(phoneNumber);
        add(phoneNumberField);

        JButton confirmButton = new JButton("Xác Nhận");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                setVisible(false);
            }
        });
        add(confirmButton);

        JButton cancelButton = new JButton("Hủy");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                setVisible(false);
            }
        });
        add(cancelButton);

        setLocationRelativeTo(null);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getCustomerName() {
        return customerNameField.getText().trim();
    }

    public String getPhoneNumber() {
        return phoneNumberField.getText().trim();
    }
}
