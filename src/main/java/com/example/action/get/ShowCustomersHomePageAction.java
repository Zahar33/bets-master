package com.example.action.get;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Match;
import com.example.model.PaginatedList;
import com.example.services.MatchService;
import com.example.services.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowCustomersHomePageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowCustomersHomePageAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        MatchService service = new MatchService();
        PaginatedList<Match> matches;
        String successRegister = req.getParameter("successRegister");
        String pageNumberParam = req.getParameter("pageNumber");
        int pageSize = 5;
        int pageNumber;
        if (pageNumberParam == null) {
            LOG.debug("Do not get page number parameter. Set page number 1");
            pageNumber = 1;
        } else {
            pageNumber = Integer.parseInt(pageNumberParam);
        }
        try {
            matches = service.getAllActiveMatch(pageNumber, pageSize);
            LOG.debug("Get paginated list of matches with {} pageNumber, {} pageCount, {} page size and size - {}", matches.getPageNumber(), matches.getPageCount(), matches.getPageSize(), matches.size());
        } catch (ServiceException e) {
            throw new ActionException("Cannot get matches list in action", e);
        }
        if (successRegister != null) {
            req.setAttribute("registerMessage", "success");
        }
        req.setAttribute("matches", matches);
        return new ActionResult("home");
    }
}
