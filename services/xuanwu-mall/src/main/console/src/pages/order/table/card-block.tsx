import React, { useEffect, useState } from 'react';
import cs from 'classnames';
import {
  Button,
  Tag,
  Card,
  Descriptions,
  Dropdown,
  Menu,
  Skeleton, Message, Modal, Typography, Space, Popconfirm
} from '@arco-design/web-react';
import {
  IconCheckCircleFill,
  IconMore
} from '@arco-design/web-react/icon';
import styles from './style/index.module.less';
import { useHistory } from 'react-router-dom';
import {
  completedOrderTable,
  deleteOrderTable,
  OrderTable,
  updateOrderTable,
  UpdateOrderTableRequest
} from '@/api/food/order-table';
import AddForm from '@/pages/order/table/add-form';
import { AVAILABLE } from '@/pages/order/table/constants';
import CreateOrder from '@/pages/order/table/create-order';
import { cancelOrder } from '@/api/food/order';

interface CardBlockType {
  orderTable: OrderTable;
  loading?: boolean;
  onOk?: (values: OrderTable) => void;
  onDel?: (orderTable: OrderTable) => void;
}


function CardBlock(props: CardBlockType) {
  const { orderTable, onOk, onDel } = props;
  const [visible, setVisible] = useState(false);
  const [status, setStatus] = useState(orderTable.status);
  const [loading, setLoading] = useState(props.loading);
  const [addFormVisible, setAddFormVisible] = useState(false);

  const history = useHistory();

  // const handleCompletedOrderTable = () => {
  //   setLoading(true);
  //   completedOrderTable(orderTable.id).then(res => {
  //     console.log(res);
  //     if (res && res.success) {
  //       Message.success('操作成功');
  //     }
  //   }).finally(() => {
  //     setLoading(false);
  //   });
  // };

  const toCreateOrder = () => {
    history.push('/order/create', {
      orderId: orderTable.order ? orderTable.order.id : null,
      orderTableCode: orderTable.code,
      orderTableStatus: status
    });
  };

  const handleCancelOrder = () => {
    Modal.confirm({
      title: '是否要取消该订单',
      okButtonProps: {
        status: 'danger'
      },
      onCancel: () => {
        setLoading(false);
      },
      onOk: async () => {
        setLoading(true);
        if (orderTable.order) {
          await cancelOrder(orderTable.order.id);
        } else {
          await updateOrderTable(orderTable.id, { ...orderTable, status: AVAILABLE });
        }
        Message.success('取消成功');
        setStatus(AVAILABLE);
        setLoading(false);
      }
    });
  };

  const handleDeleteOrderTable = () => {
    Modal.confirm({
      title: `是否要删除【${orderTable.code}】桌号？`,
      content: '删除后无法恢复，请谨慎操作！',
      onOk: () => {
        setLoading(true);
        deleteOrderTable(orderTable.id).then(() => {
          if (onDel) {
            onDel(orderTable);
          }
          Message.success('操作成功');
        }).finally(() => {
          setLoading(false);
        });
      }
    });
  };

  useEffect(() => {
    setLoading(props.loading);
  }, [props.loading]);

  useEffect(() => {
    if (orderTable.status !== status) {
      setStatus(orderTable.status);
    }
  }, [orderTable.status]);

  const getButtonGroup = () => {
    return (
      <>
        {status === AVAILABLE ? (
          <CreateOrder
            preSubmit={() => setLoading(true)}
            onOk={() => {
              setLoading(false);
              toCreateOrder();
            }}
            orderTable={orderTable}
          />
        ) : (
          <Space>
            {orderTable.order &&
              <Button type="primary" size="small" loading={loading} onClick={() => {
                history.push(`/order/detail`, {
                  orderId: orderTable.order.id
                });
              }}>
                查看
              </Button>
            }
            {/*{orderTable.order &&*/}
            {/*  <Button type="secondary" size="small" loading={loading} onClick={handleCompletedOrderTable}>*/}
            {/*    翻台*/}
            {/*  </Button>*/}
            {/*}*/}
            <Button size="small" loading={loading} onClick={toCreateOrder}>
              加菜
            </Button>
          </Space>
        )}
      </>
    );
  };

  const getStatus = () => {
    switch (status) {
      case 1:
        return (
          <Tag
            color="green"
            icon={<IconCheckCircleFill />}
            className={styles.status}
            size="small"
          >
            空闲
          </Tag>
        );
      case 2:
        return (
          <Tag
            color="blue"
            icon={<IconCheckCircleFill />}
            className={styles.status}
            size="small"
          >
            点餐中
          </Tag>
        );
      case 3:
        return (
          <Tag
            color="blue"
            icon={<IconCheckCircleFill />}
            className={styles.status}
            size="small"
          >
            用餐中
          </Tag>
        );
      default:
        return null;
    }
  };

  const getContent = () => {
    if (loading) {
      return (
        <Skeleton
          text={{ rows: 2 }}
          animation
          className={styles['card-block-skeleton']}
        />
      );
    }
    // if (type !== 'quality') {
    //    return <Typography.Paragraph>{orderTable.description}</Typography.Paragraph>;
    // }
    return (
      <Descriptions
        column={2}
        layout="inline-vertical"
        data={[
          { label: '订单金额', value: orderTable.order ? '￥' + orderTable.order.totalAmount : '-' },
          { label: '就餐人数', value: status === AVAILABLE ? '-' : orderTable.numberOfDiners }
        ]}
      />
    );
  };

  const className = cs(styles['card-block']);

  return (
    <>
      <AddForm
        visible={addFormVisible}
        orderTable={orderTable}
        onOk={(v) => {
          setAddFormVisible(false);
          onOk(v);
        }}
        onCancel={() => setAddFormVisible(false)}
      />
      <Card
        bordered={true}
        className={className}
        size="small"
        title={
          loading ? (
            <Skeleton
              animation
              text={{ rows: 1, width: ['100%'] }}
              style={{ width: '120px', height: '24px' }}
              className={styles['card-block-skeleton']}
            />
          ) : (
            <>
              <div
                className={cs(styles.title, {
                  [styles['title-more']]: visible
                })}
              >
                {orderTable.code}
                {getStatus()}
                <Dropdown
                  triggerProps={{
                    trigger: 'hover'
                  }}
                  droplist={
                    <Menu>
                      <Menu.Item
                        key="cancel"
                        hidden={status === AVAILABLE}
                        onClick={handleCancelOrder}
                      >
                        <Button size="small" type="text" status="danger">
                          取消订单
                        </Button>
                      </Menu.Item>
                      <Menu.Item
                        key="edit"
                        hidden={status !== AVAILABLE}
                        onClick={() => setAddFormVisible(true)}>
                        <Button size="small" type="text">
                          编辑
                        </Button>
                      </Menu.Item>
                      <Menu.Item
                        key="delete"
                        hidden={status !== AVAILABLE}
                        onClick={handleDeleteOrderTable}>
                        <Button size="small" type="text" status="default">
                          删除
                        </Button>
                      </Menu.Item>
                    </Menu>
                  }
                  trigger="click"
                  onVisibleChange={setVisible}
                  popupVisible={visible}
                >
                  <div className={styles.more}>
                    <IconMore />
                  </div>
                </Dropdown>
              </div>
            </>
          )
        }
      >
        <div className={styles.content}>{getContent()}</div>
        <div className={styles.extra}>{getButtonGroup()}</div>
      </Card>
    </>
  );
}

export default CardBlock;
