package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.entity.SOSboardcomment;

import java.util.List;
import java.util.Optional;

public interface SOSBoardCommentRepository extends JpaRepository<SOSboardcomment,Long> {

    List<SOSboardcomment> findBySosboard_Sosboardseq(Long sosboardseq);

    Optional<SOSboardcomment> findBySosboard_SosboardseqAndUser_Id(Long sosboardseq, Long userId);

}
