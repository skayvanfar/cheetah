package gui;

import controller.DialogAuthenticator;
import enums.DownloadCategory;
import enums.DownloadStatus;
import enums.ProtocolType;
import gui.listener.*;
import gui.preference.PreferenceDialog;
import model.dto.PreferenceConnectionDTO;
import model.dto.PreferencesDTO;
import model.dto.PreferencesDirectoryCategoryDTO;
import model.dto.PreferencesSaveDTO;
import model.htmlImpl.HttpDownload;
import org.apache.commons.io.FilenameUtils;
import utils.ConnectionUtil;
import utils.PrefObj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InvalidClassException;
import java.net.Authenticator;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Created by Saeed Kayvanfar on 9/10/2015.
 */

// implements Observer
public class DownloadManagerGUI extends JFrame {

    private final ResourceBundle messagesBundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N
    private final ResourceBundle defaultPreferencesBundle = java.util.ResourceBundle.getBundle("defaultPreferences"); // NOI18N

    private MainToolBar mainToolbar;
    private CategoryPanel categoryPanel;
    private DownloadPanel downloadPanel;
    private MessagePanel messagePanel;
    private JSplitPane mainSplitPane;
    private StatusPanel statusPanel;
    private AddNewDownloadDialog addNewDownloadDialog;
    private PreferenceDialog preferenceDialog;
    private Preferences preferences;
    private AboutDialog aboutDialog;

    // Constructor for Download Manager.
    public DownloadManagerGUI(String name) {
        super(name);
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName()); // Metal, Nimbus, CDE/Motif, Windows, Windows Classic, GTK+
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
      //          } else if ("Nimbus".equals(info.getName())) {
      //              UIManager.setLookAndFeel(info.getClassName());
                }

                if ("GTK+".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                }

        //        if ("Nimbus".equals(info.getName())) {
       //             UIManager.setLookAndFeel(info.getClassName());
       //         }
            }

            // setTheme(String themeName, String licenseKey, String logoString)
    //        com.jtattoo.plaf.acryl.AcrylLookAndFeel.setTheme("Red", "LPG", "Chita");

            // select Look and Feel
       //     UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
    //        UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");


       //     UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
        //    UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");


            //  UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        preferences = Preferences.userRoot().node("db");
        final PreferencesDTO preferencesDTO = getPreferences();

        try {
            PreferencesDTO preferencesDTOTest = (PreferencesDTO) PrefObj.getObject(preferences, "preferenceDTO");
        } catch (ClassCastException e) {
            try {
                PrefObj.putObject(preferences, "preferenceDTO", new PreferencesDTO());
            } catch (IOException | ClassNotFoundException | BackingStoreException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException | BackingStoreException | IOException e) {
            e.printStackTrace();
        }

        createFileHierarchy();

        mainToolbar = new MainToolBar();
        categoryPanel = new CategoryPanel(preferencesDTO.getPreferencesSaveDTO().getPreferencesDirectoryCategoryDTOs());
        downloadPanel = new DownloadPanel(this, preferencesDTO.getPreferencesSaveDTO().getDatabasePath());
        messagePanel = new MessagePanel();
        JTabbedPane mainTabPane = new JTabbedPane();
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoryPanel, mainTabPane);
        mainSplitPane.setOneTouchExpandable(true);
        statusPanel = new StatusPanel();
        addNewDownloadDialog = new AddNewDownloadDialog(this);

        mainTabPane.addTab(messagesBundle.getString("downloadManagerGUI.mainTabPane.downloadPanel"), downloadPanel);
        mainTabPane.addTab(messagesBundle.getString("downloadManagerGUI.mainTabPane.messagePanel"), messagePanel);

        preferenceDialog = new PreferenceDialog(this, preferencesDTO);

        aboutDialog = new AboutDialog(this);

        categoryPanel.setCategoryPanelListener(new CategoryPanelListener() {
            @Override
            public void categoryNodeSelected(List<String> fileExtensions, DownloadCategory downloadCategory) {
                downloadPanel.setDownloadsByDownloadPath(fileExtensions, downloadCategory);
            }
        });

   //     preferenceDialog.setDefaults(preferencesDTO);

        addNewDownloadDialog.setAddNewDownloadListener(new AddNewDownloadListener() {
            @Override
            public void newDownloadEventOccured(URL textUrl) {
                if (textUrl != null) {
                    try {
                        String downloadName = ConnectionUtil.getRealFileName(textUrl);
                        String fileExtension =  FilenameUtils.getExtension(downloadName);
                        File downloadPathFile = new File(preferencesDTO.getPreferencesSaveDTO().getPathByFileExtension(fileExtension));
                        File downloadRangeFile = new File(preferencesDTO.getPreferencesSaveDTO().getTempDirectory());
                        int maxNum = preferencesDTO.getPreferenceConnectionDTO().getMaxConnectionNumber();

                        // todo must set stretegy pattern
                        downloadPanel.addDownload(new HttpDownload(downloadPanel.getNextDownloadID(), textUrl, downloadName, maxNum,
                                downloadPathFile, downloadRangeFile, ProtocolType.HTTP));
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(DownloadManagerGUI.this, "Invalid Download URL", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });

        // Add panels to display.
        add(mainToolbar, BorderLayout.PAGE_START);
        add(mainSplitPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.PAGE_END);

        setJMenuBar(initMenuBar());


        mainToolbar.setMainToolbarListener(new MainToolbarListener() {
            @Override
            public void newDownloadEventOccured() {
                addNewDownloadDialog.setVisible(true);
            }

            @Override
            public void pauseEventOccured() {
                pauseDownload();
            }

            @Override
            public void resumeEventOccured() {
                downloadPanel.actionResume();
            }

            @Override
            public void pauseAllEventOccured() {
                pauseAllDownloads();
            }

            @Override
            public void clearEventOccured() {
                clearDownload();
            }

            @Override
            public void clearAllCompletedEventOccured() {
                clearAllCompletedDownloads();
            }

            @Override
            public void preferencesEventOccured() {
                preferenceDialog.setVisible(true);
            }
        });

        downloadPanel.setDownloadPanelListener(new DownloadPanelListener() {
            @Override
            public void stateChangedEventOccured(DownloadStatus downloadState) {
                updateButtons(downloadState);
            }
        });

        preferenceDialog.setPreferencesListener(new PreferencesListener() {
            @Override
            public void preferencesSet(PreferencesDTO preferenceDTO) {
                setPreferences(preferenceDTO);
            }

            @Override
            public void preferenceReset() {
                PreferencesDTO defaultPreferenceDTO = new PreferencesDTO();
                checkAndSetPreferencesDTO(defaultPreferenceDTO);
                preferenceDialog.setPreferencesDTO(defaultPreferenceDTO);
                categoryPanel.setTreeModel(defaultPreferenceDTO.getPreferencesSaveDTO().getPreferencesDirectoryCategoryDTOs());
            }
        });

        // Handle window closing events.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                int action = JOptionPane.showConfirmDialog(DownloadManagerGUI.this,
                        "Do you realy want to exit the application?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);

                if (action == JOptionPane.OK_OPTION) {
                    System.out.println("Window Closing");
                    downloadPanel.actionPauseAll();
                    dispose();
                    System.gc();
                }
            }
        });

        Authenticator.setDefault(new DialogAuthenticator(this));

        setMinimumSize(new Dimension(640, 480));
        // Set window size.
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private PreferencesDTO getPreferences() {

        PreferencesDTO preferencesDTO = null;
        try {
      //      PrefObj.putObject(preferences, "preferenceDTO", new PreferencesDTO());
            try {
                preferencesDTO = (PreferencesDTO) PrefObj.getObject(preferences, "preferenceDTO"); // todo must find a way to delete preferenceDTO from OS
            } catch(NullPointerException | EOFException e) {
                PrefObj.putObject(preferences, "preferenceDTO", new PreferencesDTO());
            }


            checkAndSetPreferencesDTO(preferencesDTO);
            setPreferences(preferencesDTO);
        } catch (IOException | BackingStoreException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return preferencesDTO;
    }

    private void checkAndSetPreferencesDTO(PreferencesDTO preferencesDTO) {
        String homeDir = System.getProperty("user.home");

        String path = homeDir + File.separator + "Downloads" + File.separator + "Chita Downloaded Files" + File.separator;

        // PreferenceConnectionDTO
        PreferenceConnectionDTO preferenceConnectionDTO = preferencesDTO.getPreferenceConnectionDTO();
        if (preferenceConnectionDTO == null) {
            preferenceConnectionDTO = new PreferenceConnectionDTO();
        }
        if (preferenceConnectionDTO.getMaxConnectionNumber() == 0) {
            preferenceConnectionDTO.setMaxConnectionNumber(Integer.parseInt(defaultPreferencesBundle.getString("maxConnectionNumber")));
        }

        // preferencesSaveDTO
        PreferencesSaveDTO preferencesSaveDTO = preferencesDTO.getPreferencesSaveDTO();
        if (preferencesSaveDTO == null) {
            preferencesSaveDTO = new PreferencesSaveDTO();
        }
        if (preferencesSaveDTO.getPreferencesDirectoryCategoryDTOs() == null) { // or empty ????
            List<PreferencesDirectoryCategoryDTO> preferencesDirectoryCategoryDTOs = new ArrayList<>();

            // preferencesCompressedDirCategoryDTO
            PreferencesDirectoryCategoryDTO preferencesCompressedDirCategoryDTO = new PreferencesDirectoryCategoryDTO();

            preferencesCompressedDirCategoryDTO.setDirectoryName(defaultPreferencesBundle.getString("compressedDirectoryCategory.directoryName"));
            preferencesCompressedDirCategoryDTO.setPath(path + defaultPreferencesBundle.getString("compressedDirectoryCategory.path"));
            String [] fileCompressedExtensions = defaultPreferencesBundle.getString("compressedDirectoryCategory.fileExtensions").split(" ");
            preferencesCompressedDirCategoryDTO.setFileExtensions(fileCompressedExtensions);
            preferencesCompressedDirCategoryDTO.setIconPath(defaultPreferencesBundle.getString("compressedDirectoryCategory.icon"));
            preferencesDirectoryCategoryDTOs.add(preferencesCompressedDirCategoryDTO);


            PreferencesDirectoryCategoryDTO preferencesDocumentDirCategoryDTO = new PreferencesDirectoryCategoryDTO();

            preferencesDocumentDirCategoryDTO.setDirectoryName(defaultPreferencesBundle.getString("documentDirectoryCategory.directoryName"));
            preferencesDocumentDirCategoryDTO.setPath(path + defaultPreferencesBundle.getString("documentDirectoryCategory.path"));
            String [] fileDocumentExtensions = defaultPreferencesBundle.getString("documentDirectoryCategory.fileExtensions").split(" ");
            preferencesDocumentDirCategoryDTO.setFileExtensions(fileDocumentExtensions);
            preferencesDocumentDirCategoryDTO.setIconPath(defaultPreferencesBundle.getString("documentDirectoryCategory.icon"));
            preferencesDirectoryCategoryDTOs.add(preferencesDocumentDirCategoryDTO);


            PreferencesDirectoryCategoryDTO preferencesMusicDirCategoryDTO = new PreferencesDirectoryCategoryDTO();

            preferencesMusicDirCategoryDTO.setDirectoryName(defaultPreferencesBundle.getString("musicDirectoryCategory.directoryName"));
            preferencesMusicDirCategoryDTO.setPath(path + defaultPreferencesBundle.getString("musicDirectoryCategory.path"));
            String [] fileMusicExtensions = defaultPreferencesBundle.getString("musicDirectoryCategory.fileExtensions").split(" ");
            preferencesMusicDirCategoryDTO.setFileExtensions(fileMusicExtensions);
            preferencesMusicDirCategoryDTO.setIconPath(defaultPreferencesBundle.getString("musicDirectoryCategory.icon"));
            preferencesDirectoryCategoryDTOs.add(preferencesMusicDirCategoryDTO);


            PreferencesDirectoryCategoryDTO preferencesProgramDirCategoryDTO = new PreferencesDirectoryCategoryDTO();

            preferencesProgramDirCategoryDTO.setDirectoryName(defaultPreferencesBundle.getString("programDirectoryCategory.directoryName"));
            preferencesProgramDirCategoryDTO.setPath(path + defaultPreferencesBundle.getString("programDirectoryCategory.path"));
            String [] fileProgramExtensions = defaultPreferencesBundle.getString("programDirectoryCategory.fileExtensions").split(" ");
            preferencesProgramDirCategoryDTO.setFileExtensions(fileProgramExtensions);
            preferencesProgramDirCategoryDTO.setIconPath(defaultPreferencesBundle.getString("programDirectoryCategory.icon"));
            preferencesDirectoryCategoryDTOs.add(preferencesProgramDirCategoryDTO);


            PreferencesDirectoryCategoryDTO preferencesVideoDirCategoryDTO = new PreferencesDirectoryCategoryDTO();

            preferencesVideoDirCategoryDTO.setDirectoryName(defaultPreferencesBundle.getString("videoDirectoryCategory.directoryName"));
            preferencesVideoDirCategoryDTO.setPath(path + defaultPreferencesBundle.getString("videoDirectoryCategory.path"));
            String [] fileVideoExtensions = defaultPreferencesBundle.getString("videoDirectoryCategory.fileExtensions").split(" ");
            preferencesVideoDirCategoryDTO.setFileExtensions(fileVideoExtensions);
            preferencesVideoDirCategoryDTO.setIconPath(defaultPreferencesBundle.getString("videoDirectoryCategory.icon"));
            preferencesDirectoryCategoryDTOs.add(preferencesVideoDirCategoryDTO);

            PreferencesDirectoryCategoryDTO preferencesImageDirCategoryDTO = new PreferencesDirectoryCategoryDTO();

            preferencesImageDirCategoryDTO.setDirectoryName(defaultPreferencesBundle.getString("imageDirectoryCategory.directoryName"));
            preferencesImageDirCategoryDTO.setPath(path + defaultPreferencesBundle.getString("imageDirectoryCategory.path"));
            String [] fileImageExtensions = defaultPreferencesBundle.getString("imageDirectoryCategory.fileExtensions").split(" ");
            preferencesImageDirCategoryDTO.setFileExtensions(fileImageExtensions);
            preferencesImageDirCategoryDTO.setIconPath(defaultPreferencesBundle.getString("imageDirectoryCategory.icon"));
            preferencesDirectoryCategoryDTOs.add(preferencesImageDirCategoryDTO);


            preferencesSaveDTO.setPreferencesDirectoryCategoryDTOs(preferencesDirectoryCategoryDTOs);

            // todo other needed ... use other method
        }
        if (preferencesSaveDTO.getTempDirectory() == null || preferencesSaveDTO.getTempDirectory().equals("")) {
            preferencesSaveDTO.setTempDirectory(path + defaultPreferencesBundle.getString("tempDirectory"));
        }
        if (preferencesSaveDTO.getDatabasePath() == null || preferencesSaveDTO.getDatabasePath().equals("")) {
            preferencesSaveDTO.setDatabasePath(path + defaultPreferencesBundle.getString("databasePath"));
        }


        // add all Main preferences objects
        preferencesDTO.setPreferenceConnectionDTO(preferenceConnectionDTO);
        preferencesDTO.setPreferencesSaveDTO(preferencesSaveDTO);
    }

    private void setPreferences(PreferencesDTO preferenceDTO) {
 //       preferences.putInt("maxConnectionNumber", preferenceDTO.getPreferenceConnectionDTO().getMaxConnectionNumber());
        try {
            PrefObj.putObject(preferences, "preferenceDTO", preferenceDTO);
        } catch (IOException | BackingStoreException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* Update each button's state based off of the
       currently selected download's status. */
    private void updateButtons(DownloadStatus state) {
        if (state != null) {
            switch (state) {
                case DOWNLOADING:
                    mainToolbar.setStateOfButtonsControl(true, false, false);
                    break;
                case PAUSED:
                    mainToolbar.setStateOfButtonsControl(false, true, true); // last false
                    break;
                case ERROR:
                    mainToolbar.setStateOfButtonsControl(false, true, true);
                    break;
                default: // COMPLETE or CANCELLED
                    mainToolbar.setStateOfButtonsControl(false, false, true);
            }
        } else {
            // No download is selected in table.
            mainToolbar.setStateOfButtonsControl(false, false, false);
        }
    }

    private JMenuBar initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        /////////////////////////////////////////////////////////////////////////
        JMenu fileMenu = new JMenu(messagesBundle.getString("downloadManagerGUI.fileMenu.name"));
        JMenuItem exportDataItem = new JMenuItem("Export Data...");
        JMenuItem importDataItem = new JMenuItem("Import Data...");
        JMenuItem exitItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.exitItem.name"));

        exitItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/primo48/others/button_cancel.png"))); // NOI18N

        fileMenu.add(exportDataItem);
        fileMenu.add(importDataItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        /////////////////////////////////////////////////////////////////////////
        JMenu windowMenu = new JMenu(messagesBundle.getString("downloadManagerGUI.windowMenu.name"));
        JMenu showMenu = new JMenu(messagesBundle.getString("downloadManagerGUI.showMenu.name"));
        JMenuItem prefsItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.prefsItem.name"));

        JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem(messagesBundle.getString("downloadManagerGUI.showFormItem.name"));
        showFormItem.setSelected(true);

        showMenu.add(showFormItem);
        windowMenu.add(showMenu);
        windowMenu.add(prefsItem);

        /////////////////////////////////////////////////////////////////////////
        JMenu downloadsMenu = new JMenu(messagesBundle.getString("downloadManagerGUI.downloadsMenu.name"));
        JMenuItem addURlItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.addURlItem.name"));
        JMenuItem resumeItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.resumeItem.name"));
        JMenuItem pauseItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.pauseItem.name"));
        JMenuItem pauseAllItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.pauseAllItem.name"));
        JMenuItem clearItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.clearItem.name"));
        JMenuItem clearAllCompletedItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.clearAllCompletedItem.name"));

        downloadsMenu.add(addURlItem);
        downloadsMenu.add(resumeItem);
        downloadsMenu.add(pauseItem);
        downloadsMenu.add(pauseAllItem);
        downloadsMenu.add(clearItem);
        downloadsMenu.add(clearAllCompletedItem);

        /////////////////////////////////////////////////////////////////////////
        JMenu helpMenu = new JMenu(messagesBundle.getString("downloadManagerGUI.helpMenu.name"));
        JMenuItem aboutItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.aboutItem.name"));

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);
        menuBar.add(downloadsMenu);
        menuBar.add(helpMenu);

        prefsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                preferenceDialog.setVisible(true);
            }
        });

        showFormItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)ev.getSource();

                if (menuItem.isSelected()) {
                    mainSplitPane.setDividerLocation((int) categoryPanel.getMinimumSize().getWidth());
                }

                categoryPanel.setVisible(menuItem.isSelected());
            }
        });

        fileMenu.setMnemonic(KeyEvent.VK_F);
        exitItem.setMnemonic(KeyEvent.VK_X);

        prefsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));

        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

        importDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));

    ///    importDataItem.addActionListener(new  ActionListener() {
    //        public void actionPerformed(ActionEvent arg0) {
    //            if(fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
    //                try {
   //                     controller.loadFromFile(fileChooser.getSelectedFile());
   //                     tablePanel.refresh();
   //                 } catch (IOException e) {
   //                     JOptionPane.showMessageDialog(MainFrame.this, "Could not load data from file.", "Error", JOptionPane.ERROR_MESSAGE);
   //                 }
   //             }
   //         }
   //     });

   //     exportDataItem.addActionListener(new ActionListener() {
     //       public void actionPerformed(ActionEvent arg0) {
     //           if(fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
     //               try {
       //                 controller.saveToFile(fileChooser.getSelectedFile());
      //              } catch (IOException e) {
     //                   JOptionPane.showMessageDialog(MainFrame.this, "Could not save data from file.", "Error", JOptionPane.ERROR_MESSAGE);
     //               }
     //           }
    //        }
    //    });

        //////////////////////////////////////////////////////////////////////// Downloads Actions
        addURlItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewDownloadDialog.setVisible(true);
            }
        });

        resumeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadPanel.actionResume();
            }
        });

        pauseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseDownload();
            }
        });

        pauseAllItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseAllDownloads();
            }
        });

        clearItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearDownload();
            }
        });

        clearAllCompletedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllCompletedDownloads();
            }
        });


        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                WindowListener[]  listeners = getWindowListeners();

                for (WindowListener listener : listeners) {
                    listener.windowClosing(new WindowEvent(DownloadManagerGUI.this, 0));
                }
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!aboutDialog.isVisible())
                    aboutDialog.setVisible(true);
            }
        });

        return menuBar;
    }

    private void createFileHierarchy() {
        PreferencesDTO preferencesDTO;
        try {
            preferencesDTO = (PreferencesDTO) PrefObj.getObject(preferences, "preferenceDTO");

            List<PreferencesDirectoryCategoryDTO> preferencesDirectoryCategoryDTOs = preferencesDTO.getPreferencesSaveDTO().getPreferencesDirectoryCategoryDTOs();
            for (PreferencesDirectoryCategoryDTO preferencesDirectoryCategoryDTO : preferencesDirectoryCategoryDTOs) {
                File dirFile = new File(preferencesDirectoryCategoryDTO.getPath());
                if (!dirFile.exists())
                    dirFile.mkdirs();
            }
            String tempDirectory = preferencesDTO.getPreferencesSaveDTO().getTempDirectory();
            File tempDirFile = new File(tempDirectory);
            if (!tempDirFile.exists())
                tempDirFile.mkdirs();
            String databasePath = preferencesDTO.getPreferencesSaveDTO().getDatabasePath();
            File databasePathFile = new File(databasePath);
            if (!databasePathFile.exists())
                databasePathFile.mkdirs();
        } catch (InvalidClassException e) {
            try {
                PrefObj.putObject(preferences, "preferenceDTO", new PreferencesDTO());
            } catch (IOException | BackingStoreException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(DownloadManagerGUI.this, "Please run program again", "Just one time", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | BackingStoreException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException se) {
            JOptionPane.showMessageDialog(DownloadManagerGUI.this, "Unable to create necessary directories. may be not have write permission, please restart program", "Directories creation problem", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pauseDownload() {
        downloadPanel.actionPause();
        mainToolbar.setStateOfButtonsControl(false, false, false); // canceled
    }

    private void pauseAllDownloads() {
        int action = JOptionPane.showConfirmDialog(DownloadManagerGUI.this, "Do you realy want to pause all downloads?", "Confirm pause all", JOptionPane.OK_CANCEL_OPTION); ////***********
        if (action == JOptionPane.OK_OPTION) {
            downloadPanel.actionPauseAll();
        }
    }

    private void clearDownload() {
        int action = JOptionPane.showConfirmDialog(DownloadManagerGUI.this, "Do you realy want to delete selected file?", "Confirm delete", JOptionPane.OK_CANCEL_OPTION);
        if (action == JOptionPane.OK_OPTION) {
            downloadPanel.actionClear();
        }
    }

    private void clearAllCompletedDownloads() {
        int action = JOptionPane.showConfirmDialog(DownloadManagerGUI.this, "Do you realy want to delete all completed files?", "Confirm delete all", JOptionPane.OK_CANCEL_OPTION);
        if (action == JOptionPane.OK_OPTION) {
            downloadPanel.actionClearAllCompleted();
        }
    }
}
