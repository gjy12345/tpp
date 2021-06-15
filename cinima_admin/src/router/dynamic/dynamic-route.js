import { getPermissions } from '@/api/user'
import Layout from '@/layout'

const map = {
  login: require('@/views/redirect/index').default, // 同步的方式,
  icons: require('@/views/icons/index').default,
  create_article: require('@/views/charts/mix-chart').default,
  cancel_reason: require('@/views/order/cancel-reason').default,
  cinema_doc: require('@/views/doc/cinema/cenima-doc').default,
  film_type: require('@/views/doc/film-type/film-type').default,
  film_doc: require('@/views/doc/film/film-doc').default,
  site_doc: require('@/views/doc/hall/hall-doc').default,
  film_schedule: require('@/views/schedule/film-schedule').default,
  user_manage: require('@/views/user/user-manage').default,
  order_list: require('@/views/order/order_list').default,
  order_comment: require('@/views/order/order_comment').default
}
async function loadPermission() {
  let data
  await getPermissions().then(res => {
    data = res.data
  }).catch(() => {
    data = []
  })
  return data
}
export async function getAsyncRoutes() {
  const data = await loadPermission()
  const permissions = []
  for (let i = 0; i < data.length; i++) {
    const item = data[i]
    if (item.children === undefined || item.children === null) {
      permissions.push({
        path: item.path,
        component: Layout,
        children: [
          {
            path: 'index',
            component: map[item.component],
            name: item.name,
            meta: { title: item.title, icon: item.icon, noCache: false }
          }
        ]
      })
    } else {
      const children = []
      for (const child of item.children) {
        children.push({
          path: child.path,
          component: map[child.component],
          name: child.name,
          meta: { title: child.title }
        })
      }
      permissions.push({
        path: item.path,
        component: Layout,
        alwaysShow: true, // will always show the root menu
        name: item.name,
        meta: {
          title: item.title,
          icon: item.icon,
          noCache: false
        },
        children: children
      })
    }
  }
  return permissions
}
