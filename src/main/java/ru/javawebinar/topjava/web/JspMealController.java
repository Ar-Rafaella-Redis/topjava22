package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController{
    @Autowired
    private MealService service;

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("")
    public String getMeals(HttpServletRequest request, Model model) {
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @RequestMapping(value = "", params={"action=filter"}, method = RequestMethod.GET)
    public String getFilteredMeals(HttpServletRequest request, Model model) {
         LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
         LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
         LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
         LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
         model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
         return "meals";
    }

    @RequestMapping(value = "", params={"action=create"}, method = RequestMethod.GET)
    public String updateMeal(Model model){
       final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
       model.addAttribute("meal", meal);
       return("mealForm");
    }

    @GetMapping(value = "", params={"action=update"})
    public String updateMeal(@RequestParam("id") String id, Model model){
        final Meal meal = get(Integer.parseInt(id));
        model.addAttribute("meal", meal);
        return("mealForm");
    }
/*
    @GetMapping(value = "", params={"action=delete"})
    public String deleteMeal(Model model) {
        int id = (int) model.getAttribute("id");
        delete(id);
        return "redirect:meals";
    }
*/

    @GetMapping(params={"action=delete"})
    public String deleteMeal(@RequestParam("id") String id) {
        delete(Integer.parseInt(id));
        return "redirect:meals";
    }

    @PostMapping("")
    protected String saveMeal(HttpServletRequest request) {
          Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {
            update(meal, getId(request));
        } else {
            create(meal);
        }
        return ("redirect:meals");
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

}
