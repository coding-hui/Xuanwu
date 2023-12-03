import request from '@/utils/request'

// 查询生成表数据
export function listTable(query) {
  return request({
    url: '/api/v1/tables',
    method: 'get',
    params: query
  })
}
// 查询db数据库列表
export function listDbTable(query) {
  return request({
    url: '/api/v1/tables/from_db',
    method: 'get',
    params: query
  })
}

// 查询表详细信息
export function getGenTable(tableId) {
  return request({
    url: '/api/v1/tables/' + tableId,
    method: 'get'
  })
}

// 修改代码生成信息
export function updateGenTable(data, tableId) {
  return request({
    url: `/api/v1/tables/${tableId}`,
    method: 'put',
    data: data
  })
}

// 导入表
export function importTable(data) {
  return request({
    url: '/api/v1/tables/batch_import',
    method: 'get',
    params: data
  })
}

// 预览生成代码
export function previewTable(tableId) {
  return request({
    url: `/api/v1/generator/${tableId}/preview`,
    method: 'get'
  })
}

// 删除表数据
export function delTable(tableIds) {
  return request({
    url: '/api/v1/tables/batch_delete',
    method: 'delete',
    params: tableIds
  })
}

// 生成代码（自定义路径）
export function genCodeToFile(tableId) {
  return request({
    url: `/api/v1/generator/${tableId}/file`,
    method: 'get'
  })
}

// 同步数据库
export function synchDb(tableId) {
  return request({
    url: `/api/v1/tables/${tableId}/sync_db`,
    method: 'get',
  })
}

// 获取 templates
export function getTemplates() {
  return request({
    url: '/api/v1/templates',
    method: 'get'
  })
}