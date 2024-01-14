import React, { useState, useEffect, useMemo } from 'react';
import {
  Table,
  Card,
  Space,
  Typography,
  Message,
  PaginationProps
} from '@arco-design/web-react';
import styles from './style/index.module.less';
import { getColumns } from './constants';

import SearchForm from './search-form';
import AddForm from './add-form';
import { deletePrinter, listPrinters, printTestPage, updatePrinter } from '@/api/food/printer';

const { Title } = Typography;

function PrinterList() {
  const tableCallback = async (record, type, status?: boolean) => {
    if (type === 'delete') {
      await deletePrinter(record.id);
      Message.success('删除成功');
    }
    if (type === 'print-test') {
      await printTestPage(record.id);
      Message.success('操作成功');
    }
    if (type === 'change-status') {
      record.status = status ? 1 : 0;
      await updatePrinter(record.id, record);
      Message.success('操作成功');
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
    listPrinters({ ...formParams, page: current - 1, size: pageSize }).then((res) => {
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
      <Title heading={6}>打印机列表</Title>
      <SearchForm onSearch={handleSearch} />
      <div className={styles['button-group']}>
        <Space>
          <AddForm onOk={() => {
            fetchData();
          }} />
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

export default PrinterList;
