package hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    // hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))") // 분리했으니 여러군데서 사용 가능
    public void allOrder(){} // pointcut signature

    // 클래스 이름이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService(){}

    // 두개 조합
    @Pointcut("allOrder() && allService()")
    public void orderAndService(){}
}
