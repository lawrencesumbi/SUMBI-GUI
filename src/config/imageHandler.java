/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class imageHandler {
    
    public static void chooseImage(JLabel image, JButton browse, JButton browse1) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                String destination = "src/usersImages/" + selectedFile.getName();
                String path = selectedFile.getAbsolutePath();

                // ✅ Check if file already exists in "usersImages"
                if (FileExistenceChecker(destination)) {
                    JOptionPane.showMessageDialog(null, "File Already Exists! Rename or Choose Another.");
                } else {
                    // ✅ Copy the file to destination
                    Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);

                    // ✅ Set the image to JLabel
                    image.setIcon(ResizeImage(destination, null, image));

                    // ✅ Adjust button visibility
                    browse.setVisible(true);
                    browse.setText("REMOVE");
                    browse1.setVisible(false);
                }
            } catch (Exception ex) {
                ex.printStackTrace(); // ✅ Print full error for debugging
            }
        }
    }

    // ✅ Corrected: Now returns boolean instead of 1/0
    public static boolean FileExistenceChecker(String destinationPath) {
        return Files.exists(Paths.get(destinationPath));
    }

    public static int getHeightFromWidth(String imagePath, int desiredWidth) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();

            return (int) ((double) desiredWidth / originalWidth * originalHeight);
        } catch (IOException ex) {
            System.out.println("No image found!");
            return -1;
        }
    }

    public static ImageIcon ResizeImage(String imagePath, byte[] pic, JLabel label) {
        ImageIcon myImage = (imagePath != null) ? new ImageIcon(imagePath) : new ImageIcon(pic);

        int newHeight = getHeightFromWidth(imagePath, label.getWidth());

        Image img = myImage.getImage();
        Image newImg = img.getScaledInstance(label.getWidth(), newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    public void imageUpdater(String existingFilePath, String newFilePath) {
        File existingFile = new File(existingFilePath);
        File newFile = new File(newFilePath);

        if (existingFile.exists()) {
            existingFile.delete();
        }

        try {
            Files.copy(newFile.toPath(), existingFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Image updated successfully.");
        } catch (IOException e) {
            e.printStackTrace(); // ✅ Prints full error message
        }
    }
}
