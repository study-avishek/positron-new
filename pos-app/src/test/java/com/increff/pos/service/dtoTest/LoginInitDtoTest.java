package com.increff.pos.service.dtoTest;

import com.increff.pos.dto.InitDto;
import com.increff.pos.dto.LoginDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.service.AbstractUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static com.increff.pos.service.util.ConstructorUtil.createLoginForm;
import static com.increff.pos.service.util.ConstructorUtil.createUserForm;


public class LoginInitDtoTest extends AbstractUnitTest {

    @Value("${supervisor.email}")
    private String supervisorEmail;

    @Autowired
    private InitDto initDto;

    @Autowired
    private LoginDto loginDto;


    @Test
    public void testInitLoginLogoutSupervisor() throws ApiException {
        UserForm userForm = createUserForm("test@test.com","Password@123");
        initDto.initSite(userForm);
        LoginForm loginForm = createLoginForm("test@test.com","Password@123");
        HttpServletRequest req = new MockHttpServletRequest();
        loginDto.login(req, loginForm);
        loginDto.logout(req);
    }

    @Test
    public void testInitLoginOperator() throws ApiException {
        UserForm userForm = createUserForm("operator.test@test.com","Password@123");
        initDto.initSite(userForm);
        LoginForm loginForm = createLoginForm("operator.test@test.com","Password@123");
        HttpServletRequest req = new MockHttpServletRequest();
        loginDto.login(req, loginForm);
    }

    @Test(expected = ApiException.class)
    public void testInitInvalidEmail() throws ApiException{
        UserForm userForm = createUserForm("test.test","Password@123");
        initDto.initSite(userForm);
    }

    @Test(expected = ApiException.class)
    public void testInitInvalidPassword() throws ApiException{
        UserForm userForm = createUserForm("test@test.com","1234");
        initDto.initSite(userForm);
    }

    @Test(expected = ApiException.class)
    public void testInitAlreadyInitialized() throws ApiException{
        UserForm userForm = createUserForm("@.com","1234");
        initDto.initSite(userForm);
        userForm = createUserForm("@.com","1234");
        initDto.initSite(userForm);
    }

    @Test(expected = ApiException.class)
    public void testInitLoginWrongPassword() throws ApiException{
        UserForm userForm = createUserForm("@.com","RightPassword@1234");
        initDto.initSite(userForm);
        LoginForm loginForm = createLoginForm("@.com","WrongPassword@1234");
        HttpServletRequest req = new MockHttpServletRequest();
        loginDto.login(req, loginForm);
    }

    @Test(expected = ApiException.class)
    public void testLoginInvalidEmail() throws ApiException{
        LoginForm loginForm = createLoginForm(".com","Password@1234");
        HttpServletRequest req = new MockHttpServletRequest();
        loginDto.login(req, loginForm);
    }

    @Test(expected = ApiException.class)
    public void testLoginInvalidPassword() throws ApiException{
        LoginForm loginForm = createLoginForm("test@test.com","1234");
        HttpServletRequest req = new MockHttpServletRequest();
        loginDto.login(req, loginForm);
    }




}
