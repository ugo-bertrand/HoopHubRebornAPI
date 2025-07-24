package fr.hoophub.hoophubAPI.accountUser;

import fr.hoophub.hoophubAPI.accountUser.dto.UpdateAccountUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accountUser")
@RequiredArgsConstructor
@Slf4j
public class AccountUserController {
    private final AccountUserService accountUserService;

    @GetMapping("/{id}")
    private ResponseEntity<AccountUser> getAccountUserById(@PathVariable UUID id) throws Exception {
        log.info("Getting account user with id: {}", id);
        return ResponseEntity.ok(accountUserService.getAccountUserById(id));
    }

    @GetMapping("")
    private ResponseEntity<List<AccountUser>> getAllAccountUser(){
        log.info("Getting all accountUsers");
        return ResponseEntity.ok(accountUserService.getAllAccountUser());
    }

    @PutMapping("/{id}")
    private ResponseEntity<AccountUser> updateAccountUserById(@PathVariable UUID id, @RequestBody UpdateAccountUser updateAccountUser) throws Exception{
        log.info("Updating account user with id: {}", id);
        return ResponseEntity.ok(accountUserService.updateAccountUser(updateAccountUser,id));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteAccountUserById(@PathVariable UUID id) throws Exception {
        log.info("Deleting account user with id: {}", id);
        accountUserService.deleteAccountUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
