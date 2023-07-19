package com.junhyeong.chatchat.controllers.company;

import com.junhyeong.chatchat.applications.company.CreateCompanyService;
import com.junhyeong.chatchat.applications.company.DeleteCompanyService;
import com.junhyeong.chatchat.applications.company.EditCompanyPasswordService;
import com.junhyeong.chatchat.applications.company.EditCompanyService;
import com.junhyeong.chatchat.applications.company.GetCompanyProfileService;
import com.junhyeong.chatchat.dtos.CompanyProfileDto;
import com.junhyeong.chatchat.dtos.CreateCompanyRequest;
import com.junhyeong.chatchat.dtos.CreateCompanyRequestDto;
import com.junhyeong.chatchat.dtos.DeleteCompanyRequestDto;
import com.junhyeong.chatchat.dtos.EditCompanyPasswordRequest;
import com.junhyeong.chatchat.dtos.EditCompanyPasswordRequestDto;
import com.junhyeong.chatchat.dtos.EditCompanyRequest;
import com.junhyeong.chatchat.dtos.EditCompanyRequestDto;
import com.junhyeong.chatchat.exceptions.AuthenticationFailed;
import com.junhyeong.chatchat.exceptions.CreateCompanyFailed;
import com.junhyeong.chatchat.exceptions.EditCompanyFailed;
import com.junhyeong.chatchat.exceptions.EditCompanyPasswordFailed;
import com.junhyeong.chatchat.exceptions.SameAsPreviousPassword;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.exceptions.UsernameAlreadyInUse;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private final GetCompanyProfileService getCompanyProfileService;
    private final CreateCompanyService createCompanyService;
    private final EditCompanyService editCompanyService;
    private final EditCompanyPasswordService editCompanyPasswordService;
    private final DeleteCompanyService deleteCompanyService;

    public CompanyController(GetCompanyProfileService getCompanyProfileService,
                             CreateCompanyService createCompanyService,
                             EditCompanyService editCompanyService,
                             EditCompanyPasswordService editCompanyPasswordService,
                             DeleteCompanyService deleteCompanyService) {
        this.getCompanyProfileService = getCompanyProfileService;
        this.createCompanyService = createCompanyService;
        this.editCompanyService = editCompanyService;
        this.editCompanyPasswordService = editCompanyPasswordService;
        this.deleteCompanyService = deleteCompanyService;
    }

    @GetMapping("me")
    public CompanyProfileDto companyProfile(
            @RequestAttribute Username username
    ) {
        Company company = getCompanyProfileService.find(username);

        return company.toProfileDto();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(
            @Validated @RequestBody CreateCompanyRequestDto createCompanyRequestDto
    ) {
        try {
            CreateCompanyRequest createCompanyRequest = CreateCompanyRequest.of(createCompanyRequestDto);

            createCompanyService.create(createCompanyRequest);
        } catch (UsernameAlreadyInUse e) {
            throw new UsernameAlreadyInUse();
        } catch (Exception e) {
            throw new CreateCompanyFailed(e.getMessage());
        }
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

    @PatchMapping("me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editPassword(
            @RequestAttribute Username username,
            @Validated @RequestBody EditCompanyPasswordRequestDto editCompanyPasswordRequestDto
    ) {
        try {
            EditCompanyPasswordRequest editCompanyPasswordRequest
                    = EditCompanyPasswordRequest.of(editCompanyPasswordRequestDto);

            editCompanyPasswordService.edit(username, editCompanyPasswordRequest);
        } catch (Unauthorized e) {
            throw new Unauthorized();
        } catch (AuthenticationFailed e) {
            throw new AuthenticationFailed();
        } catch (SameAsPreviousPassword e) {
            throw new SameAsPreviousPassword();
        } catch (Exception e) {
            throw new EditCompanyPasswordFailed(e.getMessage());
        }
    }

    @DeleteMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void secession(
            @RequestAttribute Username username,
            @Validated @RequestBody DeleteCompanyRequestDto deleteCompanyRequestDto
    ) {
        Password password = new Password(deleteCompanyRequestDto.password());

        deleteCompanyService.delete(username, password);
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(UsernameAlreadyInUse.class)
    public String usernameAlreadyInUse(Exception e, HttpServletResponse response) {
        response.setStatus(480);

        return e.getMessage();
    }

    @ExceptionHandler(AuthenticationFailed.class)
    public String authenticationFailed(Exception e, HttpServletResponse response) {
        response.setStatus(481);

        return e.getMessage();
    }

    @ExceptionHandler(SameAsPreviousPassword.class)
    public String sameAsPreviousPassword(Exception e, HttpServletResponse response) {
        response.setStatus(482);

        return e.getMessage();
    }

    @ExceptionHandler(CreateCompanyFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String createCompanyFailed(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(EditCompanyFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String editCompanyFailed(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(EditCompanyPasswordFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String editCompanyPasswordFailed(Exception e) {
        return e.getMessage();
    }
}
