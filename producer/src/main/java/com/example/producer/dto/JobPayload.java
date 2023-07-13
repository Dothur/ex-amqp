package com.example.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// Producer 가 발생시킬 Job 을 정의한 DTO
public class JobPayload {
    private String jobId;
    private String filename;
    // 파일의 경로
    private String path;
}
