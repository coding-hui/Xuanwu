import { useState, useRef, useCallback } from 'react';
import { Select, Spin, Avatar } from '@arco-design/web-react';
import debounce from 'lodash/debounce';

export default function useFoodCreateHook() {
  const [fetchFoodCatLoading, setFetchFoodCatLoading] = useState(false);

  const states = {
    fetchFoodCatLoading
  };

  const actions = {};

  return {
    states,
    actions
  } as const;
}