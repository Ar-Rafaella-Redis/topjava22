package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        String forward="";
        String action = request.getParameter("action");
        action = (action==null?" ":action);

        if (action.equalsIgnoreCase("delete")){
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            MealsUtil.delete(mealId);
            forward = MEALS;
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(LocalTime.MIN, LocalTime.MAX);
            request.setAttribute("meals",mealsTo);
           // request.setAttribute("meals", MealsUtil.filteredByStreams(LocalTime.MIN, LocalTime.MAX));
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            MealTo mealTo = MealsUtil.getMealById(mealId);
            request.setAttribute("meal", mealTo);
        } else if (action.equalsIgnoreCase("insert")){
            forward = INSERT_OR_EDIT;
        } else {
            forward = MEALS;
            request.setAttribute("meals", MealsUtil.filteredByStreams(LocalTime.MIN, LocalTime.MAX));
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);

  }

    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a POST request.
     * <p>
     * The HTTP POST method allows the client to send
     * data of unlimited length to the Web server a single time
     * and is useful when posting information such as
     * credit card numbers.
     *
     * <p>When overriding this method, read the request data,
     * write the response headers, get the response's writer or output
     * stream object, and finally, write the response data. It's best
     * to include content type and encoding. When using a
     * <code>PrintWriter</code> object to return the response, set the
     * content type before accessing the <code>PrintWriter</code> object.
     *
     * <p>The servlet container must write the headers before committing the
     * response, because in HTTP the headers must be sent before the
     * response body.
     *
     * <p>Where possible, set the Content-Length header (with the
     * {@link ServletResponse#setContentLength} method),
     * to allow the servlet container to use a persistent connection
     * to return its response to the client, improving performance.
     * The content length is automatically set if the entire response fits
     * inside the response buffer.
     *
     * <p>When using HTTP 1.1 chunked encoding (which means that the response
     * has a Transfer-Encoding header), do not set the Content-Length header.
     *
     * <p>This method does not need to be either safe or idempotent.
     * Operations requested through POST can have side effects for
     * which the user can be held accountable, for example,
     * updating stored data or buying items online.
     *
     * <p>If the HTTP POST request is incorrectly formatted,
     * <code>doPost</code> returns an HTTP "Bad Request" message.
     *
     * @param req  an {@link HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     * @throws IOException      if an input or output error is
     *                          detected when the servlet handles
     *                          the request
     * @throws ServletException if the request for the POST
     *                          could not be handled
     * @see ServletOutputStream
     * @see ServletResponse#setContentType
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!"Cancel".equalsIgnoreCase(req.getParameter("cancel"))) {
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));

            String dateTimeStr = req.getParameter("dateTime");
            LocalDateTime dateTime = LocalDateTime.now();
            if (!(dateTimeStr == null || dateTimeStr.isEmpty())) {
                dateTime = LocalDateTime.parse(dateTimeStr);
            }

            String mealId = req.getParameter("id");
            Meal meal = new Meal(dateTime, description, calories);
            if (mealId == null || mealId.isEmpty()) {
                MealsUtil.insert(meal);
            } else {
                MealsUtil.update(Integer.parseInt(mealId), meal);
            }
        }

        req.setAttribute("meals",  MealsUtil.filteredByStreams(LocalTime.MIN, LocalTime.MAX));
        RequestDispatcher view = req.getRequestDispatcher(MEALS);
        view.forward(req, resp);
     }
}
