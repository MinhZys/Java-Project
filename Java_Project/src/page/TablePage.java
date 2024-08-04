package page;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TablePage extends JPanel {
    private JButton[] tableButtons;
    private Map<String, JButton> tableButtonMap;

    private final Color BOOKED_COLOR = Color.RED;
    private final Color AVAILABLE_COLOR = Color.GRAY;
    private final Color OCCUPIED_COLOR = Color.GREEN;

    private ReserveTablePage reserveTablePage;

    public TablePage(ReserveTablePage reserveTablePage) {
        this.reserveTablePage = reserveTablePage;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Phòng Bàn", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        tableButtons = new JButton[8];
        tableButtonMap = new HashMap<>();

        for (int i = 0; i < 8; i++) {
            String tableName = "bàn " + (char) ('A' + i);
            tableButtons[i] = new JButton(tableName);
            tableButtons[i].setBackground(AVAILABLE_COLOR);
            tableButtons[i].setPreferredSize(new Dimension(80, 80));
            tableButtons[i].addActionListener(e -> updateTableStatus(tableName));
            gbc.gridx = i % 4;
            gbc.gridy = i / 4;
            gridPanel.add(tableButtons[i], gbc);
            tableButtonMap.put(tableName, tableButtons[i]);
        }

        add(gridPanel, BorderLayout.CENTER);
    }

    public void updateTableStatus(String tableName) {
        JButton button = tableButtonMap.get(tableName);
        if (button != null) {
            String[] options = {"Có Người Ngồi", "Không Có Người Ngồi", "Hủy"};
            int choice = JOptionPane.showOptionDialog(null, "Chọn trạng thái của " + tableName,
                    "Chỉnh Sửa Trạng Thái Bàn", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            if (choice == 0) {
                button.setBackground(OCCUPIED_COLOR);
                reserveTablePage.updateTableStatus(tableName, OCCUPIED_COLOR);
            } else if (choice == 1) {
                button.setBackground(AVAILABLE_COLOR);
                reserveTablePage.updateTableStatus(tableName, AVAILABLE_COLOR);
            }
        }
    }

    public void setTableStatus(String tableName, Color color) {
        JButton button = tableButtonMap.get(tableName);
        if (button != null) {
            button.setBackground(color);
        }
    }
}

