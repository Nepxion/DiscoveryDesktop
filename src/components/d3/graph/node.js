
import * as d3 from '../d3';
import * as utils from '@/utils';

class Node {
  constructor(params) {
    this.params = params.params;
    this.svg = params.svg;
    this.tip = params.tip;

    this.svgWidth = params.svgWidth;
    this.svgHeight = params.svgHeight;
    this.id = params.id || utils.makeId();
    this.x = params.x;
    this.y = params.y;
    this.title = params.title;
    this.drag = params.drag;

    this.width = 150;
    this.height = 40;

    this._imgWidth = 30;

    // 私有属性
    this._group = null;
    this._dragDeltaX = 0;
    this._dragDeltaY = 0;

    this._checked = false;

    this.width = utils.getTextWidth(this.title)+this._imgWidth+40;
    //console.log(itemWidth);

    // 回调事件
    this.onClick = params.onClick;

    this._createElement();
    this._bindEvent();
  }

  /**
   * 创建svg元素
   */
  _createElement() {
    let group = this.svg.append('g')
      .attr('transform', `translate(${this.x}, ${this.y})`);

    group.append('rect')
      .attr('width', this.width)
      .attr('height', this.height)
      .attr('rx', 5)
      .attr('rx', 5)
      .attr('fill', '#fff')
      .attr('stroke', '#169ce4')
      .attr('stroke-width', '1');

    let iconGroup = group.append('g');
    iconGroup.append('rect')
      .attr('x', 0)
      .attr('y', 0)
      .attr('rx', 5)
      .attr('rx', 5)
      .attr('width', 6)
      .attr('height', this.height)
      .attr('fill', '#169ce4')
      .attr('stroke', '#169ce4')
      .attr('stroke-width', '1');

    iconGroup.append('rect')
      .attr('x', 4)
      .attr('y', 1)
      .attr('width', 30)
      .attr('height', this.height - 2)
      .attr('fill', '#fff');

    iconGroup.append('image')
      .attr('href', 'https://g.alicdn.com/aliyun/ros/1.5.4/styles/icons/ecs.svg')
      .attr('x', 8)
      .attr('y', 8)
      .attr('width', 22)
      .attr('height', 22);

    let iconLine = d3.path()
    iconLine.moveTo(34, 0)
    iconLine.lineTo(34, this.height)
    iconGroup.append('path')
      .attr('stroke-width', 0.5)
      .attr('stroke', '#000')
      .attr('stroke-opacity', 0.1)
      .attr('d', iconLine);

    group.append('text')
      .text(this.title)
      .attr('x', 43)
      .attr('y', 20)
      .attr('dy', '0.35em')
      .attr('text-anchor', 'start');

    let circle = group.append('g').attr('transform',`translate(${this.width - 20}, 20)`);
    circle.append("circle")
      .attr("r", 18)
      .attr('stroke-width', 2)
      .attr('stroke', '#169ce4')
      .attr('fill', '#fff');

    circle.append('text')
      .text(this.params.version)
      .attr('x', 0)
      .attr('y', 0)
      .attr('dy', '0.35em')
      .attr('style', 'font-size: 10px;')
      .attr('text-anchor', 'middle');

    // group.append('image')
    //   .attr('href', require('../images/normal.svg'))
    //   .attr('x', this.width - 20)
    //   .attr('y', 12)
    //   .attr('width', 16)
    //   .attr('height', 16);

    this._group = group;
  }

  /**
   * 绑定事件
   * @private
   */
  _bindEvent() {
    this._group.on('click', this._onClick.bind(this));
    if(!this.drag){
      let drag = d3.drag()
        .on("start", this._onGroupDragstart.bind(this))
        .on("drag", this._onGroupDrag.bind(this));
      this._group.call(drag);
    }

    // this._group.on('mouseover', this.tip.html(function(d) { return 'ok'; }).show())
    //   .on('mouseout', this.tip.hide())

    this._group.on('mouseover', this.tip
      .html('host:'+ this.params.host+'<br />'+'port:'+this.params.port)
      .show)
      .on('mouseout',  this.tip.hide)
  }

  /**
   * 点击事件
   * @private
   */
  _onClick() {
    d3.event.stopPropagation();
    this.onClick(this);
  }

  /**
   * 选中
   */
  focus() {
    this._group.classed('active', true)
    this._checked = true;
  }

  /**
   * 取消选中
   */
  blur() {
    this._group.classed('active', false)
    this._checked = false;
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
    if (this.x < 0) {
      this.x = 0
    }
    if (this.x > this.svgWidth - this.width) {
      this.x = this.svgWidth - this.width;
    }
    if (this.y < 0) {
      this.y = 0
    }
    if (this.y > this.svgHeight - this.height) {
      this.y = this.svgHeight - this.height;
    }
    this._group.raise().attr('transform', `translate(${this.x}, ${this.y})`);
  }
}

export default Node;
