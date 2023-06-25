package com.junhyeong.chatchat.repositories.company;

import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyRepositoryQueryDsl {
    Optional<Company> findByUsername(Username username);
}
