import React, { useContext } from 'react';
import {
  Form,
  Input,
  Button,
  Grid,
  Space, Select
} from '@arco-design/web-react';
import { GlobalContext } from '@/context';
import { IconRefresh, IconSearch } from '@arco-design/web-react/icon';
import styles from './style/index.module.less';
import { FilterType } from '@/pages/list/search-table/constants';
import { OrderStatusTexts, SourceTypeTexts } from '@/pages/order/list/constants';

const { Row, Col } = Grid;

const { useForm } = Form;

function SearchForm(props: {
  onSearch: (values: Record<string, any>) => void;
}) {
  const { lang } = useContext(GlobalContext);

  const [form] = useForm();

  const handleSubmit = () => {
    const values = form.getFieldsValue();
    props.onSearch(values);
  };

  const handleReset = () => {
    form.resetFields();
    props.onSearch({});
  };

  const colSpan = lang === 'zh-CN' ? 8 : 12;

  return (
    <div className={styles['search-form-wrapper']}>
      <Form
        form={form}
        className={styles['search-form']}
        labelAlign="left"
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 19 }}
      >
        <Row gutter={24}>
          <Col span={colSpan}>
            <Form.Item label="订单编号" field="orderSn">
              <Input placeholder="请输入订单编号" allowClear />
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item label="订单状态" field="status">
              <Select
                placeholder="请选择订单状态"
                options={OrderStatusTexts.map((item, index) => ({
                  label: item,
                  value: index
                }))}
                allowClear
              />
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item label="订单来源" field="sourceType">
              <Select
                placeholder="请选择订单来源"
                options={SourceTypeTexts.map((item, index) => ({
                  label: item,
                  value: index
                }))}
                allowClear
              />
            </Form.Item>
          </Col>
        </Row>
      </Form>
      <div className={styles['right-button']}>
        <Space>
          <Button type="primary" icon={<IconSearch />} onClick={handleSubmit}>
            查询
          </Button>
          <Button icon={<IconRefresh />} onClick={handleReset}>
            重置
          </Button>
        </Space>
      </div>
    </div>
  );
}

export default SearchForm;
