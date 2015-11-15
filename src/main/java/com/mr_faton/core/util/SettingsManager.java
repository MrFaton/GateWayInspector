package com.mr_faton.core.util;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Description
 *
 * @author root
 * @since 11.11.2015
 */
public class SettingsManager {
    public static final String FILE_PATH = System.getProperty("user.dir") + "\\settings.properties";
    public static final String DELAY = "DELAY";
    public static final String GATEWAY_NAME_PATTERN = "GATEWAY_";
    public static final Properties SETTINGS_MANAGER = new Properties();

    public static void load() throws IOException {
        File settingsFile = new File(FILE_PATH);
        if (!settingsFile.exists()) {
            try(FileOutputStream fos = new FileOutputStream(settingsFile)) {
                String lineSeparator = System.getProperty("line.separator");
                String settings = "" +
                        DELAY + " = 15000" + lineSeparator +
                        GATEWAY_NAME_PATTERN + 1 + " = 192.168.101.1" + lineSeparator +
                        GATEWAY_NAME_PATTERN + 2 + " = 192.168.101.2";
                fos.write(settings.getBytes());
                fos.flush();
            }
        }
        try(FileInputStream fin = new FileInputStream(settingsFile)) {
            SETTINGS_MANAGER.load(fin);
        }
    }

    public static String getSetup(String key) {
        return SETTINGS_MANAGER.getProperty(key);
    }
}