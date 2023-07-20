package com.junhyeong.chatchat.applications.autoReply;

import com.junhyeong.chatchat.dtos.AutoReplyQuestionDto;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.autoReply.AutoReplyRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetAutoReplyQuestionsService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final AutoReplyRepository autoReplyRepository;

    public GetAutoReplyQuestionsService(CustomerRepository customerRepository,
                                        CompanyRepository companyRepository,
                                        AutoReplyRepository autoReplyRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.autoReplyRepository = autoReplyRepository;
    }

    @Transactional(readOnly = true)
    public List<AutoReplyQuestionDto> questions(Username username, Long companyId) {
        if (!customerRepository.existsByUsername(username)) {
            throw new Unauthorized();
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(CompanyNotFound::new);

        List<AutoReply> autoReplies = autoReplyRepository.findAllByCompanyUsername(company.username());

        return autoReplies.stream().map(AutoReply::toQuestionDto).toList();
    }
}
