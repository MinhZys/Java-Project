package page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingMenuApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public SwingMenuApp() {
        setTitle("Menu Application");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tạo panel chứa các nút bên trái
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(8, 1, 10, 10));
        menuPanel.setBackground(Color.CYAN);

        JButton productButton = new JButton("Sản Phẩm");
        JButton tableButton = new JButton("Phòng Bàn");
        JButton reserveButton = new JButton("Đặt Bàn");
        JButton statsButton = new JButton("Thống Kê");
        JButton billButton = new JButton("Hóa Đơn");
        JButton otherButton = new JButton("Trang Khác");
        JButton warehouseButton = new JButton("Kho Hàng");
        JButton logoutButton = new JButton("Đăng Xuất");

        menuPanel.add(productButton);
        menuPanel.add(tableButton);
        menuPanel.add(reserveButton);
        menuPanel.add(statsButton);
        menuPanel.add(billButton);
        menuPanel.add(otherButton);
        menuPanel.add(warehouseButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);

        // Tạo panel chính với CardLayout để chứa các trang
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tạo các trang
        ReserveTablePage reserveTablePage = new ReserveTablePage(null);
        TablePage tablePage = new TablePage(reserveTablePage);

        reserveTablePage.tablePage = tablePage;

        // Thêm các trang vào mainPanel
        mainPanel.add(new ProductPage(), "Sản Phẩm");
        mainPanel.add(tablePage, "Phòng Bàn");
        mainPanel.add(reserveTablePage, "Đặt Bàn");
        mainPanel.add(new StatsPage(), "Thống Kê");
        mainPanel.add(new BillPage(), "Hóa Đơn");
        mainPanel.add(new OtherPage(), "Trang Khác");
      

        add(mainPanel, BorderLayout.CENTER);

        // Thêm sự kiện cho các nút để chuyển đổi trang
        productButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Sản Phẩm");
            }
        });

        tableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Phòng Bàn");
            }
        });

        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Đặt Bàn");
            }
        });

        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Thống Kê");
            }
        });

        billButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Hóa Đơn");
            }
        });

        otherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Trang Khác");
            }
        });

        warehouseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Kho Hàng");
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Đăng xuất thành công!");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SwingMenuApp().setVisible(true);
            }
        });
    }
}
