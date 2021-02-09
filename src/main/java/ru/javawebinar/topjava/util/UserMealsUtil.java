package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles

       Map<LocalDateTime,List<UserMeal>> mealsByDays = new HashMap<LocalDateTime,List<UserMeal>>();
       Map<LocalDateTime,Integer> caloriesByDays = new HashMap<LocalDateTime,Integer>();

        for(UserMeal userMeal:meals){
            caloriesByDays.merge(userMeal.getDate(), userMeal.getCalories(),(prev,calories)->prev+calories);

            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(),startTime,endTime))
                mealsByDays.merge(userMeal.getDate(),Arrays.asList(userMeal),(prev,old) ->{prev.add(userMeal); return prev;});
        }

        List<UserMealWithExcess> UserMealsWithExcess = new ArrayList<UserMealWithExcess>();

        for(UserMeal userMeal:meals){
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(),startTime,endTime))
                UserMealsWithExcess.add(new UserMealWithExcess(userMeal,caloriesByDays.get(userMeal.getDate())>caloriesPerDay));
        }

        return UserMealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        Map<LocalDateTime, Integer> caloriesByDays = meals.stream().collect(Collectors.groupingBy(UserMeal::getDate,Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream().filter(userMeal->TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(),startTime,endTime)).map(userMeal->new UserMealWithExcess(userMeal, caloriesByDays.get(userMeal.getDate())>caloriesPerDay )).collect(Collectors.toList());

    }
}
