import React, { useState, useRef, useEffect, useContext, useCallback } from 'react';
import { Button, Table, Input, Select, Form, FormInstance } from '@arco-design/web-react';
import { Sku } from '@/api/food/food';

const FormItem = Form.Item;

const EditableContext = React.createContext<{ getForm?: () => FormInstance }>({});

type Props = {
  initSkus?: Sku[];
  onSave?: (values: Recordable) => void
}

function EditableRow(props) {
  const { children, record, className, ...rest } = props;
  const refForm = useRef(null);
  const getForm = () => refForm.current;

  return (
    <EditableContext.Provider
      value={{
        getForm
      }}
    >
      <Form
        style={{ display: 'table-row' }}
        /* eslint-disable-next-line react/no-children-prop */
        children={children}
        ref={refForm}
        wrapper="tr"
        wrapperProps={rest}
        className={`${className} editable-row`}
      />
    </EditableContext.Provider>
  );
}

function EditableCell(props) {
  const { children, className, rowData, column, onHandleSave } = props;
  const ref = useRef(null);
  const refInput = useRef(null);
  const { getForm } = useContext(EditableContext);
  const [editing, setEditing] = useState(false);
  const handleClick = useCallback(
    (e) => {
      if (
        editing &&
        column.editable &&
        ref.current &&
        !ref.current.contains(e.target) &&
        !e.target.classList.contains('js-demo-select-option')
      ) {
        cellValueChangeHandler(rowData[column.dataIndex]);
      }
    },
    [editing, rowData, column]
  );
  useEffect(() => {
    editing && refInput.current && refInput.current.focus();
  }, [editing]);
  useEffect(() => {
    document.addEventListener('click', handleClick, true);
    return () => {
      document.removeEventListener('click', handleClick, true);
    };
  }, [handleClick]);

  const cellValueChangeHandler = (value) => {
    if (column.dataIndex === 'salary') {
      const values = {
        [column.dataIndex]: value
      };
      onHandleSave && onHandleSave({ ...rowData, ...values });
      setTimeout(() => setEditing(!editing), 300);
    } else {
      const form = getForm();
      form.validate([column.dataIndex], (errors, values) => {
        if (!errors || !errors[column.dataIndex]) {
          setEditing(!editing);
          onHandleSave && onHandleSave({ ...rowData, ...values });
        }
      });
    }
  };

  if (editing) {
    return (
      <div ref={ref}>
        {column.dataIndex === 'salary' ? (
          <Select
            onChange={cellValueChangeHandler}
            defaultValue={rowData[column.dataIndex]}
            options={[2000, 5000, 10000, 20000]}
          />
        ) : (
          <FormItem
            style={{ marginBottom: 0 }}
            labelCol={{ span: 0 }}
            wrapperCol={{ span: 24 }}
            initialValue={rowData[column.dataIndex]}
            field={column.dataIndex}
            rules={[{ required: true }]}
          >
            <Input ref={refInput} onPressEnter={cellValueChangeHandler} />
          </FormItem>
        )}
      </div>
    );
  }

  return (
    <div
      className={column.editable ? `editable-cell ${className}` : className}
      onClick={() => column.editable && setEditing(!editing)}
    >
      {children}
    </div>
  );
}

function FoodSkus(props: Props) {
  const { initSkus, onSave } = props;
  const [count, setCount] = useState(5);
  const [skus, setSkus] = useState([]);

  useEffect(() => {
    if (initSkus) {
      setSkus(initSkus);
    }
  }, [initSkus]);

  const columns = [
    {
      title: '唯一标识',
      dataIndex: 'skuCode'
    },
    {
      title: '规格',
      dataIndex: 'spec',
      editable: true,
      placeholder: '请输入规格，如：大份/小份'
    },
    {
      title: '价格',
      dataIndex: 'price',
      editable: true,
      placeholder: '请输入价格'
    },
    {
      title: '库存',
      dataIndex: 'stock',
      editable: true,
      placeholder: '请输入库存'
    },
    {
      title: '排序',
      dataIndex: 'sort',
      editable: true,
      placeholder: '请输入显示排序，越小越靠前'
    },
    {
      title: '操作',
      width: 120,
      dataIndex: 'op',
      render: (_, record) => (
        <Button onClick={() => removeRow(record.key)} type="primary" status="danger">
          删除
        </Button>
      )
    }
  ];

  function handleSave(row) {
    const newSkus = [...skus];
    const index = newSkus.findIndex((item) => row.skuCode === item.skuCode);
    newSkus.splice(index, 1, { ...newSkus[index], ...row });
    setSkus(newSkus);
    if (onSave) {
      onSave(newSkus);
    }
  }

  function removeRow(key) {
    setSkus(skus.filter((item) => item.skuCode !== key));
  }

  function addRow() {
    setCount(count + 1);
    setSkus(
      skus.concat({
        key: `${count + 1}`,
        skuCode: `sku-${skus.length + 1}`,
        spec: '',
        price: '',
        stock: '',
        sort: skus.length + 1
      })
    );
  }

  return (
    <>
      <Button
        style={{ marginBottom: 10 }}
        type="primary"
        onClick={addRow}
      >
        新增
      </Button>
      <Table
        data={skus}
        rowKey="skuCode"
        components={{
          body: {
            row: EditableRow,
            cell: EditableCell
          }
        }}
        columns={columns.map((column) =>
          column.editable
            ? {
              ...column,
              onCell: () => ({
                onHandleSave: handleSave
              })
            }
            : column
        )}
      />
    </>
  );
}

export default FoodSkus;