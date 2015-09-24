package gui;

import enums.DownloadStatus;
import gui.listener.AddNewDownloadListener;
import gui.listener.DownloadPanelListener;
import gui.listener.MainToolbarListener;
import gui.listener.PreferencesListener;
import gui.preference.PreferenceDialog;
import model.Download;
import model.dto.PreferenceConnectionDTO;
import model.dto.PreferencesDTO;
import model.dto.PreferencesDirectoryCategoryDTO;
import model.dto.PreferencesSaveDTO;
import utils.PrefObj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
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

    private ResourceBundle bundle = java.util.ResourceBundle.getBundle("defaultPreferences"); // NOI18N

    private MainToolBar mainToolbar;
    private CategoryPanel categoryPanel;
    private JTabbedPane mainTabPane;
    private DownloadPanel downloadPanel;
    private MessagePanel messagePanel;
    private JSplitPane mainSplitPane;
    private StatusPanel statusPanel;
    private AddNewDownloadDialog addNewDownloadDialog;
    private PreferenceDialog preferenceDialog;
    private Preferences preferences;

    // Constructor for Download Manager.
    public DownloadManagerGUI(String name) {
        super(name);
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName()); // Metal, Nimbus, CDE/Motif, Windows, Windows Classic
      //          if ("Windows".equals(info.getName())) {
      //              UIManager.setLookAndFeel(info.getClassName());
      //          } else if ("Nimbus".equals(info.getName())) {
      //              UIManager.setLookAndFeel(info.getClassName());
      //          }

                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                }
            }

            // setTheme(String themeName, String licenseKey, String logoString)
    //        com.jtattoo.plaf.acryl.AcrylLookAndFeel.setTheme("Red", "LPG", "Chita");

            // select Look and Feel
       //     UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
    //        UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");


       //     UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
        //    UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");


            //  UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        mainToolbar = new MainToolBar();
        categoryPanel = new CategoryPanel();
        downloadPanel = new DownloadPanel(this);
        messagePanel = new MessagePanel();
        mainTabPane = new JTabbedPane();
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoryPanel, mainTabPane);
        mainSplitPane.setOneTouchExpandable(true);
        statusPanel = new StatusPanel();
        addNewDownloadDialog = new AddNewDownloadDialog(this);

        mainTabPane.addTab("Download Panel", downloadPanel);
        mainTabPane.addTab("Messages", messagePanel);

        preferenceDialog = new PreferenceDialog(this);

        preferences = Preferences.userRoot().node("db");

        addNewDownloadDialog.setAddNewDownloadListener(new AddNewDownloadListener() {
            @Override
            public void newDownloadEventOccured(URL textUrl) {
                if (textUrl != null) {
                    downloadPanel.addDownload(new Download(textUrl, preferences.getInt("maxConnectionNumber", Integer.parseInt(bundle.getString("maxConnectionNumber")))));
                }
            }
        });

        // Add panels to display.
        add(mainToolbar, BorderLayout.PAGE_START);
        add(mainSplitPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.PAGE_END);

        setJMenuBar(initMenuBar());

        createFileHierarchy();

        mainToolbar.setMainToolbarListener(new MainToolbarListener() {
            @Override
            public void newDownloadEventOccured() {
                addNewDownloadDialog.setVisible(true);
            }

            @Override
            public void pauseEventOccured() {
                downloadPanel.actionPause();
                mainToolbar.setStateOfButtonsControl(false, false, false, false); // canceled
            }

            @Override
            public void resumeEventOccured() {
                downloadPanel.actionResume();
          //      updateButtons();
            }

            @Override
            public void cancelEventOccured() {
                downloadPanel.actionCancel();
           //     updateButtons();
            }

            @Override
            public void clearEventOccured() {
                downloadPanel.actionClear();
           //     updateButtons();
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
                try {
                    setPreferences(preferenceDTO);
                } catch (BackingStoreException e) {
                    e.printStackTrace(); //todo use JOptionPane
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            preferenceDialog.setDefaults(getPreferences());
        } catch (BackingStoreException e) {
            e.printStackTrace(); // todo
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Handle window closing events.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.out.println("Window Closing");
                downloadPanel.cancelAllDownload();
                dispose();
                System.gc();
            }
        });

        setMinimumSize(new Dimension(640, 480));
        // Set window size.
        setSize(740, 480);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private PreferencesDTO getPreferences() throws BackingStoreException, IOException, ClassNotFoundException {

     //   PrefObj.putObject(preferences, "preferenceDTO", new PreferencesDTO());
        PreferencesDTO preferencesDTO = (PreferencesDTO) PrefObj.getObject(preferences, "preferenceDTO");
        PreferenceConnectionDTO dd = preferencesDTO.getPreferenceConnectionDTO();
        checkAndSetPreferencesDTO(preferencesDTO);

        return preferencesDTO;
    }

    private void checkAndSetPreferencesDTO(PreferencesDTO preferencesDTO) {
        // PreferenceConnectionDTO
        PreferenceConnectionDTO preferenceConnectionDTO = preferencesDTO.getPreferenceConnectionDTO();
        if (preferenceConnectionDTO == null) {
            preferenceConnectionDTO = new PreferenceConnectionDTO();
        }
        if (preferenceConnectionDTO.getMaxConnectionNumber() == 0) {
            preferenceConnectionDTO.setMaxConnectionNumber(Integer.parseInt(bundle.getString("maxConnectionNumber")));
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

            preferencesCompressedDirCategoryDTO.setDirectoryName(bundle.getString("compressedDirectoryCategory.directoryName"));
            preferencesCompressedDirCategoryDTO.setPath(bundle.getString("compressedDirectoryCategory.path"));
            String [] fileCompressedExtensions = bundle.getString("compressedDirectoryCategory.fileExtensions").split(" ");

            preferencesCompressedDirCategoryDTO.setFileExtensions(fileCompressedExtensions);
            preferencesDirectoryCategoryDTOs.add(preferencesCompressedDirCategoryDTO);


            PreferencesDirectoryCategoryDTO preferencesDocumentDirCategoryDTO = new PreferencesDirectoryCategoryDTO();

            preferencesDocumentDirCategoryDTO.setDirectoryName(bundle.getString("documentDirectoryCategory.directoryName"));
            preferencesDocumentDirCategoryDTO.setPath(bundle.getString("documentDirectoryCategory.path"));
            String [] fileDocumentExtensions = bundle.getString("documentDirectoryCategory.fileExtensions").split(" ");

            preferencesDocumentDirCategoryDTO.setFileExtensions(fileDocumentExtensions);
            preferencesDirectoryCategoryDTOs.add(preferencesDocumentDirCategoryDTO);


            PreferencesDirectoryCategoryDTO preferencesMusicDirCategoryDTO = new PreferencesDirectoryCategoryDTO();

            preferencesMusicDirCategoryDTO.setDirectoryName(bundle.getString("musicDirectoryCategory.directoryName"));
            preferencesMusicDirCategoryDTO.setPath(bundle.getString("musicDirectoryCategory.path"));
            String [] fileMusicExtensions = bundle.getString("musicDirectoryCategory.fileExtensions").split(" ");

            preferencesMusicDirCategoryDTO.setFileExtensions(fileMusicExtensions);
            preferencesDirectoryCategoryDTOs.add(preferencesMusicDirCategoryDTO);


            PreferencesDirectoryCategoryDTO preferencesProgramDirCategoryDTO = new PreferencesDirectoryCategoryDTO();

            preferencesProgramDirCategoryDTO.setDirectoryName(bundle.getString("programDirectoryCategory.directoryName"));
            preferencesProgramDirCategoryDTO.setPath(bundle.getString("programDirectoryCategory.path"));
            String [] fileProgramExtensions = bundle.getString("programDirectoryCategory.fileExtensions").split(" ");

            preferencesProgramDirCategoryDTO.setFileExtensions(fileProgramExtensions);
            preferencesDirectoryCategoryDTOs.add(preferencesProgramDirCategoryDTO);


            PreferencesDirectoryCategoryDTO preferencesVideoDirCategoryDTO = new PreferencesDirectoryCategoryDTO();

            preferencesVideoDirCategoryDTO.setDirectoryName(bundle.getString("videoDirectoryCategory.directoryName"));
            preferencesVideoDirCategoryDTO.setPath(bundle.getString("videoDirectoryCategory.path"));
            String [] fileVideoExtensions = bundle.getString("videoDirectoryCategory.fileExtensions").split(" ");

            preferencesVideoDirCategoryDTO.setFileExtensions(fileVideoExtensions);
            preferencesDirectoryCategoryDTOs.add(preferencesVideoDirCategoryDTO);


            preferencesSaveDTO.setPreferencesDirectoryCategoryDTOs(preferencesDirectoryCategoryDTOs);

            // todo other needed ... use other method
        }


        // add all Main preferences objects
        preferencesDTO.setPreferenceConnectionDTO(preferenceConnectionDTO);
        preferencesDTO.setPreferencesSaveDTO(preferencesSaveDTO);
    }

    private void setPreferences(PreferencesDTO preferenceDTO) throws BackingStoreException, IOException, ClassNotFoundException {
 //       preferences.putInt("maxConnectionNumber", preferenceDTO.getPreferenceConnectionDTO().getMaxConnectionNumber());
        PrefObj.putObject(preferences, "preferenceDTO", preferenceDTO);
    }

    /* Update each button's state based off of the
       currently selected download's status. */
    private void updateButtons(DownloadStatus state) {
        if (state != null) {
            switch (state) {
                case DOWNLOADING:
                    mainToolbar.setStateOfButtonsControl(true, false, true, false);
                    break;
                case PAUSED:
                    mainToolbar.setStateOfButtonsControl(false, true, true, false);
                    break;
                case ERROR:
                    mainToolbar.setStateOfButtonsControl(false, true, false, true);
                    break;
                default: // COMPLETE or CANCELLED
                    mainToolbar.setStateOfButtonsControl(false, false, false, true);
            }
        } else {
            // No download is selected in table.
            mainToolbar.setStateOfButtonsControl(false, false, false, false);
        }
    }

    private JMenuBar initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exportDataItem = new JMenuItem("Export Data...");
        JMenuItem importDataItem = new JMenuItem("Import Data...");
        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/primo48/others/button_cancel.png"))); // NOI18N

        fileMenu.add(exportDataItem);
        fileMenu.add(importDataItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu windowMenu = new JMenu("Window");
        JMenu showMenu = new JMenu("Show");
        JMenuItem prefsItem = new JMenuItem("Preferences...");

        JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem("Category");
        showFormItem.setSelected(true);

        showMenu.add(showFormItem);
        windowMenu.add(showMenu);
        windowMenu.add(prefsItem);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);

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

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                int action = JOptionPane.showConfirmDialog(DownloadManagerGUI.this,
                        "Do you realy want to exit the application?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);

                if (action == JOptionPane.OK_OPTION) {
                    WindowListener[]  listeners = getWindowListeners();

                    for (WindowListener listener : listeners) {
                        listener.windowClosing(new WindowEvent(DownloadManagerGUI.this, 0));
                    }
                }
            }
        });

        return menuBar;
    }

    private void createFileHierarchy() {
        String homeDir = System.getProperty("user.home");
        try {
            File CompressedFiles = new File(homeDir + File.separator + "Downloads" + File.separator + "Chita Downloaded Files");
            if (!CompressedFiles.exists()) {
                CompressedFiles.mkdir();
            }

            String pathDirectories = homeDir + File.separator + "Downloads" + File.separator + "Chita Downloaded Files";

            File compressedFiles = new File(pathDirectories + File.separator + "Compressed Files");
            if (!compressedFiles.exists())
                compressedFiles.mkdir();

            File documentFiles = new File(pathDirectories + File.separator + "Document Files");
            if (!documentFiles.exists()) {
                documentFiles.mkdir();
            }
            File musicFiles = new File(pathDirectories + File.separator + "Music Files");
            if (!musicFiles.exists()) {
                musicFiles.mkdir();
            }
            File programFiles = new File(pathDirectories + File.separator + "Program Files");
            if (!programFiles.exists()) {
                programFiles.mkdir();
            }
            File videoFiles = new File(pathDirectories + File.separator + "Video Files");
            if (!videoFiles.exists()) {
                videoFiles.mkdir();
            }
            File otherFiles = new File(pathDirectories + File.separator + "Other Files");
            if (!otherFiles.exists()) {
                otherFiles.mkdir();
            }
            File tempFiles = new File(pathDirectories + File.separator + "Temp Files");
            if (!tempFiles.exists()) {
                tempFiles.mkdir();
            }
        } catch (SecurityException se){
            JOptionPane.showMessageDialog(DownloadManagerGUI.this, "Unable to create necessary directories. may be not have write permission, please restart program", "Directories creation problem", JOptionPane.ERROR_MESSAGE);
        }


    }
}
