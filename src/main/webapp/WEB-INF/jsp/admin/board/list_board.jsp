<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://egovframework.gov/ctl/ui" prefix="ui" %>
<%@ include file="../include/header.jsp" %>

 <!-- 대시보드 본문 Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- 본문헤더 Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0">${brdMstrVO.bbsNm} 게시판리스트</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">${brdMstrVO.bbsNm} 게시판리스트</li>
            </ol>
          </div><!-- /.col -->
        </div><!-- /.row -->
      </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- 본문내용 Main content -->
    <section class="content">    
      <div class="container-fluid">

		<div class="row"><!-- 부트스트랩의 디자인 클래스 row -->
          <div class="col-12"><!-- 그리드시스템중 12개 가로칼럼 중 12개=width:100% -->
            <div class="card"><!-- 부트스트랩의 카드 클래스:네모난 디자인 -->
              <div class="card-header">
                <h3 class="card-title">게시물 검색</h3>

                <div class="card-tools">
                  
	                  <form name="search_form" action="<c:url value='/admin/board/list_board.do' />" method="get">
		                <input type="hidden" name="bbsId" value="<c:out value='${boardVO.bbsId}'/>" />
						<input type="hidden" name="nttId"  value="0" />
						<input type="hidden" name="bbsTyCode" value="<c:out value='${brdMstrVO.bbsTyCode}'/>" />
						<input type="hidden" name="bbsAttrbCode" value="<c:out value='${brdMstrVO.bbsAttrbCode}'/>" />
						<input type="hidden" name="authFlag" value="<c:out value='${brdMstrVO.authFlag}'/>" />
						<input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>
	                  <div class="input-group input-group-sm" >
	                  <!-- 부트스트랩 탬플릿만으로는 디자인 처리가 완전히 되지 않기 때문에 인라인 스타일 사용 -->
	                  <div>                            
	                        <select name="searchCnd" class="form-control">
	                        	<option value="0" <c:if test="${searchVO.searchCnd == '0'}">selected="selected"</c:if> >제목</option>
								<option value="1" <c:out value="${(searchVO.searchCnd=='1')?'selected':''}" /> >내용</option>             
								<option value="2" <c:if test="${searchVO.searchCnd == '2'}">selected="selected"</c:if> >작성자</option> 
	                        </select>
	                   </div>
	                  	<div>
	                    <input   type="text" name="searchWrd" value='<c:out value="${searchVO.searchWrd}"/>' class="form-control float-right" placeholder="Search">
						</div>
	                    <div class="input-group-append">
	                      <button type="submit" class="btn btn-default">
	                        <i class="fas fa-search"></i>
	                      </button>
	                    </div>
	                   </div>
	                  </form>
                  
                </div>
              </div>
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0">
                <table class="table table-hover text-nowrap">
                  <thead>
                    <tr>
                      <th>번호</th><!-- 테이블 헤드 타이틀태그 th -->
                      <th>제목</th>
                      <th>작성자</th>
                      <th>작성일</th>
                      <th>조회수</th>  
                    </tr>
                  </thead>
                  <tbody>
                  <c:forEach items="${resultList}" var="result" varStatus="status">
	                  <tr>
	                   <td>
	                   <!-- 공지사항전체게시물수20 + 1 -((현재페이지번호2-1)*페이지당보여줄개수10 )+forEach인덱스1 -->
	                   <!-- 20~11 이전페이지 > 이후페이지 결과10부터  -->
	                   <c:out value="${paginationInfo.totalRecordCount+1 - ((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/>
	                   </td>            
	                      <td>
	                      <form name="view_form"action="<c:url value='/admin/board/view_board.do' />" method="post">
		                     <!-- 답글일경우 계단식표시 추가 -->
		                     <c:if test="${result.replyLc!=0}">
				                <c:forEach begin="0" end="${result.replyLc}" step="1">
				                    &nbsp;<!-- 들여쓰기 역할하는 스페이스바 특수문자 -->
				                </c:forEach>
				                &#8627;<!-- 화살표 특수문자 -->
				             </c:if>
	                      	<input type="hidden" name="bbsId" value="<c:out value='${result.bbsId}'/>" />
	                        <input type="hidden" name="nttId"  value="<c:out value="${result.nttId}"/>" />
	                        <input type="hidden" name="bbsTyCode" value="<c:out value='${brdMstrVO.bbsTyCode}'/>" />
	                        <input type="hidden" name="bbsAttrbCode" value="<c:out value='${brdMstrVO.bbsAttrbCode}'/>" />
	                        <input type="hidden" name="authFlag" value="<c:out value='${brdMstrVO.authFlag}'/>" />
	                        <input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>
	                        <span class="btn_submit" style="cursor:pointer;"><c:out value='${result.nttSj}'/></span>
	                      </form>
	                      </td> 
	                      <td>${result.frstRegisterNm}</td>
	                      <td>${result.frstRegisterPnttm}</td>
	                      <td><span class="badge bg-danger">${result.inqireCo}</span></td>
	                      <!-- 권한표시는 부트스트랩 뺏지 클래스 사용 -->
	                  </tr>
                   </c:forEach>
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
                              
             </div>
            <!-- /.card -->
                     
              <!-- 버튼영역 시작 -->
              <div class="card-body">
              <a href="<c:url value='/admin/board/insert_board.do' />" class="btn btn-primary float-right">글작성</a>
              <!-- 부트스트랩 디자인 버튼클래스를 이용해서 a태그를 버튼모양으로 만들기(위) -->
              <!-- btn클래스명이 버튼모양으로 변경, btn-primary클래스명은 버튼색상을 변경하는 역할 -->
              <!-- style이 아닌 class인 이유 부트스트랩에 이미 style이 적용되어있어서 -->
              

              </div>
              <!-- 버튼영역  끝 -->  
    
           <!-- 페이징처리 시작 -->     
            <div class="pagination justify-content-center">                  
             <ul class="pagination">
               <ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="fn_egov_select_noticeList" />    
             </ul>
            </div>
            <!-- 페이징처리 끝 -->
            
                        
          </div>
        </div>

      </div><!-- /.container-fluid -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
  

<%@ include file="../include/footer.jsp" %>
<script>
$(document).ready(function(){
	$(".btn_submit").on("click",function(){
		var form_object = $(this).parent("form[name='view_form']");
		form_object.submit();
	});
});
</script>