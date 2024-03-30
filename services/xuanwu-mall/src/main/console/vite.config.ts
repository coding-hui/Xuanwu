import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import svgrPlugin from '@arco-plugins/vite-plugin-svgr';
import vitePluginForArco from '@arco-plugins/vite-react';
import setting from './src/settings.json';

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: [{ find: '@', replacement: '/src' }],
  },
  plugins: [
    react(),
    svgrPlugin({
      svgrOptions: {},
    }),
    vitePluginForArco({
      theme: '@arco-themes/react-arco-pro',
      modifyVars: {
        'arcoblue-6': setting.themeColor,
      },
    }),
  ],
  css: {
    preprocessorOptions: {
      less: {
        javascriptEnabled: true,
      },
    },
  },
  server: {
    proxy: {
      '/api/v1': {
        target: 'http://localhost:9090',
        changeOrigin: true,
        ws: false,
        // rewrite: (path) => path.replace(new RegExp(`^/basic-api`), ''),
        // only https
        // secure: false
      },
      '/upload': {
        target: 'http://localhost:3300/upload',
        changeOrigin: true,
        ws: true,
        rewrite: (path) => path.replace(new RegExp(`^/upload`), ''),
      },
    },
  },
});
