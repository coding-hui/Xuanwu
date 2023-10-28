/*
 * Copyright (c) 2022. WeCoding (wecoding@yeah.net).
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.wecoding.xuanwu.core.exception;

/**
 * 参数异常
 *
 * @author liuyuhui
 * @date 2022/04/04
 */
public class IllegalParameterException extends BaseUncheckedException {

	public IllegalParameterException() {
		super();
	}

	public IllegalParameterException(ErrorCode supplier, Object... args) {
		this(supplier, null, args);
	}

	public IllegalParameterException(ErrorCode supplier, Throwable cause, Object... args) {
		this(supplier, cause, null, null, args);
	}

	public IllegalParameterException(ErrorCode supplier, String exception, String url, Object... args) {
		this(supplier, null, exception, url, args);
	}

	public IllegalParameterException(ErrorCode supplier, Throwable cause, String exception, String url,
			Object... args) {
		super(supplier, cause, exception, url, args);
	}

}
