package com.junhyeong.chatchat.controllers.company;

import com.junhyeong.chatchat.applications.company.EditCompanyService;
import com.junhyeong.chatchat.applications.company.GetCompanyProfileService;
import com.junhyeong.chatchat.dtos.CompanyProfileDto;
import com.junhyeong.chatchat.dtos.EditCompanyRequest;
import com.junhyeong.chatchat.dtos.EditCompanyRequestDto;
import com.junhyeong.chatchat.exceptions.EditCompanyFailed;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private final GetCompanyProfileService getCompanyProfileService;
    private final EditCompanyService editCompanyService;

    public CompanyController(GetCompanyProfileService getCompanyProfileService,
                             EditCompanyService editCompanyService) {
        this.getCompanyProfileService = getCompanyProfileService;
        this.editCompanyService = editCompanyService;
    }

    @GetMapping("me")
    public CompanyProfileDto companyProfile(
            @RequestAttribute Username username
    ) {
        Company company = getCompanyProfileService.find(username);

        return company.toProfileDto();
    }

    @PatchMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(
            @RequestAttribute Username username,
            @Validated @RequestBody EditCompanyRequestDto editCompanyRequestDto
    ) {
        try {
            EditCompanyRequest editCompanyRequest = EditCompanyRequest.of(editCompanyRequestDto);

            editCompanyService.edit(username, editCompanyRequest);
        } catch (Unauthorized e) {
            throw new Unauthorized();
        } catch (Exception e) {
            throw new EditCompanyFailed(e.getMessage());
        }
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(EditCompanyFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String editCompanyFailed(Exception e) {
        return e.getMessage();
    }
}
