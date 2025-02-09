
import config.dbConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;






/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 */
public class loginform extends javax.swing.JFrame {

    /**
     * Creates new form loginform
     */
    public loginform() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        HeaderPanel = new javax.swing.JPanel();
        Header = new javax.swing.JLabel();
        LeftPanel = new javax.swing.JPanel();
        violationicon = new javax.swing.JLabel();
        systemlabel = new javax.swing.JLabel();
        studentlabel = new javax.swing.JLabel();
        RightPanel = new javax.swing.JPanel();
        loginbutton = new javax.swing.JLabel();
        usertextfield = new javax.swing.JTextField();
        passtextfield = new javax.swing.JPasswordField();
        username = new javax.swing.JLabel();
        donthaveacc = new javax.swing.JLabel();
        password = new javax.swing.JLabel();
        loginlabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        HeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        HeaderPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Header.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ifoto-ai_1738725918062.png"))); // NOI18N
        HeaderPanel.add(Header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 130));

        getContentPane().add(HeaderPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 130));

        LeftPanel.setBackground(new java.awt.Color(0, 0, 0));
        LeftPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        violationicon.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        violationicon.setForeground(new java.awt.Color(255, 255, 255));
        violationicon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        violationicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/violation-logo-860-16424 (1).png"))); // NOI18N
        LeftPanel.add(violationicon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 225, 90));

        systemlabel.setFont(new java.awt.Font("Copperplate Gothic Bold", 3, 28)); // NOI18N
        systemlabel.setForeground(new java.awt.Color(255, 255, 255));
        systemlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        systemlabel.setText("System");
        LeftPanel.add(systemlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 140, 30));

        studentlabel.setFont(new java.awt.Font("Copperplate Gothic Bold", 3, 30)); // NOI18N
        studentlabel.setForeground(new java.awt.Color(255, 255, 255));
        studentlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        studentlabel.setText("Student");
        LeftPanel.add(studentlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 160, 40));

        getContentPane().add(LeftPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 250, 220));

        RightPanel.setBackground(new java.awt.Color(204, 0, 0));
        RightPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loginbutton.setBackground(new java.awt.Color(255, 255, 255));
        loginbutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        loginbutton.setForeground(new java.awt.Color(255, 255, 255));
        loginbutton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginbutton.setText("Log in");
        loginbutton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        loginbutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginbuttonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginbuttonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginbuttonMouseExited(evt);
            }
        });
        RightPanel.add(loginbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 90, 30));

        usertextfield.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        usertextfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usertextfieldActionPerformed(evt);
            }
        });
        RightPanel.add(usertextfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 200, -1));

        passtextfield.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        passtextfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passtextfieldActionPerformed(evt);
            }
        });
        RightPanel.add(passtextfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 200, -1));

        username.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        username.setForeground(new java.awt.Color(255, 255, 255));
        username.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        username.setText("Email");
        RightPanel.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 60, 30));

        donthaveacc.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        donthaveacc.setForeground(new java.awt.Color(255, 255, 255));
        donthaveacc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        donthaveacc.setText("Dont have an account? Register");
        donthaveacc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                donthaveaccMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                donthaveaccMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                donthaveaccMouseExited(evt);
            }
        });
        RightPanel.add(donthaveacc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 220, 30));

        password.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        password.setForeground(new java.awt.Color(255, 255, 255));
        password.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        password.setText("Password");
        RightPanel.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 90, 30));

        loginlabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        loginlabel.setForeground(new java.awt.Color(255, 255, 255));
        loginlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginlabel.setText("LOG IN");
        RightPanel.add(loginlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 110, 30));

        getContentPane().add(RightPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 250, 220));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void passtextfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passtextfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passtextfieldActionPerformed

    private void usertextfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usertextfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usertextfieldActionPerformed

    private void donthaveaccMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_donthaveaccMouseClicked
        registrationform rf = new registrationform();
        rf.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_donthaveaccMouseClicked

    private void donthaveaccMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_donthaveaccMouseEntered
        donthaveacc.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_donthaveaccMouseEntered

    private void donthaveaccMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_donthaveaccMouseExited
        donthaveacc.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_donthaveaccMouseExited

    private void loginbuttonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginbuttonMouseEntered
        loginbutton.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_loginbuttonMouseEntered

    private void loginbuttonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginbuttonMouseExited
        loginbutton.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_loginbuttonMouseExited

    private void loginbuttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginbuttonMouseClicked
        String user_email = usertextfield.getText();
        String user_password = new String(passtextfield.getPassword());

        if (user_email.isEmpty() || user_password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM user_table WHERE user_email = ? AND user_password = ?";

        dbConnector db = new dbConnector(); // Create an instance of dbConnector

        try (Connection conn = db.getConnection();  // Get connection from dbConnector
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, user_email);
            pst.setString(2, user_password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                String user_type = rs.getString("user_type");

                if (user_type.equals("Admin")) {
                    adminpage admin = new adminpage();
                    admin.setVisible(true);
                } else {
                    userpage user = new userpage();
                    user.setVisible(true);
                }

                this.dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_loginbuttonMouseClicked

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
            java.util.logging.Logger.getLogger(loginform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(loginform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(loginform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(loginform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new loginform().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Header;
    private javax.swing.JPanel HeaderPanel;
    private javax.swing.JPanel LeftPanel;
    private javax.swing.JPanel RightPanel;
    private javax.swing.JLabel donthaveacc;
    private javax.swing.JLabel loginbutton;
    private javax.swing.JLabel loginlabel;
    private javax.swing.JPasswordField passtextfield;
    private javax.swing.JLabel password;
    private javax.swing.JLabel studentlabel;
    private javax.swing.JLabel systemlabel;
    private javax.swing.JLabel username;
    private javax.swing.JTextField usertextfield;
    private javax.swing.JLabel violationicon;
    // End of variables declaration//GEN-END:variables
}
