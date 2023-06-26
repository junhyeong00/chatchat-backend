package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.company.GetCompanyDetailService;
import com.junhyeong.chatchat.applications.company.GetCompanyProfilesService;
import com.junhyeong.chatchat.dtos.CompanyDetailDto;
import com.junhyeong.chatchat.dtos.CompanySummariesDto;
import com.junhyeong.chatchat.dtos.CompanySummaryDto;
import com.junhyeong.chatchat.dtos.PageDto;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("companies")
public class CompanyProfileController {
    private final GetCompanyProfilesService getCompanyProfilesService;
    private final GetCompanyDetailService getCompanyDetailService;

    public CompanyProfileController(GetCompanyProfilesService getCompanyProfilesService,
                                    GetCompanyDetailService getCompanyDetailService) {
        this.getCompanyProfilesService = getCompanyProfilesService;
        this.getCompanyDetailService = getCompanyDetailService;
    }

    @GetMapping
    public CompanySummariesDto companyProfiles(
            @RequestAttribute Username username,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        Page<CompanySummaryDto> found = getCompanyProfilesService
                .companyProfiles(username, keyword, page);

        List<CompanySummaryDto> companySummaries = found.stream().toList();

        PageDto pageDto = new PageDto(page, found.getTotalPages());

        return new CompanySummariesDto(companySummaries, pageDto);
    }

    @GetMapping("{id}")
    public CompanyDetailDto companyDetail(
            @RequestAttribute Username username,
            @PathVariable Long id
    ) {
        Company company = getCompanyDetailService.find(username, id);

        return company.toDetailDto();
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(CompanyNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String companyNotFound(Exception e) {
        return e.getMessage();
    }
}
