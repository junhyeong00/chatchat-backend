package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.dtos.EditCompanyRequest;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditCompanyService {
    private final CompanyRepository companyRepository;

    public EditCompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public void edit(Username userName, EditCompanyRequest editCompanyRequest) {
        Company company = companyRepository.findByUsername(userName)
                .orElseThrow(Unauthorized::new);

        company.edit(
                editCompanyRequest.getName(),
                editCompanyRequest.getDescription(),
                editCompanyRequest.getProfileImage(),
                editCompanyRequest.getProfileVisibility()
        );
    }
}
