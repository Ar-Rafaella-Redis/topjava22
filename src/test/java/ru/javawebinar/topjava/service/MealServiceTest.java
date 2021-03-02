package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.getNew;
import static ru.javawebinar.topjava.UserTestData.getUpdated;
import static java.time.LocalDateTime.of;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    @Autowired
    private MealService service;

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL1_ID,USER_ID);
        assertThat(meal).isEqualToComparingFieldByField(MEAL1);
    }

    @Test
    public void getOtherMeal() {
       assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID,ADMIN_ID));
    }

    @Test
    public void duplicateMailCreate() {
         assertThrows(DataAccessException.class, () ->
               service.create(new Meal(null, of(2020, Month.JANUARY, 30, 10, 0), "Завтрак постный", 500),USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL1_ID,USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID,USER_ID));
    }

    @Test
    public void deleteOtherMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID,ADMIN_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_MEAL_ID + 100000,USER_ID));
    }


    @Test
    public void getBetweenInclusive() {
        List<Meal> userMeals = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 31), LocalDate.of(2020, Month.JANUARY, 31), USER_ID);
        assertThat(userMeals).usingFieldByFieldElementComparator().isEqualTo(Arrays.asList(MEAL7, MEAL6, MEAL5, MEAL4));
    }

    @Test
    public void getAll() {
        List<Meal> allUserMeals = service.getAll(USER_ID);
        assertThat(allUserMeals).usingFieldByFieldElementComparator().isEqualTo(MEALS);

    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated,USER_ID);
        assertThat(service.get(updated.getId(),USER_ID)).isEqualToComparingFieldByField(MealTestData.getUpdated());
   }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(),ADMIN_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        assertThat(created).isEqualToComparingFieldByField(newMeal);
        assertThat(service.get(newId,ADMIN_ID)).isEqualToComparingFieldByField(newMeal);
     }
}