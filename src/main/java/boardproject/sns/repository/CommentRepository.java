package boardproject.sns.repository;

import boardproject.sns.entity.CommentEntity;
import boardproject.sns.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    Page<CommentEntity> findALLByPost(PostEntity post, Pageable page);

    @Transactional // 전체 삭제를 위해 사용
    @Modifying //업데이트문에 달아줘야 정상작동한다
    @Query("UPDATE CommentEntity entity SET entity.deletedAt = CURRENT_TIMESTAMP where entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity postEntity);
}
