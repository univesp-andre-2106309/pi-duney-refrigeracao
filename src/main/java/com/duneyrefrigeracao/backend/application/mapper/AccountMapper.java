package com.duneyrefrigeracao.backend.application.mapper;

import com.duneyrefrigeracao.backend.application.dataobject.request.account.PostUpdateAccountReq;
import com.duneyrefrigeracao.backend.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper( AccountMapper.class );

    Account updateAccountParaAccount(PostUpdateAccountReq req);
}
