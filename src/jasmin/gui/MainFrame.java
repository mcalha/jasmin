/*
 * mainframe.java Created on 16. März 2006, 17:23
 */
package jasmin.gui;

import jasmin.core.HelpLoader;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.Properties;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.AbstractDocument;
import javax.swing.undo.*;

/**
 * @author Kai Orend
 */
public class MainFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    private JasDocument document = null;
    private HelpBrowser helpDocument = null;
    public JFileChooser fileChooser = new JFileChooser();
    //File to save snapshot
    private File snapshot;
    public HelpLoader helpLoader = null;
    private Properties properties;

    /**
     * Creates new form mainframe
     */
    public MainFrame() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/jasmin/gui/resources/icons/logo.png")));
        File propfile = new File(System.getProperty("user.home") + File.separator + ".jasmin");
        try {
            properties = new Properties();
            if (!propfile.exists()) {
                propfile.createNewFile();
                putProperty("font", "Sans Serif");
                putProperty("font.size", "12");
                putProperty("memory", "4096");
                putProperty("language", "en");
                putProperty("repository", "32");
                FileOutputStream out = new FileOutputStream(propfile);
                try {
                    properties.store(out, "Jasmin configuration file");
                } finally {
                    out.close();
                }
            } else {
                FileInputStream in = new FileInputStream(propfile);
                try {
                    properties.load(in);
                } finally {
                    in.close();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not open:" + propfile.toString() + "\n"
                    + ex.toString());
            System.exit(1);
        }

        initComponents();
        helpLoader = new HelpLoader(getProperty("language"));

        checkButtonStates();
        addHelp(getClass().getResource("/jasmin/gui/resources/Welcome.htm"), "Welcome");
        this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH);

        String lastpath = getProperty("lastpath.asm");
        if (lastpath != null) {
            fileChooser.setSelectedFile(new File(lastpath));
        }

        // restore last save if execution did not terminate well
        int closedWell = this.getProperty("closed_well", 1);
        if (closedWell == 0) {
            File temp = new File(System.getProperty("user.home") + File.separator + ".jasmintemp.asm");
            if (temp.exists()) {
                this.newDocument();
                document.loadFile(temp);
            }

        }

        this.putProperty("closed_well", 0);
        this.saveProperties();

        /*shortcuts
        jMenuItemUndo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()));
        jMenuItemRedo.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()));

        jMenuItem5.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()));
        jMenuItem3.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()));
        jMenuItem4.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()));

        jMenuItem13.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        jMenuItem15.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()));

        jMenuItem16.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));

        jMenuItem14.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        */
    }

    public void changeLnF(String LnF) {

        try {

            UIManager.setLookAndFeel(LnF);

            SwingUtilities.updateComponentTreeUI(this);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, LnF, e.toString(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param key
     */
    public String getProperty(String key) {
        String result = properties.getProperty(key);
        return result;
    }

    public int getProperty(String key, int oldvalue) {
        String result = properties.getProperty(key);
        if (result == null) {
            putProperty(key, oldvalue);
            return oldvalue;
        }
        return Integer.parseInt(result);
    }

    public void putProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public void putProperty(String key, int value) {
        properties.setProperty(key, "" + value);
    }

    /**
     * @param url
     * @param title
     */
    public void addHelp(URL url, String title) {
        HelpBrowser newbrowser = new HelpBrowser(this);
        newbrowser.openUrl(url);
        DocTab.add(title, newbrowser);
        DocTab.setSelectedComponent(newbrowser);
        helpDocument = newbrowser;
    }

    public int getDelay() {
        return delaySlider.getValue();
    }

    /**
     * Update all Button states.
     */
    public synchronized void checkButtonStates() {
        if ((document != null) && !document.running) {
            button_takesnapshot.setEnabled(true);
            button_loadsnapshot.setEnabled(document.hasSnapshot());

            jMenu2.setEnabled(true);

            button_play_clear3.setEnabled(true);

            if (!button_editpaste.isEnabled()) {
                button_editpaste.setEnabled(true);
            }
            if (!jMenuItem4.isEnabled()) {
                jMenuItem4.setEnabled(true);
            }
            if (!jMenuItem_filesave.isEnabled()) {
                jMenuItem_filesave.setEnabled(true);
            }
            if (!jMenuItem6.isEnabled()) {
                jMenuItem6.setEnabled(true);
            }
            if (!jMenuItem7.isEnabled()) {
                jMenuItem7.setEnabled(true);
            }
            if (!button_filesaveas.isEnabled()) {
                button_filesaveas.setEnabled(true);
            }
            if (!button_filesave.isEnabled()) {
                button_filesave.setEnabled(true);
            }
            if (!delaySlider.isEnabled()) {
                delaySlider.setEnabled(true);
            }
            if (!jMenuItem10.isEnabled()) {
                jMenuItem10.setEnabled(true);
            }
            if (!jMenu3.isEnabled()) {
                jMenu3.setEnabled(true);
            }
            if (!jMenuItem2.isEnabled()) {
                jMenuItem2.setEnabled(true);
            }
            if (!button_play_stop.isEnabled()) {
                button_play_stop.setEnabled(true);
            }
            if (!button_play_green.isEnabled()) {
                button_play_green.setEnabled(true);
            }
            if (!button_play_step.isEnabled()) {
                button_play_step.setEnabled(true);
            }
            if (!button_play_current.isEnabled()) {
                button_play_current.setEnabled(true);
            }
            if (!buttonStack.isEnabled()) {
                buttonStack.setEnabled(true);
            }
            if (!button_parameters.isEnabled()) {
                button_parameters.setEnabled(true);
            }
            if (!document.getEditor().isEnabled()) {
                document.getEditor().setEnabled(true);
            }

            if (button_undo.isEnabled() != (document.undoManager.canUndo())) {
                button_undo.setEnabled((document.undoManager.canUndo()));
                jMenuItemUndo.setEnabled((document.undoManager.canUndo()));
            }
            if (button_redo.isEnabled() != (document.undoManager.canRedo())) {
                button_redo.setEnabled((document.undoManager.canRedo()));
                jMenuItemRedo.setEnabled((document.undoManager.canRedo()));
            }

            boolean hasSelection = ((document.getEditor().getSelectionEnd()
                    - document.getEditor().getSelectionStart()) > 0);
            if (button_editcopy.isEnabled() != hasSelection) {
                button_editcopy.setEnabled(hasSelection);
                button_editcut.setEnabled(hasSelection);
                jMenuItem8.setEnabled(hasSelection);
                jMenuItem9.setEnabled(hasSelection);
            }

            jMenuItem15.setEnabled(document.running);
            jMenuItem13.setEnabled(!document.running);

            button_play_green.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                    "/jasmin/gui/resources/icons/play_green.png")));

        } else {
            button_takesnapshot.setEnabled(false);
            button_loadsnapshot.setEnabled(false);

            button_undo.setEnabled(false);
            jMenuItemUndo.setEnabled(false);
            button_redo.setEnabled(false);
            jMenuItemRedo.setEnabled(false);
            button_editcopy.setEnabled(false);
            button_editcut.setEnabled(false);
            jMenuItem8.setEnabled(false);
            jMenuItem9.setEnabled(false);
            jMenuItem15.setEnabled(false);

            button_editpaste.setEnabled(false);
            jMenuItem4.setEnabled(false);
            jMenuItem_filesave.setEnabled(false);
            jMenuItem6.setEnabled(false);
            jMenuItem7.setEnabled(false);
            button_filesaveas.setEnabled(false);
            button_filesave.setEnabled(false);
            delaySlider.setEnabled(false);
            jMenuItem10.setEnabled(false);
            jMenu2.setEnabled(false);
            jMenuItem2.setEnabled(false);
            button_play_stop.setEnabled(false);
            button_play_green.setEnabled(false);
            button_play_step.setEnabled(false);
            button_play_current.setEnabled(false);
            button_play_clear3.setEnabled(false);
            buttonStack.setEnabled(false);
            button_parameters.setEnabled(false);
            jMenu3.setEnabled(false);
        }
        if ((document != null) && document.running) {
            button_play_clear3.setEnabled(true);
            jMenuItem13.setEnabled(false);
            jMenuItem14.setEnabled(false);
            jMenuItem15.setEnabled(false);
            delaySlider.setEnabled(true);

            button_play_green.setEnabled(true);

            document.getEditor().setEnabled(false);
            button_play_green.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                    "/jasmin/gui/resources/icons/play_pause.png")));
        }

    }

    public void open() {
        JasDocument doc = new JasDocument(ErrorLabel, this, 0);

        if (doc.open()) {
            addDocument(doc);
        }

    }

    private void save() {
        document.save();
        DocTab.setSelectedComponent(document);
    }

    /**
     * Updates the title of the tab of an JasDocument.
     */
    public void updateTitle(JasDocument doc) {
        int index = DocTab.indexOfComponent(doc);
        if (index != -1) {
            DocTab.setTitleAt(index, doc.getTitle());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CloseMenu = new javax.swing.JPopupMenu();
        jMenuItem17 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        button_new = new javax.swing.JButton();
        button_fileopen = new javax.swing.JButton();
        button_filesave = new javax.swing.JButton();
        button_filesaveas = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        button_undo = new javax.swing.JButton();
        button_redo = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        button_editcut = new javax.swing.JButton();
        button_editcopy = new javax.swing.JButton();
        button_editpaste = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        button_play_green = new javax.swing.JButton();
        button_play_step = new javax.swing.JButton();
        button_play_current = new javax.swing.JButton();
        button_play_stop = new javax.swing.JButton();
        button_play_clear3 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        buttonStack = new javax.swing.JButton();
        button_parameters = new javax.swing.JButton();
        button_takesnapshot = new javax.swing.JButton();
        button_loadsnapshot = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        delaySlider = new javax.swing.JSlider();
        delayText = new javax.swing.JTextField();
        DocTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        ErrorLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItem_filesave = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        jMenuItem18 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemUndo = new javax.swing.JMenuItem();
        jMenuItemRedo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem_play_stop = new javax.swing.JMenuItem();
        jMenuItem_play_clear3 = new javax.swing.JMenuItem();

        jMenuItem17.setText("Close Tab");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        CloseMenu.add(jMenuItem17);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Jasmin");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setMinimumSize(new java.awt.Dimension(800, 600));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 600));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setDoubleBuffered(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(855, 33));

        button_new.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/new.png"))); // NOI18N
        button_new.setToolTipText("New");
        button_new.setBorder(null);
        button_new.setBorderPainted(false);
        button_new.setContentAreaFilled(false);
        button_new.setPreferredSize(new java.awt.Dimension(35, 30));
        button_new.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_newMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_newMouseExited(evt);
            }
        });
        button_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_newActionPerformed(evt);
            }
        });
        jToolBar1.add(button_new);

        button_fileopen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/fileopen.png"))); // NOI18N
        button_fileopen.setToolTipText("Open...");
        button_fileopen.setBorder(null);
        button_fileopen.setBorderPainted(false);
        button_fileopen.setContentAreaFilled(false);
        button_fileopen.setPreferredSize(new java.awt.Dimension(35, 30));
        button_fileopen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_fileopenMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_fileopenMouseExited(evt);
            }
        });
        button_fileopen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_fileopenActionPerformed(evt);
            }
        });
        jToolBar1.add(button_fileopen);

        button_filesave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/filesave.png"))); // NOI18N
        button_filesave.setToolTipText("Save");
        button_filesave.setBorder(null);
        button_filesave.setBorderPainted(false);
        button_filesave.setContentAreaFilled(false);
        button_filesave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_filesave.setPreferredSize(new java.awt.Dimension(35, 30));
        button_filesave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        button_filesave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_filesaveMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_filesaveMouseExited(evt);
            }
        });
        button_filesave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_filesaveActionPerformed(evt);
            }
        });
        jToolBar1.add(button_filesave);

        button_filesaveas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/filesaveas.png"))); // NOI18N
        button_filesaveas.setToolTipText("Save As...");
        button_filesaveas.setBorder(null);
        button_filesaveas.setBorderPainted(false);
        button_filesaveas.setContentAreaFilled(false);
        button_filesaveas.setPreferredSize(new java.awt.Dimension(35, 30));
        button_filesaveas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_filesaveasMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_filesaveasMouseExited(evt);
            }
        });
        button_filesaveas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_filesaveasActionPerformed(evt);
            }
        });
        jToolBar1.add(button_filesaveas);

        jPanel3.setMaximumSize(new java.awt.Dimension(10, 3));
        jPanel3.setMinimumSize(new java.awt.Dimension(10, 3));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(10, 3));
        jToolBar1.add(jPanel3);

        button_undo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/undo.png"))); // NOI18N
        button_undo.setToolTipText("Undo");
        button_undo.setBorder(null);
        button_undo.setBorderPainted(false);
        button_undo.setContentAreaFilled(false);
        button_undo.setPreferredSize(new java.awt.Dimension(35, 30));
        button_undo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_undoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_undoMouseExited(evt);
            }
        });
        button_undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_undoActionPerformed(evt);
            }
        });
        jToolBar1.add(button_undo);

        button_redo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/redo.png"))); // NOI18N
        button_redo.setToolTipText("Redo");
        button_redo.setBorder(null);
        button_redo.setBorderPainted(false);
        button_redo.setContentAreaFilled(false);
        button_redo.setPreferredSize(new java.awt.Dimension(35, 30));
        button_redo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_redoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_redoMouseExited(evt);
            }
        });
        button_redo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_redoActionPerformed(evt);
            }
        });
        jToolBar1.add(button_redo);

        jPanel5.setMaximumSize(new java.awt.Dimension(10, 3));
        jPanel5.setMinimumSize(new java.awt.Dimension(10, 3));
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(10, 3));
        jToolBar1.add(jPanel5);

        button_editcut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/editcut.png"))); // NOI18N
        button_editcut.setToolTipText("Cut");
        button_editcut.setBorder(null);
        button_editcut.setBorderPainted(false);
        button_editcut.setContentAreaFilled(false);
        button_editcut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_editcutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_editcutMouseExited(evt);
            }
        });
        button_editcut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_editcutActionPerformed(evt);
            }
        });
        jToolBar1.add(button_editcut);

        button_editcopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/editcopy.png"))); // NOI18N
        button_editcopy.setToolTipText("Copy");
        button_editcopy.setBorder(null);
        button_editcopy.setBorderPainted(false);
        button_editcopy.setContentAreaFilled(false);
        button_editcopy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_editcopyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_editcopyMouseExited(evt);
            }
        });
        button_editcopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_editcopyActionPerformed(evt);
            }
        });
        jToolBar1.add(button_editcopy);

        button_editpaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/editpaste.png"))); // NOI18N
        button_editpaste.setToolTipText("Paste");
        button_editpaste.setBorder(null);
        button_editpaste.setBorderPainted(false);
        button_editpaste.setContentAreaFilled(false);
        button_editpaste.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_editpasteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_editpasteMouseExited(evt);
            }
        });
        button_editpaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_editpasteActionPerformed(evt);
            }
        });
        jToolBar1.add(button_editpaste);

        jPanel6.setMaximumSize(new java.awt.Dimension(10, 3));
        jPanel6.setMinimumSize(new java.awt.Dimension(10, 3));
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(10, 3));
        jToolBar1.add(jPanel6);

        jPanel9.setMaximumSize(new java.awt.Dimension(10, 3));
        jPanel9.setMinimumSize(new java.awt.Dimension(10, 3));
        jPanel9.setOpaque(false);
        jPanel9.setPreferredSize(new java.awt.Dimension(10, 3));
        jToolBar1.add(jPanel9);

        jPanel10.setMaximumSize(new java.awt.Dimension(10, 3));
        jPanel10.setMinimumSize(new java.awt.Dimension(10, 3));
        jPanel10.setOpaque(false);
        jPanel10.setPreferredSize(new java.awt.Dimension(10, 3));
        jToolBar1.add(jPanel10);

        button_play_green.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/play_green.png"))); // NOI18N
        button_play_green.setToolTipText("Run the program");
        button_play_green.setBorder(null);
        button_play_green.setBorderPainted(false);
        button_play_green.setContentAreaFilled(false);
        button_play_green.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_play_greenMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_play_greenMouseExited(evt);
            }
        });
        button_play_green.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_play_greenActionPerformed(evt);
            }
        });
        jToolBar1.add(button_play_green);

        button_play_step.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/play_step.png"))); // NOI18N
        button_play_step.setToolTipText("Execute the command on the selected line");
        button_play_step.setBorder(null);
        button_play_step.setBorderPainted(false);
        button_play_step.setContentAreaFilled(false);
        button_play_step.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_play_stepMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_play_stepMouseExited(evt);
            }
        });
        button_play_step.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_play_stepActionPerformed(evt);
            }
        });
        jToolBar1.add(button_play_step);

        button_play_current.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/play_current.png"))); // NOI18N
        button_play_current.setToolTipText("Execute the command on the selected line without modifying the instruction pointer");
        button_play_current.setBorder(null);
        button_play_current.setBorderPainted(false);
        button_play_current.setContentAreaFilled(false);
        button_play_current.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_play_currentMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_play_currentMouseExited(evt);
            }
        });
        button_play_current.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_play_currentActionPerformed(evt);
            }
        });
        jToolBar1.add(button_play_current);

        button_play_stop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/play_stop.png"))); // NOI18N
        button_play_stop.setToolTipText("Stop the program");
        button_play_stop.setBorder(null);
        button_play_stop.setBorderPainted(false);
        button_play_stop.setContentAreaFilled(false);
        button_play_stop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_play_stopMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_play_stopMouseExited(evt);
            }
        });
        button_play_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_play_stopActionPerformed(evt);
            }
        });
        jToolBar1.add(button_play_stop);

        button_play_clear3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/play_clear3.png"))); // NOI18N
        button_play_clear3.setToolTipText("Reset the memory and all registers");
        button_play_clear3.setBorder(null);
        button_play_clear3.setBorderPainted(false);
        button_play_clear3.setContentAreaFilled(false);
        button_play_clear3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_play_clear3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_play_clear3MouseExited(evt);
            }
        });
        button_play_clear3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_play_clear3ActionPerformed(evt);
            }
        });
        jToolBar1.add(button_play_clear3);

        jPanel7.setMaximumSize(new java.awt.Dimension(10, 3));
        jPanel7.setMinimumSize(new java.awt.Dimension(10, 3));
        jPanel7.setOpaque(false);
        jPanel7.setPreferredSize(new java.awt.Dimension(10, 3));
        jToolBar1.add(jPanel7);

        jPanel8.setMaximumSize(new java.awt.Dimension(10, 3));
        jPanel8.setMinimumSize(new java.awt.Dimension(10, 3));
        jPanel8.setOpaque(false);
        jPanel8.setPreferredSize(new java.awt.Dimension(10, 3));
        jToolBar1.add(jPanel8);

        buttonStack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/Network-Stack-iconFInal2.png"))); // NOI18N
        buttonStack.setToolTipText("Turn On/Off Stack");
        buttonStack.setBorder(null);
        buttonStack.setBorderPainted(false);
        buttonStack.setContentAreaFilled(false);
        buttonStack.setFocusable(false);
        buttonStack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonStack.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonStack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonStackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonStackMouseExited(evt);
            }
        });
        buttonStack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStackActionPerformed(evt);
            }
        });
        jToolBar1.add(buttonStack);

        button_parameters.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/parameters.png"))); // NOI18N
        button_parameters.setToolTipText("Enter parameters");
        button_parameters.setBorder(null);
        button_parameters.setBorderPainted(false);
        button_parameters.setContentAreaFilled(false);
        button_parameters.setFocusable(false);
        button_parameters.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_parameters.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        button_parameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_parametersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_parametersMouseExited(evt);
            }
        });
        button_parameters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_parametersActionPerformed(evt);
            }
        });
        jToolBar1.add(button_parameters);

        button_takesnapshot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/takesnapshot.png"))); // NOI18N
        button_takesnapshot.setToolTipText("Save Memory");
        button_takesnapshot.setBorder(null);
        button_takesnapshot.setBorderPainted(false);
        button_takesnapshot.setContentAreaFilled(false);
        button_takesnapshot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_takesnapshotMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_takesnapshotMouseExited(evt);
            }
        });
        button_takesnapshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_takesnapshotActionPerformed(evt);
            }
        });
        jToolBar1.add(button_takesnapshot);

        button_loadsnapshot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/loadsnapshot.png"))); // NOI18N
        button_loadsnapshot.setToolTipText("Load Memory");
        button_loadsnapshot.setBorder(null);
        button_loadsnapshot.setBorderPainted(false);
        button_loadsnapshot.setContentAreaFilled(false);
        button_loadsnapshot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_loadsnapshotMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_loadsnapshotMouseExited(evt);
            }
        });
        button_loadsnapshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_loadsnapshotActionPerformed(evt);
            }
        });
        jToolBar1.add(button_loadsnapshot);
        jToolBar1.add(jSeparator7);

        jLabel1.setText("Operation delay:");
        jToolBar1.add(jLabel1);

        delaySlider.setMaximum(250);
        delaySlider.setValue(0);
        delaySlider.setMaximumSize(new java.awt.Dimension(150, 27));
        delaySlider.setOpaque(false);
        delaySlider.setPreferredSize(new java.awt.Dimension(150, 27));
        delaySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                delaySliderStateChanged(evt);
            }
        });
        jToolBar1.add(delaySlider);

        delayText.setEditable(false);
        delayText.setText("0 ms");
        delayText.setBorder(null);
        delayText.setMaximumSize(new java.awt.Dimension(70, 30));
        delayText.setOpaque(false);
        delayText.setPreferredSize(new java.awt.Dimension(15, 14));
        jToolBar1.add(delayText);

        jPanel2.add(jToolBar1, java.awt.BorderLayout.NORTH);

        DocTab.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                DocTabComponentRemoved(evt);
            }
        });
        DocTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                DocTabStateChanged(evt);
            }
        });
        DocTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DocTabMouseClicked(evt);
            }
        });
        jPanel2.add(DocTab, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        ErrorLabel.setForeground(new java.awt.Color(204, 0, 51));
        ErrorLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.add(ErrorLabel, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel1, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuBar1.setBorderPainted(false);

        jMenu1.setMnemonic('f');
        jMenu1.setText("File");
        jMenu1.setContentAreaFilled(false);
        jMenu1.setPreferredSize(new java.awt.Dimension(30, 19));
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/new.png"))); // NOI18N
        jMenuItem5.setText("New");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/fileopen.png"))); // NOI18N
        jMenuItem3.setText("Open...");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);
        jMenu1.add(jSeparator2);

        jMenuItem_filesave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem_filesave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/filesave.png"))); // NOI18N
        jMenuItem_filesave.setText("Save");
        jMenuItem_filesave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_filesaveActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem_filesave);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/filesaveas.png"))); // NOI18N
        jMenuItem4.setMnemonic('s');
        jMenuItem4.setText("Save As...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);
        jMenu1.add(jSeparator3);

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/takesnapshot.png"))); // NOI18N
        jMenuItem6.setText("Save Memory");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/loadsnapshot.png"))); // NOI18N
        jMenuItem7.setText("Load Memory");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);
        jMenu1.add(jSeparator4);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/close.png"))); // NOI18N
        jMenuItem2.setText("Close Document");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator6);

        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/configuration.png"))); // NOI18N
        jMenuItem18.setText("Configuration");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem18);
        jMenu1.add(jSeparator5);

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/exit.png"))); // NOI18N
        jMenuItem1.setMnemonic('e');
        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setBorder(null);
        jMenu2.setMnemonic('E');
        jMenu2.setText("Edit");
        jMenu2.setToolTipText("");
        jMenu2.setContentAreaFilled(false);
        jMenu2.setPreferredSize(new java.awt.Dimension(30, 19));

        jMenuItemUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/undo.png"))); // NOI18N
        jMenuItemUndo.setMnemonic('u');
        jMenuItemUndo.setText("Undo");
        jMenuItemUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUndoActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemUndo);

        jMenuItemRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/redo.png"))); // NOI18N
        jMenuItemRedo.setMnemonic('r');
        jMenuItemRedo.setText("Redo");
        jMenuItemRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRedoActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemRedo);
        jMenu2.add(jSeparator1);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/editcut.png"))); // NOI18N
        jMenuItem8.setMnemonic('t');
        jMenuItem8.setText("Cut");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/editcopy.png"))); // NOI18N
        jMenuItem9.setMnemonic('c');
        jMenuItem9.setText("Copy");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/editpaste.png"))); // NOI18N
        jMenuItem10.setMnemonic('p');
        jMenuItem10.setText("Paste");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuBar1.add(jMenu2);

        jMenu3.setBorder(null);
        jMenu3.setMnemonic('r');
        jMenu3.setText("Run");
        jMenu3.setContentAreaFilled(false);
        jMenu3.setPreferredSize(new java.awt.Dimension(30, 19));

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/play_green.png"))); // NOI18N
        jMenuItem13.setMnemonic('r');
        jMenuItem13.setText("Run");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem13);

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/play_pause.png"))); // NOI18N
        jMenuItem15.setMnemonic('p');
        jMenuItem15.setText("Pause");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem15);

        jMenuItem16.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        jMenuItem16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/play_step.png"))); // NOI18N
        jMenuItem16.setMnemonic('s');
        jMenuItem16.setText("Step");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem16);

        jMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/play_current.png"))); // NOI18N
        jMenuItem14.setMnemonic('e');
        jMenuItem14.setText("Execute current line");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem14);

        jMenuItem_play_stop.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        jMenuItem_play_stop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/play_stop.png"))); // NOI18N
        jMenuItem_play_stop.setText("Stop");
        jMenuItem_play_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_play_stopActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem_play_stop);

        jMenuItem_play_clear3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        jMenuItem_play_clear3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jasmin/gui/resources/icons/play_clear3.png"))); // NOI18N
        jMenuItem_play_clear3.setText("Reset");
        jMenuItem_play_clear3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_play_clear3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem_play_clear3);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_filesaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_filesaveActionPerformed
        document.quickSave();
    }//GEN-LAST:event_button_filesaveActionPerformed

    private void delaySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_delaySliderStateChanged
        delayText.setText(delaySlider.getValue() + " ms");
    }//GEN-LAST:event_delaySliderStateChanged

    private void button_play_greenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_play_greenMouseEntered
        if(button_play_green.isEnabled()){
            button_play_green.setOpaque(true);
            button_play_green.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_play_greenMouseEntered

    private void button_play_greenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_play_greenMouseExited
        button_play_green.setOpaque(false);
    }//GEN-LAST:event_button_play_greenMouseExited

    private void button_newMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_newMouseEntered
        if(button_new.isEnabled()){
            button_new.setOpaque(true);
            button_new.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_newMouseEntered

    private void button_newMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_newMouseExited
        button_new.setOpaque(false);
    }//GEN-LAST:event_button_newMouseExited

    private void button_fileopenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_fileopenMouseEntered
        if(button_fileopen.isEnabled()){
            button_fileopen.setOpaque(true);
            button_fileopen.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_fileopenMouseEntered

    private void button_fileopenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_fileopenMouseExited
        button_fileopen.setOpaque(false);
    }//GEN-LAST:event_button_fileopenMouseExited

    private void button_filesaveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_filesaveMouseEntered
        if(button_filesave.isEnabled()){
            button_filesave.setOpaque(true);
            button_filesave.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_filesaveMouseEntered

    private void button_filesaveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_filesaveMouseExited
        button_filesave.setOpaque(false);
    }//GEN-LAST:event_button_filesaveMouseExited

    private void button_filesaveasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_filesaveasMouseEntered
        if(button_filesaveas.isEnabled()){
            button_filesaveas.setOpaque(true);
            button_filesaveas.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_filesaveasMouseEntered

    private void button_filesaveasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_filesaveasMouseExited
        button_filesaveas.setOpaque(false);
    }//GEN-LAST:event_button_filesaveasMouseExited

    private void button_undoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_undoMouseEntered
        if(button_undo.isEnabled()){
            button_undo.setOpaque(true);
            button_undo.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_undoMouseEntered

    private void button_undoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_undoMouseExited
        button_undo.setOpaque(false);
        
        if(!button_undo.isEnabled()){
            button_undo.setBackground(Color.decode("#e0e0e0"));
        }
    }//GEN-LAST:event_button_undoMouseExited

    private void button_redoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_redoMouseEntered
        if(button_redo.isEnabled()){
            button_redo.setOpaque(true);
            button_redo.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_redoMouseEntered

    private void button_redoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_redoMouseExited
        button_redo.setOpaque(false);
        
        if(!button_redo.isEnabled()){
            button_redo.setBackground(Color.decode("#e0e0e0"));
        }
    }//GEN-LAST:event_button_redoMouseExited

    private void button_play_stepMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_play_stepMouseEntered
        if(button_play_step.isEnabled()){
            button_play_step.setOpaque(true);
            button_play_step.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_play_stepMouseEntered

    private void button_play_stepMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_play_stepMouseExited
        button_play_step.setOpaque(false);
    }//GEN-LAST:event_button_play_stepMouseExited

    private void button_play_currentMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_play_currentMouseEntered
        if(button_play_current.isEnabled()){
            button_play_current.setOpaque(true);
            button_play_current.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_play_currentMouseEntered

    private void button_play_currentMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_play_currentMouseExited
        button_play_current.setOpaque(false);
    }//GEN-LAST:event_button_play_currentMouseExited

    private void button_play_stopMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_play_stopMouseEntered
        if(button_play_stop.isEnabled()){
            button_play_stop.setOpaque(true);
            button_play_stop.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_play_stopMouseEntered

    private void button_play_stopMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_play_stopMouseExited
        button_play_stop.setOpaque(false);
    }//GEN-LAST:event_button_play_stopMouseExited

    private void button_play_clear3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_play_clear3MouseEntered
        if(button_play_clear3.isEnabled()){
            button_play_clear3.setOpaque(true);
            button_play_clear3.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_play_clear3MouseEntered

    private void button_play_clear3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_play_clear3MouseExited
        button_play_clear3.setOpaque(false);
    }//GEN-LAST:event_button_play_clear3MouseExited

    private void jMenuItem_filesaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_filesaveActionPerformed
        document.quickSave();
    }//GEN-LAST:event_jMenuItem_filesaveActionPerformed

    private void jMenuItem_play_stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_play_stopActionPerformed
        document.pauseProgram();
        //Thread.yield();
        document.data.setInstructionPointer(0);
        document.updateAll();
        checkButtonStates();
    }//GEN-LAST:event_jMenuItem_play_stopActionPerformed

    private void jMenuItem_play_clear3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_play_clear3ActionPerformed
        document.data.clear();
        document.clearAll();
        document.updateAll();
        document.clearErrorLine();
    }//GEN-LAST:event_jMenuItem_play_clear3ActionPerformed

    private void button_editcutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_editcutMouseEntered
        if(button_editcut.isEnabled()){
            button_editcut.setOpaque(true);
            button_editcut.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_editcutMouseEntered

    private void button_editcutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_editcutMouseExited
        button_editcut.setOpaque(false);
        
        if(!button_editcut.isEnabled()){
            button_editcut.setBackground(Color.decode("#e0e0e0"));
        }
    }//GEN-LAST:event_button_editcutMouseExited

    private void button_editcopyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_editcopyMouseEntered
        if(button_editcopy.isEnabled()){
            button_editcopy.setOpaque(true);
            button_editcopy.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_editcopyMouseEntered

    private void button_editcopyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_editcopyMouseExited
        button_editcopy.setOpaque(false);
        
        if(!button_editcopy.isEnabled()){
            button_editcopy.setBackground(Color.decode("#e0e0e0"));
        }
    }//GEN-LAST:event_button_editcopyMouseExited

    private void button_editpasteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_editpasteMouseEntered
        if(button_editpaste.isEnabled()){
            button_editpaste.setOpaque(true);
            button_editpaste.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_editpasteMouseEntered

    private void button_editpasteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_editpasteMouseExited
        button_editpaste.setOpaque(false);
    }//GEN-LAST:event_button_editpasteMouseExited

    private void button_takesnapshotMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_takesnapshotMouseEntered
        if(button_takesnapshot.isEnabled()){
            button_takesnapshot.setOpaque(true);
            button_takesnapshot.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_takesnapshotMouseEntered

    private void button_takesnapshotMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_takesnapshotMouseExited
        button_takesnapshot.setOpaque(false);
    }//GEN-LAST:event_button_takesnapshotMouseExited

    private void button_loadsnapshotMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_loadsnapshotMouseEntered
        if(button_loadsnapshot.isEnabled()){
            button_loadsnapshot.setOpaque(true);
            button_loadsnapshot.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_loadsnapshotMouseEntered

    private void button_loadsnapshotMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_loadsnapshotMouseExited
        button_loadsnapshot.setOpaque(false);
    }//GEN-LAST:event_button_loadsnapshotMouseExited

    private void button_parametersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_parametersMouseEntered
        if(button_parameters.isEnabled()){
            button_parameters.setOpaque(true);
            button_parameters.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_button_parametersMouseEntered

    private void button_parametersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_parametersMouseExited
        button_parameters.setOpaque(false);
    }//GEN-LAST:event_button_parametersMouseExited

    private void button_parametersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_parametersActionPerformed
        // TODO add your handling code here:
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new ParamLinux(frame));
            frame.pack();
            frame.setVisible(true);
        
    }//GEN-LAST:event_button_parametersActionPerformed

    private void buttonStackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonStackMouseEntered
        if(buttonStack.isEnabled()){
            buttonStack.setOpaque(true);
            buttonStack.setBackground(Color.decode("#b3d9ff"));
        }
    }//GEN-LAST:event_buttonStackMouseEntered

    private void buttonStackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonStackMouseExited
        buttonStack.setOpaque(false);
    }//GEN-LAST:event_buttonStackMouseExited

    private void buttonStackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStackActionPerformed
        document.turnStack();
    }//GEN-LAST:event_buttonStackActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItemRedoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemRedoActionPerformed
        document.undoManager.redo();
        checkButtonStates();
    }// GEN-LAST:event_jMenuItemRedoActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItemUndoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemUndoActionPerformed
        document.undoManager.undo();
        checkButtonStates();
    }// GEN-LAST:event_jMenuItemUndoActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_loadsnapshotActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_loadsnapshotActionPerformed
        document.loadFile(snapshot);
        document.resumeSnapshot();
        checkButtonStates();
    }// GEN-LAST:event_button_loadsnapshotActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_takesnapshotActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_takesnapshotActionPerformed

        snapshot = new File(System.getProperty("user.home") + File.separator + "snapshot.asm");
        document.writeFile(snapshot);
        document.takeSnapshot();
        checkButtonStates();
        
    }// GEN-LAST:event_button_takesnapshotActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem18ActionPerformed
        addHelp(getClass().getResource("/jasmin/gui/resources/Configuration.htm"), "Configuration");
    }// GEN-LAST:event_jMenuItem18ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_play_clear3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_play_clear3ActionPerformed
        document.data.clear();
        document.clearAll();
        document.updateAll();
        document.clearErrorLine();
    }// GEN-LAST:event_button_play_clear3ActionPerformed

    public void saveProperties() {
        try {
            File propfile = new File(System.getProperty("user.home") + File.separator + ".jasmin");
            if (!propfile.exists()) {
                propfile.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(propfile);
            try {
                properties.store(out, "Jasmin configuration file");
            } finally {
                out.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.toString());
            System.exit(1);
        }
    }

    /**
     * @param evt the Event that triggered this action
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosing
        this.putProperty("closed_well", 1);
        saveProperties();
        System.exit(0);
    }// GEN-LAST:event_formWindowClosing

    /**
     * @param evt the Event that triggered this action
     */
    private void formWindowClosed(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosed

    }// GEN-LAST:event_formWindowClosed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_forwardActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_forwardActionPerformed
        if (helpDocument != null) {
            helpDocument.forward();
        }
    }// GEN-LAST:event_button_forwardActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_backActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_backActionPerformed
        if (helpDocument != null) {
            helpDocument.back();
        }
    }// GEN-LAST:event_button_backActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void DocTabComponentRemoved(java.awt.event.ContainerEvent evt) {// GEN-FIRST:event_DocTabComponentRemoved
        DocTabStateChanged(null);
    }// GEN-LAST:event_DocTabComponentRemoved

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem17ActionPerformed
        if (document != null) {
            DocTab.remove(document);
        } else if (helpDocument != null) {
            DocTab.remove(helpDocument);
        }
        checkButtonStates();
    }// GEN-LAST:event_jMenuItem17ActionPerformed

    private void DocTabMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_DocTabMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            CloseMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }// GEN-LAST:event_DocTabMouseClicked

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem2ActionPerformed
        if (document != null) {
            DocTab.remove(document);
        } else if (helpDocument != null) {
            DocTab.remove(helpDocument);
        }
        checkButtonStates();
    }// GEN-LAST:event_jMenuItem2ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenu1ActionPerformed
    }// GEN-LAST:event_jMenu1ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem7ActionPerformed
        document.openData();

    }// GEN-LAST:event_jMenuItem7ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem6ActionPerformed
        document.saveData();
    }// GEN-LAST:event_jMenuItem6ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem4ActionPerformed
        save();
    }// GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem3ActionPerformed
        open();
    }// GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_filesaveasActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_filesaveasActionPerformed
        save();
    }// GEN-LAST:event_button_filesaveasActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_fileopenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_fileopenActionPerformed
        open();
    }// GEN-LAST:event_button_fileopenActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem14ActionPerformed
        document.executeCurrentLine();
        checkButtonStates();
    }// GEN-LAST:event_jMenuItem14ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem16ActionPerformed
        document.step();
        checkButtonStates();
    }// GEN-LAST:event_jMenuItem16ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem15ActionPerformed
        document.pauseProgram();
        checkButtonStates();
    }// GEN-LAST:event_jMenuItem15ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem13ActionPerformed
        document.runProgram();
        checkButtonStates();
    }// GEN-LAST:event_jMenuItem13ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem10ActionPerformed
        document.getEditor().paste();
        checkButtonStates();
    }// GEN-LAST:event_jMenuItem10ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem9ActionPerformed
        document.getEditor().copy();
    }// GEN-LAST:event_jMenuItem9ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem8ActionPerformed
        document.getEditor().cut();
    }// GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem5ActionPerformed
        button_newActionPerformed(evt);
    }// GEN-LAST:event_jMenuItem5ActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_play_stopActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_play_stopActionPerformed
        document.pauseProgram();
        //Thread.yield();
        document.data.setInstructionPointer(0);
        document.updateAll();
        checkButtonStates();
    }// GEN-LAST:event_button_play_stopActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_redoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_redoActionPerformed
        document.undoManager.redo();
        checkButtonStates();
    }// GEN-LAST:event_button_redoActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_undoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_undoActionPerformed
        document.undoManager.undo();
        checkButtonStates();
    }// GEN-LAST:event_button_undoActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_editpasteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_editpasteActionPerformed
        document.getEditor().paste();
        checkButtonStates();
    }// GEN-LAST:event_button_editpasteActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_editcopyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_editcopyActionPerformed
        document.getEditor().copy();
        checkButtonStates();
    }// GEN-LAST:event_button_editcopyActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_editcutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_editcutActionPerformed
        document.getEditor().cut();
        checkButtonStates();
    }// GEN-LAST:event_button_editcutActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_play_currentActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_play_currentActionPerformed
        document.executeCurrentLine();
        checkButtonStates();
    }// GEN-LAST:event_button_play_currentActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_play_greenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_play_greenActionPerformed
        if (document.running) {
            document.pauseProgram();
        } else {
            document.runProgram();
        }
        checkButtonStates();
    }// GEN-LAST:event_button_play_greenActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void button_play_stepActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_play_stepActionPerformed
        document.step();
        checkButtonStates();
    }// GEN-LAST:event_button_play_stepActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void DocTabStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_DocTabStateChanged
        Object odoc = DocTab.getSelectedComponent();
        if (odoc != null) {
            if (odoc instanceof JasDocument) {
                document = (JasDocument) odoc;
                this.setTitle("Jasmin - " + document.getTitle());
                DocTab.setTitleAt(DocTab.indexOfComponent(document), document.getTitle());
                helpDocument = null;
            } else if (odoc instanceof HelpBrowser) {
                helpDocument = (HelpBrowser) odoc;
                document = null;
            }
        } else {
            document = null;
            helpDocument = null;
        }

        checkButtonStates();
    }// GEN-LAST:event_DocTabStateChanged

    public void newDocument() {
        addDocument(new JasDocument(ErrorLabel, this, DocTab.getTabCount()));
        helpDocument = null;
    }

    /**
     * @param evt the Event that triggered this action
     */
    private void button_newActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_button_newActionPerformed

        newDocument();
    }// GEN-LAST:event_button_newActionPerformed

    /**
     * @param evt the Event that triggered this action
     */
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed

        formWindowClosing(null);
    }// GEN-LAST:event_jMenuItem1ActionPerformed

    private void addDocument(JasDocument doc) {
        doc.undoManager = new UndoManager() {

            private static final long serialVersionUID = 5579743695599380564L;

            public void undoableEditHappened(UndoableEditEvent e) {

                UndoableEdit ue = e.getEdit();
                if (ue instanceof AbstractDocument.DefaultDocumentEvent) {
                    AbstractDocument.DefaultDocumentEvent ae = (AbstractDocument.DefaultDocumentEvent) ue;
                    if (ae.getType() == DocumentEvent.EventType.CHANGE) {
                        super.addEdit(new NoStyleUndo(ae));
                    } else {
                        super.addEdit(e.getEdit());
                    }

                }
                checkButtonStates();

            }

            public void undo() throws CannotUndoException {
                try {
                    super.undo();
                    checkButtonStates();
                } catch (Exception ex) {

                }
            }

            public void redo() throws CannotRedoException {
                try {
                    super.redo();
                    checkButtonStates();
                } catch (Exception ex) {

                }
            }

        };

        doc.getEditor().getDocument().addUndoableEditListener(doc.undoManager);
        doc.getEditor().addCaretListener(new CaretListener() {

            /**
             * @param e
             */
            public void caretUpdate(CaretEvent e) {
                checkButtonStates();
            }
        });
        doc.undoManager.setLimit(99999);

        DocTab.addTab(doc.getTitle(), doc);
        DocTab.setSelectedIndex(DocTab.getTabCount() - 1);
        helpDocument = null;
        document = doc;
        doc.validate();
        doc.makeLayout();
        // Set focus to the editor (when opened via the tool bar button "New").
        doc.getEditor().requestFocusInWindow();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu CloseMenu;
    private javax.swing.JTabbedPane DocTab;
    private javax.swing.JLabel ErrorLabel;
    private javax.swing.JButton buttonStack;
    private javax.swing.JButton button_editcopy;
    private javax.swing.JButton button_editcut;
    private javax.swing.JButton button_editpaste;
    private javax.swing.JButton button_fileopen;
    private javax.swing.JButton button_filesave;
    private javax.swing.JButton button_filesaveas;
    private javax.swing.JButton button_loadsnapshot;
    private javax.swing.JButton button_new;
    private javax.swing.JButton button_parameters;
    private javax.swing.JButton button_play_clear3;
    private javax.swing.JButton button_play_current;
    private javax.swing.JButton button_play_green;
    private javax.swing.JButton button_play_step;
    private javax.swing.JButton button_play_stop;
    private javax.swing.JButton button_redo;
    private javax.swing.JButton button_takesnapshot;
    private javax.swing.JButton button_undo;
    private javax.swing.JSlider delaySlider;
    private javax.swing.JTextField delayText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItemRedo;
    private javax.swing.JMenuItem jMenuItemUndo;
    private javax.swing.JMenuItem jMenuItem_filesave;
    private javax.swing.JMenuItem jMenuItem_play_clear3;
    private javax.swing.JMenuItem jMenuItem_play_stop;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

}
