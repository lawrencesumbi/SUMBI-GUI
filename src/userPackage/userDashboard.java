
import config.Session;
import javax.swing.*;
import java.sql.*;
import config.dbConnector;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.table.TableModel;

public class userDashboard extends javax.swing.JFrame {
    private String user_fname;
    private String vio_id;
    private String stud_id;
    /**
     * Creates new form userDashboard
     */
    public userDashboard() {
        initComponents();
    }
    
    public userDashboard(String user_fname){
        this.user_fname = user_fname;
        initComponents();
        J_user_fname.setText(user_fname);
        displayDashboard();
        displayImage(user_fname);
    }
    
    public void displayDashboard() {
        try {
            dbConnector dbc = new dbConnector();

            String sqlStudents = "SELECT COUNT(stud_fname) AS student_count FROM stud_table";
            ResultSet studentResultSet = dbc.getData(sqlStudents);

            int studentCount = 0;
            if (studentResultSet.next()) {
                studentCount = studentResultSet.getInt("student_count");
            }
            totalStudents.setText("" + studentCount);
            
            String sqlViolations = "SELECT COUNT(vio_name) AS violation_count FROM vio_table";
            ResultSet violationResultSet = dbc.getData(sqlViolations);

            int violationCount = 0;
            if (violationResultSet.next()) {
                violationCount = violationResultSet.getInt("violation_count");
            }
            totalViolations.setText("" + violationCount);

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            totalStudents.setText("Error fetching data");
            totalViolations.setText("Error fetching data");
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
        stat_panel = new javax.swing.JPanel();
        user_type4 = new javax.swing.JLabel();
        dash_icon = new javax.swing.JLabel();
        totalViolations = new javax.swing.JLabel();
        studnum_panel = new javax.swing.JPanel();
        dash_icon4 = new javax.swing.JLabel();
        user_type8 = new javax.swing.JLabel();
        user_type13 = new javax.swing.JLabel();
        vionum_panel = new javax.swing.JPanel();
        user_type5 = new javax.swing.JLabel();
        dash_icon3 = new javax.swing.JLabel();
        totalStudents = new javax.swing.JLabel();
        user_type2 = new javax.swing.JLabel();
        leftpanel = new javax.swing.JPanel();
        displayImage = new javax.swing.JLabel();
        user_type = new javax.swing.JLabel();
        J_user_fname = new javax.swing.JLabel();
        dash_icon1 = new javax.swing.JLabel();
        stud_icon = new javax.swing.JLabel();
        vio_icon = new javax.swing.JLabel();
        sett_icon = new javax.swing.JLabel();
        log_icon = new javax.swing.JLabel();
        dashboard = new javax.swing.JLabel();
        student = new javax.swing.JLabel();
        violation = new javax.swing.JLabel();
        account = new javax.swing.JLabel();
        logout = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        userspanel.setBackground(new java.awt.Color(204, 0, 0));
        userspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        stat_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        user_type4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        user_type4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_type4.setText("Total Violations");
        stat_panel.add(user_type4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, -1));

        dash_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-foul-48.png"))); // NOI18N
        stat_panel.add(dash_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, -1, 40));

        totalViolations.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        totalViolations.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalViolations.setText("0");
        stat_panel.add(totalViolations, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 50, 40));

        userspanel.add(stat_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 230, 190, 120));

        studnum_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dash_icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-calendar-48.png"))); // NOI18N
        studnum_panel.add(dash_icon4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 50, 40));

        user_type8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        user_type8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_type8.setText("Academic Year");
        studnum_panel.add(user_type8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, -1));

        user_type13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        user_type13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_type13.setText("2024-2025");
        studnum_panel.add(user_type13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 100, 30));

        userspanel.add(studnum_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 190, 120));

        vionum_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        user_type5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        user_type5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_type5.setText("Total Students");
        vionum_panel.add(user_type5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, -1));

        dash_icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-graduate-48.png"))); // NOI18N
        vionum_panel.add(dash_icon3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, -1, 40));

        totalStudents.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        totalStudents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalStudents.setText("0");
        vionum_panel.add(totalStudents, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 50, 40));

        userspanel.add(vionum_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 230, 190, 120));

        user_type2.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        user_type2.setForeground(new java.awt.Color(255, 255, 255));
        user_type2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_type2.setText("Welcome to Student Violation System!");
        userspanel.add(user_type2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 610, 50));

        getContentPane().add(userspanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 710, 520));

        leftpanel.setBackground(new java.awt.Color(0, 0, 0));
        leftpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        displayImage.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        displayImage.setForeground(new java.awt.Color(255, 255, 255));
        displayImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        displayImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-illustration-vector-removebg-preview1.png"))); // NOI18N
        leftpanel.add(displayImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 130, 130));

        user_type.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        user_type.setForeground(new java.awt.Color(255, 255, 255));
        user_type.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_type.setText("User");
        leftpanel.add(user_type, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, 50, -1));

        J_user_fname.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        J_user_fname.setForeground(new java.awt.Color(255, 255, 255));
        J_user_fname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        J_user_fname.setText("Fullname");
        leftpanel.add(J_user_fname, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 170, 30));

        dash_icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-dashboard-layout-24.png"))); // NOI18N
        dash_icon1.setText("jLabel1");
        leftpanel.add(dash_icon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 30, 30));

        stud_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-student-male-24.png"))); // NOI18N
        stud_icon.setText("jLabel1");
        leftpanel.add(stud_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 30, 30));

        vio_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-foul-30.png"))); // NOI18N
        vio_icon.setText("jLabel1");
        leftpanel.add(vio_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 30, 30));

        sett_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-settings-50.png"))); // NOI18N
        sett_icon.setText("jLabel1");
        leftpanel.add(sett_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 30, 30));

        log_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-open-pane-24.png"))); // NOI18N
        log_icon.setText("jLabel1");
        leftpanel.add(log_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 30, 30));

        dashboard.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dashboard.setForeground(new java.awt.Color(255, 255, 0));
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
        leftpanel.add(dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, -1, 50));

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
        leftpanel.add(student, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 90, 50));

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

        account.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        account.setForeground(new java.awt.Color(255, 255, 255));
        account.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        account.setText("SETTINGS");
        account.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accountMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                accountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                accountMouseExited(evt);
            }
        });
        leftpanel.add(account, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, 100, 50));

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
        leftpanel.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 450, 90, 50));

        getContentPane().add(leftpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 520));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseClicked

    }//GEN-LAST:event_dashboardMouseClicked

    private void dashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseEntered
        dashboard.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_dashboardMouseEntered

    private void dashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseExited
        
    }//GEN-LAST:event_dashboardMouseExited

    private void studentMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentMouseEntered
        student.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_studentMouseEntered

    private void studentMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentMouseExited
        student.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_studentMouseExited

    private void violationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseClicked
        new userViolation(user_fname, stud_id).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_violationMouseClicked

    private void violationMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseEntered
        violation.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_violationMouseEntered

    private void violationMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseExited
        violation.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_violationMouseExited

    private void accountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountMouseEntered
        account.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_accountMouseEntered

    private void accountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountMouseExited
        account.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_accountMouseExited

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

    private void studentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentMouseClicked
        new userStudent(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_studentMouseClicked

    private void accountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountMouseClicked
        new userAccount(user_fname).setVisible(true);
        this.dispose();
        
        
    }//GEN-LAST:event_accountMouseClicked

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
            java.util.logging.Logger.getLogger(userDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(userDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(userDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(userDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new userDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel J_user_fname;
    private javax.swing.JLabel account;
    private javax.swing.JLabel dash_icon;
    private javax.swing.JLabel dash_icon1;
    private javax.swing.JLabel dash_icon3;
    private javax.swing.JLabel dash_icon4;
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel displayImage;
    private javax.swing.JPanel leftpanel;
    private javax.swing.JLabel log_icon;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel sett_icon;
    private javax.swing.JPanel stat_panel;
    private javax.swing.JLabel stud_icon;
    private javax.swing.JLabel student;
    private javax.swing.JPanel studnum_panel;
    private javax.swing.JLabel totalStudents;
    private javax.swing.JLabel totalViolations;
    private javax.swing.JLabel user_type;
    private javax.swing.JLabel user_type13;
    private javax.swing.JLabel user_type2;
    private javax.swing.JLabel user_type4;
    private javax.swing.JLabel user_type5;
    private javax.swing.JLabel user_type8;
    private javax.swing.JPanel userspanel;
    private javax.swing.JLabel vio_icon;
    private javax.swing.JLabel violation;
    private javax.swing.JPanel vionum_panel;
    // End of variables declaration//GEN-END:variables
}
