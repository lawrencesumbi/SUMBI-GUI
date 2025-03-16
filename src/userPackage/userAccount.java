/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import config.dbConnector;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
/**
 *
 * @author Admin
 */
public class userAccount extends javax.swing.JFrame {
    private String user_fname;
    /**
     * Creates new form userAccount
     */
    public userAccount() {
        initComponents();
    }

    public userAccount(String user_fname) {
        this.user_fname = user_fname;
        initComponents();
        displayImage(user_fname);
        J_user_fname.setText(user_fname);  
        displayData();
    }
    
    public void displayData() {
        try {
            dbConnector dbc = new dbConnector();
            ResultSet rs = dbc.getData("SELECT * FROM user_table");

            if (rs.next()) { // Move to the first row of results
                String userFname = rs.getString("user_fname"); // Adjust column names as needed
                fullNameTextField.setText(userFname);
                contactNumberTextField.setText(rs.getString("user_cnumber"));
                emailTextField.setText(rs.getString("user_email"));
                passwordField.setText(rs.getString("user_password"));

                byte[] imgBytes = rs.getBytes("user_image"); // Adjust column name

                if (imgBytes != null && imgBytes.length > 0) {
                    ImageIcon getIcon = new ImageIcon(imgBytes);
                    Image img = getIcon.getImage().getScaledInstance(uploadImage.getWidth(), uploadImage.getHeight(), Image.SCALE_SMOOTH);
                    uploadImage.setIcon(new ImageIcon(img));
                } else {
                    uploadImage.setIcon(new ImageIcon(getClass().getResource("/images/image-removebg-preview1.png")));
                }
            } else {
                System.out.println("No user data found.");
            }

            rs.close(); // Close ResultSet

        } catch (SQLException ex) {
            ex.printStackTrace(); // More detailed error logging
        }
    }
    
    public void uploadImage(JLabel uploadImage) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Image");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try {
                Connection conn = DriverManager.getConnection(url, user, pass);
                FileInputStream fis = new FileInputStream(file);

                String sql = "UPDATE user_table SET user_image = ? WHERE user_email = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setBinaryStream(1, fis, (int) file.length());
                pstmt.setString(2, emailTextField.getText().trim());
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {

                    ImageIcon getIcon = new ImageIcon(file.getAbsolutePath());
                    Image img = getIcon.getImage().getScaledInstance(uploadImage.getWidth(), uploadImage.getHeight(), Image.SCALE_SMOOTH);
                    uploadImage.setIcon(new ImageIcon(img));

                    JOptionPane.showMessageDialog(null, "Image Uploaded Successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "User Not Found! Please check the email.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error Uploading Image!");
            }
        }
    }
    
    private void displayImage(String user_fname) {
        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            String sql = "SELECT user_image FROM user_table WHERE user_fname = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user_fname);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                byte[] imgBytes = rs.getBytes("user_image");

                if (imgBytes != null && imgBytes.length > 0) {
                    ImageIcon getIcon = new ImageIcon(imgBytes);
                    Image img = getIcon.getImage().getScaledInstance(uploadImage.getWidth(), uploadImage.getHeight(), Image.SCALE_SMOOTH);
                    displayImage.setIcon(new ImageIcon(img));
                } else {
                    displayImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/image-removebg-preview1.png")));
                }
            } else {
                displayImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/image-removebg-preview1.png")));
            }

            Border border = BorderFactory.createLineBorder(Color.WHITE, 2); // Black border with 2px thickness
            displayImage.setBorder(border);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Loading User Image!");
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

        leftpanel = new javax.swing.JPanel();
        displayImage = new javax.swing.JLabel();
        user_type = new javax.swing.JLabel();
        J_user_fname = new javax.swing.JLabel();
        dash_icon = new javax.swing.JLabel();
        stud_icon = new javax.swing.JLabel();
        vio_icon = new javax.swing.JLabel();
        sett_icon = new javax.swing.JLabel();
        log_icon = new javax.swing.JLabel();
        dashboard = new javax.swing.JLabel();
        student = new javax.swing.JLabel();
        violation = new javax.swing.JLabel();
        settings = new javax.swing.JLabel();
        logout = new javax.swing.JLabel();
        userspanel = new javax.swing.JPanel();
        user_fnamelabel = new javax.swing.JLabel();
        fullNameTextField = new javax.swing.JTextField();
        user_cnumberlabel = new javax.swing.JLabel();
        contactNumberTextField = new javax.swing.JTextField();
        user_emaillabel = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        user_passwordlabel = new javax.swing.JLabel();
        uploadImage = new javax.swing.JLabel();
        passwordField = new javax.swing.JTextField();
        settings1 = new javax.swing.JLabel();
        cancel = new javax.swing.JLabel();
        save = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        leftpanel.setBackground(new java.awt.Color(0, 0, 0));
        leftpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        displayImage.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        displayImage.setForeground(new java.awt.Color(255, 255, 255));
        displayImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        displayImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-illustration-vector-removebg-preview1.png"))); // NOI18N
        leftpanel.add(displayImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 130, 130));

        user_type.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        user_type.setForeground(new java.awt.Color(255, 255, 255));
        user_type.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_type.setText("User");
        leftpanel.add(user_type, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 50, -1));

        J_user_fname.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        J_user_fname.setForeground(new java.awt.Color(255, 255, 255));
        J_user_fname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        J_user_fname.setText("Fullname");
        leftpanel.add(J_user_fname, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 170, 30));

        dash_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-dashboard-layout-24.png"))); // NOI18N
        dash_icon.setText("jLabel1");
        leftpanel.add(dash_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 30, 30));

        stud_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-student-male-24.png"))); // NOI18N
        stud_icon.setText("jLabel1");
        leftpanel.add(stud_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 30, 30));

        vio_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-foul-30.png"))); // NOI18N
        vio_icon.setText("jLabel1");
        leftpanel.add(vio_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 30, 30));

        sett_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-settings-50.png"))); // NOI18N
        sett_icon.setText("jLabel1");
        leftpanel.add(sett_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 30, 30));

        log_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-open-pane-24.png"))); // NOI18N
        log_icon.setText("jLabel1");
        leftpanel.add(log_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, 30, 30));

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
        leftpanel.add(dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, -1, 50));

        student.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        student.setForeground(new java.awt.Color(255, 255, 255));
        student.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        student.setText("STUDENT");
        student.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                studentMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                studentMouseExited(evt);
            }
        });
        leftpanel.add(student, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 90, 50));

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
        leftpanel.add(violation, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, 110, 50));

        settings.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        settings.setForeground(new java.awt.Color(255, 255, 255));
        settings.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        settings.setText("ACCOUNT");
        settings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settingsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settingsMouseExited(evt);
            }
        });
        leftpanel.add(settings, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 100, 50));

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
        leftpanel.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 490, 90, 50));

        getContentPane().add(leftpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 600));

        userspanel.setBackground(new java.awt.Color(204, 0, 0));
        userspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        user_fnamelabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        user_fnamelabel.setForeground(new java.awt.Color(255, 255, 255));
        user_fnamelabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_fnamelabel.setText("Full Name");
        userspanel.add(user_fnamelabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 90, 20));

        fullNameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        fullNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullNameTextFieldActionPerformed(evt);
            }
        });
        userspanel.add(fullNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 200, 30));

        user_cnumberlabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        user_cnumberlabel.setForeground(new java.awt.Color(255, 255, 255));
        user_cnumberlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_cnumberlabel.setText("Contact Number");
        userspanel.add(user_cnumberlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 160, 20));

        contactNumberTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        contactNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactNumberTextFieldActionPerformed(evt);
            }
        });
        userspanel.add(contactNumberTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 240, 200, 30));

        user_emaillabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        user_emaillabel.setForeground(new java.awt.Color(255, 255, 255));
        user_emaillabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_emaillabel.setText("Email");
        userspanel.add(user_emaillabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 50, 20));

        emailTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });
        userspanel.add(emailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 200, 30));

        user_passwordlabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        user_passwordlabel.setForeground(new java.awt.Color(255, 255, 255));
        user_passwordlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_passwordlabel.setText("Password");
        userspanel.add(user_passwordlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 100, 20));

        uploadImage.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        uploadImage.setForeground(new java.awt.Color(255, 255, 255));
        uploadImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        uploadImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/image-removebg-preview1.png"))); // NOI18N
        uploadImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                uploadImageMouseClicked(evt);
            }
        });
        userspanel.add(uploadImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 200, 150, 150));

        passwordField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });
        userspanel.add(passwordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 340, 200, 30));

        settings1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        settings1.setForeground(new java.awt.Color(255, 255, 255));
        settings1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        settings1.setText("ACCOUNT SETTINGS");
        settings1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settings1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settings1MouseExited(evt);
            }
        });
        userspanel.add(settings1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, 250, 50));

        cancel.setBackground(new java.awt.Color(255, 255, 255));
        cancel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cancel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cancel.setText("Cancel");
        cancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cancel.setOpaque(true);
        cancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelMouseExited(evt);
            }
        });
        userspanel.add(cancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 400, 80, 40));

        save.setBackground(new java.awt.Color(255, 255, 255));
        save.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        save.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        save.setText("Save");
        save.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        save.setOpaque(true);
        save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveMouseExited(evt);
            }
        });
        userspanel.add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 400, 80, 40));

        getContentPane().add(userspanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 710, 600));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseClicked
        new userDashboard(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_dashboardMouseClicked

    private void dashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseEntered
        dashboard.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_dashboardMouseEntered

    private void dashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseExited
        dashboard.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_dashboardMouseExited

    private void studentMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentMouseEntered
        student.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_studentMouseEntered

    private void studentMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentMouseExited
        student.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_studentMouseExited

    private void violationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_violationMouseClicked

    private void violationMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseEntered
        violation.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_violationMouseEntered

    private void violationMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseExited
        violation.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_violationMouseExited

    private void settingsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseEntered
        settings.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_settingsMouseEntered

    private void settingsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseExited
        settings.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_settingsMouseExited

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        int response = JOptionPane.showConfirmDialog(this,
            "Confirm Log Out?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            new loginform().setVisible(true);
            this.dispose();
        } else {
        }
    }//GEN-LAST:event_logoutMouseClicked

    private void logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseEntered
        logout.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_logoutMouseEntered

    private void logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseExited
        logout.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_logoutMouseExited

    private void fullNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fullNameTextFieldActionPerformed

    private void contactNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactNumberTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contactNumberTextFieldActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void uploadImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_uploadImageMouseClicked
        uploadImage(uploadImage);
    }//GEN-LAST:event_uploadImageMouseClicked

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void settings1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settings1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_settings1MouseEntered

    private void settings1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settings1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_settings1MouseExited

    private void cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseClicked
        
    }//GEN-LAST:event_cancelMouseClicked

    private void cancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseEntered

    }//GEN-LAST:event_cancelMouseEntered

    private void cancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseExited

    }//GEN-LAST:event_cancelMouseExited

    private void saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_saveMouseClicked

    private void saveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_saveMouseEntered

    private void saveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_saveMouseExited

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
            java.util.logging.Logger.getLogger(userAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(userAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(userAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(userAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new userAccount().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel J_user_fname;
    private javax.swing.JLabel cancel;
    private javax.swing.JTextField contactNumberTextField;
    private javax.swing.JLabel dash_icon;
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel displayImage;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JTextField fullNameTextField;
    private javax.swing.JPanel leftpanel;
    private javax.swing.JLabel log_icon;
    private javax.swing.JLabel logout;
    private javax.swing.JTextField passwordField;
    private javax.swing.JLabel save;
    private javax.swing.JLabel sett_icon;
    private javax.swing.JLabel settings;
    private javax.swing.JLabel settings1;
    private javax.swing.JLabel stud_icon;
    private javax.swing.JLabel student;
    private javax.swing.JLabel uploadImage;
    private javax.swing.JLabel user_cnumberlabel;
    private javax.swing.JLabel user_emaillabel;
    private javax.swing.JLabel user_fnamelabel;
    private javax.swing.JLabel user_passwordlabel;
    private javax.swing.JLabel user_type;
    private javax.swing.JPanel userspanel;
    private javax.swing.JLabel vio_icon;
    private javax.swing.JLabel violation;
    // End of variables declaration//GEN-END:variables
}
