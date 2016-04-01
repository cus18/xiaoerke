<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>用户行为综合分析</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#treeTable").treeTable({expandLevel: 1}).show();
        });
    </script>
</head>
<>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/userActionStatistic/userActionStatistic"><font
            color="#006400">用户行为综合分析</font></a></li>
</ul>
<form:form id="searchForm" modelAttribute="resultMap" action="${ctx}/sys/userActionStatistic/userActionStatistic"
           method="post"
           class="form-search ">
    <div>
        <h5>
            &nbsp;&nbsp;&nbsp;宝大夫用户访问统计数据：<font color="red" size="3">${resultMap.startDate}</font>&nbsp;至&nbsp;
            <font color="red" size="3">${resultMap.endDate}</font>
            共&nbsp;<font color="red" size="3">${resultMap.totalNum}</font>&nbsp;人使用了业务;
            其中访问预约业务&nbsp;<font color="red" size="3">${resultMap.totalAppNum}</font>&nbsp;人，新关注用户&nbsp;
            <font color="red" size="3">${resultMap.totalNewAppNum}</font>&nbsp;人访问预约业务，
            </br>&nbsp;&nbsp;&nbsp;老关注用户&nbsp;<font color="red" size="3">${resultMap.totalOldAppNum}</font>&nbsp;人访问预约业务;&nbsp;
            <br/><font color="red" size="3">${resultMap.totalConsultNum}</font>&nbsp;&nbsp;&nbsp;人使用了咨询业务，新关注用户&nbsp;
            <font color="red" size="3">${resultMap.totalNewConsultNum}</font>&nbsp;人进行了咨询，
            老关注用户&nbsp;<font color="red" size="3">${resultMap.totalOldConsultNum}</font>&nbsp;人进行了咨询;
            共预约成功<font color="red" size="3">${resultMap.apdocNum}</font>&nbsp;个号;<br/><br/>
        </h5>
    </div>

    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
    <input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
</form:form>

<sys:message content="${message}"/>

<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
    <thead>
    <tr>
        <th class="sort-column name">
            日期
        </th>
        <th  class="sort-column name">
            用户
        </th>
        <th  class="sort-column name">
            来源
        </th>
        <th  class="sort-column name">
            关注时间
        </th>
        <th  class="sort-column name">
            咨询次数
        </th>
        <th class="sort-column name">
            用户预约医生
        </th>
        <th class="sort-column name">
            所有医院
        </th>
        <th class="sort-column name">
            所有医生
        </th>
        <th class="sort-column name">
            所有科室
        </th>
        <th class="sort-column name">
            某医院某科室医生
        </th>
        <th class="sort-column name">
            一类疾病
        </th>
        <th class="sort-column name">
            一类疾病下的二类疾病
        </th>
        <th class="sort-column name">
            二类疾病医生
        </th>
        <th class="sort-column name">
            二类疾病关联医生
        </th>
        <th class="sort-column name">
            二级疾病关联医院
        </th>
        <th class="sort-column name">
            某日期可预约医院
        </th>
        <th class="sort-column name">
            某日期可预约医生
        </th>
        <th class="sort-column name">
            某日期可预约某医院医生
        </th>
        <th class="sort-column name">
            某医生信息
        </th>
        <th class="sort-column name">
            某医生某天加号信息
        </th>
        <th class="sort-column name">
            某医生某个号信息
        </th>
        <th class="sort-column name">
            用户取消预约
        </th>
        <th class="sort-column name">
            用户评价医生
        </th>
        <th class="sort-column name">
            用户分享
        </th>
        <th class="sort-column name">
            用户预约挂号提醒
        </th>
        <th class="sort-column name">
            我的关注医生信息
        </th>
        <th class="sort-column name">
            号源交通信息
        </th>
        <th class="sort-column name">
            号源的状态
        </th>
        <th class="sort-column name">
            医生就诊信息
        </th>
        <th class="sort-column name">
            医生7天内的出诊位置
        </th>
        <th class="sort-column name">
            某出诊地点7天内的出诊信息
        </th>
        <th class="sort-column name">
            个人主页
        </th>
        <th class="sort-column name">
            我的预约
        </th>
        <th class="sort-column name">
            预约信息详情
        </th>
        <th class="sort-column name">
            获取个人信息
        </th>
        <th class="sort-column name">
            关注医生
        </th>
    </tr>
    </thead>

    <tbody id="treeTableList">
    <c:forEach items="${resultMap.statisticData}" var="statisticsVo">
        <tr>
            <td>
                    ${statisticsVo.date}
            </td>
            <td>
                    ${statisticsVo.name}
            </td>
            <td>
                    ${statisticsVo.marketid}
            </td>
            <td>
                    ${statisticsVo.attentiondate}
            </td>
            <td>
                    ${statisticsVo.consultTimes}
            </td>
            <td>
                    ${statisticsVo.apdoc}
            </td>
            <td>
                    ${statisticsVo.alhos}
            </td>
            <td>
                    ${statisticsVo.ahosaldoc}
            </td>
            <td>
                    ${statisticsVo.ahosaldep}
            </td>
            <td>
                    ${statisticsVo.ahosadepaldoc}
            </td>
            <td>
                    ${statisticsVo.alfirstill}
            </td>
            <td>
                    ${statisticsVo.afirstalsecill}
            </td>
            <td>
                    ${statisticsVo.asecillaldoc}
            </td>
            <td>
                    ${statisticsVo.asecillahosaldoc}
            </td>
            <td>
                    ${statisticsVo.asecillalhos}
            </td>
            <td>
                    ${statisticsVo.adatealhos}
            </td>
            <td>
                    ${statisticsVo.adatealdoc}
            </td>
            <td>
                    ${statisticsVo.adateahosaldoc}
            </td>
            <td>
                    ${statisticsVo.adocdetail}
            </td>
            <td>
                    ${statisticsVo.adocadateserv}
            </td>
            <td>
                    ${statisticsVo.adocaservdetail}
            </td>
            <td>
                    ${statisticsVo.canceldoc}
            </td>
            <td>
                    ${statisticsVo.appraisedoc}
            </td>
            <td>
                    ${statisticsVo.sharedoc}
            </td>
            <td>
                    ${statisticsVo.servattention}
            </td>
            <td>
                    ${statisticsVo.attentiondoc}
            </td>
            <td>
                    ${statisticsVo.checkservroute}
            </td>
            <td>
                    ${statisticsVo.checkservstatus}
            </td>
            <td>
                    ${statisticsVo.checkdocaplocation}
            </td>
            <td>
                    ${statisticsVo.checkdoc7daylocation}
            </td>
            <td>
                    ${statisticsVo.checkdoc7dayapbylocation}
            </td>
            <td>
                    ${statisticsVo.myfirstpage}
            </td>
            <td>
                    ${statisticsVo.myappointment}
            </td>
            <td>
                    ${statisticsVo.myapdetail}
            </td>
            <td>
                    ${statisticsVo.myselfinfo}
            </td>
            <td>
                    ${statisticsVo.attdoc}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>