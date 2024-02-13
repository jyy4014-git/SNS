package boardproject.sns.repository;

import boardproject.sns.entity.LikeEntity;
import boardproject.sns.entity.PostEntity;
import boardproject.sns.entity.UserEntity;
import boardproject.sns.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    Optional<LikeEntity>findByUserAndPost(UserEntity user, PostEntity post);

//    엔티티 전체는 무거우니 로우 개수만 가져오게 한다
    @Query(value = "SELECT COUNT(*) from LikeEntity entity WHERE entity.post = :post")
    Integer countByPost(@Param("post") PostEntity post);
    List<LikeEntity> findAllByPost(PostEntity postEntity);
}
