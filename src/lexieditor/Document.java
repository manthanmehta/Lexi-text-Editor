package document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


interface BaseComposite {

    JPanel addComposite();

    JTextPane addLeaf();
    Font f = new Font("Courier", Font.BOLD, 20);
    Font f1 = new Font("Lucida Handwriting", Font.BOLD, 36);
}

class ChandakSir implements BaseComposite {

    JFrame frame;
    JPanel north, editPane, status, image, tools, menu, east, west, padding1, padding2, padding_bottom;
    JLabel message, background, title, foreground;
    JTextPane editarea2;
    int flagSave = 0;
    File fileSave = null;
    int clickCount = 0;

    ChandakSir() {
        frame = new JFrame("Document Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        background = new JLabel(new ImageIcon("C:\\Users\\ruch0\\OneDrive\\Documents\\NetBeansProjects\\Document\\src\\document\\bg7.jpg"));
        frame.setContentPane(background);
        frame.setLayout(new BorderLayout());
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    @Override
    public JPanel addComposite() {
        north = new JPanel(new BorderLayout());
        editPane = new JPanel();
        status = new JPanel(new BorderLayout());
        image = new JPanel(new FlowLayout());
        tools = new JPanel(new BorderLayout());
        east = new JPanel();
        west = new JPanel();
        message = new JLabel("Status will be displayed here.");
        message.setForeground(Color.WHITE);
        message.setFont(f);
        padding1 = new JPanel();
        padding2 = new JPanel();
        padding_bottom = new JPanel();
        title = new JLabel("Document Editor");
        title.setForeground(Color.WHITE);
        title.setFont(f1);
        menu = new JPanel(new BorderLayout());

        frame.add(north, BorderLayout.NORTH);
        frame.add(editPane, BorderLayout.CENTER);
        frame.add(status, BorderLayout.SOUTH);
        frame.add(east, BorderLayout.EAST);
        frame.add(west, BorderLayout.WEST);
        north.add(image, BorderLayout.NORTH);
        north.add(menu, BorderLayout.SOUTH);
        
        image.add(title);
       
        message.setPreferredSize(new Dimension(600, 200));
        
        padding_bottom.setPreferredSize(new Dimension(800, 200));
        status.add(padding_bottom, BorderLayout.WEST);
        status.add(message, BorderLayout.CENTER);
        menu.add(padding1, BorderLayout.WEST);
        menu.add(padding2, BorderLayout.EAST);
        menu.add(tools, BorderLayout.CENTER);
        tools.setLayout(new BoxLayout(tools, BoxLayout.X_AXIS));

        image.setPreferredSize(new Dimension(500, 100));
        tools.setPreferredSize(new Dimension(500, 100));
        editPane.setPreferredSize(new Dimension(600, 150));
        status.setPreferredSize(new Dimension(100, 100));
        east.setPreferredSize(new Dimension(200, 100));
        west.setPreferredSize(new Dimension(200, 100));
        padding1.setPreferredSize(new Dimension(200, 100));
        padding2.setPreferredSize(new Dimension(200, 100));

        padding_bottom.setOpaque(false);
        menu.setOpaque(false);
        tools.setOpaque(false);
        east.setOpaque(false);
        west.setOpaque(false);
        status.setOpaque(false);
        padding1.setOpaque(false);
        padding2.setOpaque(false);
        image.setOpaque(false);
        editPane.setOpaque(false);
        north.setOpaque(false);
        return tools;
    }

    @Override
    public JTextPane addLeaf() {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        menubar.add(file);

        editarea2 = new JTextPane();
        editarea2.setMargin(new Insets(50, 50, 50, 50));
        editarea2.setPreferredSize(new Dimension(600, 600));
        JScrollPane scroll = new JScrollPane(editarea2);

        JMenuItem open = new JMenuItem("Open");
        file.add(open);
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                XWPFWordExtractor extractor;
                JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
                        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        editarea2.read(input, "READING FILE :-)");
                        fis.close();
                    } catch (Exception ex) {
                        System.out.println("problem accessing file" + file.getAbsolutePath());
                    }
                } else {
                    System.out.println("File access cancelled by user.");
                }
            }
        });

        JMenuItem neww = new JMenuItem("New");
        file.add(neww);
        neww.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (editarea2.getText().isEmpty()) {
                    editarea2.setText("");
                } else {
                    save();
                    editarea2.setText("");
                    flagSave = 0;
                    fileSave = null;
                }
            }
        });

        JMenuItem save = new JMenuItem("Save");
        file.add(save);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (flagSave == 1) {
                    BufferedWriter outFile = null;
                    try {
                        outFile = new BufferedWriter(new FileWriter(fileSave));
                    } catch (IOException ex) {
                        Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        editarea2.write(outFile);
                    } catch (IOException ex) {
                        Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (outFile != null) {
                            try {
                                outFile.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                } else {
                    save();
                }
            }
        });

        JMenuItem saveas = new JMenuItem("Save As");
        file.add(saveas);
        saveas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                save();
            }
        });

        JMenuItem quit = new JMenuItem("Quit");
        file.add(quit);
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                frame.dispose();
            }
        });

        JMenu edit = new JMenu("Edit");
        menubar.add(edit);
        JMenuItem undo = new JMenuItem("Undo");
        edit.add(undo);
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

            }
        });

        JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        edit.add(cut);

        JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        edit.add(copy);

        JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        edit.add(paste);
        
        JRootPane root = image.getRootPane();
        root.setJMenuBar(menubar);
        JRootPane root2 = editPane.getRootPane();

        root2.getContentPane().add(scroll);
        editPane.setVisible(true);
        return editarea2;
    }

    public void save() {
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Text File", "txt");
        final JFileChooser saveAsFileChooser = new JFileChooser();
        saveAsFileChooser.setApproveButtonText("Save");
        saveAsFileChooser.setFileFilter(extensionFilter);
        int actionDialog = saveAsFileChooser.showOpenDialog(frame);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = saveAsFileChooser.getSelectedFile();
        if (!file.getName().endsWith(".txt")) {
            file = new File(file.getAbsolutePath() + ".txt");
        }

        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(file));
        } catch (IOException ex) {
            System.out.println("Exception Occurred");
        }

        try {
            editarea2.write(outFile);
        } catch (IOException ex) {
            System.out.println("Exception occurred");
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {
                }
            }

        }
    }
}

interface ButtonsCommand {

    void click();
    Font f = new Font("Courier", Font.BOLD, 20);
    Dimension dimen = new Dimension(200, 200);

}

class FontSize implements ButtonsCommand {

    JButton incr, decr;
    JPanel tools, p1;
    JTextPane pane;
    JLabel l1, label;
    static int a;

    FontSize(JPanel panel, JTextPane pane) {
        p1 = new JPanel();
        p1.setPreferredSize(dimen);
        p1.setOpaque(false);
        this.tools = panel;
        this.pane = pane;

        incr = new JButton("Up");
        incr.setAlignmentX(Component.CENTER_ALIGNMENT);
        decr = new JButton("Down");
        decr.setAlignmentX(Component.CENTER_ALIGNMENT);
        l1 = new JLabel("Size ");
        l1.setAlignmentX(Component.CENTER_ALIGNMENT);
        l1.setForeground(Color.WHITE);
        label = new JLabel("\t\t\t\t");

        StyledDocument doc = (StyledDocument) pane.getDocument();
        Style style = doc.addStyle("StyleName", null);
        a = StyleConstants.getFontSize(style);
    }

    @Override
    public void click() {

        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        l1.setFont(f);
        p1.add(l1);
        p1.add(incr, Component.CENTER_ALIGNMENT);
        p1.add(decr, Component.CENTER_ALIGNMENT);
        p1.add(label);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp, BorderLayout.WEST);
        temp.setBackground(Color.ORANGE);
        tools.add(p1);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp);
        p1.setBackground(Color.ORANGE);

        incr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Font font = pane.getFont();
                float size = font.getSize() + 2.0f;
                pane.setFont(font.deriveFont(size));
            }
        });

        decr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Font font = pane.getFont();
                float size = font.getSize() - 2.0f;
                pane.setFont(font.deriveFont(size));
            }
        });
    }
}

class FontStyle implements ButtonsCommand {

    JButton bold, italics, underline;
    JPanel tools, p2;
    JTextPane pane;
    JLabel l2;

    FontStyle(JPanel panel, JTextPane pane) {
        p2 = new JPanel();
        p2.setOpaque(false);
        this.tools = panel;
        this.pane = pane;
        bold = new JButton("Bold");
        bold.setAlignmentX(Component.CENTER_ALIGNMENT);
        italics = new JButton("Italics");
        italics.setAlignmentX(Component.CENTER_ALIGNMENT);
        underline = new JButton("Underline");
        underline.setAlignmentX(Component.CENTER_ALIGNMENT);
        l2 = new JLabel("Style ");
        l2.setForeground(Color.WHITE);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    public void click() {
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        l2.setFont(f);
        p2.add(l2);
        p2.add(bold);
        p2.add(italics);
        p2.add(underline);
        tools.add(p2);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        temp.setBackground(Color.ORANGE);
        tools.add(temp);
        p2.setBackground(Color.ORANGE);

        bold.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) pane.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setBold(style, true);
                try {
                    String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                    pane.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });

        italics.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae
            ) {
                StyledDocument doc = (StyledDocument) pane.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setItalic(style, true);
                try {
                    String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                    pane.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        }
        );

        underline.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae
            ) {
                StyledDocument doc = (StyledDocument) pane.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setUnderline(style, true);
                try {
                    String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                    pane.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        }
        );
    }

}

class FontColor implements ButtonsCommand {

    JPanel tools, p3;
    JTextPane pane;
    JButton red, green, blue;
    JLabel l3;

    FontColor(JPanel tools, JTextPane pane) {
        this.pane = pane;
        this.tools = tools;
        p3 = new JPanel();
        p3.setOpaque(false);
        red = new JButton("Red");
        red.setAlignmentX(Component.CENTER_ALIGNMENT);
        green = new JButton("Green");
        green.setAlignmentX(Component.CENTER_ALIGNMENT);
        blue = new JButton("Blue");
        blue.setAlignmentX(Component.CENTER_ALIGNMENT);
        l3 = new JLabel("Color");
        l3.setForeground(Color.WHITE);
        l3.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    public void click() {
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
        l3.setFont(f);
        p3.add(l3);
        p3.add(red);
        p3.add(green);
        p3.add(blue);
        tools.add(p3);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp);
        temp.setBackground(Color.ORANGE);
        p3.setBackground(Color.ORANGE);

        red.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) pane.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setForeground(style, Color.RED);
                try {
                    String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                    pane.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });

        green.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) pane.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setForeground(style, Color.GREEN);
                try {
                    String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                    pane.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });

        blue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) pane.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setForeground(style, Color.BLUE);
                try {
                    String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                    pane.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });

    }
}

class FontFamily implements ButtonsCommand {

    JPanel tools, p4;
    JTextPane pane;
    JButton tnr, calibre, sansSerif;
    JLabel l4;

    FontFamily(JPanel tools, JTextPane pane) {
        this.pane = pane;
        this.tools = tools;
        p4 = new JPanel();
        p4.setOpaque(false);
        tnr = new JButton("Times New Roman");
        tnr.setAlignmentX(Component.CENTER_ALIGNMENT);
        calibre = new JButton("Calibre");
        calibre.setAlignmentX(Component.CENTER_ALIGNMENT);
        sansSerif = new JButton("Surprise");
        sansSerif.setAlignmentX(Component.CENTER_ALIGNMENT);
        l4 = new JLabel("Font Family");
        l4.setForeground(Color.WHITE);
        l4.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    public void click() {
        p4.setLayout(new BoxLayout(p4, BoxLayout.Y_AXIS));
        l4.setFont(f);
        p4.add(l4);
        p4.add(tnr);
        p4.add(calibre);
        p4.add(sansSerif);
        tools.add(p4);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp);
        temp.setBackground(Color.ORANGE);
        p4.setBackground(Color.ORANGE);

        tnr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) pane.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setFontFamily(style, "Times New Roman");
                try {
                    String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                    pane.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });

        calibre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) pane.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setFontFamily(style, "Calibri");
                try {
                    String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                    pane.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });

        sansSerif.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) pane.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setFontSize(style, 32);
                StyleConstants.setFontFamily(style, "OCR A Extended");
                try {
                    String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                    pane.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
    }
}

class Decorate implements ButtonsCommand {

    JPanel tools, p5;
    JTextPane pane;
    JButton addscbar, addBorder;
    JLabel l5, label;

    Decorate(JPanel tools, JTextPane pane) {
        this.pane = pane;
        this.tools = tools;
        p5 = new JPanel();
        label = new JLabel("\t\t\t\t");
        p5.setOpaque(false);
        addscbar = new JButton("Add Scroll Bar");
        addscbar.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBorder = new JButton("Add Border");
        addBorder.setAlignmentX(Component.CENTER_ALIGNMENT);
        l5 = new JLabel("Decorate");
        l5.setForeground(Color.WHITE);
        l5.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    public void click() {
        p5.setLayout(new BoxLayout(p5, BoxLayout.Y_AXIS));
        l5.setFont(f);
        p5.add(l5);
        p5.add(addscbar);
        p5.add(addBorder);
        p5.add(label);
        tools.add(p5);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp);
        temp.setBackground(Color.ORANGE);
        p5.setBackground(Color.ORANGE);

        addBorder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Decorating d = new AddBorder(pane);
                d.addDecor(0);
            }
        });

        addscbar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //Put code for adding scroll bar
            }
        });
    }
}

class Os implements ButtonsCommand {

    JPanel tools, p6;
    JTextPane pane;
    JButton windows, linux, macOS;
    JLabel status, l2;

    Os(JPanel tools, JTextPane pane, JLabel status) {
        this.pane = pane;
        this.tools = tools;
        this.status = status;
        p6 = new JPanel();
        p6.setOpaque(false);
        windows = new JButton("Windows");
        windows.setAlignmentX(Component.CENTER_ALIGNMENT);
        linux = new JButton("Linux");
        linux.setAlignmentX(Component.CENTER_ALIGNMENT);
        macOS = new JButton("MacOS");
        macOS.setAlignmentX(Component.CENTER_ALIGNMENT);
        l2 = new JLabel("Operating System");
        l2.setForeground(Color.WHITE);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    public void click() {
        p6.setLayout(new BoxLayout(p6, BoxLayout.Y_AXIS));
        l2.setFont(f);
        p6.add(l2);
        p6.add(windows);
        p6.add(linux);
        p6.add(macOS);
        tools.add(p6);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp);
        temp.setBackground(Color.ORANGE);
        p6.setBackground(Color.ORANGE);

        windows.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Software windows = new OperatingSystem("Windows", "Platform1", new Windows(), status);
                windows.display();
            }
        });

        linux.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Software linux = new OperatingSystem("Linux", "Platform2", new Linux(), status);
                linux.display();
            }
        });

        macOS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Software macOS = new OperatingSystem("MacOS", "Platform3", new Mac(), status);
                macOS.display();
            }
        });
    }
}

class Invoke {

    private java.util.List<ButtonsCommand> orderList = new ArrayList<>();

    public void addButton(ButtonsCommand button) {
        orderList.add(button);
    }

    public void placeButton() {
        for (ButtonsCommand buttonsCommand : orderList) {
            buttonsCommand.click();
        }
    }
}

//Bridge Design Pattern
interface drawOS {

    public void displayOS(String os, String platform, JLabel message);
}

class Windows implements drawOS {

    @Override
    public void displayOS(String os, String platform, JLabel status) {
        status.setText("Document made compatible with Windows OS");
    }

}

class Linux implements drawOS {

    @Override
    public void displayOS(String os, String platform, JLabel message) {
        message.setText("Document made compatible with LINUX OS");
    }

}

class Mac implements drawOS {

    @Override
    public void displayOS(String os, String platform, JLabel message) {
        message.setText("Document made compatible with MacOS");
    }

}

abstract class Software {

    private drawOS dos;

    Software(drawOS dos) {
        this.dos = dos;
    }

    public abstract void display();
}

class OperatingSystem extends Software {

    String os, platform;
    drawOS dos;
    JLabel message;

    public OperatingSystem(String os, String playform, drawOS dos, JLabel message) {
        super(dos);
        this.dos = dos;
        this.os = os;
        this.platform = platform;
        this.message = message;
    }

    @Override
    public void display() {
        dos.displayOS(os, platform, message);
    }
}

//Decorator Design Pattern
abstract class Decorating {

    JTextPane pane;

    Decorating(JTextPane pane) {
        this.pane = pane;
    }

    abstract void addDecor(int flag);
}

class AddBorder extends Decorating {

    public AddBorder(JTextPane pane) {
        super(pane);
        this.pane = pane;
    }

    @Override
    void addDecor(int flag) {
        if (flag % 2 == 0) {
            pane.setMargin(new Insets(100, 100, 100, 100));
            pane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 25), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        } else {
            pane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        }
    }
}

class AddScrollBar extends Decorating {

    public AddScrollBar(JTextPane pane) {
        super(pane);
        this.pane = pane;
    }

    @Override
    void addDecor(int flag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

public class Document {

    public static void main(String[] args) {
        ChandakSir mc = new ChandakSir();
        JPanel p = mc.addComposite();
        JTextPane t = mc.addLeaf();

        FontSize fs = new FontSize(p, t);
        FontStyle st = new FontStyle(p, t);
        FontColor clr = new FontColor(p, t);
        FontFamily fam = new FontFamily(p, t);
        Decorate dr = new Decorate(p, t);
        Os os = new Os(p, t, mc.message);

        Invoke iv = new Invoke();
        iv.addButton(fs);
        iv.addButton(st);
        iv.addButton(clr);
        iv.addButton(fam);
        iv.addButton(dr);
        iv.addButton(os);

        iv.placeButton();
    }

}