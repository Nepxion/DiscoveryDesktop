
import * as d3 from 'd3';
import Span from './span';
import * as utils from '@/utils';
import Line from "./line";
import _ from 'loadsh';

class Trace {
  constructor(selector) {
    this._padding = 40;
    this._x = 0;
    this._y = 0;

    this.dom = document.querySelector(selector)
    this.svg = d3.select(this.dom).append("svg");
    this.svgWidth = this.dom.clientWidth - this._padding;
    this.svgHeight = this.dom.clientHeight - this._padding;
    this.svg.attr("width", this.svgWidth).attr("height", this.svgHeight);

    this.data = [];
    this.list = {};
    this.lineList = {}
  }

  loadData(data) {
    this.data = data;
    data.forEach((span, i) => {
      this.addSpan(span);
    });
    _.forEach(this.list, fromItem => {
      fromItem.outputIds.forEach(outputId => {
        let targetItem = this.list[outputId]
        let line = this._addLine(fromItem, 'output', targetItem, 'input')
        line.updatePath();
        line.fromItem['outputPathIds'].add(line.id);
        line.targetItem['inputPathIds'].add(line.id);
        this.lineList[line.id] = line;
      })
    });
  }

  addSpan(params) {
    let item = new Span({
      svg: this.svg,
      svgWidth: this.svgWidth,
      svgHeight: this.svgHeight,
      id: params.id || utils.makeId(),
      x: params.x || this._x,
      y: params.y || this._y,
      title: params.title,
      inputIds: params.inputIds,
      outputIds: params.outputIds,
      onDrag: this._onItemDrag.bind(this),
      onClick: this._onItemClick.bind(this),
    });
    let l=item.width + this._padding;
    this._x = this._x + l;
    if (this._x > this.svgWidth-l) {
      this._x = 0;
      this._y = this._y + item.height + this._padding;
    }

    this.list[item.id] = item;
  }

  /**
   * 创建连线
   * @param fromItem
   * @param portType
   * @returns {Line}
   * @private
   */
  _addLine(fromItem, fromPortType, targetItem, targetPortType) {
    return new Line({
      container: this.svg,
      fromItem: fromItem,
      fromPortType: fromPortType,
      targetItem: targetItem,
      targetPortType: targetPortType,
      onClick: this._onLineClick.bind(this)
    })
  }

  /**
   * 元素移动回调事件
   * @param item
   */
  _onItemDrag(item) {
    if(item.inputPathIds.size) {
      item.inputPathIds.forEach(id => {
        this.lineList[id].updatePath()
      })
    }

    if(item.outputPathIds.size) {
      item.outputPathIds.forEach(id => {
        this.lineList[id].updatePath()
      })
    }
  }

  /**
   * 连线点击回调事件
   * @param line 连线实例对象
   * @private
   */
  _onLineClick(line) {

  }

  /**
   * 元素点击回调事件
   * @param item 元素实例对象
   * @private
   */
  _onItemClick(span) {

  }
}

export default Trace;
