package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.customer.EditCustomerService;
import com.junhyeong.chatchat.applications.customer.GetCustomerProfileService;
import com.junhyeong.chatchat.exceptions.Unauthorized;
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
    private EditCustomerService editCustomerService;

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
        }).when(editCustomerService).edit(any(),any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/customers/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"고객\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isUnauthorized());
    }
}
