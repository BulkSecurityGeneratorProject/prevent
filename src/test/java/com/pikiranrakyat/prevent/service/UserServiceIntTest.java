package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.PreventApp;
import com.pikiranrakyat.prevent.domain.User;
import com.pikiranrakyat.prevent.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
@Transactional
public class UserServiceIntTest {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;


//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
//
//    }

    @Test
    public void assertThatUserIsExists() {
        User user = userService.createUser("avew", "password", "firstname", "lastname", "aseprojali@gmail.com", "en");

    }

//    @Test
//    public void assertThatUserMustExistToResetPassword() {
//        Optional<User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
//        assertThat(maybeUser.isPresent()).isFalse();
//
//        maybeUser = userService.requestPasswordReset("admin@localhost");
//        assertThat(maybeUser.isPresent()).isTrue();
//
//        assertThat(maybeUser.get().getEmail()).isEqualTo("admin@localhost");
//        assertThat(maybeUser.get().getResetDate()).isNotNull();
//        assertThat(maybeUser.get().getResetKey()).isNotNull();
//    }
//
//    @Test
//    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
//        User user = userService.createUser("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US");
//        Optional<User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
//        assertThat(maybeUser.isPresent()).isFalse();
//        userRepository.delete(user);
//    }
//
//    @Test
//    public void assertThatResetKeyMustNotBeOlderThan24Hours() {
//        User user = userService.createUser("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US");
//
//        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(25);
//        String resetKey = RandomUtil.generateResetKey();
//        user.setActivated(true);
//        user.setResetDate(daysAgo);
//        user.setResetKey(resetKey);
//
//        userRepository.save(user);
//
//        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
//
//        assertThat(maybeUser.isPresent()).isFalse();
//
//        userRepository.delete(user);
//    }
//
//    @Test
//    public void assertThatResetKeyMustBeValid() {
//        User user = userService.createUser("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US");
//
//        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(25);
//        user.setActivated(true);
//        user.setResetDate(daysAgo);
//        user.setResetKey("1234");
//        userRepository.save(user);
//        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
//        assertThat(maybeUser.isPresent()).isFalse();
//        userRepository.delete(user);
//    }
//
//    @Test
//    public void assertThatUserCanResetPassword() {
//        User user = userService.createUser("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US");
//        String oldPassword = user.getPassword();
//        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(2);
//        String resetKey = RandomUtil.generateResetKey();
//        user.setActivated(true);
//        user.setResetDate(daysAgo);
//        user.setResetKey(resetKey);
//        userRepository.save(user);
//        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
//        assertThat(maybeUser.isPresent()).isTrue();
//        assertThat(maybeUser.get().getResetDate()).isNull();
//        assertThat(maybeUser.get().getResetKey()).isNull();
//        assertThat(maybeUser.get().getPassword()).isNotEqualTo(oldPassword);
//
//        userRepository.delete(user);
//    }
//
//    @Test
//    public void testFindNotActivatedUsersByCreationDateBefore() {
//        userService.removeNotActivatedUsers();
//        ZonedDateTime now = ZonedDateTime.now();
//        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
//        assertThat(users).isEmpty();
//    }
}
