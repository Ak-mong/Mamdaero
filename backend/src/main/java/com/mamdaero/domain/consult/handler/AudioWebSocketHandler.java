package com.mamdaero.domain.consult.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

@Component
public class AudioWebSocketHandler extends BinaryWebSocketHandler {

    private final String uploadDir;
    private FileOutputStream outputStream;
    private String finalFileName;

    public AudioWebSocketHandler(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("Connection established with session: " + session.getId());
        // 저장 경로 가져오기
        finalFileName = uploadDir + "/audio_" + System.currentTimeMillis() + ".webm";
        File file = new File(finalFileName);
        file.getParentFile().mkdirs(); // 디렉토리가 존재하지 않으면 생성
        outputStream = new FileOutputStream(file);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        ByteBuffer payload = message.getPayload();
        byte[] data = new byte[payload.remaining()];
        payload.get(data);
        try {
            System.out.println("Received data of size: " + data.length);
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        if (outputStream != null) {
            outputStream.close();
            System.out.println("File saved successfully: " + finalFileName);
            // 변환 작업 수행 (선택 사항)
            convertToMP4(finalFileName, finalFileName.replace(".webm", ".mp4"));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        exception.printStackTrace();
    }

    private void convertToMP4(String webmFilePath, String mp4FilePath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-i", webmFilePath, mp4FilePath);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor(10, TimeUnit.SECONDS);
        System.out.println("File converted to MP4: " + mp4FilePath);
    }
}