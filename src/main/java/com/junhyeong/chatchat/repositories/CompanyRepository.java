package com.junhyeong.chatchat.repositories;

import com.junhyeong.chatchat.models.commom.UserName;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUserName(UserName userName);
}
