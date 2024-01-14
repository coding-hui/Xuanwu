import React, { useState, useLayoutEffect } from 'react';
import { Select, Spin } from '@arco-design/web-react';
import { listFoodCats } from '@/api/food/food-cat';

function FoodCatSelect() {
  const [options, setOptions] = useState([]);
  const [fetching, setFetching] = useState(false);

  function fetchFoodCats() {
    setFetching(true);
    listFoodCats().then((res) => {
      const options = res.data.records.map((item) => ({
        label: item.name,
        value: item.id
      }));
      setOptions(options);
    }).finally(() => {
      setFetching(false);
    });
  }

  useLayoutEffect(() => {
    fetchFoodCats();
  }, []);

  return (
    <Select
      allowClear
      options={options}
      placeholder="请选择菜品分类"
      filterOption={false}
      notFoundContent={
        fetching ? (
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center'
            }}
          >
            <Spin style={{ margin: 12 }} />
          </div>
        ) : null
      }
    />
  );
}

export default FoodCatSelect;
