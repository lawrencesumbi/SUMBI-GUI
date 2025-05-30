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
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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
public class userStudent extends javax.swing.JFrame {
    private String user_fname;
    private String vio_id;
    private String stud_id;

    /**
     * Creates new form adminStudent
     */
    public userStudent() {
        initComponents(); 
        displayData();
    }

    public userStudent(String user_fname) {
        this.user_fname = user_fname;
        initComponents();
        displayImage(user_fname);
        J_user_fname.setText(user_fname); 
        displayData();
    }
    
    public void displayData() {
        try {
            dbConnector dbc = new dbConnector();
            ResultSet rs = dbc.getData("SELECT stud_id, stud_fname, stud_lname, stud_program, stud_section, stud_address, stud_cnumber FROM stud_table");

            studFirstName.setText("");  
            studLastName.setText("");  
            studProgram.setText("");  
            studSection.setText("");  
            studAddress.setText("");  
            studCNumber.setText(""); 
            searchfield.setText(""); 

            stud_table.setModel(DbUtils.resultSetToTableModel(rs));   
            
            String[] columnNames = {"Student ID", "First Name", "Last Name", "Program", "Section", "Address", "Contact Number"};
            for (int col = 0; col < columnNames.length; col++) {
                stud_table.getColumnModel().getColumn(col).setHeaderValue(columnNames[col]);
            }
            stud_table.getTableHeader().repaint();
            
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
                String folderPath = "src/studentImages";
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

        stud_table.clearSelection();

        boolean matchFound = false;

        for (int i = 0; i < stud_table.getRowCount(); i++) {
            for (int j = 1; j <= 6; j++) {
                Object cellValue = stud_table.getValueAt(i, j);

                if (cellValue != null) {
                    String cellText = cellValue.toString().trim().toLowerCase();

                    if (cellText.contains(searchText)) { 
                        stud_table.addRowSelectionInterval(i, i);
                        stud_table.scrollRectToVisible(stud_table.getCellRect(i, 0, true));
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
        studentpanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        stud_table = new javax.swing.JTable();
        add = new javax.swing.JLabel();
        edit = new javax.swing.JLabel();
        delete = new javax.swing.JLabel();
        refresh = new javax.swing.JLabel();
        searchfield = new javax.swing.JTextField();
        search = new javax.swing.JLabel();
        user_fnamelabel = new javax.swing.JLabel();
        studFirstName = new javax.swing.JTextField();
        user_cnumberlabel = new javax.swing.JLabel();
        studLastName = new javax.swing.JTextField();
        user_emaillabel = new javax.swing.JLabel();
        studProgram = new javax.swing.JTextField();
        user_passwordlabel = new javax.swing.JLabel();
        user_passwordlabel1 = new javax.swing.JLabel();
        user_passwordlabel2 = new javax.swing.JLabel();
        studSection = new javax.swing.JTextField();
        studAddress = new javax.swing.JTextField();
        studCNumber = new javax.swing.JTextField();
        user_fnamelabel1 = new javax.swing.JLabel();
        studIDtextfield = new javax.swing.JTextField();
        imageLabel = new javax.swing.JLabel();
        imageLabel1 = new javax.swing.JLabel();
        violate = new javax.swing.JLabel();

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
        student.setForeground(new java.awt.Color(255, 255, 0));
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

        studentpanel.setBackground(new java.awt.Color(204, 0, 0));
        studentpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBackground(new java.awt.Color(204, 0, 0));
        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        stud_table.setModel(new javax.swing.table.DefaultTableModel(
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
        stud_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stud_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(stud_table);

        studentpanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 710, 300));

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
        studentpanel.add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 60, 30));

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
        studentpanel.add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, 60, 30));

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
        studentpanel.add(delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 60, 30));

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
        studentpanel.add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 250, 120, 30));

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
        studentpanel.add(searchfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 250, 150, 30));

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
        studentpanel.add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 250, 70, 30));

        user_fnamelabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_fnamelabel.setForeground(new java.awt.Color(255, 255, 255));
        user_fnamelabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_fnamelabel.setText("Student First Name");
        studentpanel.add(user_fnamelabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 130, 20));

        studFirstName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        studFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studFirstNameActionPerformed(evt);
            }
        });
        studentpanel.add(studFirstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 190, -1));

        user_cnumberlabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_cnumberlabel.setForeground(new java.awt.Color(255, 255, 255));
        user_cnumberlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_cnumberlabel.setText("Student Last Name");
        studentpanel.add(user_cnumberlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 130, 20));

        studLastName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        studLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studLastNameActionPerformed(evt);
            }
        });
        studentpanel.add(studLastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 190, -1));

        user_emaillabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_emaillabel.setForeground(new java.awt.Color(255, 255, 255));
        user_emaillabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_emaillabel.setText("Student Program");
        studentpanel.add(user_emaillabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 150, 20));

        studProgram.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        studProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studProgramActionPerformed(evt);
            }
        });
        studentpanel.add(studProgram, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 190, -1));

        user_passwordlabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_passwordlabel.setForeground(new java.awt.Color(255, 255, 255));
        user_passwordlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_passwordlabel.setText("Student Section");
        studentpanel.add(user_passwordlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, 150, 20));

        user_passwordlabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_passwordlabel1.setForeground(new java.awt.Color(255, 255, 255));
        user_passwordlabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_passwordlabel1.setText("Student Address");
        studentpanel.add(user_passwordlabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 130, 20));

        user_passwordlabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_passwordlabel2.setForeground(new java.awt.Color(255, 255, 255));
        user_passwordlabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_passwordlabel2.setText("Student Contact Number");
        studentpanel.add(user_passwordlabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, 190, 20));

        studSection.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        studSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studSectionActionPerformed(evt);
            }
        });
        studentpanel.add(studSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 120, 190, -1));

        studAddress.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        studAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studAddressActionPerformed(evt);
            }
        });
        studentpanel.add(studAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 190, -1));

        studCNumber.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        studCNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studCNumberActionPerformed(evt);
            }
        });
        studentpanel.add(studCNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 190, 190, -1));

        user_fnamelabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        user_fnamelabel1.setForeground(new java.awt.Color(255, 255, 255));
        user_fnamelabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_fnamelabel1.setText("Student ID");
        studentpanel.add(user_fnamelabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 70, 30));

        studIDtextfield.setEditable(false);
        studIDtextfield.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        studIDtextfield.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        studIDtextfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studIDtextfieldActionPerformed(evt);
            }
        });
        studentpanel.add(studIDtextfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 40, -1));

        imageLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        imageLabel.setForeground(new java.awt.Color(255, 255, 255));
        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageLabelMouseClicked(evt);
            }
        });
        studentpanel.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, 150, 150));

        imageLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        imageLabel1.setForeground(new java.awt.Color(255, 255, 255));
        imageLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/image-removebg-preview1.png"))); // NOI18N
        imageLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageLabel1MouseClicked(evt);
            }
        });
        studentpanel.add(imageLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, 150, 150));

        violate.setBackground(new java.awt.Color(255, 255, 255));
        violate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        violate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        violate.setText("VIOLATE");
        violate.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        violate.setOpaque(true);
        violate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                violateMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                violateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                violateMouseExited(evt);
            }
        });
        studentpanel.add(violate, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 250, 90, 30));

        getContentPane().add(studentpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 710, 600));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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

    private void dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseClicked
        new userDashboard(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_dashboardMouseClicked

    private void settingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseClicked
        new userAccount(user_fname).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_settingsMouseClicked

    private void stud_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stud_tableMouseClicked
        int i = stud_table.getSelectedRow();
        TableModel model = stud_table.getModel();

        String studID = model.getValueAt(i, 0).toString();
        studIDtextfield.setText(studID);
        studFirstName.setText(model.getValueAt(i, 1).toString());
        studLastName.setText(model.getValueAt(i, 2).toString());
        studProgram.setText(model.getValueAt(i, 3).toString());
        studSection.setText(model.getValueAt(i, 4).toString());
        studAddress.setText(model.getValueAt(i, 5).toString());
        studCNumber.setText(model.getValueAt(i, 6).toString());

        // Fetch image_path separately based on stud_id
        try {
            dbConnector dbc = new dbConnector();
            ResultSet rs = dbc.getData("SELECT image_path FROM stud_table WHERE stud_id = '" + studID + "'");
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
        } catch (SQLException ex) {
            System.out.println("Error fetching image_path: " + ex.getMessage());
            imageLabel.setIcon(null);
        }
    }//GEN-LAST:event_stud_tableMouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        int i = stud_table.getSelectedRow();
        TableModel model = stud_table.getModel();

        studIDtextfield.setText(model.getValueAt(i, 0).toString());

        String stud_fname = model.getValueAt(i, 1).toString();
        studFirstName.setText(stud_fname);
        studLastName.setText(model.getValueAt(i, 2).toString());
        studProgram.setText(model.getValueAt(i, 3).toString());
        studSection.setText(model.getValueAt(i, 4).toString());
        studAddress.setText(model.getValueAt(i, 5).toString());
        studCNumber.setText(model.getValueAt(i, 6).toString());

        Object imagePathObj = model.getValueAt(i, 7);
        String imagePath = (imagePathObj != null) ? imagePathObj.toString() : ""; // Avoid NullPointerException

        if (!imagePath.isEmpty()) {
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
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseClicked
        String stud_fname = studFirstName.getText();
        String stud_lname = studLastName.getText();
        String stud_program = studProgram.getText();
        String stud_section = studSection.getText();
        String stud_address = studAddress.getText();
        String stud_cnumber = studCNumber.getText();

        if (stud_fname.isEmpty() || stud_lname.isEmpty() || stud_cnumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!stud_cnumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Contact number must be in digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        // Get the current user's user_id from Session
        int user_id = Session.getInstance().getUid();

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            String sql = "INSERT INTO stud_table (stud_fname, stud_lname, stud_program, stud_section, stud_address, stud_cnumber, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, stud_fname);
            pstmt.setString(2, stud_lname);
            pstmt.setString(3, stud_program);
            pstmt.setString(4, stud_section);
            pstmt.setString(5, stud_address);
            pstmt.setString(6, stud_cnumber);
            pstmt.setInt(7, user_id);  // Add user_id to the query

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Student Added Successfully!");
                int uid = Session.getInstance().getUid();
                logActivity(uid, "Added student: " + stud_fname + " " + stud_lname);
            }

            pstmt.close();
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
        String stud_fname = studFirstName.getText();
        String stud_lname = studLastName.getText();
        String stud_program = studProgram.getText();
        String stud_section = studSection.getText();
        String stud_address = studAddress.getText();
        String stud_cnumber = studCNumber.getText();

        if (stud_fname.isEmpty() || stud_lname.isEmpty() || stud_cnumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!stud_cnumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Contact number must be in digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this student?", "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        // Get user_id from Session
        int user_id = Session.getInstance().getUid();

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql;

            if (imageLabel.getIcon() == null) {
                // No image: Set image_path to NULL in database
                sql = "UPDATE stud_table SET stud_lname = ?, stud_program = ?, stud_section = ?, stud_address = ?, stud_cnumber = ?, image_path = NULL, user_id = ? WHERE stud_fname = ?";
            } else {
                // Image is present: Update image_path
                sql = "UPDATE stud_table SET stud_lname = ?, stud_program = ?, stud_section = ?, stud_address = ?, stud_cnumber = ?, image_path = ?, user_id = ? WHERE stud_fname = ?";
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, stud_lname);
                pstmt.setString(2, stud_program);
                pstmt.setString(3, stud_section);
                pstmt.setString(4, stud_address);
                pstmt.setString(5, stud_cnumber);

                if (imageLabel.getIcon() != null) {
                    String imagePath = saveImageToFolder(stud_fname + "_" + stud_lname); // customize filename
                    pstmt.setString(6, imagePath);
                    pstmt.setInt(7, user_id);
                    pstmt.setString(8, stud_fname);
                } else {
                    pstmt.setInt(6, user_id);
                    pstmt.setString(7, stud_fname);
                }

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Student information updated successfully!");
                    logActivity(user_id, "Updated Student: " + stud_fname + " " + stud_lname);
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
        String stud_fname = studFirstName.getText();

        if (stud_fname.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a First Name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/sumbi_db";
        String user = "root";
        String pass = "";

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM stud_table WHERE stud_fname = ?"; // Updated table and column name if needed

        try (Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, stud_fname);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "User deleted successfully!");
                int uid = Session.getInstance().getUid();
                logActivity(uid, "Deleted user: " + stud_fname);

            } else {
                JOptionPane.showMessageDialog(null, "Deletion failed. User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        studProgram.setText("");
        studSection.setText("");
        studAddress.setText("");
        studCNumber.setText("");
        imageLabel.setIcon(null);
        searchfield.setText("");
        studIDtextfield.setText("");
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

    private void studLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studLastNameActionPerformed

    private void studProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studProgramActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studProgramActionPerformed

    private void studSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studSectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studSectionActionPerformed

    private void studAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studAddressActionPerformed

    private void studCNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studCNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studCNumberActionPerformed

    private void studIDtextfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studIDtextfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studIDtextfieldActionPerformed

    private void imageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageLabelMouseClicked
        imageHandler.chooseStudImage(imageLabel);
    }//GEN-LAST:event_imageLabelMouseClicked

    private void imageLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageLabel1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_imageLabel1MouseClicked

    private void violateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violateMouseClicked
        String stud_id = studIDtextfield.getText();

        userViolation violationWindow = new userViolation(user_fname, stud_id);
        violationWindow.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_violateMouseClicked

    private void violateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violateMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_violateMouseEntered

    private void violateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_violateMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_violateMouseExited

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
            java.util.logging.Logger.getLogger(userStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(userStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(userStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(userStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new userStudent().setVisible(true);
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
    private javax.swing.JLabel refresh;
    private javax.swing.JLabel search;
    private javax.swing.JTextField searchfield;
    private javax.swing.JLabel sett_icon;
    private javax.swing.JLabel settings;
    private javax.swing.JTextField studAddress;
    private javax.swing.JTextField studCNumber;
    private javax.swing.JTextField studFirstName;
    private javax.swing.JTextField studIDtextfield;
    private javax.swing.JTextField studLastName;
    private javax.swing.JTextField studProgram;
    private javax.swing.JTextField studSection;
    private javax.swing.JLabel stud_icon;
    private javax.swing.JTable stud_table;
    private javax.swing.JLabel student;
    private javax.swing.JPanel studentpanel;
    private javax.swing.JLabel user_cnumberlabel;
    private javax.swing.JLabel user_emaillabel;
    private javax.swing.JLabel user_fnamelabel;
    private javax.swing.JLabel user_fnamelabel1;
    private javax.swing.JLabel user_passwordlabel;
    private javax.swing.JLabel user_passwordlabel1;
    private javax.swing.JLabel user_passwordlabel2;
    private javax.swing.JLabel user_type;
    private javax.swing.JLabel vio_icon;
    private javax.swing.JLabel violate;
    private javax.swing.JLabel violation;
    // End of variables declaration//GEN-END:variables
}
