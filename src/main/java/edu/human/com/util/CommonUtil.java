package edu.human.com.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.impl.SimpleLog;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.human.com.member.service.EmployerInfoVO;
import edu.human.com.member.service.MemberService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.uat.uia.service.EgovLoginService;
import egovframework.rte.fdl.string.EgovObjectUtil;

@Controller
public class CommonUtil {
	@Inject
	private MemberService memberService;
	
	@Autowired
	private EgovLoginService loginService;
	@Autowired
	private EgovMessageSource egovMessageSource;
	
	private Logger logger = Logger.getLogger(SimpleLog.class);
	
	//스프링시큐리티 권한체크X, 개발자가 임의로 만든 권한체크(아래)
	//로그인 인증+권한 체크 1개의 메서드로 처리 (아래) AdminController에서 관리자 권한 체크 때문에 생성.
	//기존 전자정부에서 List<String>사용한 이유 ROLE_ADMIN, ROLE_USER 권한이2개 이상일수 있음
	public Boolean getAuthorities()throws Exception {
		Boolean authority = Boolean.FALSE;
		//인증체크(로그인 상태인지 아닌지)//(LoginVO가 null이면 = 로그인상태가 아니면)
		if (EgovObjectUtil.isNull((LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION))) {
			return authority;
		}
		//권한체크(관리자인지, 일반사용자인지 판단)
		LoginVO sessionLoginVO = (LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
		EmployerInfoVO memberVO = memberService.viewMember(sessionLoginVO.getId());
		if("GROUP_00000000000000".equals(memberVO.getGROUP_ID())) {
			authority = Boolean.TRUE;
		}
		//여기까지 true값을 가져오면, 관리자라는 의미
		return authority;
	}
		//스프링시큐리티 권한체크(아래)
		//EgovUserDetailsHelper클래스에 있는 메서드를 복사해서 CommonUtil클래스에 가져다가 사용
		private List<String> getAuthorities(String string) {
			List<String> listAuth = new ArrayList<String>();
			if (EgovObjectUtil.isNull((LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION))) {
				return null;
			}
			//스프링시큐리티 연동 추가(아래)
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String listAuthTemp1 = authentication.getAuthorities().toString();
			logger.debug("디버그 : 로그인한 사용자의 권한 리스트 출력" + listAuthTemp1);
			//String을 리스트형으로 변환해서 반환값에 넣습니다.(아래) [ROLE_ADMIN, ROLE_USER]를 리스트형변환
			listAuthTemp1 = listAuthTemp1.replace("[", "");
			listAuthTemp1 = listAuthTemp1.replace("]", "");
			listAuthTemp1 = listAuthTemp1.replace(" ", "");
			String[] listAuthTemp2 = listAuthTemp1.split(",");
			listAuth = Arrays.asList(listAuthTemp2);//배열을 리스트로 형변환하는 메서드 asList사용
			/* 배열을 리스트형으로 변경결과(가로데이터가 세로데이터로 변경)
			ROLE_ADMIN
			ROLE_USER
			 */
			return listAuth;
	}
	/**
	 * 기존 로그인 처리는 egov것 그대로 사용하고,
	 * 단 , 로그인 처리 이후 이동할 페이지를 OLD에서 NEW로 변경합니다.
	 */
	@RequestMapping(value = "/login_action.do")//변경1
	public String actionLogin(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletResponse response, HttpServletRequest request, ModelMap model) throws Exception {

		// 1. 일반 로그인 처리
		LoginVO resultVO = loginService.actionLogin(loginVO);
		boolean loginPolicyYn = true;

		if (resultVO != null && resultVO.getId() != null && !resultVO.getId().equals("") && loginPolicyYn) {
			//로그인 성공시
			request.getSession().setAttribute("LoginVO", resultVO);
			//로그인 성공후 관리자 그룹일때 관리자 세션ROLE_ADMIN명 추가
			/* //스프링시큐리티 사용X,개발자 임의로 생성
			LoginVO sessionLoginVO = (LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
			EmployerInfoVO memberVO = memberService.viewMember(sessionLoginVO.getId());
			if("GROUP_00000000000000".equals(memberVO.getGROUP_ID())) {
				request.getSession().setAttribute("ROLE_ADMIN", memberVO.getGROUP_ID());
			}
			*/
			//스프링시큐리티 연동 추가
			UsernamePasswordAuthenticationFilter springSecurity = null;
			ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
			Map<String, UsernamePasswordAuthenticationFilter> beans = act.getBeansOfType(UsernamePasswordAuthenticationFilter.class);
			if (beans.size() > 0) {
				springSecurity = (UsernamePasswordAuthenticationFilter) beans.values().toArray()[0];
				springSecurity.setUsernameParameter("egov_security_username");
				springSecurity.setPasswordParameter("egov_security_password");
				springSecurity.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(
						request.getServletContext().getContextPath() + "/egov_security_login", "POST"));
			} else {
				//throw new IllegalStateException("No AuthenticationProcessingFilter");
				return "forward:/tiles/home.do"; //context-security.xml에서 bean설정이 없을때
			}
			//false이면 chain 처리 되지 않음(아래)(filter가 아닌 경우 false로...)
			springSecurity.setContinueChainBeforeSuccessfulAuthentication(false);
			//set으로 username과 password를 처리 - context-security의 쿼리에서 ? 변수값을 저장합니다.(아래)
			springSecurity.doFilter(new RequestWrapperForSecurity(request, resultVO.getId(), resultVO.getPassword()), response, null);

			//System.out.println("context-security.xml파일의 jdbcAuthoritiesByUsernameQuery 확인");
	    	
			//List<String> authorities = EgovUserDetailsHelper.getAuthorities();
	    	List<String> authorities = getAuthorities("EgovUserDetailsHelper is Not use");
			
	    	// 1. authorites 에  권한이 있는지 체크 TRUE/FALSE
	    	logger.debug("디버그:" + authorities.contains("ROLE_ADMIN"));
	    	logger.debug("디버그:" + authorities.contains("ROLE_USER"));
	    	logger.debug("디버그:" + authorities.contains("ROLE_ANONYMOUS"));
			//위 값을 이용해서 세션을 발생시킵니다.
	    	if(authorities.contains("ROLE_ADMIN")) {
	    		request.getSession().setAttribute("ROLE_ADMIN", true);
	    	}
			return "forward:/tiles/home.do";//변경2 new홈으로 이동
		} else {
			//로그인 실패시
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.login")); 
			return "login.tiles";//변경3 new홈 로그인으로 이동
		}
	}
	

class RequestWrapperForSecurity extends HttpServletRequestWrapper {
	private String username = null;
	private String password = null;
	
	public RequestWrapperForSecurity(HttpServletRequest request, String username, String password) {
		super(request);
		this.username = username;
		this.password = password;
	}

	@Override
	public String getRequestURI() {
		return ((HttpServletRequest) super.getRequest()).getContextPath() + "/egov_security_login";
	}

	@Override
	public String getServletPath() {
		return ((HttpServletRequest) super.getRequest()).getContextPath() + "/egov_security_login";
	}

	@Override
	public String getParameter(String name) {
		if (name.equals("egov_security_username")) {
			return username;
		}
		if (name.equals("egov_security_password")) {
			return password;
		}

		return super.getParameter(name);
	}
	
}
	
	
	//첨부파일 삭제 메서드(아래)
    /**
     * XSS 방지 처리. 자바스크립트 코드를 실행하지 못하는 특수문자로 replace(교체)하는 내용
     *
     * @param data
     * @return
     */
    public String unscript(String data) {
        if (data == null || data.trim().equals("")) {
            return "";
        }

        String ret = data;

        ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
        ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");

        ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
        ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");

        ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
        ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");

        ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
        ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");

        ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
        ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");

        return ret;
    }
	
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
