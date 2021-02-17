package edu.human.com.common;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * EgovComAbstractMapper클래스는 sqlsession템플릿을 DAO클래스에서 직접호출하지 않고,
 * 전자정부에서 제공한  EgovComAbstractMapper(마이바티스용)클래스를 상속받아서 생성한
 * 개발자(사)클래스를 사용해서 쿼리템플릿을 재정의합니다. 
 * (오버라이드해서 전자정부에서 제공한 EgovComAbstractMapper 추상클래스에서 
 * 정의된 명세를 아래 클래스 재정의(오버라이딩)해서 메서드를 구현하게 됩니다. )
 * 
 * @author 김범주
 * 2021-02-10
 * 
 */
public abstract class EgovComAbstractMapper extends EgovAbstractMapper {
	//private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Resource(name="egov.sqlSession")
	
	public void setSqlSessionFactory(SqlSessionFactory sqlSession) {
		super.setSqlSessionFactory(sqlSession);
	}
	
	@Override
	public int delete(String queryId) {
		return getSqlSession().delete(queryId);
	}

	@Override
	public int delete(String queryId, Object parameterObject) {
		return getSqlSession().delete(queryId, parameterObject);
	}
	
	@Override
	public int insert(String queryId) {
		return getSqlSession().insert(queryId);
	}

	@Override
	public int insert(String queryId, Object parameterObject) {
		return getSqlSession().insert(queryId, parameterObject);
	}
	@Override
	public <E> List<E> selectList(String queryId, Object parameterObject) {
		return getSqlSession().selectList(queryId, parameterObject);
	}
	@Override
	public <E> List<E> selectList(String queryId) {
		return getSqlSession().selectList(queryId);
	}
	
	@Override
	public <T> T selectOne(String queryId) {
		return getSqlSession().selectOne(queryId);
	}

	@Override
	public <T> T selectOne(String queryId, Object parameterObject) {
		return getSqlSession().selectOne(queryId, parameterObject);
	}
	
	@Override
	public int update(String queryId) {
		return getSqlSession().update(queryId);
	}

	@Override
	public int update(String queryId, Object parameterObject) {
		return getSqlSession().update(queryId, parameterObject);
	}
	
	@Override
	public <E> List<E> selectList(String queryId, Object parameterObject, RowBounds rowBounds) {
		return getSqlSession().selectList(queryId, parameterObject, rowBounds);
	}

	/**
	 *  페이징범위 계산 : pageIndex(선택한페이지)와 pageSize(=limit 한페이지당 보여줄개수) 2개 값을 매개변수로 받아서 
	 *  쿼리에서 시작 인덱스 번호를 구하기: offset = (pageIndex-1)*pageSize;
	 *  1페이지일때시작*쿼리번호 : (1-1)*10 = 0 [테이블에서는 1번째 레코드]
	 *  2페이지일때시작*쿼리번호 : (2-1)*10 = 10[테이블에서는 11번째 레코드]
	 */
	
	@Override
	public List<?> listWithPaging(String queryId, Object parameterObject, int pageIndex, int pageSize) {
		int offset = (pageIndex-1) * pageSize;
		RowBounds rowBounds = new RowBounds(offset, pageSize);//(시작인덱스번호, 꺼내올개수)
		return getSqlSession().selectList(queryId, parameterObject, rowBounds);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String queryId, Object parameterObject, String mapKey) {
		// 공통코드를 위한 맵타입을 반환하는 sqlSession템플릿 사용(아래)
		return getSqlSession().selectMap(queryId, parameterObject, mapKey);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String queryId, String mapKey) {
		//	그룹코드는 키, 이름은 밸류로 맵자료형으로 반환하는 sqlSession템플릿 사용(아래)
		return getSqlSession().selectMap(queryId, mapKey);
	}
}
