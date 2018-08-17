<template>
  <svg id="topo-chart" width="100%" height="100%"></svg>
</template>

<script>
  import * as d3 from "d3"
  import Chart from "@/components/d3/chart"


  let chart = null
    export default {
      name: "graph",
      mounted() {
        let container = d3.select("#topo-chart")
        chart = new Chart({
          container: container
        })
        // chart.addItem({
        //   x: 0,
        //   y: 0,
        //   name: '服务a',
        //   type: 'INPUT'
        // })


      }
    }
</script>

<style scoped>

</style>
