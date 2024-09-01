package org.zerock.projectmeongmung.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    public List<String[]> readCsv(String filePath) {
        List<String[]> records = new ArrayList<>();
        int expectedFieldCount = 30;  // 예상되는 필드 수

        try (Reader reader = new InputStreamReader(new FileInputStream(filePath), "EUC-KR");
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.size() == expectedFieldCount) {
                    List<String> values = new ArrayList<>();
                    csvRecord.forEach(values::add);
                    records.add(values.toArray(new String[0]));
                } else {
                    System.err.println("Skipping line " + csvRecord.getRecordNumber() + " due to field count mismatch.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }
}
