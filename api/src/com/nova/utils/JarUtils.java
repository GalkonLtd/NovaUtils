package com.nova.utils;

import com.nova.utils.SystemUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/22/2015
 */
public class JarUtils {

    public static boolean isJavaExecutable(String name) {
        return name.equals("java") || name.equals("java.exe") || name.equals("java.sh") || name.equals("java.bat");
    }

    public static boolean launchJar(String name, File archive, String main, String[] jvmArgs, String[] mainArgs) {
        if (!archive.exists()) {
            System.err.println("Failed to launch archive - archive does not exist: " + archive.getAbsolutePath());
            showErrorDialog("Failed to launch " +  name + " game client!\nArchive does not exist: " + archive.getAbsolutePath());
            return false;
        }
        try {
            String javaPath = System.getProperty("java.home");
            if(javaPath != null) {
                File javaBin = new File(javaPath += (System.getProperty("file.separator") + "bin" + System.getProperty("file.separator")));
                if(!javaBin.exists()) {
                    javaPath = null;
                } else {
                    File[] binFiles = javaBin.listFiles();
                    if(binFiles != null) {
                        for(File file : binFiles) {
                            if(file != null && isJavaExecutable(file.getName())) {
                                javaPath += file.getName();
                                break;
                            }
                        }
                    } else {
                        javaPath = null;
                    }
                }
            }
            if (SystemUtils.isMac()) {
                javaPath = "/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin/java";
            }
            if(javaPath == null || javaPath.contains(".plugin")) {
                javaPath = "java";
            } else {
                javaPath = javaPath.replace("\\", System.getProperty("file.separator"));
            }
            StringBuilder process = new StringBuilder();
            if (SystemUtils.isMac()) {
                //process.append("open,");
                Runtime.getRuntime().exec("chmod 000 " + javaPath);
            }
            process.append(javaPath + ",");
            if (jvmArgs != null) {
                for (String arg : jvmArgs) {
                    process.append(arg + ",");
                }
            }
            process.append("-classpath" + ",");
            process.append(archive.getAbsolutePath() + ",");
            process.append(main);
            if (mainArgs != null) {
                for (String arg : mainArgs) {
                    process.append("," + arg);
                }
            }
            System.out.println(process.toString().replaceAll(",", " "));
            new ProcessBuilder(process.toString().split(",")).start();
            return true;
        } catch (IOException e) {
            showErrorDialog("Failed to launch " + name + "!\nAttempting to launch using reflection...\n\n" + e.getMessage());
            System.err.println("Failed to launch " + name + "!");
            e.printStackTrace();
            System.err.println("Attempting to launch using reflection...");
            String errorMessage, error;
            try {
                ClassLoader classLoader = URLClassLoader.newInstance(new URL[]{
                        archive.toURI().toURL()
                });
                Class<?> mainClass = classLoader.loadClass(main);
                Method method = mainClass.getDeclaredMethod("main", String[].class);
                method.invoke(null, (Object) new String[] { "" });
                return true;
            } catch (ClassNotFoundException ex) {
                error = "ClassNotFoundException: " + ex.getCause();
            } catch (MalformedURLException ex) {
                error = "MalformedURLException: " + ex.getCause();
            } catch (SecurityException ex) {
                error = "SecurityException: " + ex.getCause();
            } catch (NoSuchMethodException ex) {
                error = "NoSuchMethodException: " + ex.getCause();
            } catch (IllegalArgumentException ex) {
                error = "IllegalArgumentException: " + ex.getCause();
            } catch (IllegalAccessException ex) {
                error = "IllegalAccessException: " + ex.getCause();
            } catch (InvocationTargetException ex) {
                error = "InvocationTargetException: " + ex.getCause() + " | " + ex.getTargetException();
            }
            errorMessage = getErrorMessage(error, archive);
            showErrorDialog(errorMessage);
        }
        return false;
    }

    private static void showErrorDialog(String error) {
        JOptionPane.showMessageDialog(null, error, "Error!", JOptionPane.ERROR_MESSAGE);
    }

    private static String getErrorMessage(String exceptionLine, File archive) {
        StringBuilder builder = new StringBuilder();
        builder.append("An error occurred while loading reflection!");
        builder.append("\n\n");
        builder.append(exceptionLine);
        builder.append("\n\n");
        builder.append("Archive FILE is " + (archive == null ? "NULL" : (archive.exists() ? "EXISTENT" : "NON-EXISTENT")));
        builder.append("\n");
        builder.append("Archive PATH: " + archive.getAbsolutePath());
        return builder.toString();
    }

}
