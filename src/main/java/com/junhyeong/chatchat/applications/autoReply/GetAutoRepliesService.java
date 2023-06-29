package com.junhyeong.chatchat.applications.autoReply;

import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.autoReply.AutoReplyRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetAutoRepliesService {
    private final CompanyRepository companyRepository;
    private final AutoReplyRepository autoReplyRepository;

    public GetAutoRepliesService(CompanyRepository companyRepository,
                                 AutoReplyRepository autoReplyRepository) {
        this.companyRepository = companyRepository;
        this.autoReplyRepository = autoReplyRepository;
    }

    @Transactional(readOnly = true)
    public List<AutoReply> autoReplies(Username username) {
        Company company = companyRepository.findByUsername(username)
                .orElseThrow(Unauthorized::new);

        return autoReplyRepository.findAllByCompanyUsername(company.username());
    }
}
