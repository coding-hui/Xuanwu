import React from 'react';
import { Button, Typography, Space, Popconfirm, Switch } from '@arco-design/web-react';
import AddForm from '@/pages/system/printer/add-form';
import { PayTypeTexts } from '@/pages/order/list/constants';

const { Text } = Typography;

export const Status = ['未启用', '已启用'];

export const Types = ['USB', '网口', '云打印'];

export function getColumns(
  callback: (record: Record<string, any>, type: string, status?: boolean) => Promise<void>
) {
  return [
    {
      title: '编号',
      dataIndex: 'id',
      render: (value) => <Text copyable>{value}</Text>
    },
    {
      title: '打印机名称',
      dataIndex: 'name'
    },
    {
      title: '类型',
      render: (_col, record) => (
        <span>{record.type > Types.length ? '-' : Types[record.type]}</span>
      )
    },
    {
      title: '备注',
      dataIndex: 'description'
    },
    {
      title: '更新时间',
      dataIndex: 'updatedAt'
    },
    {
      title: '是否启用',
      dataIndex: 'status',
      render: (_col, record) => {
        return <Switch checked={record.status} onChange={(checked) => callback(record, 'change-status', checked)} />;
      }
    },
    {
      title: '操作',
      dataIndex: 'operations',
      width: 180,
      render: (_, record) => (
        <>
          <Space>
            <AddForm printerId={record.id} onOk={() => callback(record, 'edit')} />
            <Button size="small" onClick={() => callback(record, 'print-test')}>
              打印测试页
            </Button>
            <Popconfirm
              focusLock
              position="left"
              title={`是否要删除【${record.name}】分类`}
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