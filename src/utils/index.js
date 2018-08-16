
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

export { makeId, getTextWidth }
