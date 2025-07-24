package fr.hoophub.hoophubAPI.accountUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, UUID> {

    Optional<AccountUser> findByEmail(String email);
}
