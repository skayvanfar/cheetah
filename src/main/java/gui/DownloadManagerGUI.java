package gui;

import enums.DownloadState;
import gui.listener.AddNewDownloadListener;
import gui.listener.DownloadPanelListener;
import gui.listener.MainToolbarListener;
import model.Download;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Saeed Kayvanfar on 9/10/2015.
 */

// implements Observer
public class DownloadManagerGUI extends JFrame {

    private MainToolBar mainToolbar;
    private CategoryPanel categoryPanel;
    private JTabbedPane mainTabPane;
    private DownloadPanel downloadPanel;
    private MessagePanel messagePanel;
    private JSplitPane mainSplitPane;
    private StatusPanel statusPanel;
    private AddNewDownloadDialog addNewDownloadDialog;

////////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////


    // Constructor for Download Manager.
    public DownloadManagerGUI(String name) {
        super(name);

        setLayout(new BorderLayout());

        mainToolbar = new MainToolBar();
        categoryPanel = new CategoryPanel();
        downloadPanel = new DownloadPanel();
        messagePanel = new MessagePanel();
        mainTabPane = new JTabbedPane();
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoryPanel, mainTabPane);
        mainSplitPane.setOneTouchExpandable(true);
        statusPanel = new StatusPanel();
        addNewDownloadDialog = new AddNewDownloadDialog(this);

        mainTabPane.addTab("Download Panel", downloadPanel);
        mainTabPane.addTab("Messages", messagePanel);


        addNewDownloadDialog.setAddNewDownloadListener(new AddNewDownloadListener() {
            @Override
            public void newDownloadEventOccured(URL textUrl) {
                if (textUrl != null) {
                    downloadPanel.addDownload(new Download(textUrl));
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
                downloadPanel.actionPause();
          //      updateButtons();
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
            public void rowSelectedEventOccured() {
           //     updateButtons();
            }

            @Override
            public void stateChangedEventOccured(DownloadState downloadState) {
                updateButtons(downloadState);
            }
        });

        // Handle window closing events.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.out.println("Window Closing");
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

    /* Update is called when a Download notifies its
       observers of any changes. */
  //  public void update(Observable o, Object arg) {
        // Update buttons if the selected download has changed.
    //    if (selectedDownload != null && selectedDownload.equals(o))
    //        SwingUtilities.invokeLater(new Runnable() {
    //            public void run() {
    //                updateButtons();
    //            }
  //          });
  //  }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Pause the selected download.
  //  private void actionPause() {
   //     selectedDownload.pause();
   //     updateButtons();
  //  }

    // Resume the selected download.
   // private void actionResume() {
   //     selectedDownload.resume();
   //     updateButtons();
   // }

    // Cancel the selected download.
  //  private void actionCancel() {
   //     selectedDownload.cancel();
     //   updateButtons();
 //   }

    // Clear the selected download.
 //   private void actionClear() {
  //      clearing = true;
  //      tableModel.clearDownload(table.getSelectedRow());
  //      clearing = false;
  //      selectedDownload = null;
  //      updateButtons();
  //  }

    /* Update each button's state based off of the
       currently selected download's status. */
    private void updateButtons(DownloadState state) {
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

        fileMenu.add(exportDataItem);
        fileMenu.add(importDataItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu windowMenu = new JMenu("Window");
        JMenu showMenu = new JMenu("Show");
        JMenuItem prefsItem = new JMenuItem("Preferences...");

        JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem("Person Form");
        showFormItem.setSelected(true);

        showMenu.add(showFormItem);
        windowMenu.add(showMenu);
        windowMenu.add(prefsItem);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);

        prefsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
       //         prefsDialog.setVisible(true);
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
}
