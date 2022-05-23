package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Bet;
import com.example.model.Bookmaker;
import com.example.model.Condition;
import com.example.model.Match;
import com.example.services.BetService;
import com.example.services.MatchService;
import com.example.services.PersonService;
import com.example.services.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SaveConditionsResultAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(SaveConditionsResultAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        MatchService matchService = new MatchService();
        BetService betService = new BetService();
        PersonService personService = new PersonService();
        Bookmaker bookmaker = (Bookmaker) req.getSession(false).getAttribute("bookmaker");
        String matchId = req.getParameter("match_id");
        boolean invalid = false;
        try {
            Match match = matchService.getMatchById(matchId);
            for (Condition condition : match.getConditionList()) {
                String parameter = req.getParameter(String.valueOf(condition.getId()));
                Boolean parseResult;
                if (parameter != null) {
                    if (parameter.equals("true") || parameter.equals("false")) {
                        parseResult = Boolean.parseBoolean(parameter);
                        condition.setResult(parseResult);
                    } else {
                        invalid = true;
                    }
                } else {
                    invalid = true;
                }
            }
            if (invalid) {
                req.setAttribute("match", match);
                req.setAttribute("inputError", "true");
                return new ActionResult("sum-up-result");
            }
            matchService.sumUpConditionsResult(match);
            List<Bet> playedBets = betService.sumUpBetsResultByFinishedMatch(match);
            for (Bet bet : playedBets) {
                LOG.debug("Bet's customer - {}", bet.getCustomer());
                personService.summarizeBet(bet, bookmaker);
            }
        } catch (ServiceException e) {
            throw new ActionException("Cannot get match by id", e);
        }
        req.getSession(false).setAttribute("bookmaker", bookmaker);
        return new ActionResult("matches/edit/active", true);
    }
}
