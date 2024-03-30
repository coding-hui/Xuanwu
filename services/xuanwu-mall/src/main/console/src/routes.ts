import auth, { AuthParams } from '@/utils/authentication';
import { useEffect, useMemo, useState } from 'react';

export type IRoute = AuthParams & {
  name: string;
  key: string;
  // 当前页是否展示面包屑
  breadcrumb?: boolean;
  children?: IRoute[];
  // 当前路由是否渲染菜单项，为 true 的话不会在菜单中显示，但可通过路由地址访问。
  ignore?: boolean;
};

export const routes: IRoute[] = [
  // {
  //   name: 'menu.dashboard',
  //   key: 'dashboard',
  //   children: [
  //     {
  //       name: 'menu.dashboard.workplace',
  //       key: 'dashboard/workplace',
  //     },
  //     // {
  //     //   name: 'menu.dashboard.monitor',
  //     //   key: 'dashboard/monitor',
  //     //   requiredPermissions: [
  //     //     { resource: 'menu.dashboard.monitor', actions: ['write'] },
  //     //   ],
  //     // },
  //   ],
  // },
  // {
  //   name: 'menu.visualization',
  //   key: 'visualization',
  //   children: [
  //     {
  //       name: 'menu.visualization.dataAnalysis',
  //       key: 'visualization/data-analysis',
  //       requiredPermissions: [
  //         { resource: 'menu.visualization.dataAnalysis', actions: ['read'] },
  //       ],
  //     },
  //     {
  //       name: 'menu.visualization.multiDimensionDataAnalysis',
  //       key: 'visualization/multi-dimension-data-analysis',
  //       requiredPermissions: [
  //         {
  //           resource: 'menu.visualization.dataAnalysis',
  //           actions: ['read', 'write'],
  //         },
  //         {
  //           resource: 'menu.visualization.multiDimensionDataAnalysis',
  //           actions: ['write'],
  //         },
  //       ],
  //       oneOfPerm: true,
  //     },
  //   ],
  // },
  {
    name: 'menu.food',
    key: 'food-manager',
    children: [
      {
        name: 'menu.food.cat',
        key: 'food-cat/list',
      },
      {
        name: 'menu.food.list',
        key: 'food/list',
      },
      {
        name: 'menu.food.create',
        key: 'food/create',
      },
    ],
  },
  {
    name: 'menu.order',
    key: 'order-manager',
    children: [
      {
        name: 'menu.order.create.table',
        key: 'order/table',
      },
      {
        name: 'menu.order.create',
        key: 'order/create',
        ignore: true,
      },
      {
        name: 'menu.order.list',
        key: 'order/list',
      },
      {
        name: 'menu.order.detail',
        key: 'order/detail',
        ignore: true
      },
    ],
  },
  {
    name: 'menu.system',
    key: 'system-settings',
    children: [
      {
        name: 'menu.system.printer',
        key: 'system/printer',
      },
    ],
  },
  {
    name: 'menu.user',
    key: 'user',
    ignore: true,
    children: [
      {
        name: 'menu.user.info',
        key: 'user/info',
        ignore: true
      },
      {
        name: 'menu.user.setting',
        key: 'user/setting',
        ignore: true
      },
    ],
  },
];

export const getName = (path: string, routes) => {
  return routes.find((item) => {
    const itemPath = `/${item.key}`;
    if (path === itemPath) {
      return item.name;
    } else if (item.children) {
      return getName(path, item.children);
    }
  });
};

export const generatePermission = (role: string) => {
  const actions = role === 'admin' ? ['*'] : ['read'];
  const result = {};
  routes.forEach((item) => {
    if (item.children) {
      item.children.forEach((child) => {
        result[child.name] = actions;
      });
    }
  });
  return result;
};

const useRoute = (userPermission): [IRoute[], string] => {
  const filterRoute = (routes: IRoute[], arr = []): IRoute[] => {
    if (!routes.length) {
      return [];
    }
    for (const route of routes) {
      const { requiredPermissions, oneOfPerm } = route;
      let visible = true;
      if (requiredPermissions) {
        visible = auth({ requiredPermissions, oneOfPerm }, userPermission);
      }

      if (!visible) {
        continue;
      }
      if (route.children && route.children.length) {
        const newRoute = { ...route, children: [] };
        filterRoute(route.children, newRoute.children);
        if (newRoute.children.length) {
          arr.push(newRoute);
        }
      } else {
        arr.push({ ...route });
      }
    }

    return arr;
  };

  const [permissionRoute, setPermissionRoute] = useState(routes);

  useEffect(() => {
    const newRoutes = filterRoute(routes);
    setPermissionRoute(newRoutes);
  }, [JSON.stringify(userPermission)]);

  const defaultRoute = useMemo(() => {
    const first = permissionRoute[0];
    if (first) {
      const firstRoute = first?.children?.[0]?.key || first.key;
      return firstRoute;
    }
    return '';
  }, [permissionRoute]);

  return [permissionRoute, defaultRoute];
};

export default useRoute;
