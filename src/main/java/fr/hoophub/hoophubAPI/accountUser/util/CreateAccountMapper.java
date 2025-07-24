package fr.hoophub.hoophubAPI.accountUser.util;

import fr.hoophub.hoophubAPI.accountUser.AccountUser;
import fr.hoophub.hoophubAPI.accountUser.dto.CreateAccountUser;
import fr.hoophub.hoophubAPI.common.BaseMapper;
import fr.hoophub.hoophubAPI.common.GeneratedMapper;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring")
public interface CreateAccountMapper extends BaseMapper<CreateAccountUser, AccountUser> {

    /**
     * Singleton instance of the UserMapper interface.
     */
    CreateAccountMapper INSTANCE = Mappers.getMapper(CreateAccountMapper.class);
}
