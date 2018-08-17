<template>
  <svg id="chart2" width="100%" height="100%"></svg>
</template>

<script>
  import * as d3 from "d3";
  var data = [
    {
      "name": "demo1", "description": "Atlas of Global Agriculture"
    },
    {
      "name": "demo2", "description": "Atlas of Global Agriculture"
    },
    {
      "name": "demo3", "description": "Atlas of Global Agriculture"
    },
      {
        "name": "demo4", "description": "Virtual Lab of Global Agriculture",
        "children": [
          {
            "name": "demo41",
            "address": "http://d3js.org",
            "note": "Prototype Infographics on Excess Fertilizer Nutrients"
          },
          {
            "name": "demo42",
            "address": "http://d3js.org",
            "note": "The gap between attainable yields and actual yields, with modeled yields assuming the percentage of gap closed."
          },
          {"name": "Fertilizer", "address": "http://sunsp.net"}
        ]
      },
      {
        "name": "demo5", "description": "Profiles of Country",
        "children": [
          {"name": "demo51", "address": "http://d3js.org"},
          {"name": "demo52", "address": "http://d3js.org"},
          {"name": "demo53", "address": "http://d3js.org"},
          {"name": "demo54", "address": "http://uis.edu/ens"},
          {"name": "demo55", "address": "http://uis.edu/ens"},
          {"name": "demo56", "address": "http://uis.edu/ens"},
          {"name": "demo57", "address": "http://uis.edu/ens"}
        ]
      },
      {
        "name": "demo6", "description": "Crop Data in 5 minutes grid",
        "children": [
          {"name": "demo61", "address": "http://www.earthstat.org/"},
          {"name": "demo62", "address": "http://www.earthstat.org/"},
          {"name": "demo6", "address": "http://www.earthstat.org/"},
          {"name": "demo6", "address": "http://www.earthstat.org/"}
        ]
      }
    ];

  export default {
    name: "instance",
    mounted() {
      let svg = d3.select("#chart2"),
        width = document.body.clientWidth, x = 0, y = 0,m=10,
        rwidth = 120, rheight = 120,iconSize = 100, k = parseInt(width / (rwidth + m) - 1);

      data.forEach((val, index) => {
        let sx = (rwidth + m) * x+m;
        let sy = (rheight + m) * y+m;
        let hasChild=(val.children&&val.children.length>1)||false;
        if (index % k === 0 && index > 0) {
          x = 0;
          y++;
        } else {
          x++;
        }

        let g = svg.append("g")
          .attr("transform", "translate(" + sx + "," + sy + ")");

        g.append("title")
          .text(val.name);

        let iconGroup = g.append('g');
        iconGroup.append('rect')
          .attr('x', 0)
          .attr('y', 0)
          .attr('rx', 5)
          .attr('rx', 5)
          .attr('width', iconSize)
          .attr('height', iconSize)
          .attr('fill', '#fff')
          .attr('stroke', '#169ce4')
          .attr('stroke-width', '3')
          .attr('stroke-linejoin', 'round')
          .attr('class', 'item');

        if(hasChild){
          let gchild = iconGroup.append("g")
            .attr("transform", "translate(5,5)");

          let t=iconSize-30;

          let tool=gchild.append("g")
            .attr('class', 'item-tool')
            .attr("transform", "translate(" + t + ",0)");

          tool.append("title")
            .text("展开");

          tool.append('image')
            .attr('href', require('./images/open.png'))
            .attr('width', 21)
            .attr('height', 21);

          tool.on('click', this._onClickTool.bind(this));

          var pack = d3.pack()
            .size([iconSize-10, iconSize-10]);

          var root = d3.hierarchy(val)
            .sum(function(d) { return iconSize; });

          var node = gchild.selectAll(".node")
            .data(pack(root).descendants())
            .enter().append("g")
            .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

          node.filter(function(d) { return d.parent; }).append("title")
            .text(function(d) { return d.data.name; });

          node.filter(function(d) { return d.parent; }).append("circle")
            .attr('x', 0)
            .attr('y', 0)
            .attr('stroke', '#169ce4')
            .attr('fill', '#fff')
            .attr("r", function(d) { return d.r; });

          node.filter(function(d) { return d.parent; }).append('image')
            .attr('href', require('./images/online.png'))
            .attr('x', -8)
            .attr('y', -8)
            .attr('width', function(d) { return d.r; })
            .attr('height', function(d) { return d.r; });
          // node.append("text")
          //   .attr("dy", "0.3em")
          //   .text(function(d) { return d.data.name; });
        }
        else{
          iconGroup.append('image')
            .attr('href', require('./images/online.png'))
            .attr('x', 4)
            .attr('y', 4)
            .attr('width', iconSize-8)
            .attr('height', iconSize-8);
        }

        g.append('text')
          .text(val.name.substring(0, 10))
          .attr('x', 50)
          .attr('y', 120)
          .attr("dy", "0.3em")
          .attr('text-anchor', 'middle');

        // let drag = d3.drag()
        //   .on("start", this._onGroupDragstart.bind(this))
        //   .on("drag", this._onGroupDrag.bind(this))
        // g.call(drag)
      });
    },
    methods: {
      /**
       * 开始拖拽
       * @private
       */
      _onGroupDragstart() {
        //this._dragDeltaX = d3.event.x - this.x
        //this._dragDeltaY = d3.event.y - this.y
      },

      /**
       * 正在拖拽
       * @private
       */
      _onGroupDrag() {
        //this.x = d3.event.x - this._dragDeltaX
        //this.y = d3.event.y - this._dragDeltaY
        //this._group.raise().attr('transform', `translate(${this.x}, ${this.y})`)
        //this.onDrag(this)
      },

      _onClickTool(item,e){
        console.log(item,e);
      }
    }
  }
</script>

<style scoped>

</style>
