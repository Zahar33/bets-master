package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Customer;
import com.example.services.PersonService;
import com.example.services.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterAction.class);
    private boolean invalid = false;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        PersonService service = new PersonService();
        Customer customer = new Customer();
        Properties properties = new Properties();


        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream("validation.properties"));
        } catch (IOException e) {
            throw new ActionException("Cannot load properties", e);
        }

        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String repeatPassword = req.getParameter("repeatPassword");
        try {
            if (!service.checkEmailAvailable(email)) {
                req.setAttribute("emailError", "busy");
                invalid = true;
            } else {
                checkParameterBeRegex(email, "email", properties.getProperty("email.regex"), req);
            }
        } catch (ServiceException e) {
            throw new ActionException("Cannot check email available", e);
        }
        checkParameterBeRegex(firstName, "firstName", properties.getProperty("word.regex"), req);
        checkParameterBeRegex(lastName, "lastName", properties.getProperty("word.regex"), req);
        if (!password.equals(repeatPassword)) {
            req.setAttribute("passwordError", "wrong repeat");
        } else {
            checkParameterBeRegex(password, "password", properties.getProperty("password.regex"), req);
        }
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPassword(password);
        LOG.info("Get customer {} from view", customer);
        if (invalid) {
            invalid = false;
            return new ActionResult("register");
        } else {
            try {
                service.registerCustomer(customer);
            } catch (ServiceException e) {
                throw new ActionException("Register action cannot register", e);
            }
            req.getSession(false).setAttribute("loggedCustomer", customer);
            return new ActionResult("avatar/upload", true);
        }
    }

    private void checkParameterBeRegex(String parameter, String parameterName, String regex, HttpServletRequest req) {
        LOG.debug("Check parameter '{}' with value '{}' by regex '{}'", parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug("Parameter '{}' with value '{}' is unsuitable.", parameterName, parameter);
            req.setAttribute(parameterName + "Error", "true");
            invalid = true;
        }
    }
}
