
import * as utils from '@/utils'
import * as d3 from 'd3'

class Item {
  constructor(params) {
    this.container = params.container
    this.id = params.id || utils.makeId()
    this.x = params.x
    this.y = params.y
    this.name = params.name
    this.title = params.title

    // 记录input/output元素的id和连线对象
    this.inputIds = params.inputIds || new Set()
    this.outputIds = params.outputIds || new Set()
    this.inputPathIds = new Set()
    this.outputPathIds = new Set()
    this.data = params.data || null

    // 回调事件
    this.onDrag = params.onDrag
    this.onClick = params.onClick
    this.onDblclick = params.onDblclick
    this.onRemove = params.onRemove
    this.onMouseup = params.onMouseup
    this.onPortMousedown = params.onPortMousedown
    this.onPortMouseup = params.onPortMouseup

    // 私有属性
    this._group = null
    this._text = null
    this._input = null
    this._output = null
    this._dragDeltaX = 0
    this._dragDeltaY = 0

    this._createElement()
    this._createGroup()
    this._createGroupOpen()

    this._bindEvent()
  }

  /**
   * 删除元素
   */
  remove() {
    this._group.remove()
    this.onRemove(this)
  }

  /**
   * 取消选中
   */
  blur() {
    this._group.classed('active', false)
  }

  /**
   * 获取input具柄
   * @returns {null|*}
   */
  getInputPort() {
    return this._input
  }

  /**
   * 获取output具柄
   * @returns {null|*}
   */
  getOutputPort() {
    return this._output
  }

  /**
   * 更新元素
   * @param params
   */
  updateItem(params) {
    let textWidth = utils.getTextWidth(params.text) + 15
    let itemWidth = textWidth + 30
    this._group.select('rect').attr('width', itemWidth)
    this.title = params.text
    this._text.text(this.title).attr('x', textWidth / 2 + 30)
    if(this._output) {
      this._output.attr('transform', `translate(${itemWidth - 5}, 10)`)
    }
    this.onDrag(this)
  }

  getItemWidth() {
    return parseInt(this._group.select('rect').attr('width'))
  }

  /**
   * 创建svg元素
   */
  _createElement() {
    let group = this.container.append('g')
      .attr('transform', `translate(${this.x}, ${this.y})`)
      .attr('class', 'item')
    this._group = group
    group.append('rect')
      .attr('width', 150)
      .attr('height', 40)
      .attr('rx', 5)
      .attr('rx', 5)
      .attr('fill', '#fff')
      .attr('stroke', '#169ce4')
      .attr('stroke-width', '1')
      .attr('class', 'item item-rect')

    let iconGroup = group.append('g')
    iconGroup.append('rect')
      .attr('x', 0)
      .attr('y', 0)
      .attr('rx', 5)
      .attr('rx', 5)
      .attr('width', 6)
      .attr('height', 40)
      .attr('fill', '#169ce4')
      .attr('stroke', '#169ce4')
      .attr('stroke-width', '1')

    iconGroup.append('rect')
      .attr('x', 4)
      .attr('y', 1)
      .attr('width', 30)
      .attr('height', 38)
      .attr('fill', '#fff')

    iconGroup.append('image')
      .attr('href', 'https://g.alicdn.com/aliyun/ros/1.5.4/styles/icons/ecs.svg')
      .attr('x', 8)
      .attr('y', 8)
      .attr('width', 22)
      .attr('height', 22)

    let iconLine = d3.path()
    iconLine.moveTo(34, 0)
    iconLine.lineTo(34, 40)
    iconGroup.append('path')
      .attr('stroke-width', 0.5)
      .attr('stroke', '#000')
      .attr('stroke-opacity', 0.1)
      .attr('d', iconLine)

    let text = group.append('text')
    let title = this.name
    this._text = text
    text.text(title)
      .attr('x', 43)
      .attr('y', 20)
      .attr('dy', '0.35em')
      .attr('class', 'item_label')
      .attr('text-anchor', 'start')


    group.append('image')
      .attr('href', 'https://gw.alipayobjects.com/zos/rmsportal/MXXetJAxlqrbisIuZxDO.svg')
      .attr('x', 130)
      .attr('y', 12)
      .attr('width', 16)
      .attr('height', 16)

    // if(~['ACTION', 'FUNCTION'].indexOf(this.type)) {
    //   let input = group.append('g').attr('transform', 'translate(-5, 10)').attr('class', 'port_input')
    //   this._input = input
    //   input.append('rect')
    //     .attr('rx', 3)
    //     .attr('ry', 3)
    //     .attr('width', 10)
    //     .attr('height', 10)
    //     .attr('class', 'port')
    // }
    //
    // if(~['INPUT', 'FUNCTION'].indexOf(this.type)) {
    //   let output = group.append('g').attr('transform', 'translate(115, 10)').attr('class', 'port_output')
    //   this._output = output
    //   output.append('rect')
    //     .attr('rx', 3)
    //     .attr('ry', 3)
    //     .attr('width', 10)
    //     .attr('height', 10)
    //     .attr('class', 'port')
    // }
  }

  _createGroup() {
    let group = this.container.append('g')
      .attr('transform', `translate(180, 0)`)
      .attr('class', 'item')
    this._group = group
    group.append('rect')
      .attr('width', 150)
      .attr('height', 40)
      .attr('rx', 5)
      .attr('rx', 5)
      .attr('fill', '#fff')
      .attr('stroke', '#169ce4')
      .attr('stroke-width', '1')
      .attr('class', 'item item-rect')

    let iconGroup = group.append('g')

    iconGroup.append('rect')
      .attr('x', 0)
      .attr('y', 0)
      .attr('width', 30)
      .attr('height', 40)
      .attr('fill', 'none')

    iconGroup.append('image')
      .attr('href', 'https://gw.alipayobjects.com/zos/rmsportal/czNEJAmyDpclFaSucYWB.svg')
      .attr('x', 8)
      .attr('y', 8)
      .attr('width', 22)
      .attr('height', 22)

    let iconLine = d3.path()
    iconLine.moveTo(34, 0)
    iconLine.lineTo(34, 40)
    iconGroup.append('path')
      .attr('stroke-width', 0.5)
      .attr('stroke', '#000')
      .attr('stroke-opacity', 0.1)
      .attr('d', iconLine)

    let text = group.append('text')
    let title = this.name
    this._text = text
    text.text('群组(折叠)')
      .attr('x', 43)
      .attr('y', 20)
      .attr('dy', '0.35em')
      .attr('class', 'item_label')
      .attr('text-anchor', 'start')


    group.append('image')
      .attr('href', require('./images/minus.svg'))
      .attr('x', 130)
      .attr('y', 12)
      .attr('width', 16)
      .attr('height', 16)
  }

  _createGroupOpen() {
    let group = this.container.append('g')
      .attr('transform', `translate(350, 0)`)
      .attr('class', 'item')
    this._group = group
    group.append('rect')
      .attr('width', 200)
      .attr('height', 200)
      .attr('rx', 5)
      .attr('rx', 5)
      .attr('fill', '#fff')
      .attr('stroke', '#169ce4')
      .attr('stroke-width', '1')
      .attr('class', 'item item-rect')

    let iconGroup = group.append('g')

    iconGroup.append('rect')
      .attr('x', 0)
      .attr('y', 0)
      .attr('width', 30)
      .attr('height', 40)
      .attr('fill', 'none')

    iconGroup.append('image')
      .attr('href', 'https://gw.alipayobjects.com/zos/rmsportal/czNEJAmyDpclFaSucYWB.svg')
      .attr('x', 8)
      .attr('y', 8)
      .attr('width', 22)
      .attr('height', 22)

    let iconLine = d3.path()
    iconLine.moveTo(34, 0)
    iconLine.lineTo(34, 40)
    iconGroup.append('path')
      .attr('stroke-width', 0.5)
      .attr('stroke', '#000')
      .attr('stroke-opacity', 0.1)
      .attr('d', iconLine)

    let text = group.append('text')
    let title = this.name
    this._text = text
    text.text('群组(展开)')
      .attr('x', 43)
      .attr('y', 20)
      .attr('dy', '0.35em')
      .attr('class', 'item_label')
      .attr('text-anchor', 'start')


    group.append('image')
      .attr('href', require('./images/minus.svg'))
      .attr('x', 170)
      .attr('y', 12)
      .attr('width', 16)
      .attr('height', 16)


    let groupNode = group.append('g')
      .attr('transform', `translate(25, 40)`)

    let node1 = groupNode.append('g')
    this._createNode(node1);


    let node2 = groupNode.append('g')
      .attr('transform', `translate(0, 50)`)
    this._createNode(node2);

  }

  _createNode(group) {
    group.append('rect')
      .attr('width', 150)
      .attr('height', 40)
      .attr('rx', 5)
      .attr('rx', 5)
      .attr('fill', '#fff')
      .attr('stroke', '#169ce4')
      .attr('stroke-width', '1')
      .attr('class', 'item item-rect')

    let iconGroup = group.append('g')
    iconGroup.append('rect')
      .attr('x', 0)
      .attr('y', 0)
      .attr('rx', 5)
      .attr('rx', 5)
      .attr('width', 6)
      .attr('height', 40)
      .attr('fill', '#169ce4')
      .attr('stroke', '#169ce4')
      .attr('stroke-width', '1')

    iconGroup.append('rect')
      .attr('x', 4)
      .attr('y', 1)
      .attr('width', 30)
      .attr('height', 38)
      .attr('fill', '#fff')

    iconGroup.append('image')
      .attr('href', 'https://g.alicdn.com/aliyun/ros/1.5.4/styles/icons/ecs.svg')
      .attr('x', 8)
      .attr('y', 8)
      .attr('width', 22)
      .attr('height', 22)

    let iconLine = d3.path()
    iconLine.moveTo(34, 0)
    iconLine.lineTo(34, 40)
    iconGroup.append('path')
      .attr('stroke-width', 0.5)
      .attr('stroke', '#000')
      .attr('stroke-opacity', 0.1)
      .attr('d', iconLine)

    let text = group.append('text')
    let title = this.name
    this._text = text
    text.text(title)
      .attr('x', 43)
      .attr('y', 20)
      .attr('dy', '0.35em')
      .attr('class', 'item_label')
      .attr('text-anchor', 'start')


    group.append('image')
      .attr('href', 'https://gw.alipayobjects.com/zos/rmsportal/MXXetJAxlqrbisIuZxDO.svg')
      .attr('x', 130)
      .attr('y', 12)
      .attr('width', 16)
      .attr('height', 16)
  }

  /**
   * 绑定事件
   * @private
   */
  _bindEvent() {
    this._group.on('click', this._onClick.bind(this))
    this._group.on('dblclick', this._onDblclick.bind(this))
    this._group.on('mouseup', this._onMouseup.bind(this))
    if(this._input) {
      this._input.on('mouseenter', this._onPortEnter)
      this._input.on('mouseleave', this._onPortLeave)
      this._input.on('mousedown', this._onPortMousedown.bind(this))
      this._input.on('mouseup', this._onPortMouseup.bind(this))
    }
    if(this._output) {
      this._output.on('mouseenter', this._onPortEnter)
      this._output.on('mouseleave', this._onPortLeave)
      this._output.on('mousedown', this._onPortMousedown.bind(this))
      this._output.on('mouseup', this._onPortMouseup.bind(this))
    }
    let drag = d3.drag()
      .on("start", this._onGroupDragstart.bind(this))
      .on("drag", this._onGroupDrag.bind(this))
    this._group.call(drag)

  }

  /**
   * 鼠标进入连线具柄
   * @private
   */
  _onPortEnter() {
    d3.select(this).select('rect').classed('port-hovered', true)
  }

  /**
   * 鼠标离开连线具柄
   * @private
   */
  _onPortLeave() {
    d3.select(this).select('rect').classed('port-hovered', false)
  }

  /**
   * 鼠标按下连线具柄
   * @private
   */
  _onPortMousedown() {
    d3.event.stopPropagation()
    let portType = d3.select(d3.event.target.parentNode).attr('class').replace('port_', '')
    this.onPortMousedown(this, portType)
  }

  /**
   * 鼠标按钮抬在线具柄抬起
   * @private
   */
  _onPortMouseup() {
    d3.event.stopPropagation()
    let portType = d3.select(d3.event.target.parentNode).attr('class').replace('port_', '')
    this.onPortMouseup(this, portType)
  }

  /**
   * 开始拖拽
   * @private
   */
  _onGroupDragstart() {
    this._dragDeltaX = d3.event.x - this.x;
    this._dragDeltaY = d3.event.y - this.y;
  }

  /**
   * 正在拖拽
   * @private
   */
  _onGroupDrag() {
    this.x = d3.event.x - this._dragDeltaX;
    this.y = d3.event.y - this._dragDeltaY;
    this._group.raise().attr('transform', `translate(${this.x}, ${this.y})`)
    this.onDrag(this)
  }

  /**
   * 点击当前元素
   */
  _onClick() {
    d3.event.stopPropagation()
    this._group.classed('active', true)
    this.onClick(this)
  }

  /**
   * 双击当前元素
   */
  _onDblclick() {
    d3.event.stopPropagation()
    this.onDblclick(this)
  }

  /**
   * 鼠标按钮在元素位置抬起
   * @private
   */
  _onMouseup() {
    d3.event.stopPropagation()
    this.onMouseup(this)
  }
}

export default Item
