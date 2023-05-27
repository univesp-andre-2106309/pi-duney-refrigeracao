package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.exception.*;
import com.duneyrefrigeracao.backend.domain.model.Account;
import com.duneyrefrigeracao.backend.domain.model.RefreshToken;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import com.duneyrefrigeracao.backend.infrastructure.encryption.IEncrypter;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
import com.duneyrefrigeracao.backend.infrastructure.repository.IUnitOfWork;
import com.duneyrefrigeracao.backend.infrastructure.security.IJwtProvider;
import com.duneyrefrigeracao.backend.application.dataobject.request.account.PostCreateAccountReq;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;


@Service
public class AccountService implements IAccountService {

    private final IUnitOfWork _unitOfWork;
    private final IEncrypter _encrypter;
    private final IJwtProvider _jwtProvider;
    private final ILogging _logging;
    private final IMatcherService _matcherService;

    public AccountService(IUnitOfWork _unitOfWork, IEncrypter _encrypter, IJwtProvider _jwtProvider, IMatcherService matcherService) {
        this._unitOfWork = _unitOfWork;
        this._encrypter = _encrypter;
        this._jwtProvider = _jwtProvider;
        this._matcherService = matcherService;
        this._logging = new Logging(AccountService.class);
    }

    public Account persistAccount(PostCreateAccountReq accountReq) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {

            this._logging.LogMessage(LogLevel.INFO, "Buscando conta existente no banco pelo nome de usuario...");
            Account accUserVal = _unitOfWork.getAccountRepository().findAccountByUsername(accountReq.username());

            if (accUserVal != null) {
                throw new AccountValidationException("Username", "Nome de usuario já existe no sistema!");
            }

            this._logging.LogMessage(LogLevel.INFO, "Buscando conta existente no banco pelo email...");
            Account accEmailVal = _unitOfWork.getAccountRepository().findAccountByEmail(accountReq.email());

            if (accEmailVal != null) {
                throw new AccountValidationException("Email", "Email já existe no sistema!");
            }

            Account account = new Account();

            account.setFirstName(accountReq.firstName());
            account.setLastName(accountReq.lastName());
            account.setUsername(accountReq.username());
            account.setEmail(accountReq.email());
            account.setBirthDate(accountReq.birthDate());
            account.setIsEnabled(true);


            Tuple<String, String> saltPassword = _encrypter.encryptValue(accountReq.password());

            account.setPassword(saltPassword.getFirstValue());
            account.setSalt(saltPassword.getSecondValue());

            account.setSigningDate(new Date());

            this._logging.LogMessage(LogLevel.INFO, String.format("Armazenando o usuario %s no banco...", account.getUsername()));
            _unitOfWork.getAccountRepository().save(account);
            this._logging.LogMessage(LogLevel.INFO, String.format("Usuario %s adicionado ao banco.", account.getUsername()));

            return account;
        } catch (AccountValidationException ave) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro de validação -> %s", ave.getMessage()));
            throw ave;
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro generico -> %s", er.getMessage()));
            er.printStackTrace();
            throw er;
        }

    }

    public boolean verifyLogin(String email, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this._logging.LogMessage(LogLevel.INFO, "Procurando uma conta vinculada ao email....");
        Account account = _unitOfWork.getAccountRepository().findAccountByEmail(email);

        if (account == null) {
            throw new AccountValidationException("email", "Não existe uma conta vinculada ao email informado");
        }

        this._logging.LogMessage(LogLevel.INFO, String.format("Foi encontrado uma conta com o nome %s", account.getUsername()));


        String hashPassword = this._encrypter.encryptValueWithSalt(password, account.getSalt()).getFirstValue();


        return account.getPassword().equals(hashPassword);

    }

    public String retrieveUsername(String email) {
        this._logging.LogMessage(LogLevel.INFO, "Buscando nome de usuario...");
        return this._unitOfWork.getAccountRepository().findAccountByEmail(email).getUsername();
    }

    public String generateAccountToken(String userName) {
        this._logging.LogMessage(LogLevel.INFO, "Gerando token do usuario");
        return _jwtProvider.generateToken(userName);
    }

    public UserDetails loadUserDetailsByUsername(String username) {
        Account account = this._unitOfWork.getAccountRepository().findAccountByUsername(username);

        if (account != null) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Requisição do usuario %s", account.getUsername()));
            return new User(
                    account.getUsername(),
                    account.getPassword(),
                    new ArrayList<>()
            );
        }
        return null;
    }

    @Override
    public Account accountUpdate(Account upAccount) throws AccountNotAvailableException, EmailPatternException {
        UserDetails details;
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this._logging.LogMessage(LogLevel.INFO, "Iniciando processo de atualização de dados da senha.....");
        if (object instanceof UserDetails) {
            details = (UserDetails) object;
        } else {
            throw new AccountNotAvailableException();
        }

        if (!_matcherService.validateEmailPattern(upAccount.getEmail())) {
            throw new EmailPatternException();
        }

        this._logging.LogMessage(LogLevel.INFO, "Buscando conta pelo nome do usuairo.");
        Account ogAccount = this._unitOfWork.getAccountRepository().findAccountByUsername(details.getUsername());

        ogAccount.setFirstName(upAccount.getFirstName());
        ogAccount.setLastName(upAccount.getLastName());
        ogAccount.setEmail(upAccount.getEmail());
        ogAccount.setBirthDate(upAccount.getBirthDate());

        this._logging.LogMessage(LogLevel.INFO, "Atualizando dados no banco");
        this._unitOfWork.getAccountRepository().save(ogAccount);
        this._logging.LogMessage(LogLevel.INFO, "Dados atualizados com sucesso!");

        return ogAccount;
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword, String newPasswordCheck) throws PasswordNotEqualException, InvalidPasswordException, AccountNotAvailableException, NoSuchAlgorithmException, InvalidKeySpecException {
        UserDetails details;
        String hashPassword;
        this._logging.LogMessage(LogLevel.INFO, "Iniciando processo de atualização de dados da conta.....");

        if (!newPassword.equals(newPasswordCheck)) {
            throw new PasswordNotEqualException();
        }

        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object instanceof UserDetails) {
            details = (UserDetails) object;
        } else {
            throw new AccountNotAvailableException();
        }

        this._logging.LogMessage(LogLevel.INFO, "Buscando conta pelo nome do usuairo.");
        Account account = _unitOfWork.getAccountRepository().findAccountByUsername(details.getUsername());

        if (account != null) {
            this._logging.LogMessage(LogLevel.INFO, "Comparando senhas....");
            hashPassword = _encrypter.encryptValueWithSalt(oldPassword, account.getSalt()).getFirstValue();

            if (!hashPassword.equals(account.getPassword())) {
                throw new InvalidPasswordException();
            }

            Tuple<String, String> newPasswordTpl = _encrypter.encryptValue(newPassword);

            account.setPassword(newPasswordTpl.getFirstValue());
            account.setSalt(newPasswordTpl.getSecondValue());

            this._logging.LogMessage(LogLevel.INFO, "Salvando nova senha...");
            this._unitOfWork.getAccountRepository().save(account);
            this._logging.LogMessage(LogLevel.INFO, "Senha alterada com sucesso.");
        } else {
            throw new AccountNotFoundException(details.getUsername());
        }

    }

    @Override
    public RefreshToken generateRefreshToken(String token, String username) {

        return this._jwtProvider.generateRefreshToken(token, username);
    }

    @Override
    public Account findAccountById(Long id) {

        this._logging.LogMessage(LogLevel.INFO, String.format("Buscando account pelo id - %d", id));

        try {
            this._logging.LogMessage(LogLevel.INFO, String.format("Buscando dados a conta de Id - %s", id));
            Account account = this._unitOfWork.getAccountRepository().getReferenceById(id);
            this._logging.LogMessage(LogLevel.INFO, String.format("Foi encontrado dados da conta - %s", account.toString()));
            return account;
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao consultar conta, erro será jogado como AccountNotFoundException, erro original -> %s", er.getMessage()));
            throw new AccountNotFoundException();
        }
    }

    @Override
    public Tuple<String, String> validateLoginStatus(String jwtToken, String refreshToken) {
        try {

            jwtToken = jwtToken.replace("Bearer ", "");

            if (this._jwtProvider.checkTokenValidation(jwtToken)) {
                return new Tuple<>(jwtToken, refreshToken);
            }
            throw new AccountLoginRefreshException();

        } catch (ExpiredJwtException er) {
            this._logging.LogMessage(LogLevel.ERROR, "Token expirado, tentando gerar um novo token....");
            Tuple<RefreshToken, String> newRefreshTokenTpl = this._jwtProvider.validateRefreshToken(refreshToken);

            if (newRefreshTokenTpl == null) {
                this._logging.LogMessage(LogLevel.ERROR, "Não foi possivel gerar um novo token");
                throw new AccountLoginRefreshException();
            }
            this._logging.LogMessage(LogLevel.ERROR, "Foi gerado um novo token");
            return new Tuple<>(newRefreshTokenTpl.getSecondValue(), newRefreshTokenTpl.getFirstValue().getRefreshToken());
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro generico -> %s", er.getMessage()));
            er.printStackTrace();
            throw er;
        }
    }
}
