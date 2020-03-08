package com.easylife.loki.service;

import com.easylife.loki.model.Credential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    private static final String ROOT = System.getProperty("user.dir");

    @Value("${dump-folder-path}")
    String dumpFolderPath;

    public DataService() {
    }

    @PostConstruct
    public void createDir() {
        File file = new File(ROOT + dumpFolderPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public List<Credential> loadCredentials() {
        List<File> files = loadFiles(ROOT + dumpFolderPath);
        for (File file : files) {

        }
        return new ArrayList<>();
    }

    private List<String> readFile(File file) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

            return lines;
        }
    }

    private List<File> loadFiles(String folderPath) {
        List<File> files = new ArrayList<>();
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                files.addAll(loadFiles(file.getAbsolutePath()));
            } else {
                files.add(file);
            }
        }

        return files;
    }
}
