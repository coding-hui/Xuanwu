import React from 'react';
import { Button, Typography, Space, Popconfirm, Badge } from '@arco-design/web-react';
import { Status } from '@/pages/list/search-table/constants';

const { Text } = Typography;

export const PayTypeTexts = ['未支付', '支付宝', '微信'];

export const OrderTypeTexts = ['正常订单', '秒杀订单'];

export const SourceTypeTexts = ['PC订单', 'APP订单'];

export const OrderStatusTexts = ['待付款', '已完成', '已关闭'];

export function getColumns(
  callback: (record: Record<string, any>, type: string) => Promise<void>
) {
  return [
    {
      title: '订单编号',
      dataIndex: 'orderSn',
      render: (value) => <Text copyable>{value}</Text>
    },
    {
      title: '订单桌号',
      dataIndex: 'tableCode'
    },
    {
      title: '订单金额',
      dataIndex: 'totalAmount',
      render: (_col, record) => (
        <span>￥{record.totalAmount}</span>
      )
    },
    {
      title: '支付方式',
      render: (_col, record) => (
        <span>{record.payType > PayTypeTexts.length ? '-' : PayTypeTexts[record.payType]}</span>
      )
    },
    {
      title: '订单来源',
      render: (_col, record) => (
        <span>{record.sourceType > SourceTypeTexts.length ? '-' : SourceTypeTexts[record.sourceType]}</span>
      )
    },
    {
      title: '订单状态',
      render: (_col, record) => {
        if (record.status === 0) {
          return <Badge status="warning" text={OrderStatusTexts[record.status]}></Badge>;
        }
        if (record.status === 1) {
          return <Badge status="success" text={OrderStatusTexts[record.status]}></Badge>;
        }
        return <Badge status="default" text={OrderStatusTexts[record.status]}></Badge>;
      }
    },
    {
      title: '提交时间',
      dataIndex: 'createdAt'
    },
    {
      title: '操作',
      dataIndex: 'operations',
      width: 180,
      render: (_, record) => (
        <>
          <Space>
            <Button type="primary" size="small" onClick={() => callback(record, 'view')}>查看</Button>
            <Popconfirm
              focusLock
              position="left"
              title={`是否要删除该订单`}
              onOk={async () => {
                await callback(record, 'delete');
              }}
            >
              <Button size="small">
                删除
              </Button>
            </Popconfirm>

          </Space>
        </>
      )
    }
  ];
}