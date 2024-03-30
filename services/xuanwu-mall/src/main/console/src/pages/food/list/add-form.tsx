import React from 'react';
import { Modal, Button, Form, Input, Spin, Message } from '@arco-design/web-react';
import { IconPlus } from '@arco-design/web-react/icon';
import { createFoodCat, CreateFoodCatRequest, getFoodCat, updateFoodCat, UpdateFoodCatRequest } from '@/api/food/food-cat';

const { useForm } = Form;

type FormType = UpdateFoodCatRequest | CreateFoodCatRequest

type Props = {
  foodCatId?: number | undefined
  onOk?: (values: FormType) => void
}

function AddFoodCat(props: Props) {
  const { foodCatId, onOk } = props;
  const [visible, setVisible] = React.useState(false);
  const [loadDataLoading, setLoadDataLoading] = React.useState(false);
  const [submitLoading, setSubmitLoading] = React.useState(false);

  const isCreated = foodCatId === undefined;
  const modalTitle = isCreated ? '新增菜品分类' : '编辑菜品分类';
  const [form] = useForm<FormType>();

  const loadFoodCatInfo = (foodCatId: number) => {
    setVisible(true);
    setLoadDataLoading(true);
    getFoodCat(foodCatId)
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
    if (isCreated) {
      await createFoodCat(values as CreateFoodCatRequest);
    } else {
      await updateFoodCat(foodCatId, values as UpdateFoodCatRequest);
    }
    setTimeout(() => {
      if (onOk) {
        onOk(values as FormType);
      }
      setVisible(false);
      setSubmitLoading(false);
      Message.success('操作成功');
    }, 500);
  };

  return (
    <>
      {
        isCreated ?
          <Button onClick={() => setVisible(true)} type="primary" icon={<IconPlus />}>
            新增
          </Button> :
          <Button onClick={() => loadFoodCatInfo(foodCatId)} size="small" type="primary">
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
            <Form.Item disabled hidden={isCreated} label="分类编号" field="id">
              <Input placeholder="请输入分类编号" allowClear />
            </Form.Item>
            <Form.Item label="分类名称" field="name" required>
              <Input placeholder="请输入分类名称" allowClear />
            </Form.Item>
            <Form.Item label="分类描述" field="description">
              <Input.TextArea placeholder="请输入分类描述" allowClear />
            </Form.Item>
          </Form>
        </Spin>
      </Modal>
    </>
  );
}

export default AddFoodCat;