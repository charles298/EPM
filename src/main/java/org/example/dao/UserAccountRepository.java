package org.example.dao;

import org.example.entities.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    UserAccount findByUserName(String username);
}
