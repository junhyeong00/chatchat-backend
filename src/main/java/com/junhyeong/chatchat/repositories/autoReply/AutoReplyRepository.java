package com.junhyeong.chatchat.repositories.autoReply;

import com.junhyeong.chatchat.models.autoReply.AutoReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoReplyRepository extends JpaRepository<AutoReply, Long>, AutoReplyRepositoryQueryDsl {
}
