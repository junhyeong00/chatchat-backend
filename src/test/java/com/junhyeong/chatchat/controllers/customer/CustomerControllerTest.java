package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.customer.CreateCustomerService;
import com.junhyeong.chatchat.applications.customer.DeleteCustomerService;
import com.junhyeong.chatchat.applications.customer.EditCustomerPasswordService;
import com.junhyeong.chatchat.applications.customer.EditCustomerService;
import com.junhyeong.chatchat.applications.customer.GetCustomerProfileService;
import com.junhyeong.chatchat.exceptions.AuthenticationFailed;
import com.junhyeong.chatchat.exceptions.NotMatchPassword;
import com.junhyeong.chatchat.exceptions.SameAsPreviousPassword;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.exceptions.UsernameAlreadyInUse;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
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

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCustomerProfileService getCustomerProfileService;

    @MockBean
    private CreateCustomerService createCustomerService;

    @MockBean
    private EditCustomerService editCustomerService;

    @MockBean
    private EditCustomerPasswordService editCustomerPasswordService;

    @MockBean
    private DeleteCustomerService deleteCustomerService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void customerProfile() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        given(getCustomerProfileService.find(username))
                .willReturn(Customer.fake(username));

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void customerProfileWithCustomerNotFound() throws Exception {
        Username invalidUserName = new Username("xxx");
        String token = jwtUtil.encode(invalidUserName);

        given(getCustomerProfileService.find(invalidUserName))
                .willThrow(Unauthorized.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void editCustomer() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"고객\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void editCustomerWithBlankName() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editCustomerWithInvalidName() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"123456789012345678901234567890\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editCustomerWithUnauthorized() throws Exception {
        Username invalidUserName = new Username("xxx");
        String token = jwtUtil.encode(invalidUserName);

        doAnswer(invocation -> {
            throw new Unauthorized();
        }).when(editCustomerService).edit(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"고객\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
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
        }).when(createCustomerService).create(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
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
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me/password")
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
        }).when(editCustomerPasswordService).edit(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me/password")
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
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me/password")
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
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me/password")
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
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me/password")
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
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        doAnswer(invocation -> {
            throw new NotMatchPassword();
        }).when(editCustomerPasswordService).edit(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me/password")
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
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        doAnswer(invocation -> {
            throw new SameAsPreviousPassword();
        }).when(editCustomerPasswordService).edit(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me/password")
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
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        doAnswer(invocation -> {
            throw new AuthenticationFailed();
        }).when(editCustomerPasswordService).edit(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me/password")
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

    void delete() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/me")
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
        }).when(deleteCustomerService).delete(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/me")
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
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        doAnswer(invocation -> {
            throw new AuthenticationFailed();
        }).when(deleteCustomerService).delete(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/me")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"password\":\"Xxxxxx321!\"" +
                                "}"))
                .andExpect(status().is(481));
    }
}
