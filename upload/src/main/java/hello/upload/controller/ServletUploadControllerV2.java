package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile(){
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV2(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName={}" , itemName);

        /**
         * part : multipart-form-data에서 각 항목별로 구분된 것을 각각 받아볼 수 있는것
         */
        Collection<Part> parts = request.getParts();
        log.info("parts={}" , parts);

        for (Part part : parts) {
            log.info("==== PART ====");
            log.info("name={}" , part.getName());

            // part도 header와 body로 구분된다.
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {} : {}" , headerName, part.getHeader(headerName));
            }

            // 편의 메서드
            // content-disposition; filename
            log.info("submittedFilename={}" , part.getSubmittedFileName());
            log.info("size={}", part.getSize());

            // 데이터 읽기
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);// body에 읽은 것을 string으로 해줌. 꼭 charset 지정해주기
            log.info("body={}" , body);

            // 파일에 저장하기
            if(StringUtils.hasText(part.getSubmittedFileName())){ // 실제 파일이 있는지 확인
                String fullPath = fileDir + part.getSubmittedFileName();
                log.info("파일 저장 fullPath={}" , fullPath);
                part.write(fullPath); // 파일 저장
            }

        }
        return "upload-form";
    }
}
