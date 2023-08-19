package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) /* method에 붙이는 애노테이션 */
@Retention(RetentionPolicy.RUNTIME) /* 애플리케이션이 실행될 때까지 어노테이션이 살아있음 */
public @interface MethodAop {
    String value();
}
