package boardproject.sns.repository;

import boardproject.sns.entity.CommentEntity;
import boardproject.sns.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    Page<CommentEntity> findALLByPost(PostEntity post, Pageable page);
}
