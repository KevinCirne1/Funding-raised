package com.checkr.interviews;

import java.util.*;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;

public class FundingRaised {
    private static final String CSV_FILE = "startup_funding.csv";

    // Load CSV Data Once
    private static List<String[]> loadCsvData() throws IOException {
        List<String[]> csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE))) {
            String[] row;
            while ((row = reader.readNext()) != null) {
                csvData.add(row);
            }
        }
        return csvData;
    }

    // Map Row to a Map<String, String>
    private static Map<String, String> mapRow(String[] row) {
        Map<String, String> mapped = new HashMap<>();
        mapped.put("permalink", row[0]);
        mapped.put("company_name", row[1]);
        mapped.put("number_employees", row[2]);
        mapped.put("category", row[3]);
        mapped.put("city", row[4]);
        mapped.put("state", row[5]);
        mapped.put("funded_date", row[6]);
        mapped.put("raised_amount", row[7]);
        mapped.put("raised_currency", row[8]);
        mapped.put("round", row[9]);
        return mapped;
    }

    // Generic Filter Method
    private static boolean matchesFilters(String[] row, Map<String, String> options) {
        return options.entrySet().stream().allMatch(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "company_name": return row[1].equals(value);
                case "city": return row[4].equals(value);
                case "state": return row[5].equals(value);
                case "round": return row[9].equals(value);
                default: return true;
            }
        });
    }

    public static List<Map<String, String>> where(Map<String, String> options) throws IOException {
        List<String[]> csvData = loadCsvData();
        List<Map<String, String>> output = new ArrayList<>();

        // Filter Rows
        for (String[] row : csvData) {
            if (matchesFilters(row, options)) {
                output.add(mapRow(row));
            }
        }

        return output;
    }

    public static Map<String, String> findBy(Map<String, String> options) throws IOException, NoSuchEntryException {
        List<String[]> csvData = loadCsvData();

        // Find First Matching Row
        for (String[] row : csvData) {
            if (matchesFilters(row, options)) {
                return mapRow(row);
            }
        }

        throw new NoSuchEntryException("No matching entry found for options: " + options);
    }

    public static void main(String[] args) {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("company_name", "Facebook");
            options.put("round", "a");
            System.out.println(FundingRaised.where(options).size());
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        } catch (NoSuchEntryException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

class NoSuchEntryException extends Exception {
    public NoSuchEntryException(String message) {
        super(message);
    }
}
