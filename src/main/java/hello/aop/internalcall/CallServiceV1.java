package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

    private CallServiceV1 callServiceV1;

    /**
     * 참고로 생성자 주입시 순환 참조 문제 만들기 때문에 실패한다
     *
     *
     * 스프링 부트 2.6부터 순환 참조 금지하도록 정책 변경
     * 허용 하려면 설정 추가 필요
     * spring.main.allow-circular-references=true
     *
     * @Lazy 애노테이션 사용하는 방법도 가능
     * https://www.baeldung.com/circular-dependencies-in-spring
     * https://github.com/spring-projects/spring-boot/issues/27652
     * @param callServiceV1
     */
    @Autowired
    public void setCallServiceV1(@Lazy CallServiceV1 callServiceV1) {
        log.info("callServiceV1 setter = {}", callServiceV1.getClass());
        this.callServiceV1 = callServiceV1;
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal(); // 외부 메서드 호출
    }

    public void internal() {
        log.info("call internal");
    }
}
