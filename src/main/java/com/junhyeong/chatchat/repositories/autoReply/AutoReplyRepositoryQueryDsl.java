package com.junhyeong.chatchat.repositories.autoReply;

import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;

import java.util.List;

public interface AutoReplyRepositoryQueryDsl {
    List<AutoReply> findAllByCompanyUsername(Username username);

}
