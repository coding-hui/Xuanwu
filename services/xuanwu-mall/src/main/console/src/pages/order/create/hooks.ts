import { useState, useRef, useCallback, useEffect } from 'react';
import { Select, Spin, Avatar } from '@arco-design/web-react';
import debounce from 'lodash/debounce';
import { listCartItem } from '@/api/food/cart';

export default function useFoodCreateHook() {
  const [fetchFoodCatLoading, setFetchFoodCatLoading] = useState(false);
  const [cartItems, setCartItems] = useState([]);

  const states = {
    fetchFoodCatLoading
  };

  function fetchCartItem(tableCode?: string) {
    setFetchFoodCatLoading(true);
    listCartItem({
      page: 0,
      size: 500,
      tableCode: tableCode
    }).then(res => {
      setCartItems(res.data.records);
    }).finally(() => {
      setFetchFoodCatLoading(false);
    });
  }

  const actions = {};

  return {
    states,
    actions
  } as const;
}