package org.zerock.projectmeongmung.controller;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.projectmeongmung.service.CsvService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CsvController {

    @Autowired
    private CsvService csvService;

    // EPSG:5174 (Korea 1985 / Unified CS) 좌표계를 사용하여 변환
    private static final CRSFactory factory = new CRSFactory();
    private static final CoordinateReferenceSystem srcCrs = factory.createFromName("EPSG:5174"); // Korea 1985 / Unified CS
    private static final CoordinateReferenceSystem dstCrs = factory.createFromName("EPSG:4326"); // WGS84 좌표계

    private static final CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
    private static final CoordinateTransform transform = ctFactory.createTransform(srcCrs, dstCrs);

    // 위도와 경도의 소수점 이하 자릿수를 제한하기 위한 DecimalFormat
    private static final DecimalFormat df = new DecimalFormat("#.######");

    // 유효한 숫자인지 확인하는 메서드
    private boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @GetMapping("/read-csv")
    public List<Map<String, String>> readCsv() throws IOException {
        Resource resource = new ClassPathResource("static/csv/서울시송파구동물병원인허가정보.csv");
        String filePath = resource.getFile().getPath();

        List<String[]> csvData = csvService.readCsv(filePath);

        // 데이터가 없으면 빈 리스트 반환
        if (csvData == null || csvData.isEmpty() || csvData.size() <= 1) {
            return List.of();
        }

        // CSV 데이터를 Map 형식으로 변환 (데이터가 있는 경우만)
        return csvData.stream().skip(1).map(row -> {
                    Map<String, String> map = new HashMap<>();
                    try {
                        // 필수 데이터 검증
                        if (row.length <= 24 || row[18] == null || row[23] == null || row[24] == null) {
                            return null; // 데이터가 3개 모두 존재하지 않으면 null 반환
                        }

                        // 숫자 검증
                        if (!isNumeric(row[23]) || !isNumeric(row[24])) {
                            System.err.println("Invalid number format: " + row[23] + ", " + row[24]);
                            return null; // 유효하지 않은 숫자 형식인 경우 null 반환
                        }

                        double x = Double.parseDouble(row[23]);
                        double y = Double.parseDouble(row[24]);
                        ProjCoordinate srcCoord = new ProjCoordinate(x, y);
                        ProjCoordinate dstCoord = new ProjCoordinate();

                        // 좌표 변환 시도
                        transform.transform(srcCoord, dstCoord);

                        // 위도와 경도 값을 소수점 이하 6자리로 제한
                        String lng = df.format(dstCoord.x);
                        String lat = df.format(dstCoord.y);

                        map.put("title", row[18]);  // 19번째 데이터: 사업장명
                        map.put("lng", lng);    // 변환된 경도 (소수점 이하 6자리)
                        map.put("lat", lat);    // 변환된 위도 (소수점 이하 6자리)

                    } catch (Exception e) {
                        System.err.println("좌표 변환 오류: " + e.getMessage());
                        e.printStackTrace();
                        return null; // 변환 실패 시 null 반환
                    }

                    return map;
                })
                .filter(map -> map != null) // null 값 필터링
                .collect(Collectors.toList());
    }

    @GetMapping("/search-csv")
    public List<Map<String, String>> searchCsv(@RequestParam("keyword") String keyword) throws IOException {
        Resource resource = new ClassPathResource("static/csv/서울시송파구동물병원인허가정보.csv");
        String filePath = resource.getFile().getPath();

        List<String[]> csvData = csvService.readCsv(filePath);

        if (csvData == null || csvData.isEmpty() || csvData.size() <= 1) {
            return List.of();
        }

        // 키워드 검색
        return csvData.stream()
                .skip(1) // 헤더를 건너뜁니다.
                .filter(row -> row[18] != null && row[18].contains(keyword)) // 사업장명에 키워드가 포함된 경우 필터링
                .map(row -> {
                    Map<String, String> map = new HashMap<>();
                    try {
                        if (row.length <= 24 || row[18] == null || row[23] == null || row[24] == null) {
                            return null; // 데이터가 3개 모두 존재하지 않으면 null 반환
                        }

                        if (!isNumeric(row[23]) || !isNumeric(row[24])) {
                            System.err.println("Invalid number format: " + row[23] + ", " + row[24]);
                            return null;
                        }

                        double x = Double.parseDouble(row[23]);
                        double y = Double.parseDouble(row[24]);
                        ProjCoordinate srcCoord = new ProjCoordinate(x, y);
                        ProjCoordinate dstCoord = new ProjCoordinate();

                        transform.transform(srcCoord, dstCoord);

                        String lng = df.format(dstCoord.x);
                        String lat = df.format(dstCoord.y);

                        map.put("title", row[18]);  // 사업장명
                        map.put("lng", lng);    // 변환된 경도
                        map.put("lat", lat);    // 변환된 위도

                    } catch (Exception e) {
                        System.err.println("좌표 변환 오류: " + e.getMessage());
                        e.printStackTrace();
                        return null; // 변환 실패 시 null 반환
                    }

                    return map;
                })
                .filter(map -> map != null) // null 값 필터링
                .collect(Collectors.toList());
    }
}
