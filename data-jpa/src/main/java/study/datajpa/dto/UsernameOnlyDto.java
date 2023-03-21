package study.datajpa.dto;

/**
 * class 기반 Projections
 */
public class UsernameOnlyDto {
    private final String username;

    /**
     * 생성자가 중요
     * 생성자에 파라미터로 매칭
     */
    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
