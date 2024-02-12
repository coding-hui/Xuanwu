import React, { useEffect, useState } from 'react';
import {
  Card,
  Steps,
  Typography,
  Grid,
  Space,
  Button,
  Table, Modal, Message, Divider
} from '@arco-design/web-react';
import BasicInfo from './item';
import styles from './style/index.module.less';
import {cancelOrder, deleteOrderItem, getOrder, Order, OrderItem, paySuccess} from '@/api/food/order';
import { useHistory, useLocation } from 'react-router-dom';
import {IconAlipayCircle, IconDelete, IconPlus, IconWechatpay} from '@arco-design/web-react/icon';
import { submitPrintJob } from '@/api/food/printer';
import {Tooltip} from "bizcharts";

function BasicProfile() {
  const history = useHistory();
  const location = useLocation<{ orderId: null, printSalesTicket: boolean }>();
  const [fetchLoading, setFetchLoading] = useState(false);
  const [paying, setPaying] = useState(false);
  const [order, setOrder] = useState<Order>();
  const [orderItems, setOrderItems] = useState<OrderItem[]>([]);
  const [payOrderModalVisible, setPayOrderModalVisible] = useState(false);

  const { orderId, printSalesTicket } = location.state;

  function fetchOrderDetail() {
    setFetchLoading(true);
    getOrder(orderId)
      .then(res => {
        setOrder(res.data);
        setOrderItems(res.data && res.data.orderItems ? res.data.orderItems : []);
      })
      .finally(() => {
        setFetchLoading(false);
      });
  }

  function handleSubmitPrintJob() {
    setFetchLoading(true);
    submitPrintJob(orderId)
      .then(res => {
        Message.success('提交成功');
      })
      .finally(() => {
        setFetchLoading(false);
      });
  }

  const handlePayOrder = (payType: number, modalIns) => {
    if (order) {
      setPaying(true);
      paySuccess(order.id, payType, printSalesTicket).then(res => {
        Message.success('订单已完成');
        setTimeout(() => {
          history.push('/order/table');
        }, 500);
      }).finally(() => {
        setPaying(false);
        setPayOrderModalVisible(false);
        modalIns.close();
      });
    }
  };

  const handleOpenPayOrder = () => {
    const modalIns = Modal.info({
      title: '选择支付方式',
      closable: true,
      footer: null,
      visible: payOrderModalVisible,
      content: (
        <div style={{ textAlign: 'center' }}>
          <Space split={<Divider type="vertical" />}>
            <Button icon={<IconAlipayCircle />} size="large" onClick={() => handlePayOrder(1, modalIns)}>支付宝</Button>
            <Button icon={<IconWechatpay />} size="large" onClick={() => handlePayOrder(2, modalIns)}>微信</Button>
          </Space>
        </div>
      ),
      onCancel: () => {
        setPaying(false);
        setPayOrderModalVisible(false);
      }
    });
  };

  const handleCancelOrder = () => {
    Modal.confirm({
      title: '是否要取消该订单',
      okButtonProps: {
        status: 'danger'
      },
      onCancel: () => {
        setFetchLoading(false);
      },
      onOk: () => {
        if (order) {
          setFetchLoading(true);
          cancelOrder(order.id).then(res => {
            Message.success('取消成功');
            setTimeout(() => {
              history.push('/order/table');
            }, 500);
          }).finally(() => {
            setFetchLoading(false);
          });
        }
      }
    });
  };

  const handleDeleteOrderItem = (orderItemId: number, foodName: string) => {
    // Modal.confirm({
    //   title: `是否要移除【${foodName}】菜品`,
    //   okButtonProps: {
    //     status: 'danger'
    //   },
    //   onCancel: () => {
    //     setFetchLoading(false);
    //   },
    //   onOk: () => {
    if (order) {
      setFetchLoading(true);
      deleteOrderItem(order.id, orderItemId).then(res => {
        Message.success('操作成功');
        fetchOrderDetail();
      }).finally(() => {
        setFetchLoading(false);
      });
    }
      // }
    // });
  };

  useEffect(() => {
    fetchOrderDetail();
  }, []);

  return (
    <div className={styles.container}>
      <Card>
        <Grid.Row justify="space-between" align="center">
          <Grid.Col span={16}>
            <Typography.Title heading={6}>
              订单流程
            </Typography.Title>
          </Grid.Col>
          <Grid.Col span={8} style={{ textAlign: 'right' }}>
            <Space>
              <Button onClick={() => history.push('/order/table')}>返回</Button>
              {order && order.status === 0 && <Button onClick={handleCancelOrder}>取消订单</Button>}
              {order && order.status === 0 && <Button onClick={handleSubmitPrintJob}>打印</Button>}
              {order && order.status === 0 && <Button type="primary" onClick={handleOpenPayOrder}>结账</Button>}
            </Space>
          </Grid.Col>
        </Grid.Row>
        <Steps current={order && order.status ? order.status + 1 : 1} className={styles.steps}>
          <Steps.Step title="点餐/提交订单" />
          <Steps.Step title="支付订单" />
          <Steps.Step title="完成" />
        </Steps>
      </Card>
      {order && <BasicInfo
        order={order}
        loading={fetchLoading || paying}
      />}
      <Card>
        <Typography.Title heading={6}>
          菜品信息
        </Typography.Title>
        <Table
          loading={fetchLoading || paying}
          data={orderItems}
          pagination={false}
          rowKey="id"
          columns={[
            {
              title: '序号',
              render: (_col, _record, index) => (
                <span>{index + 1}</span>
              )
            },
            {
              dataIndex: 'foodName',
              title: '菜品名称'
            },
            {
              title: '菜品价格',
              dataIndex: 'price',
              render: (_col, record) => (
                <span>￥{record.foodPrice}</span>
              )
            },
            {
              title: '数量',
              dataIndex: 'foodQuantity'
            },
            {
              title: '小计',
              render: (_col, record) => (
                <span>￥{record.foodPrice}</span>
              )
            },
            {
              title: '操作',
              width: 80,
              render: (_, record) => (
                  <>
                    <Space>
                        <Button
                            size="small"
                            shape="circle"
                            icon={<IconDelete />}
                            onClick={() => handleDeleteOrderItem(record.id, record.foodName)}
                        />
                    </Space>
                  </>
              )
            }
          ]}
          summary={(currentData) => (
            <Table.Summary>
              <Table.Summary.Row>
                <Table.Summary.Cell>合计</Table.Summary.Cell>
                <Table.Summary.Cell />
                <Table.Summary.Cell />
                <Table.Summary.Cell />
                <Table.Summary.Cell>
                  <Typography.Text>
                    {currentData.reduce((prev, next) => prev + next.foodQuantity, 0)}
                  </Typography.Text>
                </Table.Summary.Cell>
                <Table.Summary.Cell>
                  <Typography.Text style={{ fontWeight: 600 }}>
                    ￥{order && order.totalAmount}
                  </Typography.Text>
                </Table.Summary.Cell>
              </Table.Summary.Row>
            </Table.Summary>
          )}
        />
      </Card>
    </div>
  );
}

export default BasicProfile;
