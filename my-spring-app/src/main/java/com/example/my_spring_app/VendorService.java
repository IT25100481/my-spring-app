package com.example.my_spring_app;

import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class VendorService {

    private static final String FILE_PATH = "vendors.txt";

    // Save vendor to vendors.txt
    public void saveVendor(Vendor vendor) throws IOException {
        FileWriter fw = new FileWriter(FILE_PATH, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(vendor.toString());
        bw.newLine();
        bw.close();
    }

    // Check if email already exists
    public boolean emailExists(String email) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 1 && parts[1].equals(email)) {
                return true;
            }
        }
        return false;
    }

    // Get all vendors
    public List<Vendor> getAllVendors() throws IOException {
        List<Vendor> vendors = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return vendors;

        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                vendors.add(new Vendor(parts[0], parts[1], parts[2],
                        parts[3], parts[4]));
            }
        }
        return vendors;
    }
}
