package com.pikiranrakyat.prevent.aop.logging;

import com.pikiranrakyat.prevent.domain.AbstractAuditingEntityWithUser;
import com.pikiranrakyat.prevent.domain.User;
import com.pikiranrakyat.prevent.exception.ResourceNotFoundException;
import com.pikiranrakyat.prevent.repository.UserRepository;
import com.pikiranrakyat.prevent.security.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by adildramdan on 3/24/15.
 */
@Aspect
public class UserAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    UserRepository userRepository;

    @Pointcut("execution(com.pikiranrakyat.prevent.repository..*) || execution(com.pikiranrakyat.prevent.service..*)")
    public void userPointcut() {
    }

    @Before("userPointcut()")
    public void addUser(JoinPoint jp) {
        Object o = jp.getArgs()[0];
        log.info("run...User acpect");
        Optional<User> login = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (login.isPresent()) {
            if (o instanceof AbstractAuditingEntityWithUser) {
                log.info("Add User to Entity..");
                ((AbstractAuditingEntityWithUser) o).setUser(
                    login.orElseThrow(() -> new ResourceNotFoundException("User tidak ada"))
                );
            }
        }


    }
}
