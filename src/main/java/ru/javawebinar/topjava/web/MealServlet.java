package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    /**
     * A convenience method which can be overridden so that there's no need
     * to call <code>super.init(config)</code>.
     *
     * <p>Instead of overriding {@link #init(ServletConfig)}, simply override
     * this method and it will be called by
     * <code>GenericServlet.init(ServletConfig config)</code>.
     * The <code>ServletConfig</code> object can still be retrieved via {@link
     * #getServletConfig}.
     *
     * @throws ServletException if an exception occurs that
     *                          interrupts the servlet's
     *                          normal operation
     */
    private static String INSERT_OR_EDIT = "/edit.jsp";
    private static String MEALS = "/meals.jsp";
    private List<MealTo> mealsTo;

    @Override
    public void init() throws ServletException {
        super.init();
        mealsTo = MealsUtil.filteredByStreams(LocalTime.MIN, LocalTime.MAX);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

         request.setAttribute("meals", mealsTo);
         request.getRequestDispatcher("meals.jsp").forward(request, response);
  }


}
