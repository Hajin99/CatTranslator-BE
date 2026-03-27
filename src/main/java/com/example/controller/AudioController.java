package com.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AudioController {

    private final ObjectMapper objectMapper;
    private String rawPrompt;

    @PostConstruct
    public void init() {
        try {
            rawPrompt = loadPrompt("prompt.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 응답을 바이너리 이미지(PNG)로 받아 iOS에 전달
    @PostMapping("/audio")
    public ResponseEntity<byte[]> uploadAudio(@RequestParam("file") MultipartFile file) {
        try {
            Path tempPath = Paths.get("/Users/gimhajin/Desktop/springSaved.wav");
            //Path tempPath = Files.createTempFile("uploaded-", ".wav");
            //iles.write(tempPath, file.getBytes());
            file.transferTo(tempPath); // ⭐️이게 더 안정적

            byte[] savedData = Files.readAllBytes(tempPath);
            System.out.println("저장된 데이터 바이트 수: " + savedData.length);
            byte[] receivedBytes = file.getBytes(); // MultipartFile의 바이트를 직접 가져옴

            Path testBytesPath = Files.createTempFile("test_received_bytes-", ".wav");
            Files.write(testBytesPath, receivedBytes);
            System.out.println("DEBUG: 직접 바이트 배열로 저장된 파일 경로: " + testBytesPath);
            System.out.println("DEBUG: 직접 바이트 배열로 저장된 파일 크기: " + Files.size(testBytesPath));
            // 이 testBytesPath 파일을 Mac으로 가져가서 재생해 보세요.
            // 만약 이 파일도 재생이 안된다면, 파일 전송 과정에서부터 데이터 손상이 발생한 것입니다.

            System.out.println("받은 파일 이름: " + file.getOriginalFilename());
            System.out.println("파일 크기: " + file.getSize()); // 바이트 크기 확인

            System.out.println("저장된 파일 경로: " + tempPath);
            System.out.println("저장된 파일 크기: " + Files.size(tempPath));


            // Flask로 바이너리 전송
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5050/analyze"))
                    .header("Content-Type", "application/octet-stream")
                    .POST(HttpRequest.BodyPublishers.ofFile(tempPath))
                    .build();


            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "image/png")
                    .body(response.body());

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


//    @PostMapping("/audio")
//    public ResponseEntity<String> uploadAudio(@RequestParam("file") MultipartFile file) {
//        System.out.println("파일 업로드 도착");
//        try {
//            // 임시 저장
//            Path tempPath = Files.createTempFile("uploaded-", ".wav");
//            Files.write(tempPath, file.getBytes());
//
//            // Python으로 전달하거나 Java에서 분석
//            String result = analyzeFrequency(tempPath.toFile());
//
//            return ResponseEntity.ok("분석 결과: " + result);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 처리 실패");
//        }
//    }

//    private String analyzeFrequency(File wavFile) {
//        try {
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofFile(wavFile.toPath());
//
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create("http://localhost:5000/analyze"))
//                    .header("Content-Type", "application/octet-stream")
//                    .POST(body)
//                    .build();
//
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            return response.body();
//
//        } catch (Exception e) {
//            return "분석 실패: " + e.getMessage();
//        }
//    }

    // prompt.txt 읽기
    private String loadPrompt(String templateName) throws IOException {
        Resource resource = new ClassPathResource(templateName);
        try(InputStream is = resource.getInputStream()){
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


    // 응답을 json으로 받아 jpt 응답 생성 후 ios에 전달
    @PostMapping("/audio/emotion")
    public ResponseEntity<String> uploadAudio2(@RequestParam("file") MultipartFile file) {
        try {
            Path tempPath = Paths.get("/Users/gimhajin/Desktop/springSaved.wav");
            //Path tempPath = Files.createTempFile("uploaded-", ".wav");
            //iles.write(tempPath, file.getBytes());
            file.transferTo(tempPath); // 💡 이게 더 안정적

            byte[] savedData = Files.readAllBytes(tempPath);
            System.out.println("저장된 데이터 바이트 수: " + savedData.length);
            byte[] receivedBytes = file.getBytes(); // MultipartFile의 바이트를 직접 가져옴

            Path testBytesPath = Files.createTempFile("test_received_bytes-", ".wav");
            Files.write(testBytesPath, receivedBytes);
            System.out.println("DEBUG: 직접 바이트 배열로 저장된 파일 경로: " + testBytesPath);
            System.out.println("DEBUG: 직접 바이트 배열로 저장된 파일 크기: " + Files.size(testBytesPath));
            // 이 testBytesPath 파일을 Mac으로 가져가서 재생해 보세요.
            // 만약 이 파일도 재생이 안된다면, 파일 전송 과정에서부터 데이터 손상이 발생한 것입니다.

            System.out.println("받은 파일 이름: " + file.getOriginalFilename());
            System.out.println("파일 크기: " + file.getSize()); // 바이트 크기 확인

            System.out.println("저장된 파일 경로: " + tempPath);
            System.out.println("저장된 파일 크기: " + Files.size(tempPath));

            // Flask로 JSON 분석 요청 (별도 라우트)
            HttpRequest jsonRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5050/emotion"))
                    .header("Content-Type", "application/octet-stream")
                    .POST(HttpRequest.BodyPublishers.ofFile(tempPath))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(jsonRequest, HttpResponse.BodyHandlers.ofString());
            String analysisJson = response.body();

            // GPT 프롬프트 구성
            String prompt = rawPrompt.formatted(analysisJson);
            String gptRequestBody = objectMapper.writeValueAsString(Map.of(
                    "model", "gpt-4o",
                    "messages", List.of(
                            Map.of("role", "system", "content", "너는 고양이 울음 분석 전문가야."),
                            Map.of("role", "user", "content", prompt)
                    )
            ));

            // GPT API 요청
            HttpRequest gptRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer ")
                    .POST(HttpRequest.BodyPublishers.ofString(gptRequestBody))
                    .build();

            HttpResponse<String> gptResponse = client.send(gptRequest, HttpResponse.BodyHandlers.ofString());
            String gptAnswer = gptResponse.body();

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json")
                    .body(gptAnswer);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
