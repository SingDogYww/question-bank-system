import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import BasicLayout from '../layouts/BasicLayout.vue'
import Login from '../views/Login.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/',
    component: BasicLayout,
    children: [
      // 首页
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/Dashboard.vue'),
        meta: { title: '数据概览' }
      },
      {
        path: 'monitor',
        name: 'monitor',
        component: () => import('../views/dashboard/Monitor.vue'),
        meta: { title: '数据概览' }
      },
      
      // 试题管理
      {
        path: 'questions/list',
        name: 'QuestionList',
        component: () => import('../views/questions/QuestionList.vue'),
        meta: { title: '试题列表' }
      },
      {
        path: 'questions/category',
        name: 'QuestionCategory',
        component: () => import('../views/questions/QuestionCategory.vue'),
        meta: { title: '试题分类' }
      },
      {
        path: 'questions/import',
        name: 'QuestionImport',
        component: () => import('../views/questions/QuestionImport.vue'),
        meta: { title: '试题导入' }
      },
      {
        path: 'questions/tag',
        name: 'QuestionTag',
        component: () => import('../views/questions/QuestionTag.vue'),
        meta: { title: '知识点标签' }
      },
      {
        path: 'questions/review',
        name: 'QuestionReview',
        component: () => import('../views/questions/QuestionReview.vue'),
        meta: { title: '试题审核' }
      },
      
      // 试卷管理
      {
        path: 'papers/list',
        name: 'PaperList',
        component: () => import('../views/papers/PaperList.vue'),
        meta: { title: '试卷列表' }
      },
      {
        path: 'papers/create',
        name: 'PaperCreate',
        component: () => import('../views/papers/PaperCreate.vue'),
        meta: { title: '创建试卷' }
      },
      {
        path: 'papers/template',
        name: 'PaperTemplate',
        component: () => import('../views/papers/PaperTemplate.vue'),
        meta: { title: '试卷模板' }
      },
      {
        path: 'papers/strategy',
        name: 'PaperStrategy',
        component: () => import('../views/papers/PaperStrategy.vue'),
        meta: { title: '组卷策略' }
      },
      {
        path: 'papers/publish',
        name: 'PaperPublish',
        component: () => import('../views/papers/PaperPublish.vue'),
        meta: { title: '试卷发布' }
      },
      
      // 考试管理
      {
        path: 'exams/list',
        name: 'ExamList',
        component: () => import('../views/exams/ExamList.vue'),
        meta: { title: '考试列表' }
      },
      {
        path: 'exams/schedule',
        name: 'ExamSchedule',
        component: () => import('../views/exams/ExamSchedule.vue'),
        meta: { title: '考试安排' }
      },
      {
        path: 'exams/monitor',
        name: 'ExamMonitor',
        component: () => import('../views/exams/ExamMonitor.vue'),
        meta: { title: '考试监控' }
      },
      {
        path: 'exams/rules',
        name: 'ExamRules',
        component: () => import('../views/exams/ExamRules.vue'),
        meta: { title: '考试规则' }
      },
      
      // 练习与错题
      {
        path: 'practice/chapter',
        name: 'ChapterPractice',
        component: () => import('../views/practice/Chapter.vue'),
        meta: { title: '章节练习' }
      },
      {
        path: 'practice/smart',
        name: 'SmartPractice',
        component: () => import('../views/practice/Smart.vue'),
        meta: { title: '智能刷题' }
      },
      {
        path: 'practice/mock',
        name: 'MockExam',
        component: () => import('../views/practice/Mock.vue'),
        meta: { title: '模拟考试' }
      },
      {
        path: 'practice/mistakes',
        name: 'MistakeBook',
        component: () => import('../views/practice/Mistakes.vue'),
        meta: { title: '错题本' }
      },
      
      // 成绩分析
      {
        path: 'analysis/report',
        name: 'ScoreReport',
        component: () => import('../views/analysis/Report.vue'),
        meta: { title: '成绩报告' }
      },
      {
        path: 'analysis/knowledge',
        name: 'KnowledgeAnalysis',
        component: () => import('../views/analysis/Knowledge.vue'),
        meta: { title: '知识点分析' }
      },
      {
        path: 'analysis/dashboard',
        name: 'DataDashboard',
        component: () => import('../views/analysis/Dashboard.vue'),
        meta: { title: '数据看板' }
      },
      {
        path: 'analysis/trend',
        name: 'TrendAnalysis',
        component: () => import('../views/analysis/Trend.vue'),
        meta: { title: '趋势分析' }
      },
      
      // 系统设置
      {
        path: 'settings/users',
        name: 'UserManagement',
        component: () => import('../views/settings/UserManagement.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'settings/roles',
        name: 'RoleManagement',
        component: () => import('../views/settings/RoleManagement.vue'),
        meta: { title: '角色权限' }
      },
      {
        path: 'settings/config',
        name: 'SystemConfig',
        component: () => import('../views/settings/SystemConfig.vue'),
        meta: { title: '系统配置' }
      },
      {
        path: 'settings/logs',
        name: 'OperationLog',
        component: () => import('../views/settings/OperationLog.vue'),
        meta: { title: '操作日志' }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由拦截器
router.beforeEach((to, from, next) => {
  // 可以在这里添加登录验证等逻辑
  document.title = `${to.meta.title || '在线考试系统'}`;
  next();
});

export default router
