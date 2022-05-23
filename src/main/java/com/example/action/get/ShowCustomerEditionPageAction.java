package com.example.action.get;

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

public class ShowCustomerEditionPageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowCustomerEditionPageAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        String id = req.getParameter("id");
        PersonService service = new PersonService();
        Customer customer;
        if (id == null) {
            return null;
        }
        try {
            customer = service.findById(id);
            LOG.info("Get current customer - {} with purse - {}", customer, customer.getPersonsPurse());
        } catch (ServiceException e) {
            throw new ActionException("Cannot find by id", e);
        }
        if (customer.getId() == 0) {
            return null;
        }
        req.setAttribute("customer", customer);
        return new ActionResult("customer-edit");
    }
}
