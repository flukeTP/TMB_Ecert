package com.tmb.ecert.report.persistence.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.batchjob.constant.BatchJobConstant.PAID_TYPE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.common.constant.DateConstant;
import com.tmb.ecert.report.persistence.vo.Rep01000FormVo;
import com.tmb.ecert.report.persistence.vo.Rep01000Vo;
import com.tmb.ecert.report.persistence.vo.Rep02000FormVo;
import com.tmb.ecert.report.persistence.vo.Rep02000Vo;
import com.tmb.ecert.report.persistence.vo.Rep02100FormVo;
import com.tmb.ecert.report.persistence.vo.Rep02100Vo;
import com.tmb.ecert.report.persistence.vo.Rep02200FormVo;
import com.tmb.ecert.report.persistence.vo.Rep02200Vo;
import com.tmb.ecert.report.persistence.vo.Rep03000FormVo;
import com.tmb.ecert.report.persistence.vo.Rep03000Vo;
import com.tmb.ecert.report.persistence.vo.ReqReceiptVo;

import th.co.baiwa.buckwaframework.common.bean.DatatableSort;
import th.co.baiwa.buckwaframework.common.util.DatatableUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Repository
public class RepDao {
	private Logger log = LoggerFactory.getLogger(RepDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Rep01000Vo> getDataRep01000(Rep01000FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		List<Rep01000Vo> rep01000VoList = new ArrayList<Rep01000Vo>();

		sql.append(" SELECT a.*,b.NAME AS CERTYPE_DESC, ");
		sql.append(" c.NAME AS CUSTSEGMENT_DESC, ");
		sql.append(" d.NAME AS PAIDTYPE_DESC ");
		sql.append(" FROM ECERT_REQUEST_FORM a ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE b on a.CERTYPE_CODE = b.CODE  ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE c on a.CUSTSEGMENT_CODE = c.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE d on a.PAIDTYPE_CODE = d.CODE ");
		sql.append(
				" WHERE a.DELETE_FLAG = 0 AND (a.STATUS = '10003' OR a.STATUS = '10004' OR a.STATUS = '10007' OR a.STATUS = '10008' OR a.STATUS = '10009' OR a.STATUS = '10010') ");

		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append(" AND  CAST(a.REQUEST_DATE as DATE) >= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateForm());
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append(" AND  CAST(a.REQUEST_DATE as DATE) <= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateTo());
			params.add(date);
		}
		if (StringUtils.isNoneBlank(formVo.getPaymentDateForm())) {
			sql.append(" AND  CAST(a.PAYMENT_DATE as DATE) >= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getPaymentDateForm());
			params.add(date);
		}
		if (StringUtils.isNoneBlank(formVo.getPaymentDateTo())) {
			sql.append(" AND  CAST(a.PAYMENT_DATE as DATE) <= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getPaymentDateTo());
			params.add(date);
		}

		if (StringUtils.isNotBlank(formVo.getOrganizeId())) {
			sql.append(" AND a.ORGANIZE_ID = ?");
			params.add(formVo.getOrganizeId());
		}
		if (StringUtils.isNotBlank(formVo.getCompanyName())) {
			sql.append(" AND a.COMPANY_NAME = ? ");
			params.add(formVo.getCompanyName());
		}
		if (StringUtils.isNotBlank(formVo.getRequestTypeCode())) {
			sql.append(" AND a.CERTYPE_CODE = ?");
			params.add(formVo.getRequestTypeCode());
		}

		if (StringUtils.isNotBlank(formVo.getPaidtypeCode())) {
			sql.append(" AND a.PAIDTYPE_CODE = ?");
			params.add(formVo.getPaidtypeCode());
		}

		sql.append(" ORDER BY a.REQUEST_DATE DESC ");
		log.info("sqlRep01000 : {}", sql.toString());
		rep01000VoList = jdbcTemplate.query(sql.toString(), params.toArray(), rep01000RowMapper);

		return rep01000VoList;
	}

	public List<Rep01000Vo> getDataRep01000Datatable(Rep01000FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		List<Rep01000Vo> rep01000VoList = new ArrayList<Rep01000Vo>();

		sql.append(" SELECT a.*,b.NAME AS CERTYPE_DESC, ");
		sql.append(" c.NAME AS CUSTSEGMENT_DESC, ");
		sql.append(" d.NAME AS PAIDTYPE_DESC ");
		sql.append(" FROM ECERT_REQUEST_FORM a ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE b on a.CERTYPE_CODE = b.CODE  ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE c on a.CUSTSEGMENT_CODE = c.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE d on a.PAIDTYPE_CODE = d.CODE ");
		sql.append(
				" WHERE a.DELETE_FLAG = 0 AND (a.STATUS = '10003' OR a.STATUS = '10004' OR a.STATUS = '10007' OR a.STATUS = '10008' OR a.STATUS = '10009' OR a.STATUS = '10010') ");

		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append(" AND  CAST(a.REQUEST_DATE as DATE) >= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateForm());
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append(" AND  CAST(a.REQUEST_DATE as DATE) <= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateTo());
			params.add(date);
		}
		if (StringUtils.isNoneBlank(formVo.getPaymentDateForm())) {
			sql.append(" AND  CAST(a.PAYMENT_DATE as DATE) >= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getPaymentDateForm());
			params.add(date);
		}
		if (StringUtils.isNoneBlank(formVo.getPaymentDateTo())) {
			sql.append(" AND  CAST(a.PAYMENT_DATE as DATE) <= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getPaymentDateTo());
			params.add(date);
		}

		if (StringUtils.isNotBlank(formVo.getOrganizeId())) {
			sql.append(" AND a.ORGANIZE_ID = ?");
			params.add(formVo.getOrganizeId());
		}
		if (StringUtils.isNotBlank(formVo.getCompanyName())) {
			sql.append(" AND a.COMPANY_NAME = ? ");
			params.add(formVo.getCompanyName());
		}
		if (StringUtils.isNotBlank(formVo.getRequestTypeCode())) {
			sql.append(" AND a.CERTYPE_CODE = ?");
			params.add(formVo.getRequestTypeCode());
		}

		if (StringUtils.isNotBlank(formVo.getPaidtypeCode())) {
			sql.append(" AND a.PAIDTYPE_CODE = ?");
			params.add(formVo.getPaidtypeCode());
		}

		if (!formVo.getSort().isEmpty()) {
			sql.append("ORDER BY ");
			List<String> orders = new ArrayList<>();
			for (DatatableSort item : formVo.getSort()) {
				orders.add(item.getColumn() + " " + item.getOrder());
			}
			sql.append(StringUtils.join(orders, ", "));
		} else {
			// default order
			sql.append(" ORDER BY a.REQUEST_DATE DESC ");
		}

		log.info("sqlRep01000 : {}", sql.toString());
//		rep01000VoList = jdbcTemplate.query(sql.toString(), params.toArray(), rep01000RowMapper);
		rep01000VoList = jdbcTemplate.query(
				DatatableUtils.limitForDataTable(sql.toString(), formVo.getStart(), formVo.getLength()),
				params.toArray(), rep01000RowMapper);

		return rep01000VoList;
	}

	public int getDataRep01000Count(Rep01000FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();

		sql.append(" SELECT a.*,b.NAME AS CERTYPE_DESC, ");
		sql.append(" c.NAME AS CUSTSEGMENT_DESC, ");
		sql.append(" d.NAME AS PAIDTYPE_DESC ");
		sql.append(" FROM ECERT_REQUEST_FORM a ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE b on a.CERTYPE_CODE = b.CODE  ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE c on a.CUSTSEGMENT_CODE = c.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE d on a.PAIDTYPE_CODE = d.CODE ");
		sql.append(
				" WHERE a.DELETE_FLAG = 0 AND (a.STATUS = '10003' OR a.STATUS = '10004' OR a.STATUS = '10007' OR a.STATUS = '10008' OR a.STATUS = '10009' OR a.STATUS = '10010') ");

		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append(" AND  CAST(a.REQUEST_DATE as DATE) >= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateForm());
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append(" AND  CAST(a.REQUEST_DATE as DATE) <= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateTo());
			params.add(date);
		}
		if (StringUtils.isNoneBlank(formVo.getPaymentDateForm())) {
			sql.append(" AND  CAST(a.PAYMENT_DATE as DATE) >= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getPaymentDateForm());
			params.add(date);
		}
		if (StringUtils.isNoneBlank(formVo.getPaymentDateTo())) {
			sql.append(" AND  CAST(a.PAYMENT_DATE as DATE) <= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getPaymentDateTo());
			params.add(date);
		}

		if (StringUtils.isNotBlank(formVo.getOrganizeId())) {
			sql.append(" AND a.ORGANIZE_ID = ?");
			params.add(formVo.getOrganizeId());
		}
		if (StringUtils.isNotBlank(formVo.getCompanyName())) {
			sql.append(" AND a.COMPANY_NAME = ? ");
			params.add(formVo.getCompanyName());
		}
		if (StringUtils.isNotBlank(formVo.getRequestTypeCode())) {
			sql.append(" AND a.CERTYPE_CODE = ?");
			params.add(formVo.getRequestTypeCode());
		}

		if (StringUtils.isNotBlank(formVo.getPaidtypeCode())) {
			sql.append(" AND a.PAIDTYPE_CODE = ?");
			params.add(formVo.getPaidtypeCode());
		}

		BigDecimal rs = jdbcTemplate.queryForObject(DatatableUtils.countForDatatable(sql.toString()), BigDecimal.class,
				params.toArray());
		return rs.intValue();
	}

	private RowMapper<Rep01000Vo> rep01000RowMapper = new RowMapper<Rep01000Vo>() {
		@Override
		public Rep01000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Rep01000Vo vo = new Rep01000Vo();
			vo.setId(rs.getLong("REQFORM_ID"));
			vo.setRequestDate(DateConstant.convertDateToStrDDMMYYYY(rs.getDate("REQUEST_DATE")));
			vo.setTmbRequestno(rs.getString("TMB_REQUESTNO"));
			vo.setOrganizeId(rs.getString("ORGANIZE_ID"));
			vo.setCompanyName(rs.getString("COMPANY_NAME"));
			vo.setPaymentDate(DateConstant.convertDateToStrDDMMYYYY(rs.getDate("PAYMENT_DATE")));

			vo.setCustsegmentCode(rs.getString("CUSTSEGMENT_CODE"));
			vo.setCustsegmentDesc(rs.getString("CUSTSEGMENT_DESC"));

			vo.setCertypeCode(rs.getString("CERTYPE_CODE"));
			vo.setCertypeDesc(rs.getString("CERTYPE_DESC"));

			vo.setAccountNo(rs.getString("ACCOUNT_NO"));

//	    		Float totalAmountDbdVat = 0f;
//	    		totalAmountDbdVat=convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_DBD"))*0.93f;
//	    		vo.setAmountDbd(new BigDecimal(totalAmountDbdVat).setScale(2, BigDecimal.ROUND_HALF_EVEN));  
//	    		
//	    		Float totalAmountTmbVat = 0f;
//	    		totalAmountTmbVat=convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_TMB"))*0.93f;
//	    		vo.setAmountTmb(new BigDecimal(totalAmountTmbVat).setScale(2, BigDecimal.ROUND_HALF_EVEN));  
//	    		
//	    		vo.setAmount(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT"))); 
//	    		
//	    		Float totalAmountVat = 0f;
//	    		totalAmountVat=(convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_TMB"))*0.07f)+(convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_DBD"))*0.07f);
//	    		vo.setTotalAmountVat(new BigDecimal(totalAmountVat).setScale(2, BigDecimal.ROUND_HALF_EVEN));   

//	    		String vatpercent = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.VAT_PERCENT);
			Float tmbAmountTmbVat = 0f;
			Float dbdAmountTmbVat = 0f;
			Float tmbAmountVat = 0f;
			Float dbdAmountVat = 0f;
//	    		BigDecimal vatSum = new BigDecimal(0);
			BigDecimal vattmb = new BigDecimal(0);
//	    		BigDecimal vatdbd = new BigDecimal(0);

			tmbAmountVat = (convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_VAT_TMB")));
//	    		dbdAmountVat= (convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_DBD")) * Float.parseFloat(vatpercent) / (100 + Float.parseFloat(vatpercent) ));

			tmbAmountTmbVat = convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_TMB")) - tmbAmountVat;
			dbdAmountTmbVat = convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_DBD"));

			vattmb = new BigDecimal(tmbAmountVat).setScale(2, BigDecimal.ROUND_HALF_EVEN);
//	    		vatdbd = new BigDecimal(dbdAmountVat).setScale(2, BigDecimal.ROUND_HALF_EVEN);
//	    		vatSum = vatSum.add(vattmb);
//	    		vatSum = vatSum.add(vatdbd);

			vo.setAmount(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT")));
			vo.setAmountTmb(new BigDecimal(tmbAmountTmbVat).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			vo.setAmountDbd(new BigDecimal(dbdAmountTmbVat).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			vo.setTotalAmountVat(vattmb.setScale(2, BigDecimal.ROUND_HALF_EVEN));

			vo.setPaidtypeCode(rs.getString("PAIDTYPE_CODE"));
			vo.setPaidtypeDesc(rs.getString("PAIDTYPE_DESC"));

			vo.setMakerById(rs.getString("MAKER_BY_ID"));
			vo.setMakerByName(rs.getString("MAKER_BY_NAME"));

			vo.setCheckerById(rs.getString("CHECKER_BY_ID"));
			vo.setCheckerByName(rs.getString("CHECKER_BY_NAME"));

			vo.setStatus(rs.getString("STATUS"));
			vo.setRemark(rs.getString("REMARK"));
			return vo;
		}
	};

	public List<Rep01000Vo> getRequestTypeRep01000(Long id) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		List<Rep01000Vo> rep01000VoList = new ArrayList<Rep01000Vo>();

		sql.append(
				" SELECT b.CERTIFICATE AS REQUEST_TYPE_DESC FROM ECERT_REQUEST_CERTIFICATE a LEFT JOIN ECERT_CERTIFICATE b on a.CERTIFICATE_CODE = b.CODE where a.REQFORM_ID = ? ");

		params.add(id);

		rep01000VoList = jdbcTemplate.query(sql.toString(), params.toArray(), rep01000RequestTypeRowMapper);

		return rep01000VoList;
	}

	private RowMapper<Rep01000Vo> rep01000RequestTypeRowMapper = new RowMapper<Rep01000Vo>() {
		@Override
		public Rep01000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Rep01000Vo vo = new Rep01000Vo();

			vo.setRequestTypeDesc(rs.getString("REQUEST_TYPE_DESC"));

			return vo;
		}
	};

	public List<Rep02000Vo> getCountCertificateRep02000(Rep02000FormVo formVo, Rep02000Vo vo) {
		StringBuilder sql = new StringBuilder("");
		sql.append("  SELECT    ");
		sql.append("   sum(b.TOTALNUMBER) AS SUM     ");
		sql.append("   FROM ECERT_REQUEST_FORM a      ");
		sql.append("   LEFT JOIN ECERT_LISTOFVALUE c on a.CUSTSEGMENT_CODE = c.CODE   ");
		sql.append("   LEFT JOIN ECERT_REQUEST_CERTIFICATE b on a.REQFORM_ID = b.REQFORM_ID    ");
		sql.append(
				"   WHERE a.DELETE_FLAG = 0 AND (a.STATUS = '10003' OR a.STATUS = '10004' OR a.STATUS = '10007'OR a.STATUS = '10008' OR a.STATUS = '10009' OR a.STATUS = '10010')");
		sql.append("   AND (b.CERTIFICATE_CODE = '10001' OR b.CERTIFICATE_CODE = '20001')  ");
		sql.append("   AND c.CODE = ? ");

		List<Object> params = new ArrayList<>();

		params.add(vo.getCustsegmentCode());

		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) >= MONTH(?) AND YEAR(a.REQUEST_DATE)>= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateForm());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) <= MONTH(?) AND YEAR(a.REQUEST_DATE)<= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateTo());
			params.add(date);
			params.add(date);
		}

		if (StringUtils.isNotBlank(formVo.getPaidtypeCode())) {
			sql.append(" AND a.PAIDTYPE_CODE = ?");
			params.add(formVo.getPaidtypeCode());
		}
		sql.append(" GROUP BY c.NAME,c.CODE,a.DEPARTMENT ORDER BY c.CODE ");

//			log.info("sql getCountCertificateRep02000 : {}",sql.toString());

		List<Rep02000Vo> voReturn = new ArrayList<Rep02000Vo>();
		voReturn = jdbcTemplate.query(sql.toString(), params.toArray(), cRowMapper);

		return voReturn;
	}

	private RowMapper<Rep02000Vo> cRowMapper = new RowMapper<Rep02000Vo>() {
		@Override
		public Rep02000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Rep02000Vo vo = new Rep02000Vo();

			vo.setCertificate((rs.getInt("SUM")));

			return vo;
		}
	};

	public List<Rep02000Vo> getCountUnCertificateRep02000(Rep02000FormVo formVo, Rep02000Vo vo) {
		StringBuilder sql = new StringBuilder("");
		sql.append("  SELECT    ");
		sql.append("   sum(b.TOTALNUMBER) AS SUM     ");
		sql.append("   FROM ECERT_REQUEST_FORM a      ");
		sql.append("   LEFT JOIN ECERT_LISTOFVALUE c on a.CUSTSEGMENT_CODE = c.CODE   ");
		sql.append("   LEFT JOIN ECERT_REQUEST_CERTIFICATE b on a.REQFORM_ID = b.REQFORM_ID    ");
		sql.append(
				"   WHERE a.DELETE_FLAG = 0 and (a.STATUS = '10003' OR a.STATUS = '10004' OR a.STATUS = '10007'OR a.STATUS = '10008' OR a.STATUS = '10009' OR a.STATUS = '10010')");
		sql.append("   AND (b.CERTIFICATE_CODE != '10001' AND b.CERTIFICATE_CODE != '20001')  ");
		sql.append("   AND c.CODE = ? ");

		List<Object> params = new ArrayList<>();
		int count = 0;

		params.add(vo.getCustsegmentCode());

		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) >= MONTH(?) AND YEAR(a.REQUEST_DATE)>= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateForm());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) <= MONTH(?) AND YEAR(a.REQUEST_DATE)<= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateTo());
			params.add(date);
			params.add(date);
		}

		if (StringUtils.isNotBlank(formVo.getPaidtypeCode())) {
			sql.append(" AND a.PAIDTYPE_CODE = ?");
			params.add(formVo.getPaidtypeCode());
		}
		sql.append(" GROUP BY c.NAME,c.CODE,a.DEPARTMENT ORDER BY c.CODE ");

//			log.info("sql getUnCountCertificateRep02000 : {}",sql.toString());

		List<Rep02000Vo> voReturn = new ArrayList<Rep02000Vo>();
		voReturn = jdbcTemplate.query(sql.toString(), params.toArray(), unCerRowMapper);

		return voReturn;
	}

	private RowMapper<Rep02000Vo> unCerRowMapper = new RowMapper<Rep02000Vo>() {
		@Override
		public Rep02000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Rep02000Vo vo = new Rep02000Vo();

			vo.setCopyGuarantee(rs.getInt("SUM"));

			return vo;
		}
	};

	public int getPaymentTypeCountRep02000(Rep02000FormVo formVo, Rep02000Vo vo, String paidtypeCode) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT count(*) FROM ECERT_REQUEST_FORM a ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE b on a.CUSTSEGMENT_CODE = b.CODE  ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE d on a.PAIDTYPE_CODE = d.CODE ");
		sql.append(
				" WHERE a.DELETE_FLAG = 0 and (a.STATUS = '10003' OR a.STATUS = '10004' OR a.STATUS = '10007' OR a.STATUS = '10008' OR a.STATUS = '10009' OR a.STATUS = '10010') ");
		sql.append(" AND b.CODE = ? AND a.PAIDTYPE_CODE = ? ");
		List<Object> params = new ArrayList<>();
		int count = 0;

		params.add(vo.getCustsegmentCode());
		params.add(paidtypeCode);

		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) >= MONTH(?) AND YEAR(a.REQUEST_DATE)>= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateForm());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) <= MONTH(?) AND YEAR(a.REQUEST_DATE)<= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateTo());
			params.add(date);
			params.add(date);
		}

		if (StringUtils.isNotBlank(formVo.getPaidtypeCode())) {
			sql.append(" AND a.PAIDTYPE_CODE = ?");
			params.add(formVo.getPaidtypeCode());
		}

		count = jdbcTemplate.queryForObject(sql.toString(), params.toArray(), int.class);
		return count;
	}

	public int getCountStatusRep02000(Rep02000FormVo formVo, Rep02000Vo vo) {
		StringBuilder sql = new StringBuilder(
				" SELECT count(*) AS COUNT FROM ECERT_REQUEST_FORM a WHERE a.DELETE_FLAG=0 and  (a.STATUS = '10009' OR a.STATUS = '10010') AND a.CUSTSEGMENT_CODE = ? ");
		List<Object> params = new ArrayList<>();
		int count = 0;

		params.add(vo.getCustsegmentCode());

		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) >= MONTH(?) AND YEAR(a.REQUEST_DATE)>= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateForm());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) <= MONTH(?) AND YEAR(a.REQUEST_DATE)<= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateTo());
			params.add(date);
			params.add(date);
		}

		if (StringUtils.isNotBlank(formVo.getPaidtypeCode())) {
			sql.append(" AND a.PAIDTYPE_CODE = ?");
			params.add(formVo.getPaidtypeCode());
		}

		count = jdbcTemplate.queryForObject(sql.toString(), params.toArray(), int.class);
		return count;
	}

	public List<Rep02000Vo> getDataRep02000(Rep02000FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		List<Rep02000Vo> rep02000VoList = new ArrayList<Rep02000Vo>();

		sql.append("  SELECT  ");
		sql.append("  c.NAME AS CUSTSEGMENT_DESC,c.CODE AS CUSTSEGMENT_CODE,count(*) AS CUSTSEGMENT_COUNT , ");
		sql.append("  sum(a.AMOUNT_DBD) AS AMOUNT_DBD  , ");
		sql.append("  sum(a.AMOUNT_TMB) AS AMOUNT_TMB  , ");
		sql.append("  sum(a.AMOUNT_TMB+a.AMOUNT_DBD) AS AMOUNT_TATOL   ");
		sql.append("  FROM ECERT_REQUEST_FORM a    ");
		sql.append("  LEFT JOIN ECERT_LISTOFVALUE c  ");
		sql.append("  on a.CUSTSEGMENT_CODE = c.CODE  ");
		sql.append("  WHERE a.DELETE_FLAG = 0 and (a.STATUS = '10003' OR a.STATUS = '10004' OR a.STATUS = '10007'  ");
		sql.append("  OR a.STATUS = '10008' OR a.STATUS = '10009' OR a.STATUS = '10010') ");

		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) >= MONTH(?) AND YEAR(a.REQUEST_DATE)>= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateForm());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) <= MONTH(?) AND YEAR(a.REQUEST_DATE)<= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateTo());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getPaidtypeCode())) {
			sql.append(" AND a.PAIDTYPE_CODE = ?");
			params.add(formVo.getPaidtypeCode());
		}

		sql.append("  GROUP BY c.NAME,c.CODE ORDER BY c.CODE ");
//			log.info("sqlRep02000 : {}",sql.toString());
		rep02000VoList = jdbcTemplate.query(sql.toString(), params.toArray(), rep02000RowMapper);

		return rep02000VoList;
	}

	private RowMapper<Rep02000Vo> rep02000RowMapper = new RowMapper<Rep02000Vo>() {
		@Override
		public Rep02000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Rep02000Vo vo = new Rep02000Vo();

			vo.setCustsegmentDesc(rs.getString("CUSTSEGMENT_DESC"));
			vo.setCustsegmentCode(rs.getString("CUSTSEGMENT_CODE"));
			vo.setCustsegmentCount(rs.getInt("CUSTSEGMENT_COUNT"));

			vo.setCertificate(0);
			vo.setCopyGuarantee(0);

			vo.setAmountDbd(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_DBD")));
			vo.setAmountTmb(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_TMB")));
			vo.setTotalAmount(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_TATOL")));

			vo.setDepartment("นิติกรรมสัญญา");
			vo.setSuccess(0);
			vo.setFail(0);

			return vo;
		}
	};

	public List<Rep02100Vo> getDataRep02100(Rep02100FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		List<Rep02100Vo> rep02100VoList = new ArrayList<Rep02100Vo>();

		sql.append(" SELECT a.*,b.NAME AS CERTYPE_DESC, ");
		sql.append(" c.NAME AS CUSTSEGMENT_DESC, ");
		sql.append(" d.NAME AS STATUS_DESC, ");
		sql.append(" e.NAME AS REASON_DESC ");
		sql.append(" FROM ECERT_REQUEST_FORM a ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE b on a.CERTYPE_CODE = b.CODE  ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE c on a.CUSTSEGMENT_CODE = c.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE d on a.STATUS = d.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE e on a.REJECTREASON_CODE = e.CODE ");
		sql.append(
				" WHERE a.DELETE_FLAG = 0 and (a.STATUS = '10003' OR a.STATUS = '10004' OR a.STATUS = '10007' OR a.STATUS = '10008') ");

		if (StringUtils.isNotBlank(formVo.getCustsegmentCode())) {
			sql.append(" AND c.CODE = ?");
			params.add(formVo.getCustsegmentCode());
		}

		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) >= MONTH(?) AND YEAR(a.REQUEST_DATE)>= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateForm());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) <= MONTH(?) AND YEAR(a.REQUEST_DATE)<= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateTo());
			params.add(date);
			params.add(date);
		}

		sql.append(" ORDER BY a.REQUEST_DATE DESC ");

//				log.info("sqlRep02100 : {}",sql.toString());
		rep02100VoList = jdbcTemplate.query(sql.toString(), params.toArray(), rep02100RowMapper);

		return rep02100VoList;
	}

	private RowMapper<Rep02100Vo> rep02100RowMapper = new RowMapper<Rep02100Vo>() {
		@Override
		public Rep02100Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Rep02100Vo vo = new Rep02100Vo();
			vo.setId(rs.getLong("REQFORM_ID"));
			vo.setCustsegmentDesc(rs.getString("CUSTSEGMENT_DESC"));
			vo.setRequestDate(DateConstant.convertDateToStrDDMMYYYY(rs.getDate("REQUEST_DATE")));
			vo.setTmbRequestno(rs.getString("TMB_REQUESTNO"));
			vo.setRef1(rs.getString("REF1"));
			vo.setRef2(rs.getString("REF2"));
			vo.setCertypeDesc(rs.getString("CERTYPE_DESC"));
			vo.setAmount(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT")));
			vo.setOrganizeId(rs.getString("ORGANIZE_ID"));
			vo.setCompanyName(rs.getString("COMPANY_NAME"));
			vo.setStatus(rs.getString("STATUS_DESC"));
			vo.setReason(rs.getString("REASON_DESC"));
			vo.setReasonOther(rs.getString("REJECTREASON_OTHER"));
			return vo;
		}
	};

	public List<Rep02200Vo> getDataRep02200(Rep02200FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		List<Rep02200Vo> rep02200VoList = new ArrayList<Rep02200Vo>();

		sql.append(" SELECT a.*,b.NAME AS CERTYPE_DESC, ");
		sql.append(" c.NAME AS CUSTSEGMENT_DESC, ");
		sql.append(" d.NAME AS STATUS_DESC ");
		sql.append(" FROM ECERT_REQUEST_FORM a ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE b on a.CERTYPE_CODE = b.CODE  ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE c on a.CUSTSEGMENT_CODE = c.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE d on a.STATUS = d.CODE ");
		sql.append(
				" WHERE a.DELETE_FLAG=0 and (a.STATUS = '10003' OR a.STATUS = '10004' OR a.STATUS = '10007' OR a.STATUS = '10008' OR a.STATUS = '10009' OR a.STATUS = '10010') ");

		if (StringUtils.isNotBlank(formVo.getCustsegmentCode())) {
			sql.append(" AND c.CODE = ?");
			params.add(formVo.getCustsegmentCode());
		}

		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) >= MONTH(?) AND YEAR(a.REQUEST_DATE)>= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateForm());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append("  AND  MONTH(a.REQUEST_DATE) <= MONTH(?) AND YEAR(a.REQUEST_DATE)<= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getDateTo());
			params.add(date);
			params.add(date);
		}

		sql.append(" ORDER BY a.REQUEST_DATE DESC ");

		log.info("sqlRep02200 : {}", sql.toString());
		rep02200VoList = jdbcTemplate.query(sql.toString(), params.toArray(), rep02200RowMapper);

		return rep02200VoList;
	}

	private RowMapper<Rep02200Vo> rep02200RowMapper = new RowMapper<Rep02200Vo>() {
		@Override
		public Rep02200Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Rep02200Vo vo = new Rep02200Vo();

			vo.setId(rs.getLong("REQFORM_ID"));
			vo.setCustsegmentDesc(rs.getString("CUSTSEGMENT_DESC"));

			vo.setRequestDate(DateConstant.convertDateToStrDDMMYYYY(rs.getDate("REQUEST_DATE")));
			vo.setTmbRequestno(rs.getString("TMB_REQUESTNO"));
			vo.setCustsegmentDesc(rs.getString("CUSTSEGMENT_DESC"));
			vo.setOrganizeId(rs.getString("ORGANIZE_ID"));
			vo.setCompanyName(rs.getString("COMPANY_NAME"));
			vo.setDepartment(rs.getString("DEPARTMENT"));
			return vo;
		}
	};

	public List<Rep03000Vo> getDataRep03000(Rep03000FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		List<Rep03000Vo> rep03000VoList = new ArrayList<Rep03000Vo>();

		sql.append(
				" SELECT a.* FROM ECERT_REQUEST_FORM a WHERE 1=1 AND a.DELETE_FLAG=0 AND STATUS IN ('10009','10010') "
						+ " AND a.PAIDTYPE_CODE IN ('" + PAID_TYPE.CUSTOMER_PAY_DBD + "','"
						+ PAID_TYPE.CUSTOMER_PAY_DBD_TMB + "','" + PAID_TYPE.TMB_PAY_DBD_TMB + "') ");

		if (StringUtils.isNotBlank(formVo.getPaymentDate())) {
			sql.append("  AND  MONTH(a.PAYMENT_DATE) = MONTH(?) AND YEAR(a.PAYMENT_DATE)= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getPaymentDate());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getOrganizeId())) {
			sql.append(" AND a.ORGANIZE_ID = ?");
			params.add(formVo.getOrganizeId());
		}
//					log.info("formVo.getCustomerName() : {}",formVo.getCustomerName());
		if (StringUtils.isNotBlank(formVo.getCustomerName())) {
			sql.append(" AND a.CUSTOMER_NAME = ?");
			params.add(formVo.getCustomerName());
		}
		sql.append(" ORDER BY RECEIPT_NO , PAYMENT_DATE ASC ");
//					log.info("sqlRep03000 : {}",sql.toString());
		rep03000VoList = jdbcTemplate.query(sql.toString(), params.toArray(), rep03000RowMapper);

		return rep03000VoList;
	}

	public List<Rep03000Vo> getDataRep03000Datatable(Rep03000FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		List<Rep03000Vo> rep03000VoList = new ArrayList<Rep03000Vo>();

		sql.append(
				" SELECT a.* FROM ECERT_REQUEST_FORM a WHERE 1=1 AND a.DELETE_FLAG=0 AND STATUS IN ('10009','10010') "
						+ " AND a.PAIDTYPE_CODE IN ('" + PAID_TYPE.CUSTOMER_PAY_DBD + "','"
						+ PAID_TYPE.CUSTOMER_PAY_DBD_TMB + "','" + PAID_TYPE.TMB_PAY_DBD_TMB + "') ");

		if (StringUtils.isNotBlank(formVo.getPaymentDate())) {
			sql.append("  AND  MONTH(a.PAYMENT_DATE) = MONTH(?) AND YEAR(a.PAYMENT_DATE)= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getPaymentDate());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getOrganizeId())) {
			sql.append(" AND a.ORGANIZE_ID = ?");
			params.add(formVo.getOrganizeId());
		}
//					log.info("formVo.getCustomerName() : {}",formVo.getCustomerName());
		if (StringUtils.isNotBlank(formVo.getCustomerName())) {
			sql.append(" AND a.CUSTOMER_NAME = ?");
			params.add(formVo.getCustomerName());
		}
		sql.append(" ORDER BY RECEIPT_NO , PAYMENT_DATE ASC ");
//					log.info("sqlRep03000 : {}",sql.toString());
		rep03000VoList = jdbcTemplate.query(
				DatatableUtils.limitForDataTable(sql.toString(), formVo.getStart(), formVo.getLength()),
				params.toArray(), rep03000RowMapper);
//					rep03000VoList = jdbcTemplate.query(sql.toString(), params.toArray(), rep03000RowMapper);

		return rep03000VoList;
	}

	public int getDataRep03000Count(Rep03000FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();

		sql.append(
				" SELECT a.* FROM ECERT_REQUEST_FORM a WHERE 1=1 AND a.DELETE_FLAG=0 AND STATUS IN ('10009','10010') "
						+ " AND a.PAIDTYPE_CODE IN ('" + PAID_TYPE.CUSTOMER_PAY_DBD + "','"
						+ PAID_TYPE.CUSTOMER_PAY_DBD_TMB + "','" + PAID_TYPE.TMB_PAY_DBD_TMB + "') ");

		if (StringUtils.isNotBlank(formVo.getPaymentDate())) {
			sql.append("  AND  MONTH(a.PAYMENT_DATE) = MONTH(?) AND YEAR(a.PAYMENT_DATE)= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getPaymentDate());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getOrganizeId())) {
			sql.append(" AND a.ORGANIZE_ID = ?");
			params.add(formVo.getOrganizeId());
		}
//					log.info("formVo.getCustomerName() : {}",formVo.getCustomerName());
		if (StringUtils.isNotBlank(formVo.getCustomerName())) {
			sql.append(" AND a.CUSTOMER_NAME = ?");
			params.add(formVo.getCustomerName());
		}
		BigDecimal rs = jdbcTemplate.queryForObject(DatatableUtils.countForDatatable(sql.toString()), BigDecimal.class,
				params.toArray());
		return rs.intValue();
	}

	private RowMapper<Rep03000Vo> rep03000RowMapper = new RowMapper<Rep03000Vo>() {
		@Override
		public Rep03000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Rep03000Vo vo = new Rep03000Vo();

			vo.setId(new Long(rs.getInt("REQFORM_ID")));

			vo.setReceiptNo(rs.getString("RECEIPT_NO"));
			vo.setPaymentDate(DateConstant.convertDateToStrDDMMYYYY(rs.getDate("PAYMENT_DATE")));

			vo.setCompanyName(rs.getString("COMPANY_NAME"));
			vo.setOrganizeId(rs.getString("ORGANIZE_ID"));

			vo.setAddress(rs.getString("ADDRESS"));
			vo.setBranch(rs.getString("BRANCH"));
			vo.setMajorNo(rs.getString("MAJOR_NO"));

			String vatpercent = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.VAT_PERCENT);
			Float totalAmountTmbVat = 0f;
//				    		totalAmountTmbVat=convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_TMB"))* (1-(Float.parseFloat(vatpercent)/100));
//				    		vo.setAmountTmbVat(new BigDecimal(totalAmountTmbVat).setScale(2, BigDecimal.ROUND_HALF_EVEN));  
//				    		
//				    		Float totalAmountVat = 0f;
//				    		totalAmountVat=convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_TMB"))* (Float.parseFloat(vatpercent)/100);
//				    		vo.setAmountVat(new BigDecimal(totalAmountVat).setScale(2, BigDecimal.ROUND_HALF_EVEN));  

			Float totalAmountVat = 0f;
			totalAmountVat = (convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_TMB")) * Float.parseFloat(vatpercent)
					/ (100 + Float.parseFloat(vatpercent)));
			vo.setAmountVat(new BigDecimal(totalAmountVat).setScale(2, BigDecimal.ROUND_HALF_EVEN));

			totalAmountTmbVat = convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_TMB")) - totalAmountVat;
			vo.setAmountTmbVat(new BigDecimal(totalAmountTmbVat).setScale(2, BigDecimal.ROUND_HALF_EVEN));

			vo.setAmountTmb(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_TMB")));

			vo.setCustomerName(rs.getString("CUSTOMER_NAME"));

			return vo;
		}
	};

	public List<ReqReceiptVo> getDataReqRep03000(Rep03000FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		List<ReqReceiptVo> rep03000VoList = new ArrayList<ReqReceiptVo>();

		sql.append(" SELECT * FROM ECERT_REQFORM_RECEIPT  WHERE 1=1 AND DELETE_FLAG = 0 ");
		if (StringUtils.isNotBlank(formVo.getPaymentDate())) {
			sql.append("  AND  MONTH(CREATED_DATETIME) = MONTH(?) AND YEAR(CREATED_DATETIME)= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getPaymentDate());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getOrganizeId())) {
			sql.append(" AND ORGANIZE_ID = ?");
			params.add(formVo.getOrganizeId());
		}
//			log.info("formVo.getCustomerName() : {}",formVo.getCustomerName());
		if (StringUtils.isNotBlank(formVo.getCustomerName())) {
			sql.append(" AND CUSTOMER_NAME = ?");
			params.add(formVo.getCustomerName());
		}
		sql.append(" ORDER BY RECEIPT_NO , RECEIPT_DATE ASC ");
//			log.info("sqlRep03000 : {}",sql.toString());
		rep03000VoList = jdbcTemplate.query(sql.toString(), params.toArray(),reqReceiptMapper );

		return rep03000VoList;
	}
	
	public List<Rep03000Vo> getDataReqRep03000Datateble(Rep03000FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		List<Rep03000Vo> rep03000VoList = new ArrayList<Rep03000Vo>();

		sql.append(" SELECT * FROM ECERT_REQFORM_RECEIPT  WHERE 1=1 AND DELETE_FLAG = 0 ");
		if (StringUtils.isNotBlank(formVo.getPaymentDate())) {
			sql.append("  AND  MONTH(CREATED_DATETIME) = MONTH(?) AND YEAR(CREATED_DATETIME)= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getPaymentDate());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getOrganizeId())) {
			sql.append(" AND ORGANIZE_ID = ?");
			params.add(formVo.getOrganizeId());
		}
//			log.info("formVo.getCustomerName() : {}",formVo.getCustomerName());
		if (StringUtils.isNotBlank(formVo.getCustomerName())) {
			sql.append(" AND CUSTOMER_NAME = ?");
			params.add(formVo.getCustomerName());
		}
		sql.append(" ORDER BY RECEIPT_NO , RECEIPT_DATE ASC ");
//			log.info("sqlRep03000 : {}",sql.toString());
//		rep03000VoList = jdbcTemplate.query(sql.toString(), params.toArray(), ReqRep03000RowMapper);
		rep03000VoList = jdbcTemplate.query(
				DatatableUtils.limitForDataTable(sql.toString(), formVo.getStart(), formVo.getLength()),
				params.toArray(), convertReq03000RowMapper);

		return rep03000VoList;
	}
	
	public int getDataReqRep03000Count(Rep03000FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		sql.append(" SELECT * FROM ECERT_REQFORM_RECEIPT  WHERE 1=1 AND DELETE_FLAG = 0 ");
		if (StringUtils.isNotBlank(formVo.getPaymentDate())) {
			sql.append("  AND  MONTH(CREATED_DATETIME) = MONTH(?) AND YEAR(CREATED_DATETIME)= YEAR(?) ");
			Date date = DateConstant.convertStringMMYYYYToDate(formVo.getPaymentDate());
			params.add(date);
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getOrganizeId())) {
			sql.append(" AND ORGANIZE_ID = ?");
			params.add(formVo.getOrganizeId());
		}
		if (StringUtils.isNotBlank(formVo.getCustomerName())) {
			sql.append(" AND CUSTOMER_NAME = ?");
			params.add(formVo.getCustomerName());
		}

		BigDecimal rs = jdbcTemplate.queryForObject(DatatableUtils.countForDatatable(sql.toString()), BigDecimal.class,
				params.toArray());
		return rs.intValue();
	}


	private RowMapper<ReqReceiptVo> ReqRep03000RowMapper = new RowMapper<ReqReceiptVo>() {
		@Override
		public ReqReceiptVo mapRow(ResultSet rs, int arg1) throws SQLException {
			ReqReceiptVo list = new ReqReceiptVo();
			list.setTmb_requestno(rs.getString("TMB_REQUESTNO"));
			list.setReceipt_no(rs.getString("RECEIPT_NO"));
			list.setReceipt_date(rs.getTimestamp("RECEIPT_DATE"));
			list.setCustomer_name(rs.getString("CUSTOMER_NAME"));
			list.setOrganize_id(rs.getString("ORGANIZE_ID"));
			list.setAddress(rs.getString("ADDRESS"));
			list.setMajor_no(rs.getString("MAJOR_NO"));
			list.setAmount(rs.getBigDecimal("AMOUNT"));
			list.setAmount_tmb(rs.getBigDecimal("AMOUNT_TMB"));
			list.setAmount_vat_tmb(rs.getBigDecimal("AMOUNT_VAT_TMB"));
			list.setPrint_count(rs.getInt("PRINT_COUNT"));
			list.setReason(rs.getString("REASON"));
			return list;
		}
	};
	
	private RowMapper<Rep03000Vo> convertReq03000RowMapper = new RowMapper<Rep03000Vo>() {
		@Override
		public Rep03000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Rep03000Vo vo = new Rep03000Vo();
			
			vo.setId(new Long(rs.getInt("REQFORM_ID")));

			vo.setReceiptNo(rs.getString("RECEIPT_NO"));
			vo.setPaymentDate(DateConstant.convertDateToStrDDMMYYYY(rs.getDate("RECEIPT_DATE")));

			vo.setCompanyName(rs.getString("CUSTOMER_NAME"));
			vo.setOrganizeId(rs.getString("ORGANIZE_ID"));

			vo.setAddress(rs.getString("ADDRESS"));
			vo.setBranch(rs.getString("MAJOR_NO"));
			vo.setMajorNo(rs.getString("MAJOR_NO"));

			String vatpercent = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.VAT_PERCENT);
			Float totalAmountTmbVat = 0f;
		
			Float totalAmountVat = 0f;
			totalAmountVat = (convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_TMB")) * Float.parseFloat(vatpercent)
					/ (100 + Float.parseFloat(vatpercent)));
			vo.setAmountVat(new BigDecimal(totalAmountVat).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			totalAmountTmbVat = convertBigDecimalToLong(rs.getBigDecimal("AMOUNT_TMB")) - totalAmountVat;
			vo.setAmountTmbVat(new BigDecimal(totalAmountTmbVat).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			vo.setAmountTmb(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_TMB")));
			vo.setCustomerName(rs.getString("CUSTOMER_NAME"));
			vo.setRemark(rs.getString("REASON"));

//			list.setReceipt_no("RECEIPT_NO");
//			list.setReceipt_date(rs.getTimestamp("RECEIPT_DATE"));
//			list.setCustomer_name(rs.getString("CUSTOMER_NAME"));
//			list.setOrganize_id(rs.getString("ORGANIZE_ID"));
//			list.setAddress(rs.getString("ADDRESS"));
//			list.setMajor_no(rs.getString("MAJOR_NO"));
//			list.setAmount(rs.getBigDecimal("AMOUNT"));
//			list.setAmount_tmb(rs.getBigDecimal("AMOUNT_TMB"));
//			list.setAmount_vat_tmb(rs.getBigDecimal("AMOUNT_VAT_TMB"));
//			list.setPrint_count(rs.getInt("PRINT_COUNT"));
//			list.setReason(rs.getString("REASON"));
			return vo;
		}
	};
	
	private RowMapper<ReqReceiptVo> reqReceiptMapper = new RowMapper<ReqReceiptVo>() {
		public ReqReceiptVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReqReceiptVo list = new  ReqReceiptVo();
			list.setReceipt_id(rs.getLong("RECEIPT_ID"));
			list.setReqform_id(rs.getLong("REQFORM_ID"));
			list.setReceipt_no(rs.getString("RECEIPT_NO"));
			list.setReceipt_date(rs.getTimestamp("RECEIPT_DATE"));
			list.setCustomer_name(rs.getString("CUSTOMER_NAME"));
			list.setOrganize_id(rs.getString("ORGANIZE_ID"));
			list.setAddress(rs.getString("ADDRESS"));
			list.setMajor_no(rs.getString("MAJOR_NO"));
			list.setAmount(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT")));
			list.setAmount_dbd(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_DBD")));
			list.setAmount_tmb(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_TMB")));
			list.setAmount_vat_tmb(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_VAT_TMB")));
			list.setPrint_count(rs.getInt("PRINT_COUNT"));
			list.setReason(rs.getString("REASON"));
			list.setReceipt_date(rs.getTimestamp("RECEIPT_DATE"));
			list.setCancel_flag(rs.getInt("CANCEL_FLAG"));
			
			return list;
		}
	};
	
	public Float convertBigDecimalToLong(BigDecimal bigdecimal) {
		return (bigdecimal != null) ? bigdecimal.floatValue() : 0f;
	}

	public BigDecimal convertBigDecimalToZero(BigDecimal bigdecimal) {
		return (bigdecimal != null) ? bigdecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN)
				: BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

}
