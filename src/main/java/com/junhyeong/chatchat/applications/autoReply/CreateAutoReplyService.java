package com.junhyeong.chatchat.applications.autoReply;

import com.junhyeong.chatchat.dtos.CreateAutoReplyRequest;
import com.junhyeong.chatchat.exceptions.ExceededNumberAutoReply;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.Answer;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.autoReply.Question;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.autoReply.AutoReplyRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateAutoReplyService {
    private final CompanyRepository companyRepository;
    private final AutoReplyRepository autoReplyRepository;

    public CreateAutoReplyService(CompanyRepository companyRepository,
                                  AutoReplyRepository autoReplyRepository) {
        this.companyRepository = companyRepository;
        this.autoReplyRepository = autoReplyRepository;
    }

    @Transactional
    public Long create(Username username, CreateAutoReplyRequest createAutoReplyRequest) {
        Company company = companyRepository.findByUsername(username)
                .orElseThrow(Unauthorized::new);

        AutoReply autoReply = new AutoReply(
                company.username(),
                createAutoReplyRequest.getQuestion(),
                createAutoReplyRequest.getAnswer()
        );

        Long autoReplyCount = autoReplyRepository.countByUsername(username);

        validateCount(autoReplyCount);

        AutoReply saved = autoReplyRepository.save(autoReply);

        return saved.id();
    }

    private void validateCount(Long autoReplyCount) {
        if (autoReplyCount >= 6) {
            throw new ExceededNumberAutoReply();
        }
    }
}
