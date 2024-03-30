import React, { useState, useLayoutEffect } from 'react';
import { Select, Spin } from '@arco-design/web-react';
import { listFoodCats } from '@/api/food/food-cat';

type Props = {
    mode?: 'multiple' | 'tags';
    onChange?: (value: any) => void;
}

function FoodCatSelect(props: Props) {
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
      mode={props.mode}
      options={options}
      placeholder="请选择菜品分类"
      filterOption={false}
      onChange={props.onChange}
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
