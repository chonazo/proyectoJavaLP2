package com.proyecto.launcher;


import com.proyecto.vista.Login;


public class MainLauncher {

    public static void main(String[] args) {
       Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Error crítico:\n" + ex.toString(), 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        });
        
        try {
            Login.main(args);
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Error al iniciar:\n" + ex.toString(), 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
