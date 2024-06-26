package top.wecoding.xuanwu.core.helper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author liuyuhui
 */
public class ExceptionHelper {

	/**
	 * 将CheckedException转换为UncheckedException.
	 * @param e Throwable
	 * @return {RuntimeException}
	 */
	public static RuntimeException unchecked(Throwable e) {
		if (e instanceof Error) {
			throw (Error) e;
		}
		else if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		}
		else if (e instanceof InvocationTargetException) {
			return ExceptionHelper.runtime(((InvocationTargetException) e).getTargetException());
		}
		else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		else if (e instanceof InterruptedException) {
			Thread.currentThread().interrupt();
		}
		return ExceptionHelper.runtime(e);
	}

	/**
	 * 不采用 RuntimeException 包装，直接抛出，使异常更加精准
	 * @param throwable Throwable
	 * @param <T> 泛型标记
	 * @return Throwable
	 * @throws T 泛型
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Throwable> T runtime(Throwable throwable) throws T {
		throw (T) throwable;
	}

	/**
	 * 代理异常解包
	 * @param wrapped 包装过得异常
	 * @return 解包后的异常
	 */
	public static Throwable unwrap(Throwable wrapped) {
		Throwable unwrapped = wrapped;
		while (true) {
			if (unwrapped instanceof InvocationTargetException) {
				unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
			}
			else if (unwrapped instanceof UndeclaredThrowableException) {
				unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
			}
			else {
				return unwrapped;
			}
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 * @param ex Throwable
	 * @return {String}
	 */
	public static String getStackTraceAsString(Throwable ex) {
		StringWriter printWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(printWriter));
		return printWriter.toString();
	}

}
