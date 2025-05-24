import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class LibrarySystemGUI extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private Connection conn;
    private String currentUser = "";
    private String currentRole = "";

    // 登录界面的用户名和密码输入框
    private JTextField loginUserField = new JTextField(20);
    private JPasswordField loginPassField = new JPasswordField(20);

    // 颜色主题
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);  // 主色调
    private final Color SECONDARY_COLOR = new Color(245, 245, 245); // 背景色
    private final Color ACCENT_COLOR = new Color(255, 152, 0);    // 强调色
    private final Color TEXT_COLOR = new Color(33, 33, 33);       // 文本色
    private final Color BUTTON_HOVER = new Color(21, 101, 192);   // 按钮悬停色

    public LibrarySystemGUI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            // 全局UI设置
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("ProgressBar.arc", 8);
            UIManager.put("Button.borderWidth", 1);
            UIManager.put("TextField.caretBlinkRate", 500);
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.width", 12);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("图书借阅系统");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/img/book_icon.png")).getImage());

        connectDatabase();

        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createRegisterPanel(), "register");
        mainPanel.add(createAdminPanel(), "admin");
        mainPanel.add(createUserPanel(), "user");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        setVisible(true);
    }

    private void connectDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_db", "root", "123456");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "数据库连接失败: " + e.getMessage(),
                    "连接错误", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        // 创建左侧图像面板
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(PRIMARY_COLOR);
        imagePanel.setPreferredSize(new Dimension(400, 700));

        JLabel logoLabel = new JLabel("图书借阅系统", SwingConstants.CENTER);
        logoLabel.setFont(new Font("微软雅黑", Font.BOLD, 32));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(new EmptyBorder(30, 0, 0, 0));
        imagePanel.add(logoLabel, BorderLayout.NORTH);

        JLabel imageLabel = new JLabel(new ImageIcon(getClass().getResource("/img/library.png")));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JLabel copyrightLabel = new JLabel("© 2025 图书借阅系统", SwingConstants.CENTER);
        copyrightLabel.setForeground(new Color(255, 255, 255, 180));
        copyrightLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        imagePanel.add(copyrightLabel, BorderLayout.SOUTH);

        // 创建登录表单面板
        JPanel loginFormPanel = new JPanel();
        loginFormPanel.setLayout(new BoxLayout(loginFormPanel, BoxLayout.Y_AXIS));
        loginFormPanel.setBackground(SECONDARY_COLOR);
        loginFormPanel.setBorder(new EmptyBorder(100, 60, 100, 60));

        JLabel titleLabel = new JLabel("用户登录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("请输入您的账号和密码");
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(117, 117, 117));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 用户名输入框
        JLabel userLabel = new JLabel("用户名");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_COLOR);
        loginUserField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        loginUserField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        // 密码输入框
        JLabel passLabel = new JLabel("密码");
        passLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passLabel.setForeground(TEXT_COLOR);
        loginPassField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        loginPassField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        // 登录按钮
        JButton loginBtn = createStyledButton("登录", PRIMARY_COLOR, Color.WHITE);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 注册链接
        JPanel registerLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerLinkPanel.setBackground(SECONDARY_COLOR);
        JLabel registerLabel = new JLabel("没有账号？");
        registerLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        registerLabel.setForeground(TEXT_COLOR);

        JLabel registerLink = new JLabel("立即注册");
        registerLink.setFont(new Font("微软雅黑", Font.BOLD, 14));
        registerLink.setForeground(PRIMARY_COLOR);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerLinkPanel.add(registerLabel);
        registerLinkPanel.add(registerLink);

        // 添加组件到登录表单
        loginFormPanel.add(titleLabel);
        loginFormPanel.add(Box.createVerticalStrut(10));
        loginFormPanel.add(subtitleLabel);
        loginFormPanel.add(Box.createVerticalStrut(40));
        loginFormPanel.add(userLabel);
        loginFormPanel.add(Box.createVerticalStrut(8));
        loginFormPanel.add(loginUserField);
        loginFormPanel.add(Box.createVerticalStrut(20));
        loginFormPanel.add(passLabel);
        loginFormPanel.add(Box.createVerticalStrut(8));
        loginFormPanel.add(loginPassField);
        loginFormPanel.add(Box.createVerticalStrut(40));
        loginFormPanel.add(loginBtn);
        loginFormPanel.add(Box.createVerticalStrut(20));
        loginFormPanel.add(registerLinkPanel);

        // 登录事件处理
        loginBtn.addActionListener(e -> {
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
                ps.setString(1, loginUserField.getText());
                ps.setString(2, new String(loginPassField.getPassword()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    currentUser = rs.getString("username");
                    currentRole = rs.getString("role");

                    JDialog successDialog = new JDialog(this, "登录成功", true);
                    successDialog.setLayout(new BorderLayout());
                    successDialog.setSize(300, 150);
                    successDialog.setLocationRelativeTo(this);

                    JLabel successLabel = new JLabel("登录成功！欢迎回来，" + currentUser);
                    successLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    successLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
                    successLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));

                    JButton okButton = createStyledButton("确定", PRIMARY_COLOR, Color.WHITE);
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    buttonPanel.add(okButton);
                    buttonPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

                    successDialog.add(successLabel, BorderLayout.CENTER);
                    successDialog.add(buttonPanel, BorderLayout.SOUTH);

                    okButton.addActionListener(event -> {
                        successDialog.dispose();
                        if (currentRole.equals("admin")) {
                            cardLayout.show(mainPanel, "admin");
                        } else {
                            cardLayout.show(mainPanel, "user");
                        }
                    });

                    successDialog.setVisible(true);
                } else {
                    showErrorDialog("用户名或密码错误", "登录失败");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showErrorDialog("数据库错误: " + ex.getMessage(), "登录失败");
            }
        });

        // 切换注册界面
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "register");
            }
        });

        // 添加面板到主面板
        panel.add(imagePanel, BorderLayout.WEST);
        panel.add(loginFormPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        // 创建左侧图像面板 - 与登录界面类似，但颜色不同
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(76, 175, 80)); // 绿色主题
        imagePanel.setPreferredSize(new Dimension(400, 700));

        JLabel logoLabel = new JLabel("图书借阅系统", SwingConstants.CENTER);
        logoLabel.setFont(new Font("微软雅黑", Font.BOLD, 32));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(new EmptyBorder(30, 0, 0, 0));
        imagePanel.add(logoLabel, BorderLayout.NORTH);

        JLabel imageLabel = new JLabel(new ImageIcon(getClass().getResource("/img/register.png")));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JLabel copyrightLabel = new JLabel("© 2025 图书借阅系统", SwingConstants.CENTER);
        copyrightLabel.setForeground(new Color(255, 255, 255, 180));
        copyrightLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        imagePanel.add(copyrightLabel, BorderLayout.SOUTH);

        // 创建注册表单面板
        JPanel registerFormPanel = new JPanel();
        registerFormPanel.setLayout(new BoxLayout(registerFormPanel, BoxLayout.Y_AXIS));
        registerFormPanel.setBackground(SECONDARY_COLOR);
        registerFormPanel.setBorder(new EmptyBorder(100, 60, 100, 60));

        JLabel titleLabel = new JLabel("新用户注册");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("创建您的账号");
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(117, 117, 117));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 用户名输入框
        JLabel userLabel = new JLabel("用户名");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_COLOR);
        JTextField userField = new JTextField(20);
        userField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        // 密码输入框
        JLabel passLabel = new JLabel("密码");
        passLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passLabel.setForeground(TEXT_COLOR);
        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        // 确认密码输入框
        JLabel confirmPassLabel = new JLabel("确认密码");
        confirmPassLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        confirmPassLabel.setForeground(TEXT_COLOR);
        JPasswordField confirmPassField = new JPasswordField(20);
        confirmPassField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        confirmPassField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        // 注册按钮
        JButton registerBtn = createStyledButton("注册", new Color(76, 175, 80), Color.WHITE);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 返回登录链接
        JPanel loginLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginLinkPanel.setBackground(SECONDARY_COLOR);
        JLabel loginLabel = new JLabel("已有账号？");
        loginLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        loginLabel.setForeground(TEXT_COLOR);

        JLabel loginLink = new JLabel("返回登录");
        loginLink.setFont(new Font("微软雅黑", Font.BOLD, 14));
        loginLink.setForeground(new Color(76, 175, 80));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginLinkPanel.add(loginLabel);
        loginLinkPanel.add(loginLink);

        // 添加组件到注册表单
        registerFormPanel.add(titleLabel);
        registerFormPanel.add(Box.createVerticalStrut(10));
        registerFormPanel.add(subtitleLabel);
        registerFormPanel.add(Box.createVerticalStrut(40));
        registerFormPanel.add(userLabel);
        registerFormPanel.add(Box.createVerticalStrut(8));
        registerFormPanel.add(userField);
        registerFormPanel.add(Box.createVerticalStrut(20));
        registerFormPanel.add(passLabel);
        registerFormPanel.add(Box.createVerticalStrut(8));
        registerFormPanel.add(passField);
        registerFormPanel.add(Box.createVerticalStrut(20));
        registerFormPanel.add(confirmPassLabel);
        registerFormPanel.add(Box.createVerticalStrut(8));
        registerFormPanel.add(confirmPassField);
        registerFormPanel.add(Box.createVerticalStrut(40));
        registerFormPanel.add(registerBtn);
        registerFormPanel.add(Box.createVerticalStrut(20));
        registerFormPanel.add(loginLinkPanel);

        // 注册按钮逻辑
        registerBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String confirmPassword = new String(confirmPassField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                showErrorDialog("用户名和密码不能为空", "注册失败");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showErrorDialog("两次输入的密码不一致", "注册失败");
                return;
            }

            try {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, 'user')");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();

                JDialog successDialog = new JDialog(this, "注册成功", true);
                successDialog.setLayout(new BorderLayout());
                successDialog.setSize(300, 150);
                successDialog.setLocationRelativeTo(this);

                JLabel successLabel = new JLabel("注册成功！请登录系统");
                successLabel.setHorizontalAlignment(SwingConstants.CENTER);
                successLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
                successLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));

                JButton okButton = createStyledButton("确定", new Color(76, 175, 80), Color.WHITE);
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.add(okButton);
                buttonPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

                successDialog.add(successLabel, BorderLayout.CENTER);
                successDialog.add(buttonPanel, BorderLayout.SOUTH);

                okButton.addActionListener(event -> {
                    successDialog.dispose();
                    cardLayout.show(mainPanel, "login");
                });

                successDialog.setVisible(true);
            } catch (SQLException ex) {
                showErrorDialog("注册失败：用户名已存在或数据库错误", "注册失败");
                ex.printStackTrace();
            }
        });

        // 返回登录按钮逻辑
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "login");
            }
        });

        // 添加面板到主面板
        panel.add(imagePanel, BorderLayout.WEST);
        panel.add(registerFormPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // 创建顶部标题栏
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("图书借阅系统 - 管理员控制台");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoPanel.setOpaque(false);

        JLabel userLabel = new JLabel("欢迎, admin");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("退出登录");
        logoutBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(255, 255, 255, 40));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        userInfoPanel.add(userLabel);
        userInfoPanel.add(Box.createHorizontalStrut(15));
        userInfoPanel.add(logoutBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userInfoPanel, BorderLayout.EAST);

        // 创建侧边菜单
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(33, 33, 33));
        sidePanel.setPreferredSize(new Dimension(220, 0));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // 创建功能按钮
        JButton[] menuButtons = new JButton[6];
        String[] btnLabels = {
                "查找图书", "新增图书", "删除图书", "显示图书", "查看借阅记录", "系统设置"
        };
        String[] btnIcons = {
                "search", "add", "delete", "list", "history", "settings"
        };

        for (int i = 0; i < btnLabels.length; i++) {
            menuButtons[i] = createMenuButton(btnLabels[i], btnIcons[i]);
            sidePanel.add(menuButtons[i]);
            sidePanel.add(Box.createVerticalStrut(5));
        }

        // 创建主内容区域
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel welcomeLabel = new JLabel("欢迎使用图书借阅系统管理员控制台");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(33, 33, 33));

        JLabel dateLabel = new JLabel("当前日期: " + java.time.LocalDate.now());
        dateLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        dateLabel.setForeground(new Color(117, 117, 117));

        // 创建统计信息面板
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);

        String[] statsTitles = {"总图书数量", "总借阅次数", "当前借出数量"};
        Color[] statsColors = {
                new Color(25, 118, 210), // 蓝色
                new Color(76, 175, 80),  // 绿色
                new Color(255, 152, 0)   // 橙色
        };

        for (int i = 0; i < 3; i++) {
            JPanel stat = createStatPanel(statsTitles[i], "获取中...", statsColors[i]);
            statsPanel.add(stat);
        }

        // 添加最近借阅表格
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][] {
                        {"系统启动中，请稍候..."}
                },
                new String[] {"最近借阅记录"}
        );

        JTable recentBorrowTable = new JTable(tableModel);
        recentBorrowTable.setRowHeight(35);
        recentBorrowTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        recentBorrowTable.setShowGrid(false);
        recentBorrowTable.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = recentBorrowTable.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(66, 66, 66));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JScrollPane scrollPane = new JScrollPane(recentBorrowTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // 布局内容区域
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        welcomePanel.add(dateLabel, BorderLayout.CENTER);

        JPanel topContent = new JPanel(new BorderLayout(0, 30));
        topContent.setOpaque(false);
        topContent.add(welcomePanel, BorderLayout.NORTH);
        topContent.add(statsPanel, BorderLayout.CENTER);

        contentPanel.add(topContent, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // 设置菜单按钮事件
        menuButtons[0].addActionListener(e -> searchBooks());
        menuButtons[1].addActionListener(e -> addBook());
        menuButtons[2].addActionListener(e -> deleteBook());
        menuButtons[3].addActionListener(e -> displayBooks());
        menuButtons[4].addActionListener(e -> viewAllBorrowHistory());
        menuButtons[5].addActionListener(e -> JOptionPane.showMessageDialog(
                this, "系统设置功能正在开发中", "提示", JOptionPane.INFORMATION_MESSAGE));

        // 退出登录事件
        logoutBtn.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    this, "确定要退出登录吗？", "退出确认",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                currentUser = "";
                currentRole = "";
                clearLoginFields();
                cardLayout.show(mainPanel, "login");
            }
        });

        // 更新用户标签
        userLabel.setText("欢迎, " + currentUser);

        // 组装面板
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(sidePanel, BorderLayout.WEST);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // 创建顶部标题栏
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(76, 175, 80)); // 绿色主题
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("图书借阅系统 - 用户中心");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoPanel.setOpaque(false);

        JLabel userLabel = new JLabel("欢迎, user");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("退出登录");
        logoutBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(255, 255, 255, 40));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        userInfoPanel.add(userLabel);
        userInfoPanel.add(Box.createHorizontalStrut(15));
        userInfoPanel.add(logoutBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userInfoPanel, BorderLayout.EAST);

        // 创建侧边菜单
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(33, 33, 33));
        sidePanel.setPreferredSize(new Dimension(220, 0));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // 创建功能按钮
        JButton[] menuButtons = new JButton[5];
        String[] btnLabels = {
                "查找图书", "借阅图书", "归还图书", "我的借阅记录", "用户设置"
        };
        String[] btnIcons = {
                "search", "borrow", "return", "history", "settings"
        };

        for (int i = 0; i < btnLabels.length; i++) {
            menuButtons[i] = createMenuButton(btnLabels[i], btnIcons[i]);
            sidePanel.add(menuButtons[i]);
            sidePanel.add(Box.createVerticalStrut(5));
        }

        // 创建主内容区域
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel welcomeLabel = new JLabel("欢迎使用图书借阅系统");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(33, 33, 33));

        JLabel dateLabel = new JLabel("当前日期: " + java.time.LocalDate.now());
        dateLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        dateLabel.setForeground(new Color(117, 117, 117));

        // 创建统计信息面板
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setOpaque(false);

        JPanel borrowedBooks = createStatPanel("我已借阅", "获取中...", new Color(76, 175, 80));
        JPanel overdueBooks = createStatPanel("即将到期", "获取中...", new Color(255, 152, 0));

        statsPanel.add(borrowedBooks);
        statsPanel.add(overdueBooks);

        // 添加推荐书籍列表
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][] {
                        {"系统启动中，请稍候..."}
                },
                new String[] {"推荐图书"}
        );

        JTable recommendedBooks = new JTable(tableModel);
        recommendedBooks.setRowHeight(35);
        recommendedBooks.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        recommendedBooks.setShowGrid(false);
        recommendedBooks.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = recommendedBooks.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(66, 66, 66));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JScrollPane scrollPane = new JScrollPane(recommendedBooks);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // 布局内容区域
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        welcomePanel.add(dateLabel, BorderLayout.CENTER);

        JPanel topContent = new JPanel(new BorderLayout(0, 30));
        topContent.setOpaque(false);
        topContent.add(welcomePanel, BorderLayout.NORTH);
        topContent.add(statsPanel, BorderLayout.CENTER);

        contentPanel.add(topContent, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // 设置菜单按钮事件
        menuButtons[0].addActionListener(e -> searchBooks());
        menuButtons[1].addActionListener(e -> borrowBook());
        menuButtons[2].addActionListener(e -> returnBook());
        menuButtons[3].addActionListener(e -> viewMyBorrowHistory());
        menuButtons[4].addActionListener(e -> JOptionPane.showMessageDialog(
                this, "用户设置功能正在开发中", "提示", JOptionPane.INFORMATION_MESSAGE));

        // 退出登录事件
        logoutBtn.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    this, "确定要退出登录吗？", "退出确认",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                currentUser = "";
                currentRole = "";
                clearLoginFields();
                cardLayout.show(mainPanel, "login");
            }
        });

        // 更新用户标签
        userLabel.setText("欢迎, " + currentUser);

        // 组装面板
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(sidePanel, BorderLayout.WEST);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // 创建菜单按钮
    private JButton createMenuButton(String text, String iconName) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(33, 33, 33));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(220, 50));

        // 悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(66, 66, 66));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(33, 33, 33));
            }
        });

        return button;
    }

    // 创建统计面板
    private JPanel createStatPanel(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JPanel colorStrip = new JPanel();
        colorStrip.setBackground(color);
        colorStrip.setPreferredSize(new Dimension(0, 5));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setForeground(new Color(66, 66, 66));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        panel.add(colorStrip, BorderLayout.NORTH);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(valueLabel, BorderLayout.SOUTH);

        return panel;
    }

    // 创建样式化按钮
    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setForeground(fg);
        button.setBackground(bg);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 添加悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(darken(bg, 0.1f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bg);
            }
        });

        return button;
    }

    // 调整颜色亮度的辅助方法
    private Color darken(Color color, float fraction) {
        int red = Math.max(0, Math.min(255, (int)(color.getRed() * (1 - fraction))));
        int green = Math.max(0, Math.min(255, (int)(color.getGreen() * (1 - fraction))));
        int blue = Math.max(0, Math.min(255, (int)(color.getBlue() * (1 - fraction))));
        return new Color(red, green, blue);
    }

    // 显示错误对话框
    private void showErrorDialog(String message, String title) {
        JDialog errorDialog = new JDialog(this, title, true);
        errorDialog.setLayout(new BorderLayout());
        errorDialog.setSize(350, 180);
        errorDialog.setLocationRelativeTo(this);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        messagePanel.setBackground(Color.WHITE);

        JLabel errorLabel = new JLabel(message);
        errorLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton okButton = createStyledButton("确定", new Color(239, 83, 80), Color.WHITE);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(okButton);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        messagePanel.add(errorLabel, BorderLayout.CENTER);

        errorDialog.add(messagePanel, BorderLayout.CENTER);
        errorDialog.add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> errorDialog.dispose());

        errorDialog.setVisible(true);
    }

    // 清空登录界面的用户名和密码输入框的方法
    private void clearLoginFields() {
        loginUserField.setText("");
        loginPassField.setText("");
    }

    // 搜索图书实现
    private void searchBooks() {
        // 创建一个自定义的搜索对话框
        JDialog searchDialog = new JDialog(this, "图书查询", true);
        searchDialog.setLayout(new BorderLayout());
        searchDialog.setSize(600, 450);
        searchDialog.setLocationRelativeTo(this);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        searchPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("图书查询");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        JButton searchButton = createStyledButton("搜索", PRIMARY_COLOR, Color.WHITE);
        searchButton.setPreferredSize(new Dimension(100, 38));

        inputPanel.add(searchField, BorderLayout.CENTER);
        inputPanel.add(searchButton, BorderLayout.EAST);

        // 结果表格
        DefaultTableModel model = new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID", "书名", "作者", "库存"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };

        JTable resultTable = new JTable(model);
        resultTable.setRowHeight(30);
        resultTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        resultTable.setShowVerticalLines(false);
        resultTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        resultTable.getTableHeader().setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);

        JButton closeButton = createStyledButton("关闭", new Color(117, 117, 117), Color.WHITE);

        if (currentRole.equals("admin")) {
            JButton editButton = createStyledButton("编辑", new Color(76, 175, 80), Color.WHITE);
            actionPanel.add(editButton);

            // 编辑按钮功能
            editButton.addActionListener(e -> {
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow != -1) {
                    // 获取选中图书信息
                    int bookId = (int) model.getValueAt(selectedRow, 0);
                    String title = (String) model.getValueAt(selectedRow, 1);
                    String author = (String) model.getValueAt(selectedRow, 2);
                    int quantity = (int) model.getValueAt(selectedRow, 3);

                    // 弹出编辑对话框
                    JDialog editDialog = new JDialog(searchDialog, "编辑图书", true);
                    editDialog.setLayout(new BorderLayout());
                    editDialog.setSize(400, 300);
                    editDialog.setLocationRelativeTo(searchDialog);

                    JPanel editPanel = new JPanel(new GridBagLayout());
                    editPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
                    editPanel.setBackground(Color.WHITE);

                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    gbc.insets = new Insets(5, 5, 5, 5);

                    // 标题
                    JLabel idLabel = new JLabel("图书ID:");
                    JTextField idField = new JTextField(String.valueOf(bookId));
                    idField.setEditable(false);

                    JLabel titleFieldLabel = new JLabel("书名:");
                    JTextField titleField = new JTextField(title);

                    JLabel authorFieldLabel = new JLabel("作者:");
                    JTextField authorField = new JTextField(author);

                    JLabel qtyLabel = new JLabel("库存:");
                    JTextField qtyField = new JTextField(String.valueOf(quantity));

                    // 设置字体
                    Font labelFont = new Font("微软雅黑", Font.PLAIN, 14);
                    idLabel.setFont(labelFont);
                    titleFieldLabel.setFont(labelFont);
                    authorFieldLabel.setFont(labelFont);
                    qtyLabel.setFont(labelFont);

                    Font fieldFont = new Font("微软雅黑", Font.PLAIN, 14);
                    idField.setFont(fieldFont);
                    titleField.setFont(fieldFont);
                    authorField.setFont(fieldFont);
                    qtyField.setFont(fieldFont);

                    // 添加组件
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    editPanel.add(idLabel, gbc);

                    gbc.gridx = 1;
                    gbc.weightx = 1;
                    editPanel.add(idField, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 1;
                    gbc.weightx = 0;
                    editPanel.add(titleFieldLabel, gbc);

                    gbc.gridx = 1;
                    gbc.weightx = 1;
                    editPanel.add(titleField, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 2;
                    gbc.weightx = 0;
                    editPanel.add(authorFieldLabel, gbc);

                    gbc.gridx = 1;
                    gbc.weightx = 1;
                    editPanel.add(authorField, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 3;
                    gbc.weightx = 0;
                    editPanel.add(qtyLabel, gbc);

                    gbc.gridx = 1;
                    gbc.weightx = 1;
                    editPanel.add(qtyField, gbc);

                    // 按钮面板
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    buttonPanel.setBackground(Color.WHITE);

                    JButton saveButton = createStyledButton("保存", new Color(76, 175, 80), Color.WHITE);
                    JButton cancelButton = createStyledButton("取消", new Color(117, 117, 117), Color.WHITE);

                    buttonPanel.add(saveButton);
                    buttonPanel.add(cancelButton);

                    // 保存按钮功能
                    saveButton.addActionListener(event -> {
                        try {
                            String newTitle = titleField.getText().trim();
                            String newAuthor = authorField.getText().trim();
                            int newQty = Integer.parseInt(qtyField.getText().trim());

                            if (newTitle.isEmpty() || newAuthor.isEmpty()) {
                                showErrorDialog("书名和作者不能为空", "编辑失败");
                                return;
                            }

                            // 更新数据库
                            PreparedStatement ps = conn.prepareStatement(
                                    "UPDATE books SET title=?, author=?, quantity=? WHERE id=?");
                            ps.setString(1, newTitle);
                            ps.setString(2, newAuthor);
                            ps.setInt(3, newQty);
                            ps.setInt(4, bookId);
                            ps.executeUpdate();

                            // 更新表格
                            model.setValueAt(newTitle, selectedRow, 1);
                            model.setValueAt(newAuthor, selectedRow, 2);
                            model.setValueAt(newQty, selectedRow, 3);

                            editDialog.dispose();
                            JOptionPane.showMessageDialog(searchDialog, "图书信息已更新",
                                    "更新成功", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            showErrorDialog("更新失败: " + ex.getMessage(), "编辑失败");
                        }
                    });

                    // 取消按钮功能
                    cancelButton.addActionListener(event -> editDialog.dispose());

                    editDialog.add(editPanel, BorderLayout.CENTER);
                    editDialog.add(buttonPanel, BorderLayout.SOUTH);
                    editDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(searchDialog, "请选择要编辑的图书",
                            "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }

        actionPanel.add(closeButton);

        searchPanel.add(titleLabel, BorderLayout.NORTH);
        searchPanel.add(inputPanel, BorderLayout.CENTER);

        searchDialog.add(searchPanel, BorderLayout.NORTH);
        searchDialog.add(scrollPane, BorderLayout.CENTER);
        searchDialog.add(actionPanel, BorderLayout.SOUTH);

        // 搜索按钮功能
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                // 清空表格
                model.setRowCount(0);
                return;
            }

            try {
                // 判断是否为数字（尝试匹配 ID）
                boolean isNumeric = keyword.matches("\\d+");

                PreparedStatement ps;
                if (isNumeric) {
                    // 如果是数字，尝试按 ID 或书名/作者匹配
                    ps = conn.prepareStatement(
                            "SELECT * FROM books WHERE id = ? OR title LIKE ? OR author LIKE ?");
                    ps.setInt(1, Integer.parseInt(keyword));
                    ps.setString(2, "%" + keyword + "%");
                    ps.setString(3, "%" + keyword + "%");
                } else {
                    // 非数字，仅按书名和作者模糊匹配
                    ps = conn.prepareStatement(
                            "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?");
                    ps.setString(1, "%" + keyword + "%");
                    ps.setString(2, "%" + keyword + "%");
                }

                ResultSet rs = ps.executeQuery();

                // 清空表格
                model.setRowCount(0);

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("quantity")
                    });
                }

                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(searchDialog, "未找到匹配的图书",
                            "查询结果", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(searchDialog, "查询失败: " + ex.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 关闭按钮功能
        closeButton.addActionListener(e -> searchDialog.dispose());

        // 按回车键触发搜索
        searchField.addActionListener(e -> searchButton.doClick());

        searchDialog.setVisible(true);
    }

    private void addBook() {
        JDialog addDialog = new JDialog(this, "新增图书", true);
        addDialog.setLayout(new BorderLayout());
        addDialog.setSize(450, 350);
        addDialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        JLabel titleLabel = new JLabel("添加新图书");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));

        JLabel bookTitleLabel = new JLabel("书名:");
        JTextField titleField = new JTextField(20);

        JLabel authorLabel = new JLabel("作者:");
        JTextField authorField = new JTextField(20);

        JLabel publisherLabel = new JLabel("出版社:");
        JTextField publisherField = new JTextField(20);

        JLabel qtyLabel = new JLabel("数量:");
        JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbnField = new JTextField(20);

        Font labelFont = new Font("微软雅黑", Font.PLAIN, 14);
        bookTitleLabel.setFont(labelFont);
        authorLabel.setFont(labelFont);
        publisherLabel.setFont(labelFont);
        qtyLabel.setFont(labelFont);
        isbnLabel.setFont(labelFont);

        Font fieldFont = new Font("微软雅黑", Font.PLAIN, 14);
        titleField.setFont(fieldFont);
        authorField.setFont(fieldFont);
        publisherField.setFont(fieldFont);
        qtySpinner.setFont(fieldFont);
        isbnField.setFont(fieldFont);

        // 标题跨越两列
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // 重置为单列宽度
        gbc.gridwidth = 1;

        // 书名
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(bookTitleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(titleField, gbc);

        // 作者
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(authorLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(authorField, gbc);

        // 出版社
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        formPanel.add(publisherLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(publisherField, gbc);

        // ISBN
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        formPanel.add(isbnLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(isbnField, gbc);

        // 数量
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        formPanel.add(qtyLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(qtySpinner, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("添加", new Color(76, 175, 80), Color.WHITE);
        JButton cancelButton = createStyledButton("取消", new Color(117, 117, 117), Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        // 添加按钮功能
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String publisher = publisherField.getText().trim();
            String isbn = isbnField.getText().trim();
            int quantity = (int) qtySpinner.getValue();

            if (title.isEmpty() || author.isEmpty()) {
                showErrorDialog("书名和作者不能为空", "添加失败");
                return;
            }

            try {
                // 实际项目中可以增加出版社和ISBN字段
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)");
                ps.setString(1, title);
                ps.setString(2, author);
                ps.setInt(3, quantity);
                ps.executeUpdate();

                addDialog.dispose();

                JOptionPane.showMessageDialog(this, "图书添加成功",
                        "添加成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                showErrorDialog("添加失败: " + ex.getMessage(), "添加失败");
            }
        });

        // 取消按钮功能
        cancelButton.addActionListener(e -> addDialog.dispose());

        addDialog.add(formPanel, BorderLayout.CENTER);
        addDialog.add(buttonPanel, BorderLayout.SOUTH);

        addDialog.setVisible(true);
    }

    private void deleteBook() {
        JDialog deleteDialog = new JDialog(this, "删除图书", true);
        deleteDialog.setLayout(new BorderLayout());
        deleteDialog.setSize(450, 250);
        deleteDialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        JLabel titleLabel = new JLabel("删除图书");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(new Color(211, 47, 47)); // 警告色

        JLabel warningLabel = new JLabel("警告：此操作不可恢复，请确认！");
        warningLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        warningLabel.setForeground(new Color(211, 47, 47));

        JLabel idLabel = new JLabel("图书ID:");
        JTextField idField = new JTextField(20);

        // 设置字体
        idLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        idField.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // 标题跨越两列
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // 警告信息
        gbc.gridy = 1;
        formPanel.add(warningLabel, gbc);

        // 重置为单列宽度
        gbc.gridwidth = 1;

        // 图书ID
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(idField, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton deleteButton = createStyledButton("删除", new Color(211, 47, 47), Color.WHITE);
        JButton cancelButton = createStyledButton("取消", new Color(117, 117, 117), Color.WHITE);

        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);

        // 删除按钮功能
        deleteButton.addActionListener(e -> {
            String bookIdStr = idField.getText().trim();

            if (bookIdStr.isEmpty()) {
                showErrorDialog("请输入图书ID", "删除失败");
                return;
            }

            try {
                int bookId = Integer.parseInt(bookIdStr);

                // 检查图书是否存在
                PreparedStatement checkPs = conn.prepareStatement(
                        "SELECT title FROM books WHERE id = ?");
                checkPs.setInt(1, bookId);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    String bookTitle = rs.getString("title");

                    // 确认删除
                    int option = JOptionPane.showConfirmDialog(
                            deleteDialog,
                            "确定要删除图书 \"" + bookTitle + "\" (ID: " + bookId + ")？\n此操作不可恢复！",
                            "确认删除",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (option == JOptionPane.YES_OPTION) {
                        PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id = ?");
                        ps.setInt(1, bookId);
                        int affected = ps.executeUpdate();

                        deleteDialog.dispose();

                        if (affected > 0) {
                            JOptionPane.showMessageDialog(this, "图书已成功删除",
                                    "删除成功", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } else {
                    showErrorDialog("未找到ID为 " + bookId + " 的图书", "删除失败");
                }
            } catch (NumberFormatException ex) {
                showErrorDialog("无效的图书ID：请输入数字", "删除失败");
            } catch (SQLException ex) {
                ex.printStackTrace();
                showErrorDialog("删除失败: " + ex.getMessage(), "删除失败");
            }
        });

        // 取消按钮功能
        cancelButton.addActionListener(e -> deleteDialog.dispose());

        deleteDialog.add(formPanel, BorderLayout.CENTER);
        deleteDialog.add(buttonPanel, BorderLayout.SOUTH);

        deleteDialog.setVisible(true);
    }

    private void displayBooks() {
        JDialog booksDialog = new JDialog(this, "图书列表", true);
        booksDialog.setLayout(new BorderLayout());
        booksDialog.setSize(800, 500);
        booksDialog.setLocationRelativeTo(this);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("图书列表");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));

        // 搜索和刷新按钮
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);

        JButton refreshButton = new JButton("刷新");
        refreshButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshButton.setFocusPainted(false);

        actionPanel.add(refreshButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(actionPanel, BorderLayout.EAST);

        // 表格模型
        DefaultTableModel model = new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID", "书名", "作者", "库存", "状态"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };

        JTable booksTable = new JTable(model);
        booksTable.setRowHeight(30);
        booksTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        booksTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        booksTable.getTableHeader().setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // 底部状态栏
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        statusPanel.setBackground(new Color(240, 240, 240));

        JLabel countLabel = new JLabel("共 0 本图书");
        countLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JButton closeButton = createStyledButton("关闭", new Color(117, 117, 117), Color.WHITE);

        statusPanel.add(countLabel, BorderLayout.WEST);
        statusPanel.add(closeButton, BorderLayout.EAST);

        booksDialog.add(headerPanel, BorderLayout.NORTH);
        booksDialog.add(scrollPane, BorderLayout.CENTER);
        booksDialog.add(statusPanel, BorderLayout.SOUTH);

        // 加载图书数据
        loadBooksData(model, countLabel);

        // 刷新按钮功能
        refreshButton.addActionListener(e -> loadBooksData(model, countLabel));

        // 关闭按钮功能
        closeButton.addActionListener(e -> booksDialog.dispose());

        booksDialog.setVisible(true);
    }

    // 加载图书数据到表格
    private void loadBooksData(DefaultTableModel model, JLabel countLabel) {
        try {
            model.setRowCount(0); // 清空表格

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books ORDER BY id");

            int count = 0;
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                String status = quantity > 0 ? "可借阅" : "已借完";

                model.addRow(new Object[]{id, title, author, quantity, status});
                count++;
            }

            countLabel.setText("共 " + count + " 本图书");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "加载图书数据失败: " + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrowBook() {
        JDialog borrowDialog = new JDialog(this, "借阅图书", true);
        borrowDialog.setLayout(new BorderLayout());
        borrowDialog.setSize(800, 500);
        borrowDialog.setLocationRelativeTo(this);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("可借阅图书列表");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));

        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField(15);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        JButton searchButton = new JButton("搜索");
        searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchButton.setFocusPainted(false);

        searchPanel.add(new JLabel("搜索: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        // 表格模型
        DefaultTableModel model = new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID", "书名", "作者", "库存"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };

        JTable booksTable = new JTable(model);
        booksTable.setRowHeight(30);
        booksTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        booksTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        booksTable.getTableHeader().setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // 底部操作面板
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        actionPanel.setBackground(new Color(240, 240, 240));

        JPanel borrowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        borrowPanel.setOpaque(false);

        JLabel bookIdLabel = new JLabel("图书ID:");
        JTextField bookIdField = new JTextField(8);
        bookIdField.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        borrowPanel.add(bookIdLabel);
        borrowPanel.add(bookIdField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton borrowButton = createStyledButton("借阅", new Color(76, 175, 80), Color.WHITE);
        JButton closeButton = createStyledButton("关闭", new Color(117, 117, 117), Color.WHITE);

        buttonPanel.add(borrowButton);
        buttonPanel.add(closeButton);

        actionPanel.add(borrowPanel, BorderLayout.WEST);
        actionPanel.add(buttonPanel, BorderLayout.EAST);

        borrowDialog.add(headerPanel, BorderLayout.NORTH);
        borrowDialog.add(scrollPane, BorderLayout.CENTER);
        borrowDialog.add(actionPanel, BorderLayout.SOUTH);

        // 加载可借阅图书数据
        loadAvailableBooks(model);

        // 表格选择图书时自动填充ID
        booksTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = booksTable.getSelectedRow();
                if (selectedRow >= 0) {
                    bookIdField.setText(model.getValueAt(selectedRow, 0).toString());
                }
            }
        });

        // 搜索功能
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                loadAvailableBooks(model);
            } else {
                searchAvailableBooks(model, keyword);
            }
        });

        // 按回车搜索
        searchField.addActionListener(e -> searchButton.doClick());

        // 借阅按钮功能
        borrowButton.addActionListener(e -> {
            String bookId = bookIdField.getText().trim();
            if (bookId.isEmpty()) {
                JOptionPane.showMessageDialog(borrowDialog, "请输入或选择图书ID",
                        "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                conn.setAutoCommit(false);

                // 检查库存
                PreparedStatement check = conn.prepareStatement("SELECT title, quantity FROM books WHERE id = ?");
                check.setInt(1, Integer.parseInt(bookId));
                ResultSet rs = check.executeQuery();

                if (rs.next() && rs.getInt("quantity") > 0) {
                    String bookTitle = rs.getString("title");

                    // 确认借阅
                    int option = JOptionPane.showConfirmDialog(
                            borrowDialog,
                            "确定要借阅《" + bookTitle + "》？",
                            "确认借阅",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        // 扣减库存
                        PreparedStatement updateQty = conn.prepareStatement(
                                "UPDATE books SET quantity = quantity - 1 WHERE id = ?");
                        updateQty.setInt(1, Integer.parseInt(bookId));
                        updateQty.executeUpdate();

                        // 记录借阅
                        PreparedStatement insert = conn.prepareStatement(
                                "INSERT INTO borrow_records (username, book_id, borrow_date) VALUES (?, ?, NOW())");
                        insert.setString(1, currentUser);
                        insert.setInt(2, Integer.parseInt(bookId));
                        insert.executeUpdate();

                        conn.commit();

                        JOptionPane.showMessageDialog(borrowDialog, "借阅成功！",
                                "借阅成功", JOptionPane.INFORMATION_MESSAGE);

                        // 刷新表格
                        loadAvailableBooks(model);
                    }
                } else {
                    JOptionPane.showMessageDialog(borrowDialog, "无效的图书ID或库存不足",
                            "借阅失败", JOptionPane.ERROR_MESSAGE);
                    conn.rollback();
                }

                conn.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                try { conn.rollback(); } catch (SQLException ignored) {}
                JOptionPane.showMessageDialog(borrowDialog, "借阅失败: " + ex.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
            }
        });

        // 关闭按钮功能
        closeButton.addActionListener(e -> borrowDialog.dispose());

        borrowDialog.setVisible(true);
    }

    // 加载可借阅图书数据
    private void loadAvailableBooks(DefaultTableModel model) {
        try {
            model.setRowCount(0); // 清空表格

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books WHERE quantity > 0 ORDER BY id");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "加载图书数据失败: " + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 搜索可借阅图书
    private void searchAvailableBooks(DefaultTableModel model, String keyword) {
        try {
            model.setRowCount(0); // 清空表格

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM books WHERE quantity > 0 AND (title LIKE ? OR author LIKE ? OR id = ?) ORDER BY id");
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            // 尝试将关键词转换为ID
            try {
                ps.setInt(3, Integer.parseInt(keyword));
            } catch (NumberFormatException e) {
                ps.setInt(3, -1); // 不可能匹配的ID
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "搜索失败: " + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBook() {
        JDialog returnDialog = new JDialog(this, "归还图书", true);
        returnDialog.setLayout(new BorderLayout());
        returnDialog.setSize(800, 500);
        returnDialog.setLocationRelativeTo(this);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("我的借阅记录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));

        JButton refreshButton = new JButton("刷新");
        refreshButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshButton.setFocusPainted(false);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);

        // 表格模型
        DefaultTableModel model = new DefaultTableModel(
                new Object[][] {},
                new String[] {"记录ID", "书名", "借阅时间", "状态"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };

        JTable borrowTable = new JTable(model);
        borrowTable.setRowHeight(30);
        borrowTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        borrowTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        borrowTable.getTableHeader().setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(borrowTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // 底部操作面板
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        actionPanel.setBackground(new Color(240, 240, 240));

        JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        returnPanel.setOpaque(false);

        JLabel recordIdLabel = new JLabel("记录ID:");
        JTextField recordIdField = new JTextField(8);
        recordIdField.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        returnPanel.add(recordIdLabel);
        returnPanel.add(recordIdField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton returnButton = createStyledButton("归还", new Color(76, 175, 80), Color.WHITE);
        JButton closeButton = createStyledButton("关闭", new Color(117, 117, 117), Color.WHITE);

        buttonPanel.add(returnButton);
        buttonPanel.add(closeButton);

        actionPanel.add(returnPanel, BorderLayout.WEST);
        actionPanel.add(buttonPanel, BorderLayout.EAST);

        returnDialog.add(headerPanel, BorderLayout.NORTH);
        returnDialog.add(scrollPane, BorderLayout.CENTER);
        returnDialog.add(actionPanel, BorderLayout.SOUTH);

        // 加载未归还的借阅记录
        loadUnreturnedBooks(model);

        // 表格选择记录时自动填充ID
        borrowTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = borrowTable.getSelectedRow();
                if (selectedRow >= 0) {
                    recordIdField.setText(model.getValueAt(selectedRow, 0).toString());
                }
            }
        });

        // 刷新按钮功能
        refreshButton.addActionListener(e -> loadUnreturnedBooks(model));

        // 归还按钮功能
        returnButton.addActionListener(e -> {
            String recordId = recordIdField.getText().trim();
            if (recordId.isEmpty()) {
                JOptionPane.showMessageDialog(returnDialog, "请输入或选择记录ID",
                        "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                conn.setAutoCommit(false);

                // 获取记录对应书籍信息
                PreparedStatement getRecord = conn.prepareStatement(
                        "SELECT r.book_id, b.title FROM borrow_records r " +
                                "JOIN books b ON r.book_id = b.id " +
                                "WHERE r.id = ? AND r.username = ? AND r.return_date IS NULL");
                getRecord.setInt(1, Integer.parseInt(recordId));
                getRecord.setString(2, currentUser);
                ResultSet rs = getRecord.executeQuery();

                if (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String bookTitle = rs.getString("title");

                    // 确认归还
                    int option = JOptionPane.showConfirmDialog(
                            returnDialog,
                            "确定要归还《" + bookTitle + "》？",
                            "确认归还",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        // 更新记录
                        PreparedStatement update = conn.prepareStatement(
                                "UPDATE borrow_records SET return_date = NOW() WHERE id = ?");
                        update.setInt(1, Integer.parseInt(recordId));
                        update.executeUpdate();

                        // 增加库存
                        PreparedStatement updateBook = conn.prepareStatement(
                                "UPDATE books SET quantity = quantity + 1 WHERE id = ?");
                        updateBook.setInt(1, bookId);
                        updateBook.executeUpdate();

                        conn.commit();

                        JOptionPane.showMessageDialog(returnDialog, "归还成功！",
                                "归还成功", JOptionPane.INFORMATION_MESSAGE);

                        // 刷新表格
                        loadUnreturnedBooks(model);
                    }
                } else {
                    JOptionPane.showMessageDialog(returnDialog, "无效的记录ID或已归还",
                            "归还失败", JOptionPane.ERROR_MESSAGE);
                    conn.rollback();
                }

                conn.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                try { conn.rollback(); } catch (SQLException ignored) {}
                JOptionPane.showMessageDialog(returnDialog, "归还失败: " + ex.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
            }
        });

        // 关闭按钮功能
        closeButton.addActionListener(e -> returnDialog.dispose());

        returnDialog.setVisible(true);
    }

    // 加载未归还的借阅记录
    private void loadUnreturnedBooks(DefaultTableModel model) {
        try {
            model.setRowCount(0); // 清空表格

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT r.id, b.title, r.borrow_date " +
                            "FROM borrow_records r " +
                            "JOIN books b ON r.book_id = b.id " +
                            "WHERE r.username = ? AND r.return_date IS NULL " +
                            "ORDER BY r.borrow_date DESC");
            ps.setString(1, currentUser);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getTimestamp("borrow_date"),
                        "未归还"
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "您当前没有未归还的图书",
                        "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "加载借阅记录失败: " + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewMyBorrowHistory() {
        JDialog historyDialog = new JDialog(this, "我的借阅历史", true);
        historyDialog.setLayout(new BorderLayout());
        historyDialog.setSize(900, 500);
        historyDialog.setLocationRelativeTo(this);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("我的借阅历史记录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);

        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"全部", "已归还", "未归还"});
        statusFilter.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        filterPanel.add(new JLabel("状态: "));
        filterPanel.add(statusFilter);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(filterPanel, BorderLayout.EAST);

        // 表格模型
        DefaultTableModel model = new DefaultTableModel(
                new Object[][] {},
                new String[] {"记录ID", "书名", "借阅时间", "归还时间", "状态"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };

        JTable historyTable = new JTable(model);
        historyTable.setRowHeight(30);
        historyTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        historyTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        historyTable.getTableHeader().setBackground(new Color(240, 240, 240));

        // 设置状态列的单元格渲染器
        historyTable.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value.toString());
            label.setOpaque(true);
            label.setHorizontalAlignment(JLabel.CENTER);

            if ("已归还".equals(value)) {
                label.setForeground(new Color(76, 175, 80));
            } else {
                label.setForeground(new Color(255, 152, 0));
            }

            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
            } else {
                label.setBackground(table.getBackground());
            }

            return label;
        });

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // 底部状态栏
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        statusPanel.setBackground(new Color(240, 240, 240));

        JLabel countLabel = new JLabel("共 0 条记录");
        countLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JButton closeButton = createStyledButton("关闭", new Color(117, 117, 117), Color.WHITE);

        statusPanel.add(countLabel, BorderLayout.WEST);
        statusPanel.add(closeButton, BorderLayout.EAST);

        historyDialog.add(headerPanel, BorderLayout.NORTH);
        historyDialog.add(scrollPane, BorderLayout.CENTER);
        historyDialog.add(statusPanel, BorderLayout.SOUTH);

        // 加载借阅历史数据
        loadBorrowHistory(model, countLabel, "全部");

        // 状态筛选功能
        statusFilter.addActionListener(e -> {
            String filter = (String) statusFilter.getSelectedItem();
            loadBorrowHistory(model, countLabel, filter);
        });

        // 关闭按钮功能
        closeButton.addActionListener(e -> historyDialog.dispose());

        historyDialog.setVisible(true);
    }

    // 加载借阅历史数据
    private void loadBorrowHistory(DefaultTableModel model, JLabel countLabel, String filter) {
        try {
            model.setRowCount(0); // 清空表格

            String sql = "SELECT r.id, b.title, r.borrow_date, r.return_date " +
                    "FROM borrow_records r " +
                    "JOIN books b ON r.book_id = b.id " +
                    "WHERE r.username = ? ";

            if ("已归还".equals(filter)) {
                sql += "AND r.return_date IS NOT NULL ";
            } else if ("未归还".equals(filter)) {
                sql += "AND r.return_date IS NULL ";
            }

            sql += "ORDER BY r.borrow_date DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, currentUser);
            ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                Timestamp borrowDate = rs.getTimestamp("borrow_date");
                Timestamp returnDate = rs.getTimestamp("return_date");
                String status = returnDate != null ? "已归还" : "未归还";

                model.addRow(new Object[]{id, title, borrowDate, returnDate, status});
                count++;
            }

            countLabel.setText("共 " + count + " 条记录");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "加载借阅历史失败: " + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAllBorrowHistory() {
        JDialog historyDialog = new JDialog(this, "借阅记录管理", true);
        historyDialog.setLayout(new BorderLayout());
        historyDialog.setSize(1000, 600);
        historyDialog.setLocationRelativeTo(this);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("全部借阅记录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField(15);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"全部", "已归还", "未归还"});
        statusFilter.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JButton searchButton = new JButton("搜索");
        searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        filterPanel.add(new JLabel("用户名: "));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("状态: "));
        filterPanel.add(statusFilter);
        filterPanel.add(searchButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(filterPanel, BorderLayout.EAST);

        // 表格模型
        DefaultTableModel model = new DefaultTableModel(
                new Object[][] {},
                new String[] {"记录ID", "用户名", "书名", "借阅时间", "归还时间", "状态"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };

        JTable historyTable = new JTable(model);
        historyTable.setRowHeight(30);
        historyTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        historyTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        historyTable.getTableHeader().setBackground(new Color(240, 240, 240));

        // 设置状态列的单元格渲染器
        historyTable.getColumnModel().getColumn(5).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value.toString());
            label.setOpaque(true);
            label.setHorizontalAlignment(JLabel.CENTER);

            if ("已归还".equals(value)) {
                label.setForeground(new Color(76, 175, 80));
            } else {
                label.setForeground(new Color(255, 152, 0));
            }

            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
            } else {
                label.setBackground(table.getBackground());
            }

            return label;
        });

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // 底部状态栏
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        statusPanel.setBackground(new Color(240, 240, 240));

        JLabel countLabel = new JLabel("共 0 条记录");
        countLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton exportButton = createStyledButton("导出", new Color(25, 118, 210), Color.WHITE);
        JButton closeButton = createStyledButton("关闭", new Color(117, 117, 117), Color.WHITE);

        buttonPanel.add(exportButton);
        buttonPanel.add(closeButton);

        statusPanel.add(countLabel, BorderLayout.WEST);
        statusPanel.add(buttonPanel, BorderLayout.EAST);

        historyDialog.add(headerPanel, BorderLayout.NORTH);
        historyDialog.add(scrollPane, BorderLayout.CENTER);
        historyDialog.add(statusPanel, BorderLayout.SOUTH);

        // 加载全部借阅历史数据
        loadAllBorrowHistory(model, countLabel, "", "全部");

        // 搜索功能
        searchButton.addActionListener(e -> {
            String username = searchField.getText().trim();
            String status = (String) statusFilter.getSelectedItem();
            loadAllBorrowHistory(model, countLabel, username, status);
        });

        // 导出按钮功能
        exportButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(historyDialog, "导出功能正在开发中",
                    "提示", JOptionPane.INFORMATION_MESSAGE);
        });

        // 关闭按钮功能
        closeButton.addActionListener(e -> historyDialog.dispose());

        historyDialog.setVisible(true);
    }

    // 加载全部借阅历史数据
    private void loadAllBorrowHistory(DefaultTableModel model, JLabel countLabel, String username, String filter) {
        try {
            model.setRowCount(0); // 清空表格

            String sql = "SELECT r.id, r.username, b.title, r.borrow_date, r.return_date " +
                    "FROM borrow_records r " +
                    "JOIN books b ON r.book_id = b.id ";

            boolean hasWhere = false;

            if (username != null && !username.isEmpty()) {
                sql += "WHERE r.username LIKE ? ";
                hasWhere = true;
            }

            if ("已归还".equals(filter)) {
                sql += hasWhere ? "AND r.return_date IS NOT NULL " : "WHERE r.return_date IS NOT NULL ";
                hasWhere = true;
            } else if ("未归还".equals(filter)) {
                sql += hasWhere ? "AND r.return_date IS NULL " : "WHERE r.return_date IS NULL ";
            }

            sql += "ORDER BY r.borrow_date DESC";

            PreparedStatement ps = conn.prepareStatement(sql);

            if (username != null && !username.isEmpty()) {
                ps.setString(1, "%" + username + "%");
            }

            ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {
                int id = rs.getInt("id");
                String user = rs.getString("username");
                String title = rs.getString("title");
                Timestamp borrowDate = rs.getTimestamp("borrow_date");
                Timestamp returnDate = rs.getTimestamp("return_date");
                String status = returnDate != null ? "已归还" : "未归还";

                model.addRow(new Object[]{id, user, title, borrowDate, returnDate, status});
                count++;
            }

            countLabel.setText("共 " + count + " 条记录");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "加载借阅历史失败: " + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibrarySystemGUI::new);
    }
}