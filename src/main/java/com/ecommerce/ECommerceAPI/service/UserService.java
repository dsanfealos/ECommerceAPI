package com.ecommerce.ECommerceAPI.service;

import com.ecommerce.ECommerceAPI.api.model.LoginBody;
import com.ecommerce.ECommerceAPI.api.model.RegistrationBody;
import com.ecommerce.ECommerceAPI.exception.EmailFailureException;
import com.ecommerce.ECommerceAPI.exception.UserAlreadyExistsException;
import com.ecommerce.ECommerceAPI.exception.UserNotVerifiedException;
import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.VerificationToken;
import com.ecommerce.ECommerceAPI.model.dao.LocalUserDAO;
import com.ecommerce.ECommerceAPI.model.dao.VerificationTokenDAO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private VerificationTokenDAO verificationTokenDAO;
    private EncryptionService encryptionService;
    private JWTService jwtService;
    private EmailService emailService;

    public UserService(LocalUserDAO localUserDAO, VerificationTokenDAO verificationTokenDAO,
                       EncryptionService encryptionService, JWTService jwtService, EmailService emailService) {
        this.localUserDAO = localUserDAO;
        this.verificationTokenDAO = verificationTokenDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    /**
     * Attempts to register a user given the information provided
     * @param registrationBody The registration information
     * @return The local user that has been written to the database
     * @throws UserAlreadyExistsException Thrown if there is already a user with the same username/email.
     */
    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException, EmailFailureException {
        if(localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
                localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("A user with that username or email already exists!");
        }
        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        return localUserDAO.save(user);
    }

    private VerificationToken createVerificationToken(LocalUser user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    /**
     * Logins in a user and provides an authentication token back.
     * @param loginBody The login request.
     * @return The authentication token. Null if the request was invalid.
     * @throws UserNotVerifiedException The user is not verified or the email could not be sent.
     */
    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<LocalUser> opUser= localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()){
            LocalUser user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())){
                if (user.isEmailVerified()){
                    return jwtService.generateJWT(user);
                }else{
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.isEmpty() ||
                            verificationTokens.get(0).getCreatedTimestamp().before(new Timestamp(System.currentTimeMillis() - (1000 * 3600)));
                    if (resend){
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenDAO.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }

                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }

    @Transactional
    public boolean verifyUser(String token){
        Optional<VerificationToken> opToken = verificationTokenDAO.findByToken(token);
        if (opToken.isPresent()){
            VerificationToken verificationToken = opToken.get();
            LocalUser user = verificationToken.getUser();
            if (!user.isEmailVerified()){
                user.setEmailVerified(true);
                localUserDAO.save(user);
                verificationTokenDAO.deleteByUser(user);
                return true;
            }
        }
        return false;
    }
}
