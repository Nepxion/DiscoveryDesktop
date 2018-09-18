
import * as d3 from '../d3';
import Node from './node';
import Group from './group';
import * as utils from '../../../utils';
import console from "../../../store/modules/console";

class Graph {
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

    this.tip = d3.tip().attr('class', 'd3-tip');
    this.svg.call(this.tip);

    this.data = {};
    this.list = {};
    this.selectedNode = null;
    this.onNodeChecked = null;
  }

  loadData(data) {
    try {
      this.data = data;
      const keys = Object.keys(data);
      keys.forEach(key => {
        if (data[key].length > 1) {
          this.addGroup(key, data[key]);
        } else {
          const node = data[key][0];
          if (node) {
            this.addNode(node);
          }
        }
      });
    } catch (e) {
    }
  }

  addGroup(group, child) {
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
      onNodeClick: this._onNodeClick.bind(this),
    });
    let l = item.width + this._padding;
    this._x = this._x + l;
    if (this._x > this.svgWidth - l) {
      this._x = 0;
      this._y = this._y + item.height + this._padding;
      if(this._y>this.svgHeight) {
        this.svgHeight = this._y + this._padding;
        this.svg.attr("height", this.svgHeight);
      }
    }

    this.list[item.id] = item;
  }

  addNode(params) {
    try {
      let item = new Node({
        svg: this.svg,
        tip: this.tip,
        svgWidth: this.svgWidth,
        svgHeight: this.svgHeight,
        id: utils.makeId(),
        x: params.x || this._x,
        y: params.y || this._y,
        title: params.serviceId,
        params: params,
        onClick: this._onNodeClick.bind(this),
      });
      let l = item.width + this._padding;
      this._x = this._x + l;
      if (this._x > this.svgWidth - l) {
        this._x = 0;
        this._y = this._y + item.height + this._padding;
      }

      this.list[item.id] = item;

    } catch (e) {
    }
  }

  /**
   * 节点点击事件
   * @private
   */
  _onNodeClick(node) {
    if (node === this.selectedNode) return;
    if (node.params) {
      const isPlugin = utils.isPlugin(node.params);
      if (isPlugin) {
        if (this.selectedNode) {
          this.selectedNode.blur();
        }
        this.selectedNode = node;
        this.selectedNode.focus();
        this.onNodeChecked && this.onNodeChecked(node.params);//触发节点回调
      }
    }
  }
}

export default Graph;
