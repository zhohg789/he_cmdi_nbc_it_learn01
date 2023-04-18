/*
 * @Description: 
 * @Author: hyj
 * @Date: 2023-03-20 18:08:40
 * @LastEditTime: 2023-03-28 11:05:11
 * @LastEditors: hyj
 */
let headerHtml = "";
// 添加xml数据到数据库
function addXmlData() {
  //创建请求
  let xhr  = new XMLHttpRequest();
  xhr.timeout = 40;
  // 设置请求参数
  xhr.open("post","http://localhost:7201/addXmlData",true);
  // 监听请求的状态变化，写请求成功执行的函数
  xhr.onreadystatechange = function(){
    // 通信成功时，状态值为4
    if (xhr.readyState === 4){
      if (xhr.status === 200){
        console.log(xhr.responseText);
      } else {
        console.error(xhr.statusText);
      }
    }
  };
  // 发送请求
  xhr.send();
}
function getXmlData() {
  let xhr  = new XMLHttpRequest();
  xhr.timeout = 40;
  xhr.open("post","http://localhost:7201/getXmlData",true);
  xhr.onreadystatechange = function(){
    // 通信成功时，状态值为4
    if (xhr.readyState === 4){
      if (xhr.status === 200){
        // 将得到的字符串数据通过JSON格式化
        let xmlData = JSON.parse(xhr.responseText);
        dealData(xmlData)
      } else {
        console.error(xhr.statusText);
      }
    }
  };
  xhr.send();
}

// 处理数据并在页面展示
function dealData(data) {
  let tbodyDom = document.getElementById('tbody')
  for (let [index,item] of data.entries()) {
    if(!headerHtml) {
      for (const key in item) {
        headerHtml = headerHtml + "<td class='"+ key +"'>" + key + "</td>"
      }
      // 拼接成html标签格式的字符串
      headerHtml = "<td class='id'></td>" + headerHtml
      let temp = document.createElement('tr');
      temp.innerHTML += headerHtml;
      //放到页面展示
      tbodyDom.appendChild(temp)
    }
    let contenthtml = ""
    for (const key in item) {
      contenthtml = contenthtml + "<td class='"+ item[key] +"'>" + item[key] + "</td>"
    }
    contenthtml = "<td class='id'>" + parseInt(index) + 1 + "</td>" + contenthtml
    let temp = document.createElement('tr');
    temp.innerHTML += contenthtml;
    tbodyDom.appendChild(temp)
  }
}