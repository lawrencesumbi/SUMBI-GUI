/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import config.Session;
import config.dbConnector;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;


/**
 *
 * @author Admin
 */
public class adminLogs extends javax.swing.JFrame {
    private String user_fname;
    private String stud_id;
    /**
     * Creates new form adminLogs
     */
    public adminLogs() {
        initComponents();
    }

    public adminLogs(String user_fname) {
        this.user_fname = user_fname;
        initComponents();
        displayImage(user_fname);
        J_user_fname.setText(user_fname);
        displayLogs();
    }
    
    
    public void displayLogs() {
        try {
            dbConnector db = new dbConnector();
            String query = "SELECT logs_table.logs_id AS 'Log ID', " +
                           "user_table.user_fname AS 'User', " +
                           "logs_table.logs_action AS 'Activity', " +
                           "logs_table.logs_stamp AS 'Timestamp' " +
                           "FROM logs_table " +
                           "JOIN user_table ON logs_table.user_id = user_table.user_id " +
                           "ORDER BY logs_table.logs_id DESC";

            ResultSet rs = db.getData(query);
            logs_table.setModel(DbUtils.resultSetToTableModel(rs));

            // Set preferred widths (adjust as needed)
            logs_table.getColumnModel().getColumn(0).setPreferredWidth(100);   // Log ID
            logs_table.getColumnModel().getColumn(1).setPreferredWidth(150);  // User
            logs_table.getColumnModel().getColumn(2).setPreferredWidth(300);  // Activity
            logs_table.getColumnModel().getColumn(3).setPreferredWidth(180);  // Timestamp

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error loading logs: " + e.getMessage());
        }
    }


    
    private void displayImage(String user_fname) {
        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT image_path FROM user_table WHERE user_fname = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, user_fname);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String imagePath = rs.getString("image_path");

                        if (imagePath != null && !imagePath.isEmpty()) {
                            File file = new File(imagePath);
                            if (file.exists()) {
                                BufferedImage originalImg = ImageIO.read(file);
                                if (originalImg != null) {
                                    BufferedImage squaredImg = cropToSquare(originalImg);
                                    BufferedImage roundedImg = getRoundedImageWithBorder(squaredImg, displayImage.getWidth(), displayImage.getHeight(), 2, Color.WHITE);
                                    displayImage.setIcon(new ImageIcon(roundedImg));
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private BufferedImage cropToSquare(BufferedImage img) {
        int size = Math.min(img.getWidth(), img.getHeight());
        int x = (img.getWidth() - size) / 2;
        int y = (img.getHeight() - size) / 2;
        return img.getSubimage(x, y, size, size);
    }

    private BufferedImage getRoundedImageWithBorder(BufferedImage img, int width, int height, int borderThickness, Color borderColor) {
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Float clip = new Ellipse2D.Float(borderThickness / 2f, borderThickness / 2f, width - borderThickness, height - borderThickness);
        g2.setClip(clip);
        g2.drawImage(img, borderThickness / 2, borderThickness / 2, width - borderThickness, height - borderThickness, null);

        g2.setClip(null);
        g2.setStroke(new BasicStroke(borderThickness));
        g2.setColor(borderColor);
        g2.draw(new Ellipse2D.Float(borderThickness / 2f, borderThickness / 2f, width - borderThickness, height - borderThickness));

        g2.dispose();
        return output;
    }
    
    
     private void logActivity(int user_id, String action) {
        String sql = "INSERT INTO logs_table (user_id, logs_action, logs_stamp) VALUES (?, ?, NOW())";
        dbConnector db = new dbConnector();

        try (Connection conn = db.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, user_id);
            pst.setString(2, action);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logging activity: " + e.getMessage());
        }
    }
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        userspanel = new javax.swing.JPanel();
        settings2 = new javax.swing.JLabel();
        settings3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logs_table = new javax.swing.JTable();
        activityLogs = new javax.swing.JLabel();
        leftpanel = new javax.swing.JPanel();
        logout = new javax.swing.JLabel();
        displayImage = new javax.swing.JLabel();
        violation = new javax.swing.JLabel();
        users = new javax.swing.JLabel();
        student = new javax.swing.JLabel();
        record = new javax.swing.JLabel();
        dashboard = new javax.swing.JLabel();
        J_user_fname = new javax.swing.JLabel();
        settings = new javax.swing.JLabel();
        stud_icon = new javax.swing.JLabel();
        vio_icon = new javax.swing.JLabel();
        rec_icon = new javax.swing.JLabel();
        users_icon = new javax.swing.JLabel();
        sett_icon = new javax.swing.JLabel();
        log_icon = new javax.swing.JLabel();
        user_type2 = new javax.swing.JLabel();
        dash_icon1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        userspanel.setBackground(new java.awt.Color(204, 0, 0));
        userspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        settings2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        settings2.setForeground(new java.awt.Color(255, 255, 255));
        settings2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        settings2.setText("ACTIVITY LOGS");
        settings2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settings2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settings2MouseExited(evt);
            }
        });
        userspanel.add(settings2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, 250, 50));

        settings3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        settings3.setForeground(new java.awt.Color(255, 255, 255));
        settings3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        settings3.setText("Account Settings");
        settings3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settings3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settings3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settings3MouseExited(evt);
            }
        });
        userspanel.add(settings3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 150, 30));

        logs_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(logs_table);

        userspanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 710, 470));

        activityLogs.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        activityLogs.setForeground(new java.awt.Color(255, 255, 0));
        activityLogs.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        activityLogs.setText("Activity Logs");
        activityLogs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                activityLogsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                activityLogsMouseExited(evt);
            }
        });
        userspanel.add(activityLogs, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, 150, 30));

        leftpanel.setBackground(new java.awt.Color(0, 0, 0));
        leftpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logout.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logout.setText("LOG OUT");
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutMouseExited(evt);
            }
        });
        leftpanel.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 530, 90, 50));

        displayImage.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        displayImage.setForeground(new java.awt.Color(255, 255, 255));
        displayImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        displayImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-illustration-vector-removebg-preview1.png"))); // NOI18N
        leftpanel.add(displayImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 130, 130));

        violation.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        violation.setForeground(new java.awt.Color(255, 255, 255));
        violation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        violation.setText("VIOLATION");
        violation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                violationMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                violationMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                violationMouseExited(evt);
            }
        });
        leftpanel.add(violation, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 110, 50));

        users.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        users.setForeground(new java.awt.Color(255, 255, 255));
        users.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        users.setText("USERS");
        users.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usersMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                usersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                usersMouseExited(evt);
            }
        });
        leftpanel.add(users, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 70, 50));

        student.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        student.setForeground(new java.awt.Color(255, 255, 255));
        student.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        student.setText("STUDENT");
        student.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                studentMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                studentMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                studentMouseExited(evt);
            }
        });
        leftpanel.add(student, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 280, -1, 50));

        record.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        record.setForeground(new java.awt.Color(255, 255, 255));
        record.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        record.setText("RECORD");
        record.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recordMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                recordMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                recordMouseExited(evt);
            }
        });
        leftpanel.add(record, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, 90, 50));

        dashboard.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dashboard.setForeground(new java.awt.Color(255, 255, 255));
        dashboard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashboard.setText("DASHBOARD");
        dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dashboardMouseExited(evt);
            }
        });
        leftpanel.add(dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 230, -1, 50));

        J_user_fname.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        J_user_fname.setForeground(new java.awt.Color(255, 255, 255));
        J_user_fname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        J_user_fname.setText("Fullname");
        leftpanel.add(J_user_fname, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 170, 30));

        settings.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        settings.setForeground(new java.awt.Color(255, 255, 0));
        settings.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        settings.setText("SETTINGS");
        settings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settingsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settingsMouseExited(evt);
            }
        });
        leftpanel.add(settings, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 480, 100, 50));

        stud_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-student-male-24.png"))); // NOI18N
        stud_icon.setText("jLabel1");
        leftpanel.add(stud_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 30, 30));

        vio_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-foul-30.png"))); // NOI18N
        vio_icon.setText("jLabel1");
        leftpanel.add(vio_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 30, 30));

        rec_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-record-50.png"))); // NOI18N
        rec_icon.setText("jLabel1");
        leftpanel.add(rec_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 30, 30));

        users_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-user-icon-30.png"))); // NOI18N
        users_icon.setText("jLabel1");
        leftpanel.add(users_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 30, 30));

        sett_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-settings-50.png"))); // NOI18N
        sett_icon.setText("jLabel1");
        leftpanel.add(sett_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, 30, 30));

        log_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-open-pane-24.png"))); // NOI18N
        log_icon.setText("jLabel1");
        leftpanel.add(log_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 30, 30));

        user_type2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        user_type2.setForeground(new java.awt.Color(255, 255, 255));
        user_type2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_type2.setText("Admin");
        leftpanel.add(user_type2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 70, -1));

        dash_icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-dashboard-layout-24.png"))); // NOI18N
        dash_icon1.setText("jLabel1");
        leftpanel.add(dash_icon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 30, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(leftpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(userspanel, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(leftpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(userspanel, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        int response = JOptionPane.showConfirmDialog(this, 
        "Confirm Log Out?", 
        "Logout Confirmation", 
        JOptionPane.YES_NO_OPTION);

    if (response == JOptionPane.YES_OPTION) {
        int uid = Session.getInstance().getUid(); 
        logActivity(uid, "Logged out");           
        Session.getInstance().clearSession();    
        
        new loginform().setVisible(true);
        this.dispose();
    }     
    }//GEN-LAST:event_logoutMouseClicked

    private void logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseEntered
        logout.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_logoutMouseEntered

    private void logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseExited
        logout.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_logoutMouseExited

    private void violationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseClicked
        new adminViolation(user_fname, stud_id).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_violationMouseClicked

    private void violationMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseEntered
        violation.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_violationMouseEntered

    private void violationMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseExited
        violation.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_violationMouseExited

    private void usersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersMouseClicked
        new adminUsers(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_usersMouseClicked

    private void usersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersMouseEntered
        users.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_usersMouseEntered

    private void usersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersMouseExited
        users.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_usersMouseExited

    private void studentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentMouseClicked
        new adminStudent(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_studentMouseClicked

    private void studentMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentMouseEntered
        student.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_studentMouseEntered

    private void studentMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentMouseExited
        student.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_studentMouseExited

    private void recordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recordMouseClicked

    }//GEN-LAST:event_recordMouseClicked

    private void recordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recordMouseEntered
        record.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_recordMouseEntered

    private void recordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recordMouseExited
        record.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_recordMouseExited

    private void dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseClicked
        new adminDashboard(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_dashboardMouseClicked

    private void dashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseEntered
        dashboard.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_dashboardMouseEntered

    private void dashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseExited
        dashboard.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_dashboardMouseExited

    private void settingsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseEntered
        settings.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_settingsMouseEntered

    private void settingsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseExited
        
    }//GEN-LAST:event_settingsMouseExited

    private void settings3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settings3MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_settings3MouseExited

    private void settings3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settings3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_settings3MouseEntered

    private void settings2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settings2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_settings2MouseExited

    private void settings2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settings2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_settings2MouseEntered

    private void activityLogsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_activityLogsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_activityLogsMouseEntered

    private void activityLogsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_activityLogsMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_activityLogsMouseExited

    private void settings3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settings3MouseClicked
        new adminAccount(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_settings3MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(adminLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(adminLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(adminLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(adminLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new adminLogs().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel J_user_fname;
    private javax.swing.JLabel activityLogs;
    private javax.swing.JLabel dash_icon1;
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel displayImage;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel leftpanel;
    private javax.swing.JLabel log_icon;
    private javax.swing.JLabel logout;
    private javax.swing.JTable logs_table;
    private javax.swing.JLabel rec_icon;
    private javax.swing.JLabel record;
    private javax.swing.JLabel sett_icon;
    private javax.swing.JLabel settings;
    private javax.swing.JLabel settings2;
    private javax.swing.JLabel settings3;
    private javax.swing.JLabel stud_icon;
    private javax.swing.JLabel student;
    private javax.swing.JLabel user_type2;
    private javax.swing.JLabel users;
    private javax.swing.JLabel users_icon;
    private javax.swing.JPanel userspanel;
    private javax.swing.JLabel vio_icon;
    private javax.swing.JLabel violation;
    // End of variables declaration//GEN-END:variables
}
