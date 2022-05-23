package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.CashAccount;
import com.example.model.Customer;
import com.example.services.PersonService;
import com.example.services.ServiceException;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class ReplenishCustomersBalanceAction implements Action {

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        PersonService service = new PersonService();
        String money = req.getParameter("addToCustomerBalance");
        String id = req.getParameter("customersId");
        Customer customer;

        Properties properties = new Properties();
        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream("validation.properties"));
        } catch (IOException e) {
            throw new ActionException("Cannot load properties", e);
        }
        try {
            customer = service.findById(id);
        } catch (ServiceException e) {
            throw new ActionException("Cannot find customer by id", e);
        }

        if (money.matches(properties.getProperty("number.regex"))) {
            try {
                service.replenishPersonsBalance(Money.of(CurrencyUnit.of(CashAccount.CURRENCY), Double.parseDouble(money)), customer);
            } catch (ServiceException e) {
                throw new ActionException("Cannot deposit on cash account", e);
            }
            req.setAttribute("flash.add_message", "success");
        } else {
            req.setAttribute("flash.add_message", "error");
        }
        return new ActionResult("customer/edit?id=" + id, true);
    }
}
