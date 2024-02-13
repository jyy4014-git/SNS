package boardproject.sns.service;

import boardproject.sns.entity.UserEntity;
import boardproject.sns.exception.ErrorCode;
import boardproject.sns.exception.SnsException;
import boardproject.sns.model.Alarm;
import boardproject.sns.model.User;
import boardproject.sns.repository.AlarmRepository;
import boardproject.sns.repository.UserRepository;
import boardproject.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final BCryptPasswordEncoder passwordEncoder;

//    config 설정값 참조

    private String secretKey = "testSecretKey20230327testSecretKey20230327testSecretKey2023032";

    @Transactional
    public User join(String userName, String password){

//        기존회원인지 체크
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s 는 중복회원입니다", userName));
        });

//        회원가입 진행
        UserEntity userEntity = userRepository.save(UserEntity.of(userName, passwordEncoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    //로그인시 jwt 토큰을 반환하기 때문에 String으로 return
    public String login(String userName, String password){
//        회원가입 여부체크
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() ->
                new SnsException(ErrorCode.USER_NOT_FOUND, String.format("%s 회원을 찾을 수 없습니다", userName)));

//        User savedUser = loadUserByUserName(userName);

//        비밀번호 체크
        if(!passwordEncoder.matches(password, userEntity.getPassword())){
            throw new SnsException(ErrorCode.INVALID_PASSWORD);
        }
//        토큰 생성
        return JwtTokenUtils.generateAccessToken(userName, secretKey, 2592000000L);
    }

    public User loadUserByUserName(String userName) throws UsernameNotFoundException {
        return userRepository.findByUserName(userName).map(User :: fromEntity).orElseThrow(() ->
                new SnsException(ErrorCode.USER_NOT_FOUND, String.format("%s 회원을 찾을 수 없습니다", userName)));
    }

    public Page<Alarm> alamList(String userName, Pageable pageable) {

        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() ->
                new SnsException(ErrorCode.USER_NOT_FOUND, String.format("%s 회원을 찾을 수 없습니다", userName)));

        return alarmRepository.findAllByUser(userEntity, pageable).map(Alarm::fromEntity);
    }
}
