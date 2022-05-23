package com.example.action.get;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Customer;
import com.example.model.PaginatedList;
import com.example.services.PersonService;
import com.example.services.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowBookmakerHomePageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowBookmakerHomePageAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        PersonService service = new PersonService();
        String pageNumberParam = req.getParameter("pageNumber");
        PaginatedList<Customer> customers;
        int pageSize = 5;
        int pageNumber;
        if (pageNumberParam == null) {
            LOG.debug("Do not get page number parameter. Set page number 1");
            pageNumber = 1;
        } else {
            pageNumber = Integer.parseInt(pageNumberParam);
        }
        try {
            customers = service.getAllCustomers(pageNumber, pageSize);
            LOG.debug("Get customers paginated list with {} page numbers of {} pages at all and {} page size ", customers.getPageNumber(), customers.getPageCount(), customers.getPageSize());
        } catch (ServiceException e) {
            throw new ActionException("Cannot get customers list in action", e);
        }
        req.setAttribute("customers", customers);
        return new ActionResult("bookmaker-home");
    }
}