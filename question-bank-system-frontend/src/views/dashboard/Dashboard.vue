<template>
  <div class="dashboard-container">
    <h1 class="page-title">数据概览</h1>
    
    <!-- 顶部统计卡片 -->
    <a-row :gutter="16" class="stat-cards">
      <a-col :xs="24" :sm="12" :md="6">
        <a-card hoverable>
          <template #title>在线人数</template>
          <template #extra>
            <a-tag color="blue">实时</a-tag>
          </template>
          <div class="card-content">
            <div class="stat-number">{{ dashboardData.onlineUsers }}</div>
            <div class="stat-trend up">
              <span>↑ {{ dashboardData.onlineUsersTrend }}%</span> 较昨日
            </div>
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :md="6">
        <a-card hoverable>
          <template #title>注册用户</template>
          <template #extra>
            <a-tag color="green">用户</a-tag>
          </template>
          <div class="card-content">
            <div class="stat-number">{{ dashboardData.registeredUsers }}</div>
            <div class="stat-trend up">
              <span>↑ {{ dashboardData.registeredUsersTrend }}%</span> 较上周
            </div>
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :md="6">
        <a-card hoverable>
          <template #title>试题总数</template>
          <template #extra>
            <a-tag color="orange">题库</a-tag>
          </template>
          <div class="card-content">
            <div class="stat-number">{{ dashboardData.totalQuestions }}</div>
            <div class="stat-trend up">
              <span>↑ {{ dashboardData.totalQuestionsTrend }}%</span> 较上周
            </div>
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :md="6">
        <a-card hoverable>
          <template #title>试卷总数</template>
          <template #extra>
            <a-tag color="red">考试</a-tag>
          </template>
          <div class="card-content">
            <div class="stat-number">{{ dashboardData.totalPapers }}</div>
            <div class="stat-trend" :class="dashboardData.totalPapersTrend > 0 ? 'up' : 'down'">
              <span>{{ dashboardData.totalPapersTrend > 0 ? '↑' : '↓' }} {{ Math.abs(dashboardData.totalPapersTrend) }}%</span> 较上周
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>
    
    <!-- 中部图表区 -->
    <a-row :gutter="16" class="chart-row">
      <!-- 活跃用户趋势图 -->
      <a-col :xs="24" :lg="16">
        <a-card class="chart-card" title="用户活跃度趋势">
          <template #extra>
            <a-radio-group v-model:value="userActivityTimeRange" button-style="solid" size="small">
              <a-radio-button value="week">周</a-radio-button>
              <a-radio-button value="month">月</a-radio-button>
              <a-radio-button value="year">年</a-radio-button>
            </a-radio-group>
          </template>
          <div ref="userActivityChart" class="chart-container"></div>
        </a-card>
      </a-col>
      <!-- 试题分类占比 -->
      <a-col :xs="24" :lg="8">
        <a-card class="chart-card" title="试题分类占比">
          <div ref="questionCategoryChart" class="chart-container"></div>
        </a-card>
      </a-col>
    </a-row>
    
    <a-row :gutter="16" class="chart-row">
      <!-- 考试成绩分布 -->
      <a-col :xs="24" :lg="12">
        <a-card class="chart-card" title="考试成绩分布">
          <div ref="examScoreChart" class="chart-container"></div>
        </a-card>
      </a-col>
      <!-- 用户地区分布 -->
      <a-col :xs="24" :lg="12">
        <a-card class="chart-card" title="用户地区分布">
          <div ref="userRegionChart" class="chart-container"></div>
        </a-card>
      </a-col>
    </a-row>
    
    <!-- 底部区域 -->
    <a-row :gutter="16" class="bottom-row">
      <!-- 系统公告 -->
      <a-col :xs="24" :lg="16">
        <a-card title="系统公告" class="announcement-card">
          <a-list itemLayout="horizontal" :dataSource="dashboardData.announcements">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta>
                  <template #title>
                    <a href="javascript:void(0)">{{ item.title }}</a>
                  </template>
                  <template #description>
                    <div class="announcement-desc">
                      <span>{{ item.content }}</span>
                      <span class="announcement-time">{{ item.time }}</span>
                    </div>
                  </template>
                  <template #avatar>
                    <a-avatar :style="{ backgroundColor: '#f56a00' }">公告</a-avatar>
                  </template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
      <!-- 最近考试 -->
      <a-col :xs="24" :lg="8">
        <a-card title="最近考试" class="recent-exam-card">
          <a-timeline>
            <a-timeline-item v-for="(exam, index) in dashboardData.recentExams" 
                            :key="index" 
                            :color="exam.status === '进行中' ? 'green' : exam.status === '已结束' ? 'gray' : 'blue'">
              <div class="exam-item">
                <div class="exam-title">{{ exam.title }}</div>
                <div class="exam-info">
                  <span>{{ exam.time }}</span>
                  <a-tag :color="exam.status === '进行中' ? 'green' : exam.status === '已结束' ? 'gray' : 'blue'">
                    {{ exam.status }}
                  </a-tag>
                </div>
              </div>
            </a-timeline-item>
          </a-timeline>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive, watch } from 'vue';
import * as echarts from 'echarts';

// 定义图表引用
const userActivityChart = ref<HTMLElement | null>(null);
const questionCategoryChart = ref<HTMLElement | null>(null);
const examScoreChart = ref<HTMLElement | null>(null);
const userRegionChart = ref<HTMLElement | null>(null);
const userActivityTimeRange = ref<string>('month');

// 图表实例
let userActivityChartInstance: echarts.ECharts | null = null;
let questionCategoryChartInstance: echarts.ECharts | null = null;
let examScoreChartInstance: echarts.ECharts | null = null;
let userRegionChartInstance: echarts.ECharts | null = null;

// 示例数据，实际应用中应从API获取
const dashboardData = reactive({
  // 统计数据
  onlineUsers: 368,
  onlineUsersTrend: 12.5,
  registeredUsers: 15642,
  registeredUsersTrend: 5.3,
  totalQuestions: 8456,
  totalQuestionsTrend: 7.2,
  totalPapers: 342,
  totalPapersTrend: -1.8,
  
  // 公告数据
  announcements: [
    { 
      title: '系统更新通知', 
      content: '系统将于2023年12月25日进行版本更新，届时系统将暂停使用2小时，请合理安排时间。', 
      time: '2023-12-20' 
    },
    { 
      title: '期末考试安排', 
      content: '2023学年第一学期期末考试将于2024年1月10日开始，请各位同学提前做好准备。', 
      time: '2023-12-15' 
    },
    { 
      title: '新功能发布通知', 
      content: '系统新增智能错题本功能，可自动收集学生错题并生成针对性练习。', 
      time: '2023-12-10' 
    }
  ],
  
  // 最近考试
  recentExams: [
    { title: '2023学年第一学期期末考试', time: '2024-01-10', status: '未开始' },
    { title: '数据库原理与应用测试', time: '2023-12-15', status: '已结束' },
    { title: '计算机网络原理期中考试', time: '2023-11-20', status: '已结束' },
    { title: '软件工程实践考核', time: '2023-12-25', status: '未开始' },
    { title: 'Java程序设计阶段测试', time: '2023-12-22', status: '进行中' }
  ],
  
  // 用户活跃度数据
  userActivity: {
    week: {
      dates: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
      values: [120, 132, 101, 134, 90, 180, 210]
    },
    month: {
      dates: ['1日', '5日', '10日', '15日', '20日', '25日', '30日'],
      values: [350, 420, 380, 470, 510, 620, 580]
    },
    year: {
      dates: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
      values: [820, 932, 901, 934, 1290, 1330, 1320, 1150, 1230, 1410, 1530, 1620]
    }
  },
  
  // 试题分类数据
  questionCategories: [
    { value: 1048, name: '选择题' },
    { value: 735, name: '填空题' },
    { value: 580, name: '判断题' },
    { value: 484, name: '简答题' },
    { value: 300, name: '编程题' }
  ],
  
  // 考试成绩分布
  examScores: [
    { score: '不及格', count: 150 },
    { score: '60-70分', count: 300 },
    { score: '71-80分', count: 450 },
    { score: '81-90分', count: 380 },
    { score: '91-100分', count: 220 }
  ],
  
  // 用户地区分布
  userRegions: [
    { name: '北京', value: 1230 },
    { name: '上海', value: 980 },
    { name: '广州', value: 870 },
    { name: '深圳', value: 830 },
    { name: '杭州', value: 720 },
    { name: '成都', value: 610 },
    { name: '武汉', value: 580 },
    { name: '西安', value: 520 },
    { name: '南京', value: 490 },
    { name: '重庆', value: 460 }
  ]
});

// 初始化活跃用户趋势图
const initUserActivityChart = () => {
  if (userActivityChart.value) {
    userActivityChartInstance = echarts.init(userActivityChart.value);
    const data = dashboardData.userActivity[userActivityTimeRange.value as keyof typeof dashboardData.userActivity];
    
    const option = {
      title: {
        text: '用户活跃度趋势',
        left: 'center',
        show: false
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: data.dates,
        axisTick: {
          alignWithLabel: true
        }
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '活跃用户',
          type: 'line',
          smooth: true,
          data: data.values,
          itemStyle: {
            color: '#f5615c'
          },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                { offset: 0, color: 'rgba(245, 97, 92, 0.8)' },
                { offset: 1, color: 'rgba(245, 97, 92, 0.1)' }
              ]
            }
          }
        }
      ]
    };
    
    userActivityChartInstance.setOption(option);
  }
};

// 初始化试题分类占比图
const initQuestionCategoryChart = () => {
  if (questionCategoryChart.value) {
    questionCategoryChartInstance = echarts.init(questionCategoryChart.value);
    
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 10,
        data: dashboardData.questionCategories.map(item => item.name)
      },
      series: [
        {
          name: '试题分类',
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '18',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: dashboardData.questionCategories
        }
      ]
    };
    
    questionCategoryChartInstance.setOption(option);
  }
};

// 初始化考试成绩分布图
const initExamScoreChart = () => {
  if (examScoreChart.value) {
    examScoreChartInstance = echarts.init(examScoreChart.value);
    
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: dashboardData.examScores.map(item => item.score)
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '人数',
          type: 'bar',
          data: dashboardData.examScores.map(item => item.count),
          itemStyle: {
            color: function(params: any) {
              const colorList = ['#e74c3c', '#e67e22', '#f1c40f', '#2ecc71', '#3498db'];
              return colorList[params.dataIndex];
            }
          }
        }
      ]
    };
    
    examScoreChartInstance.setOption(option);
  }
};

// 初始化用户地区分布图
const initUserRegionChart = () => {
  if (userRegionChart.value) {
    userRegionChartInstance = echarts.init(userRegionChart.value);
    
    // 由于缺少地图数据，这里使用条形图代替
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'value'
      },
      yAxis: {
        type: 'category',
        data: dashboardData.userRegions.map(item => item.name),
        axisLabel: {
          interval: 0
        }
      },
      series: [
        {
          name: '用户数',
          type: 'bar',
          data: dashboardData.userRegions.map(item => item.value),
          itemStyle: {
            color: '#3498db'
          }
        }
      ]
    };
    
    userRegionChartInstance.setOption(option);
  }
};

// 监听时间范围变化
watch(userActivityTimeRange, () => {
  initUserActivityChart();
});

// 监听窗口大小变化
const handleResize = () => {
  userActivityChartInstance?.resize();
  questionCategoryChartInstance?.resize();
  examScoreChartInstance?.resize();
  userRegionChartInstance?.resize();
};

// 页面挂载时初始化图表
onMounted(() => {
  // 初始化所有图表
  initUserActivityChart();
  initQuestionCategoryChart();
  initExamScoreChart();
  initUserRegionChart();
  
  // 添加窗口大小变化监听
  window.addEventListener('resize', handleResize);
});

// 组件卸载时移除事件监听
const onBeforeUnmount = () => {
  window.removeEventListener('resize', handleResize);
};
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 0 10px;
}

.page-title {
  font-size: 24px;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.85);
  margin-bottom: 24px;
  border-left: 4px solid #f5615c;
  padding-left: 12px;
}

.stat-cards {
  margin-bottom: 24px;
  
  .ant-card {
    margin-bottom: 16px;
    
    .card-content {
      text-align: center;
      
      .stat-number {
        font-size: 28px;
        font-weight: bold;
        color: rgba(0, 0, 0, 0.85);
      }
      
      .stat-trend {
        margin-top: 8px;
        font-size: 14px;
        
        &.up {
          color: #52c41a;
        }
        
        &.down {
          color: #f5222d;
        }
      }
    }
  }
}

.chart-row {
  margin-bottom: 24px;
  
  .chart-card {
    margin-bottom: 16px;
    
    .chart-container {
      height: 320px;
      width: 100%;
    }
  }
}

.bottom-row {
  margin-bottom: 24px;
  
  .announcement-card {
    margin-bottom: 16px;
    
    .announcement-desc {
      display: flex;
      flex-direction: column;
      
      .announcement-time {
        color: rgba(0, 0, 0, 0.45);
        font-size: 12px;
        margin-top: 4px;
      }
    }
  }
  
  .recent-exam-card {
    margin-bottom: 16px;
    
    .exam-item {
      .exam-title {
        font-weight: 500;
        margin-bottom: 4px;
      }
      
      .exam-info {
        display: flex;
        justify-content: space-between;
        color: rgba(0, 0, 0, 0.45);
        font-size: 12px;
      }
    }
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .chart-container {
    height: 260px !important;
  }
}
</style>