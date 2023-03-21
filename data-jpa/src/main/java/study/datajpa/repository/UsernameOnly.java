package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly { // 구현체가 아니라 인터페이스로 만듦
    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
