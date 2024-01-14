import React from 'react';
import { Modal, Button, Form, Input, Spin, Message, Radio, InputNumber } from '@arco-design/web-react';
import { IconPlus } from '@arco-design/web-react/icon';
import {
  createPrinter,
  CreatePrinterRequest,
  getPrinter,
  updatePrinter,
  UpdatePrinterRequest
} from '@/api/food/printer';
import { Types } from '@/pages/system/printer/constants';

const RadioGroup = Radio.Group;

const { useForm } = Form;

type FormType = UpdatePrinterRequest | CreatePrinterRequest

type Props = {
  printerId?: number | undefined
  onOk?: (values: FormType) => void
}

function AddForm(props: Props) {
  const { printerId, onOk } = props;
  const [visible, setVisible] = React.useState(false);
  const [loadDataLoading, setLoadDataLoading] = React.useState(false);
  const [submitLoading, setSubmitLoading] = React.useState(false);

  const isCreated = printerId === undefined;
  const modalTitle = isCreated ? '新增打印机' : '编辑打印机';
  const [form] = useForm<FormType>();
  const type = Form.useWatch('type', form);

  const loadFoodCatInfo = (printerId: number) => {
    setVisible(true);
    setLoadDataLoading(true);
    getPrinter(printerId)
      .then(res => {
        form.setFieldsValue(res.data);
      })
      .finally(() => {
        setLoadDataLoading(false);
      });
  };

  const handleSubmit = async () => {
    const values = form.getFieldsValue();
    setSubmitLoading(true);
    try {
      let res;
      if (isCreated) {
        res = await createPrinter(values as CreatePrinterRequest);
      } else {
        res = await updatePrinter(printerId, values as UpdatePrinterRequest);
      }
      if (res.success) {
        Message.success('操作成功');
        setTimeout(() => {
          if (onOk) {
            onOk(values as FormType);
          }
          setVisible(false);
          form.resetFields();
        }, 200);
      }
    } finally {
      setSubmitLoading(false);
    }
  };

  return (
    <>
      {
        isCreated ?
          <Button onClick={() => setVisible(true)} type="primary" icon={<IconPlus />}>
            新增
          </Button> :
          <Button onClick={() => loadFoodCatInfo(printerId)} size="small" type="primary">
            编辑
          </Button>
      }
      <Modal
        title={
          <div style={{ textAlign: 'left' }}>
            {modalTitle}
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
            <Form.Item label="打印机类型" field="type" required>
              <RadioGroup
                type="button"
                name="type"
                defaultValue={0}
              >
                {
                  Types.map((t, i) => {
                    return <Radio key={i} value={i}>{t}</Radio>;
                  })
                }
              </RadioGroup>
            </Form.Item>
            <Form.Item disabled hidden={isCreated} label="打印机编号" field="id">
              <Input allowClear />
            </Form.Item>
            <Form.Item label="打印机名称" field="name" required>
              <Input placeholder="请输入打印机名称" allowClear />
            </Form.Item>
            {
              type == 1 &&
              <>
                <Form.Item label="打印机 IP" field="ip" required>
                  <Input placeholder="请输入打印机 IP" allowClear />
                </Form.Item>
                <Form.Item label="打印机端口" field="port" required>
                  <InputNumber placeholder="请输入打印机端口" />
                </Form.Item>
              </>
            }
            <Form.Item label="打印机描述" field="description">
              <Input.TextArea placeholder="请输入打印机描述" allowClear />
            </Form.Item>
          </Form>
        </Spin>
      </Modal>
    </>
  );
}

export default AddForm;