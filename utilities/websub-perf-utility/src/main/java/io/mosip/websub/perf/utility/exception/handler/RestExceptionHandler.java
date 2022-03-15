package io.mosip.websub.perf.utility.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.mosip.kernel.core.exception.ExceptionUtils;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@ConditionalOnProperty(name = "websub.utilty.proxy-positive-response-enable",havingValue = "true")
public class RestExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleEntityNotFound(Exception ex) {
		ExceptionUtils.logRootCause(ex);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}