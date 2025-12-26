// requireIcons.js
// 自动导入 svg 文件夹下的所有 SVG 图标名称

// 导入 context
const req = require.context('../../assets/icons/svg', false, /\.svg$/);

// 获取所有文件名
const requireAll = (requireContext) => requireContext.keys();

// 正则匹配文件名，例如 './icon-name.svg' → 'icon-name'
const re = /\.\/(.*)\.svg$/;

// 生成图标数组
const icons = requireAll(req).map((i) => {
  const match = i.match(re);
  return match ? match[1] : '';
}).filter(Boolean); // 过滤掉空字符串

export default icons;
