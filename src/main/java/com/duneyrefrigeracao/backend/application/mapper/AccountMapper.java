package com.duneyrefrigeracao.backend.application.mapper;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.AccountDTO;
import com.duneyrefrigeracao.backend.application.dataobject.request.account.PutUpdateAccountReq;
import com.duneyrefrigeracao.backend.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper( AccountMapper.class );

    Account updateAccountParaAccount(PutUpdateAccountReq req);

    AccountDTO accountParaAccountDTO(Account req);
}
