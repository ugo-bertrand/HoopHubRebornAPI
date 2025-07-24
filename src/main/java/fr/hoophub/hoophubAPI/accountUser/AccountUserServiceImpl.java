package fr.hoophub.hoophubAPI.accountUser;

import fr.hoophub.hoophubAPI.accountUser.dto.CreateAccountUser;
import fr.hoophub.hoophubAPI.accountUser.dto.UpdateAccountUser;
import fr.hoophub.hoophubAPI.accountUser.util.CreateAccountMapper;
import fr.hoophub.hoophubAPI.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountUserServiceImpl implements AccountUserService{

    private final AccountUserRepository accountUserRepository;


    @Override
    public AccountUser createAccountUser(CreateAccountUser createAccountUser) {
        AccountUser accountUser = CreateAccountMapper.INSTANCE.toDto(createAccountUser);
        log.info("Result of the mapping of the accountUser with createAccountUser: {}", accountUser);
        return accountUserRepository.save(accountUser);
    }

    @Override
    public AccountUser updateAccountUser(UpdateAccountUser updateAccountUser, UUID accountUserId) throws Exception{
        Optional<AccountUser> accountUserOptional = accountUserRepository.findById(accountUserId);
        if (accountUserOptional.isEmpty()){
            throw new BaseException("The user does not exist", HttpStatus.NOT_FOUND);
        }
        if(updateAccountUser.getFirstname().isEmpty()) {
            accountUserOptional.get().setFirstname(updateAccountUser.getFirstname());
        }
        if(updateAccountUser.getLastname().isEmpty()){
            accountUserOptional.get().setLastname(updateAccountUser.getLastname());
        }
        if(updateAccountUser.getUsername().isEmpty()){
            // check if the username is not already use
            accountUserOptional.get().setUsername(updateAccountUser.getUsername());
        }
        if(updateAccountUser.getEmail().isEmpty()){
            // check if the email is not already use
            accountUserOptional.get().setEmail(updateAccountUser.getEmail());
        }
        if(updateAccountUser.getPhone().isEmpty()){
            accountUserOptional.get().setPhone(updateAccountUser.getPhone());
        }
        if(updateAccountUser.getPassword().isEmpty()){
            // add encryption to the password
            accountUserOptional.get().setPassword(updateAccountUser.getPassword());
        }
        accountUserOptional.get().setUpdatedAt(LocalDateTime.now());
        return accountUserRepository.save(accountUserOptional.get());
    }

    @Override
    public AccountUser getAccountUserById(UUID accountUserId) throws Exception {
        return accountUserRepository.findById(accountUserId).orElseThrow(() -> new BaseException("The account user is not found",HttpStatus.NOT_FOUND));
    }

    @Override
    public List<AccountUser> getAllAccountUser() {
        return accountUserRepository.findAll();
    }

    @Override
    public void deleteAccountUserById(UUID accountUserId) throws Exception {
        Optional<AccountUser> accountUserOptional = accountUserRepository.findById(accountUserId);
        if(accountUserOptional.isEmpty()){
            throw new BaseException("The account user is not found",HttpStatus.NOT_FOUND);
        }
        accountUserRepository.deleteById(accountUserId);
    }

    @Override
    public AccountUser getAccountUserByEmail(String email) throws BaseException {
        Optional<AccountUser> accountUserOptional = accountUserRepository.findByEmail(email);
        if(accountUserOptional.isEmpty()){
            throw new BaseException("The account with the following email address does not exist",HttpStatus.NOT_FOUND);
        }
        return accountUserOptional.get();
    }
}
