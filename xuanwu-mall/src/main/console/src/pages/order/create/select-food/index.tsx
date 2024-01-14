import React, { useState, useEffect } from 'react';
import {
  Table,
  Card,
  Typography,
  PaginationProps
} from '@arco-design/web-react';
import { getColumns } from './constants';

import SearchForm from './search-form';
import { FoodInfo, listFood } from '@/api/food/food';

const { Title } = Typography;

export const Status = ['已上线', '未上线'];

type Props = {
  onSelect: (values: FoodInfo) => void
  onRemove: (foodId: number) => void
}

function SelectFood(props: Props) {
  const { onSelect, onRemove } = props;

  const tableCallback = (record, type) => {
    if (type === 'delete') {
      onRemove(record.id);
      // Message.success('移除成功');
    }
    if (type === 'add') {
      onSelect(record);
      // Message.success('添加成功');
      return;
    }
  };

  const [foods, setFoods] = useState([]);

  const [pagination, setPatination] = useState<PaginationProps>({
    sizeCanChange: true,
    showTotal: true,
    pageSize: 10,
    current: 1,
    pageSizeChangeResetCurrent: true
  });
  const [loading, setLoading] = useState(true);
  const [formParams, setFormParams] = useState({});

  useEffect(() => {
    fetchData();
  }, [pagination.current, pagination.pageSize, JSON.stringify(formParams)]);

  function fetchData() {
    const { current, pageSize } = pagination;
    setLoading(true);
    listFood({ ...formParams, page: current - 1, size: pageSize }).then((res) => {
      setFoods(res.data.records);
      setPatination({
        ...pagination,
        current,
        pageSize,
        total: res.data.total
      });
      setLoading(false);
    });
  }

  function onChangeTable({ current, pageSize }) {
    setPatination({
      ...pagination,
      current,
      pageSize
    });
  }

  function handleSearch(params) {
    setPatination({ ...pagination, current: 1 });
    setFormParams(params);
    fetchData();
  }

  return (
    <Card style={{ height: 'calc(100vh - 196px)', overflowY: 'auto' }}>
      <Title heading={6}>菜品列表</Title>
      <SearchForm onSearch={handleSearch} />
      <Table
        rowKey="id"
        loading={loading}
        onChange={onChangeTable}
        pagination={pagination}
        columns={getColumns(tableCallback)}
        data={foods}
      />
    </Card>
  );
}

export default SelectFood;
