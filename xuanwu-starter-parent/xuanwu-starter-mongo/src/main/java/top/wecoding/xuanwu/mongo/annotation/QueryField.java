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
package top.wecoding.xuanwu.mongo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * type表示查询类似，默认为equals attribute表示要查询的属性，默认为空串，如果为空则为字段名称
 *
 * @author liuyuhui
 * @author LaoWang
 * @date 2022/02/14
 * @link <a href="https://gitee.com/qwer.com/open-mongodb">...</a>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryField {

  QueryType type();

  String attribute() default "";
}
