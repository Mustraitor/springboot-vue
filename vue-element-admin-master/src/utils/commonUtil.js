const baseURL = process.env.VUE_APP_BASE_API
//日期格式化
export function parseTime(time, pattern){
  if (arguments.length === 0 || !time){
    return null
  }
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object'){
    date = time
  }else{
    if ((typeof time === 'string') && (/^[0-9]+$/.test(time))){
      time =parselnt(time)
    }
    if ((typeof time === 'number') && (time.toString().length === 10)){
      time =time * 1000
    }
    date = new Date(time)
  }
  const formatObj ={
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g,(result, key) => {
    let value = formatObj[key]
    if(key === 'a'){
      return ['日','-', '=','三','四','五','六'][value]
    }
    if(result.length > 0 && value < 10){
      value ='0' + value
    }
    return value || 0
  })
  return time_str
}
// 表单重置
export function resetForm(refName){
  if(this.$refs[refName]){
    this.$refs[refName].resetFields()
  }
}
//回显数据字典
// export function selectDictLabel(datas, value){
//   var actions=[]
//   Object.keys(datas).map((key)=>{
//     if(datas[key].dictValue === (''+value)){
//       actions.push(datas[key].dictLabel)
//       return false
//     }
//   })
// }
export function selectDictLabel(datas, value){
  if(!datas) return '';
  const item = datas.find(d => d.dictValue === ('' + value));
  return item ? item.dictLabel : '';
}
// 通用下载方法
export function download(fileName){
  window.location.href = baseURL + '/common/download?fileName=' + encodeURI(fileName) +'&delete='+ true
}

//转换字符串，将undefined、null 等转化为 ""
export function praseStrEmpty(str){
  if (!str ||str === 'undefined' || str=== 'null'){
    return ''
  }
  return str
}

export function handleTree(data, id, parentId, children, rootId){
  id = id || 'id'
  parentId = parentId || 'parentId'
  children = children || 'children'
  rootId = rootId || 0
  const cloneData = JSON.parse(JSON.stringify(data))
  const treeData = cloneData.filter(father => {
    const branchArr = cloneData.filter(child => {
      return father[id] === child[parentId]
    })
    branchArr.length > 0? father.children = branchArr: ''
    return father[parentId] === rootId

  })
  return treeData !== ''? treeData: data

}
