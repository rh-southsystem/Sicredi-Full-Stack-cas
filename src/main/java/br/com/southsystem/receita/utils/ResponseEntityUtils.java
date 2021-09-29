package br.com.southsystem.receita.utils;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public class ResponseEntityUtils {

    private ResponseEntityUtils() {
    }

    public static ResponseEntity<Resource> responseResource(Resource resource, String fileName) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
        headers.add("Content-Disposition",
                String.format("%s; filename=%s", "attachment", createCSVName(fileName)));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    private static String createCSVName(String fileName) {
        LocalDate data = LocalDate.now();
        return String.format("%s%s%s%s%s", "", data.getYear(), data.getMonthValue(), data.getDayOfMonth(), ".csv");
    }

}
