package rs.lab.notes.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        var time = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            if (log.isInfoEnabled()) {
                time = System.currentTimeMillis() - time;
                var req = ((HttpServletRequest) request);
                log.info("HTTP \"{}\" \"{}\" responded {} in {} ms", req.getMethod(), req.getRequestURI(), ((HttpServletResponse) response).getStatus(), time);
            }
        }
    }
}