package com.cxqm.xiaoerke.modules.search.service.util;

import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.modules.search.entity.SearchKeyword;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @create author 得良
 */
@Service
@Transactional(readOnly = false)
public class RdsDataSourceJDBC {

    @Autowired
    private HospitalInfoService hospitalInfoService;

    /**
     * 查询所有的热词
     * flag 为"notlike"，表示不是模糊查询；为"like",表示是模糊查询；为"all",表示查询全部
     */
    public List<SearchKeyword> findAllKeyWord(String keyword, String flag) {
        DataSource dataSource = SpringContextHolder.getBean("rdsDataSource");
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<SearchKeyword> lists = new ArrayList<SearchKeyword>();
        try {
            con = dataSource.getConnection();
            String sql = "";
            if ("like".equals(flag)) {
                sql = "select * from search_keywords where  keyword like '%" + keyword + "%'";
            } else if ("all".equals(flag)) {
                sql = "select * from search_keywords ";
            } else if ("notlike".equals(flag)) {
                sql = "select * from search_keywords where  keyword = '" + keyword + "'";
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                SearchKeyword searchKeyword = new SearchKeyword();
                searchKeyword.setId(rs.getInt("id"));
                searchKeyword.setKeyword(rs.getString("keyword"));
                lists.add(searchKeyword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource(rs, ps, con);
        }
        return lists;
    }


    /**
     * 更新医生表信息
     * @param doctorVo
     * @return
     */
    public List<SearchKeyword> updateSysDoctorById(DoctorVo doctorVo) {
        DataSource dataSource = SpringContextHolder.getBean("rdsDataSource");
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<SearchKeyword> lists = new ArrayList<SearchKeyword>();
        try {
            con = dataSource.getConnection();
            String sql = "update sys_doctor set name='"+doctorVo.getDoctorName()+"', hospitalName='"+doctorVo.getHospital()+"',career_time='"+doctorVo.getCareerTimeForDisplay()+"',position1='"+doctorVo.getPosition1()+"',position2='"+doctorVo.getPosition2() +
                    "',personal_details='"+doctorVo.getPersonDetails()+"',experince='"+doctorVo.getPersonDetails()+"',card_experince='"+doctorVo.getCardExperience()+"',subsidy='"+doctorVo.getSubsidy()+"' where id='"+doctorVo.getId()+"'";
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource(rs, ps, con);
        }
        return lists;
    }

    /**
     * 同步医院信息
     *
     * @param hospitalVo
     */
    public void insertHospitalToRds(HospitalVo hospitalVo) {
        DataSource dataSource = SpringContextHolder.getBean("rdsDataSource");
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("INSERT INTO sys_hospital(id,name,position,details,cityName,medical_process,contact_name,contact_phone,special_discount,charge_standard,medical_examination,hospital_type) VALUES ('");
            sbSQL.append(hospitalVo.getId());
            sbSQL.append("','");
            sbSQL.append(hospitalVo.getName());
            sbSQL.append("','");
            sbSQL.append(hospitalVo.getPosition());
            sbSQL.append("','");
            sbSQL.append(hospitalVo.getDetails());
            sbSQL.append("','");
            sbSQL.append(hospitalVo.getCityName());
            sbSQL.append("','");
            sbSQL.append(hospitalVo.getMedicalProcess());
            sbSQL.append("','");
            sbSQL.append(hospitalVo.getContactName());
            sbSQL.append("','");
            sbSQL.append(hospitalVo.getContactPhone());
            sbSQL.append("','");
            sbSQL.append(hospitalVo.getSpecialDiscount());
            sbSQL.append("','");
            sbSQL.append(hospitalVo.getChargeStandard());
            sbSQL.append("','");
            sbSQL.append(hospitalVo.getHospitalType());
            sbSQL.append("','");
            sbSQL.append(hospitalVo.getMedicalExamination());

            sbSQL.append("')");
            ps = con.prepareStatement(sbSQL.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource(rs, ps, con);
        }
    }
    /**
     * 查询与该热词有关联的疾病表主键
     *
     * @param keyWordId
     * @return
     */
    public List<String> findAllillnessKeyByKeyword(String keyWordId) {
        DataSource dataSource = SpringContextHolder.getBean("rdsDataSource");
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<String> lists = new ArrayList<String>();
        try {
            con = dataSource.getConnection();
            StringBuilder sbSql = new StringBuilder();
            sbSql.append("select illness_id from search_keywords_illness_rel where  keyword_id = '");
            sbSql.append(keyWordId);
            sbSql.append("'");
            ps = con.prepareStatement(sbSql.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(rs.getString("illness_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource(rs, ps, con);
        }
        return lists;
    }

    /**
     * 保存热词
     */
    public void saveKeyWord(String keyword) {
        DataSource dataSource = SpringContextHolder.getBean("rdsDataSource");
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            int id = Integer.parseInt(ChangzhuoMessageUtil.createRandom(true, 8));
            StringBuilder sbSql = new StringBuilder();
            sbSql.append("INSERT into search_keywords(id,keyword) VALUES('");
            sbSql.append(id);
            sbSql.append("','");
            sbSql.append(keyword);
            sbSql.append("')");
            ps = con.prepareStatement(sbSql.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource(rs, ps, con);
        }
    }

    /**
     * 删除热词
     *
     * @param id
     */
    public void deletekeyword(String id) {
        DataSource dataSource = SpringContextHolder.getBean("rdsDataSource");
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            //根据id删除search_keywords表中的热词
            StringBuilder sbSql = new StringBuilder();
            sbSql.append("delete from search_keywords where id=");
            sbSql.append(Integer.parseInt(id));
            ps = con.prepareStatement(sbSql.toString());
            ps.executeUpdate();

            //根据id删除search_keywords_illness_rel表中的与该热词相关的记录
            StringBuilder sbSql2 = new StringBuilder();
            sbSql2.append("delete from search_keywords_illness_rel where keyword_id=");
            sbSql2.append(Integer.parseInt(id));
            ps = con.prepareStatement(sbSql2.toString());
            ps.executeUpdate();

            //根据id删除search_keywords_illness_rel表中的与该热词相关的记录
            StringBuilder sbSql3 = new StringBuilder();
            sbSql3.append("delete from doctor_searchkeyword_rel where keyword_id=");
            sbSql3.append(Integer.parseInt(id));
            ps = con.prepareStatement(sbSql3.toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource(rs, ps, con);
        }
    }

    /**
     * 删除医生
     *
     * @param id
     */
    public void deletedoctor(DoctorVo doctorVo) {
        DataSource dataSource = SpringContextHolder.getBean("rdsDataSource");
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            String sql = "DELETE from sys_doctor where id= '"+doctorVo.getId()+"'";
            ps = con.prepareStatement(sql);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource(rs, ps, con);
        }
    }

    /**
     * 保存疾病与热词关系
     */
    public void deleteAndSaveKeyWordAndIllnessRelation(String tArray, String keyWordId) {
        DataSource dataSource = SpringContextHolder.getBean("rdsDataSource");
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            //根据keyWordId删除search_keywords_illness_rel表中的与该热词相关的记录
            StringBuilder sbSql = new StringBuilder();
            sbSql.append("delete from search_keywords_illness_rel where keyword_id=");
            sbSql.append(Integer.parseInt(keyWordId));
            ps = con.prepareStatement(sbSql.toString());
            ps.executeUpdate();
            //批量将疾病与热词的关系插入到数据库
            ps = con.prepareStatement("insert into search_keywords_illness_rel(id,illness_id,keyword_id) VALUES(?,?,?);");
            String[] strings = tArray.split(",");
            for (String str : strings) {
                ps.setInt(1, Integer.parseInt(ChangzhuoMessageUtil.createRandom(true, 9)));
                ps.setString(2, str);
                ps.setInt(3, Integer.parseInt(keyWordId));
                ps.addBatch();//加入批处理，进行打包
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource(rs, ps, con);
        }
    }

    /**
     * 同步医生信息
     *
     * @param doctorMap
     */
    public void insertDoctorToRds(HashMap<String, Object> doctorMap ,String insertRelationFlag) {
        DataSource dataSource = SpringContextHolder.getBean("rdsDataSource");
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();

            java.util.Date create_date = (java.util.Date) doctorMap.get("create_date");
            java.sql.Date create_date2 = new java.sql.Date(create_date.getTime());

            java.util.Date career_time = (java.util.Date) doctorMap.get("career_time");
            java.sql.Date career_time2 = new java.sql.Date(career_time.getTime());
            //构造医生数据，用StringBuilder
            StringBuilder sbSql = new StringBuilder();
            sbSql.append("INSERT INTO sys_doctor(id,sys_user_id,name,hospitalName,career_time,personal_details,");
            sbSql.append("position1,position2,fans_number,experince,qrcode,create_date) VALUES ('");
            sbSql.append(doctorMap.get("sysDoctorId"));
            sbSql.append("','");
            sbSql.append(doctorMap.get("sys_user_id"));
            sbSql.append("','");
            sbSql.append(doctorMap.get("doctorName"));
            sbSql.append("','");
            sbSql.append(doctorMap.get("hospitalName"));
            sbSql.append("','");
            sbSql.append(career_time2);
            sbSql.append("','");
            sbSql.append(doctorMap.get("personal_details"));
            sbSql.append("','");
            sbSql.append(doctorMap.get("position1"));
            sbSql.append("','");
            sbSql.append(doctorMap.get("position2"));
            sbSql.append("','");
            sbSql.append(doctorMap.get("fans_number"));
            sbSql.append("','");
            sbSql.append(doctorMap.get("experince"));
            sbSql.append("','");
            sbSql.append(doctorMap.get("ticket"));
            sbSql.append("','");
            sbSql.append(create_date2);
            sbSql.append("')");

            ps = con.prepareStatement(sbSql.toString());
            ps.executeUpdate();
            //插入医院与医生的关联关系
            if(insertRelationFlag.equals("yes")){
//                ps = insertDoctorAndHospitalRelationToRds(doctorMap, con, ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource(rs, ps, con);
        }
    }

    /**
     * 插入医生与医院的关联关系
     *
     * @param doctorMap
     * @param con
     * @param ps
     * @return
     * @throws SQLException
     */
    private PreparedStatement insertDoctorAndHospitalRelationToRds(HashMap<String, Object> doctorMap, Connection con, PreparedStatement ps) throws SQLException {
        //构造医生与医院关系数据
        HashMap<String, Object> relationMap = hospitalInfoService.findHospitalIdByHospitalName(doctorMap);
        String id = IdGen.uuid();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO doctor_hospital_relation(id,sys_doctor_id,sys_hospital_id,relation_type,department_level1,department_level2,contact_person,contact_person_phone) VALUES ('");
        stringBuilder.append(id);
        stringBuilder.append("','");
        stringBuilder.append(doctorMap.get("sysDoctorId"));
        stringBuilder.append("','");
        stringBuilder.append(relationMap.get("hospitalId"));
        stringBuilder.append("','");
        stringBuilder.append(doctorMap.get("relation_type"));
        stringBuilder.append("','");
        stringBuilder.append(doctorMap.get("department_level1"));
        stringBuilder.append("','");
        stringBuilder.append(doctorMap.get("department_level2"));
        stringBuilder.append("','");
        stringBuilder.append(doctorMap.get("contact_person_phone"));
        stringBuilder.append("','");
        stringBuilder.append(doctorMap.get("contact_person"));
        stringBuilder.append("')");
        ps = con.prepareStatement(stringBuilder.toString());
        ps.executeUpdate();
        return ps;
    }

    /**
     * 生成并保存医生与热词关系
     *
     * @param arrays
     * @param doctorId
     */
    public void insertDoctorAndHotWordsRelationToRds(String arrays, String doctorId) {
        DataSource dataSource = SpringContextHolder.getBean("rdsDataSource");
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {

            //查询与疾病关联的热词
            String sql = "SELECT keyword_id from search_keywords_illness_rel where illness_id in (" + arrays + ") GROUP BY keyword_id";
            List<Map> lists = new ArrayList<Map>();//存放map的list
            Map map = new HashMap();//存放数据的map
            con = dataSource.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put("keyword_id", rs.getString("keyword_id"));
                lists.add(map);
            }

            //删除医生与热词的关联关系
            String deleteSQL = "delete from doctor_searchkeyword_rel where sys_doctor_id='" + doctorId + "';";
            ps = con.prepareStatement(deleteSQL);
            ps.executeUpdate(deleteSQL);

            //批量插入医生与热词的关联关系
            ps = con.prepareStatement("insert into doctor_searchkeyword_rel(id,sys_doctor_id,keyword_id) VALUES (?,?,?)");
            for (Map newMap : lists) {
                ps.setInt(1, Integer.parseInt(ChangzhuoMessageUtil.createRandom(true, 9)));
                ps.setString(2, doctorId);
                ps.setInt(3, Integer.parseInt((String) newMap.get("keyword_id")));
                ps.addBatch();//加入批处理，进行打包
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource(rs, ps, con);
        }
    }

    /**
     * jdbc批处理操作
     *
     * @param conn
     */
    public void exec3(Connection conn) {
        try {
            PreparedStatement pst = conn.prepareStatement("insert into t1(id) values (?)");
            for (int i = 1; i <= 10000; i++) {
                pst.setInt(1, i);
                pst.addBatch();//加入批处理，进行打包
                if (i % 1000 == 0) {//可以设置不同的大小；如50，100，500，1000等等
                    pst.executeBatch();
                    conn.commit();
                    pst.clearBatch();
                }
            }
            pst.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void closeSource(ResultSet rs, Statement stat, Connection con) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
