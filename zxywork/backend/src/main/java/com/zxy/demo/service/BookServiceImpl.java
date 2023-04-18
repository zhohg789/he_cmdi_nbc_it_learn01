package com.zxy.demo.service;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.Keymap;
import java.io.File;
import java.util.*;

/**
 * @Author:zff
 * @Description:Todo
 * @Date:2020/4/19
 */
@Service
public class BookServiceImpl  implements BookService{

    //把连接数据库的JDBC类自动注入进来
    @Autowired
    JdbcTemplate jdbcTemplate;
//    获取xml数据
    @Override
    public List getXmlData() {
        //sql语句 查询hwxml表中的所有数据
        String sql = "select * from hwxml";
        //查询hwxml表中的所有数据并返回一个map集合
        List<Map<String,Object>> xmlList = jdbcTemplate.queryForList(sql);
        return xmlList;
    }
    public void addXmlData() {
        String filePath = "src/main/resources/xml/1.xml";
        //读取xml数据为一个map集合
        List<Map<String,String>> dataList = readXml(filePath);

        for (Map<String,String> dataMap : dataList) {
            String sql = "";
            String keySql = "";
            String valueSql = "";
            for(Map.Entry<String,String> entry : dataMap.entrySet()){
                keySql = keySql + entry.getKey() + ",";
                valueSql = valueSql + "'" + entry.getValue() + "',";
            }
            sql = "insert into hwxml (" + keySql.substring(0,keySql.length() - 1) + ") values (" + valueSql.substring(0,valueSql.length() - 1) + ")";
                    jdbcTemplate.update(sql);
        }

    }
//    读取xml文件数据
    public List<Map<String,String>> readXml(String filePath) {
        //初始化读取文件对象
        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(new File(filePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //获得根节点
        Element root = doc.getRootElement();
        //获取根节点下的Objects节点对象
        Element objectsEle = root.element("Objects");
        //获取Objects节点下的FieldName节点对象
        Element fileNameEle = objectsEle.element("FieldName");
        //获取Objects节点下的FieldValue节点对象
        Element fileValueEle = objectsEle.element("FieldValue");
        //获取FieldName节点下的所有子节点对象
        List<Element> fileNameElementList = fileNameEle.elements();
        //获取FieldValue节点下的所有子节点对象
        List<Element> fileValueEleList = fileValueEle.elements();
        //创建key字段的map对象
        Map keyMap = new HashMap();
        //创建map数据集合
        List<Map<String,String>> objectList = new ArrayList<>();
        //for循环把FieldName节点下所有节点的i属性和值拿出来组成map键值对放到map中
        for(Element element : fileNameElementList) {
            keyMap.put(element.attributeValue("i"),element.getText());
        }
        //循环把FieldValue节点下所有节点数据整合成一个list
        for(Element element : fileValueEleList) {
            //获取FieldValue节点的子节点的所有节点对象  即Object节点的所有子节点
            List<Element> objectElementList = element.elements();
            //常见map对象存放Object节点的数据
            Map objMap = new HashMap();
            //循环获取Object节点的每一个属性并把它放到map中
            for(Iterator it = element.attributeIterator(); it.hasNext();){
                Attribute attribute = (Attribute) it.next();
                objMap.put(attribute.getName(),attribute.getText());
            }
            //循环获取Object节点下所有子节点的值并根据i的属性值和上方FieldName的i属性值一一对应找到对应的fileName值，实现数据fileName和fileValue的键值对对应
            for(Element ele : objectElementList) {
                objMap.put(keyMap.get(ele.attributeValue("i")),ele.getText());
            }
            //把Object对象的值转换得到的map对象放到集合中
            objectList.add(objMap);
        }
        return objectList;
    }
}
