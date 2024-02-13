package boardproject.sns.service;

import boardproject.sns.entity.UserEntity;
import boardproject.sns.exception.SnsException;
import boardproject.sns.fixture.UserEntityFixture;
import boardproject.sns.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void 회원가입정상동작(){
//        given
        String userName = "userName";
        String password = "password";

//        when 회원가입시  DB에 정보가 없어야한다

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);


        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encode_password");
        when(userRepository.save(any())).thenReturn(UserEntityFixture.get(userName, password, 1));


        Assertions.assertDoesNotThrow(() -> userService.join(fixture.getUserName(), fixture.getPassword()));
    }

    @Test
    void 회원가입시기존회원인경우(){
//        given
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

//        when 회원가입시  DB에 정보가 없어야한다
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encode_password");
        when(userRepository.save(any())).thenReturn(Optional.of(fixture));


        Assertions.assertThrows(SnsException.class, () -> userService.join(userName, password));
    }

    @Test
    void 로그인정상동작(){
//        given
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(bCryptPasswordEncoder.matches(password, fixture.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(userName, password));
    }

    @Test
    void 로그인시유저가없음(){
//        given
        String userName = "userName";
        String password = "password";

//        when 회원가입시  DB에 정보가 없어야한다
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        Assertions.assertThrows(SnsException.class, () -> userService.login(userName, password));
    }

    @Test
    void 로그인시유저는있으나패스워드가틀림(){
//        given
        String userName = "userName";
        String password = "password";
        String failPassword = "1234";
        UserEntity fixture = UserEntityFixture.get(userName, password, 1);


//        when 회원가입시  DB에 정보가 없어야한다
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        Assertions.assertThrows(SnsException.class, () -> userService.login(userName, failPassword));
    }

}