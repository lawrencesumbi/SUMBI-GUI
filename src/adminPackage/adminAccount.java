/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import config.dbConnector;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
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
public class adminAccount extends javax.swing.JFrame {
    private String user_fname;
    /**
     * Creates new form adminAccount
     */
    public adminAccount() {
        initComponents();
    }

    public adminAccount(String user_fname) {
        this.user_fname = user_fname;
        initComponents();
        displayImage(user_fname);
        J_user_fname.setText(user_fname);  
        displayData();
        loadUserData(user_fname);
    }
    
    public void displayData() {
        try {
            dbConnector dbc = new dbConnector();
            Connection conn = dbc.getConnection();

            String query = "SELECT * FROM user_table WHERE user_fname = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, user_fname);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                fullNameTextField.setText(rs.getString("user_fname"));
                contactNumberTextField.setText(rs.getString("user_cnumber"));
                emailTextField.setText(rs.getString("user_email"));
                               
                String enteredPassword = getUserPassword();
                oldPasswordField.setText(enteredPassword);
                
                userIDtextfield.setText(rs.getString("user_id"));
                
           
            } else {
                System.out.println("No user data found.");
            }

            rs.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
    
    
    
    private String saveImageToFolder(String user_email) {
        try {
            // Convert JLabel Icon to BufferedImage
            Icon icon = imageLabel.getIcon();
            if (icon instanceof ImageIcon) {
                Image image = ((ImageIcon) icon).getImage();
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = bufferedImage.createGraphics();
                g2.drawImage(image, 0, 0, null);
                g2.dispose();

                // Define Folder and File Name
                String folderPath = "src/usersImages";
                File directory = new File(folderPath);
                if (!directory.exists()) {
                    directory.mkdir(); // Create folder if not exists
                }

                // Save image with unique name
                String filePath = folderPath + user_email + ".jpg";
                File outputFile = new File(filePath);
                ImageIO.write(bufferedImage, "jpg", outputFile);

                return filePath; // Return the saved image path
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    private void loadUserData(String user_fname) {
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

                        // Load image from file
                        if (imagePath != null && !imagePath.isEmpty()) {
                            File file = new File(imagePath);
                            if (file.exists()) {
                                ImageIcon icon = new ImageIcon(imagePath);
                                Image img = icon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
                                imageLabel.setIcon(new ImageIcon(img));
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
    
    
    
    private boolean isEmailDuplicate(Connection conn, String email) {
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM user_table WHERE user_email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }
    
    public static String passwordHash(String user_password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(user_password.getBytes());
            byte[] rbt = md.digest();
            StringBuilder sb = new StringBuilder();

            for (byte b : rbt) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String getUserPassword() {
        return new String(oldPasswordField.getPassword());
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
        user_fnamelabel = new javax.swing.JLabel();
        fullNameTextField = new javax.swing.JTextField();
        user_cnumberlabel = new javax.swing.JLabel();
        contactNumberTextField = new javax.swing.JTextField();
        user_emaillabel = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        user_passwordlabel = new javax.swing.JLabel();
        imageLabel = new javax.swing.JLabel();
        cancel = new javax.swing.JLabel();
        updatePassword = new javax.swing.JLabel();
        user_fnamelabel1 = new javax.swing.JLabel();
        userIDtextfield = new javax.swing.JTextField();
        oldPasswordField = new javax.swing.JPasswordField();
        user_passwordlabel1 = new javax.swing.JLabel();
        newPasswordField = new javax.swing.JPasswordField();
        save = new javax.swing.JLabel();
        imageLabel1 = new javax.swing.JLabel();
        settings2 = new javax.swing.JLabel();
        settings3 = new javax.swing.JLabel();
        activityLogs = new javax.swing.JLabel();
        activityLogs3 = new javax.swing.JLabel();
        activityLogs2 = new javax.swing.JLabel();
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
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        userspanel.setBackground(new java.awt.Color(204, 0, 0));
        userspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        user_fnamelabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        user_fnamelabel.setForeground(new java.awt.Color(255, 255, 255));
        user_fnamelabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_fnamelabel.setText("Full Name:");
        userspanel.add(user_fnamelabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, 90, 20));

        fullNameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        fullNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullNameTextFieldActionPerformed(evt);
            }
        });
        userspanel.add(fullNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, 200, 30));

        user_cnumberlabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        user_cnumberlabel.setForeground(new java.awt.Color(255, 255, 255));
        user_cnumberlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_cnumberlabel.setText("Contact Number:");
        userspanel.add(user_cnumberlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 160, 20));

        contactNumberTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        contactNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactNumberTextFieldActionPerformed(evt);
            }
        });
        userspanel.add(contactNumberTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 240, 200, 30));

        user_emaillabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        user_emaillabel.setForeground(new java.awt.Color(255, 255, 255));
        user_emaillabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_emaillabel.setText("Email:");
        userspanel.add(user_emaillabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, 70, 20));

        emailTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });
        userspanel.add(emailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 300, 200, 30));

        user_passwordlabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        user_passwordlabel.setForeground(new java.awt.Color(255, 255, 255));
        user_passwordlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_passwordlabel.setText("Old Password:");
        userspanel.add(user_passwordlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 360, 120, 20));

        imageLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        imageLabel.setForeground(new java.awt.Color(255, 255, 255));
        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageLabelMouseClicked(evt);
            }
        });
        userspanel.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 180, 150, 150));

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
        userspanel.add(cancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 490, 80, 30));

        updatePassword.setBackground(new java.awt.Color(255, 255, 255));
        updatePassword.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        updatePassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updatePassword.setText("ChangePass");
        updatePassword.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        updatePassword.setOpaque(true);
        updatePassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updatePasswordMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                updatePasswordMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                updatePasswordMouseExited(evt);
            }
        });
        userspanel.add(updatePassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 490, 110, 30));

        user_fnamelabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_fnamelabel1.setForeground(new java.awt.Color(255, 255, 255));
        user_fnamelabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_fnamelabel1.setText("User ID");
        userspanel.add(user_fnamelabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 350, 60, 30));

        userIDtextfield.setEditable(false);
        userIDtextfield.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        userIDtextfield.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        userIDtextfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userIDtextfieldActionPerformed(evt);
            }
        });
        userspanel.add(userIDtextfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 350, 40, -1));

        oldPasswordField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        oldPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oldPasswordFieldActionPerformed(evt);
            }
        });
        oldPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                oldPasswordFieldKeyPressed(evt);
            }
        });
        userspanel.add(oldPasswordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 360, 200, 30));

        user_passwordlabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        user_passwordlabel1.setForeground(new java.awt.Color(255, 255, 255));
        user_passwordlabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_passwordlabel1.setText("New Password:");
        userspanel.add(user_passwordlabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 420, 130, 20));

        newPasswordField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        newPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPasswordFieldActionPerformed(evt);
            }
        });
        newPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                newPasswordFieldKeyPressed(evt);
            }
        });
        userspanel.add(newPasswordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 200, 30));

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
        userspanel.add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 490, 80, 30));

        imageLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        imageLabel1.setForeground(new java.awt.Color(255, 255, 255));
        imageLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/image-removebg-preview1.png"))); // NOI18N
        imageLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageLabel1MouseClicked(evt);
            }
        });
        userspanel.add(imageLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 180, 150, 150));

        settings2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        settings2.setForeground(new java.awt.Color(255, 255, 255));
        settings2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        settings2.setText("ACCOUNT SETTINGS");
        settings2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settings2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settings2MouseExited(evt);
            }
        });
        userspanel.add(settings2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, 250, 50));

        settings3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        settings3.setForeground(new java.awt.Color(255, 255, 255));
        settings3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        settings3.setText("Account Settings");
        settings3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settings3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settings3MouseExited(evt);
            }
        });
        userspanel.add(settings3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 150, 30));

        activityLogs.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        activityLogs.setForeground(new java.awt.Color(255, 255, 255));
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
        userspanel.add(activityLogs, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 150, 30));

        activityLogs3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        activityLogs3.setForeground(new java.awt.Color(255, 255, 255));
        activityLogs3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        activityLogs3.setText("Appearance");
        activityLogs3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                activityLogs3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                activityLogs3MouseExited(evt);
            }
        });
        userspanel.add(activityLogs3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 150, 30));

        activityLogs2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        activityLogs2.setForeground(new java.awt.Color(255, 255, 255));
        activityLogs2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        activityLogs2.setText("About System");
        activityLogs2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                activityLogs2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                activityLogs2MouseExited(evt);
            }
        });
        userspanel.add(activityLogs2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 30, 150, 30));

        getContentPane().add(userspanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 710, 600));

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
        settings.setForeground(new java.awt.Color(255, 255, 255));
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

        getContentPane().add(leftpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 600));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void fullNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fullNameTextFieldActionPerformed

    private void contactNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactNumberTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contactNumberTextFieldActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed
        // TODO add your handling code shere:
    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void imageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageLabelMouseClicked
        imageHandler.chooseImage(imageLabel);
    }//GEN-LAST:event_imageLabelMouseClicked

    private void cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseClicked
        try {
            dbConnector dbc = new dbConnector();
            Connection conn = dbc.getConnection(); // Ensure you have a method to get Connection

            String query = "SELECT * FROM user_table WHERE user_fname = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, user_fname); // Set the parameter

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                fullNameTextField.setText(rs.getString("user_fname"));
                contactNumberTextField.setText(rs.getString("user_cnumber"));
                emailTextField.setText(rs.getString("user_email"));

                oldPasswordField.setText("");
                newPasswordField.setText("");
                
            } else {
                System.out.println("No user data found.");
            }

            rs.close(); 

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_cancelMouseClicked

    private void cancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseEntered

    }//GEN-LAST:event_cancelMouseEntered

    private void cancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseExited

    }//GEN-LAST:event_cancelMouseExited

    private void updatePasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updatePasswordMouseClicked
        String user_fname = fullNameTextField.getText();
        String user_cnumber = contactNumberTextField.getText();
        String user_email = emailTextField.getText();

        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();

        if (!user_cnumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Contact number must be in digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPassword.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password should have at least 8 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!user_email.toLowerCase().endsWith(".com")) {
            JOptionPane.showMessageDialog(this, "Email must be valid. Please enter a valid email account.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hashedNewPassword = passwordHash(newPassword);

        String oldPasswordHash = passwordHash(oldPassword);

        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            String selectQuery = "SELECT user_password FROM user_table WHERE user_email = ?";
            PreparedStatement selectPstmt = conn.prepareStatement(selectQuery);
            selectPstmt.setString(1, user_email);
            ResultSet rs = selectPstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("user_password");

                if (!storedPassword.equals(oldPasswordHash)) {
                    JOptionPane.showMessageDialog(this, "Old password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String updateQuery = "UPDATE user_table SET user_fname = ?, user_cnumber = ?, user_password = ? WHERE user_email = ?";
                PreparedStatement updatePstmt = conn.prepareStatement(updateQuery);
                updatePstmt.setString(1, user_fname);
                updatePstmt.setString(2, user_cnumber);
                updatePstmt.setString(3, hashedNewPassword);
                updatePstmt.setString(4, user_email);

                int rowsUpdated = updatePstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Password changed successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                updatePstmt.close();
            } else {
                JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }


            rs.close();
            selectPstmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_updatePasswordMouseClicked

    private void updatePasswordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updatePasswordMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_updatePasswordMouseEntered

    private void updatePasswordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updatePasswordMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_updatePasswordMouseExited

    private void userIDtextfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userIDtextfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userIDtextfieldActionPerformed

    private void oldPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oldPasswordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oldPasswordFieldActionPerformed

    private void oldPasswordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oldPasswordFieldKeyPressed

    }//GEN-LAST:event_oldPasswordFieldKeyPressed

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked

        String sql = "SELECT user_id FROM user_table WHERE user_fname = ?"; 
    
        dbConnector db = new dbConnector();

        try (Connection conn = db.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, this.user_fname); // Set the logged-in user's first name

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int user_id = rs.getInt("user_id");
                int response = JOptionPane.showConfirmDialog(this,
                    "Confirm Log Out?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    logActivity(user_id, "Logged out");
                    new loginform().setVisible(true);
                    this.dispose();
                }
            }        

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_logoutMouseClicked

    private void logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseEntered
        logout.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_logoutMouseEntered

    private void logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseExited
        logout.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_logoutMouseExited

    private void violationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseClicked
        new adminViolation(user_fname).setVisible(true);
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
        settings.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_settingsMouseExited

    private void newPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPasswordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newPasswordFieldActionPerformed

    private void newPasswordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newPasswordFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_newPasswordFieldKeyPressed

    private void saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseClicked
        String user_fname = fullNameTextField.getText();
        String user_cnumber = contactNumberTextField.getText();
        String user_email = emailTextField.getText();  

        if (!user_cnumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Contact number must be in digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!user_email.toLowerCase().endsWith(".com")) {
            JOptionPane.showMessageDialog(this, "Email must be valid. Please enter a valid email account.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String selectQuery = "SELECT user_email FROM user_table WHERE user_email = ?";
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectQuery)) {
                selectPstmt.setString(1, user_email);
                try (ResultSet rs = selectPstmt.executeQuery()) {
                    if (rs.next()) {
                        
                    boolean hasImage = (imageLabel.getIcon() != null);
                    String updateQuery;

                    if (hasImage) {
                        updateQuery = "UPDATE user_table SET user_fname = ?, user_cnumber = ?, image_path = ? WHERE user_email = ?";
                    } else {
                        updateQuery = "UPDATE user_table SET user_fname = ?, user_cnumber = ? WHERE user_email = ?";
                    }

                    try (PreparedStatement updatePstmt = conn.prepareStatement(updateQuery)) {
                        updatePstmt.setString(1, user_fname);
                        updatePstmt.setString(2, user_cnumber);

                        if (hasImage) {
                            String imagePath = saveImageToFolder(user_email);
                            updatePstmt.setString(3, imagePath);
                            updatePstmt.setString(4, user_email);
                        } else {
                            updatePstmt.setString(3, user_email);
                        }

                            int rowsUpdated = updatePstmt.executeUpdate();
                            if (rowsUpdated > 0) {
                                JOptionPane.showMessageDialog(this, "Account information updated successfully!");
                            } else {
                                JOptionPane.showMessageDialog(this, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveMouseClicked

    private void saveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_saveMouseEntered

    private void saveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_saveMouseExited

    private void dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseClicked
        new adminDashboard(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_dashboardMouseClicked

    private void imageLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageLabel1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_imageLabel1MouseClicked

    private void settings2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settings2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_settings2MouseEntered

    private void settings2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settings2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_settings2MouseExited

    private void settings3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settings3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_settings3MouseEntered

    private void settings3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settings3MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_settings3MouseExited

    private void activityLogsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_activityLogsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_activityLogsMouseEntered

    private void activityLogsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_activityLogsMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_activityLogsMouseExited

    private void activityLogs3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_activityLogs3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_activityLogs3MouseEntered

    private void activityLogs3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_activityLogs3MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_activityLogs3MouseExited

    private void activityLogs2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_activityLogs2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_activityLogs2MouseEntered

    private void activityLogs2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_activityLogs2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_activityLogs2MouseExited

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
            java.util.logging.Logger.getLogger(adminAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(adminAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(adminAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(adminAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new adminAccount().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel J_user_fname;
    private javax.swing.JLabel activityLogs;
    private javax.swing.JLabel activityLogs2;
    private javax.swing.JLabel activityLogs3;
    private javax.swing.JLabel cancel;
    private javax.swing.JTextField contactNumberTextField;
    private javax.swing.JLabel dash_icon1;
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel displayImage;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JTextField fullNameTextField;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel imageLabel1;
    private javax.swing.JPanel leftpanel;
    private javax.swing.JLabel log_icon;
    private javax.swing.JLabel logout;
    private javax.swing.JPasswordField newPasswordField;
    private javax.swing.JPasswordField oldPasswordField;
    private javax.swing.JLabel rec_icon;
    private javax.swing.JLabel record;
    private javax.swing.JLabel save;
    private javax.swing.JLabel sett_icon;
    private javax.swing.JLabel settings;
    private javax.swing.JLabel settings2;
    private javax.swing.JLabel settings3;
    private javax.swing.JLabel stud_icon;
    private javax.swing.JLabel student;
    private javax.swing.JLabel updatePassword;
    private javax.swing.JTextField userIDtextfield;
    private javax.swing.JLabel user_cnumberlabel;
    private javax.swing.JLabel user_emaillabel;
    private javax.swing.JLabel user_fnamelabel;
    private javax.swing.JLabel user_fnamelabel1;
    private javax.swing.JLabel user_passwordlabel;
    private javax.swing.JLabel user_passwordlabel1;
    private javax.swing.JLabel user_type2;
    private javax.swing.JLabel users;
    private javax.swing.JLabel users_icon;
    private javax.swing.JPanel userspanel;
    private javax.swing.JLabel vio_icon;
    private javax.swing.JLabel violation;
    // End of variables declaration//GEN-END:variables
}
