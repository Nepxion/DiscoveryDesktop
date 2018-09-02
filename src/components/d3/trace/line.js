
import * as utils from '@/utils'
import * as d3 from 'd3'

class Line {
  constructor(params) {
    this.container = params.container
    this.id = utils.makeId()
    this.sourceItem = params.sourceItem
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
    let fromPortPosition = this._getPortPosition(this.sourceItem)
    let targetPosition = _targetPosition || this._getPortPosition(this.targetItem)
    let bezierY = this.targetItem.getItemHeight()
    let path = d3.path()
    path.moveTo(fromPortPosition.x, fromPortPosition.y)


    path.bezierCurveTo(fromPortPosition.x, fromPortPosition.y, targetPosition.x,
      targetPosition.y - bezierY, targetPosition.x, targetPosition.y - bezierY)
    this.path.attr("d", path)
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
    this.path = this.container
      .append("path")
      .attr('fill', 'none')
      .attr('stroke', '#ccc')
      .attr('stroke-width', '3')
      .lower()
    this.updatePath()
  }

  /**
   * 绑定事件
   * @private
   */
  _bindEvent() {
    this.path.on('click', this._onClick.bind(this));

    this.path.on("mouseover", function(d) {
      d3.select(this).style("stroke", "#999");
    }).on("mouseout", function(d) {
      d3.select(this).style("stroke", "#ccc");
    });
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
  _getPortPosition(item) {
    let delta = {
      x: item.getItemWidth()/2,
      y: item.getItemHeight()
    }
    return {
      x: item.x + delta.x,
      y: item.y + delta.y
    }
  }
}

export default Line
