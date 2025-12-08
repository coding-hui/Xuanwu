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

import java.lang.reflect.Field;
import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

/**
 * @author liuyuhui
 * @author LaoWang
 * @date 2022/02/14
 */
public enum QueryType {

  /** 查询类型 */
  EQUALS {
    @Override
    public Criteria buildCriteria(QueryField queryFieldAnnotation, Field field, Object value) {
      if (check(queryFieldAnnotation, field, value)) {
        String queryField = getQueryFieldName(queryFieldAnnotation, field);
        return Criteria.where(queryField).is(value.toString());
      }
      return new Criteria();
    }
  },

  LIKE {
    @Override
    public Criteria buildCriteria(QueryField queryFieldAnnotation, Field field, Object value) {
      if (check(queryFieldAnnotation, field, value)) {
        String queryField = getQueryFieldName(queryFieldAnnotation, field);
        return Criteria.where(queryField).regex(value.toString());
      }
      return new Criteria();
    }
  },

  IN {
    @Override
    public Criteria buildCriteria(QueryField queryFieldAnnotation, Field field, Object value) {
      if (check(queryFieldAnnotation, field, value)) {
        if (value instanceof List) {
          String queryField = getQueryFieldName(queryFieldAnnotation, field);
          // 此处必须转型为List，否则会在in外面多一层[]
          return Criteria.where(queryField).in((List<?>) value);
        }
      }
      return new Criteria();
    }
  };

  private static boolean check(QueryField queryField, Field field, Object value) {
    return !(queryField == null || field == null || value == null);
  }

  /** 如果实体 Bean 的字段上 QueryField 注解没有设置 attribute 属性时，默认为该字段的名称 */
  private static String getQueryFieldName(QueryField queryField, Field field) {
    String queryFieldValue = queryField.attribute();
    if (!StringUtils.hasText(queryFieldValue)) {
      queryFieldValue = field.getName();
    }
    return queryFieldValue;
  }

  public abstract Criteria buildCriteria(
      QueryField queryFieldAnnotation, Field field, Object value);
}
