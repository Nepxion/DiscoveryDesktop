
import * as utils from '@/utils'
import * as d3 from 'd3'

class Line {
  constructor(params) {
    this.container = params.container
    this.id = utils.makeId()
    this.sourceItem = params.sourceItem
    this.targetItem = params.targetItem
    this.text = null
    this.path = null
    this.title = params.title

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
    this.path.attr("d", path);

    if (this.title && this.title !== '-1') {
      const txtWidth = utils.getTextWidth(this.title)/2;
      const location = utils.getLineCentre(path);
      this.text.attr('x', location.x - txtWidth)
        .attr('y', location.y);
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
    const group = this.container.append('g');
    this.path = group.append("path")
      .attr('fill', 'none')
      .attr('stroke', '#ccc')
      .attr('stroke-width', '3')
      .lower();

    if (this.title && this.title !== '-1') {
      this.text = group.append('text')
        .text(this.title)
        .attr('x', 0)
        .attr('y', 0)
        .attr('dy', '0.35em')
        .attr('text-anchor', 'start');
    }
    this.updatePath();
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
