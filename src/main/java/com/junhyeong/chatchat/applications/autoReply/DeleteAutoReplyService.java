package com.junhyeong.chatchat.applications.autoReply;

import com.junhyeong.chatchat.exceptions.AutoReplyNotFound;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.autoReply.AutoReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteAutoReplyService {
    private final AutoReplyRepository autoReplyRepository;

    public DeleteAutoReplyService(AutoReplyRepository autoReplyRepository) {
        this.autoReplyRepository = autoReplyRepository;
    }

    @Transactional
    public void delete(Username username, Long id) {
        AutoReply autoReply = autoReplyRepository.findById(id)
                .orElseThrow(AutoReplyNotFound::new);

        autoReply.delete(username);
    }
}
