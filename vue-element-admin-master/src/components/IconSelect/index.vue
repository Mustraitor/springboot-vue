<template>
  <div class="icon-body">
    <el-input
      v-model="name"
      clearable
      placeholder="请输入图标名称"
      @clear="reset"
      @input.native="filterIcons"
    >
      <i slot="suffix" class="el-icon-search el-input_icon" />
    </el-input>

    <div class="icon-list">
      <div
        v-for="(item, index) in iconList"
        :key="index"
        class="icon-item"
        @click="selectIcon(item)"
      >
        <svg-icon :icon-class="item || ''" class="icon-svg" />
        <span>{{ item }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import icons from './requireIcons.js' // 图标路径

export default {
  name: 'IconSelect',
  data() {
    return {
      name: '',
      iconList: icons
    }
  },
  methods: {
    filterIcons() {
      this.iconList = this.name
        ? icons.filter(item => item.includes(this.name))
        : icons;
    },
    selectIcon(name) {
      this.$emit('selected', name)
      document.body.click(); // 关闭弹出框
    },
    reset() {
      this.name = ''
      this.iconList = icons
    }
  }
}
</script>

<style lang="scss" scoped>
.icon-body {
  width: 100%;
  padding: 10px;

  .icon-list {
    display: flex;
    flex-wrap: wrap;       /* 自动换行 */
    gap: 10px;             /* 图标间距 */
    max-height: 200px;
    overflow-y: auto;

    .icon-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      width: 60px;         /* 每个图标固定宽度 */
      cursor: pointer;
      padding: 5px;
      border-radius: 4px;
      transition: background-color 0.2s;

      &:hover {
        background-color: #f5f5f5;
      }

      .icon-svg {
        height: 32px;
        width: 32px;
        margin-bottom: 4px;
      }

      span {
        text-align: center;
        font-size: 12px;
        word-break: break-word;
      }
    }
  }
}
</style>
