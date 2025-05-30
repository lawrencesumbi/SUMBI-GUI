/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import config.dbConnector;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
/**
 *
 * @author Admin
 */
public class forgotpassword extends javax.swing.JFrame {
    private static int generatedPin;
    /**
     * Creates new form forgotpassword
     */
    public forgotpassword() {
        initComponents();
    }
    
    private void sendPin() {
    dbConnector dbc = new dbConnector();
    Connection con = dbc.getConnection();

    String user_input = enteremail.getText().trim();
    if (user_input.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter your email or contact number.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    boolean isEmail = user_input.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$"); 
    boolean isPhone = user_input.matches("^\\d{10,15}$");

    if (!isEmail && !isPhone) {
        JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid email or contact number.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        String query = "SELECT user_email, user_cnumber FROM user_table WHERE user_email = ? OR user_cnumber = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, user_input);
        pst.setString(2, user_input);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String foundEmail = rs.getString("user_email"); 

            // Generate random 6-digit PIN
            int generatedPin = 100000 + new Random().nextInt(900000);

            // Display PIN with copy option
            String message = "<html><center>NOTIFICATION:<br>Reset PIN for <b>" + foundEmail + "</b> is:<br><h2>" + generatedPin + "</h2></center></html>";

            int option = JOptionPane.showOptionDialog(
                this,
                message,
                "PIN Generated",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Copy PIN", "OK"},
                "OK"
            );

            if (option == 0) { // If "Copy PIN" is clicked
                StringSelection stringSelection = new StringSelection(String.valueOf(generatedPin));
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                JOptionPane.showMessageDialog(this, "PIN copied to clipboard!");
            }

            // Proceed to verification screen
            this.setVisible(false);
            new verification(foundEmail, generatedPin).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Email or contact number not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        con.close();
    } catch (Exception e) {
        e.printStackTrace();
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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        back = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        enteremail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        sendbutton = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/6179011.png"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 230, 190));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("a pin to help you reset your password.");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 310, 30));

        back.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        back.setForeground(new java.awt.Color(255, 255, 255));
        back.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/image-removebg-preview (19).png"))); // NOI18N
        back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backMouseExited(evt);
            }
        });
        jPanel1.add(back, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 60));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Forgot Password?");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 140, 30));

        enteremail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        enteremail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        enteremail.setToolTipText("");
        enteremail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        enteremail.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        enteremail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enteremailActionPerformed(evt);
            }
        });
        jPanel1.add(enteremail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 360, 40));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Enter your email/phone # and we'll send you ");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 370, 30));

        sendbutton.setBackground(new java.awt.Color(255, 255, 255));
        sendbutton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        sendbutton.setForeground(new java.awt.Color(255, 255, 255));
        sendbutton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sendbutton.setText("Send");
        sendbutton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        sendbutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendbuttonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sendbuttonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sendbuttonMouseExited(evt);
            }
        });
        sendbutton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sendbuttonKeyPressed(evt);
            }
        });
        jPanel1.add(sendbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 430, 110, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backMouseClicked
        new loginform().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_backMouseClicked

    private void backMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backMouseEntered

    }//GEN-LAST:event_backMouseEntered

    private void backMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backMouseExited

    }//GEN-LAST:event_backMouseExited

    private void enteremailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enteremailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_enteremailActionPerformed

    private void sendbuttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendbuttonMouseClicked
        sendPin();
    }//GEN-LAST:event_sendbuttonMouseClicked

    private void sendbuttonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendbuttonMouseEntered
        sendbutton.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_sendbuttonMouseEntered

    private void sendbuttonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendbuttonMouseExited
        sendbutton.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_sendbuttonMouseExited

    private void sendbuttonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sendbuttonKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sendbuttonKeyPressed

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
            java.util.logging.Logger.getLogger(forgotpassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(forgotpassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(forgotpassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(forgotpassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new forgotpassword().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel back;
    private javax.swing.JTextField enteremail;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel sendbutton;
    // End of variables declaration//GEN-END:variables
}
