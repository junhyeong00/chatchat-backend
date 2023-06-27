package com.junhyeong.chatchat.controllers.customer;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCustomerProfileService getCustomerProfileService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void customerProfile() throws Exception {
        Username userName = new Username("customer123");
        String token = jwtUtil.encode(userName);

        given(getCustomerProfileService.find(userName))
                .willReturn(Customer.fake(userName));

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
}
