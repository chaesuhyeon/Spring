package hello.proxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


/**
 * @Configuration은 내부에 @Component 애노테이션을 포함하고 있어서 컴포넌트 스캔의 대상이 된다.
 * 따라서 컴포넌트 스캔에 의해 hello.proxy.config 위치의 설정 파일들이 스프링 빈으로 자동등록되지 않도록 컴포넌트 스택의 시작 위치를 hello.proxy.app으로 설정했다.
 */
@Import({AppV1Config.class, AppV2Config.class})
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의, app 디렉토리 하위에 있는 파일들만 컴포넌트 대상
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

}
