package io.mosip.websub.perf.utility.filter;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;



/**
 * This filter is used for handle intent verification request by hub after subscribe and unsubscribe
 * operations with help of metadata collected by {@link PreAuthenticateContentAndVerifyIntent} and
 * {@link IntentVerificationConfig} class.
 * 
 * @author Urvil Joshi
 *
 */
public class IntentVerificationFilter extends OncePerRequestFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IntentVerificationFilter.class);
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {		
		if (request.getRequestURI().contains("/callback") &&  request.getMethod().equals(HttpMethod.GET.name())) {
			LOGGER.info("intent verification intercepted");	
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
				try {
					String challange =request.getParameter("hub.challenge");
					if(challange!=null){
					response.getWriter().write(request.getParameter("hub.challenge"));
					response.getWriter().flush();
					response.getWriter().close();
				}else{
					LOGGER.info("hub mode in no challange error {}",request.getParameter("hub.mode"));	
					LOGGER.info("hub reason in no challange error {}",request.getParameter("hub.reason"));	
					response.getWriter().flush();
					response.getWriter().close();
				}
				} catch (IOException exception) {
					LOGGER.error("error received while writing challange back {}",exception.getMessage());
				}
		}else {
			filterChain.doFilter(request, response);
		}
	}

}
