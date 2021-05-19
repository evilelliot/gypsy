/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileManager;
// Librerías para manejar los archivos
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
// Librerías para manejar las excepciones
import java.io.IOException;
import java.util.Random;
import javax.swing.ImageIcon;
// Librerías para manejar el filechooser
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFrame;
/**
 *
 * @author Alberto Ocaranza
 */
public class FileManager {
    public boolean isSaved;
    public String fileURL = null;
    public JFrame panel;
    // Constructor
    public FileManager(JFrame rootFrame){
        this.panel = rootFrame;
    }
    public void openFile(JTextArea content){
        // Instancia del filechooser
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Abrir archivo");
        chooser.showOpenDialog(null);
        // Filename a un array (encontrar el nombre solamente)
        
        File file = chooser.getSelectedFile();
        this.fileURL = file.getAbsolutePath();
        
        System.out.println(this.getFileName(this.fileURL));

        try {
            FileReader reader = new FileReader(fileURL);
            BufferedReader bufferedReader = new BufferedReader(reader);
            content.read(bufferedReader, null);
            bufferedReader.close();
            content.requestFocus();

            // lblMensaje.setForeground(success);
            // lblMensaje.setText("Archivo abierto.");
        } catch (Exception e) {
            // lblMensaje.setForeground(failed);
            // lblMensaje.setText("No se pudo abrir el archivo.");
            e.printStackTrace();
        }

    }
    public void save(JTextArea content){
        if(this.fileURL != null){
            // Se puede guardar
            String data = content.getText();
            try {
                FileWriter myWriter = new FileWriter(this.fileURL);
                myWriter.write(data);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");

                isSaved = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("No hay archivo abierto aún.");
        }
    }
    public void saveAs(JTextArea content){
        // Borrar el texto
        // Instancia del filechooser
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar archivo");
        chooser.showOpenDialog(null);
        
        File file = chooser.getSelectedFile();
        this.fileURL = file.getAbsolutePath();
        
        if(this.checkFileExist(this.fileURL) == true){
            // Quiere decir que ya existe el archivo
            // Mostrar pregunta para sobreescribir: si acepta sobreescribe, si no acepta se genera un nuevo nombre de archivo
            Object[] options = {"Aceptar", "Guardar con otro nombre", "Cancelar"};
                    
            int n = JOptionPane.showOptionDialog(this.panel,
                "El archivo ya existe, ¿deseas sobreescribir?",
                "Sobreescribir archivo",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
            // Manejar la opción de usuario
            switch(n){
                case 0:
                    // Sobreescribe
                    this.fileURL = file.getAbsolutePath();
                    break;
                case 1:
                    // Guarda la misma información en un archivo distinto
                    this.fileURL = this.newRandomFileName(this.fileURL);
                    System.out.println(this.newRandomFileName(this.fileURL));
                    break;
                case 2:
                    // Cancela todo
                    return;
            }
        }else{
            this.fileURL = file.getAbsolutePath();
        }
        String data = content.getText();
        try {
            FileWriter myWriter = new FileWriter(this.fileURL);
            myWriter.write(data);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            
            isSaved = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Checar si ya existe un archivo
    public boolean checkFileExist(String fileURL){
        boolean data = false;
        try{
            File file = new File(fileURL);
            
            if(file.exists()){
              data = true;
            }else{
              data = false;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }
    // Obtener un nombre de archivo con base en el original
    public String newRandomFileName(String fileURL){
        // Random numero
        Random random = new Random();
        int randomPrefix = 100000 + random.nextInt(900000);;
        // Separar el nombre de la URL
        String[] fileToArray  = fileURL.split("\\\\");
        String   fileNameOnly = fileToArray[fileToArray.length - 1];
        // Crear una nueva URL
        String newFileURL = "";  
        // Agregar una string random al inicio de fileNameOnly
        fileNameOnly = randomPrefix + "_" + fileNameOnly;
        
        for(int i = 0; i <= fileToArray.length - 2; i++){
            // Añadir los datos a la nueva URL
            if(i == 0){
                newFileURL = newFileURL + fileToArray[i];
            }else if(i == fileToArray.length - 1){
                newFileURL = newFileURL + fileToArray[i];
            }else{
                newFileURL = newFileURL + "\\" + fileToArray[i];
            }
            System.out.println(fileToArray[i]);
        }      
        newFileURL = newFileURL + "\\" + fileNameOnly;
        
        return newFileURL;
    }
    // Obtener solo el nombre del archivo: explotamos en /
    public String getFileName(String fileURL){
        String[] fileToArray = fileURL.split("\\\\");
        return fileToArray[fileToArray.length - 1];
    }
}
