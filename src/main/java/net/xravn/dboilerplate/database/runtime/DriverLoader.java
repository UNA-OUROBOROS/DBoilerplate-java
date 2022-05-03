package net.xravn.dboilerplate.database.runtime;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DriverLoader extends URLClassLoader {
    private DriverLoader(String path, boolean isPath) throws MalformedURLException {
        super(new URL[] {});
        if(!isPath){
            addURL(new File(path).toURI().toURL());
        }
        else{
            File driverFolder = new File(path);
            if(driverFolder.exists()){
                for(File file : driverFolder.listFiles()){
                    if(file.getName().endsWith(".jar")){
                        System.out.println("Adding JAR Driver: " + file.getAbsolutePath());
                        addURL(file.toURI().toURL());
                    }
                }
            }
        }
    }

    private static DriverLoader driverLoader;

    public static Class<?> load(String driverClassName, String path, boolean isPath) throws ClassNotFoundException, MalformedURLException {
        try {
            return Class.forName(driverClassName);
        } catch (ClassNotFoundException ex) {
            if (driverLoader == null) {
                driverLoader = new DriverLoader(path, isPath);
            }
            return driverLoader.loadClass(driverClassName);
        }
    }
}
