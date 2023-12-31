package com.example.producer;

import com.example.producer.dto.JobPayload;
import com.example.producer.dto.JobTicket;
import com.example.producer.jpa.JobEntity;
import com.example.producer.jpa.JobRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {
    // RabbitMQ 에 메세지를 적재 하기 위한 클래스
    private final RabbitTemplate rabbitTemplate;
    // Config 에 정의한 처리할 작업 정보가 대기하는 Queue
    private final Queue jobQueue;
    // 객체를 쉽게 JSON 문자열로 직렬화 해주는 라이브러리
    private final Gson gson;

    private final JobRepository jobRepository;

    // filename 을 인자로 받고
    // filename 을 바탕으로 JSON 으로 직렬화된 작업 정보를
    // Queue 에 적재한 뒤 사용자에게 JobTicket 을 반환하는 메소드
    public JobTicket send(String filename) {
        // jobId 발행 (UUID)
        String jobId = UUID.randomUUID().toString();
        JobTicket jobTicket = new JobTicket(jobId);

        // JobPayload 생성 (Consumer 확인하는 데이터)
        JobPayload payload = new JobPayload(
                jobId,
                filename,
                // PATH
                String.format("/media/user-uploaded/raw/%s", filename)
        );

        // JobEntity 로 작업 내역 입력 기록
        JobEntity jobEntity = new JobEntity();
        jobEntity.setJobId(jobId);
        jobEntity.setStatus("IDLE");
        jobRepository.save(jobEntity);

        // Message Broker 에게 메세지 전달
        rabbitTemplate.convertAndSend(
                // 어떤 Queue 에 적재할지에 대한 이름
                jobQueue.getName(),
                // 메세지로 보낼 문자열
                gson.toJson(payload)
        );
        log.info("Sent Job: {}", jobTicket.getJobId());
        return jobTicket;
    }
}
