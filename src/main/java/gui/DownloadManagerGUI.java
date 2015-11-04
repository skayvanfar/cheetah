package gui;

import controller.DialogAuthenticator;
import enums.ConnectionType;
import enums.DownloadCategory;
import enums.DownloadStatus;
import enums.ProtocolType;
import gui.listener.*;
import gui.preference.PreferenceDialog;
import model.Download;
import model.dto.*;
import model.htmlImpl.HttpDownload;
import model.httpsImpl.HttpsDownload;
import org.apache.commons.io.FilenameUtils;
import utils.*;
import utils.LookAndFeel;

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
public class DownloadManagerGUI extends JFrame implements ActionListener {

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

    // Menu Items
    private JMenuItem exportDataItem;
    private JMenuItem importDataItem;
    private JMenuItem exitItem;
    private JMenuItem prefsItem;

    private JMenuItem addURlItem;
    private JMenuItem openItem;
    private JMenuItem openFolderItem;
    private JMenuItem pauseItem;
    private JMenuItem pauseAllItem;
    private JMenuItem resumeItem;
    private JMenuItem clearItem;
    private JMenuItem clearAllCompletedItem;
    private JMenuItem reJoinItem;
    private JMenuItem reDownloadItem;
    private JMenuItem moveToQueueItem;
    private JMenuItem removeFromQueueItem;
    private JMenuItem propertiesItem;

    private JMenuItem aboutItem;

    // Constructor for Download Manager.
    public DownloadManagerGUI(String name) {
        super(name);
    //    try {
   //         for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
   //             System.out.println(info.getName()); // Metal, Nimbus, CDE/Motif, Windows, Windows Classic, GTK+
   //             if ("Windows".equals(info.getName())) {

      //          } else if ("Nimbus".equals(info.getName())) {
      //              UIManager.setLookAndFeel(info.getClassName());
      //          }

      //          if ("GTK+".equals(info.getName())) {
     //               UIManager.setLookAndFeel(info.getClassName());
       //         }

        //        if ("Nimbus".equals(info.getName())) {
       //             UIManager.setLookAndFeel(info.getClassName());
       //         }
   //         }

            // setTheme(String themeName, String licenseKey, String logoString)
    //        com.jtattoo.plaf.acryl.AcrylLookAndFeel.setTheme("Red", "LPG", "Cheetah");

            // select Look and Feel
       //     UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
    //        UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");


       //     UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
        //    UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");


            //  UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    //    } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
    //        e.printStackTrace();
    //    }

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

        LookAndFeel.setLaf(preferencesDTO.getPreferencesInterfaceDTO().getLookAndFeelName());

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
                        int maxNum = preferencesDTO.getPreferencesConnectionDTO().getMaxConnectionNumber();

                        Download download = null;
                        // todo must set stretegy pattern
                        switch (ProtocolType.valueOfByDesc(textUrl.getProtocol())) {
                            case HTTP:
                                download = new HttpDownload(downloadPanel.getNextDownloadID(), textUrl, downloadName, maxNum,
                                        downloadPathFile, downloadRangeFile, ProtocolType.HTTPS);
                                break;
                            case FTP:
                                // todo must be created ...
                                break;
                            case HTTPS:
                                download = new HttpsDownload(downloadPanel.getNextDownloadID(), textUrl, downloadName, maxNum,
                                        downloadPathFile, downloadRangeFile, ProtocolType.HTTPS);
                                break;
                        }
                        downloadPanel.addDownload(download);
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
                addNewDownloadDialog.onPaste();
            }

            @Override
            public void pauseEventOccured() {
                downloadPanel.actionPause();
                mainToolbar.setStateOfButtonsControl(false, false, false, false, false, true); // canceled
            }

            @Override
            public void resumeEventOccured() {
                downloadPanel.actionResume();
            }

            @Override
            public void pauseAllEventOccured() {
                downloadPanel.actionPauseAll();
            }

            @Override
            public void clearEventOccured() {
                downloadPanel.actionClear();
            }

            @Override
            public void clearAllCompletedEventOccured() {
                downloadPanel.actionClearAllCompleted();
            }

            @Override
            public void reJoinEventOccured() {
                downloadPanel.actionReJoinFileParts();
            }

            @Override
            public void reDownloadEventOccured() {
                downloadPanel.actionReDownload();
            }

            @Override
            public void propertiesEventOccured() {
                downloadPanel.actionProperties();
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

            @Override
            public void downloadSelected(Download download) {
                statusPanel.setStatus(download.getDownloadName());
            }
        });

        preferenceDialog.setPreferencesListener(new PreferencesListener() {
            @Override
            public void preferencesSet(PreferencesDTO preferenceDTO) {
                setPreferences(preferenceDTO);
            }

            @Override
            public void preferenceReset() {
                PreferencesDTO resetPreferencesDTO = getPreferences();
                checkAndSetPreferencesDTO(resetPreferencesDTO);
                preferenceDialog.setPreferencesDTO(resetPreferencesDTO);
                categoryPanel.setTreeModel(resetPreferencesDTO.getPreferencesSaveDTO().getPreferencesDirectoryCategoryDTOs());
            }

            @Override
            public void preferenceDefaults() {
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

        setIconImage(Utils.createIcon(messagesBundle.getString("downloadManagerGUI.mainFrame.iconPath")).getImage());

        setMinimumSize(new Dimension(640, 480));
        // Set window size.
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private void setStateOfMenuItems() {
        resumeItem.setEnabled(false);
        pauseItem.setEnabled(false);
    //    pauseAllButton.setEnabled(true);
        clearItem.setEnabled(false);
   //     clearAllCompletedButton.setEnabled(true);
  //      preferencesButton.setEnabled(true);
        reJoinItem.setEnabled(false);
        reDownloadItem.setEnabled(false);
        propertiesItem.setEnabled(false);
    }

    private PreferencesDTO getPreferences() {

        PreferencesDTO preferencesDTO = null;
        try {
       //   PrefObj.putObject(preferences, "preferenceDTO", new PreferencesDTO());
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

        String path = homeDir + File.separator + "Downloads" + File.separator + "Cheetah Downloaded Files" + File.separator;

        // PreferencesConnectionDTO
        PreferencesConnectionDTO preferencesConnectionDTO = preferencesDTO.getPreferencesConnectionDTO();
        if (preferencesConnectionDTO == null) {
            preferencesConnectionDTO = new PreferencesConnectionDTO();
        }
        if (preferencesConnectionDTO.getConnectionType() == null) {
            preferencesConnectionDTO.setConnectionType(ConnectionType.valueOfByDesc(defaultPreferencesBundle.getString("connectionType")));
        }
        if (preferencesConnectionDTO.getMaxConnectionNumber() == 0) {
            preferencesConnectionDTO.setMaxConnectionNumber(Integer.parseInt(defaultPreferencesBundle.getString("maxConnectionNumber")));
        }
        if (preferencesConnectionDTO.getTimeBetweenAttempts() == 0) {
            preferencesConnectionDTO.setTimeBetweenAttempts(Integer.parseInt(defaultPreferencesBundle.getString("timeBetweenAttempts")));
        }
        if (preferencesConnectionDTO.getMaxNumberAttempts() == 0) {
            preferencesConnectionDTO.setMaxNumberAttempts(Integer.parseInt(defaultPreferencesBundle.getString("maxNumberAttempts")));
        }
        if (preferencesConnectionDTO.getConnectionTimeOut() == 0) {
            preferencesConnectionDTO.setConnectionTimeOut(Integer.parseInt(defaultPreferencesBundle.getString("connectionTimeOut")));
        }
        if (preferencesConnectionDTO.getReadTimeOut() == 0) {
            preferencesConnectionDTO.setReadTimeOut(Integer.parseInt(defaultPreferencesBundle.getString("readTimeOut")));
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

        // PreferenceProxyDTO
        PreferencesProxyDTO preferencesProxyDTO = preferencesDTO.getPreferencesProxyDTO();
        if (preferencesProxyDTO == null) {
            preferencesProxyDTO = new PreferencesProxyDTO();
        }

        if (preferencesProxyDTO.getProxySettingType() == 0) {
            preferencesProxyDTO.setProxySettingType(Integer.parseInt(defaultPreferencesBundle.getString("proxySettingType")));
        }

        if (preferencesProxyDTO.getHttpProxyAddress() == null || preferencesProxyDTO.getHttpProxyAddress().equals("")) {
            preferencesProxyDTO.setHttpProxyAddress(defaultPreferencesBundle.getString("httpProxyAddress"));
        }
        if (preferencesProxyDTO.getHttpProxyPort() != 0) {
            preferencesProxyDTO.setHttpProxyPort(Integer.parseInt(defaultPreferencesBundle.getString("httpProxyPort")));
        }
        if (preferencesProxyDTO.getHttpProxyUserName() == null || preferencesProxyDTO.getHttpProxyUserName().equals("")) {
            preferencesProxyDTO.setHttpProxyUserName(defaultPreferencesBundle.getString("httpProxyUserName"));
        }
        if (preferencesProxyDTO.getHttpProxyPassword() == null || preferencesProxyDTO.getHttpProxyPassword().equals("")) {
            preferencesProxyDTO.setHttpProxyPassword(defaultPreferencesBundle.getString("httpProxyPassword"));
        }

        if (preferencesProxyDTO.getHttpsProxyAddress() == null || preferencesProxyDTO.getHttpsProxyAddress().equals("")) {
            preferencesProxyDTO.setHttpsProxyAddress(defaultPreferencesBundle.getString("httpsProxyAddress"));
        }
        if (preferencesProxyDTO.getHttpsProxyPort() != 0) {
            preferencesProxyDTO.setHttpsProxyPort(Integer.parseInt(defaultPreferencesBundle.getString("httpsProxyPort")));
        }
        if (preferencesProxyDTO.getHttpsProxyUserName() == null || preferencesProxyDTO.getHttpsProxyUserName().equals("")) {
            preferencesProxyDTO.setHttpsProxyUserName(defaultPreferencesBundle.getString("httpsProxyUserName"));
        }
        if (preferencesProxyDTO.getHttpsProxyPassword() == null || preferencesProxyDTO.getHttpsProxyPassword().equals("")) {
            preferencesProxyDTO.setHttpsProxyPassword(defaultPreferencesBundle.getString("httpsProxyPassword"));
        }

        if (preferencesProxyDTO.getFtpProxyAddress() == null || preferencesProxyDTO.getFtpProxyAddress().equals("")) {
            preferencesProxyDTO.setFtpProxyAddress(defaultPreferencesBundle.getString("ftpProxyAddress"));
        }
        if (preferencesProxyDTO.getFtpProxyPort() != 0) {
            preferencesProxyDTO.setFtpProxyPort(Integer.parseInt(defaultPreferencesBundle.getString("ftpProxyPort")));
        }
        if (preferencesProxyDTO.getFtpProxyUserName() == null || preferencesProxyDTO.getFtpProxyUserName().equals("")) {
            preferencesProxyDTO.setFtpProxyUserName(defaultPreferencesBundle.getString("ftpProxyUserName"));
        }
        if (preferencesProxyDTO.getFtpProxyPassword() == null || preferencesProxyDTO.getFtpProxyPassword().equals("")) {
            preferencesProxyDTO.setFtpProxyPassword(defaultPreferencesBundle.getString("ftpProxyPassword"));
        }

        // PreferenceInterfaceDTO
        PreferencesInterfaceDTO preferencesInterfaceDTO = preferencesDTO.getPreferencesInterfaceDTO();
        if (preferencesInterfaceDTO == null) {
            preferencesInterfaceDTO = new PreferencesInterfaceDTO();
        }

        if (preferencesInterfaceDTO.getLookAndFeelName() == null || preferencesInterfaceDTO.getLookAndFeelName().equals("")) {
            preferencesInterfaceDTO.setLookAndFeelName(defaultPreferencesBundle.getString("lookAndFeelName"));
        }


        // add all Main preferences objects
        preferencesDTO.setPreferencesConnectionDTO(preferencesConnectionDTO);
        preferencesDTO.setPreferencesSaveDTO(preferencesSaveDTO);
        preferencesDTO.setPreferencesProxyDTO(preferencesProxyDTO);
        preferencesDTO.setPreferencesInterfaceDTO(preferencesInterfaceDTO);
    }

    private void setPreferences(PreferencesDTO preferenceDTO) {
 //       preferences.putInt("maxConnectionNumber", preferenceDTO.getPreferencesConnectionDTO().getMaxConnectionNumber());
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
                    mainToolbar.setStateOfButtonsControl(true, false, false, false, false, true); // Toolbar Buttons
                    downloadPanel.setStateOfButtonsControl(true, false, false, false, false, true); // Toolbar Buttons
                    setStateOfMenuItemsControl(true, false, false, false, false, true); // MenuItems
                    break;
                case PAUSED:
                    mainToolbar.setStateOfButtonsControl(false, true, true, false, true, true); // last false
                    downloadPanel.setStateOfButtonsControl(false, true, true, false, true, true); // Toolbar Buttons
                    setStateOfMenuItemsControl(false, true, true, false, true, true);
                    break;
                case ERROR:
                    mainToolbar.setStateOfButtonsControl(false, true, true, false, true, true);
                    downloadPanel.setStateOfButtonsControl(false, true, true, false, true, true); // Toolbar Buttons
                    setStateOfMenuItemsControl(false, true, true, false, true, true);
                    break;
                default: // COMPLETE or CANCELLED
                    mainToolbar.setStateOfButtonsControl(false, false, true, true, true, true);
                    downloadPanel.setStateOfButtonsControl(false, false, true, true, true, true); // Toolbar Buttons
                    setStateOfMenuItemsControl(false, false, true, true, true, true);
            }
        } else {
            // No download is selected in table.
            mainToolbar.setStateOfButtonsControl(false, false, false, false, false, false);
            downloadPanel.setStateOfButtonsControl(false, false, false, false, false, false); // Toolbar Buttons
            setStateOfMenuItemsControl(false, false, false, false, false, false);
        }
    }

    private JMenuBar initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        /////////////////////////////////////////////////////////////////////////
        JMenu fileMenu = new JMenu(messagesBundle.getString("downloadManagerGUI.fileMenu.name"));
        exportDataItem = new JMenuItem("Export Data...");
        importDataItem = new JMenuItem("Import Data...");
        exitItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.exitItem.name"));
        exitItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.exitItem.iconPath")))); // NOI18N

        fileMenu.add(exportDataItem);
        fileMenu.add(importDataItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        /////////////////////////////////////////////////////////////////////////
        JMenu windowMenu = new JMenu(messagesBundle.getString("downloadManagerGUI.windowMenu.name"));
        JMenu showMenu = new JMenu(messagesBundle.getString("downloadManagerGUI.showMenu.name"));
        prefsItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.prefsItem.name"));
        prefsItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.prefsItem.iconPath"))));


        JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem(messagesBundle.getString("downloadManagerGUI.showFormItem.name"));
        showFormItem.setSelected(true);

        showMenu.add(showFormItem);
        windowMenu.add(showMenu);
        windowMenu.add(prefsItem);

        exportDataItem.addActionListener(this);
        importDataItem.addActionListener(this);
        exitItem.addActionListener(this);
        prefsItem.addActionListener(this);

        /////////////////////////////////////////////////////////////////////////
        JMenu downloadsMenu = new JMenu(messagesBundle.getString("downloadManagerGUI.downloadsMenu.name"));
        addURlItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.addURlItem.name"));
        addURlItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.addURlItem.iconPath"))));
        openItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.openItem.name"));
        openFolderItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.openFolderItem.name"));
        resumeItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.resumeItem.name"));
        resumeItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.resumeItem.iconPath"))));
        pauseItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.pauseItem.name"));
        pauseItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.pauseItem.iconPath"))));
        pauseAllItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.pauseAllItem.name"));
        pauseAllItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.pauseAllItem.iconPath"))));
        clearItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.clearItem.name"));
        clearItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.clearItem.iconPath"))));
        clearAllCompletedItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.clearAllCompletedItem.name"));
        clearAllCompletedItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.clearAllCompletedItem.iconPath"))));
        reJoinItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.reJoinItem.name"));
        reJoinItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.reJoinItem.iconPath"))));
        reDownloadItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.reDownloadItem.name"));
        reDownloadItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.reDownloadItem.iconPath"))));

        moveToQueueItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.moveToQueueItem.name"));

        removeFromQueueItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.removeFromQueueItem.name"));
        propertiesItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.propertiesItem.name"));
        propertiesItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.propertiesItem.iconPath"))));

        downloadsMenu.add(addURlItem);
        downloadsMenu.add(new JSeparator());
        downloadsMenu.add(openItem);
        downloadsMenu.add(openFolderItem);
        downloadsMenu.add(new JSeparator());
        downloadsMenu.add(resumeItem);
        downloadsMenu.add(pauseItem);
        downloadsMenu.add(pauseAllItem);
        downloadsMenu.add(new JSeparator());
        downloadsMenu.add(clearItem);
        downloadsMenu.add(clearAllCompletedItem);
        downloadsMenu.add(new JSeparator());
        downloadsMenu.add(reJoinItem);
        downloadsMenu.add(reDownloadItem);
        downloadsMenu.add(new JSeparator());
        downloadsMenu.add(moveToQueueItem);
        downloadsMenu.add(removeFromQueueItem);
        downloadsMenu.add(new JSeparator());
        downloadsMenu.add(propertiesItem);

        addURlItem.addActionListener(this);
        openItem.addActionListener(this);
        openFolderItem.addActionListener(this);
        resumeItem.addActionListener(this);
        pauseItem.addActionListener(this);
        pauseAllItem.addActionListener(this);
        clearItem.addActionListener(this);
        clearAllCompletedItem.addActionListener(this);
        reDownloadItem.addActionListener(this);
        moveToQueueItem.addActionListener(this);
        removeFromQueueItem.addActionListener(this);
        propertiesItem.addActionListener(this);

        setStateOfMenuItems();

        /////////////////////////////////////////////////////////////////////////
        JMenu helpMenu = new JMenu(messagesBundle.getString("downloadManagerGUI.helpMenu.name"));
        aboutItem = new JMenuItem(messagesBundle.getString("downloadManagerGUI.aboutItem.name"));
        aboutItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(messagesBundle.getString("downloadManagerGUI.aboutItem.iconPath"))));

        helpMenu.add(aboutItem);

        aboutItem.addActionListener(this);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);
        menuBar.add(downloadsMenu);
        menuBar.add(helpMenu);

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

    private void setStateOfMenuItemsControl(boolean pauseState, boolean resumeState, boolean clearState, boolean reJoinState, boolean reDownloadState, boolean propertiesState) {
        pauseItem.setEnabled(pauseState);
        resumeItem.setEnabled(resumeState);
        clearItem.setEnabled(clearState);
        reJoinItem.setEnabled(reJoinState);
        reDownloadItem.setEnabled(reDownloadState);
        propertiesItem.setEnabled(propertiesState);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem clicked= (JMenuItem) e.getSource();

        if (clicked == exportDataItem) {
        //    addNewDownloadDialog.setVisible(true);
        } else if (clicked == importDataItem) {
        //    addNewDownloadDialog.setVisible(true);
        } else if (clicked == exitItem) {
            WindowListener[]  listeners = getWindowListeners();
            for (WindowListener listener : listeners)
                listener.windowClosing(new WindowEvent(DownloadManagerGUI.this, 0));
        } else if (clicked == prefsItem) {
            preferenceDialog.setVisible(true);
        } else if (clicked == addURlItem) {
            addNewDownloadDialog.setVisible(true);
            addNewDownloadDialog.onPaste();
        } else if (clicked == openItem) {
            downloadPanel.actionOpenFile();
        } else if (clicked == openFolderItem) {
            downloadPanel.actionOpenFolder();
        } else if (clicked == resumeItem) {
            downloadPanel.actionResume();
        } else if (clicked == pauseItem) {
            downloadPanel.actionPause();
            mainToolbar.setStateOfButtonsControl(false, false, false, false, false, true); // canceled
        } else if (clicked == pauseAllItem) {
            int action = JOptionPane.showConfirmDialog(DownloadManagerGUI.this, "Do you realy want to pause all downloads?", "Confirm pause all", JOptionPane.OK_CANCEL_OPTION); ////***********
            if (action == JOptionPane.OK_OPTION) {
                downloadPanel.actionPauseAll();
            }
        } else if (clicked == clearItem) {
            downloadPanel.actionClear();
        } else if (clicked == clearAllCompletedItem) {
            downloadPanel.actionClearAllCompleted();
        } else if (clicked == reJoinItem) {
            downloadPanel.actionReJoinFileParts();
        } else if (clicked == reDownloadItem) {
            downloadPanel.actionReDownload();
        } else if (clicked == moveToQueueItem) {
            //        mainToolbarListener.preferencesEventOccured();
        } else if (clicked == removeFromQueueItem) {
            //        mainToolbarListener.preferencesEventOccured();
        } else if (clicked == propertiesItem) {
            downloadPanel.actionProperties();
        } else if (clicked == aboutItem) {
            if (!aboutDialog.isVisible())
                aboutDialog.setVisible(true);
        }
    }
}
