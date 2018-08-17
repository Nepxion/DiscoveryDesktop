
import Item from './item'
import Line from './line'
import * as d3 from 'd3'
import _ from 'loadsh'

class Chart {
  constructor(params) {
    this.container = params.container
    this.onItemDblclick = params.onItemDblclick

    this.list = {}
    this.lineList = {}
    this.drawingLine = false
    this.currentLine = null
    this.selectedLine = null

    this._bindEvent()
  }

  /**
   * 添加元素
   * @param params 元素属性
   */
  addItem(params) {
    let item = new Item({
      container: this.container,
      id: params.id,
      x: params.x,
      y: params.y,
      name: params.name,
      text: params.text,
      type: params.type,
      inputIds: params.inputIds,
      outputIds: params.outputIds,
      data: params.data,
      onDrag: this._onItemDrag.bind(this),
      onClick: this._onItemClick.bind(this),
      onDblclick: this._onItemDblclick.bind(this),
      onMouseup: this._onItemMouseup.bind(this),
      onRemove: this._onItemRemove.bind(this),
      onPortMousedown: this._onPortMousedown.bind(this),
      onPortMouseup: this._onPortMouseup.bind(this)
    })

    this.list[item.id] = item
    return item
  }

  /**
   * 获取所有元素
   */
  getItems() {
    return this.list
  }

  /**
   * 存放已有元素
   */
  setItems(items) {
    _.forEach(items, item => {
      item.inputIds = new Set(item.inputIds)
      item.outputIds = new Set(item.outputIds)
      this.addItem(item)
    })

    _.forEach(this.list, fromItem => {
      fromItem.outputIds.forEach(outputId => {
        let targetItem = this.list[outputId]
        let line = this._addLine(fromItem, 'output', targetItem, 'input')
        line.updatePath()
        line.fromItem['outputPathIds'].add(line.id)
        line.targetItem['inputPathIds'].add(line.id)
        this.lineList[line.id] = line
      })
    })
  }

  /**
   * 绑定事件
   */
  _bindEvent() {
    this.container.on('mousemove', this._onMousemove.bind(this))
    this.container.on('mouseup', this._onMouseup.bind(this))
    this.container.on('click', this._onClick.bind(this))
    d3.select('body').on('keyup', this._onKeyup.bind(this))
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
      container: this.container,
      fromItem: fromItem,
      fromPortType: fromPortType,
      targetItem: targetItem,
      targetPortType: targetPortType,
      onClick: this._onLineClick.bind(this)
    })
  }

  /**
   * 鼠标按下连线具柄回调
   */
  _onPortMousedown(fromItem, fromPortType) {
    this.currentLine = this._addLine(fromItem, fromPortType)
    this.drawingLine = true
  }

  /**
   * 鼠标在画板中移动
   */
  _onMousemove() {
    if(this.drawingLine && this.currentLine) {
      let coordinates = {
        x: d3.event.offsetX,
        y: d3.event.offsetY
      }
      this.currentLine.updatePath(coordinates)
      this.currentLine.path.classed('active', true)
    }
  }

  /**
   * 鼠标在连线具柄位置抬起后回调事件
   * @param targetItem
   * @param targetPortType
   * @private
   */
  _onPortMouseup(targetItem, targetPortType) {
    if(!this.drawingLine) {
      return
    }

    // 同一IO类型不允许连线
    if(this.currentLine.fromPortType === targetPortType) {
      this.currentLine.remove()
      this.currentLine = null
      return
    }

    this.currentLine.targetPortType = targetPortType
    this.currentLine.targetItem = targetItem
    this.currentLine.updatePath()
    this.currentLine.path.classed('active', false)

    // 记录input/output元素的id
    this.currentLine.fromItem[this.currentLine.fromPortType + 'Ids'].add(targetItem.id)
    this.currentLine.fromItem[this.currentLine.fromPortType + 'PathIds'].add(this.currentLine.id)
    this.currentLine.targetItem[this.currentLine.targetPortType + 'Ids'].add(this.currentLine.fromItem.id)
    this.currentLine.targetItem[this.currentLine.targetPortType + 'PathIds'].add(this.currentLine.id)

    this.lineList[this.currentLine.id] = this.currentLine
    this.drawingLine = false
  }

  /**
   * 鼠标在元素位置抬起后回调事件，对元素进行自动连线
   * @param targetItem
   * @private
   */
  _onItemMouseup(targetItem) {
    if(!this.drawingLine) return

    if(this.currentLine.fromPortType === 'input' && targetItem.getOutputPort()) {
      this._onPortMouseup(targetItem, 'output')
      return
    }

    if(this.currentLine.fromPortType === 'output' && targetItem.getInputPort()) {
      this._onPortMouseup(targetItem, 'input')
      return
    }

    this._onMouseup()
  }

  /**
   * 鼠标在画板空白位置抬起
   */
  _onMouseup() {
    if(!this.drawingLine) return

   if(this.currentLine) {
      this.currentLine.remove()
      this.currentLine = null
    }
   this.drawingLine = false
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
   * 画板点击事件
   * @private
   */
  _onClick() {
    if(this.selectedLine) {
      this.selectedLine.blur()
      this.selectedLine = null
    }

    if(this.selectedItem) {
      this.selectedItem.blur()
      this.selectedItem = null
    }
  }

  /**
   * 连线点击回调事件
   * @param line 连线实例对象
   * @private
   */
  _onLineClick(line) {
    if(line === this.selectedLine) return
    if(this.selectedLine) {
      this.selectedLine.blur()
    }
    this.selectedLine = line
  }

  /**
   * 元素点击回调事件
   * @param item 元素实例对象
   * @private
   */
  _onItemClick(item) {
    if(item === this.selectedItem) return
    if(this.selectedItem) {
      this.selectedItem.blur()
    }
    this.selectedItem = item
  }

  /**
   * 元素双击回调事件
   * @param item 元素实例对象
   * @private
   */
  _onItemDblclick(item) {
    this.onItemDblclick(item)
  }

  /**
   * 当元素删除时回调事件
   * @param item 元素实例对象
   * @private
   */
  _onItemRemove(item) {
    if(item.inputPathIds.size) {
      item.inputPathIds.forEach(id => {
        this.lineList[id].remove()
        delete this.lineList[id]
      })
    }

    if(item.outputPathIds.size) {
      item.outputPathIds.forEach(id => {
        this.lineList[id].remove()
        delete this.lineList[id]
      })
    }
  }

  /**
   * 键盘事件
   * @private
   */
  _onKeyup() {
    switch(d3.event.keyCode) {
      case 8:
      case 46:
        if(this.selectedLine) {
          this.selectedLine.remove()
          delete this.lineList[this.selectedLine.id]
          this.selectedLine = null
        }
        if(this.selectedItem) {
          this.selectedItem.remove()
          delete this.list[this.selectedItem.id]
          this.selectedItem = null
        }
        break
    }
  }
}

export default Chart
