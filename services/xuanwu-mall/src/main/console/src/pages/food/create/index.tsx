import React, { useState, useRef, useEffect } from 'react';
import {
  Typography,
  Card,
  Form,
  Select,
  Input,
  Grid,
  Space,
  Button,
  InputNumber,
  Message
} from '@arco-design/web-react';
import { FormInstance } from '@arco-design/web-react/es/Form';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import { useHistory, useLocation } from 'react-router-dom';
import styles from './style/index.module.less';
import FoodSkus from '@/pages/food/create/food-skus';
import { listFoodCats } from '@/api/food/food-cat';
import { createFood, CreateFoodRequest, getFood, Sku, updateFood, UpdateFoodRequest } from '@/api/food/food';

function GroupForm() {
  const t = useLocale(locale);
  const history = useHistory();
  const location = useLocation<{ foodId: number }>();
  const formRef = useRef<FormInstance>();
  const [loading, setLoading] = useState(false);
  const [foodInfo, setFoodInfo] = useState<{ skus: Sku[] }>({ skus: [] });
  const [foodCatOptions, setFoodCatOptions] = useState([]);
  const [fetching, setFetching] = useState(false);

  function fetchFoodCats() {
    setFetching(true);
    listFoodCats().then((res) => {
      const options = res.data.records.map((item) => ({
        label: item.name,
        value: item.id
      }));
      (() => {
        setFoodCatOptions(options);
      })();
    }).finally(() => {
      setFetching(false);
    });
  }

  function fetchFoodInfo(foodId: number) {
    setFetching(true);
    getFood(foodId).then((res) => {
      setFoodInfo(res.data);
      formRef.current.setFieldsValue(res.data);
      if (res.data.category) {
        formRef.current.setFieldValue('categoryId', res.data.category.id);
      }
    }).finally(() => {
      setFetching(false);
    });
  }

  async function submit(data) {
    setLoading(true);
    try {
      const foodId = location.state && location.state.foodId;
      if (foodId) {
        await updateFood(foodId, data as UpdateFoodRequest);
      } else {
        await createFood(data as CreateFoodRequest);
      }
      Message.success('操作成功');
      history.push('/food/list');
    } catch (err) {
    } finally {
      setLoading(false);
    }
  }

  function handleSkusChange(skus) {
    formRef.current.setFieldValue('skus', skus);
  }

  function handleSubmit() {
    formRef.current.validate().then(async (values) => {
      await submit(values);
    }).catch(() => {
      Message.warning('请填写必要信息');
    });
  }

  function handleReset() {
    formRef.current.setFieldsValue(foodInfo);
  }

  useEffect(() => {
    if (location.state && location.state.foodId) {
      fetchFoodInfo(location.state.foodId);
    }
    fetchFoodCats();
  }, []);

  return (
    <div className={styles.container}>
      <Form layout="vertical" ref={formRef} className={styles['form-group']}>
        <Card loading={fetching}>
          <Typography.Title heading={6}>
            菜品信息
          </Typography.Title>
          <Grid.Row gutter={80}>
            <Grid.Col span={8}>
              <Form.Item
                required
                label="菜品分类"
                field="categoryId"
              >
                <Select
                  allowClear
                  options={foodCatOptions}
                  placeholder="请选择菜品分类"
                />
              </Form.Item>
            </Grid.Col>
            <Grid.Col span={8}>
              <Form.Item
                required
                label="菜品名称"
                field="name"
                tooltip="菜品名称，最多不超过30个字符"
              >
                <Input
                  allowClear
                  placeholder="请输入菜品名称"
                />
              </Form.Item>
            </Grid.Col>
            <Grid.Col span={8}>
              <Form.Item
                label="菜品状态"
                field="soldOut"
                initialValue={1}
              >
                <Select placeholder="请选择菜品上下架状态" allowClear>
                  <Select.Option value={1}>上架</Select.Option>
                  <Select.Option value={0}>下架</Select.Option>
                </Select>
              </Form.Item>
            </Grid.Col>
          </Grid.Row>
          <Grid.Row gutter={80}>
            <Grid.Col span={8}>
              <Form.Item
                required
                label="菜品价格"
                field="price"
                tooltip="菜品价格，不能为负数"
              >
                <InputNumber
                  min={0}
                  max={1000000000}
                  prefix="¥"
                  placeholder="请输入菜品价格"
                />
              </Form.Item>
            </Grid.Col>
            <Grid.Col span={8}>
              <Form.Item
                required
                label="单位"
                field="unit"
                initialValue="份"
              >
                <Input
                  allowClear
                  placeholder="请输入菜品单位"
                />
              </Form.Item>
            </Grid.Col>
            <Grid.Col span={8}>
              <Form.Item
                label="排序序号"
                field="sort"
                tooltip="当前分类下的排序序号，越小越靠前"
                initialValue={10}
              >
                <InputNumber
                  min={0}
                  max={1000000000}
                  step={1}
                  placeholder="请输入排序序号"
                />
              </Form.Item>
            </Grid.Col>
          </Grid.Row>
          <Grid.Row gutter={80}>
            <Grid.Col span={24}>
              <Form.Item
                label="菜品描述"
                field="description"
                tooltip="菜品描述，最多不超过255个字符"
              >
                <Input.TextArea
                  placeholder="请输入菜品描述"
                  allowClear
                />
              </Form.Item>
            </Grid.Col>
          </Grid.Row>
        </Card>
        <Card style={{ marginBottom: '40px' }}>
          <Typography.Title heading={6}>
            菜品属性
          </Typography.Title>
          <Form.Item field="skus">
            <FoodSkus initSkus={foodInfo?.skus} onSave={(skus) => handleSkusChange(skus)} />
          </Form.Item>
        </Card>
      </Form>
      <div className={styles.actions}>
        <Space>
          <Button onClick={handleReset} size="large">
            {t['groupForm.reset']}
          </Button>
          <Button
            type="primary"
            onClick={handleSubmit}
            loading={loading}
            size="large"
          >
            {t['groupForm.submit']}
          </Button>
        </Space>
      </div>
    </div>
  );
}

export default GroupForm;
