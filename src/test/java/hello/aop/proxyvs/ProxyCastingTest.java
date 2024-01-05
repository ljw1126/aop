package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); // JDK 동적 프록시

        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        log.info("proxy class = {}", memberServiceProxy.getClass()); //  proxy class = class jdk.proxy3.$Proxy11

        // JDK 동적 프록시를 구현 클래스로 캐스팅 시도할 경우 실패한다
        assertThatThrownBy(() -> {
            MemberServiceImpl memberServiceImpl = (MemberServiceImpl) memberServiceProxy;
        }).isInstanceOf(ClassCastException.class);
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // CGLIB

        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        log.info("proxy class = {}", memberServiceProxy.getClass()); // proxy class = class hello.aop.member.MemberServiceImpl$$SpringCGLIB$$0

        // CGLIB 프록시를 구현 클래스로 캐스팅 시도할 경우 성공한다
        MemberServiceImpl memberServiceImpl = (MemberServiceImpl) memberServiceProxy;
    }
}
