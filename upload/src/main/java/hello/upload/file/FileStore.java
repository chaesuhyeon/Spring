package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    // 파일 저장 경로 반환
    public String getFullPath(String filename){
        return fileDir+filename;
    }

    // 파일 여러개 저장
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();

        // 파일들에 대해 반복문을 돌려가며 저장을 한다.
        for (MultipartFile multipartFile : multipartFiles) {

            // 파일이 있을 경우에만 저장을 한다.
            if(!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile)); // storeFile(multipartFile)의 반환 타입이 UploadFile

            }
        }

        return storeFileResult;
    }

    // 단일 파일 저장
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            return null;
        }

        // 업로드한 파일명
        String originalFilename = multipartFile.getOriginalFilename();

        // 서버에 저장하는 파일명
        String storeFileName = createStoreFileName(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename,storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        // 파일 확장자 추출
        String ext = extractExt(originalFilename);

        // 랜덤 문자열 생성
        String uuid = UUID.randomUUID().toString();

        // 서버에 저장하는 파일명(storeFileName)
        return uuid + "." + ext;
    }

    // 파일 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
