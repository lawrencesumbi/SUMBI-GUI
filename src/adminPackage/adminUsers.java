
import config.Session;
import javax.swing.*;
import java.sql.*;
import config.dbConnector;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.table.TableModel;
import javax.swing.JLabel;


public class adminUsers extends javax.swing.JFrame {
    private String user_fname;
    private String vio_id;
    private String stud_id;


    /**
     * Creates new form adminUsers
     */
    public adminUsers() {
        initComponents();
        displayData();
    }
    
    public adminUsers(String user_fname) {
        this.user_fname = user_fname;
        initComponents();
        displayImage(user_fname);
        J_user_fname.setText(user_fname);
        displayData();
    }
    

    public void displayData() {
        try {
            dbConnector dbc = new dbConnector();

            int currentUserId = Session.getInstance().getUid(); // Get current user's ID

            // Exclude current user from the result
            String query = "SELECT user_id, user_fname, user_cnumber, user_email, user_type, user_status " +
                           "FROM user_table WHERE user_id != " + currentUserId;

            ResultSet rs = dbc.getData(query);

            // Clear input fields
            userIDtextfield.setText("");
            fullNameTextField.setText("");  
            contactNumberTextField.setText("");  
            emailTextField.setText("");    
            userTypeComboBox.setSelectedIndex(-1);
            userStatusComboBox.setSelectedIndex(-1);
            searchfield.setText(""); 

            // Set the data model
            user_table.setModel(DbUtils.resultSetToTableModel(rs));
            user_table.getColumnModel().getColumn(3).setPreferredWidth(150);

            // Set custom column headers
            String[] columnNames = {
                "User ID", "Full Name", "Contact Number", "Email", "User Type", "Status"
            };

            for (int i = 0; i < columnNames.length; i++) {
                user_table.getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);
            }

            // Force header update
            user_table.getTableHeader().repaint();

            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
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
    
    private void highlightRow() {
        String searchText = searchfield.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            return;
        }

        user_table.clearSelection();

        boolean matchFound = false;

        for (int i = 0; i < user_table.getRowCount(); i++) {
            for (int j = 1; j <= 6; j++) {
                Object cellValue = user_table.getValueAt(i, j);

                if (cellValue != null) {
                    String cellText = cellValue.toString().trim().toLowerCase();

                    if (cellText.contains(searchText)) { 
                        user_table.addRowSelectionInterval(i, i);
                        user_table.scrollRectToVisible(user_table.getCellRect(i, 0, true));
                        matchFound = true;
                        break;
                    }
                }
            }
        }

        if (!matchFound) {
            JOptionPane.showMessageDialog(null, "No matching record found!", "Search", JOptionPane.INFORMATION_MESSAGE);
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

        jCheckBox1 = new javax.swing.JCheckBox();
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
        user_type1 = new javax.swing.JLabel();
        dash_icon = new javax.swing.JLabel();
        stud_icon = new javax.swing.JLabel();
        vio_icon = new javax.swing.JLabel();
        rec_icon = new javax.swing.JLabel();
        users_icon = new javax.swing.JLabel();
        sett_icon = new javax.swing.JLabel();
        log_icon = new javax.swing.JLabel();
        userspanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        user_table = new javax.swing.JTable();
        refresh = new javax.swing.JLabel();
        add = new javax.swing.JLabel();
        edit = new javax.swing.JLabel();
        delete = new javax.swing.JLabel();
        user_fnamelabel = new javax.swing.JLabel();
        fullNameTextField = new javax.swing.JTextField();
        user_cnumberlabel = new javax.swing.JLabel();
        contactNumberTextField = new javax.swing.JTextField();
        user_emaillabel = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        user_typelabel = new javax.swing.JLabel();
        userTypeComboBox = new javax.swing.JComboBox<>();
        search = new javax.swing.JLabel();
        searchfield = new javax.swing.JTextField();
        imageLabel = new javax.swing.JLabel();
        user_statuslabel = new javax.swing.JLabel();
        userStatusComboBox = new javax.swing.JComboBox<>();
        user_fnamelabel1 = new javax.swing.JLabel();
        userIDtextfield = new javax.swing.JTextField();
        imageLabel1 = new javax.swing.JLabel();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        displayImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                displayImageMouseClicked(evt);
            }
        });
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
        users.setForeground(new java.awt.Color(255, 255, 0));
        users.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        users.setText("USERS");
        users.addMouseListener(new java.awt.event.MouseAdapter() {
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
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settingsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settingsMouseExited(evt);
            }
        });
        leftpanel.add(settings, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 480, 100, 50));

        user_type1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        user_type1.setForeground(new java.awt.Color(255, 255, 255));
        user_type1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_type1.setText("Admin");
        leftpanel.add(user_type1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 70, -1));

        dash_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-dashboard-layout-24.png"))); // NOI18N
        dash_icon.setText("jLabel1");
        leftpanel.add(dash_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 30, 30));

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

        getContentPane().add(leftpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 600));

        userspanel.setBackground(new java.awt.Color(204, 0, 0));
        userspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBackground(new java.awt.Color(204, 0, 0));
        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        user_table.setModel(new javax.swing.table.DefaultTableModel(
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
        user_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                user_tableMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                user_tableMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(user_table);

        userspanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 710, 300));

        refresh.setBackground(new java.awt.Color(255, 255, 255));
        refresh.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        refresh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        refresh.setText("REFRESH/CLEAR");
        refresh.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        refresh.setOpaque(true);
        refresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshMouseExited(evt);
            }
        });
        userspanel.add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 250, 120, 30));

        add.setBackground(new java.awt.Color(255, 255, 255));
        add.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        add.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add.setText("ADD");
        add.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        add.setOpaque(true);
        add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addMouseExited(evt);
            }
        });
        userspanel.add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 60, 30));

        edit.setBackground(new java.awt.Color(255, 255, 255));
        edit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        edit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        edit.setText("EDIT");
        edit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        edit.setOpaque(true);
        edit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                editMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                editMouseExited(evt);
            }
        });
        userspanel.add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 60, 30));

        delete.setBackground(new java.awt.Color(255, 255, 255));
        delete.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        delete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        delete.setText("DELETE");
        delete.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        delete.setOpaque(true);
        delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteMouseExited(evt);
            }
        });
        userspanel.add(delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 250, 60, 30));

        user_fnamelabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_fnamelabel.setForeground(new java.awt.Color(255, 255, 255));
        user_fnamelabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_fnamelabel.setText("Full Name");
        userspanel.add(user_fnamelabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 110, 20));

        fullNameTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        fullNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullNameTextFieldActionPerformed(evt);
            }
        });
        userspanel.add(fullNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 190, -1));

        user_cnumberlabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_cnumberlabel.setForeground(new java.awt.Color(255, 255, 255));
        user_cnumberlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_cnumberlabel.setText("Contact Number");
        userspanel.add(user_cnumberlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 110, 20));

        contactNumberTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        contactNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactNumberTextFieldActionPerformed(evt);
            }
        });
        userspanel.add(contactNumberTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 190, -1));

        user_emaillabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_emaillabel.setForeground(new java.awt.Color(255, 255, 255));
        user_emaillabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_emaillabel.setText("Email");
        userspanel.add(user_emaillabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 70, 20));

        emailTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });
        userspanel.add(emailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 190, -1));

        user_typelabel.setBackground(new java.awt.Color(255, 255, 255));
        user_typelabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_typelabel.setForeground(new java.awt.Color(255, 255, 255));
        user_typelabel.setText("User Type");
        userspanel.add(user_typelabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 70, 20));

        userTypeComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        userTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "User", "Admin" }));
        userTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userTypeComboBoxActionPerformed(evt);
            }
        });
        userspanel.add(userTypeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 190, -1));

        search.setBackground(new java.awt.Color(255, 255, 255));
        search.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        search.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        search.setText("SEARCH");
        search.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        search.setOpaque(true);
        search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchMouseExited(evt);
            }
        });
        userspanel.add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, 70, 30));

        searchfield.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        searchfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchfieldActionPerformed(evt);
            }
        });
        searchfield.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchfieldKeyPressed(evt);
            }
        });
        userspanel.add(searchfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 250, 150, 30));

        imageLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        imageLabel.setForeground(new java.awt.Color(255, 255, 255));
        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageLabelMouseClicked(evt);
            }
        });
        userspanel.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, 150, 150));

        user_statuslabel.setBackground(new java.awt.Color(255, 255, 255));
        user_statuslabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_statuslabel.setForeground(new java.awt.Color(255, 255, 255));
        user_statuslabel.setText("User Status");
        userspanel.add(user_statuslabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, 80, 20));

        userStatusComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        userStatusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "Active" }));
        userStatusComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userStatusComboBoxActionPerformed(evt);
            }
        });
        userspanel.add(userStatusComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 190, 190, -1));

        user_fnamelabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_fnamelabel1.setForeground(new java.awt.Color(255, 255, 255));
        user_fnamelabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_fnamelabel1.setText("User ID");
        userspanel.add(user_fnamelabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 30, 60, 30));

        userIDtextfield.setEditable(false);
        userIDtextfield.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        userIDtextfield.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        userIDtextfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userIDtextfieldActionPerformed(evt);
            }
        });
        userspanel.add(userIDtextfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 30, 40, -1));

        imageLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        imageLabel1.setForeground(new java.awt.Color(255, 255, 255));
        imageLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/image-removebg-preview1.png"))); // NOI18N
        imageLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageLabel1MouseClicked(evt);
            }
        });
        userspanel.add(imageLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, 150, 150));

        getContentPane().add(userspanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 710, 600));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void refreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseClicked
        displayData();
        userIDtextfield.setText("");
        fullNameTextField.setText("");  
        contactNumberTextField.setText("");  
        emailTextField.setText("");  
        userTypeComboBox.setSelectedIndex(-1);
        userStatusComboBox.setSelectedIndex(-1);
        searchfield.setText(""); 
        imageLabel.setIcon(null);
        
    }//GEN-LAST:event_refreshMouseClicked

    private void refreshMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseEntered
        
    }//GEN-LAST:event_refreshMouseEntered

    private void refreshMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseExited
        
    }//GEN-LAST:event_refreshMouseExited

    private void addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseClicked
        String user_fname = fullNameTextField.getText();
        String user_cnumber = contactNumberTextField.getText();
        String user_email = emailTextField.getText();

        // Set default password and hash it
        String defaultPassword = "password123";
        String user_password = passwordHash(defaultPassword);  // Assumes passwordHash() is implemented

        String user_type = (userTypeComboBox.getSelectedItem() != null) ? userTypeComboBox.getSelectedItem().toString() : "";
        String user_status = (userStatusComboBox.getSelectedItem() != null) ? userStatusComboBox.getSelectedItem().toString() : "";
        String imagePath = null;

        if (imageLabel.getIcon() != null) {
            imagePath = saveImageToFolder(user_email);
        }

        // Basic validations
        if (user_fname.isEmpty() || user_cnumber.isEmpty() || user_email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!user_cnumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Contact number must be in digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!user_email.toLowerCase().endsWith(".com")) {
            JOptionPane.showMessageDialog(null, "Email must be valid. Please enter a valid email account.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {

            if (isEmailDuplicate(conn, user_email)) {
                JOptionPane.showMessageDialog(null, "Email already exists. Please use a different email.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to add this user?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                conn.close();
                return;
            }

            String sql;
            if (imagePath != null) {
                sql = "INSERT INTO user_table (user_fname, user_cnumber, user_email, user_password, user_type, user_status, image_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO user_table (user_fname, user_cnumber, user_email, user_password, user_type, user_status) VALUES (?, ?, ?, ?, ?, ?)";
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user_fname);
                pstmt.setString(2, user_cnumber);
                pstmt.setString(3, user_email);
                pstmt.setString(4, user_password);
                pstmt.setString(5, user_type);
                pstmt.setString(6, user_status);

                if (imagePath != null) {
                    pstmt.setString(7, imagePath);
                }

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(null, "Registration successful!\n(Default password: password123)");
                    int uid = Session.getInstance().getUid();
                    logActivity(uid, "Added new user: " + user_email);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addMouseClicked

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
    
   
    
    
    
    private void addMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_addMouseEntered

    private void addMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_addMouseExited

    private void editMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseClicked
        String user_fname = fullNameTextField.getText();
    String user_cnumber = contactNumberTextField.getText();
    String user_email = emailTextField.getText();   
    String user_type = (userTypeComboBox.getSelectedItem() != null) ? userTypeComboBox.getSelectedItem().toString() : "";
    String user_status = (userStatusComboBox.getSelectedItem() != null) ? userStatusComboBox.getSelectedItem().toString() : "";

    if (user_fname.isEmpty() || user_cnumber.isEmpty() || user_email.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!user_cnumber.matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "Contact number must be in digits.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!user_email.toLowerCase().endsWith(".com")) {
        JOptionPane.showMessageDialog(this, "Email must be valid. Please enter a valid email account.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String url = "jdbc:mysql://localhost:3306/sumbi_db";
    String dbUser = "root";
    String pass = "";

    try (Connection conn = DriverManager.getConnection(url, dbUser, pass)) {
        if (!isEmailDuplicate(conn, user_email)) {
            JOptionPane.showMessageDialog(this, "Email does not exist. Please check the email.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this user?", "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            conn.close();
            return;
        }

        String sql;
        if (imageLabel.getIcon() == null) {
            // No image: Set image_path to NULL
            sql = "UPDATE user_table SET user_fname = ?, user_cnumber = ?, user_type = ?, user_status = ?, image_path = NULL WHERE user_email = ?";
        } else {
            // Image present: Update image_path
            sql = "UPDATE user_table SET user_fname = ?, user_cnumber = ?, user_type = ?, user_status = ?, image_path = ? WHERE user_email = ?";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user_fname);
            pstmt.setString(2, user_cnumber);
            pstmt.setString(3, user_type);
            pstmt.setString(4, user_status);

            if (imageLabel.getIcon() != null) {
                String imagePath = saveImageToFolder(user_email);
                pstmt.setString(5, imagePath);
                pstmt.setString(6, user_email);
            } else {
                pstmt.setString(5, user_email);
            }

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "User information updated successfully!");
                int uid = Session.getInstance().getUid();
                logActivity(uid, "Updated user: " + user_email);
            } else {
                JOptionPane.showMessageDialog(this, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_editMouseClicked

    private void editMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_editMouseEntered

    private void editMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_editMouseExited

    private void deleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseClicked
        String user_email = emailTextField.getText();

        if (user_email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an email.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            if (!isEmailDuplicate(conn, user_email)) {
                JOptionPane.showMessageDialog(this, "Email does not exist. Please check the email.", "Error", JOptionPane.ERROR_MESSAGE);
                conn.close();
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                conn.close();
                return;
            }

            String sql = "DELETE FROM user_table WHERE user_email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user_email);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
                int uid = Session.getInstance().getUid();
                logActivity(uid, "Deleted user: " + user_email);

            } else {
                JOptionPane.showMessageDialog(this, "Deletion failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteMouseClicked

    private void deleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteMouseEntered

    private void deleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteMouseExited

    private void fullNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fullNameTextFieldActionPerformed

    private void contactNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactNumberTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contactNumberTextFieldActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void userTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userTypeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userTypeComboBoxActionPerformed

    private void searchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchMouseClicked
        highlightRow();
    }//GEN-LAST:event_searchMouseClicked

    private void searchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_searchMouseEntered

    private void searchMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_searchMouseExited

    private void searchfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchfieldActionPerformed

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
        new adminViolation(user_fname, stud_id).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_violationMouseClicked

    private void violationMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseEntered
        violation.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_violationMouseEntered

    private void violationMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violationMouseExited
        violation.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_violationMouseExited

    private void recordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recordMouseEntered
        record.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_recordMouseEntered

    private void recordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recordMouseExited
        record.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_recordMouseExited

    private void usersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersMouseEntered
        users.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_usersMouseEntered

    private void usersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersMouseExited
        
    }//GEN-LAST:event_usersMouseExited

    private void logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseEntered
        logout.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_logoutMouseEntered

    private void logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseExited
        logout.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_logoutMouseExited

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

    private void recordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recordMouseClicked
        new adminRecord(user_fname, vio_id).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_recordMouseClicked

    private void settingsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseEntered
        settings.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_settingsMouseEntered

    private void settingsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseExited
        settings.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_settingsMouseExited

    private void userStatusComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userStatusComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userStatusComboBoxActionPerformed

    private void dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseClicked
        new adminDashboard(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_dashboardMouseClicked

    private void user_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_user_tableMouseClicked
         int i = user_table.getSelectedRow();
        TableModel model = user_table.getModel();

        String user_id = model.getValueAt(i, 0).toString();
        userIDtextfield.setText(user_id);

        String userFname = model.getValueAt(i, 1).toString();
        fullNameTextField.setText(userFname);
        contactNumberTextField.setText(model.getValueAt(i, 2).toString());
        emailTextField.setText(model.getValueAt(i, 3).toString());

        // Removed password field setting

        String type = model.getValueAt(i, 4).toString(); // Adjusted index
        userTypeComboBox.setSelectedItem(type);

        String status = model.getValueAt(i, 5).toString(); // Adjusted index
        userStatusComboBox.setSelectedItem(status);  

        // --- Fetch image_path separately based on user_id ---
        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            String query = "SELECT image_path FROM user_table WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, user_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String imagePath = rs.getString("image_path");
                if (imagePath != null && !imagePath.isEmpty()) {
                    File file = new File(imagePath);
                    if (file.exists()) {
                        ImageIcon icon = new ImageIcon(imagePath);
                        Image img = icon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(img));
                    } else {
                        imageLabel.setIcon(null);
                    }
                } else {
                    imageLabel.setIcon(null);
                }
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_user_tableMouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void imageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageLabelMouseClicked
        imageHandler.chooseImage(imageLabel);
    }//GEN-LAST:event_imageLabelMouseClicked

    private void displayImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_displayImageMouseClicked
        
    }//GEN-LAST:event_displayImageMouseClicked

    private void searchfieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchfieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { 
            highlightRow();
        }
    }//GEN-LAST:event_searchfieldKeyPressed

    private void studentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentMouseClicked
        new adminStudent(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_studentMouseClicked

    private void userIDtextfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userIDtextfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userIDtextfieldActionPerformed

    private void settingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseClicked
        new adminAccount(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_settingsMouseClicked

    private void user_tableMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_user_tableMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_user_tableMouseEntered

    private void imageLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageLabel1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_imageLabel1MouseClicked

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
            java.util.logging.Logger.getLogger(adminUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(adminUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(adminUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(adminUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new adminUsers().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel J_user_fname;
    private javax.swing.JLabel add;
    private javax.swing.JTextField contactNumberTextField;
    private javax.swing.JLabel dash_icon;
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel delete;
    private javax.swing.JLabel displayImage;
    private javax.swing.JLabel edit;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JTextField fullNameTextField;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel imageLabel1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel leftpanel;
    private javax.swing.JLabel log_icon;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel rec_icon;
    private javax.swing.JLabel record;
    private javax.swing.JLabel refresh;
    private javax.swing.JLabel search;
    private javax.swing.JTextField searchfield;
    private javax.swing.JLabel sett_icon;
    private javax.swing.JLabel settings;
    private javax.swing.JLabel stud_icon;
    private javax.swing.JLabel student;
    private javax.swing.JTextField userIDtextfield;
    private javax.swing.JComboBox<String> userStatusComboBox;
    private javax.swing.JComboBox<String> userTypeComboBox;
    private javax.swing.JLabel user_cnumberlabel;
    private javax.swing.JLabel user_emaillabel;
    private javax.swing.JLabel user_fnamelabel;
    private javax.swing.JLabel user_fnamelabel1;
    private javax.swing.JLabel user_statuslabel;
    private javax.swing.JTable user_table;
    private javax.swing.JLabel user_type1;
    private javax.swing.JLabel user_typelabel;
    private javax.swing.JLabel users;
    private javax.swing.JLabel users_icon;
    private javax.swing.JPanel userspanel;
    private javax.swing.JLabel vio_icon;
    private javax.swing.JLabel violation;
    // End of variables declaration//GEN-END:variables
}
