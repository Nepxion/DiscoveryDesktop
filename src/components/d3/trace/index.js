
import * as d3 from 'd3';
import Span from './span';
import * as utils from '@/utils';
import Line from "./line";

class Trace {
  constructor(selector) {
    this._padding = 40;
    this._x = 0;
    this._y = 0;

    this.dom = document.querySelector(selector) || document.querySelector('body');
    while (this.dom.firstChild) {
      this.dom.removeChild(this.dom.firstChild);
    }
    this.svg = d3.select(this.dom).append("svg");
    this.svgWidth = this.dom.clientWidth - this._padding;
    this.svgHeight = this.dom.clientHeight - this._padding;
    this.svg.attr("width", this.svgWidth).attr("height", this.svgHeight);


    this.treemap = d3.tree()
      .size([this.svgWidth - this._padding, this.svgHeight - this._padding]);

    this.data = [];
    this.list = {};
    this.lineList = {}
  }

  loadData(data) {
    this.data = this._addKey(data);

    var nodes = d3.hierarchy(this.data);
    nodes = this.treemap(nodes);
    var lines = nodes.links();
    var descendants = nodes.descendants();

    descendants.forEach((span, i) => {
      this.addSpan(span);
    });

    lines.forEach((line, i) => {
      const weight=line.target.data.weight;
      this._addLine(line.source,line.target,weight);
    });
  }

  addSpan(params) {
    let item = new Span({
      svg: this.svg,
      svgWidth: this.svgWidth,
      svgHeight: this.svgHeight,
      x: params.x,
      y: params.y,
      id: params.data.id || utils.makeId(),
      title: params.data.serviceId,
      onDrag: this._onItemDrag.bind(this),
      onClick: this._onItemClick.bind(this),

      params: params,
      depth: params.depth,
    });

    this.list[item.id] = item;
  }

  _addKey(obj) {
    if (typeof obj === 'object') {
      obj.id = obj.id || utils.makeId();
      if (obj.children) {
        obj.children = obj.children.map(item => {
          return this._addKey(item);
        })
      }
      return obj;
    }
  }
  /**
   * 创建连线
   * @param sourceItem
   * @param targetItem
   * @returns {Line}
   * @private
   */
  _addLine(sourceItem, targetItem, weight) {
    const sourceId = sourceItem.data.id;
    const targetId = targetItem.data.id;
    let line = new Line({
      container: this.svg,
      sourceItem: this.list[sourceId],
      targetItem: this.list[targetId],
      title: weight,
      onClick: this._onLineClick.bind(this)
    })
    this.list[sourceId]['outputPathIds'].add(line.id);
    this.list[targetId]['inputPathIds'].add(line.id);

    this.lineList[line.id] = line;
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

  destroy() {
    if (this.svg) {
      while (this.dom.firstChild) {
        this.dom.removeChild(this.dom.firstChild);
      }
    }
  }
}

export default Trace;
