import React, { useContext } from 'react';
import {
  Form,
  Grid,
} from '@arco-design/web-react';
import { GlobalContext } from '@/context';
import styles from './style/index.module.less';
import FoodCatSelect from "@/pages/food/create/food-cat-select";

const { Row, Col } = Grid;

const { useForm } = Form;

function SearchForm(props: {
  onSearch: (values: Record<string, any>) => void;
}) {
  const { lang } = useContext(GlobalContext);

  const [form] = useForm();

  const handleSubmit = (val) => {
    props.onSearch({"categoryIds": val});
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
            <Form.Item field="name">
              <FoodCatSelect onChange={handleSubmit} mode='multiple' />
            </Form.Item>
          </Col>
        </Row>
      </Form>
      {/*<div className={styles['right-button']}>*/}
      {/*  <Space>*/}
      {/*    <Button type="primary" icon={<IconSearch />} onClick={handleSubmit}>*/}
      {/*      查询*/}
      {/*    </Button>*/}
      {/*    <Button icon={<IconRefresh />} onClick={handleReset}>*/}
      {/*      重置*/}
      {/*    </Button>*/}
      {/*  </Space>*/}
      {/*</div>*/}
    </div>
  );
}

export default SearchForm;