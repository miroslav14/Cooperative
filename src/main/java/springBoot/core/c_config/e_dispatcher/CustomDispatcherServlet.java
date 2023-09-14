package springBoot.core.c_config.e_dispatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

@Component("dispatcherServlet")
public class CustomDispatcherServlet extends DispatcherServlet {
    private final Logger JAVA_LOGGER = Logger.getLogger(CustomDispatcherServlet.class.getName());

    @Override
    public void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            //here we read all cookies and store them as key and value pairs into object of type of Cookie array
            Cookie[] cookies = request.getCookies();

            //if statement to check if we received any cookies in the request
            if ((cookies != null) && (cookies.length > 0)) {
                //this option is for transferring cookie array into the string separated by comma
                //String cookieString = Arrays.stream(cookies).map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", "));

                //here we read individual cookie and despite the name and value
                // we create new cookie with null value to secure the response of the server
                Arrays.stream(cookies).forEach(singleCookie -> {
                    // create a cookie
                    Cookie cookie = new Cookie(singleCookie.getName(), null);
                    cookie.setMaxAge(0);
                    cookie.setSecure(true);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");

                    //add a cookie to the response
                    response.addCookie(cookie);
                });
            }
        } catch (Exception ex) {
            JAVA_LOGGER.warning("Failure occurred in capturing the cookies and setting up the response with new value of all cookies - " + ex.getMessage());
            JAVA_LOGGER.info("Despite the exception the dispatcherServlet will process the request ");
        } finally {
            JAVA_LOGGER.info("Customized DispatcherServlet -> Initializing method called processRequest(arg,arg)");
            processRequest(request, response);
        }
    }
}
