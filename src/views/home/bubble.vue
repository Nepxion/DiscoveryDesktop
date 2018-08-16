<template>
  <div id="mainBubble">
  </div>
</template>

<script>
  import * as d3 from "d3";

  var root = [
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

  var w = window.innerWidth;
  var h = window.innerHeight;
  var oR = 0;
  var nTop = 0;
  var svgContainer,svg;

  export default {
    name: "bubble",
    mounted() {
      nTop = root.length;
      oR = w / (1 + 3 * nTop);

      //h = Math.ceil(w / nTop * 2);

      svgContainer = d3.select("#mainBubble")
        .style("height", h + "px");

      svg = d3.select("#mainBubble").append("svg")
        .attr("class", "mainBubbleSVG")
        .attr("width", w)
        .attr("height", h)
        //.on("mouseleave",this.resetBubbles);

      var bubbleObj = svg.selectAll(".topBubble")
        .data(root)
        .enter().append("g")
        .attr("id", function(d, i) {
          return "topBubbleAndText_" + i
        })
        .attr("class", "topBubbleAndText")
        //.on("mouseover", this._activateBubble);


      bubbleObj.attr("transform", function(d, i) {
        let x=oR * (3 * (1 + i) - 1)-oR;
        let y=(h + oR) / 3-oR;
        return "translate(" + x + "," + y + ")";
      });


      bubbleObj.append("rect")
        .attr("class", "topBubble")
        .attr("id", function(d, i) {
          return "topBubble" + i;
        })
        .attr('width', oR*2)
        .attr('height', oR*2)
        .attr('x', 0)
        .attr('y', 0)
        // .attr("x", function(d, i) {
        //   return oR * (3 * (1 + i) - 1)-oR;
        // })
        // .attr("y", (h + oR) / 3-oR)
        .attr('rx', 5)
        .style("fill", '#fff') // #1f77b4
        .attr('stroke', '#169ce4')
        .attr('stroke-width', '3')
        .attr('stroke-linejoin', 'round')
        .style("opacity", 0.3);

      bubbleObj.append("text")
        .attr("class", "topBubbleText")
        .attr('x', oR)
        // .attr("x", function(d, i) {
        //   return oR * (3 * (1 + i) - 1);
        // })
        .attr("y", oR*2+20)
        .attr("font-size", 20)
        .style("fill", '#169ce4') // #1f77b4
        .attr("text-anchor", "middle")
        .attr("dominant-baseline", "middle")
        .attr("alignment-baseline", "middle")
        .text(function(d) {
          return d.name
        });

      bubbleObj.filter(function(d) { return !d.children; })
        .attr("cursor", "pointer");

      bubbleObj.append("title")
        .text(function(d) {
          return d.name;
        });

      let tool=bubbleObj.append("g")
        .attr('class', 'topBubbleTool')
        .attr("cursor", "pointer")
        .attr("transform", "translate(5,5)");

      tool.filter(function(d) { return d.children; }).append("title")
        .text("展开");

      tool.filter(function(d) { return d.children; }).append('image')
        .attr('href', require('./images/open.png'))
        .attr('width', 21)
        .attr('height', 21);

      tool.on('click', (d, i) => {
        if(d.children){
          this.openBox(d,i)
        }
      });

      bubbleObj.filter(function(d) { return !d.children; }).append('image')
        .attr("class", "topBubbleImg")
        .attr('href', require('./images/online.png'))
        .attr('x', 4)
        .attr('y', 4)
        .attr('width', oR*2-8)
        .attr('height', oR*2-8);

      for (var iB = 0; iB < nTop; iB++) {
        let hasChild=(root[iB]&&root[iB].children&&root[iB].children.length>1)||false;

        if(hasChild){
          var l=root[iB].children.length;

          let g = svg.selectAll("#topBubbleAndText_" + iB).append("g")
            .attr("class", "childBubbleAndText")
            .attr("transform", "translate(0,0)");

          var pack = d3.pack()
            .size([oR*2, oR*2]);

          var data = d3.hierarchy(root[iB])
            .sum(function(d) { return oR; });

        var childBubbles = g.selectAll(".childBubble" + iB)
          .data(pack(data).descendants())
          .enter().filter(function(d) { return !d.children; }).append("g")
          .attr("class", "childBubble")
          .attr("cursor", "pointer")
          .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

          childBubbles.append("title")
            .text(function(d) {
              return d.data.name;
            });

        childBubbles.append("circle")
          .attr("class", "childBubble" + iB)
          .attr("id", function(d, i) {
            return "childBubble_" + iB + "sub_" + i;
          })
          .attr("r", function(d) {
            return oR / l;
          })
          .style("opacity", 0.5)
          .style("fill", '#fff') // #1f77b4
          .attr('stroke', '#169ce4');

          childBubbles.append('image')
            .attr("class", "childBubbleImg")
            .attr('href', require('./images/online.png'))
            .attr('x', -oR / l+4)
            .attr('y', -oR / l+4)
            .attr('width', oR / l*2-8)
            .attr('height', oR / l*2-8);

        // childBubbles.append("text")
        //   .attr("class", "childBubbleText" + iB)
        //   .attr("x", function(d, i) {
        //     return (oR * (3 * (iB + 1) - 1) + oR * 1.5 * Math.cos((i - 1) * 45 / 180 * 3.1415926));
        //   })
        //   .attr("y", function(d, i) {
        //     return ((h + oR) / 3 + oR * 1.5 * Math.sin((i - 1) * 45 / 180 * 3.1415926));
        //   })
        //   .style("opacity", 0.5)
        //   .attr("text-anchor", "middle")
        //   .style("fill", '#169ce4') // #1f77b4
        //   .attr("font-size", 6)
        //   .attr("cursor", "pointer")
        //   .attr("dominant-baseline", "middle")
        //   .attr("alignment-baseline", "middle")
        //   .text(function(d) {
        //     return d.name
        //   })
        //   .on("click", function(d, i) {
        //     window.open(d.address);
        //   });
        }
      }

      //window.onresize = resetBubbles;
    },
    methods: {
      resetBubbles() {
        w = window.innerWidth;
        oR = w / (1 + 3 * nTop);

        h = Math.ceil(w / nTop * 2);
        svgContainer.style("height", h + "px");

        svg.attr("width", w);
        svg.attr("height", h);

        var t = svg.transition()
          .duration(650);

        t.selectAll(".topBubbleAndText").attr("transform", function(d, ii) {
          let x=oR * (3 * (1 + ii) - 1)-oR;
          let y=(h + oR) / 3-oR;
          return "translate(" + x + "," + y + ")";
        });

        t.selectAll(".topBubble")
          .attr('width', oR*2)
          .attr('height', oR*2);

        t.selectAll(".topBubbleText")
          .attr('x', oR)
          .attr("y", oR*2+20)
          .attr("font-size", 20);

        for (var k = 0; k < nTop; k++) {
          t.selectAll(".childBubbleText" + k)
            .attr("x", function(d, i) {
              return (oR * (3 * (k + 1) - 1) + oR * 1.5 * Math.cos((i - 1) * 45 / 180 * 3.1415926));
            })
            .attr("y", function(d, i) {
              return ((h + oR) / 3 + oR * 1.5 * Math.sin((i - 1) * 45 / 180 * 3.1415926));
            })
            .attr("font-size", 6)
            .style("opacity", 0.5);

          t.selectAll(".childBubble" + k)
            .attr("r", function(d) {
              return oR / 3.0;
            })
            .style("opacity", 0.5)
            .attr("cx", function(d, i) {
              return (oR * (3 * (k + 1) - 1) + oR * 1.5 * Math.cos((i - 1) * 45 / 180 * 3.1415926));
            })
            .attr("cy", function(d, i) {
              return ((h + oR) / 3 + oR * 1.5 * Math.sin((i - 1) * 45 / 180 * 3.1415926));
            });

        }
      },
      _activateBubble(d, i) {
        if(!d.children)
          return;
        // increase this bubble and decrease others
        var t = svg.transition()
          .duration(d3.event.altKey ? 7500 : 350);

        //topBubbleAndText

        t.selectAll(".topBubbleAndText").attr("transform", function(d, ii) {
          let y=(h + oR) / 3-oR;
          let x=oR * (3 * (1 + ii) - 1)-oR;
          if (i == ii) {
            // Nothing to change
            x= x - 0.8 * oR * (ii - 1);
          } else {
            // Push away a little bit
            if (ii < i) {
              // left side
              x = x * 0.8;
            } else {
              // right side
              x = oR * 10 - oR * 0.8 * (3 * (3 - ii) - 1);
            }
          }
          return "translate(" + x + "," + y + ")";
        });

        t.selectAll(".topBubble")
          .attr('width', function(d, ii) {
            if (i == ii)
              return oR * 3.8;
            else
              return oR * 2;
          })
          .attr('height', function(d, ii) {
            if (i == ii)
              return oR * 3.8;
            else
              return oR * 2;
          });

        t.selectAll(".topBubbleText")
          .attr("font-size", function(d, ii) {
            if (i == ii)
              return 20 * 1.5;
            else
              return 20 * 0.6;
          })
          .attr("x", function(d, ii) {
            if (i == ii)
              return oR * 1.9;
            else
              return oR;
          })
          .attr("y", function(d, ii) {
            if (i == ii)
              return oR * 1.9*2+20;
            else
              return oR*2+20;
          });

        var signSide = -1;
        for (var k = 0; k < nTop; k++) {
          signSide = 1;
          if (k < nTop / 2) signSide = 1;
          t.selectAll(".childBubbleText" + k)
            .attr("x", function(d, i) {
              return (oR * (3 * (k + 1) - 1) - 0.6 * oR * (k - 1) + signSide * oR * 2.5 * Math.cos((i - 1) * 45 / 180 * 3.1415926));
            })
            .attr("y", function(d, i) {
              return ((h + oR) / 3 + signSide * oR * 2.5 * Math.sin((i - 1) * 45 / 180 * 3.1415926));
            })
            .attr("font-size", function() {
              return (k == i) ? 12 : 6;
            })
            .style("opacity", function() {
              return (k == i) ? 1 : 0;
            });

          t.selectAll(".childBubble" + k)
            .attr("cx", function(d, i) {
              return (oR * (3 * (k + 1) - 1) - 0.6 * oR * (k - 1) + signSide * oR * 2.5 * Math.cos((i - 1) * 45 / 180 * 3.1415926));
            })
            .attr("cy", function(d, i) {
              return ((h + oR) / 3 + signSide * oR * 2.5 * Math.sin((i - 1) * 45 / 180 * 3.1415926));
            })
            .attr("r", function() {
              return (k == i) ? (oR * 0.55) : (oR / 3.0);
            })
            .style("opacity", function() {
              return (k == i) ? 1 : 0;
            });
        }
      },

      openBox(d,i) {
        if (!d.children)
          return;

        var t = svg.transition()
          .duration(d3.event.altKey ? 7500 : 350);

        t.selectAll(".topBubbleAndText")
          .attr("transform", function(d, ii) {
          let y = (h + oR) / 3 - oR;
          let x = oR * (3 * (1 + ii) - 1) - oR;
          if (i == ii) {
            // Nothing to change
            x = x - 0.8 * oR * (ii - 1);
          } else {
            // Push away a little bit
            if (ii < i) {
              // left side
              x = x * 0.8;
            } else {
              // right side
              x = oR * 10 - oR * 0.8 * (3 * (3 - ii) - 1);
            }
          }
          return "translate(" + x + "," + y + ")";
        });

        t.selectAll(".topBubble")
          .attr('width', function(d, ii) {
            if (i == ii)
              return oR * 3.8;
            else
              return oR * 2;
          })
          .attr('height', function(d, ii) {
            if (i == ii)
              return oR * 3.8;
            else
              return oR * 2;
          });

        t.selectAll(".topBubbleText")
          .attr("font-size", function(d, ii) {
            if (i == ii)
              return 20 * 1.5;
            else
              return 20 * 0.6;
          })
          .attr("x", function(d, ii) {
            if (i == ii)
              return oR * 1.9;
            else
              return oR;
          })
          .attr("y", function(d, ii) {
            if (i == ii)
              return oR * 1.9 * 2 + 20;
            else
              return oR * 2 + 20;
          });

        // t.selectAll(".topBubbleTool").on('click', (d, i) => {
        //   if(d.children){
        //     this.closeBox(d,i)
        //   }
        // });

        var childBubbles = t.selectAll(".childBubbleAndText");
        console.log(childBubbles);

        var signSide = -1;
        for (var k = 0; k < nTop; k++) {
          //signSide = 1;
          //if (k < nTop / 2) signSide = 1;
          // t.selectAll(".childBubbleText" + k)
          //   .attr("x", function(d, i) {
          //     return (oR * (3 * (k + 1) - 1) - 0.6 * oR * (k - 1) + signSide * oR * 2.5 * Math.cos((i - 1) * 45 / 180 * 3.1415926));
          //   })
          //   .attr("y", function(d, i) {
          //     return ((h + oR) / 3 + signSide * oR * 2.5 * Math.sin((i - 1) * 45 / 180 * 3.1415926));
          //   })
          //   .attr("font-size", function() {
          //     return (k == i) ? 12 : 6;
          //   })
          //   .style("opacity", function() {
          //     return (k == i) ? 1 : 0;
          //   });

          // t.selectAll(".childBubble" + k)
          //   .attr("cx", function(d, i) {
          //     return (oR * (3 * (k + 1) - 1) - 0.6 * oR * (k - 1) + signSide * oR * 2.5 * Math.cos((i - 1) * 45 / 180 * 3.1415926));
          //   })
          //   .attr("cy", function(d, i) {
          //     return ((h + oR) / 3 + signSide * oR * 2.5 * Math.sin((i - 1) * 45 / 180 * 3.1415926));
          //   })
          //   .attr("r", function() {
          //     return (k == i) ? (oR * 0.55) : (oR / 3.0);
          //   })
          //   .style("opacity", function() {
          //     return (k == i) ? 1 : 0;
          //   });
        }

      },
      closeBox(d,i){

      }
    }
  }
</script>

<style scoped>
  #mainBubble {
    font: 10px sans-serif;
    position: relative;
    width: 100%;
    height: 100%;


    border-width: 1px;
    border-color: rgb(255, 255, 255);
    border-style: solid;
    background-color: rgb(255, 255, 255);
    background-image: url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImdyaWQiIHdpZHRoPSI0MCIgaGVpZ2h0PSI0MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PHBhdGggZD0iTSAwIDEwIEwgNDAgMTAgTSAxMCAwIEwgMTAgNDAgTSAwIDIwIEwgNDAgMjAgTSAyMCAwIEwgMjAgNDAgTSAwIDMwIEwgNDAgMzAgTSAzMCAwIEwgMzAgNDAiIGZpbGw9Im5vbmUiIHN0cm9rZT0iI2UwZTBlMCIgb3BhY2l0eT0iMC4yIiBzdHJva2Utd2lkdGg9IjEiLz48cGF0aCBkPSJNIDQwIDAgTCAwIDAgMCA0MCIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjZTBlMGUwIiBzdHJva2Utd2lkdGg9IjEiLz48L3BhdHRlcm4+PC9kZWZzPjxyZWN0IHdpZHRoPSIxMDAlIiBoZWlnaHQ9IjEwMCUiIGZpbGw9InVybCgjZ3JpZCkiLz48L3N2Zz4=");
    background-position: -1px -1px;
  }

  #mainBubble svg {
    left: 0;
    position: absolute;
    top: 0;
  }

  #mainBubble circle.topBubble {
    fill: #aaa;
    stroke: #666;
    stroke-width: 1.5px;
  }
</style>
