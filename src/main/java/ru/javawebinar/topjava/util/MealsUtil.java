package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MealsUtil {
  private static int START_NUM =1000;
  private static final AtomicInteger counter = new AtomicInteger(START_NUM);

  static  List<Meal> meals = new ArrayList<>( Arrays.asList(
            new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),  "Еда на граничное значение", 100),
            new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    ));
  private static int caloriesPerDay =2000;

    public static void main(String[] args) {
       /* List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );*/

       // List<MealTo> mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), caloriesPerDay);
        //mealsTo.forEach(System.out::println);
    }

    public static int getSeqNum(){
        return counter.incrementAndGet();
    }
    public static List<MealTo> filteredByStreams(LocalTime startTime, LocalTime endTime) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static void delete(int mealId) {
        final boolean b = meals.removeIf(m -> m.getId() == mealId);
    }

    public static Meal findMealById(int mealId) {
        return meals.stream().filter(m->m.getId()==mealId).findFirst().orElse(null);
    }

    public static void insert(Meal meal) {
        meal.setId(MealsUtil.getSeqNum());
        meals.add(meal);
    }

    public static void update(int id, Meal meal) {

        Meal mealOld = findMealById(id);

        if (mealOld.getCalories()!=meal.getCalories())
            mealOld.setCalories(meal.getCalories());
        if (!mealOld.getDateTime().equals(meal.getDateTime()))
            mealOld.setDateTime(meal.getDateTime());
        if (!mealOld.getDescription().equals(meal.getDescription()))
            mealOld.setDescription(meal.getDescription());
     }

    public static MealTo getMealById(int mealId) {
        Meal meal = meals.stream().filter(m->m.getId()==mealId).findFirst().orElse(null);
       // int caloriesSum = meals.stream().filter(m->m.getDate()==meal.getDate()).map(m-> m.getCalories()).reduce(0,Integer::sum);
        int caloriesSum = meals.stream().filter(m->m.getDate()==meal.getDate()).mapToInt(m->m.getCalories()).sum();
        return createTo(meal,caloriesSum>caloriesPerDay);
    }



}
