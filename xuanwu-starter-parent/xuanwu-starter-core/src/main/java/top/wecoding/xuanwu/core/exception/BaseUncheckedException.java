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

import lombok.Data;

import java.io.Serial;

/**
 * base unchecked exceptions
 *
 * @author liuyuhui
 */
@Data
public class BaseUncheckedException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	protected ErrorCode supplier;

	protected String exception;

	protected String url;

	protected Object[] args;

	public BaseUncheckedException() {
		super();
	}

	public BaseUncheckedException(String exception) {
		super(exception);
	}

	public BaseUncheckedException(Throwable cause) {
		super(cause);
	}

	public BaseUncheckedException(String exception, Throwable cause) {
		super(exception, cause);
	}

	public BaseUncheckedException(ErrorCode supplier, Throwable cause, String exception, String url, Object... args) {
		super(exception, cause);
		this.supplier = supplier;
		this.exception = exception;
		this.url = url;
		this.args = args;
	}

}
