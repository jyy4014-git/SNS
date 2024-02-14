package boardproject.sns.repository;

import boardproject.sns.config.RedisConfig;
import boardproject.sns.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserCacheRepository {

    private final RedisTemplate<String, User> userRedisTemplate;
    private final static Duration CACHE_TTL = Duration.ofDays(1); //1일동안 캐싱


    public void setUser(User user) {
        String key = getKey(user.getUsername());
        log.info("Set User to Redis {}({})", key, user);
        userRedisTemplate.opsForValue().setIfAbsent(key, user, CACHE_TTL);
    }

    public Optional<User> getUser(String userName) {
        User data = userRedisTemplate.opsForValue().get(getKey(userName));
        log.info("Get User from Redis {}", data);
        return Optional.ofNullable(data);
    }


    private String getKey(String userName) {
        return "USER:" + userName;
    }
}

