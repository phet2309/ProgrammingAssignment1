package com.pa1.textdetectionapp.textdetectionapp.service;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class FileWriteService {
    public void fileWrite(Map<String, String> mp) {
        try {
            FileWriter writer = new FileWriter("ImageText.txt");

            Iterator<Map.Entry<String, String>> it = mp.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = it.next();
                writer.write(pair.getKey() + ":" + pair.getValue() + "\n");
                it.remove();
            }
            writer.close();
            log.info("Write operation complete, new file created ImageText.txt");
        } catch (IOException e) {
            log.info("Error occurred while writing file");
            e.printStackTrace();
        }
    }
}
