package edu.human.com.util;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.human.com.member.service.EmployerInfoVO;
import edu.human.com.member.service.MemberService;

@Controller
public class CommonUtil {
	@Inject
	private MemberService memberService;
	
	@RequestMapping(value="/idcheck.do",method=RequestMethod.GET)
	@ResponseBody //반환값으로 페이지를 명시하지 않고, text라고 명시 (없으면 예를들어 return "1" 일때  1.jsp 이 페이지로 이동)
	public String idcheck(@RequestParam("emplyr_id") String emplyr_id) throws Exception {
		String result = "0";//기본값으로 중복ID가 없다는 표시 0
			EmployerInfoVO memberVO = memberService.viewMember(emplyr_id);
			if(memberVO !=null) {
				result="1";//중복ID가 있을때 표시 1
			}
		return result;
	}
}
