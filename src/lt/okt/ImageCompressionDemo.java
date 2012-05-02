package lt.okt;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;
import java.io.*;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.util.Locale;

class ImageCompressionDemo {

    private BufferedImage originalImage;
    private BufferedImage textImage;

    private JPanel gui;

    private JCheckBox antialiasing;
    private JCheckBox rendering;
    private JCheckBox fractionalMetrics;
    private JCheckBox strokeControl;
    private JCheckBox colorRendering;
    private JCheckBox dithering;

    private JComboBox textAntialiasing;
    private JComboBox textLcdContrast;

    private JLabel jpegLabel;
    private JLabel pngLabel;

    private JTextArea output;

    private JSlider quality;

    private int pngSize;
    private int jpgSize;

    final static Object[] VALUES_TEXT_ANTIALIASING = {
        RenderingHints.VALUE_TEXT_ANTIALIAS_OFF,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
//        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP,
//        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR,
//        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB,
//        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR,
//        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB
    };

    final static Object[] VALUES_TEXT_LCD_CONTRAST = {
        new Integer(100),
        new Integer(150),
        new Integer(200),
        new Integer(250)
    };

    ImageCompressionDemo() {
        int width = 280;
        int height = 100;

        gui = new JPanel(new BorderLayout(3,4));

        quality = new JSlider(JSlider.VERTICAL, 0, 100, 75);
        quality.setSnapToTicks(true);
        quality.setPaintTicks(true);
        quality.setPaintLabels(true);
        quality.setMajorTickSpacing(10);
        quality.setMinorTickSpacing(5);
        quality.addChangeListener( new ChangeListener(){
            public void stateChanged(ChangeEvent ce) {
                updateImages();
            }
        } );
        gui.add(quality, BorderLayout.WEST);

        originalImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        textImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        JPanel controls = new JPanel(new GridLayout(0,1,0,0));
        antialiasing = new JCheckBox("Anti-aliasing", false);
        rendering = new JCheckBox("Rendering - Quality", true);
        fractionalMetrics = new JCheckBox("Fractional Metrics", true);
        strokeControl = new JCheckBox("Stroke Control - Pure", false);
        colorRendering = new JCheckBox("Color Rendering - Quality", true);
        dithering = new JCheckBox("Dithering", false);

        controls.add(antialiasing);

        controls.add(fractionalMetrics);

        textLcdContrast = new JComboBox(VALUES_TEXT_LCD_CONTRAST);
        JPanel lcdContrastPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        lcdContrastPanel.add(textLcdContrast);
        lcdContrastPanel.add(new JLabel("Text LCD Contrast"));
        controls.add(lcdContrastPanel);

        textAntialiasing = new JComboBox(VALUES_TEXT_ANTIALIASING);
        controls.add(textAntialiasing);

        controls.add(dithering);
        controls.add(rendering);
        controls.add(colorRendering);
        controls.add(strokeControl);


        ItemListener itemListener = new ItemListener(){    
            public void itemStateChanged(ItemEvent e) {
                updateImages();
            }
        };
        antialiasing.addItemListener(itemListener);
        rendering.addItemListener(itemListener);
        fractionalMetrics.addItemListener(itemListener);
        strokeControl.addItemListener(itemListener);
        colorRendering.addItemListener(itemListener);
        dithering.addItemListener(itemListener);

        textAntialiasing.addItemListener(itemListener);
        textLcdContrast.addItemListener(itemListener);

        Graphics2D g2d = originalImage.createGraphics();
        GradientPaint gp = new GradientPaint(
            0f, 0f, Color.red,
            (float)width, (float)height, Color.orange);
        g2d.setPaint(gp);
        g2d.fillRect(0,0, width, height);

        g2d.setColor(Color.blue);
        for (int ii=0; ii<width; ii+=10) {
            g2d.drawLine(ii, 0, ii, height);
        }
        g2d.setColor(Color.green);
        for (int jj=0; jj<height; jj+=10) {
            g2d.drawLine(0, jj, width, jj);
        }

        gui.add(controls, BorderLayout.EAST);

        JPanel images = new JPanel(new GridLayout(0,1,2,2));
        images.add(new JLabel(new ImageIcon(textImage)));

        try {
            pngLabel = new JLabel(new ImageIcon(getPngCompressedImage(textImage)));
            images.add(pngLabel);
            jpegLabel = new JLabel(new ImageIcon(getJpegCompressedImage(textImage)));
            images.add(jpegLabel);
        } catch(IOException ioe) {
        }

        gui.add(images, BorderLayout.CENTER);
        output = new JTextArea(4,40);
        output.setEditable(false);
        gui.add(new JScrollPane(output), BorderLayout.SOUTH);

        updateImages();

        JOptionPane.showMessageDialog(null, gui);
    }

    private Image getPngCompressedImage(BufferedImage image) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ImageIO.write( image, "png", outStream );

        pngSize = outStream.toByteArray().length;

        BufferedImage compressedImage =
            ImageIO.read(new ByteArrayInputStream(outStream.toByteArray()));

        return compressedImage;
    }

    private Image getJpegCompressedImage(BufferedImage image) throws IOException {
        float qualityFloat = (float)quality.getValue()/100f;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        ImageWriter imgWriter = ImageIO.getImageWritersByFormatName( "jpg" ).next();
        ImageOutputStream ioStream = ImageIO.createImageOutputStream( outStream );
        imgWriter.setOutput( ioStream );

        JPEGImageWriteParam jpegParams = new JPEGImageWriteParam( Locale.getDefault() );
        jpegParams.setCompressionMode( ImageWriteParam.MODE_EXPLICIT );
        jpegParams.setCompressionQuality( qualityFloat );

        imgWriter.write( null, new IIOImage( image, null, null ), jpegParams );

        ioStream.flush();
        ioStream.close();
        imgWriter.dispose();

        jpgSize = outStream.toByteArray().length;

        BufferedImage compressedImage = ImageIO.read(new ByteArrayInputStream(outStream.toByteArray()));
        return compressedImage;
    }

    private void updateText() {
        StringBuilder builder = new StringBuilder();

        builder.append("Fractional Metrics: \t");
        builder.append( fractionalMetrics.isSelected() );
        builder.append("\n");
        builder.append( textAntialiasing.getSelectedItem() );
        builder.append("\nPNG size: \t");
        builder.append(pngSize);
        builder.append(" bytes\n");
        builder.append("JPG size: \t");
        builder.append(jpgSize);
        builder.append(" bytes \tquality: ");
        builder.append(quality.getValue());

        output.setText(builder.toString());
    }

    private void updateImages() {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        Graphics2D g2dText = textImage.createGraphics();

        if (antialiasing.isSelected()) {
            g2dText.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g2dText.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        if (rendering.isSelected()) {
            g2dText.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        } else {
            g2dText.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);
        }

        if (fractionalMetrics.isSelected()) {
            g2dText.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        } else {
            g2dText.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        }

        if (strokeControl.isSelected()) {
            g2dText.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE);
        } else {
            g2dText.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
        }

        if (dithering.isSelected()) {
            g2dText.setRenderingHint(RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_ENABLE);
        } else {
            g2dText.setRenderingHint(RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_DISABLE);
        }

        if (colorRendering.isSelected()) {
            g2dText.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        } else {
            g2dText.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_SPEED);
        }

//        g2dText.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST,
            textLcdContrast.getSelectedItem();

        g2dText.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            textAntialiasing.getSelectedItem());

        g2dText.drawImage(originalImage, 0,0, null);
        g2dText.setColor(Color.black);
        g2dText.drawString("The quick brown fox jumped over the lazy dog.", 10,50);

        try {
            jpegLabel.setIcon(new ImageIcon(getJpegCompressedImage(textImage)));
            pngLabel.setIcon(new ImageIcon(getPngCompressedImage(textImage)));
        } catch(IOException ioe) {
        }

        gui.repaint();
        updateText();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                ImageCompressionDemo iwt = new ImageCompressionDemo();
            }
        } );
    }
}