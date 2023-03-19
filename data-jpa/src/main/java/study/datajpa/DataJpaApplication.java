package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing //(modifyOnCreate = false) create는 값이 들어가고 modified 컬럼은 null 값이 들어감 , 하지만 관례상 그냥 created 값 넣을 떄 modified값을 같이 넣어주는 것이 관례. 개발하다보면 null값 처리가 더 귀찮고 괴로움
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider(){
		return new AuditorAware<String>() {
			@Override
			public Optional<String> getCurrentAuditor() {
				return Optional.of(UUID.randomUUID().toString()); // 예시로 uuid로 해줌
				// 실제에서는 세션 정보를 가져와서 정보를 꺼내와야 함
			}
		};
//		return () -> Optional.of(UUID.randomUUID().toString()); // 위에 식을 람다로 사용하면 이렇게..
	}
}
