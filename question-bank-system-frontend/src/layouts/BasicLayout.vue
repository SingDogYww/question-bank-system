<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { Layout, Menu, Dropdown, Avatar, Breadcrumb, Badge, Button } from 'ant-design-vue';
import { 
  MenuUnfoldOutlined, 
  MenuFoldOutlined, 
  UserOutlined, 
  HomeOutlined, 
  FileTextOutlined, 
  BookOutlined, 
  SettingOutlined,
  LogoutOutlined,
  BellOutlined,
  FormOutlined,
  ReadOutlined,
  CheckCircleOutlined,
  LineChartOutlined,
  ExceptionOutlined,
  BarChartOutlined,
  DatabaseOutlined,
  AppstoreOutlined,
  TagOutlined,
  AuditOutlined,
  PlusOutlined,
  ToolOutlined,
  TeamOutlined,
  LockOutlined,
  SettingFilled,
  FileOutlined,
  FolderOutlined,
  ImportOutlined
} from '@ant-design/icons-vue';
import { useRouter, useRoute } from 'vue-router';

// 定义主题颜色，保持与登录/注册页面一致
const themeColors = {
  primary: '#f5615c',
  hover: '#ff7875',
  active: '#e84749',
  light: '#fff1f0',
  submenuBg: '#e05450', // 添加子菜单背景色，稍深一点的红色
  hoverDeep: '#f26b69'  // 添加子菜单悬停色，稍深一点的悬停色
};

const { Header, Sider, Content, Footer } = Layout;
const router = useRouter();
const route = useRoute();

// 状态定义
const collapsed = ref(false);
const selectedKeys = ref<string[]>(['dashboard']); // 默认选中数据概览
const openKeys = ref<string[]>(['home']); // 默认展开首页菜单
const breadcrumbs = ref<{title: string, path?: string}[]>([]);

// 计算当前用户名称
const userName = computed(() => {
  return '管理员'; // 实际项目中应从用户状态或API获取
});

// 定义菜单项的类型
interface MenuItem {
  key: string;
  title: string;
  path: string;
  icon?: any; // 图标组件为可选属性
  children?: MenuItem[];
}

// 菜单定义，采用JSON格式便于后端获取
const menuData = reactive<MenuItem[]>([
  {
    key: 'home',
    title: '首页',
    icon: HomeOutlined,
    path: '/dashboard',
    children: [
      {
        key: 'dashboard',
        title: '数据概览',
        path: '/dashboard',
        icon: BarChartOutlined
      },
      {
        key: 'monitor',
        title: '系统监控',
        path: '/monitor',
        icon: DatabaseOutlined
      }
    ]
  },
  {
    key: 'questions',
    title: '试题管理',
    icon: FileTextOutlined,
    path: '/questions',
    children: [
      {
        key: 'question-list',
        title: '试题列表',
        path: '/questions/list',
        icon: FileOutlined
      },
      {
        key: 'question-category',
        title: '试题分类',
        path: '/questions/category',
        icon: FolderOutlined
      },
      {
        key: 'question-import',
        title: '试题导入',
        path: '/questions/import',
        icon: ImportOutlined
      },
      {
        key: 'question-tag',
        title: '知识点标签',
        path: '/questions/tag',
        icon: TagOutlined
      },
      {
        key: 'question-review',
        title: '试题审核',
        path: '/questions/review',
        icon: AuditOutlined
      }
    ]
  },
  {
    key: 'papers',
    title: '试卷管理',
    icon: BookOutlined,
    path: '/papers',
    children: [
      {
        key: 'paper-list',
        title: '试卷列表',
        path: '/papers/list',
        icon: FileOutlined
      },
      {
        key: 'paper-create',
        title: '创建试卷',
        path: '/papers/create',
        icon: PlusOutlined
      },
      {
        key: 'paper-template',
        title: '试卷模板',
        path: '/papers/template',
        icon: AppstoreOutlined
      },
      {
        key: 'paper-strategy',
        title: '组卷策略',
        path: '/papers/strategy',
        icon: ToolOutlined
      },
      {
        key: 'paper-publish',
        title: '试卷发布',
        path: '/papers/publish',
        icon: AuditOutlined
      }
    ]
  },
  {
    key: 'exams',
    title: '考试管理',
    icon: FormOutlined,
    path: '/exams',
    children: [
      {
        key: 'exam-list',
        title: '考试列表',
        path: '/exams/list',
        icon: FileOutlined
      },
      {
        key: 'exam-schedule',
        title: '考试安排',
        path: '/exams/schedule',
        icon: AppstoreOutlined
      },
      {
        key: 'exam-monitor',
        title: '考试监控',
        path: '/exams/monitor',
        icon: ExceptionOutlined
      },
      {
        key: 'exam-rules',
        title: '考试规则',
        path: '/exams/rules',
        icon: LockOutlined
      }
    ]
  },
  {
    key: 'practice',
    title: '练习与错题',
    icon: ReadOutlined,
    path: '/practice',
    children: [
      {
        key: 'chapter-practice',
        title: '章节练习',
        path: '/practice/chapter',
        icon: BookOutlined
      },
      {
        key: 'smart-practice',
        title: '智能刷题',
        path: '/practice/smart',
        icon: CheckCircleOutlined
      },
      {
        key: 'mock-exam',
        title: '模拟考试',
        path: '/practice/mock',
        icon: FormOutlined
      },
      {
        key: 'mistake-book',
        title: '错题本',
        path: '/practice/mistakes',
        icon: ExceptionOutlined
      }
    ]
  },
  {
    key: 'analysis',
    title: '成绩分析',
    icon: LineChartOutlined,
    path: '/analysis',
    children: [
      {
        key: 'score-report',
        title: '成绩报告',
        path: '/analysis/report',
        icon: FileTextOutlined
      },
      {
        key: 'knowledge-radar',
        title: '知识点分析',
        path: '/analysis/knowledge',
        icon: TagOutlined
      },
      {
        key: 'data-dashboard',
        title: '数据看板',
        path: '/analysis/dashboard',
        icon: BarChartOutlined
      },
      {
        key: 'trend-analysis',
        title: '趋势分析',
        path: '/analysis/trend',
        icon: LineChartOutlined
      }
    ]
  },
  {
    key: 'settings',
    title: '系统设置',
    icon: SettingOutlined,
    path: '/settings',
    children: [
      {
        key: 'user-management',
        title: '用户管理',
        path: '/settings/users',
        icon: TeamOutlined
      },
      {
        key: 'role-management',
        title: '角色权限',
        path: '/settings/roles',
        icon: LockOutlined
      },
      {
        key: 'system-config',
        title: '系统配置',
        path: '/settings/config',
        icon: SettingFilled
      },
      {
        key: 'operation-log',
        title: '操作日志',
        path: '/settings/logs',
        icon: FileOutlined
      }
    ]
  }
]);

// 用户下拉菜单项
const userMenuItems = [
  {
    key: 'profile',
    label: '个人中心',
    icon: UserOutlined
  },
  {
    key: 'settings',
    label: '账号设置',
    icon: SettingOutlined
  },
  {
    type: 'divider'
  },
  {
    key: 'logout',
    label: '退出登录',
    icon: LogoutOutlined
  }
];

// 处理用户菜单点击
const handleUserMenuClick = (info: any) => {
  const key = info.key as string;
  if (key === 'logout') {
    // 退出登录逻辑
    router.push('/login');
  } else if (key === 'profile') {
    router.push('/profile');
  } else if (key === 'settings') {
    router.push('/account/settings');
  }
};

// 处理菜单点击
const handleMenuClick = (info: any) => {
  const key = info.key as string;
  
  // 立即阻止折叠，无论点击哪个菜单项
  collapsed.value = false;
  
  const findMenuPath = (menus: any[], targetKey: string): string | undefined => {
    for (const menu of menus) {
      if (menu.key === targetKey) {
        return menu.path;
      }
      if (menu.children) {
        const path = findMenuPath(menu.children, targetKey);
        if (path) return path;
      }
    }
    return undefined;
  };

  const path = findMenuPath(menuData, key);
  if (path) {
    router.push(path);
  }
  
  updateBreadcrumbs(key);
};

// 更新面包屑
const updateBreadcrumbs = (currentKey: string) => {
  const result: {title: string, path?: string}[] = [];
  
  const findItem = (items: any[], key: string, parents: any[] = []): boolean => {
    for (const item of items) {
      if (item.key === key) {
        // 添加所有父级
        for (const parent of parents) {
          result.push({
            title: parent.title,
            path: parent.path
          });
        }
        // 添加当前项
        result.push({
          title: item.title,
          path: item.path
        });
        return true;
      }
      
      if (item.children && item.children.length > 0) {
        const newParents = [...parents, item];
        if (findItem(item.children, key, newParents)) {
          return true;
        }
      }
    }
    return false;
  };
  
  findItem(menuData, currentKey);
  breadcrumbs.value = result;
};

// 根据当前路由更新菜单状态
const updateMenuFromRoute = () => {
  const path = route.path;
  
  // 确保不会因为路由变化而收起菜单
  collapsed.value = false; // 确保菜单保持展开状态
  
  // 如果是根路径或dashboard路径，确保首页菜单展开，数据概览选中
  if (path === '/' || path === '/dashboard') {
    selectedKeys.value = ['dashboard'];
    openKeys.value = ['home'];
    updateBreadcrumbs('dashboard');
    return;
  }
  
  const findKeyByPath = (items: any[], targetPath: string): {key: string, parents: string[]} | null => {
    for (const item of items) {
      if (item.path === targetPath) {
        return { key: item.key, parents: [] };
      }
      
      if (item.children && item.children.length > 0) {
        const result = findKeyByPath(item.children, targetPath);
        if (result) {
          result.parents.unshift(item.key);
          return result;
        }
      }
    }
    return null;
  };
  
  const result = findKeyByPath(menuData, path);
  if (result) {
    selectedKeys.value = [result.key];
    openKeys.value = result.parents;
    updateBreadcrumbs(result.key);
  } else {
    // 默认首页
    selectedKeys.value = ['dashboard'];
    openKeys.value = ['home'];
    updateBreadcrumbs('dashboard');
  }
};

// 监听路由变化
const watchRoute = () => {
  router.beforeEach((to, from, next) => {
    // 如果访问根路径，重定向到/dashboard
    if (to.path === '/') {
      router.push('/dashboard');
    }
    next();
  });
  
  router.afterEach(() => {
    // 在路由变化完成后，确保菜单保持展开状态
    collapsed.value = false;
    
    // 更新菜单状态
    updateMenuFromRoute();
  });
};

// 监听路由变化
watch(() => route.path, (newPath) => {
  // 如果路径是根路径或dashboard，确保首页菜单展开，数据概览选中
  if (newPath === '/' || newPath === '/dashboard') {
    selectedKeys.value = ['dashboard'];
    openKeys.value = ['home'];
    collapsed.value = false;
  }
});

// 初始化
onMounted(() => {
  // 如果当前是根路径，重定向到/dashboard
  if (route.path === '/') {
    router.push('/dashboard');
  }
  
  updateMenuFromRoute();
  watchRoute(); // 添加路由监听
});
</script>

<template>
  <Layout class="layout">
    <!-- 侧边菜单 -->
    <Sider
      v-model:collapsed="collapsed"
      collapsible
      :trigger="null"
      class="sidebar"
    >
      <div class="logo">
        <span v-if="!collapsed">在线考试系统</span>
        <span v-else>考试</span>
      </div>
      <Menu
        v-model:selectedKeys="selectedKeys"
        v-model:openKeys="openKeys"
        theme="dark"
        mode="inline"
        :inlineIndent="32"
        @click="handleMenuClick"
      >
        <Menu.SubMenu v-for="menu in menuData" :key="menu.key">
          <template #icon>
            <component :is="menu.icon" />
          </template>
          <template #title>{{ menu.title }}</template>
          
          <Menu.Item v-for="subMenu in menu.children" :key="subMenu.key">
            <template #icon v-if="subMenu.icon">
              <component :is="subMenu.icon" />
            </template>
            {{ subMenu.title }}
          </Menu.Item>
        </Menu.SubMenu>
      </Menu>
    </Sider>
    
    <!-- 主体布局 -->
    <Layout class="site-layout">
      <!-- 顶部导航 -->
      <Header class="header">
        <!-- 折叠按钮 -->
        <Button
          type="text"
          class="trigger"
          @click="() => (collapsed = !collapsed)"
        >
          <MenuUnfoldOutlined v-if="collapsed" />
          <MenuFoldOutlined v-else />
        </Button>
        
        <!-- 顶部右侧菜单 -->
        <div class="right-menu">
          <!-- 消息通知 -->
          <Badge count="5" class="notification-badge">
            <Button type="text" class="notification-button">
              <BellOutlined style="font-size: 16px;" />
            </Button>
          </Badge>
          
          <!-- 用户头像和下拉菜单 -->
          <Dropdown :trigger="['click']" placement="bottomRight">
            <div class="avatar-wrapper">
              <Avatar :size="32" class="user-avatar" :style="{ backgroundColor: themeColors.primary }">
                <template #icon><UserOutlined /></template>
              </Avatar>
              <span class="username">{{ userName }}</span>
            </div>
            <template #overlay>
              <Menu @click="handleUserMenuClick">
                <template v-for="(item, index) in userMenuItems" :key="index">
                  <Menu.Item v-if="!item.type" :key="item.key">
                    <component :is="item.icon" /> {{ item.label }}
                  </Menu.Item>
                  <Menu.Divider v-else />
                </template>
              </Menu>
            </template>
          </Dropdown>
        </div>
      </Header>
      
      <!-- 内容区域 -->
      <Content class="content">
        <!-- 面包屑导航 -->
        <Breadcrumb class="breadcrumb">
          <Breadcrumb.Item v-for="(item, index) in breadcrumbs" :key="index">
            <router-link v-if="item.path && index < breadcrumbs.length - 1" :to="item.path">
              {{ item.title }}
            </router-link>
            <span v-else>{{ item.title }}</span>
          </Breadcrumb.Item>
        </Breadcrumb>
        
        <!-- 页面内容 -->
        <div class="inner-content">
          <router-view></router-view>
        </div>
      </Content>
      
      <!-- 页脚 -->
      <Footer class="footer">
        在线考试系统 ©{{ new Date().getFullYear() }} 版权所有
      </Footer>
    </Layout>
  </Layout>
</template>

<style scoped lang="scss">
.layout {
  min-height: 100vh;
}

.sidebar {
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.2);
  background: v-bind('themeColors.primary');
  
  .logo {
    height: 64px;
    padding: 0 24px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    color: white;
    font-size: 18px;
    font-weight: 600;
    white-space: nowrap;
    overflow: hidden;
    background: v-bind('themeColors.primary');
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    letter-spacing: 1px;
    box-shadow: none;
    
    .logo-icon {
      font-size: 24px;
      display: inline-block;
    }
  }
  
  :deep(.ant-menu-dark) {
    background: v-bind('themeColors.primary');
    border-top: none;
    
    // 统一所有菜单项的基础字体样式
    .ant-menu-item, .ant-menu-submenu-title {
      font-size: 14px;
      font-weight: 400;
      line-height: 40px;
      height: 40px;
      color: rgba(255, 255, 255, 0.9);
      margin: 4px 0;
      padding-right: 16px !important;
    }
    
    // 统一图标大小
    .ant-menu-item .anticon, .ant-menu-submenu-title .anticon {
      font-size: 16px;
      vertical-align: middle;
      margin-right: 10px;
    }
    
    .ant-menu-submenu-title {
      font-weight: 500; // 父菜单标题加粗
      
      &:hover {
        background-color: v-bind('themeColors.hover') !important;
      }
    }
    
    // 确保子菜单背景也是红色，但略微深一点，创造层次感
    .ant-menu-sub {
      // 使用预定义的深红色背景色
      background: v-bind('themeColors.submenuBg') !important;
      
      .ant-menu-item {
        font-size: 14px; // 保持与父菜单相同的字体大小
        height: 40px; // 保持与父菜单相同的高度
        line-height: 40px;
        padding-right: 16px !important;
        margin-left: 0 !important;
        margin-right: 0 !important;
        
        &:hover {
          // 使用预定义的深一点的悬停色
          background-color: v-bind('themeColors.hoverDeep') !important;
        }
      }
    }
    
    // 选中状态样式 - 只应用于子菜单项
    .ant-menu-item-selected {
      background-color: rgba(255, 255, 255, 0.15) !important;
      font-weight: 600;
      color: white !important;
      border-left: 3px solid white !important;  // 添加左侧边框指示器
      padding-left: 57px !important;  // 调整左边距以补偿边框
    }
    
    // 移除父级菜单的选中特殊样式
    .ant-menu-submenu-selected {
      // 保持基本样式，不添加特殊选中效果
      > .ant-menu-submenu-title {
        background-color: transparent !important; // 移除背景色变化
        border-left: none !important; // 确保没有左侧边框
        font-weight: 500; // 恢复正常字体粗细
        
        // 移除其他可能的指示器
        &::after {
          content: none;
        }
        
        // 移除可能的左侧白色显示效果
        &::before {
          content: none;
        }
      }
    }
    
    // 优化滚动条样式
    &::-webkit-scrollbar {
      width: 6px;
    }
    
    &::-webkit-scrollbar-thumb {
      background: rgba(255, 255, 255, 0.2);
      border-radius: 3px;
    }
  }
}

// 折叠状态下的弹出菜单样式
:global(.ant-menu-vertical.ant-menu-dark),
:global(.ant-menu-vertical-left.ant-menu-dark),
:global(.ant-menu-vertical-right.ant-menu-dark) {
  background-color: v-bind('themeColors.submenuBg') !important;
  
  .ant-menu-item {
    color: rgba(255, 255, 255, 0.85);
    
    &:hover {
      background-color: v-bind('themeColors.hoverDeep') !important;
    }
    
    &.ant-menu-item-selected {
      background-color: rgba(255, 255, 255, 0.15) !important;
      color: white !important;
    }
  }
}

// 修复菜单弹出层的样式
:global(.ant-menu-submenu-popup) {
  .ant-menu-sub.ant-menu-vertical {
    background-color: v-bind('themeColors.submenuBg') !important;
    
    .ant-menu-item {
      &:hover {
        background-color: v-bind('themeColors.hoverDeep') !important;
      }
      
      &.ant-menu-item-selected {
        background-color: rgba(255, 255, 255, 0.15) !important;
      }
    }
  }
}

.header {
  padding: 0 24px;
  background: v-bind('themeColors.primary');
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  z-index: 1;
  height: 64px;
  
  .trigger {
    font-size: 16px;
    cursor: pointer;
    transition: color 0.3s;
    color: white;
    padding: 8px;
    border-radius: 4px;
    
    &:hover {
      color: white;
      background-color: rgba(255, 255, 255, 0.1);
    }
  }
  
  .right-menu {
    display: flex;
    align-items: center;
    
    .notification-badge {
      margin-right: 24px;
      cursor: pointer;
      
      .notification-button {
        color: white;
        padding: 8px;
        border-radius: 50%;
        transition: all 0.3s ease;
        
        &:hover {
          color: white;
          background-color: rgba(255, 255, 255, 0.1);
        }
      }
    }
    
    .avatar-wrapper {
      display: flex;
      align-items: center;
      cursor: pointer;
      padding: 4px 12px;
      border-radius: 24px;
      transition: all 0.3s ease;
      
      &:hover {
        background-color: rgba(255, 255, 255, 0.1);
      }
      
      .user-avatar {
        margin-right: 8px;
        background-color: white !important;
        color: v-bind('themeColors.primary') !important;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }
      
      .username {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.9);
        font-weight: 400;
      }
    }
  }
}

.content {
  margin: 0 16px;
  padding: 24px;
  
  .breadcrumb {
    margin-bottom: 16px;
    padding: 8px 0;
    
    :deep(.ant-breadcrumb-link) {
      color: rgba(0, 0, 0, 0.65);
      transition: color 0.3s ease;
      
      a {
        color: v-bind('themeColors.primary');
        
        &:hover {
          color: v-bind('themeColors.hover');
        }
      }
    }
    
    :deep(.ant-breadcrumb-separator) {
      color: rgba(0, 0, 0, 0.3);
    }
  }
  
  .inner-content {
    padding: 24px;
    background: #fff;
    border-radius: 8px;
    min-height: 80vh;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  }
}

.footer {
  text-align: center;
  color: rgba(0, 0, 0, 0.45);
  padding: 16px;
  font-size: 12px;
  letter-spacing: 0.5px;
}

// 调整子菜单图标的样式
:deep(.ant-menu-sub .ant-menu-item .anticon) {
  font-size: 14px;
  margin-right: 10px;
  vertical-align: -0.125em;
}

// 添加展开/收起动效
:deep(.ant-menu-inline-collapsed) {
  transition: width 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  background: v-bind('themeColors.primary') !important; // 确保折叠状态下背景色一致
  
  .ant-menu-item .anticon, .ant-menu-submenu-title .anticon {
    font-size: 18px;  // 折叠状态下图标放大
    margin: 0;
  }
}
</style>

<!-- 添加非scoped样式块处理全局菜单样式 -->
<style lang="scss">
/* 自定义弹出菜单样式 */
.ant-menu-submenu-popup > .ant-menu {
  background-color: #e05450 !important;
}

.ant-menu-submenu-popup > .ant-menu-submenu > .ant-menu {
  background-color: #e05450 !important;
}

.ant-menu-submenu-popup .ant-menu-item {
  color: rgba(255, 255, 255, 0.85) !important;
  
  &:hover {
    background-color: #f26b69 !important;
  }
  
  &.ant-menu-item-selected {
    background-color: rgba(255, 255, 255, 0.15) !important;
    color: white !important;
  }
}

/* 确保垂直弹出菜单样式 */
.ant-menu-vertical.ant-menu-dark,
.ant-menu-vertical-left.ant-menu-dark,
.ant-menu-vertical-right.ant-menu-dark {
  background-color: #e05450 !important;
  
  .ant-menu-item {
    &:hover {
      background-color: #f26b69 !important;
    }
  }
}

/* 强制覆盖任何可能的内联样式 */
.ant-menu-dark.ant-menu-dark:not(.ant-menu-horizontal) .ant-menu-item-selected {
  background-color: rgba(255, 255, 255, 0.15) !important;
}

/* 确保折叠状态下的菜单背景色 */
.ant-menu-inline-collapsed.ant-menu-dark {
  background-color: #f5615c !important;
}
</style> 