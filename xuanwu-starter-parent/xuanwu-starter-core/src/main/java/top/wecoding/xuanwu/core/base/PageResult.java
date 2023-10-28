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
package top.wecoding.xuanwu.core.base;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * WeCoding 分页参数
 *
 * @author liuyuhui
 * @date 2022/02/19
 */
public class PageResult<T> extends PageSerializable<T> {

	public PageResult() {
	}

	public PageResult(List<T> list, long total) {
		this.records = list;
		this.total = total;
	}

	public static <T> PageResult<T> of(List<T> list, long total) {
		return new PageResult<>(list, total);
	}

	public static <T> PageResult<T> empty() {
		return new PageResult<>(Collections.emptyList(), 0L);
	}

	@Override
	public <U> PageResult<U> map(Function<? super T, ? extends U> converter) {
		return of(getConvertedContent(converter), total);
	}

}
