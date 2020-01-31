<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title></head>
<body>
Hello ${name}!
<br>
遍历数据模型中的list学生信息，（stus）
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>金额</td>
        <td>出生日期</td>
    </tr>

    <#if stus?? >
        <#list stus as stu>
            <tr>
                <td>${stu_index+1}</td>
                <td <#if stu.name == "小方"> style="background: cadetblue"</#if>>${stu.name}</td>
                <td>${stu.money}</td>
                <#-- 大于号> 会和标签的尖括号混淆，可以用括号(stu.money > 300) 括起来，或者用gt代替>-->
                <td <#if stu.money gt 150> style="background: cadetblue"</#if>>${stu.age}</td>
                <td>${stu.birthday?date}</td>
            </tr>
        </#list>

    </#if>
    <br>
    list的大小：${stus?size}
</table>
    <br>
遍历数据模型中的map学生信息，（stuMap），第一种方法：在中括号中填写map的key，第二种方法：在map后直接“点key”
    <br>
<#--空值处理， (属性)!'默认值'-->
    姓名：${(stuMap['stu1'].name)!''}<br>
    年龄：${(stuMap['stu1'].age)!''}<br>
    姓名：${stuMap.stu2.name}<br>
    年龄：${stuMap.stu2.age}<br>
<br>
遍历map中的key, stuMap?keys是key列表（是一个list）
<br>
<#list stuMap?keys as k>
    姓名：${stuMap[k].name}<br>
    年龄：${stuMap[k].age}<br>
</#list>

<br>
<#--c函数将数字型转为字符串，123,456,789 -> 123456789-->
${point?c}

<br>
<#--json转对象-->
<#assign text="{'name':'json_name','age':'20'}" />
<#assign data=text?eval />
name:${data.name} <br>
age:${data.age} <br>

</body>
</html>