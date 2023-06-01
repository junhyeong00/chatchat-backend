package com.junhyeong.chatchat.controllers;

import com.junhyeong.chatchat.applications.company.GetCompanyProfileService;
import com.junhyeong.chatchat.dtos.CompanyProfileDto;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.models.commom.UserName;
import com.junhyeong.chatchat.models.company.Company;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private final GetCompanyProfileService getCompanyProfileService;

    public CompanyController(GetCompanyProfileService getCompanyProfileService) {
        this.getCompanyProfileService = getCompanyProfileService;
    }

    @GetMapping("me")
    public CompanyProfileDto companyProfile(
            @RequestAttribute UserName userName
    ) {
        Company company = getCompanyProfileService.find(userName);

        return company.toProfileDto();
    }

    @ExceptionHandler(CompanyNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String companyNotFound(Exception e) {
        return e.getMessage();
    }
}
