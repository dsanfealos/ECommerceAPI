package com.ecommerce.ECommerceAPI.service;

import com.ecommerce.ECommerceAPI.api.model.LoginBody;
import com.ecommerce.ECommerceAPI.api.model.PasswordResetBody;
import com.ecommerce.ECommerceAPI.exception.EmailFailureException;
import com.ecommerce.ECommerceAPI.exception.EmailNotFoundException;
import com.ecommerce.ECommerceAPI.exception.UserNotVerifiedException;
import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.VerificationToken;
import com.ecommerce.ECommerceAPI.model.dao.LocalUserDAO;
import com.ecommerce.ECommerceAPI.model.dao.VerificationTokenDAO;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.ecommerce.ECommerceAPI.api.model.RegistrationBody;
import com.ecommerce.ECommerceAPI.exception.UserAlreadyExistsException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Test class to unit test the UserService class.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    /** Extension for mocking email sending. */
    @RegisterExtension
    private static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true);
    /** The UserService to test. */
    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private LocalUserDAO localUserDAO;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private VerificationTokenDAO verificationTokenDAO;

    /**
     * Tests the registration process of the user.
     * @throws MessagingException Thrown if the mocked email service fails somehow.
     */
    @Test
    @Transactional
    public void testRegisterUser() throws MessagingException {
        RegistrationBody body = new RegistrationBody();
        body.setUsername("UserA");
        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        body.setFirstName("FirstName");
        body.setLastName("LastName");
        body.setPassword("MySecretPassword123");
        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(body), "Username should already be in use.");
        body.setUsername("UserServiceTest$testRegisterUser");
        body.setEmail("UserA@junit.com");
        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(body), "Email should already be in use.");
        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        Assertions.assertDoesNotThrow(() -> userService.registerUser(body),
                "User should register successfully.");
        Assertions.assertEquals(body.getEmail(), greenMailExtension.getReceivedMessages()[0]
                .getRecipients(Message.RecipientType.TO)[0].toString());
    }

    @Test
    @Transactional
    public void testLoginUser() throws UserNotVerifiedException, EmailFailureException {
        LoginBody body = new LoginBody();

        body.setUsername("UserA-NotExists");
        body.setPassword("PasswordA123-BadPassword");
        Assertions.assertNull(userService.loginUser(body), "The user should not exist");

        body.setUsername("UserA");
        Assertions.assertNull(userService.loginUser(body), "The password should be incorrect");

        body.setPassword("PasswordA123");
        Assertions.assertNotNull(userService.loginUser(body), "The user should login successfully");

        body.setUsername("UserB");
        body.setPassword("PasswordB123");
        try{
            userService.loginUser(body);
            Assertions.assertTrue(false, "User should not have email verified");
        } catch(UserNotVerifiedException e){
            Assertions.assertTrue(e.isNewEmailSent(), "Email verification should be sent");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
        try{
            userService.loginUser(body);
            Assertions.assertTrue(false, "User should not have email verified");
        } catch(UserNotVerifiedException e){
            Assertions.assertFalse(e.isNewEmailSent(), "Email verification should not be resent");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
    }

    @Test
    @Transactional
    public void testVerifyUser() throws EmailFailureException {
        Assertions.assertFalse(userService.verifyUser("bad Token"), "Token that is bad or does not exist should return false");
        LoginBody body = new LoginBody();
        body.setUsername("UserB");
        body.setPassword("PasswordB123");
        try{
            userService.loginUser(body);
            Assertions.assertTrue(false, "User should not have email verified");
        } catch(UserNotVerifiedException e){
            List<VerificationToken> tokens = verificationTokenDAO.findByUser_IdOrderByIdDesc(2L);
            String token = tokens.get(0).getToken();
            Assertions.assertTrue(userService.verifyUser(token));
        }
        Assertions.assertNotNull(body, "The user should now be verified");
    }

    @Test
    @Transactional
    public void testForgotPassword() throws MessagingException {
        Assertions.assertThrows(EmailNotFoundException.class, () -> userService.forgotPassword("UserNotExist@junit.com"));
        Assertions.assertDoesNotThrow(() -> userService.forgotPassword("UserA@junit.com"), "Non existing" +
                "email should be rejected.");
        Assertions.assertEquals("UserA@junit.com", greenMailExtension.getReceivedMessages()[0]
                .getRecipients(Message.RecipientType.TO)[0].toString(), "Password reset" +
                "email should be sent.");
    }

    @Test
    @Transactional
    public void testResetPassword(){
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        String token = jwtService.generatePasswordResetJWT(user);
        PasswordResetBody body = new PasswordResetBody();
        body.setToken(token);
        body.setPassword("Password123456");
        userService.resetPassword(body);
        Assertions.assertTrue(encryptionService.verifyPassword("Password123456",user.getPassword()),
                "Password change should be written to DB.");
    }

    @Test
    @Transactional
    public void testUpdateName(){
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        String newFirstName = "Antonia";
        String newLastName = "Bezoya";
        userService.updateName(user.getId(), newFirstName, newLastName);
        Assertions.assertTrue(user.getFirstName() == "Antonia",
                "First name should be Antonia");
        Assertions.assertTrue(user.getLastName() == "Bezoya",
                "Last name should be Bezoya");
    }
}
