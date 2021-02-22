package edu.human.com.board.service.impl;

import org.springframework.stereotype.Repository;

import edu.human.com.common.EgovComAbstractMapper;

@Repository
public class BoardDAO extends EgovComAbstractMapper {
	public Integer delete_board(Integer nttId) throws Exception{
		//egov매퍼추상클래스 사용 : sqlSession템플릿 직접 접근하지 않고 사용// null값 허용을 위해 Integer로 
		return delete("boardMapper.delete_board",nttId);
	}
}
