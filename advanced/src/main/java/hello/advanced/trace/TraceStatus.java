package hello.advanced.trace;

/**
 * 로그를 시작할 때 상태 정보를 가지고 있음
 * 상태 정보는 로그를 종료할 때 사용
 */
public class TraceStatus {
    private TraceId traceId; // 내부에 트랜잭션 ID와 level을 가지고 있다.
    private Long startTimeMs; // 로그 시작 시간. 로그 종료시 이 시각 시간을 기준으로 시작~종료까지 전체 수행 시간을 구할 수있음
    private String message; // 시작시 사용한 메시지. 이후 로그 종료시에도 이 메시지를 사용하여 출력

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}
