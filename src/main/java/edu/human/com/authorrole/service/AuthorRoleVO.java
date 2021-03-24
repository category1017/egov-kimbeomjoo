package edu.human.com.authorrole.service;
/**
 * AuthorRoleVO 클래스는 jsp,DAO에서 사용하는 임시저장소 (GET/SET하는 역할) 
 * @author categoryK
 *
 */
public class AuthorRoleVO {
	private int UTHORROLE_ID;
	private String ROLE_PTTRN;
	private String AUTHOR_CODE;
	private String AUTHORROLE_DC;
	private String SORT_ORDR;
	private String USE_AT;
	
	@Override
	public String toString() {
		return "AuthorRoleVO [UTHORROLE_ID=" + UTHORROLE_ID + ", ROLE_PTTRN=" + ROLE_PTTRN + ", AUTHOR_CODE="
				+ AUTHOR_CODE + ", AUTHORROLE_DC=" + AUTHORROLE_DC + ", SORT_ORDR=" + SORT_ORDR + ", USE_AT=" + USE_AT
				+ "]";
	}
	
	public int getUTHORROLE_ID() {
		return UTHORROLE_ID;
	}
	public void setUTHORROLE_ID(int uTHORROLE_ID) {
		UTHORROLE_ID = uTHORROLE_ID;
	}
	public String getROLE_PTTRN() {
		return ROLE_PTTRN;
	}
	public void setROLE_PTTRN(String rOLE_PTTRN) {
		ROLE_PTTRN = rOLE_PTTRN;
	}
	public String getAUTHOR_CODE() {
		return AUTHOR_CODE;
	}
	public void setAUTHOR_CODE(String aUTHOR_CODE) {
		AUTHOR_CODE = aUTHOR_CODE;
	}
	public String getAUTHORROLE_DC() {
		return AUTHORROLE_DC;
	}
	public void setAUTHORROLE_DC(String aUTHORROLE_DC) {
		AUTHORROLE_DC = aUTHORROLE_DC;
	}
	public String getSORT_ORDR() {
		return SORT_ORDR;
	}
	public void setSORT_ORDR(String sORT_ORDR) {
		SORT_ORDR = sORT_ORDR;
	}
	public String getUSE_AT() {
		return USE_AT;
	}
	public void setUSE_AT(String uSE_AT) {
		USE_AT = uSE_AT;
	}

	
}