import React from 'react';
import { Button, Typography, Space } from '@arco-design/web-react';
import { IconDelete, IconPlus, IconPlusCircle } from '@arco-design/web-react/icon';

const { Text } = Typography;

export const Status = ['未上线', '已上线'];

export function getColumns(
  callback: (record: Record<string, any>, type: string) => void
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
      title: '描述',
      dataIndex: 'description'
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
      title: '操作',
      dataIndex: 'operations',
      width: 80,
      render: (_, record) => (
        <>
          <Space>
            <Button
              size="small"
              shape="circle"
              icon={<IconPlus />}
              onClick={() => callback(record, 'add')}
            />
            <Button
              size="small"
              shape="circle"
              icon={<IconDelete />}
              onClick={() => callback(record, 'delete')}
            />
          </Space>
        </>
      )
    }
  ];
}