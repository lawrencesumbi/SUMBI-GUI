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
        login = new javax.swing.JLabel();
        login1 = new javax.swing.JLabel();
        login3 = new javax.swing.JLabel();
        RightPanel = new javax.swing.JPanel();
        loginbutton = new javax.swing.JLabel();
        usertextfield = new javax.swing.JTextField();
        passtextfield = new javax.swing.JPasswordField();
        username1 = new javax.swing.JLabel();
        password1 = new javax.swing.JLabel();
        password2 = new javax.swing.JLabel();
        login2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        HeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        HeaderPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Header.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ifoto-ai_1738725918062.png"))); // NOI18N
        HeaderPanel.add(Header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 130));

        getContentPane().add(HeaderPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 130));

        LeftPanel.setBackground(new java.awt.Color(0, 0, 0));
        LeftPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        login.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        login.setForeground(new java.awt.Color(255, 255, 255));
        login.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        login.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/violation-logo-860-16424 (1).png"))); // NOI18N
        LeftPanel.add(login, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 225, 90));

        login1.setFont(new java.awt.Font("Copperplate Gothic Bold", 3, 28)); // NOI18N
        login1.setForeground(new java.awt.Color(255, 255, 255));
        login1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        login1.setText("System");
        LeftPanel.add(login1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 140, 30));

        login3.setFont(new java.awt.Font("Copperplate Gothic Bold", 3, 30)); // NOI18N
        login3.setForeground(new java.awt.Color(255, 255, 255));
        login3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        login3.setText("Student");
        LeftPanel.add(login3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 160, 40));

        getContentPane().add(LeftPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 250, 220));

        RightPanel.setBackground(new java.awt.Color(204, 0, 0));
        RightPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loginbutton.setBackground(new java.awt.Color(255, 255, 255));
        loginbutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        loginbutton.setForeground(new java.awt.Color(255, 255, 255));
        loginbutton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginbutton.setText("Log in");
        loginbutton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        RightPanel.add(loginbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 90, 30));

        usertextfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usertextfieldActionPerformed(evt);
            }
        });
        RightPanel.add(usertextfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 220, -1));

        passtextfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passtextfieldActionPerformed(evt);
            }
        });
        RightPanel.add(passtextfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 220, -1));

        username1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        username1.setForeground(new java.awt.Color(255, 255, 255));
        username1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        username1.setText("Username");
        RightPanel.add(username1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 90, 30));

        password1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        password1.setForeground(new java.awt.Color(255, 255, 255));
        password1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        password1.setText("Dont have an account? Register");
        RightPanel.add(password1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 220, 30));

        password2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        password2.setForeground(new java.awt.Color(255, 255, 255));
        password2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        password2.setText("Password");
        RightPanel.add(password2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 90, 30));

        login2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        login2.setForeground(new java.awt.Color(255, 255, 255));
        login2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        login2.setText("LOG IN");
        RightPanel.add(login2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 110, 30));

        getContentPane().add(RightPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 250, 220));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void passtextfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passtextfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passtextfieldActionPerformed

    private void usertextfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usertextfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usertextfieldActionPerformed

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
    private javax.swing.JLabel login;
    private javax.swing.JLabel login1;
    private javax.swing.JLabel login2;
    private javax.swing.JLabel login3;
    private javax.swing.JLabel loginbutton;
    private javax.swing.JPasswordField passtextfield;
    private javax.swing.JLabel password1;
    private javax.swing.JLabel password2;
    private javax.swing.JLabel username1;
    private javax.swing.JTextField usertextfield;
    // End of variables declaration//GEN-END:variables
}
