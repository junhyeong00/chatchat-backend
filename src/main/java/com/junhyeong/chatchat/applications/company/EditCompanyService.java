package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.dtos.EditCompanyRequest;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.models.commom.UserName;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditCompanyService {
    private final CompanyRepository companyRepository;

    public EditCompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public void edit(UserName userName, EditCompanyRequest editCompanyRequest) {
        Company company = companyRepository.findByUserName(userName)
                .orElseThrow(CompanyNotFound::new);

        company.edit(
                editCompanyRequest.getName(),
                editCompanyRequest.getDescription(),
                editCompanyRequest.getProfileImage()
        );
    }
}
