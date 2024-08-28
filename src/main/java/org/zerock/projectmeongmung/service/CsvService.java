package org.zerock.projectmeongmung.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    public List<String[]> readCsv(String filePath) throws IOException {
        List<String[]> records = new ArrayList<>();

        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

            for (CSVRecord csvRecord : csvParser) {
                List<String> values = new ArrayList<>();
                csvRecord.forEach(values::add);
                records.add(values.toArray(new String[0]));
            }
        }

        return records;
    }
}
