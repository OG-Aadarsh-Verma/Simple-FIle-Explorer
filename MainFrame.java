import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;


/**
 * This class is used to set & manage the layout of the display window.
 * It is also responsible to add componenets to the above layout
 */
public class MainFrame extends JFrame {

    private JList<String> fileList;
    private DefaultListModel<String> listModel;

    public MainFrame(String title){
        super(title); // to set the window title

        // to terminate the program when the application window is closed.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(300, 300);


        // set logo image for the window
        ImageIcon icon = new ImageIcon("images/small_logo.png");
        this.setIconImage(icon.getImage());


        // create the file list and scrollpane
        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(fileList);

        
        //create the buttons for creating a new file
        JButton createNewFileButton = new JButton("Create new file");
        JButton openFileButton = new JButton("Open file");
        createNewFileButton.addActionListener(e -> createNewFile());
        openFileButton.addActionListener(e -> openFile());
        //create the top panel
        JPanel topButtonPanel = new JPanel();
        topButtonPanel.add(createNewFileButton);
        topButtonPanel.add(openFileButton);

        //create the scrollable view and 2 buttons for refresh and delete
        JButton refreshButton = new JButton("Refresh");
        JButton deleteButton = new JButton("Delete");
        refreshButton.addActionListener(e -> refresh("."));
        deleteButton.addActionListener(e -> delete());
        //create a panel for the bottom buttons
        JPanel buttomButtonPanel = new JPanel();
        buttomButtonPanel.add(refreshButton);
        buttomButtonPanel.add(deleteButton);


        // Add components to display window
        Container display = getContentPane();


        // Set layout manager
        setLayout(new BorderLayout());


        display.add(scrollPane, BorderLayout.CENTER);
        display.add(topButtonPanel, BorderLayout.NORTH);
        display.add(buttomButtonPanel, BorderLayout.SOUTH);


        //this.pack(); // to set window size according to the components
    }


    // method to refreh the file list
    private void refresh(String path){
        listModel.clear();
        File currentDirectory = new File(path);
        File[] files = currentDirectory.listFiles();
        if(files != null){
            for(File file : files){
                listModel.addElement(file.getName());
            }
        }
    }

    //method to delete selected files
    private void delete(){
        String selectedFile = fileList.getSelectedValue();
        if(selectedFile != null){
            int option =  JOptionPane.showConfirmDialog(this, "Do you want to delete the selected File?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION){
                File toDelete = new File(selectedFile);
                if(toDelete.delete()){
                listModel.removeElement(selectedFile);
                JOptionPane.showMessageDialog(this, "File deleted successfully.", "Successful", JOptionPane.PLAIN_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(this, "Failed to delete the file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "Deletion cancelled successfully.", "Cancelled Deletion", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Please select a file to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }

        refresh(".");
    }


    //method to create a new file
    private void createNewFile(){
        String fileName = JOptionPane.showInputDialog("Enter the name of the file to be created: \n (along with the extension)");
        try{
            File newFile = new File(fileName);
            if(newFile.createNewFile()){
                JOptionPane.showMessageDialog(this, "File created successfully.", "Created new file", JOptionPane.PLAIN_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(this, "File already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        refresh(".");
    }

    //method to open the selected file
    private void openFile(){
        String selectedFile = fileList.getSelectedValue();
        if(selectedFile != null){
            File file = new File(selectedFile);
            int option = JOptionPane.showConfirmDialog(this, "Do you want to open the selected file / folder?", "Open file / folder", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION){
                if(file.isDirectory()){
                    refresh(file.getAbsolutePath());
                }
                else{
                    if(Desktop.isDesktopSupported()){
                        Desktop desktop = Desktop.getDesktop();
                        try{
                            desktop.open(file);
                        }catch(Exception e){
                            JOptionPane.showMessageDialog(this, "Cannot open file \n"+e.toString() ,"Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Desktop is not supporeted by the platform", "Error", option);
                    }
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "Cancelled successfully.", "Cancelled", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Please select a file to open", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}