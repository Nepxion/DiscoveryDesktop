
/**
 * 生成随机ID
 */
let makeId = function() {
  let text = "";
  let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  for (let i = 0; i < 10; i++)
    text += possible.charAt(Math.floor(Math.random() * possible.length));

  return text;
}

let getTextWidth = function(text) {
  let i = text.length
  let width = 0
  while(i) {
    let char = text.charAt(i - 1)
    if(char.charCodeAt() > 126) {
      width += 13
    } else {
      width += 9
    }
    i--
  }
  return width
}

const getGroups = function(obj) {
  const groups = [];
  Object.keys(obj).forEach(key => {
    const childs=obj[key];
    childs&&childs.forEach(v => {
      if(isPlugin(v)){
        const metadate=v.metadata;
        const groupKey = metadate["spring.application.group.key"];
        if(groupKey){
          const group = metadate[groupKey];
          if (groups.indexOf(group) == -1) {
            groups.push(group);
          }
        }
      }
    });
  });
  return groups;
}

const filterGroups = function(obj,group) {
  let data = {};
  if (group && group !== '') {
    Object.keys(obj).forEach(key => {
      const childs = obj[key].filter(child => {
        if(isPlugin(v)) {
          const metadate = child.metadata;
          const groupKey = metadate["spring.application.group.key"];
          if (groupKey) {
            const groupVal = metadate[groupKey];
            return groupVal === group;
          }
        }
      });
      if (childs.length > 0) {
        data[key] = childs;
      }
    });
    return data;
  } else {
    return obj;
  }
}

const isPlugin = function(obj) {
  if(obj&&obj.metadata){
    const meta = obj.metadata || {};
    return Object.keys(meta).indexOf("spring.application.discovery.plugin") > -1;
  } else{
    return false;
  }
}

const getPluginService = function(obj, without) {
  let data = [];
  Object.keys(obj).forEach(key => {
    if(key!==without){
      const childs = obj[key].find(child => isPlugin(child));
      if (childs) {
        data.push(key);
      }
    }
  });
  return data;
}

const convRoutes = function(obj) {
  const json = JSON.parse(JSON.stringify(obj).replace(/nexts/g, "children").replace(/serviceId/g, "name"));
  return json;
}

const getLineCentre = function(line) {
  let x = line._x0;
  let y = line._y0;
  if (line._x0 > line._x1) {
    x = (line._x0 - line._x1) / 2 + line._x1;
  } else if (line._x0 < line._x1) {
    x = (line._x1 - line._x0) / 2 + line._x0;
  }
  if (line._y0 > line._y1) {
    y = (line._y0 - line._y1) / 2 + line._y1;
  } else if (line._y0 < line._y1) {
    y = (line._y1 - line._y0) / 2 + line._y0;
  }
  return {x, y};
}

const getLineMiddle = function (line) {
  var x0=line.source.x;
  var y0=line.source.y;
  var x1=line.target.x;
  var y1=line.target.y;

  if (x0 > x1) {
    x0 = (x0 - x1) / 2 + x1;
  } else if (x0 < x1) {
    x0 = (x1 - x0) / 2 + x0;
  }
  if (y0 > y1) {
    y0 = (y0 - y1) / 2 + y1;
  } else if (y0 < y1) {
    y0 = (y1 - y0) / 2 + y0;
  }
  return {x:y0, y:x0};
}

const getNode = function(obj) {
  var newData = [];
  if (obj.children) {
    newData = obj.children;
    obj.children.forEach((item) => {
      newData = newData.concat(item.children)
    });
  }
  newData.push(obj);
  if (obj.parent) {
    newData.push(obj.parent);
    if (obj.parent.parent) {
      newData.push(obj.parent.parent);
    }
  }
  return newData;
}

const convMap = function(obj,groups) {
  const json = { name: !groups && groups !== '' ? groups : '全部集群', children: [] };
  const keys = Object.keys(obj);
  keys.forEach(key => {
    const children=obj[key].map(child=>{
        return {
          ...child,
          name:child.host+":"+child.port
        };
    });
    json.children.push({
      name: key,
      children
    });
  });
  return json;
}

export { makeId, getTextWidth, getGroups, filterGroups, isPlugin, getPluginService, convRoutes, getLineCentre, getLineMiddle, getNode, convMap }
