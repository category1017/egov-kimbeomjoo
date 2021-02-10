package edu.human.com.member.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.human.com.common.EgovComAbstractMapper;
import edu.human.com.member.service.EmployerInfoVO;

/**
 * egov에서 DAO는 sqlSession템플릿을 바로 접근하지 않고,
 * EgovAbstracdtMapper클래스를 상속받아서 DAO구현메서드를 사용
 * @author 김범주
 * 2021.02.10
 *
 */
@Repository
public class MemberDAO extends EgovComAbstractMapper{
	
	public List<EmployerInfoVO> selectMember() throws Exception {
		
		return selectList("memberMapper.selectMember"); //resources/egovframework/mapper/com/member/member_mysql.xml에서 확인
	}
}
