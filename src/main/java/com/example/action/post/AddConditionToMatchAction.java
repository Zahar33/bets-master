package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Condition;
import com.example.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class AddConditionToMatchAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(AddConditionToBetAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        Match match = (Match) req.getSession(false).getAttribute("match");
        String conditionsName = req.getParameter("conditionsName");
        String coefficient = req.getParameter("coefficient");
        Condition condition = new Condition();
        Properties properties = new Properties();
        boolean result = true;
        try {
            properties.load(CreateEmptyMatchAction.class.getClassLoader().getResourceAsStream("validation.properties"));
        } catch (IOException e) {
            throw new ActionException("Cannot load validation properties", e);
        }
        if (conditionsName.matches(properties.getProperty("stringWithSpaces.regex"))) {
            condition.setConditionsName(conditionsName);
        } else {
            LOG.debug("Cannot matches condition name - {}", conditionsName);
            req.setAttribute("conditionsNameError", "true");
            result = false;
        }
        if (coefficient.matches(properties.getProperty("doubleNumber.regex"))) {
            condition.setCoefficient(Double.parseDouble(coefficient));
        } else {
            req.setAttribute("coefficientError", "true");
            result = false;
        }

        if (!result) {
            result = true;
            return new ActionResult("create-condition");
        }

        match.addCondition(condition);
        req.getSession(false).setAttribute("match", match);
        return new ActionResult("match/new/edit", true);
    }
}
