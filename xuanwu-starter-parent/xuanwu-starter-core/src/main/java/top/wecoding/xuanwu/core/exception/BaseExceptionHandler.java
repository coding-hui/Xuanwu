package top.wecoding.xuanwu.core.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import top.wecoding.xuanwu.core.base.R;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.context.i18n.LocaleContextHolder.getLocaleContext;
import static top.wecoding.xuanwu.core.constant.StrPool.SEMICOLON;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.DATABASE_ERROR;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.FAILURE;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.INVALID_REQUEST;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.PARAM_BIND_ERROR;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.PARAM_ERROR;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.PARAM_VALID_ERROR;

/**
 * 全局异常处理基类
 *
 * @author liuyuhui
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalParameterException.class)
	public R<Object> argumentException(IllegalParameterException e, HttpServletRequest request, HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Illegal argument exception", request, method));
		}
		return createResult(e);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ServerException.class)
	public R<Object> serverException(ServerException e, HttpServletRequest request, HandlerMethod method) {
		if (log.isErrorEnabled()) {
			log.error(exceptionMessage("Internal Server exception", request, method));
		}
		return createResult(e);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DemoException.class)
	public R<Object> demoException(DemoException e, HttpServletRequest request, HandlerMethod method) {
		log.info(exceptionMessage("Demo exception", request, method));
		return createResult(e);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> missParamException(MissingServletRequestParameterException e, HttpServletRequest request,
			HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Miss param exception", request, method));
		}
		String message = PARAM_ERROR.getDesc(e.getParameterName(), e.getParameterType());
		return R.error(PARAM_ERROR, message, null);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> handleError(MethodArgumentTypeMismatchException e, HttpServletRequest request,
			HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Miss param exception", request, method));
		}
		String message = INVALID_REQUEST.getDesc(e.getName());
		return R.error(INVALID_REQUEST, message);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> validatedBindException(MethodArgumentNotValidException e, HttpServletRequest request,
			HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Param valid exception", request, method));
		}
		String message = e.getBindingResult()
			.getAllErrors()
			.stream()
			.map(objectError -> String.format("%s %s",
					((DefaultMessageSourceResolvable) Objects.requireNonNull(objectError.getArguments())[0])
						.getDefaultMessage(),
					objectError.getDefaultMessage()))
			.collect(Collectors.joining(SEMICOLON));
		return R.error(PARAM_BIND_ERROR, message);
	}

	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> illegalStateException(IllegalStateException e, HttpServletRequest request, HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Illegal state exception", request, method));
		}
		String message = PARAM_ERROR.getDesc(e.getMessage());
		return R.error(PARAM_ERROR, message, null);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> constraintViolationException(ConstraintViolationException e, HttpServletRequest request,
			HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Constraint violation exception", request, method));
		}
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		String message = violations.stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.joining(SEMICOLON));
		return R.error(PARAM_VALID_ERROR, message, null);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> handleError(HttpMessageNotReadableException e, HttpServletRequest request, HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Message not readable exception", request, method));
		}
		String message = INVALID_REQUEST.getDesc(e.getMessage());
		return R.error(INVALID_REQUEST, message);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public R<Object> handleError(HttpRequestMethodNotSupportedException e, HttpServletRequest request,
			HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Request method not supported exception", request, method));
		}
		String message = INVALID_REQUEST.getDesc(e.getMessage());
		return R.error(SystemErrorCode.INVALID_REQUEST, message);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public R<Object> handleError(HttpMediaTypeNotSupportedException e, HttpServletRequest request,
			HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Request media type not supported exception", request, method));
		}
		String message = INVALID_REQUEST.getDesc(e.getMessage());
		return R.error(SystemErrorCode.INVALID_REQUEST, message);
	}

	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public R<Object> handleError(HttpMediaTypeNotAcceptableException e, HttpServletRequest request,
			HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Request media type not acceptable exception", request, method));
		}
		String message = PARAM_ERROR
			.getDesc(e.getMessage() + " " + e.getSupportedMediaTypes().stream().map(MediaType::getQualityValue));
		return R.error(SystemErrorCode.PARAM_ERROR, message);
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> validatedBindException(BindException e, HttpServletRequest request, HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Params bind exception", request, method));
		}
		String error = e.getBindingResult()
			.getAllErrors()
			.stream()
			.map(ObjectError::getDefaultMessage)
			.collect(Collectors.joining(SEMICOLON));
		return R.error(PARAM_BIND_ERROR, error);
	}

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<Object> nullPointerException(NullPointerException e, HttpServletRequest request, HandlerMethod method) {
		if (log.isWarnEnabled()) {
			log.warn(exceptionMessage("Null pointer exception", request, method));
		}
		String message = PARAM_ERROR.getDesc(e.getMessage());
		return R.error(PARAM_ERROR, message, null);
	}

	@ExceptionHandler(SQLException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<Object> process(SQLException e, HttpServletRequest request, HandlerMethod method) {
		if (log.isErrorEnabled()) {
			log.error(exceptionMessage("Sql exception", request, method), e);
		}
		String message = DATABASE_ERROR.getDesc(e.getMessage());
		return R.error(DATABASE_ERROR, message, null);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<Object> handleException(Exception e, HttpServletRequest request) {
		if (log.isErrorEnabled()) {
			log.error(exceptionMessage("Unknown exception", request, null));
		}
		String message = FAILURE.getDesc(e.getMessage());
		return R.error(FAILURE, message, null);
	}

	protected R<Object> createResult(BaseUncheckedException ex) {
		String message;
		// @formatter:off
		try {
			message = ex.getErrorCode().getDesc(ex.getArgs());
		}
		catch (Exception e) {
			message = ex.getMessage();
			log.warn("Failed to get message from source{} by code {}", getLocaleContext(), ex.getErrorCode().getCode(), e);
		}
		// @formatter:on
		return R.error(ex.getErrorCode(), message, null);
	}

	private String exceptionMessage(String message, HttpServletRequest request, HandlerMethod method) {
		return String.format(message + ": URI [%s] method [%s]", request.getRequestURI(),
				Optional.ofNullable(method).map(HandlerMethod::toString).orElse("NullMethod"));
	}

}
