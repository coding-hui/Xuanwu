import React, { useState } from 'react';
import { Card } from '@arco-design/web-react';
import cs from 'classnames';
import { IconPlus } from '@arco-design/web-react/icon';
import styles from './style/index.module.less';
import AddForm from '@/pages/order/table/add-form';
import { OrderTable } from '@/api/food/order-table';

interface AddCardProps {
  description?: string;
  onOk?: (orderTable: OrderTable) => void;
}

function AddCard(props: AddCardProps) {
  const { onOk } = props;
  const [visible, setVisible] = useState(false);

  return (
    <>
      <AddForm
        visible={visible}
        onOk={(v) => {
          setVisible(false);
          onOk(v);
        }}
        onCancel={() => setVisible(false)} />
      <Card
        className={cs(styles['card-block'], styles['add-card'])}
        title={null}
        bordered={true}
        size="small"
        onClick={() => {
          setVisible(true);
        }}
      >
        <div className={styles.content}>
          <div className={styles['add-icon']}>
            <IconPlus />
          </div>
          <div className={styles.description}>{props.description}</div>
        </div>
      </Card>
    </>
  );
}

export default AddCard;
