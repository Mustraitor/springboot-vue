import { praseStrEmpty } from '@/utils/commonUtil'
import request from '@/utils/request'

export function listRole(query) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params: query
  })
}

export function getRole(roleId) {
  return request({
    url: '/system/role/' + praseStrEmpty(roleId),
    method: 'get'
  })
}

export function addRole(data) {
  return request({
    url: '/system/role',
    method: 'post',
    data: data
  })
}

export function updateRole(data) {
  return request({
    url: '/system/role',
    method: 'put',
    data: data
  })
}

export function delRole(roleId) {
  return request({
    url: '/system/role/' + roleId,
    method: 'delete'
  })
}
