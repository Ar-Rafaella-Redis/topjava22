package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import static ru.javawebinar.topjava.Profiles.JDBC;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest{
}
