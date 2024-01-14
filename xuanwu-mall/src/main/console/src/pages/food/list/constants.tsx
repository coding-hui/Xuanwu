import React from 'react';
import { Button, Typography, Space, Popconfirm } from '@arco-design/web-react';

const { Text } = Typography;

export const Status = ['未上线', '已上线'];

export function getColumns(
  callback: (record: Record<string, any>, type: string) => Promise<void>
) {
  return [
    {
      title: '编号',
      dataIndex: 'id',
      render: (value) => <Text copyable>{value}</Text>
    },
    {
      title: '菜品名称',
      dataIndex: 'name'
    },
    {
      title: '菜品分类',
      dataIndex: 'category.name'
    },
    {
      title: '价格',
      dataIndex: 'price',
      render: (_col, record) => (
        <span>￥{record.price}</span>
      )
    },
    {
      title: '单位',
      dataIndex: 'unit'
    },
    {
      title: '排序',
      dataIndex: 'sort'
    },
    {
      title: '更新时间',
      dataIndex: 'updatedAt'
    },
    {
      title: '描述',
      dataIndex: 'description'
    },
    {
      title: '操作',
      dataIndex: 'operations',
      width: 180,
      render: (_, record) => (
        <>
          <Space>
            <Button type="primary" size="small" onClick={() => callback(record, 'edit')}>编辑</Button>
            <Popconfirm
              focusLock
              position="left"
              title={`是否要删除【${record.name}】`}
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