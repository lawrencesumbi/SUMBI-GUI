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
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.Icon;

/**
 *
 * @author Admin
 */
public class adminRecord extends javax.swing.JFrame {
    private String user_fname;
    private String vio_id;
    private String stud_id;
    /**
     * Creates new form adminRecord
     */
    public adminRecord() {
        initComponents(); 
        displayData();
    }
    
    public adminRecord(String user_fname, String vio_id ) {
        this.user_fname = user_fname;
        this.vio_id = vio_id;
        initComponents();
        vioID.setText(vio_id);
        displayImage(user_fname);
        J_user_fname.setText(user_fname); 
        displayData();
    }
    
    
    public void displayData() {
        try {
            dbConnector dbc = new dbConnector();

            // Only get the needed columns
            ResultSet rs = dbc.getData("SELECT rec_id, vio_id, rec_sanction, rec_comment, rec_stamp FROM rec_table");

            // Clear the input fields
            studFirstName.setText("");  
            studLastName.setText("");  
            vioName.setText("");  
            recSanction.setText("");  
            recComment.setText("");  
            recStamp.setText(""); 
            searchfield.setText(""); 

            // Set the table model
            rec_table.setModel(DbUtils.resultSetToTableModel(rs));

            // Set custom column headers
            String[] columnNames = {"Record ID", "Violation ID", "Sanction", "Comment", "Time Stamp"};
            for (int i = 0; i < columnNames.length; i++) {
                rec_table.getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);
            }

            // Force JTable to repaint headers
            rec_table.getTableHeader().repaint();

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
                String folderPath = "src/violationImages";
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

        leftpanel = new javax.swing.JPanel();
        displayImage = new javax.swing.JLabel();
        J_user_fname = new javax.swing.JLabel();
        user_type1 = new javax.swing.JLabel();
        dashboard = new javax.swing.JLabel();
        dash_icon = new javax.swing.JLabel();
        stud_icon = new javax.swing.JLabel();
        student = new javax.swing.JLabel();
        vio_icon = new javax.swing.JLabel();
        violation = new javax.swing.JLabel();
        rec_icon = new javax.swing.JLabel();
        record = new javax.swing.JLabel();
        users_icon = new javax.swing.JLabel();
        users = new javax.swing.JLabel();
        sett_icon = new javax.swing.JLabel();
        settings = new javax.swing.JLabel();
        log_icon = new javax.swing.JLabel();
        logout = new javax.swing.JLabel();
        violationpanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        rec_table = new javax.swing.JTable();
        add = new javax.swing.JLabel();
        edit = new javax.swing.JLabel();
        delete = new javax.swing.JLabel();
        refresh = new javax.swing.JLabel();
        searchfield = new javax.swing.JTextField();
        search = new javax.swing.JLabel();
        user_fnamelabel = new javax.swing.JLabel();
        studFirstName = new javax.swing.JTextField();
        vioName = new javax.swing.JTextField();
        user_passwordlabel = new javax.swing.JLabel();
        imageLabel = new javax.swing.JLabel();
        recSanction = new javax.swing.JTextField();
        user_fnamelabel1 = new javax.swing.JLabel();
        recIDtextfield = new javax.swing.JTextField();
        user_emaillabel1 = new javax.swing.JLabel();
        user_emaillabel2 = new javax.swing.JLabel();
        recComment = new javax.swing.JTextField();
        recStamp = new javax.swing.JTextField();
        studLastName = new javax.swing.JTextField();
        vioID = new javax.swing.JTextField();
        imageLabel1 = new javax.swing.JLabel();
        printPrev = new javax.swing.JLabel();
        user_passwordlabel1 = new javax.swing.JLabel();
        user_fnamelabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        leftpanel.setBackground(new java.awt.Color(0, 0, 0));
        leftpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        J_user_fname.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        J_user_fname.setForeground(new java.awt.Color(255, 255, 255));
        J_user_fname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        J_user_fname.setText("Fullname");
        leftpanel.add(J_user_fname, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 170, 30));

        user_type1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        user_type1.setForeground(new java.awt.Color(255, 255, 255));
        user_type1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_type1.setText("Admin");
        leftpanel.add(user_type1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 70, -1));

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

        dash_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-dashboard-layout-24.png"))); // NOI18N
        dash_icon.setText("jLabel1");
        leftpanel.add(dash_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 30, 30));

        stud_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-student-male-24.png"))); // NOI18N
        stud_icon.setText("jLabel1");
        leftpanel.add(stud_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 30, 30));

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

        vio_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-foul-30.png"))); // NOI18N
        vio_icon.setText("jLabel1");
        leftpanel.add(vio_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 30, 30));

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

        rec_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-record-50.png"))); // NOI18N
        rec_icon.setText("jLabel1");
        leftpanel.add(rec_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 30, 30));

        record.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        record.setForeground(new java.awt.Color(255, 255, 0));
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

        users_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-user-icon-30.png"))); // NOI18N
        users_icon.setText("jLabel1");
        leftpanel.add(users_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 30, 30));

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

        sett_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-settings-50.png"))); // NOI18N
        sett_icon.setText("jLabel1");
        leftpanel.add(sett_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, 30, 30));

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

        log_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-open-pane-24.png"))); // NOI18N
        log_icon.setText("jLabel1");
        leftpanel.add(log_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 30, 30));

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

        getContentPane().add(leftpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 600));

        violationpanel.setBackground(new java.awt.Color(204, 0, 0));
        violationpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBackground(new java.awt.Color(204, 0, 0));
        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        rec_table.setModel(new javax.swing.table.DefaultTableModel(
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
        rec_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rec_tableMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                rec_tableMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(rec_table);

        violationpanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 710, 260));

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
        violationpanel.add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 60, 30));

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
        violationpanel.add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 60, 30));

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
        violationpanel.add(delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, 60, 30));

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
        violationpanel.add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 300, 120, 30));

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
        violationpanel.add(searchfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 300, 150, 30));

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
        violationpanel.add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 300, 70, 30));

        user_fnamelabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_fnamelabel.setForeground(new java.awt.Color(255, 255, 255));
        user_fnamelabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_fnamelabel.setText("RECORD DETAILS:");
        violationpanel.add(user_fnamelabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 140, 20));

        studFirstName.setEditable(false);
        studFirstName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        studFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studFirstNameActionPerformed(evt);
            }
        });
        violationpanel.add(studFirstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, 110, -1));

        vioName.setEditable(false);
        vioName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        vioName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vioNameActionPerformed(evt);
            }
        });
        violationpanel.add(vioName, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 200, -1));

        user_passwordlabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_passwordlabel.setForeground(new java.awt.Color(255, 255, 255));
        user_passwordlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_passwordlabel.setText("Student Information:");
        violationpanel.add(user_passwordlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 140, 30));

        imageLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        imageLabel.setForeground(new java.awt.Color(255, 255, 255));
        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageLabelMouseClicked(evt);
            }
        });
        violationpanel.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 190, 190));

        recSanction.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        recSanction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recSanctionActionPerformed(evt);
            }
        });
        violationpanel.add(recSanction, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 160, 200, -1));

        user_fnamelabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_fnamelabel1.setForeground(new java.awt.Color(255, 255, 255));
        user_fnamelabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_fnamelabel1.setText("Record ID");
        violationpanel.add(user_fnamelabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 90, 30));

        recIDtextfield.setEditable(false);
        recIDtextfield.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        recIDtextfield.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        recIDtextfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recIDtextfieldActionPerformed(evt);
            }
        });
        violationpanel.add(recIDtextfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, 40, -1));

        user_emaillabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_emaillabel1.setForeground(new java.awt.Color(255, 255, 255));
        user_emaillabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_emaillabel1.setText("Reaction / Suggestion");
        violationpanel.add(user_emaillabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 180, 20));

        user_emaillabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_emaillabel2.setForeground(new java.awt.Color(255, 255, 255));
        user_emaillabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_emaillabel2.setText("Time Stamp");
        violationpanel.add(user_emaillabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 240, 100, 20));

        recComment.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        recComment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recCommentActionPerformed(evt);
            }
        });
        violationpanel.add(recComment, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 200, 200, -1));

        recStamp.setEditable(false);
        recStamp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        recStamp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recStampActionPerformed(evt);
            }
        });
        violationpanel.add(recStamp, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, 200, -1));

        studLastName.setEditable(false);
        studLastName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        studLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studLastNameActionPerformed(evt);
            }
        });
        violationpanel.add(studLastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 70, 90, -1));

        vioID.setEditable(false);
        vioID.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        vioID.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        vioID.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                vioIDComponentAdded(evt);
            }
        });
        vioID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                vioIDFocusLost(evt);
            }
        });
        vioID.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                vioIDInputMethodTextChanged(evt);
            }
        });
        vioID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vioIDActionPerformed(evt);
            }
        });
        vioID.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                vioIDPropertyChange(evt);
            }
        });
        vioID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                vioIDKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                vioIDKeyTyped(evt);
            }
        });
        violationpanel.add(vioID, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 40, -1));

        imageLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        imageLabel1.setForeground(new java.awt.Color(255, 255, 255));
        imageLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/image-removebg-preview (18).png"))); // NOI18N
        imageLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageLabel1MouseClicked(evt);
            }
        });
        violationpanel.add(imageLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 190, 190));

        printPrev.setBackground(new java.awt.Color(255, 255, 255));
        printPrev.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        printPrev.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        printPrev.setText("PRINTPREV");
        printPrev.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        printPrev.setOpaque(true);
        printPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printPrevMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                printPrevMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                printPrevMouseExited(evt);
            }
        });
        violationpanel.add(printPrev, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 300, 90, 30));

        user_passwordlabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_passwordlabel1.setForeground(new java.awt.Color(255, 255, 255));
        user_passwordlabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_passwordlabel1.setText("Sanction / Action");
        violationpanel.add(user_passwordlabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 140, 20));

        user_fnamelabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_fnamelabel2.setForeground(new java.awt.Color(255, 255, 255));
        user_fnamelabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_fnamelabel2.setText("Violation Information:");
        violationpanel.add(user_fnamelabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 160, 30));

        getContentPane().add(violationpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 710, 600));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void displayImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_displayImageMouseClicked

    }//GEN-LAST:event_displayImageMouseClicked

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

    private void recordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recordMouseClicked

    }//GEN-LAST:event_recordMouseClicked

    private void recordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recordMouseEntered
        record.setForeground(new java.awt.Color(255, 255, 0));
    }//GEN-LAST:event_recordMouseEntered

    private void recordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recordMouseExited
        
    }//GEN-LAST:event_recordMouseExited

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

    private void settingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseClicked
        new adminAccount(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_settingsMouseClicked

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

    private void rec_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rec_tableMouseClicked
        int i = rec_table.getSelectedRow();
        TableModel model = rec_table.getModel();

        // Get rec_id and vio_id from the selected row
        String rec_id = model.getValueAt(i, 0).toString();
        String vio_id = model.getValueAt(i, 1).toString();

        recIDtextfield.setText(rec_id);
        vioID.setText(vio_id);

        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            // Query to fetch student and violation details
            String query = "SELECT s.stud_fname, s.stud_lname, v.vio_name " +
                           "FROM rec_table r " +
                           "JOIN vio_table v ON r.vio_id = v.vio_id " +
                           "JOIN stud_table s ON v.stud_id = s.stud_id " +
                           "WHERE r.rec_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, rec_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                studFirstName.setText(rs.getString("stud_fname"));
                studLastName.setText(rs.getString("stud_lname"));
                vioName.setText(rs.getString("vio_name"));
            } else {
                studFirstName.setText("");
                studLastName.setText("");
                vioName.setText("");
            }

            rs.close();
            pstmt.close();

            // NEW: Query to fetch image path using vio_id
            String imgQuery = "SELECT image_path FROM vio_table WHERE vio_id = ?";
            PreparedStatement imgStmt = conn.prepareStatement(imgQuery);
            imgStmt.setString(1, vio_id);
            ResultSet imgRs = imgStmt.executeQuery();

            if (imgRs.next()) {
                String imagePath = imgRs.getString("image_path");
                if (imagePath != null && !imagePath.isEmpty()) {
                    File file = new File(imagePath);
                    if (file.exists()) {
                        ImageIcon icon = new ImageIcon(imagePath);
                        Image img = icon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(img));
                    } else {
                        imageLabel.setIcon(null); // File not found
                    }
                } else {
                    imageLabel.setIcon(null); // No image path in DB
                }
            }

            imgRs.close();
            imgStmt.close();
            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Set other details from table model
        recSanction.setText(model.getValueAt(i, 2).toString()); // vio_des
        recComment.setText(model.getValueAt(i, 3).toString());  // vio_sev
        recStamp.setText(model.getValueAt(i, 4).toString());    // vio_stampA
    }//GEN-LAST:event_rec_tableMouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseClicked
        String vio_id = vioID.getText();        
        String rec_sanction = recSanction.getText();
        String rec_comment = recComment.getText();
        String imagePath = null;       
        String vio_status = "Recorded";

        if (imageLabel.getIcon() != null) {
            imagePath = saveImageToFolder(vio_id);
        }

        LocalDateTime currDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yy/MM/dd hh:mm a");
        String rec_stamp = currDateTime.format(format);

        if (vio_id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Violation ID doesn't exist!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            // Insert into rec_table first
            String sql;
            PreparedStatement pstmt;

            if (imagePath != null) {
                sql = "INSERT INTO rec_table (vio_id, rec_sanction, rec_comment, rec_stamp, image_path) VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, vio_id);
                pstmt.setString(2, rec_sanction);
                pstmt.setString(3, rec_comment);
                pstmt.setString(4, rec_stamp);
                pstmt.setString(5, imagePath);
            } else {
                sql = "INSERT INTO rec_table (vio_id, rec_sanction, rec_comment, rec_stamp) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, vio_id);
                pstmt.setString(2, rec_sanction);
                pstmt.setString(3, rec_comment);
                pstmt.setString(4, rec_stamp);
            }

            int rowsInserted = pstmt.executeUpdate();
            pstmt.close();

            if (rowsInserted > 0) {
                // Now update the vio_table to set vio_status to 'Recorded'
                String updateSql = "UPDATE vio_table SET vio_status = ? WHERE vio_id = ?";
                PreparedStatement updatePstmt = conn.prepareStatement(updateSql);
                updatePstmt.setString(1, vio_status);
                updatePstmt.setString(2, vio_id);
                updatePstmt.executeUpdate();
                updatePstmt.close();

                JOptionPane.showMessageDialog(this, "Student Violation Successfully Recorded!");
               
                int uid = Session.getInstance().getUid(); 
                logActivity(uid, "Added New Record");

            }

            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_addMouseClicked

    private void addMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_addMouseEntered

    private void addMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_addMouseExited

    private void editMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseClicked
        // Get the rec_id, rec_sanction, and rec_comment from text fields
        String rec_id_text = recIDtextfield.getText();  // Assuming recID is the text field for rec_id
        String rec_sanction = recSanction.getText();
        String rec_comment = recComment.getText();

        // Check if rec_id is empty or invalid
        if (rec_id_text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Record ID is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int rec_id;
        try {
            rec_id = Integer.parseInt(rec_id_text);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Record ID. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database connection setup
        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            // SQL query to update the rec_sanction and rec_comment for the given rec_id
            String sql = "UPDATE rec_table SET rec_sanction = ?, rec_comment = ? WHERE rec_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, rec_sanction);
            pstmt.setString(2, rec_comment);
            pstmt.setInt(3, rec_id);

            int rowsUpdated = pstmt.executeUpdate();
            pstmt.close();

            if (rowsUpdated > 0) {
                // Successfully updated the record
                JOptionPane.showMessageDialog(this, "Record Updated Successfully!");

                int uid = Session.getInstance().getUid(); // Log activity
                logActivity(uid, "Updated Record for Record ID: " + rec_id);

            } else {
                // No record found for the given rec_id
                JOptionPane.showMessageDialog(this, "No record found with Record ID: " + rec_id, "Error", JOptionPane.ERROR_MESSAGE);
            }

            conn.close();
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
        String rec_id = recIDtextfield.getText(); // Assuming there's a text field for user_id

        if (rec_id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a User ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                conn.close();
                return;
            }

            String sql = "DELETE FROM rec_table WHERE rec_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, rec_id);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Record deleted successfully!");
                int uid = Session.getInstance().getUid();
                logActivity(uid, "Deleted student: " + rec_id );

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

    private void refreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseClicked
        displayData();
        studFirstName.setText("");
        studLastName.setText("");
        vioName.setText("");
        recSanction.setText("");
        recComment.setText("");
        recStamp.setText("");
        imageLabel.setIcon(null);
        searchfield.setText("");
        recIDtextfield.setText("");
        vioID.setText("");
    }//GEN-LAST:event_refreshMouseClicked

    private void refreshMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseEntered

    }//GEN-LAST:event_refreshMouseEntered

    private void refreshMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseExited

    }//GEN-LAST:event_refreshMouseExited

    private void searchfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchfieldActionPerformed

    private void searchfieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchfieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            highlightRow();
        }
    }//GEN-LAST:event_searchfieldKeyPressed

    private void searchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchMouseClicked
        highlightRow();
    }//GEN-LAST:event_searchMouseClicked

    private void searchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_searchMouseEntered

    private void searchMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_searchMouseExited

    private void studFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studFirstNameActionPerformed

    private void vioNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vioNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vioNameActionPerformed

    private void recSanctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recSanctionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_recSanctionActionPerformed

    private void recIDtextfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recIDtextfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_recIDtextfieldActionPerformed

    private void recCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recCommentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_recCommentActionPerformed

    private void recStampActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recStampActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_recStampActionPerformed

    private void studLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studLastNameActionPerformed

    private void vioIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vioIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vioIDActionPerformed

    private void vioIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vioIDKeyReleased
        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        String vio_id = vioID.getText().trim(); // Get the entered ID

        if (vio_id.isEmpty()) {
            studFirstName.setText(""); // Clear fields if empty
            studLastName.setText("");
            vioName.setText("");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            // Correct SQL query with JOIN to fetch both student name and violation name
            String sql = "SELECT s.stud_fname, s.stud_lname, v.vio_name FROM vio_table v " +
                         "JOIN stud_table s ON v.stud_id = s.stud_id WHERE v.vio_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, vio_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                studFirstName.setText(rs.getString("stud_fname"));
                studLastName.setText(rs.getString("stud_lname"));
                vioName.setText(rs.getString("vio_name"));
            } else {
                studFirstName.setText(""); // Clear fields if no match
                studLastName.setText("");
                vioName.setText("");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_vioIDKeyReleased

    private void vioIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vioIDKeyTyped

    }//GEN-LAST:event_vioIDKeyTyped

    private void imageLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageLabel1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_imageLabel1MouseClicked

    private void printPrevMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printPrevMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_printPrevMouseExited

    private void printPrevMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printPrevMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_printPrevMouseEntered

    private void printPrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printPrevMouseClicked
// Get rec_id from text field
    String recIdText = recIDtextfield.getText().trim();

    if (recIdText.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please enter a Record ID!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int rec_id;
    try {
        rec_id = Integer.parseInt(recIdText);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid Record ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String url = "jdbc:mysql://localhost:3306/sumbi_db";
    String user = "root";
    String pass = "";

    try (Connection conn = DriverManager.getConnection(url, user, pass)) {

        String query = "SELECT s.stud_fname, s.stud_lname, s.stud_program, s.stud_section, s.stud_address, s.stud_cnumber, " +
                       "v.vio_name, v.vio_des, v.vio_sev, v.vio_stamp, " +
                       "r.rec_sanction, r.rec_comment, r.rec_stamp " +
                       "FROM rec_table r " +
                       "JOIN vio_table v ON r.vio_id = v.vio_id " +
                       "JOIN stud_table s ON v.stud_id = s.stud_id " +
                       "WHERE r.rec_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, rec_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Student info
                    String fullName = rs.getString("stud_fname") + " " + rs.getString("stud_lname");
                    String program = rs.getString("stud_program");
                    String section = rs.getString("stud_section");
                    String address = rs.getString("stud_address");
                    String contact = rs.getString("stud_cnumber");

                    // Violation info
                    String vioName = rs.getString("vio_name");
                    String vioDes = rs.getString("vio_des");
                    String vioSev = rs.getString("vio_sev");
                    String vioStamp = rs.getString("vio_stamp");

                    // Record info
                    String recSanction = rs.getString("rec_sanction");
                    String recComment = rs.getString("rec_comment");
                    String recStamp = rs.getString("rec_stamp");

                    // Show report window
                    new adminPrintPreview(
                        fullName, program, section, address, contact,
                        vioName, vioDes, vioSev, vioStamp,
                        recSanction, recComment, recStamp
                    ).setVisible(true);

                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "No data found for Record ID: " + rec_id);
                }
            }
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }                
    }//GEN-LAST:event_printPrevMouseClicked

    private void rec_tableMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rec_tableMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_rec_tableMouseEntered

    private void imageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageLabelMouseClicked

    }//GEN-LAST:event_imageLabelMouseClicked

    private void vioIDInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_vioIDInputMethodTextChanged
        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        String vio_id = vioID.getText().trim(); // Get the entered ID

        if (vio_id.isEmpty()) {
            studFirstName.setText(""); // Clear fields if empty
            studLastName.setText("");
            vioName.setText("");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            // Correct SQL query with JOIN to fetch both student name and violation name
            String sql = "SELECT s.stud_fname, s.stud_lname, v.vio_name FROM vio_table v " +
                         "JOIN stud_table s ON v.stud_id = s.stud_id WHERE v.vio_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, vio_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                studFirstName.setText(rs.getString("stud_fname"));
                studLastName.setText(rs.getString("stud_lname"));
                vioName.setText(rs.getString("vio_name"));
            } else {
                studFirstName.setText(""); // Clear fields if no match
                studLastName.setText("");
                vioName.setText("");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_vioIDInputMethodTextChanged

    private void vioIDComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_vioIDComponentAdded
        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        String vio_id = vioID.getText().trim(); // Get the entered ID

        if (vio_id.isEmpty()) {
            studFirstName.setText(""); // Clear fields if empty
            studLastName.setText("");
            vioName.setText("");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            // Correct SQL query with JOIN to fetch both student name and violation name
            String sql = "SELECT s.stud_fname, s.stud_lname, v.vio_name FROM vio_table v " +
                         "JOIN stud_table s ON v.stud_id = s.stud_id WHERE v.vio_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, vio_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                studFirstName.setText(rs.getString("stud_fname"));
                studLastName.setText(rs.getString("stud_lname"));
                vioName.setText(rs.getString("vio_name"));
            } else {
                studFirstName.setText(""); // Clear fields if no match
                studLastName.setText("");
                vioName.setText("");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_vioIDComponentAdded

    private void vioIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vioIDFocusLost
        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        String vio_id = vioID.getText().trim(); // Get the entered ID

        if (vio_id.isEmpty()) {
            studFirstName.setText(""); // Clear fields if empty
            studLastName.setText("");
            vioName.setText("");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            // Correct SQL query with JOIN to fetch both student name and violation name
            String sql = "SELECT s.stud_fname, s.stud_lname, v.vio_name FROM vio_table v " +
                         "JOIN stud_table s ON v.stud_id = s.stud_id WHERE v.vio_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, vio_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                studFirstName.setText(rs.getString("stud_fname"));
                studLastName.setText(rs.getString("stud_lname"));
                vioName.setText(rs.getString("vio_name"));
            } else {
                studFirstName.setText(""); // Clear fields if no match
                studLastName.setText("");
                vioName.setText("");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_vioIDFocusLost

    private void vioIDPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_vioIDPropertyChange
        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        String vio_id = vioID.getText().trim(); // Get the entered ID

        if (vio_id.isEmpty()) {
            studFirstName.setText(""); // Clear fields if empty
            studLastName.setText("");
            vioName.setText("");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            // Correct SQL query with JOIN to fetch both student name and violation name
            String sql = "SELECT s.stud_fname, s.stud_lname, v.vio_name FROM vio_table v " +
                         "JOIN stud_table s ON v.stud_id = s.stud_id WHERE v.vio_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, vio_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                studFirstName.setText(rs.getString("stud_fname"));
                studLastName.setText(rs.getString("stud_lname"));
                vioName.setText(rs.getString("vio_name"));
            } else {
                studFirstName.setText(""); // Clear fields if no match
                studLastName.setText("");
                vioName.setText("");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_vioIDPropertyChange

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
            java.util.logging.Logger.getLogger(adminRecord.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(adminRecord.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(adminRecord.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(adminRecord.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new adminRecord().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel J_user_fname;
    private javax.swing.JLabel add;
    private javax.swing.JLabel dash_icon;
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel delete;
    private javax.swing.JLabel displayImage;
    private javax.swing.JLabel edit;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel imageLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel leftpanel;
    private javax.swing.JLabel log_icon;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel printPrev;
    private javax.swing.JTextField recComment;
    private javax.swing.JTextField recIDtextfield;
    private javax.swing.JTextField recSanction;
    private javax.swing.JTextField recStamp;
    private javax.swing.JLabel rec_icon;
    private javax.swing.JTable rec_table;
    private javax.swing.JLabel record;
    private javax.swing.JLabel refresh;
    private javax.swing.JLabel search;
    private javax.swing.JTextField searchfield;
    private javax.swing.JLabel sett_icon;
    private javax.swing.JLabel settings;
    private javax.swing.JTextField studFirstName;
    private javax.swing.JTextField studLastName;
    private javax.swing.JLabel stud_icon;
    private javax.swing.JLabel student;
    private javax.swing.JLabel user_emaillabel1;
    private javax.swing.JLabel user_emaillabel2;
    private javax.swing.JLabel user_fnamelabel;
    private javax.swing.JLabel user_fnamelabel1;
    private javax.swing.JLabel user_fnamelabel2;
    private javax.swing.JLabel user_passwordlabel;
    private javax.swing.JLabel user_passwordlabel1;
    private javax.swing.JLabel user_type1;
    private javax.swing.JLabel users;
    private javax.swing.JLabel users_icon;
    private javax.swing.JTextField vioID;
    private javax.swing.JTextField vioName;
    private javax.swing.JLabel vio_icon;
    private javax.swing.JLabel violation;
    private javax.swing.JPanel violationpanel;
    // End of variables declaration//GEN-END:variables
}
