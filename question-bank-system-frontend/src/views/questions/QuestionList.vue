<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import {
  PlusOutlined,
  DeleteOutlined,
  UploadOutlined,
  DownloadOutlined,
  ReloadOutlined,
} from '@ant-design/icons-vue';
import type { TableColumnsType, TablePaginationConfig } from 'ant-design-vue';
import { message, Upload, Form } from 'ant-design-vue';
import { theme } from 'ant-design-vue';

// 定义主题颜色
const themeColors = {
  primary: '#f5615c',
  hover: '#ff7875',
  active: '#e84749',
  light: '#fff1f0'
};

// 定义自定义主题
const customTheme = {
  token: {
    colorPrimary: themeColors.primary,
    colorLink: '#1890ff',
    colorSuccess: '#52c41a',
    colorWarning: '#faad14',
    colorError: '#ff4d4f',
    borderRadius: 4,
  },
  components: {
    Button: {
      colorPrimary: themeColors.primary,
      algorithm: true, // 启用算法
    },
    Table: {
      colorPrimary: themeColors.primary,
      colorBgContainer: '#ffffff',
      colorTableHeaderBg: '#f5f5f5',
    },
    Pagination: {
      colorPrimary: themeColors.primary,
    },
    Tag: {
      colorPrimary: themeColors.primary,
    },
    Input: {
      colorPrimary: themeColors.primary,
      colorPrimaryHover: themeColors.hover,
    },
    Modal: {
      colorPrimary: themeColors.primary,
    },
    Select: {
      colorPrimary: themeColors.primary,
    },
    Form: {
      colorPrimary: themeColors.primary,
    },
  },
};

// 定义表格列
const columns: TableColumnsType = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 80,
  },
  {
    title: '试题内容',
    dataIndex: 'content',
    ellipsis: true,
  },
  {
    title: '题型',
    dataIndex: 'type',
    width: 120,
  },
  {
    title: '分类',
    dataIndex: 'category',
    width: 120,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
    sorter: true,
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 120,
    fixed: 'right',
  },
];

// 状态变量
const searchText = ref('');
const loading = ref(false);
const selectedRowKeys = ref<number[]>([]);
const importModalVisible = ref(false);
const addModalVisible = ref(false);
const editModalVisible = ref(false);
const batchDeleteModalVisible = ref(false);
const pagination = reactive<TablePaginationConfig>({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total) => `共 ${total} 条记录`,
});

// 表单状态
const formState = reactive({
  id: null,
  content: '',
  type: '',
  category: '',
  difficulty: '',
  status: '',
});

// 计算属性
const hasSelected = computed(() => selectedRowKeys.value.length > 0);

// 模拟数据
const questionList = ref<any[]>([
  {
    id: 1,
    content: '在Java中，用于启动一个线程的方法是？',
    type: '单选题',
    category: 'Java',
    difficulty: 'easy',
    status: '已发布',
    createTime: '2023-12-20 15:30:22',
  },
  {
    id: 2,
    content: '下列哪种设计模式用于创建对象实例？',
    type: '多选题',
    category: '设计模式',
    difficulty: 'medium',
    status: '已发布',
    createTime: '2023-12-19 10:25:18',
  },
  {
    id: 3,
    content: '数据库中的事务应该满足的特性是？(ACID)',
    type: '填空题',
    category: '数据库',
    difficulty: 'medium', 
    status: '草稿',
    createTime: '2023-12-18 09:15:46',
  },
  {
    id: 4,
    content: '分析时间复杂度为O(n log n)的排序算法的性能特点。',
    type: '简答题',
    category: '算法',
    difficulty: 'hard',
    status: '已发布',
    createTime: '2023-12-17 14:20:33',
  },
  {
    id: 5,
    content: 'HTTP状态码404表示什么？',
    type: '单选题',
    category: '网络',
    difficulty: 'easy',
    status: '已发布',
    createTime: '2023-12-16 11:10:05',
  },
]);

// 初始化
onMounted(() => {
  // 这里可以添加获取列表数据的API调用
  loading.value = true;
  setTimeout(() => {
    pagination.total = questionList.value.length;
    loading.value = false;
  }, 500);
});

// 处理选择变化
const onSelectChange = (selectedKeys: number[]) => {
  selectedRowKeys.value = selectedKeys;
};

// 清空选择
const onSelectNone = () => {
  selectedRowKeys.value = [];
};

// 处理搜索
const handleSearch = () => {
  loading.value = true;
  // 实际项目中，这里应该调用API进行搜索
  setTimeout(() => {
    // 模拟搜索结果
    if (searchText.value) {
      questionList.value = questionList.value.filter(
        item => item.content.includes(searchText.value) ||
               item.type.includes(searchText.value) ||
               item.category.includes(searchText.value)
      );
    }
    loading.value = false;
  }, 500);
};

// 重置搜索
const handleReset = () => {
  searchText.value = '';
  // 重新加载数据
  // 实际项目中这里应该重新调用API获取数据
};

// 处理表格变化（排序、筛选、分页）
const handleTableChange = (pag: TablePaginationConfig) => {
  pagination.current = pag.current || 1;
  pagination.pageSize = pag.pageSize || 10;
  // 实际项目中这里应该重新调用API获取数据
};

// 处理添加
const handleAdd = () => {
  // 重置表单
  Object.keys(formState).forEach(key => {
    if (key === 'id') {
      formState.id = null;
    } else {
      (formState as any)[key] = '';
    }
  });
  
  // 显示添加弹窗
  addModalVisible.value = true;
};

// 确认添加
const confirmAdd = () => {
  // 表单验证
  if (!formState.content || !formState.type || !formState.category || !formState.difficulty || !formState.status) {
    message.error('请填写完整信息');
    return;
  }
  
  // 模拟添加
  const newId = Math.max(...questionList.value.map(item => item.id)) + 1;
  const now = new Date();
  const createTime = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}:${String(now.getSeconds()).padStart(2, '0')}`;
  
  questionList.value.unshift({
    id: newId,
    content: formState.content,
    type: formState.type,
    category: formState.category,
    difficulty: formState.difficulty,
    status: formState.status,
    createTime,
  });
  
  pagination.total = questionList.value.length;
  message.success('添加成功');
  addModalVisible.value = false;
};

// 取消添加
const cancelAdd = () => {
  addModalVisible.value = false;
};

// 处理编辑
const handleEdit = (record: any) => {
  // 填充表单
  Object.keys(formState).forEach(key => {
    if (key in record) {
      (formState as any)[key] = record[key];
    }
  });
  
  // 显示编辑弹窗
  editModalVisible.value = true;
};

// 确认编辑
const confirmEdit = () => {
  // 表单验证
  if (!formState.content || !formState.type || !formState.category || !formState.difficulty || !formState.status) {
    message.error('请填写完整信息');
    return;
  }
  
  // 更新数据
  const index = questionList.value.findIndex(item => item.id === formState.id);
  if (index !== -1) {
    questionList.value[index] = {
      ...questionList.value[index],
      content: formState.content,
      type: formState.type,
      category: formState.category,
      difficulty: formState.difficulty,
      status: formState.status,
    };
    
    message.success('更新成功');
    editModalVisible.value = false;
  }
};

// 取消编辑
const cancelEdit = () => {
  editModalVisible.value = false;
};

// 处理删除
const handleDelete = (record: any) => {
  // 调用API删除试题
  loading.value = true;
  setTimeout(() => {
    questionList.value = questionList.value.filter(item => item.id !== record.id);
    pagination.total = questionList.value.length;
    loading.value = false;
    message.success('删除成功');
  }, 500);
};

// 根据ID获取试题
const getQuestionById = (id: number) => {
  return questionList.value.find(item => item.id === id);
};

// 处理批量删除
const handleBatchDelete = () => {
  if (!hasSelected.value) return;
  
  batchDeleteModalVisible.value = true;
};

// 确认批量删除
const confirmBatchDelete = () => {
  // 调用API批量删除试题
  loading.value = true;
  setTimeout(() => {
    questionList.value = questionList.value.filter(
      item => !selectedRowKeys.value.includes(item.id)
    );
    pagination.total = questionList.value.length;
    selectedRowKeys.value = [];
    loading.value = false;
    message.success('批量删除成功');
    batchDeleteModalVisible.value = false;
  }, 500);
};

// 取消批量删除
const cancelBatchDelete = () => {
  batchDeleteModalVisible.value = false;
};

// 处理导入
const handleImport = () => {
  importModalVisible.value = true;
};

// 处理上传前验证
const beforeUpload = (file: File) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || 
                 file.type === 'application/vnd.ms-excel';
  if (!isExcel) {
    message.error('只能上传Excel文件！');
  }
  return isExcel || Upload.LIST_IGNORE;
};

// 处理上传变更
const handleUploadChange = (info: any) => {
  // 处理上传状态变化
};

// 确认导入
const confirmImport = () => {
  message.success('导入成功');
  importModalVisible.value = false;
};

// 取消导入
const cancelImport = () => {
  importModalVisible.value = false;
};

// 下载模板
const downloadTemplate = () => {
  message.info('下载模板');
  // 实际项目中这里应该提供模板下载链接
};

// 处理导出
const handleExport = () => {
  if (selectedRowKeys.value.length > 0) {
    // 导出选中的试题
    message.info(`导出选中的${selectedRowKeys.value.length}道试题`);
  } else {
    // 导出全部试题
    message.info('导出全部试题');
  }
};

// 文本截断工具函数
const truncateText = (text: string, maxLength: number) => {
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
};
</script>

<template>
  <a-config-provider :theme="customTheme">
    <div class="question-list-container">
      <h1 class="page-title">试题列表</h1>
      
      <!-- 功能区 -->
      <div class="operation-area">
        <a-space>
          <a-button type="primary" @click="handleAdd">
            <template #icon><plus-outlined /></template>
            新增试题
          </a-button>
          <a-button danger :disabled="!hasSelected" @click="handleBatchDelete">
            <template #icon><delete-outlined /></template>
            批量删除
          </a-button>
          <a-button @click="handleImport">
            <template #icon><upload-outlined /></template>
            批量导入
          </a-button>
          <a-button @click="handleExport">
            <template #icon><download-outlined /></template>
            导出试题
          </a-button>
        </a-space>

        <a-space>
          <a-input-search
            v-model:value="searchText"
            placeholder="请输入试题关键字"
            style="width: 300px"
            @search="handleSearch"
          />
          <a-button @click="handleReset">
            <template #icon><reload-outlined /></template>
            重置
          </a-button>
        </a-space>
      </div>
      
      <!-- 试题列表 -->
      <a-table
        :columns="columns"
        :row-selection="{ selectedRowKeys, onChange: onSelectChange }"
        :data-source="questionList"
        :pagination="false"
        :loading="loading"
        @change="handleTableChange"
        row-key="id"
        class="question-table"
      >
        <!-- 题目列 -->
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'content'">
            <div class="question-content">
              <a-tooltip :title="record.content">
                {{ truncateText(record.content, 50) }}
              </a-tooltip>
              <a-tag v-if="record.difficulty === 'easy'" color="green">简单</a-tag>
              <a-tag v-else-if="record.difficulty === 'medium'" color="orange">中等</a-tag>
              <a-tag v-else-if="record.difficulty === 'hard'" color="red">困难</a-tag>
            </div>
          </template>
          
          <!-- 状态列 -->
          <template v-else-if="column.dataIndex === 'status'">
            <a-tag :color="record.status === '已发布' ? 'green' : 'orange'">
              {{ record.status }}
            </a-tag>
          </template>
          
          <!-- 操作列 -->
          <template v-else-if="column.dataIndex === 'action'">
            <div class="action-buttons">
              <a @click="handleEdit(record)" class="edit-link">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm
                title="确定要删除这道试题吗？"
                @confirm="handleDelete(record)"
                ok-text="确定"
                cancel-text="取消"
              >
                <a class="delete-text">删除</a>
              </a-popconfirm>
            </div>
          </template>
        </template>
      </a-table>

      <!-- 自定义分页和选择提示区域 -->
      <div class="custom-pagination-area">
        <div class="selection-area">
          <span v-if="hasSelected" class="selection-text">
            已选择 {{ selectedRowKeys.length }} 项
            <a class="clear-selection" @click="onSelectNone">清除</a>
          </span>
        </div>
        <div class="pagination-wrapper">
          <a-pagination
            v-model:current="pagination.current"
            :total="pagination.total"
            :pageSize="pagination.pageSize"
            :showSizeChanger="pagination.showSizeChanger"
            :showQuickJumper="pagination.showQuickJumper"
            :showTotal="pagination.showTotal"
            @change="(page: number, pageSize: number) => handleTableChange({ current: page, pageSize })"
            @showSizeChange="(current: number, size: number) => handleTableChange({ current, pageSize: size })"
          />
        </div>
      </div>

      <!-- 导入试题弹窗 -->
      <a-modal
        v-model:visible="importModalVisible"
        title="批量导入试题"
        @ok="confirmImport"
        @cancel="cancelImport"
        width="500px"
      >
        <a-upload
          name="file"
          :multiple="false"
          :before-upload="beforeUpload"
          @change="handleUploadChange"
        >
          <a-button class="theme-button">
            <template #icon><upload-outlined /></template>
            选择文件
          </a-button>
        </a-upload>
        <div class="upload-hint">
          支持Excel格式的试题导入，请先下载<a @click="downloadTemplate">试题导入模板</a>
        </div>
      </a-modal>

      <!-- 新增试题弹窗 -->
      <a-modal
        v-model:visible="addModalVisible"
        title="新增试题"
        @ok="confirmAdd"
        @cancel="cancelAdd"
        width="700px"
      >
        <Form :model="formState" layout="vertical">
          <Form.Item label="试题内容" name="content" :rules="[{ required: true, message: '请输入试题内容' }]">
            <a-textarea v-model:value="formState.content" placeholder="请输入试题内容" :rows="4" />
          </Form.Item>
          <a-row :gutter="16">
            <a-col :span="12">
              <Form.Item label="题型" name="type" :rules="[{ required: true, message: '请选择题型' }]">
                <a-select v-model:value="formState.type" placeholder="请选择题型">
                  <a-select-option value="单选题">单选题</a-select-option>
                  <a-select-option value="多选题">多选题</a-select-option>
                  <a-select-option value="填空题">填空题</a-select-option>
                  <a-select-option value="简答题">简答题</a-select-option>
                </a-select>
              </Form.Item>
            </a-col>
            <a-col :span="12">
              <Form.Item label="分类" name="category" :rules="[{ required: true, message: '请选择分类' }]">
                <a-select v-model:value="formState.category" placeholder="请选择分类">
                  <a-select-option value="Java">Java</a-select-option>
                  <a-select-option value="设计模式">设计模式</a-select-option>
                  <a-select-option value="数据库">数据库</a-select-option>
                  <a-select-option value="算法">算法</a-select-option>
                  <a-select-option value="网络">网络</a-select-option>
                </a-select>
              </Form.Item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="12">
              <Form.Item label="难度" name="difficulty" :rules="[{ required: true, message: '请选择难度' }]">
                <a-select v-model:value="formState.difficulty" placeholder="请选择难度">
                  <a-select-option value="easy">简单</a-select-option>
                  <a-select-option value="medium">中等</a-select-option>
                  <a-select-option value="hard">困难</a-select-option>
                </a-select>
              </Form.Item>
            </a-col>
            <a-col :span="12">
              <Form.Item label="状态" name="status" :rules="[{ required: true, message: '请选择状态' }]">
                <a-select v-model:value="formState.status" placeholder="请选择状态">
                  <a-select-option value="已发布">已发布</a-select-option>
                  <a-select-option value="草稿">草稿</a-select-option>
                </a-select>
              </Form.Item>
            </a-col>
          </a-row>
        </Form>
      </a-modal>

      <!-- 编辑试题弹窗 -->
      <a-modal
        v-model:visible="editModalVisible"
        title="编辑试题"
        @ok="confirmEdit"
        @cancel="cancelEdit"
        width="700px"
      >
        <Form :model="formState" layout="vertical">
          <Form.Item label="试题内容" name="content" :rules="[{ required: true, message: '请输入试题内容' }]">
            <a-textarea v-model:value="formState.content" placeholder="请输入试题内容" :rows="4" />
          </Form.Item>
          <a-row :gutter="16">
            <a-col :span="12">
              <Form.Item label="题型" name="type" :rules="[{ required: true, message: '请选择题型' }]">
                <a-select v-model:value="formState.type" placeholder="请选择题型">
                  <a-select-option value="单选题">单选题</a-select-option>
                  <a-select-option value="多选题">多选题</a-select-option>
                  <a-select-option value="填空题">填空题</a-select-option>
                  <a-select-option value="简答题">简答题</a-select-option>
                </a-select>
              </Form.Item>
            </a-col>
            <a-col :span="12">
              <Form.Item label="分类" name="category" :rules="[{ required: true, message: '请选择分类' }]">
                <a-select v-model:value="formState.category" placeholder="请选择分类">
                  <a-select-option value="Java">Java</a-select-option>
                  <a-select-option value="设计模式">设计模式</a-select-option>
                  <a-select-option value="数据库">数据库</a-select-option>
                  <a-select-option value="算法">算法</a-select-option>
                  <a-select-option value="网络">网络</a-select-option>
                </a-select>
              </Form.Item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="12">
              <Form.Item label="难度" name="difficulty" :rules="[{ required: true, message: '请选择难度' }]">
                <a-select v-model:value="formState.difficulty" placeholder="请选择难度">
                  <a-select-option value="easy">简单</a-select-option>
                  <a-select-option value="medium">中等</a-select-option>
                  <a-select-option value="hard">困难</a-select-option>
                </a-select>
              </Form.Item>
            </a-col>
            <a-col :span="12">
              <Form.Item label="状态" name="status" :rules="[{ required: true, message: '请选择状态' }]">
                <a-select v-model:value="formState.status" placeholder="请选择状态">
                  <a-select-option value="已发布">已发布</a-select-option>
                  <a-select-option value="草稿">草稿</a-select-option>
                </a-select>
              </Form.Item>
            </a-col>
          </a-row>
        </Form>
      </a-modal>

      <!-- 批量删除确认弹窗 -->
      <a-modal
        v-model:visible="batchDeleteModalVisible"
        title="批量删除确认"
        @ok="confirmBatchDelete"
        @cancel="cancelBatchDelete"
        width="500px"
      >
        <p>确定要删除以下试题吗？</p>
        <div class="delete-list">
          <a-list size="small" bordered>
            <a-list-item v-for="id in selectedRowKeys" :key="id">
              {{ getQuestionById(id)?.content }}
            </a-list-item>
          </a-list>
        </div>
      </a-modal>
    </div>
  </a-config-provider>
</template>

<style scoped lang="scss">
.question-list-container {
  padding: 0 10px;
}

.page-title {
  font-size: 24px;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.85);
  margin-bottom: 24px;
  border-left: 4px solid v-bind('themeColors.primary');
  padding-left: 12px;
}

.operation-area {
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.selected-info {
  display: none;
}

.question-table {
  margin-bottom: 8px;
  
  :deep(.ant-table-thead > tr > th) {
    background-color: #f5f5f5;
    color: rgba(0, 0, 0, 0.85);
    font-weight: 500;
  }
  
  :deep(.ant-table-tbody > tr > td) {
    color: rgba(0, 0, 0, 0.65);
  }
  
  :deep(.ant-table-tbody > tr:hover > td) {
    background-color: v-bind('themeColors.light');
  }
  
  :deep(.ant-pagination-item-active) {
    border-color: v-bind('themeColors.primary');
    
    a {
      color: v-bind('themeColors.primary');
    }
  }
}

.question-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.action-buttons {
  display: flex;
  align-items: center;
  white-space: nowrap;
}

.edit-link {
  color: #1890ff;
}

.delete-text {
  color: #ff4d4f;
}

.delete-list {
  max-height: 300px;
  overflow-y: auto;
}

.upload-hint {
  margin-top: 16px;
  color: rgba(0, 0, 0, 0.45);
}

// 搜索框样式（独立样式）
:deep(.ant-input-search) {
  // 输入框样式
  .ant-input {
    height: 32px;
    border-radius: 4px 0 0 4px;
    
    &::placeholder {
      color: #bfbfbf;
      font-size: 14px;
    }
  }
  
  // 搜索图标
  .anticon {
    font-size: 16px;
  }
}

// 添加自定义分页区域样式
.custom-pagination-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  margin-bottom: 24px;
}

.selection-area {
  min-width: 120px;
}

.selection-text {
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
  padding: 4px 0;
  display: inline-block;
}

.clear-selection {
  margin-left: 8px;
  color: v-bind('themeColors.primary');
  cursor: pointer;
  
  &:hover {
    color: v-bind('themeColors.hover');
  }
}

.pagination-wrapper {
  // 分页组件容器
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .operation-area {
    flex-direction: column;
    align-items: flex-start;
  }

  .custom-pagination-area {
    flex-direction: column;
    align-items: stretch;
    
    .selection-area {
      margin-bottom: 12px;
    }
    
    .pagination-wrapper {
      width: 100%;
      justify-content: flex-end;
    }
  }
}
</style>