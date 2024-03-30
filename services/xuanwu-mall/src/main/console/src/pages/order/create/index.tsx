import React, { useState, useRef, useEffect, useCallback, useMemo } from 'react';
import {
  Typography,
  Card,
  Grid,
  Space,
  Button,
  Empty,
  Message,
  Table, Spin
} from '@arco-design/web-react';
import { FormInstance } from '@arco-design/web-react/es/Form';
import { useHistory, useLocation } from 'react-router-dom';
import styles from './style/index.module.less';
import { listFoodCats } from '@/api/food/food-cat';
import { createFood, CreateFoodRequest, FoodInfo, getFood, Sku, updateFood, UpdateFoodRequest } from '@/api/food/food';
import SelectFood from '@/pages/order/create/select-food';
import { IconDelete } from '@arco-design/web-react/icon';
import { addCartItem, CartItem, deleteCartItem, listCartItem, updateQuantity } from '@/api/food/cart';
import { ORDERING } from '@/pages/order/table/constants';
import { createOrder } from '@/api/food/order';

const Row = Grid.Row;
const Col = Grid.Col;

const { Title } = Typography;

function GroupForm() {
  const history = useHistory();
  const location = useLocation<{ orderId: null, foodId: number, orderTableCode: string, orderTableStatus: number }>();
  const formRef = useRef<FormInstance>();
  const [loading, setLoading] = useState(false);
  const [submitLoading, setSubmitLoading] = useState(false);
  const [foodInfo, setFoodInfo] = useState<{ skus: Sku[] }>({ skus: [] });
  const [foodCatOptions, setFoodCatOptions] = useState([]);
  const [fetching, setFetching] = useState(false);
  const [cartItems, setCartItems] = useState<CartItem[]>([]);

  const orderTableCode = useMemo(() => {
    if (location.state && location.state.orderTableCode) {
      return location.state.orderTableCode;
    }
    return '请选择桌号';
  }, [location]);

  const orderTableStatus = useMemo(() => {
    if (location.state && location.state.orderTableStatus) {
      return location.state.orderTableStatus;
    }
    return ORDERING;
  }, [location]);

  const orderId = useMemo(() => {
    if (location.state && location.state.orderId) {
      return location.state.orderId;
    }
    return null;
  }, [location]);

  const totalPrice = useMemo(() => {
    let total = 0;
    if (!cartItems) {
      return '0';
    }
    cartItems.forEach(item => {
      total += item.foodQuantity * parseFloat(item.foodPrice);
    });
    return total.toFixed(2);
  }, [cartItems]);

  function fetchCartItem(tableCode: string) {
    setFetching(true);
    listCartItem({
      page: 0,
      size: 500,
      tableCode: tableCode
    }).then(res => {
      setCartItems(res.data);
    }).finally(() => {
      setFetching(false);
    });
  }

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

  function fetchFoods(foodId: number) {
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

  async function handleCreateOrder(toDetail?: boolean) {
    setSubmitLoading(true);
    createOrder({ payType: 0, orderId: orderId, tableCode: orderTableCode }).then(res => {
      Message.success('下单成功');
      setTimeout(() => {
        if (toDetail) {
          history.push('/order/detail', {
            orderId: res.data.order.id,
            printSalesTicket: false
          });
        } else {
          history.push('/order/table');
        }
      }, 500);
    }).finally(() => {
      setSubmitLoading(false);
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

  const handleSelectFood = useCallback(
    async (foodInfo: FoodInfo) => {
      let exist = false;
      let foodQuantity = 1;
      const newCartItems = cartItems.map(item => {
        if (item.foodId === foodInfo.id) {
          item.foodQuantity = item.foodQuantity + 1;
          foodQuantity = item.foodQuantity;
          exist = true;
        }
        return item;
      });
      const cartItem = {
        foodId: foodInfo.id,
        foodPrice: foodInfo.price,
        foodQuantity: foodQuantity,
        tableCode: orderTableCode,
        foodName: foodInfo.name
      };
      if (!exist) {
        newCartItems.push(cartItem);
        setCartItems(newCartItems);
        await addCartItem(cartItem);
      } else {
        setCartItems(newCartItems);
        await updateQuantity(orderTableCode, foodInfo.id, foodQuantity);
      }
    },
    [cartItems]
  );

  async function handleRemoveFood(foodId: number) {
    let foodQuantity = -1;
    const newCartItems = cartItems.map((item) => {
      if (item.foodId === foodId) {
        item.foodQuantity = item.foodQuantity - 1;
        foodQuantity = item.foodQuantity;
      }
      return item;
    }).filter(item => item.foodQuantity > 0);
    setCartItems(newCartItems);
    if (foodQuantity == 0) {
      await deleteCartItem(orderTableCode, foodId);
    } else {
      await updateQuantity(orderTableCode, foodId, foodQuantity);
    }
  }

  useEffect(() => {
    if (location.state && location.state.foodId) {
      fetchFoods(location.state.foodId);
    }
    fetchFoodCats();
  }, []);

  useEffect(() => {
    if (orderTableCode) {
      fetchCartItem(orderTableCode);
    }
  }, [orderTableCode]);

  return (
    <div className={styles.container}>
      <Spin style={{ width: '100%' }} loading={submitLoading}>
        <Row className="grid-demo" gutter={10} style={{ marginBottom: 16 }}>
          <Col flex={4}>
            <Card style={{ height: 'calc(100vh - 196px)', overflowY: 'auto' }}>
              <Spin style={{ width: '100%' }} loading={loading || fetching}>
                <Title heading={6}>【{orderTableCode}】已选菜品</Title>
                <Table
                  rowKey="foodId"
                  noDataElement={<Empty description="选择右侧菜品加入订单" />}
                  showHeader={false}
                  pagination={false}
                  columns={[
                    {
                      title: '菜品',
                      render: (_col, record) => {
                        return (
                          <div style={{ display: 'flex', flexDirection: 'column' }}>
                            <Typography.Text bold>{record.foodName}</Typography.Text>
                            <Typography.Text type="secondary">￥{record.foodPrice}</Typography.Text>
                          </div>
                        );
                      }
                    },
                    {
                      title: '数量',
                      width: 70,
                      render: (_col, record) => {
                        return (
                          <span>x{record.foodQuantity}</span>
                        );
                      }
                    },
                    {
                      title: '操作',
                      dataIndex: 'operations',
                      width: 50,
                      render: (_, record) => (
                        <Button
                          size="small"
                          shape="circle"
                          icon={<IconDelete />}
                          onClick={() => handleRemoveFood(record.foodId)}
                        />
                      )
                    }
                  ]}
                  data={cartItems}
                />
              </Spin>
            </Card>
          </Col>
          <Col flex={9}>
            <SelectFood onSelect={handleSelectFood} onRemove={handleRemoveFood} />
          </Col>
        </Row>
        <div className={styles.actions}>
          <Space>
            <Typography.Text style={{ fontWeight: 600, marginRight: '24px' }}>
              共计：￥{totalPrice}
            </Typography.Text>
            <Button
              onClick={() => {
                history.push('/order/table');
              }}
              loading={submitLoading}
              status="danger"
              size="large">
              返回
            </Button>
            {/*<Button onClick={handleReset} loading={submitLoading} size="large">*/}
            {/*  备注*/}
            {/*</Button>*/}
            {/*<Button onClick={() => handleCreateOrder(false)} loading={submitLoading} type="outline" size="large">*/}
            {/*  下单*/}
            {/*</Button>*/}
            <Button
              type="primary"
              onClick={() => handleCreateOrder(true)}
              loading={submitLoading}
              size="large"
            >
              下单
            </Button>
          </Space>
        </div>
      </Spin>
    </div>
  );
}

export default GroupForm;
