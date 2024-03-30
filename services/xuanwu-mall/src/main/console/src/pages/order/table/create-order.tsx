import React from 'react';
import { Modal, Button, Form, InputNumber, Input } from '@arco-design/web-react';
import { OrderTable, updateOrderTable, UpdateOrderTableRequest } from '@/api/food/order-table';
import { IN_USE } from '@/pages/order/table/constants';

const { useForm } = Form;

type FormType = UpdateOrderTableRequest

type Props = {
  orderTable: OrderTable;
  preSubmit: (values: FormType) => void;
  onOk: (values: FormType) => void;
}

function CreateOrder(props: Props) {
  const { orderTable, preSubmit, onOk } = props;
  const [visible, setVisible] = React.useState(false);
  const [submitLoading, setSubmitLoading] = React.useState(false);

  const [form] = useForm<FormType>();

  const handleSubmit = async () => {
    const values = form.getFieldsValue();
    setSubmitLoading(true);
    preSubmit(values as FormType);
    values.id = orderTable.id;
    values.code = orderTable.code;
    values.status = IN_USE;
    updateOrderTable(orderTable.id, values as UpdateOrderTableRequest).then(() => {
      onOk(values as FormType);
      setVisible(false);
      form.resetFields();
    }).finally(() => {
      setSubmitLoading(false);
    });
  };

  return (
    <>
      {
        <Button loading={submitLoading} onClick={() => setVisible(true)} size="small">
          下单
        </Button>
      }
      <Modal
        title={
          <div style={{ textAlign: 'left' }}>
            创建订单
          </div>
        }
        visible={visible}
        confirmLoading={submitLoading}
        onCancel={() => {
          setVisible(false);
        }}
        onOk={async () => {
          await handleSubmit();
        }}
      >
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
          <Form.Item required label="就餐人数" field="numberOfDiners">
            <InputNumber placeholder="请输入就餐人数" />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}

export default CreateOrder;
