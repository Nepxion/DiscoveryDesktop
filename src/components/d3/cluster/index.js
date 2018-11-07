
import * as d3 from '../d3';
import * as utils from '../../../utils';
import {icon} from '../enum';

class Cluster {
  constructor(selector) {
    this._padding = 40;
    this.nodeWidth = 54;
    this.nodeHeight = 130;
    this.maxSize=1,this.zoomSize=1;

    this.dom = document.querySelector(selector) || document.querySelector('body');
    while (this.dom.firstChild) {
      this.dom.removeChild(this.dom.firstChild);
    }
    this.svg = d3.select(this.dom).append("svg");
    this.svgWidth = this.dom.clientWidth - this._padding;
    this.svgHeight = this.dom.clientHeight - this._padding;
    this.svg.attr("preserveAspectRatio", 'xMinYMin meet')
      .attr("viewBox", '0 0 ' + this.svgWidth + ' ' + this.svgHeight)
      .attr("width", this.svgWidth)
      .attr("height", this.svgHeight);

    this.g = this.svg.append("g")
      .attr("transform", "translate(" + this._padding + "," + this._padding + ")");
    this.node = this.g.selectAll(".node");
    this.link = this.g.selectAll(".link");
    this.linkText = this.g.selectAll(".linkText");
    this.linkBox = this.g.selectAll(".linkBox");
    this.nodeBox = this.g.selectAll(".nodeBox");

    this.root = null;

    this.zoom = d3.zoom()
      .scaleExtent([0.3, 10])
      .on("zoom", ()=>this.zoomed());

    this.drag = d3.drag()
      .on("start", (d)=>this.dragstarted(d))
      .on("drag", (d)=>this.dragged(d))
      .on("end", (d)=>this.dragended(d));

    this.svg.call(this.zoom)
    //.on("dblclick.zoom", null)
      .on("click", ()=>this.stopped(), true);

    //var tree = d3.cluster().size([height, width/2]); //集群图
    this.tree = d3.tree()
        .separation((a, b) => {
            if(a&&a.children&&a.children.length>this.maxSize){
              this.maxSize=a.children.length;
            }
            if(b&&b.children&&b.children.length>this.maxSize){
              this.maxSize=b.children.length;
            }
            return (a.parent == b.parent ? 1 : 2); })
        .size([this.svgHeight, this.svgWidth/2]); //树形图

    // tree.children = function(d) { return d.children; }

    this.selectedNode = null;
    this.onNodeChecked = null;

    this.strokeTime = null;
    this.dependsNode = null;
  }

  loadData(data) {
    if(data){
      this.maxSize=data&&data.children?data.children.length:1;
      this.root = d3.hierarchy(data);
      this.tree(this.root);
      const maxHeight=this.maxSize*(this.nodeHeight);
      if(maxHeight>this.svgHeight) {
        this.zoomSize = this.svgHeight / maxHeight;

        this.svgHeight = maxHeight;
        //svg.attr("height", height);
        this.tree.size([this.svgHeight, this.svgWidth / 2]);
        this.tree(this.root);

        //g.attr("transform","translate("+s+","+s+")scale("+zoomSize+")")
        //zoom.transform(g, d3.zoomTransform(0).translate(s,s));
        //zoom.scaleTo(svg,zoomSize);

        //判断自动缩放等级
        this.svg.call(this.zoom.transform, d3.zoomIdentity.translate(20, 0).scale(this.zoomSize));

        this.svg.call(this.zoom)
        //.on("dblclick.zoom", null)
          .on("click", ()=>this.stopped(), true);
      }

      this.updateData();
    }
  }

  updateData() {
    const nodes = this.root.descendants();
    //var links=this.root.descendants().slice(1);
    const links = this.root.links();

    this.linkBox = this.linkBox.data(links, function(d) {
      return d.id || utils.makeId();
    });
    this.linkBox.exit().remove();

    this.svg.append('defs').append('marker')
      .attr("id", "arrow")
      .attr("viewBox", "0 -10 20 20")
      .attr("refX", 0)
      .attr("refY", 0)
      .attr("markerWidth", 10)
      .attr("markerHeight", 10)
      .attr("orient", "auto")
      .append('svg:path')
      .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
      .attr('fill', '#3e78b7')
      .attr('stroke', 'none');

    this.linkBox = this.linkBox
      .enter().append("g");

    this.link = this.linkBox
      .append("path")
      .attr("id", (l) => {
        if(!l.id) l.id=utils.makeId();
        return "linkPath_"+l.id;
      })
      .attr("class", "link")
      .attr('fill', 'none')
      .attr('marker-end', 'url(#arrow)')
      .style('stroke', '#3e78b7')
      .style('stroke-width', 2)
      // .attr("d", d3.linkHorizontal()
      //   .x(function(d) {
      //     return d.y;
      //   })
      //   .y(function(d) {
      //     return d.x;
      //   }));
      .attr("d", (d) => {
          //var twidth = getTextWidth(d.source.data.name);
          return "M" + (d.source.y) + "," + d.source.x
            + "C" + (d.source.y + d.target.y) / 2 + "," + d.source.x
            + " " + (d.source.y + d.target.y) / 2 + "," + d.target.x
            + " " + (d.target.y-10)+ "," + d.target.x;
        });

    this.linkText=this.linkBox.append("text")
      .append("textPath")
      .attr("xlink:href", (l) => {
        return "#linkPath_"+l.id;
      })
      .style("text-anchor","middle")
      .attr("startOffset", "50%")
      .attr("class", "linkText")
      .text((l) => {
        if(l.target.data.weight&&l.target.data.weight>-1) {
          return 'weight='+l.target.data.weight;
        }
      });

    this.node = this.node.data(nodes, function(d) {
      return d.id || utils.makeId();
    });
    this.node.exit().remove();
    this.node = this.node
      .enter().append("g")
      .attr("class", (d) => {
        return "node" + (d.children && d.children.length > 0 ? " node--internal" : " node--leaf");
      })
      .attr("transform", (d) => {
        return "translate(" + d.y + "," + d.x + ")";
      })
      .on('mouseover', (d) => {
        this.highlightObject(d);
      })
      .on('mouseout', () => {
        this.highlightObject(null);
      })
      .on('click', (d) => {
        d3.event.stopPropagation();
        this._onNodeClick(d.data);
      })
      .on('dblclick', (d) => {
        d3.event.stopPropagation();
        if (!d._children && !d.children) {
          return false;
        }
        this.open(d);
      })
      .call(this.drag);

    this.nodeBox = this.node.append('rect')
      .attr("class", "nodeBox")
      .attr('width', this.nodeWidth)
      .attr('height', this.nodeWidth)
      .attr('rx', this.nodeWidth / 2)
      .attr('x', 0)
      .attr('y', -this.nodeWidth / 2)
      .attr('fill', '#3e414b')
      //.attr('stroke', '#515a6e')
      .attr('stroke', (d) => {
        if(this.selectedNode===d.data){
          return '#fbd044';
        } else {
          if (d.children && d.children.length > 0) {
            return '#515a6e';
          } else if (d._children && d._children.length > 0) {
            return '#d65048';
          } else {
            return '#515a6e';
          }
        }
      })
      .attr('stroke-width', (d) => {
        if (d._children && d._children.length > 0) {
          return '2'
        } else {
          return '1';
        }
      });

    this.node.append("image")
      .attr("class", "circle")
      .attr("xlink:href", (d) => {
        if (!d.parent) {
          return icon.colony;
        } else {
          if (!d.children && !d._children) {
            return icon.ecs;
          }
          else {
            return icon.topo;
          }
        }
      })
      .attr('x', 10)
      .attr("y", -this.nodeWidth / 2 + 10)
      .attr("width", this.nodeWidth - 20)
      .attr("height", this.nodeWidth - 20);

    const nodeText = this.node.append("text")
      .attr('x', this.nodeWidth / 2)
      .attr('y', this.nodeWidth - 10)
      //.attr("fill", "#ffffff")
      .attr("cursor", "default")
      .style("text-anchor", "middle")
      .text(null);

    nodeText.append('tspan')
      .attr('x', this.nodeWidth / 2)
      .attr('dy', 0).text((d) => {
      return d.data.name
    });

    nodeText.append('tspan')
      .text((d) => {
        if(d.data.contextPath){
          return d.data.host+":"+d.data.port;
        }
      })
      .attr('x', this.nodeWidth / 2)
      .attr('dy', 20);

    nodeText.append('tspan')
      .text((d) => {
        if(d.data.version){
          return "[V"+d.data.version+"]";
        }
      })
      .attr('x', this.nodeWidth / 2)
      .attr('dy', 20);

    nodeText.append('tspan')
      .text((d) => {
        if(d.data.region){
          return d.data.region
        }
      })
      .attr('x', this.nodeWidth / 2)
      .attr('dy', 20);

  }

  /**
   * 展开／闭合 children
   * @param d
   */
  open(d) {
    if(d){
      if(d.children&&d.children.length>0){
        d._children=d.children;
        d.children=null;
      }else{
        d.children=d._children;
        d._children=null;
      }
      this.updateData();
    }
  }

  dragstarted(d) {
    d.fx = d.x;
    d.fy = d.y;
  }

  dragged(d) {
    d.fx = d3.event.x;
    d.fy = d3.event.y;
  }

  dragended(d) {
    d.fx = null;
    d.fy = null;
  }

  /**
   * 高亮链路
   * @param obj
   */
  highlightObject(obj){
    if (obj) {
      this.dependsNode=utils.getNode(obj);
      this.node.classed('inactive',(d) => {
        return (this.dependsNode && (this.dependsNode.indexOf(d) == -1))
      });
      this.link.classed('inactive', (d) => {
        return (this.dependsNode&&(this.dependsNode.indexOf(d.source)==-1)||(this.dependsNode.indexOf(d.target)==-1))
      });
      this.linkText.classed('inactive',(d) => {
        return (this.dependsNode&&(this.dependsNode.indexOf(d.source)==-1)||(this.dependsNode.indexOf(d.target)==-1))
      });

      this.dynamicLink();
      this.strokeTime=setInterval(() => {
        this.dynamicLink();
      }, 500);
    } else {
      this.dependsNode=null;
      clearInterval(this.strokeTime);

      this.node.classed('inactive', false);
      this.link.classed('inactive', false).attr('stroke-dasharray','0');
      this.linkText.classed('inactive', false);
    }
  }

  /**
   * 动态链路
   */
  dynamicLink() {
    const totalLength = d3.randomUniform(6, 20)();
    this.link.attr('stroke-dasharray', (l) => {
      if (this.dependsNode&&((this.dependsNode.indexOf(l.source) > -1) && (this.dependsNode.indexOf(l.target) > -1))) {
        return 30 + " " + totalLength;
      }
      else {
        return "0";
      }
    });
    //link.attr("stroke-dasharray", "20 2").attr("stroke-dashoffset", "-200");
  }

  /**
   * tick
   */
  tick() {
    if(this.link){
      this.link.attr("d", (d) => {

        // var dx = d.target.x - d.source.x,
        //     dy = d.target.y - d.source.y,
        //     dr = Math.sqrt(dx * dx + dy * dy)+nodeWidth;
        // return "M" + (d.source.x+nodeWidth/2) + "," + (d.source.y) + "A" + dr + ","
        //     + dr + " 0 0,1 " + (d.target.x+nodeWidth/2) + "," + (d.target.y);

        let endx = d.target.x;
        let endy1 = d.target.y, endy2 = d.target.y;

        if (d.target.x - d.source.x > 60) {
          endx = d.target.x - 10;
        } else if (d.target.x - d.source.x < -60) {
          endx = d.target.x + 60;
        } else {
          endx = endx + this.nodeWidth / 2;
          if (d.target.y > d.source.y) {
            endy1 = endy1 - this.nodeWidth;
            endy2 = endy1 + 20;
          } else {
            endy1 = endy1 + this.nodeWidth;
            endy2 = endy1 - 20;
          }
        }

        let startx = d.source.x + this.nodeWidth / 2;
        let dr = (d.source.x + this.nodeWidth + d.target.x) / 2;

        return "M" + startx + "," + d.source.y
          + "C" + dr + "," + (d.source.y)
          + " " + dr + "," + endy1
          + " " + endx + "," + endy2;
      });
    }

    if(this.node){
      this.node.attr("transform", (d) => {
        return "translate(" + d.x + "," + d.y + ")"
      });
    }
  }

  /**
   * 缩小放大
   */
  zoomed() {
    //g.style("stroke-width", 1.5 / d3.event.transform.k + "px");
    //g.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")"); // no
    const transform = d3.event.transform;
    // if (transform.x < 0 || transform.y < 0 || transform.x + this.nodeWidth >= this.svgWidth || transform.y + this.nodeWidth >= this.svgHeight) {
    //   return;
    // }
    this.g.attr("transform", transform);
  }

  stopped() {
    if (d3.event.defaultPrevented) d3.event.stopPropagation();
    this.highlightObject(null);
    this._onNodeClick(null);
  }

  /**
   * 节点点击事件
   * @private
   */
  _onNodeClick(node) {
    if (node === this.selectedNode) return;
    const isPlugin = utils.isPlugin(node);
    const selectedNode = isPlugin ? node : null;

    this.selectedNode = selectedNode;
    this.onNodeChecked && this.onNodeChecked(selectedNode);//触发节点回调

    this.nodeBox
      .attr('stroke', (d) => {
        if (this.selectedNode === d.data) {
          return '#fbd044';
        } else {
          if (d.children && d.children.length > 0) {
            return '#515a6e';
          } else if (d._children && d._children.length > 0) {
            return '#d65048';
          } else {
            return '#515a6e';
          }
        }
      })
      .attr('stroke-width', (d) => {
        if (d._children && d._children.length > 0) {
          return '2'
        } else {
          return '1';
        }
      });
  }

  destroy(){
    if (this.svg&&this.dom) {
      while (this.dom.firstChild) {
        this.dom.removeChild(this.dom.firstChild);
      }
    }
  }
}
export default Cluster;
