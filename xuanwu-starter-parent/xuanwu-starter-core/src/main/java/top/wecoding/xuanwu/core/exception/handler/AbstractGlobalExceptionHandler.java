// /*
// * Copyright (c) 2022. WeCoding (wecoding@yeah.net).
// *
// * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.gnu.org/licenses/lgpl.html
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
// package top.wecoding.xuanwu.core.exception.handler;
//
// import jakarta.validation.ConstraintViolation;
// import jakarta.validation.ConstraintViolationException;
// import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.context.MessageSource;
// import org.springframework.context.i18n.LocaleContextHolder;
// import org.springframework.context.support.DefaultMessageSourceResolvable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.converter.HttpMessageNotReadableException;
// import org.springframework.validation.BindException;
// import org.springframework.validation.ObjectError;
// import org.springframework.web.HttpMediaTypeNotAcceptableException;
// import org.springframework.web.HttpMediaTypeNotSupportedException;
// import org.springframework.web.HttpRequestMethodNotSupportedException;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.MissingServletRequestParameterException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
// import top.wecoding.commons.core.constant.StrPool;
// import top.wecoding.commons.core.enums.SystemErrorCodeEnum;
// import top.wecoding.commons.core.exception.BaseUncheckedException;
// import top.wecoding.commons.core.exception.DemoException;
// import top.wecoding.commons.core.exception.IllegalParameterException;
// import top.wecoding.commons.core.exception.ServerException;
// import top.wecoding.commons.core.model.R;
//
// import java.util.Objects;
// import java.util.Set;
// import java.util.stream.Collectors;
//
// /**
// * 全局异常处理基类
// *
// * @author liuyuhui
// */
// @Slf4j
// @AllArgsConstructor
// public abstract class AbstractGlobalExceptionHandler {
//
// protected final MessageSource messageSource;
//
// @ResponseStatus(HttpStatus.BAD_REQUEST)
// @ExceptionHandler(IllegalParameterException.class)
// public R<Object> argumentException(IllegalParameterException e) {
// R<Object> result = createResult(e);
// Throwable cause = e.getCause();
// if (cause != null) {
// log.warn("IllegalArgumentException: ", cause);
// }
// log.debug("IllegalArgumentException: {} {}", result.getCode(), result.getMsg());
// return result;
// }
//
// @ResponseStatus(HttpStatus.BAD_REQUEST)
// @ExceptionHandler(ServerException.class)
// public R<Object> serverException(ServerException e) {
// log.error("ServerException: ", e);
// return createResult(e);
// }
//
// @ResponseStatus(HttpStatus.BAD_REQUEST)
// @ExceptionHandler(DemoException.class)
// public R<Object> demoException(DemoException e) {
// log.warn("ServerException: ", e);
// return createResult(e);
// }
//
// @ExceptionHandler(MissingServletRequestParameterException.class)
// @ResponseStatus(HttpStatus.BAD_REQUEST)
// public R<Object> missParamException(MissingServletRequestParameterException e) {
// log.warn("MissingServletRequestParameterException: ", e);
// String parameterType = e.getParameterType();
// String parameterName = e.getParameterName();
// String message =
// messageSource.getMessage(
// SystemErrorCodeEnum.PARAM_MISS.getCode(),
// new Object[] {parameterName, parameterType},
// LocaleContextHolder.getLocale());
// return R.error(SystemErrorCodeEnum.PARAM_ERROR, message, null, null, e.getCause());
// }
//
// @ExceptionHandler(MethodArgumentTypeMismatchException.class)
// @ResponseStatus(HttpStatus.BAD_REQUEST)
// public R<Object> handleError(MethodArgumentTypeMismatchException e) {
// log.warn("MethodArgumentTypeMismatchException: ", e);
// String message =
// messageSource.getMessage(
// SystemErrorCodeEnum.PARAM_TYPE_ERROR.getCode(),
// new Object[] {e.getName()},
// LocaleContextHolder.getLocale());
// return R.error(SystemErrorCodeEnum.PARAM_TYPE_ERROR, message);
// }
//
// @ExceptionHandler(MethodArgumentNotValidException.class)
// @ResponseStatus(HttpStatus.BAD_REQUEST)
// public R<Object> validatedBindException(MethodArgumentNotValidException e) {
// log.warn("MethodArgumentNotValidException: ", e);
// String message =
// e.getBindingResult().getAllErrors().stream()
// .map(
// objectError ->
// String.format(
// "%s %s",
// ((DefaultMessageSourceResolvable)
// Objects.requireNonNull(objectError.getArguments())[0])
// .getDefaultMessage(),
// objectError.getDefaultMessage()))
// .collect(Collectors.joining(StrPool.SEMICOLON));
// return R.error(SystemErrorCodeEnum.PARAM_BIND_ERROR, message);
// }
//
// @ExceptionHandler(IllegalStateException.class)
// @ResponseStatus(HttpStatus.BAD_REQUEST)
// public R<Object> illegalStateException(IllegalStateException e) {
// log.warn("IllegalStateException:", e);
// String message =
// messageSource.getMessage(
// SystemErrorCodeEnum.PARAM_ERROR.getCode(),
// new Object[] {e.getMessage()},
// LocaleContextHolder.getLocale());
// return R.error(SystemErrorCodeEnum.PARAM_ERROR, message, null, null, e.getCause());
// }
//
// @ExceptionHandler(ConstraintViolationException.class)
// @ResponseStatus(HttpStatus.BAD_REQUEST)
// public R<Object> constraintViolationException(ConstraintViolationException e) {
// log.warn("ConstraintViolationException: ", e);
// Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
// String message =
// violations.stream()
// .map(ConstraintViolation::getMessage)
// .collect(Collectors.joining(StrPool.SEMICOLON));
// return R.error(SystemErrorCodeEnum.PARAM_VALID_ERROR, message, null, null,
// e.getCause());
// }
//
// @ExceptionHandler(HttpMessageNotReadableException.class)
// @ResponseStatus(HttpStatus.BAD_REQUEST)
// public R<Object> handleError(HttpMessageNotReadableException e) {
// log.error("HttpMessageNotReadableException: ", e);
// String message =
// messageSource.getMessage(
// SystemErrorCodeEnum.MSG_NOT_READABLE.getCode(),
// new Object[] {e.getMessage()},
// LocaleContextHolder.getLocale());
// return R.error(SystemErrorCodeEnum.MSG_NOT_READABLE, message);
// }
//
// @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
// @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
// public R<Object> handleError(HttpRequestMethodNotSupportedException e) {
// log.error("HttpRequestMethodNotSupportedException: ", e);
// String message =
// messageSource.getMessage(
// SystemErrorCodeEnum.METHOD_NOT_SUPPORTED.getCode(),
// new Object[] {e.getMessage()},
// LocaleContextHolder.getLocale());
// return R.error(SystemErrorCodeEnum.METHOD_NOT_SUPPORTED, message);
// }
//
// @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
// @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
// public R<Object> handleError(HttpMediaTypeNotSupportedException e) {
// log.error("HttpMediaTypeNotSupportedException: ", e);
// String message =
// messageSource.getMessage(
// SystemErrorCodeEnum.MEDIA_TYPE_NOT_SUPPORTED.getCode(),
// new Object[] {e.getMessage()},
// LocaleContextHolder.getLocale());
// return R.error(SystemErrorCodeEnum.MEDIA_TYPE_NOT_SUPPORTED, message);
// }
//
// @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
// @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
// public R<Object> handleError(HttpMediaTypeNotAcceptableException e) {
// String message =
// messageSource.getMessage(
// SystemErrorCodeEnum.MEDIA_TYPE_NOT_ACCEPT.getCode(),
// new Object[] {
// e.getMessage()
// + " "
// + e.getSupportedMediaTypes().stream().map(MediaType::getQualityValue)
// },
// LocaleContextHolder.getLocale());
// log.error("HttpMediaTypeNotAcceptableException: ", e);
// return R.error(SystemErrorCodeEnum.MEDIA_TYPE_NOT_ACCEPT, message);
// }
//
// @ExceptionHandler(BindException.class)
// @ResponseStatus(HttpStatus.BAD_REQUEST)
// public R<Object> validatedBindException(BindException e) {
// log.warn("BindException: ", e);
// String error =
// e.getBindingResult().getAllErrors().stream()
// .map(ObjectError::getDefaultMessage)
// .collect(Collectors.joining(StrPool.SEMICOLON));
// return R.error(SystemErrorCodeEnum.PARAM_BIND_ERROR, error);
// }
// //
// // @ExceptionHandler(NoHandlerFoundException.class)
// // @ResponseStatus(HttpStatus.NOT_FOUND)
// // public R<Object> handleError(NoHandlerFoundException e) {
// // log.error("NoHandlerFoundException: ", e);
// // String message =
// // messageSource.getMessage(
// // SystemErrorCodeEnum.NOT_FOUND.getCode(),
// // new Object[] {e.getMessage()},
// // LocaleContextHolder.getLocale());
// // return R.error(SystemErrorCodeEnum.NOT_FOUND, message);
// // }
//
// @ExceptionHandler(NullPointerException.class)
// @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
// public R<Object> nullPointerException(NullPointerException e) {
// log.warn("NullPointerException: ", e);
// String message =
// messageSource.getMessage(
// SystemErrorCodeEnum.PARAM_ERROR.getCode(),
// new Object[] {e.getMessage()},
// LocaleContextHolder.getLocale());
// return R.error(SystemErrorCodeEnum.PARAM_ERROR, message, null, null, e.getCause());
// }
//
// @ExceptionHandler(Exception.class)
// @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
// public R<Object> handleException(Exception e) {
// log.warn("Exception: ", e);
// String message =
// messageSource.getMessage(
// SystemErrorCodeEnum.FAILURE.getCode(),
// new Object[] {e.getMessage()},
// LocaleContextHolder.getLocale());
// return R.error(SystemErrorCodeEnum.FAILURE, message, null, null, e.getCause());
// }
//
// protected R<Object> createResult(BaseUncheckedException ex) {
// String message;
// try {
// message =
// messageSource.getMessage(
// ex.getSupplier().getCode(), ex.getArgs(), LocaleContextHolder.getLocale());
// } catch (Exception e) {
// message = ex.getMessage();
// log.warn(
// "Failed to get message from source{} by code {}",
// LocaleContextHolder.getLocaleContext(),
// ex.getSupplier().getCode(),
// e);
// }
// return R.error(ex.getSupplier(), message, null, ex.getException(), ex.getCause());
// }
// }
