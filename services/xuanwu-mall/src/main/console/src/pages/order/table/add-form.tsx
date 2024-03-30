import React, { useEffect } from 'react';
import { Modal, Form, Input, Spin, Message } from '@arco-design/web-react';
import {
  updateOrderTable,
  createOrderTable,
  CreateOrderTableRequest,
  UpdateOrderTableRequest, OrderTable
} from '@/api/food/order-table';
import { AVAILABLE } from '@/pages/order/table/constants';

const { useForm } = Form;

type FormType = UpdateOrderTableRequest | CreateOrderTableRequest

type Props = {
  visible: boolean;
  loadDataLoading?: boolean;
  orderTable?: OrderTable;
  onOk?: (values: OrderTable) => void
  onCancel: () => void
}

function AddFoodCat(props: Props) {
  const { orderTable, visible, loadDataLoading = false, onOk, onCancel } = props;
  const [submitLoading, setSubmitLoading] = React.useState(false);

  const isCreated = orderTable === undefined || orderTable.id === null;
  const modalTitle = isCreated ? '新增桌号' : '编辑桌号';
  const [form] = useForm<FormType>();

  useEffect(() => {
    if (orderTable) {
      form.setFieldsValue(orderTable);
    }
  }, [orderTable]);

  const handleSubmit = async () => {
    let res;
    const values = form.getFieldsValue();
    setSubmitLoading(true);
    try {
      if (isCreated) {
        values.status = AVAILABLE;
        res = await createOrderTable(values as CreateOrderTableRequest);
      } else {
        res = await updateOrderTable(orderTable.id, values as UpdateOrderTableRequest);
      }
      if (res.success) {
        Message.success('操作成功');
        setTimeout(() => {
          if (onOk) {
            onOk(values as OrderTable);
          }
          form.resetFields();
        }, 200);
      }
    } finally {
      setSubmitLoading(false);
    }
  };

  return (
    <>
      <Modal
        title={
          <div style={{ textAlign: 'left' }}>
            {modalTitle}
          </div>
        }
        visible={visible}
        confirmLoading={submitLoading}
        onOk={async () => {
          await handleSubmit();
        }}
        onCancel={onCancel}
      >
        <Spin loading={loadDataLoading}>
          <Form
            form={form}
            layout="vertical"
            autoComplete="off"
            labelCol={{
              span: 7
            }}
            wrapperCol={{
              span: 17
            }}
            style={{ width: 480 }}
          >
            <Form.Item disabled hidden={isCreated} label="编号" field="id">
              <Input allowClear />
            </Form.Item>
            <Form.Item label="桌号" field="code" required>
              <Input placeholder="请输入桌号" allowClear />
            </Form.Item>
            <Form.Item label="描述" field="description">
              <Input.TextArea placeholder="请输入描述信息" allowClear />
            </Form.Item>
          </Form>
        </Spin>
      </Modal>
    </>
  );
}

export default AddFoodCat;