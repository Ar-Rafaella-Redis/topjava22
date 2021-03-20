package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }


    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(crudUserRepository.getOne(userId));
        //em.getReference(User.class, userId)
        if (meal.isNew()) {
            crudRepository.save(meal);
            return meal;
        } else if (get(meal.id(), userId) == null) {
            return null;
        }
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id,userId) !=0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudRepository.findById(id).orElse(null);
        return (meal != null && meal.getUser().getId() == userId)? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> meals = crudRepository.findByUserId(userId);
        meals.sort(Comparator.comparing(Meal::getDateTime).reversed());
        return meals;
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        List<Meal> meals = crudRepository.getBetweenHalfOpen(userId, startDateTime, endDateTime);
        meals.sort(Comparator.comparing(Meal::getDateTime).reversed());
        return meals;
    }
}
