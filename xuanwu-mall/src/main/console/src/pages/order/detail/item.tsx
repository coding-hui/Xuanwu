import React, { CSSProperties } from 'react';
import { Descriptions, Card, Skeleton } from '@arco-design/web-react';
import { Order } from '@/api/food/order';
import { OrderTypeTexts, PayTypeTexts, SourceTypeTexts } from '@/pages/order/list/constants';

interface ProfileItemProps {
  order: Order;
  style?: CSSProperties;
  loading?: boolean;
}

function BasicInfo(props: ProfileItemProps) {
  const { order, loading } = props;

  const blockDataList: {
    title: string;
    data: {
      label: string;
      value: string;
    }[];
  }[] = [];

  blockDataList.push({
    title: '基本信息',
    data: [
      {
        label: '订单编号',
        value: order?.orderSn || '-'
      },
      {
        label: '订单桌号',
        value: order?.tableCode || '-'
      },
      {
        label: '支付方式',
        value: `${order.payType > PayTypeTexts.length ? '-' : PayTypeTexts[order?.payType]}`
      },
      {
        label: '订单来源',
        value: `${order.payType > SourceTypeTexts.length ? '-' : SourceTypeTexts[order?.payType]}`
      },
      {
        label: '订单类型',
        value: `${order.payType > OrderTypeTexts.length ? '-' : OrderTypeTexts[order?.payType]}`
      }
    ]
  });

  return (
    <Card>
      <div>
        {blockDataList.map(({ title: blockTitle, data: blockData }, index) => (
          <Descriptions
            key={`${index}`}
            border
            layout="vertical"
            column={5}
            title={blockTitle}
            data={
              loading
                ? blockData.map((item) => ({
                  ...item,
                  value: (
                    <Skeleton
                      text={{ rows: 1, style: { width: '200px' } }}
                      animation
                    />
                  )
                }))
                : blockData
            }
            style={index > 0 ? { marginTop: '20px' } : {}}
          />
        ))}
      </div>
    </Card>
  );
}

export default BasicInfo;
