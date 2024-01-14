import React from 'react';
import { Button, Typography, Space, Popconfirm } from '@arco-design/web-react';
import AddFoodCat from '@/pages/food-cat/list/add-form';

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
      title: '分类名称',
      dataIndex: 'name'
    },
    {
      title: '描述',
      dataIndex: 'description'
    },
    {
      title: '更新时间',
      dataIndex: 'updatedAt'
    },
    {
      title: '操作',
      dataIndex: 'operations',
      width: 180,
      render: (_, record) => (
        <>
          <Space>
            <AddFoodCat foodCatId={record.id} onOk={() => callback(record, 'edit')} />
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