package fr.hoophub.hoophubAPI.accountUser.util;

import fr.hoophub.hoophubAPI.accountUser.AccountUser;
import fr.hoophub.hoophubAPI.accountUser.dto.CreateAccountUser;
import fr.hoophub.hoophubAPI.common.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateAccountMapper extends BaseMapper<CreateAccountUser, AccountUser> {

    /**
     * Singleton instance of the UserMapper interface.
     */
    CreateAccountMapper INSTANCE = Mappers.getMapper(CreateAccountMapper.class);
}
