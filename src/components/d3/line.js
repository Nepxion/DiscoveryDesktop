
import { portDelta } from './enum'
import * as utils from '@/utils'
import * as d3 from 'd3'

class Line {
  constructor(params) {
    this.container = params.container
    this.id = utils.makeId()
    this.fromPortType = params.fromPortType
    this.fromItem = params.fromItem
    this.targetPortType = params.targetPortType
    this.targetItem = params.targetItem
    this.path = null

    // 回调事件
    this.onClick = params.onClick

    this._createElement()
    this._bindEvent()
  }

  /**
   * 绘制连线
   * @param _targetPosition 目标坐标，如果targetPosition未传直接使用targetPort的坐标
   */
  updatePath(_targetPosition) {
    let fromPortPosition = this._getPortPosition(this.fromPortType, this.fromItem)
    let targetPosition = _targetPosition || this._getPortPosition(this.targetPortType, this.targetItem)
    let path = d3.path()
    let deltaX = fromPortPosition.x - targetPosition.x
    let bezierX = deltaX * 0.5
    if((this.fromPortType === 'input' && deltaX > 0) || (this.fromPortType === 'output' && deltaX < 0)) {
      bezierX = - bezierX
    }
    path.moveTo(fromPortPosition.x, fromPortPosition.y)
    path.bezierCurveTo(fromPortPosition.x + bezierX, fromPortPosition.y, targetPosition.x - bezierX, targetPosition.y, targetPosition.x, targetPosition.y)
    this.path.attr("d", path)
  }

  /**
   * 删除连线
   */
  remove() {
    this.path.remove()
    if(this.fromItem && this.targetItem) {
      this.fromItem[this.fromPortType + 'Ids'].delete(this.targetItem.id)
      this.fromItem[this.fromPortType + 'PathIds'].delete(this.id)
      this.targetItem[this.targetPortType + 'Ids'].delete(this.fromItem.id)
      this.targetItem[this.targetPortType + 'PathIds'].delete(this.id)
    }
  }

  /**
   * 取消选中
   */
  blur() {
    this.path.classed('active', false)
  }

  /**
   * 创建线条元素
   * @private
   */
  _createElement() {
    this.path = this.container.append("path").attr('class', 'line').lower()
  }

  /**
   * 绑定事件
   * @private
   */
  _bindEvent() {
    this.path.on('click', this._onClick.bind(this))
  }

  /**
   * 点击事件
   * @private
   */
  _onClick() {
    d3.event.stopPropagation()
    this.path.classed('active', true)
    this.onClick(this)
  }

  /**
   * 获取具柄的坐标
   * @param type 具柄类型（input/output）
   * @param item 元素对象
   * @returns {{x: *, y: *}}
   * @private
   */
  _getPortPosition(type, item) {
    let delta = {
      x: type === 'input' ? portDelta.INPUT_X : item.getItemWidth(),
      y: portDelta.Y
    }
    return {
      x: item.x + delta.x,
      y: item.y + delta.y
    }
  }
}

export default Line
