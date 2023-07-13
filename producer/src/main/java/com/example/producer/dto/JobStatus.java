package com.example.producer.dto;

import com.example.producer.jpa.JobEntity;
import lombok.Data;

@Data
// 사용자에게 요청 처리 상태를 알리기 위한 DTO
public class JobStatus {
    private String jobId;
    private String status;
    private String resultPath;

    // 팩토리 메소드 패턴
    public static JobStatus fromEntity(JobEntity entity) {
        JobStatus jobStatus = new JobStatus();
        jobStatus.setJobId(entity.getJobId());
        jobStatus.setStatus(entity.getStatus());
        jobStatus.setResultPath(entity.getResultPath());

        return jobStatus;
    }
}
