<template>
  <div class="monitor-container">
    <h1 class="page-title">系统监控</h1>
    
    <!-- 顶部监控卡片 -->
    <a-row :gutter="16" class="stat-cards">
      <a-col :xs="24" :sm="12" :md="6">
        <a-card hoverable>
          <template #title>CPU 使用率</template>
          <template #extra>
            <a-tag color="red">{{ monitorData.cpu.status }}</a-tag>
          </template>
          <div class="card-content">
            <div class="stat-number">{{ monitorData.cpu.usage }}%</div>
            <a-progress :percent="monitorData.cpu.usage" :stroke-color="getProgressColor(monitorData.cpu.usage)" />
            <div class="stat-trend" :class="monitorData.cpu.trend > 0 ? 'up' : 'down'">
              <span>{{ monitorData.cpu.trend > 0 ? '↑' : '↓' }} {{ Math.abs(monitorData.cpu.trend) }}%</span> 较1小时前
            </div>
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :md="6">
        <a-card hoverable>
          <template #title>内存使用率</template>
          <template #extra>
            <a-tag color="orange">{{ monitorData.memory.status }}</a-tag>
          </template>
          <div class="card-content">
            <div class="stat-number">{{ monitorData.memory.usage }}%</div>
            <a-progress :percent="monitorData.memory.usage" :stroke-color="getProgressColor(monitorData.memory.usage)" />
            <div class="stat-trend" :class="monitorData.memory.trend > 0 ? 'up' : 'down'">
              <span>{{ monitorData.memory.trend > 0 ? '↑' : '↓' }} {{ Math.abs(monitorData.memory.trend) }}%</span> 较1小时前
            </div>
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :md="6">
        <a-card hoverable>
          <template #title>磁盘空间</template>
          <template #extra>
            <a-tag color="green">{{ monitorData.disk.status }}</a-tag>
          </template>
          <div class="card-content">
            <div class="stat-number">{{ monitorData.disk.usage }}%</div>
            <a-progress :percent="monitorData.disk.usage" :stroke-color="getProgressColor(monitorData.disk.usage)" />
            <div class="stat-detail">已用: {{ monitorData.disk.used }}GB / 总计: {{ monitorData.disk.total }}GB</div>
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :md="6">
        <a-card hoverable>
          <template #title>网络流量</template>
          <template #extra>
            <a-tag color="blue">{{ monitorData.network.status }}</a-tag>
          </template>
          <div class="card-content">
            <div class="stat-number">{{ monitorData.network.currentRate }}MB/s</div>
            <div class="stat-detail">
              <div>↑ 上行: {{ monitorData.network.upload }}MB/s</div>
              <div>↓ 下行: {{ monitorData.network.download }}MB/s</div>
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>
    
    <!-- 中部图表区 -->
    <a-row :gutter="16" class="chart-row">
      <!-- CPU 历史负载图 -->
      <a-col :xs="24" :lg="12">
        <a-card class="chart-card" title="CPU 历史负载">
          <template #extra>
            <a-radio-group v-model:value="cpuTimeRange" button-style="solid" size="small">
              <a-radio-button value="hour">小时</a-radio-button>
              <a-radio-button value="day">日</a-radio-button>
              <a-radio-button value="week">周</a-radio-button>
            </a-radio-group>
          </template>
          <div ref="cpuHistoryChart" class="chart-container"></div>
        </a-card>
      </a-col>
      <!-- 内存历史使用率 -->
      <a-col :xs="24" :lg="12">
        <a-card class="chart-card" title="内存历史使用率">
          <template #extra>
            <a-radio-group v-model:value="memoryTimeRange" button-style="solid" size="small">
              <a-radio-button value="hour">小时</a-radio-button>
              <a-radio-button value="day">日</a-radio-button>
              <a-radio-button value="week">周</a-radio-button>
            </a-radio-group>
          </template>
          <div ref="memoryHistoryChart" class="chart-container"></div>
        </a-card>
      </a-col>
    </a-row>
    
    <a-row :gutter="16" class="chart-row">
      <!-- 磁盘IO图 -->
      <a-col :xs="24" :lg="12">
        <a-card class="chart-card" title="磁盘IO">
          <div ref="diskIOChart" class="chart-container"></div>
        </a-card>
      </a-col>
      <!-- 网络流量历史 -->
      <a-col :xs="24" :lg="12">
        <a-card class="chart-card" title="网络流量历史">
          <div ref="networkTrafficChart" class="chart-container"></div>
        </a-card>
      </a-col>
    </a-row>
    
    <!-- 底部区域 -->
    <a-row :gutter="16" class="bottom-row">
      <!-- 系统告警 -->
      <a-col :xs="24" :lg="16">
        <a-card title="系统告警" class="logs-card">
          <a-list itemLayout="horizontal" :dataSource="monitorData.systemLogs">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta>
                  <template #title>
                    <div class="log-title">
                      <span>{{ item.message }}</span>
                      <a-tag :color="getLogLevelColor(item.level)">{{ item.level }}</a-tag>
                    </div>
                  </template>
                  <template #description>
                    <div class="log-desc">
                      <span>{{ item.source }}</span>
                      <span class="log-time">{{ item.time }}</span>
                    </div>
                  </template>
                  <template #avatar>
                    <a-avatar :style="{ backgroundColor: getLogLevelBgColor(item.level) }">
                      {{ item.level.charAt(0) }}
                    </a-avatar>
                  </template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
      <!-- 服务状态 -->
      <a-col :xs="24" :lg="8">
        <a-card title="服务状态" class="services-card">
          <a-list itemLayout="horizontal" :dataSource="monitorData.services">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta>
                  <template #title>
                    <div class="service-title">{{ item.name }}</div>
                  </template>
                  <template #description>
                    <div class="service-desc">
                      <span>{{ item.description }}</span>
                    </div>
                  </template>
                  <template #avatar>
                    <a-avatar :style="{ backgroundColor: item.status === '运行中' ? '#52c41a' : item.status === '已停止' ? '#f5222d' : '#faad14' }">
                      {{ item.status === '运行中' ? '正' : item.status === '已停止' ? '停' : '警' }}
                    </a-avatar>
                  </template>
                </a-list-item-meta>
                <div>
                  <a-tag :color="item.status === '运行中' ? 'green' : item.status === '已停止' ? 'red' : 'orange'">
                    {{ item.status }}
                  </a-tag>
                </div>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive, watch, onBeforeUnmount } from 'vue';
import * as echarts from 'echarts';

// 定义图表引用
const cpuHistoryChart = ref<HTMLElement | null>(null);
const memoryHistoryChart = ref<HTMLElement | null>(null);
const diskIOChart = ref<HTMLElement | null>(null);
const networkTrafficChart = ref<HTMLElement | null>(null);
const cpuTimeRange = ref<string>('hour');
const memoryTimeRange = ref<string>('hour');

// 图表实例
let cpuHistoryChartInstance: echarts.ECharts | null = null;
let memoryHistoryChartInstance: echarts.ECharts | null = null;
let diskIOChartInstance: echarts.ECharts | null = null;
let networkTrafficChartInstance: echarts.ECharts | null = null;

// 获取进度条颜色
const getProgressColor = (value: number) => {
  if (value < 60) return '#52c41a';
  if (value < 80) return '#faad14';
  return '#f5222d';
};

// 获取日志级别颜色
const getLogLevelColor = (level: string) => {
  switch (level) {
    case 'ERROR': return 'red';
    case 'WARNING': return 'orange';
    case 'INFO': return 'blue';
    default: return 'default';
  }
};

// 获取日志级别背景颜色
const getLogLevelBgColor = (level: string) => {
  switch (level) {
    case 'ERROR': return '#f5222d';
    case 'WARNING': return '#faad14';
    case 'INFO': return '#1890ff';
    default: return '#d9d9d9';
  }
};

// 示例数据，实际应用中应从API获取
const monitorData = reactive({
  // CPU数据
  cpu: {
    usage: 65,
    status: '中等负载',
    trend: 5.2,
    cores: 8,
    history: {
      hour: {
        times: Array.from({ length: 60 }, (_, i) => `${i}分`),
        values: Array.from({ length: 60 }, () => Math.floor(40 + Math.random() * 40))
      },
      day: {
        times: Array.from({ length: 24 }, (_, i) => `${i}时`),
        values: Array.from({ length: 24 }, () => Math.floor(30 + Math.random() * 50))
      },
      week: {
        times: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        values: Array.from({ length: 7 }, () => Math.floor(35 + Math.random() * 45))
      }
    }
  },
  
  // 内存数据
  memory: {
    usage: 72,
    status: '负载较高',
    trend: -2.5,
    total: 16,
    used: 11.52,
    history: {
      hour: {
        times: Array.from({ length: 60 }, (_, i) => `${i}分`),
        values: Array.from({ length: 60 }, () => Math.floor(60 + Math.random() * 30))
      },
      day: {
        times: Array.from({ length: 24 }, (_, i) => `${i}时`),
        values: Array.from({ length: 24 }, () => Math.floor(55 + Math.random() * 35))
      },
      week: {
        times: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        values: Array.from({ length: 7 }, () => Math.floor(50 + Math.random() * 40))
      }
    }
  },
  
  // 磁盘数据
  disk: {
    usage: 52,
    status: '正常',
    total: 500,
    used: 260,
    io: {
      times: Array.from({ length: 30 }, (_, i) => `${i*2}分`),
      read: Array.from({ length: 30 }, () => Math.floor(Math.random() * 100)),
      write: Array.from({ length: 30 }, () => Math.floor(Math.random() * 80))
    }
  },
  
  // 网络数据
  network: {
    status: '正常',
    currentRate: 15.6,
    upload: 5.2,
    download: 10.4,
    history: {
      times: Array.from({ length: 30 }, (_, i) => `${i*2}分`),
      upload: Array.from({ length: 30 }, () => Math.floor(Math.random() * 8)),
      download: Array.from({ length: 30 }, () => Math.floor(5 + Math.random() * 15))
    }
  },
  
  // 系统日志
  systemLogs: [
    { level: 'ERROR', message: '数据库连接失败', source: 'Database Server', time: '2023-12-23 15:42:30' },
    { level: 'WARNING', message: 'CPU使用率超过80%', source: 'System Monitor', time: '2023-12-23 15:30:15' },
    { level: 'INFO', message: '系统自动备份完成', source: 'Backup Service', time: '2023-12-23 15:00:00' },
    { level: 'INFO', message: '用户认证服务重启', source: 'Auth Service', time: '2023-12-23 14:45:22' },
    { level: 'WARNING', message: '磁盘I/O等待时间过长', source: 'Disk Monitor', time: '2023-12-23 14:30:10' },
    { level: 'INFO', message: '系统更新检查完成', source: 'Update Service', time: '2023-12-23 14:00:05' },
    { level: 'ERROR', message: 'API服务响应超时', source: 'API Gateway', time: '2023-12-23 13:52:41' }
  ],
  
  // 服务状态
  services: [
    { name: '数据库服务', description: 'MySQL Database', status: '运行中' },
    { name: 'API网关', description: 'RESTful API Gateway', status: '警告' },
    { name: '认证服务', description: 'OAuth2 Authentication', status: '运行中' },
    { name: '文件存储', description: 'File Storage Service', status: '运行中' },
    { name: '缓存服务', description: 'Redis Cache', status: '运行中' },
    { name: '任务调度', description: 'Task Scheduler', status: '已停止' }
  ]
});

// 初始化CPU历史负载图
const initCpuHistoryChart = () => {
  if (cpuHistoryChart.value) {
    cpuHistoryChartInstance = echarts.init(cpuHistoryChart.value);
    const data = monitorData.cpu.history[cpuTimeRange.value as keyof typeof monitorData.cpu.history];
    
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
        data: data.times,
        axisTick: {
          alignWithLabel: true
        }
      },
      yAxis: {
        type: 'value',
        max: 100,
        name: '使用率(%)'
      },
      series: [
        {
          name: 'CPU使用率',
          type: 'line',
          smooth: true,
          data: data.values,
          itemStyle: {
            color: '#f5615c'
          },
          markLine: {
            data: [
              { type: 'average', name: '平均值' },
              { yAxis: 80, lineStyle: { color: '#ff4d4f' }, name: '警戒线' }
            ]
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
    
    cpuHistoryChartInstance.setOption(option);
  }
};

// 初始化内存历史使用率图
const initMemoryHistoryChart = () => {
  if (memoryHistoryChart.value) {
    memoryHistoryChartInstance = echarts.init(memoryHistoryChart.value);
    const data = monitorData.memory.history[memoryTimeRange.value as keyof typeof monitorData.memory.history];
    
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
        data: data.times,
        axisTick: {
          alignWithLabel: true
        }
      },
      yAxis: {
        type: 'value',
        max: 100,
        name: '使用率(%)'
      },
      series: [
        {
          name: '内存使用率',
          type: 'line',
          smooth: true,
          data: data.values,
          itemStyle: {
            color: '#faad14'
          },
          markLine: {
            data: [
              { type: 'average', name: '平均值' },
              { yAxis: 80, lineStyle: { color: '#ff4d4f' }, name: '警戒线' }
            ]
          },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                { offset: 0, color: 'rgba(250, 173, 20, 0.8)' },
                { offset: 1, color: 'rgba(250, 173, 20, 0.1)' }
              ]
            }
          }
        }
      ]
    };
    
    memoryHistoryChartInstance.setOption(option);
  }
};

// 初始化磁盘IO图
const initDiskIOChart = () => {
  if (diskIOChart.value) {
    diskIOChartInstance = echarts.init(diskIOChart.value);
    
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          label: {
            backgroundColor: '#6a7985'
          }
        }
      },
      legend: {
        data: ['读取', '写入']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: monitorData.disk.io.times
      },
      yAxis: {
        type: 'value',
        name: 'MB/s'
      },
      series: [
        {
          name: '读取',
          type: 'line',
          data: monitorData.disk.io.read,
          itemStyle: {
            color: '#52c41a'
          }
        },
        {
          name: '写入',
          type: 'line',
          data: monitorData.disk.io.write,
          itemStyle: {
            color: '#1890ff'
          }
        }
      ]
    };
    
    diskIOChartInstance.setOption(option);
  }
};

// 初始化网络流量历史图
const initNetworkTrafficChart = () => {
  if (networkTrafficChart.value) {
    networkTrafficChartInstance = echarts.init(networkTrafficChart.value);
    
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          label: {
            backgroundColor: '#6a7985'
          }
        }
      },
      legend: {
        data: ['上传', '下载']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: monitorData.network.history.times
      },
      yAxis: {
        type: 'value',
        name: 'MB/s'
      },
      series: [
        {
          name: '上传',
          type: 'line',
          data: monitorData.network.history.upload,
          itemStyle: {
            color: '#722ed1'
          }
        },
        {
          name: '下载',
          type: 'line',
          data: monitorData.network.history.download,
          itemStyle: {
            color: '#13c2c2'
          }
        }
      ]
    };
    
    networkTrafficChartInstance.setOption(option);
  }
};

// 监听时间范围变化
watch(cpuTimeRange, () => {
  initCpuHistoryChart();
});

watch(memoryTimeRange, () => {
  initMemoryHistoryChart();
});

// 监听窗口大小变化
const handleResize = () => {
  cpuHistoryChartInstance?.resize();
  memoryHistoryChartInstance?.resize();
  diskIOChartInstance?.resize();
  networkTrafficChartInstance?.resize();
};

// 页面挂载时初始化图表
onMounted(() => {
  // 初始化所有图表
  initCpuHistoryChart();
  initMemoryHistoryChart();
  initDiskIOChart();
  initNetworkTrafficChart();
  
  // 添加窗口大小变化监听
  window.addEventListener('resize', handleResize);
});

// 组件卸载时移除事件监听
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped lang="scss">
.monitor-container {
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
          color: #f5222d; // 对于系统监控，上升通常是负面的
        }
        
        &.down {
          color: #52c41a; // 对于系统监控，下降通常是正面的
        }
      }
      
      .stat-detail {
        margin-top: 8px;
        font-size: 14px;
        color: rgba(0, 0, 0, 0.65);
      }
    }
  }
}

.chart-row {
  margin-bottom: 24px;
  
  .chart-card {
    margin-bottom: 16px;
    
    .chart-container {
      height: 300px;
      width: 100%;
    }
  }
}

.bottom-row {
  margin-bottom: 24px;
  
  .logs-card {
    margin-bottom: 16px;
    
    .log-title {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .log-desc {
      display: flex;
      justify-content: space-between;
      
      .log-time {
        color: rgba(0, 0, 0, 0.45);
      }
    }
  }
  
  .services-card {
    margin-bottom: 16px;
    
    .service-title {
      font-weight: 500;
    }
    
    .service-desc {
      color: rgba(0, 0, 0, 0.45);
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