package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Bookmaker;
import com.example.model.Customer;
import com.example.model.Person;
import com.example.services.PersonService;
import com.example.services.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Properties;


public class LoginAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(LoginAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        HttpSession captchaSession = req.getSession();
        String email = req.getParameter("login");
        String password = req.getParameter("password");

        String captchaText = String.valueOf(captchaSession.getAttribute("captcha_security"));
        if(!req.getParameter("captcha").equals(captchaText)){
            req.setAttribute("loginError", "Неверно введен код из картинки");
            LOG.info("Wrong error captcha ({})", captchaText);
            return new ActionResult("welcome");
        }

        LOG.info("Get email- {} and password- {}", email, password);
        Person person;
        PersonService service = new PersonService();
        Properties properties = new Properties();
        try {
            properties.load(LoginAction.class.getClassLoader().getResourceAsStream("validation.properties"));
        } catch (IOException e) {
            throw new ActionException("Cannot get properties", e);
        }
        if (!email.matches(properties.getProperty("email.regex")) || !password.matches(properties.getProperty("password.regex"))) {
            req.setAttribute("loginError", "Неправильный логин или пароль");
            LOG.info("Wrong login ({}) or password ({})", email, password);
            return new ActionResult("welcome");
        }

        try {
            person = service.performUserLogin(email, password);
            LOG.debug("Get customer - {} to Login action");
        } catch (ServiceException e) {
            throw new ActionException("Cannot get customer from dao", e);
        }

        if (person instanceof Bookmaker) {
            Bookmaker bookmaker = (Bookmaker) person;
            req.getSession(false).setAttribute("bookmaker", bookmaker);
            LOG.info("{} logged in", bookmaker);
            LOG.info("Action result - bookmaker/home redirect");
            return new ActionResult("bookmaker/home", true);
        } else if (person instanceof Customer) {
            Customer customer = (Customer) person;
            req.getSession(false).setAttribute("loggedCustomer", customer);
            LOG.info("{} logged in", customer);
            LOG.info("Action result - home redirect");
            return new ActionResult("home", true);
        } else {
            req.setAttribute("loginError", "Invalid Login or Password");
            LOG.info("Wrong login ({}) or password ({})", email, password);
            LOG.info("Action result - welcome forward");
            return new ActionResult("welcome");
        }
    }
}
