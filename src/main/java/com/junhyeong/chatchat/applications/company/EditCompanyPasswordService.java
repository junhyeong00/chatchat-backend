package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.dtos.EditCompanyPasswordRequest;
import com.junhyeong.chatchat.exceptions.NotMatchPassword;
import com.junhyeong.chatchat.exceptions.SameAsPreviousPassword;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditCompanyPasswordService {
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public EditCompanyPasswordService(CompanyRepository companyRepository,
                                      PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void edit(Username username, EditCompanyPasswordRequest editCompanyRequest) {
        if (!editCompanyRequest.getNewPassword().equals(editCompanyRequest.getConfirmNewPassword())) {
            throw new NotMatchPassword();
        }

        Company company = companyRepository.findByUsername(username)
                .orElseThrow(Unauthorized::new);

        company.authenticate(editCompanyRequest.getPassword(), passwordEncoder);

        if (editCompanyRequest.getPassword().equals(editCompanyRequest.getNewPassword())) {
            throw new SameAsPreviousPassword();
        }

        company.changePassword(editCompanyRequest.getNewPassword(), passwordEncoder);
    }
}
