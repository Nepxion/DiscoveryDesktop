
import * as d3 from 'd3';
import * as utils from '@/utils';

class Span {
  constructor(params) {
    this.svg = params.svg;
    this.svgWidth = params.svgWidth;
    this.svgHeight = params.svgHeight;
    this.id = params.id || utils.makeId();
    this.x = params.x;
    this.y = params.y;
    this.title = params.title;

    this.width = 150;
    this.height = 40;

    // 记录input/output元素的id和连线对象
    this.inputPathIds = new Set();
    this.outputPathIds = new Set();

    // 私有属性
    this._dragDeltaX = 0;
    this._dragDeltaY = 0;

    this.depth=params.depth||0;
    this.parent=params.parent||null;
    this.children=params.children||null;


    // 回调事件
    this.onDrag = params.onDrag;
    this.onClick = params.onClick;

    this._createElement();
    this._bindEvent();
  }

  getItemWidth() {
    return this.width;
  }

  getItemHeight() {
    return this.height;
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
      .attr('style', 'cursor: move')
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
      .attr('fill', '#fff')
      .attr('stroke-width', '0');

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

    group.append('image')
      .attr('href', require('../images/normal.svg'))
      .attr('x', this.width - 20)
      .attr('y', 12)
      .attr('width', 16)
      .attr('height', 16);

    this._group = group;
  }

  /**
   * 绑定事件
   * @private
   */
  _bindEvent() {
    let drag = d3.drag()
      .on("start", this._onGroupDragStart.bind(this))
      .on("drag", this._onGroupDrag.bind(this));
    this._group.call(drag);

    this._group.on("mouseover", function(d) {
      d3.select(this).select("rect").style("stroke", "#3393db");
    }).on("mouseout", function(d) {
      d3.select(this).select("rect").style("stroke", "#169ce4");
    });
  }

  /**
   * 开始拖拽
   * @private
   */
  _onGroupDragStart() {
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
    this.onDrag(this);
  }


  /**
   * 点击当前元素
   */
  _onClick() {
    d3.event.stopPropagation();
    this.onClick(this);
  }
}

export default Span;
