import React, { useEffect, useState } from 'react';
import { Tabs, Card, Input, Typography, Grid, Spin } from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import CardBlock from './card-block';
import AddCard from './card-add';
import { listOrderTable } from '@/api/food/order-table';
import { AVAILABLE, IN_USE, ORDERING } from '@/pages/order/table/constants';

const { Title } = Typography;
const { Row, Col } = Grid;

export default function ListCard() {
  const [loading, setLoading] = useState(true);
  const [orderTables, setOrderTables] = useState([]);

  const [activeKey, setActiveKey] = useState<string | number>('all');

  const getOrderTables = (keyword?: string) => {
    setLoading(true);
    const status = activeKey === 'all' ? null : activeKey;
    listOrderTable({ page: 0, size: 100, status: status as number, code: keyword === '' ? null : keyword })
      .then((res) => {
        setOrderTables(res.data.records);
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    getOrderTables();
  }, [activeKey]);

  return (
    <Card>
      <Title heading={6}>创建订单</Title>
      <Tabs
        activeTab={activeKey as string}
        type="rounded"
        onChange={setActiveKey}
        extra={
          <Input.Search
            style={{ width: '240px' }}
            placeholder="请输入桌号"
            onChange={(keyword) => {
              getOrderTables(keyword);
            }}
          />
        }
      >
        <Tabs.TabPane key="all" title="全部" />
        <Tabs.TabPane key={AVAILABLE} title="空闲" />
        <Tabs.TabPane key={ORDERING} title="点餐中" />
        <Tabs.TabPane key={IN_USE} title="用餐中" />
      </Tabs>
      <Spin loading={loading} className={styles.container}>
        <Title heading={6}>订单桌位号</Title>
        <Row gutter={24} className={styles['card-content']}>
          {
            orderTables.map(table => {
              return (
                <Col key={table.code} xs={24} sm={12} md={8} lg={6} xl={6} xxl={6}>
                  <CardBlock orderTable={table} onDel={() => getOrderTables()} />
                </Col>
              );
            })
          }
          <Col xs={24} sm={12} md={8} lg={6} xl={6} xxl={6}>
            <AddCard description="点击新增卓号" onOk={() => getOrderTables()} />
          </Col>
        </Row>
      </Spin>
    </Card>
  );
}
