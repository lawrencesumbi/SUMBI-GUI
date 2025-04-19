package config;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.*;
import javax.swing.JPanel;

public class panelPrinter implements Printable {

    private final JPanel panelToPrint;

    // Constructor to receive JPanel
    public panelPrinter(JPanel panelToPrint) {
        if (panelToPrint == null) {
            throw new IllegalArgumentException("Panel to print cannot be null!");
        }
        this.panelToPrint = panelToPrint;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        
        // ✅ Correctly configure page format
        pageFormat = getModifiedPageFormat(pageFormat);
        
        // ✅ Center content
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // ✅ Scale panel to fit printable area (prevents content cut-off)
        double scaleX = pageFormat.getImageableWidth() / panelToPrint.getWidth();
        double scaleY = pageFormat.getImageableHeight() / panelToPrint.getHeight();
        double scaleFactor = Math.min(scaleX, scaleY);
        g2d.scale(scaleFactor, scaleFactor);

        // ✅ Print panel content
        panelToPrint.printAll(g2d);

        return Printable.PAGE_EXISTS;
    }

    private PageFormat getModifiedPageFormat(PageFormat pageFormat) {
        Paper paper = new Paper();
        double width = 8.5 * 72; // Convert inches to points
        double height = 11 * 72;
        paper.setSize(width, height);
        paper.setImageableArea(36, 36, width - 72, height - 72); // ✅ Add margins

        PageFormat newFormat = (PageFormat) pageFormat.clone();
        newFormat.setPaper(paper);
        newFormat.setOrientation(PageFormat.PORTRAIT);
        return newFormat;
    }

    public void printPanel() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }
}
