<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template user="${user}" title="People Search Results">
	<jsp:attribute name="header">
		<h1>Search results</h1>
	</jsp:attribute>
	<jsp:body>
		<c:choose>
			<c:when test="${outcome == 'emptyField'}">
				<div class="alert alert-block">
	  				Insert, into the above field, the name of the user you are looking for!
				</div>
			</c:when>
			<c:when test="${outcome == 'noUserFound'}">
				<div class="alert alert-block">
	  				No user found!
				</div>
			</c:when>
			<c:otherwise>
				<c:forEach items="${usersList}" var="curUser">
					<t:single-result user="${curUser}" />
				</c:forEach>
			</c:otherwise>
		</c:choose>
		
		<div class="pagination pagination-centered">
  			<ul>
  				<c:if test="${curPage <= 1}"><li class="disabled"><a href="#">Prev</a></li></c:if>
  				<c:if test="${curPage > 1}"><li><a href="search-user?search=${param.search}&page=${curPage - 1}">Prev</a></li></c:if>
  				<c:forEach var="i" begin="1" end="${pageMax}">
  				<li <c:if test="${i == curPage}">class="active"</c:if>><a href="search-user?search=${param.search}&page=${i}">${i}</a></li>
  				</c:forEach>
  				<c:if test="${curPage >= pageMax}"><li class="disabled"><a href="#">Next</a></li></c:if>
  				<c:if test="${curPage < pageMax}"><li><a href="search-user?search=${param.search}&page=${curPage + 1}">Next</a></li></c:if>
  			</ul>
		</div>
		
	</jsp:body>
</t:template>