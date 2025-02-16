/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import config.dbConnector;
import java.sql.SQLException;
import java.sql.ResultSet;
import net.proteanit.sql.DbUtils;
/**
 *
 * @author Admin
 */
public class userpage extends javax.swing.JFrame {

    /**
     * 
     */
    public userpage() {
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

        leftpanel = new javax.swing.JPanel();
        logout = new javax.swing.JLabel();
        icon = new javax.swing.JLabel();
        violation = new javax.swing.JLabel();
        student = new javax.swing.JLabel();
        dashboard = new javax.swing.JLabel();
        user_cnumber = new javax.swing.JLabel();
        user_fname = new javax.swing.JLabel();
        user_email = new javax.swing.JLabel();
        user_type = new javax.swing.JLabel();
        userspanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        leftpanel.setBackground(new java.awt.Color(0, 0, 0));
        leftpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logout.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logout.setText("LOG OUT");
        leftpanel.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, 140, 50));

        icon.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        icon.setForeground(new java.awt.Color(255, 255, 255));
        icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/117891351.png"))); // NOI18N
        leftpanel.add(icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 80, 80));

        violation.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        violation.setForeground(new java.awt.Color(255, 255, 255));
        violation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        violation.setText("VIOLATION");
        leftpanel.add(violation, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 140, 50));

        student.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        student.setForeground(new java.awt.Color(255, 255, 255));
        student.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        student.setText("STUDENT");
        leftpanel.add(student, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 140, 50));

        dashboard.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dashboard.setForeground(new java.awt.Color(255, 255, 255));
        dashboard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashboard.setText("DASHBOARD");
        leftpanel.add(dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 140, 50));

        user_cnumber.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        user_cnumber.setForeground(new java.awt.Color(255, 255, 255));
        user_cnumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_cnumber.setText("Contact Number");
        leftpanel.add(user_cnumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 100, -1));

        user_fname.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        user_fname.setForeground(new java.awt.Color(255, 255, 255));
        user_fname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_fname.setText("Fullname");
        leftpanel.add(user_fname, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));

        user_email.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        user_email.setForeground(new java.awt.Color(255, 255, 255));
        user_email.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_email.setText("Email Account");
        leftpanel.add(user_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 100, -1));

        user_type.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        user_type.setForeground(new java.awt.Color(255, 255, 255));
        user_type.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_type.setText("User");
        leftpanel.add(user_type, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 150, 70, -1));

        getContentPane().add(leftpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 140, 500));

        userspanel.setBackground(new java.awt.Color(204, 0, 0));
        userspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(userspanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 660, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(userpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(userpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(userpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(userpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new userpage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel icon;
    private javax.swing.JPanel leftpanel;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel student;
    private javax.swing.JLabel user_cnumber;
    private javax.swing.JLabel user_email;
    private javax.swing.JLabel user_fname;
    private javax.swing.JLabel user_type;
    private javax.swing.JPanel userspanel;
    private javax.swing.JLabel violation;
    // End of variables declaration//GEN-END:variables
}
