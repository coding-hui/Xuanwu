import React, { useState, useEffect, useMemo } from 'react';
import {
  Table,
  Card,
  Space,
  Typography,
  Message,
  PaginationProps, Button
} from '@arco-design/web-react';
import { useHistory } from 'react-router-dom';
import PermissionWrapper from '@/components/PermissionWrapper';
import styles from './style/index.module.less';
import { getColumns } from './constants';

import SearchForm from './search-form';
import AddFoodCat from './add-form';
import { deleteFood, listFood } from '@/api/food/food';
import { IconPlus } from '@arco-design/web-react/icon';

const { Title } = Typography;
export const Status = ['已上线', '未上线'];

function FoodCatList() {

  const history = useHistory();

  const tableCallback = async (record, type) => {
    if (type === 'delete') {
      await deleteFood(record.id);
      Message.success('删除成功');
    }
    if (type === 'edit') {
      history.push(`/food/create`, {
        foodId: record.id
      });
      return;
    }
    fetchData();
  };

  const columns = useMemo(() => getColumns(tableCallback), []);

  const [data, setData] = useState([]);
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
      setData(res.data.records);
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
    <Card>
      <Title heading={6}>菜品管理</Title>
      <SearchForm onSearch={handleSearch} />
      <div className={styles['button-group']}>
        <Space>
          <Button onClick={() => history.push('/food/create')} type="primary" icon={<IconPlus />}>
            新增
          </Button>
        </Space>
      </div>
      <Table
        rowKey="id"
        loading={loading}
        onChange={onChangeTable}
        pagination={pagination}
        columns={columns}
        data={data}
      />
    </Card>
  );
}

export default FoodCatList;
