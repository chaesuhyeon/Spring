package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

/**
 * proxy도 실제 객체와 그 모양이 같아야 하기 때문에 Subject 인터페이스를 구현해야한다.
 * 클라이언트가 프록시를 호출하면 프록시가 최종적으로 실제 객체를 호출해야 한다. 따라서 내부에 실제 객체의 참조를 구현해야한다. (의존관계 주입)
 */
@Slf4j
public class CacheProxy implements Subject{
    private Subject target; // 프록시가 호출하는 대상
    private String cacheValue;

    // proxy가 realSubject를 참조하려면 의존관계를 주입해야한다.
    public CacheProxy(Subject target) {
        this.target = target;
    }

    /**
     * cacheValue에 값이 없으면 실제 객체(target)을 호출해서 값을 구한다. 그리고 구한 값을 cacheValue에 저장하고 반환한다.
     * cacheValue에 값이 있으면 실제 객체를 전혀 호출하지 않고 캐시 값을 그대로 반환한다. -> realSubject 호출 안됨 (접근 제어)
     * 따라서 처음 조회 이후에는 캐시(cacheValue)에서 매우 빠르게 데이터를 조회할 수 있다.
     */
    @Override
    public String operation() {
        log.info("프록시 호출");

        if (cacheValue == null) {
            cacheValue = target.operation();
        }
        return cacheValue  ;
    }
}
