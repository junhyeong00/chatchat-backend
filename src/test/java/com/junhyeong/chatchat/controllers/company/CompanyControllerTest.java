package com.junhyeong.chatchat.controllers.company;

import com.junhyeong.chatchat.applications.company.CreateCompanyService;
import com.junhyeong.chatchat.applications.company.DeleteCompanyService;
import com.junhyeong.chatchat.applications.company.EditCompanyPasswordService;
import com.junhyeong.chatchat.applications.company.EditCompanyService;
import com.junhyeong.chatchat.applications.company.GetCompanyProfileService;
import com.junhyeong.chatchat.exceptions.AuthenticationFailed;
import com.junhyeong.chatchat.exceptions.NotMatchPassword;
import com.junhyeong.chatchat.exceptions.SameAsPreviousPassword;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.exceptions.UsernameAlreadyInUse;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCompanyProfileService getCompanyProfileService;

    @MockBean
    private CreateCompanyService createCompanyService;

    @MockBean
    private EditCompanyService editCompanyService;

    @MockBean
    private EditCompanyPasswordService editCompanyPasswordService;

    @MockBean
    private DeleteCompanyService deleteCompanyService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void companyProfile() throws Exception {
        Username userName = new Username("company123");
        String token = jwtUtil.encode(userName);

        given(getCompanyProfileService.find(userName))
                .willReturn(Company.fake(userName));

        mockMvc.perform(MockMvcRequestBuilders.get("/companies/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void companyProfileWithCompanyNotFound() throws Exception {
        Username invalidUserName = new Username("xxx");
        String token = jwtUtil.encode(invalidUserName);

        given(getCompanyProfileService.find(invalidUserName))
                .willThrow(Unauthorized.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/companies/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void editCompany() throws Exception {
        Username userName = new Username("company123");
        String token = jwtUtil.encode(userName);

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"악덕기업\", " +
                                "   \"description\":\"악덕기업입니다\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void editCompanyWithBlankName() throws Exception {
        Username userName = new Username("company123");
        String token = jwtUtil.encode(userName);

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"\", " +
                                "   \"description\":\"악덕기업입니다\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editCompanyWithInvalidName() throws Exception {
        Username userName = new Username("company123");
        String token = jwtUtil.encode(userName);

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"123456789012345678901234567890\", " +
                                "   \"description\":\"악덕기업입니다\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editCompanyWithCompanyNotFound() throws Exception {
        Username invalidUserName = new Username("xxx");
        String token = jwtUtil.encode(invalidUserName);

        doAnswer(invocation -> {
            throw new Unauthorized();
        }).when(editCompanyService).edit(any(),any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"악덕기업\", " +
                                "   \"description\":\"악덕기업입니다\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/companies")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"배준형\"," +
                                "\"username\":\"jhbae0420\"," +
                                "\"password\":\"Password1234!\"," +
                                "\"confirmPassword\":\"Password1234!\"" +
                                "}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createWithBlankName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/companies")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"\"," +
                                "\"username\":\"jhbae0420\"," +
                                "\"password\":\"Password1234!\"," +
                                "\"confirmPassword\":\"Password1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithBlankUsername() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/companies")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"배준형\"," +
                                "\"username\":\"\"," +
                                "\"password\":\"Password1234!\"," +
                                "\"confirmPassword\":\"Password1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithBlankPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/companies")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"배준형\"," +
                                "\"username\":\"jhbae0420\"," +
                                "\"password\":\"\"," +
                                "\"confirmPassword\":\"Password1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithBlankConfirmPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/companies")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"배준형\"," +
                                "\"username\":\"jhbae0420\"," +
                                "\"password\":\"Password1234!\"," +
                                "\"confirmPassword\":\"\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithAlreadyExistingUserName() throws Exception {
        doAnswer(invocation -> {
            throw new UsernameAlreadyInUse();
        }).when(createCompanyService).create(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/companies")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"배준형\"," +
                                "\"username\":\"jhbae0420\"," +
                                "\"password\":\"Password1234!\"," +
                                "\"confirmPassword\":\"Password1234!\"" +
                                "}"))
                .andExpect(status().is(480));
    }

    @Test
    void editPassword() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me/password")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"Password1234!\"," +
                                "\"newPassword\":\"newPassword1234!\"," +
                                "\"confirmNewPassword\":\"newPassword1234!\"" +
                                "}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void editPasswordWithUnauthorized() throws Exception {
        Username invalidUserName = new Username("xxx");
        String token = jwtUtil.encode(invalidUserName);

        doAnswer(invocation -> {
            throw new Unauthorized();
        }).when(editCompanyPasswordService).edit(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me/password")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"Password1234!\"," +
                                "\"newPassword\":\"newPassword1234!\"," +
                                "\"confirmNewPassword\":\"newPassword1234!\"" +
                                "}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void editPasswordWithBlankPassword() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me/password")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"\"," +
                                "\"newPassword\":\"newPassword1234!\"," +
                                "\"confirmNewPassword\":\"newPassword1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editPasswordWithBlankNewPassword() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me/password")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"Password1234!\"," +
                                "\"newPassword\":\"\"," +
                                "\"confirmNewPassword\":\"newPassword1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editPasswordWithBlankConfirmNewPassword() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me/password")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"Password1234!\"," +
                                "\"newPassword\":\"newPassword1234!\"," +
                                "\"confirmNewPassword\":\"\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editPasswordWithNotMatchPassword() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        doAnswer(invocation -> {
            throw new NotMatchPassword();
        }).when(editCompanyPasswordService).edit(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me/password")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"Password1234!\"," +
                                "\"newPassword\":\"newPassword1234!\"," +
                                "\"confirmNewPassword\":\"newPassword321!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editPasswordWithSameAsPreviousPassword() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        doAnswer(invocation -> {
            throw new SameAsPreviousPassword();
        }).when(editCompanyPasswordService).edit(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me/password")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"Password1234!\"," +
                                "\"newPassword\":\"Password1234!\"," +
                                "\"confirmNewPassword\":\"Password1234!\"" +
                                "}"))
                .andExpect(status().is(482));
    }

    @Test
    void editPasswordWithWrongPassword() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        doAnswer(invocation -> {
            throw new AuthenticationFailed();
        }).when(editCompanyPasswordService).edit(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me/password")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"Xxxxxx321!\"," +
                                "\"newPassword\":\"newPassword1234!\"," +
                                "\"confirmNewPassword\":\"newPassword1234!\"" +
                                "}"))
                .andExpect(status().is(481));
    }

    @Test
    void delete() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.delete("/companies/me")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"Password1234!\"" +
                                "}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWithUnauthorized() throws Exception {
        Username invalidUserName = new Username("xxx");
        String token = jwtUtil.encode(invalidUserName);

        doAnswer(invocation -> {
            throw new Unauthorized();
        }).when(deleteCompanyService).delete(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/companies/me")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"Password1234!\"" +
                                "}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteWithWrongPassword() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        doAnswer(invocation -> {
            throw new AuthenticationFailed();
        }).when(deleteCompanyService).delete(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/companies/me")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"Xxxxxx321!\"" +
                                "}"))
                .andExpect(status().is(481));
    }
}
