
import * as d3 from '../d3';
import Node from './node';
import Group from './group';
import * as utils from '@/utils';

class Graph {
  constructor(selector) {
    this._padding = 40;
    this._x = 0;
    this._y = 0;

    this.dom = document.querySelector(selector)
    this.svg = d3.select(this.dom).append("svg");
    this.svgWidth = this.dom.clientWidth - this._padding;
    this.svgHeight = this.dom.clientHeight - this._padding;
    this.svg.attr("width", this.svgWidth).attr("height", this.svgHeight);

    this.tip = d3.tip().attr('class', 'd3-tip');
    this.svg.call(this.tip);

    this.data = {};
    this.list = {};
  }

  loadData(data) {
    this.data = data;
    Object.keys(data).forEach(key => {
      if (data[key].length > 1) {
        this.addGroup(key, data[key]);
      } else {
        const node = data[key][0];
        this.addNode(node);
      }
    });
  }

  addGroup(group,child) {
    let item = new Group({
      svg: this.svg,
      tip: this.tip,
      svgWidth: this.svgWidth,
      svgHeight: this.svgHeight,
      id: child.id || utils.makeId(),
      x: child.x || this._x,
      y: child.y || this._y,
      title: group,
      child: child,
    });
    let l=item.width + this._padding;
    this._x = this._x + l;
    if (this._x > this.svgWidth-l) {
      this._x = 0;
      this._y = this._y + item.height + this._padding;
    }

    this.list[item.id] = item;
  }

  addNode(params) {
    let item = new Node({
      svg: this.svg,
      tip: this.tip,
      svgWidth: this.svgWidth,
      svgHeight: this.svgHeight,
      id: params.id || utils.makeId(),
      x: params.x || this._x,
      y: params.y || this._y,
      title: params.serviceId,
      params: params,
    });
    let l=item.width + this._padding;
    this._x = this._x + l;
    if (this._x > this.svgWidth-l) {
      this._x = 0;
      this._y = this._y + item.height + this._padding;
    }

    this.list[item.id] = item;
  }
}

export default Graph;
