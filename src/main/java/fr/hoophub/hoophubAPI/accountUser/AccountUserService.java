package fr.hoophub.hoophubAPI.accountUser;

import fr.hoophub.hoophubAPI.accountUser.dto.CreateAccountUser;
import fr.hoophub.hoophubAPI.accountUser.dto.UpdateAccountUser;
import fr.hoophub.hoophubAPI.common.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface AccountUserService {
    AccountUser createAccountUser(CreateAccountUser accountUser);
    AccountUser updateAccountUser(UpdateAccountUser accountUser, UUID accountUserId) throws Exception;
    AccountUser getAccountUserById(UUID accountUserId) throws BaseException, Exception;
    List<AccountUser> getAllAccountUser();
    void deleteAccountUserById(UUID accountUserId) throws Exception;
    AccountUser getAccountUserByEmail(String email) throws BaseException;
}
