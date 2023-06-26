package com.junhyeong.chatchat.backdoor;

import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backdoor")
public class BackdoorController {
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public BackdoorController(CustomerRepository customerRepository,
                              CompanyRepository companyRepository,
                              PasswordEncoder passwordEncoder,
                              JdbcTemplate jdbcTemplate) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/customers")
    public String customers() {
        jdbcTemplate.execute("DELETE FROM customer");

        Customer customer1 = new Customer(new Username("customer1"), new Name("고객1"));

        customer1.changePassword(new Password("Password1234!"), passwordEncoder);

        customerRepository.save(customer1);

        return "ok";
    }

    @GetMapping("/companies")
    public String companies() {
        jdbcTemplate.execute("DELETE FROM company");

        Company company1 = new Company(new Username("company1"), new Name("기업1"));

        company1.changePassword(new Password("Password1234!"), passwordEncoder);

        companyRepository.save(company1);

        return "ok";
    }
}
