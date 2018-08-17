
let type = {
  INPUT: 'INPUT',
  FUNCTION: 'FUNCTION',
  ACTION: 'ACTION'
}

let bgColor = {
  INPUT: '#169ce4',
  FUNCTION: '#8fbb34',
  ACTION: '#ffc539'
}

let icon = {
  DEVICE: require('./images/report.png'),
  CUSTOM_DATA: require('./images/online.png'),
  LOGIC_RULE: require('./images/condition.png'),
  ARITHMETIC: require('./images/caculator.png'),
  APP: require('./images/post.png'),
  HTTP: require('./images/http.png'),
  EMAIL: require('./images/email.png')
}

let name = {
  DEVICE: '设备数据',
  CUSTOM_DATA: '自定义数据',
  LOGIC_RULE: '逻辑规则',
  ARITHMETIC: '四则运算',
  APP: 'APP推送',
  HTTP: 'HTTP请求',
  EMAIL: '邮件'
}

let portDelta = {
  INPUT_X: 0,
  Y: 15
}

export { type, bgColor, icon, name, portDelta }
