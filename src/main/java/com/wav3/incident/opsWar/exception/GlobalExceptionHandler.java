package com.wav3.incident.opsWar.exception;

import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.wav3.incident.opsWar.dto.response.APIResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<APIResponse> handleCustomExceptions(CustomException e, HttpServletRequest request) {

		APIResponse response = new APIResponse(e.getMessage(), request.getRequestURI(), null, null,
				e.getStatus().value(), -9999);

		return new ResponseEntity<>(response, e.getStatus());
	}

	@ExceptionHandler(IncidentNotFound.class)
	public ResponseEntity<APIResponse> handleResourceNotFoundException(IncidentNotFound ex,
			HttpServletRequest request) {
		APIResponse response = new APIResponse(ex.getMessage(), request.getRequestURI(), null, null,
				HttpStatus.NOT_FOUND.value(), -1999);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(DuplicateIncidentFound.class)
	public ResponseEntity<APIResponse> handleResourceNotFoundException(DuplicateIncidentFound ex,
			HttpServletRequest request) {
		APIResponse response = new APIResponse(ex.getMessage(), request.getRequestURI(), null, null,
				HttpStatus.CONFLICT.value(), -9990);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<APIResponse> handleAccessDeniedException(AccessDeniedException ex,
			HttpServletRequest request) {
		APIResponse response = new APIResponse(ex.getMessage(), request.getRequestURI(), null, null,
				HttpStatus.FORBIDDEN.value(), -1);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	/**
	 * JSON de-serialization issues (wrong content-type / malformed JSON)
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		String raw = ex.getMostSpecificCause().getMessage();
		String errorMessage = (raw != null && raw.contains("DTO")) ? "Invalid or missing 'type' fields"
				: "Malformed request";

		APIResponse response = new APIResponse("The request cannot be processed because of incorrect request data",
				request.getDescription(false).replace("uri=", ""), null, errorMessage, HttpStatus.BAD_REQUEST.value(),
				-99999);

		return ResponseEntity.badRequest().body(response);
	}

	/**
	 * Bean validation errors thrown by @valid Its fun
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		String errors = ex.getBindingResult().getFieldErrors().stream().map(err -> err.getDefaultMessage())
				.collect(Collectors.joining(", "));

		APIResponse response = new APIResponse("The request cannot be processed because of incorrect request data",
				request.getDescription(false).replace("uri=", ""), null, errors, HttpStatus.BAD_REQUEST.value(),
				-99999);

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<APIResponse> handleDatabaseExceptions(DataAccessException ex, HttpServletRequest request) {

		String message;
		if (ex instanceof DuplicateKeyException) {
			message = "Duplicate record detected";
		} else if (ex instanceof DataIntegrityViolationException) {
			message = "Data integrity violation";
		} else {
			message = "Database error occurred";
		}

		APIResponse response = new APIResponse("Database operation failed", request.getRequestURI(), null, message,
				HttpStatus.CONFLICT.value(), -99999);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@Override
	protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {

		APIResponse response = new APIResponse("Invalid URI", request.getDescription(false).replace("uri=", ""), null,
				"URI DOESN'T EXIST", HttpStatus.NOT_FOUND.value(), -99999);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		APIResponse response = new APIResponse("Invalid METHOD TYPE", request.getDescription(false).replace("uri=", ""),
				null, "Invalid METHOD TYPE", HttpStatus.METHOD_NOT_ALLOWED.value(), -99999);

		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
	}

	/**
	 * Fallback for uncaught exceptions :'( - OOPS I DID IT AGAIN
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<APIResponse> handleAllUncaughtException(Exception ex, HttpServletRequest request) {

		ex.printStackTrace();

		APIResponse response = new APIResponse("oops! Something went wrong", request.getRequestURI(), null,
				ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), -99);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
