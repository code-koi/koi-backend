package com.codekoi.apiserver.user.repository;

import com.codekoi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQueryRepository extends JpaRepository<User, Long> {

}
