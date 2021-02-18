package edu.human.com.util;

public class PageVO {
	//다중게시판 추가 때문에 매퍼쿼리에 보낼 board_type변수가 필요
	private String board_type;
	//변수 중에 boolean(일반데이터형변수) / Boolean(대문자로시작-클래스형변수)
	//일반데이터형변수는 Null값은 오류가 나타나나, 클래스형변수는 Null로 입력되었을때 처리(허용)하는 로직이 있음.
	private int perPageNum;///  페이징리스트 목록 개수 변수(리스트 하단에 보이는 페이징번호의 갯수)
	private int queryPerPageNum;//쿼리에서 사용하는 1페이지당 출력할 개수값 변수 (모바일버전을 위해 생성)
	private Integer page;//jsp에서 선택한 페이지 번호값이 들어가는 변수
	private int queryStartNo;//쿼리에서 사용되는 시작인덱스 변수
	private boolean prev;//[계산식]페이징에서 이전 번호가 있을때 표시값이 들어가는 변수
	private boolean next;//[계산식]페이징에서 이후 번호가 있을때 표시값이 들어가는 변수
	//위에 생성한 prev, next 변수값이 있는지 없는지 확인하려면, [계산식]이 필요하다.이때 계산할때, 필요한 변수 3개가 필요하다(아래) 
	private int totalCount;//회원[게시물] 전체의 갯수값이 들어가는 변수
	private int startPage;//jsp화면에서 보여주는 페이징 리스트의 시작번호
	private int endPage;//jsp화면에서 보여주는 페이징 리스트의 끝번호
	//start 페이지와 end페이지 변수가 필요한 이유는?
	
	//검색에 필요한 변수2개도 포함시켜서, 컨트롤러에서 매개변수 사용을 축소하게 됩니다.
	private String search_type;//검색조건
	private String search_keyword;//검색어
	
	public String getBoard_type() {
		//this.board_type = "notice";//세션으로 사용할 예정
		return board_type;
	}

	public void setBoard_type(String board_type) {
		this.board_type = board_type;
	}
	
	//전체 클래스에서 [게산식]이 4개가 필요. 자동x, 개발자가 만들어야함.(아래)
	//게산식4개로 반환되는 값은(예) :startPage(11),endPage(20),prev(true),next(false)
	//아래 메서드에서 사용되는 totalCount변수는 컨트롤러에서 쿼리문을 통해서 전송받는다.
	private void calcPage() {
		//page변수는 현재 jsp화면에서 클릭한 페이지번호 예로11  를 받아서 아래 게산식에서 사용
		//int로 형변환 : 2.1,  2.3 -> 모두 2로 반환이됨.정수
		//ceil메서드   : 천장값을 반환 1.1 -> 2, 2.3 ->3  올림
		//floor메서드 : 바닥값을 반환 1.1 -> 1, 2.3 ->2  내림
		//ceil(1/10) -> 1.0  0.9 0.8 0.7 ...0.1 0.0
		//ceil(11/10) * 10 ->20페이지
		//=> ceil(1.1) ->2 -> 2*10 ->20페이지
		int tempEnd = (int)(
				Math.ceil(page/(double)this.perPageNum)*this.perPageNum
				);
		//jsp에서 클릭한 페이지번호 예로 1을 기준으로 끝페이지를 계산한다.(위)
		//예) <1 2 3 4 5 6 7 8 9 10(tempEnd)> 페이징 리스트의 시작1 끝20 값이 바뀌게 됨.
		//예) <11 12 13 14 15 16 17 18 19 20(tempEnd)> 시작 11 , 끝20
		this.startPage = (tempEnd - this.perPageNum) + 1 ;
		//jsp에서 11을 클릭했을 때, (20 - 10) + 1 = 11 스타트페이지값(위)
		//(아래) 20*10 = 200 개의 레코드(회원[게시물])
		//꼭 200개의 레코드라는 것은 아니다 회원[게시물] 195개일 수도 있다.이럴경우를 위해 조건문 작성 
		if(tempEnd*this.queryPerPageNum > this.totalCount){//경우 200>195
			//(임시끝페이지*쿼리에서 1페이지당출력할개수 >실제전체개수)
			//클릭한 page번호로 계산된 게시물수가 실제게시물(totalCount)수보다 클때
			this.endPage = (int)Math.ceil(
					this.totalCount/(double)this.queryPerPageNum
					);// 195/10 ->19.5 -> [20] 19.9 19.8 ...19.5
		}else {
			//전체 회원[게시물]수가 195일때 page 1을 클릭한 경우 100 > 195
			//결과가 195/10 -> 20,다음처럼 나와서 위 조건이 되면 안되고 else문을 써야 올바른결과가 나온다.  
			this.endPage =tempEnd;// tempEnd가 10이니까 endPage가 10
		}
	
	//================여기까지가 endPage를 구하는 계산식===============================
	//아래는 prev, next 구하는 방식
		this.prev = (this.startPage != 1);//예, 스타트페이지 11 결과값은 true 
		//시작페이지가 1보다 크면 무조건 이전페이지가 있다고 봄(위)
		this.next=(this.endPage*this.queryPerPageNum < this.totalCount);
		//20*10 <195 결과는 false, 이기 때문에 jsp에서 꺽쇠> 표시가 안보이게 처리.
		//예 < 11 12 13 14 15 16 17 18 19 20(tempEnd) 시작 11과 끝 20
	
		
	}
	
	//마우스 오른쪽버튼 source -> Generate getters and setters로 생성(아래)
	public int getPerPageNum() {
		return perPageNum;
	}
	public void setPerPageNum(int perPageNum) {
		//perPageNum = 10;//강제로 1페이지당 보여줄 갯수값을 10개로 지정한다.
		this.perPageNum = perPageNum;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public int getQueryStartNo() {
		//DB쿼리에서 사용 시작 인덱스번호(0부터시작)
		//시작인덱스 번호를 구하는 계산식 = (jsp에서 클릭한 페이지번호-1)*페이지당 보여지는 갯수
		// 1페이지계산  10(1페이지당출력할갯수) * (1[몇번째페이지번호]-1) = 0 1페이지일때
		// 2페이지계산  10x(2-1) = 10[계산결과나온 시작페이지번호] 2페이지일때
		queryStartNo = queryPerPageNum*(this.page-1);//개발자가 추가한 계산식//queryPerPageNum=10
		return queryStartNo;
	}
	public void setQueryStartNo(int queryStartNo) {
		this.queryStartNo = queryStartNo;
	}
	public boolean getPrev() {
		return prev;
	}
	public void setPrev(boolean prev) {
		this.prev = prev;
	}
	public boolean getNext() {
		return next;
	}
	public void setNext(boolean next) {
		this.next = next;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		//totalCount변수값이 만들어지는 순간 calcPage메서드가 실행되면, 4개의 변수값 SET반환.
		calcPage();//totalCount변수값이 필수로 필요한 메서드.
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public String getSearch_type() {
		return search_type;
	}
	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}
	public String getSearch_keyword() {
		return search_keyword;
	}
	public void setSearch_keyword(String search_keyword) {
		this.search_keyword = search_keyword;
	}

	public int getQueryPerPageNum() {
		return queryPerPageNum;
	}

	public void setQueryPerPageNum(int queryPerPageNum) {
		this.queryPerPageNum = queryPerPageNum;
	}
}
