<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:private-page user="${user}" title="SWIMv2 - Profile Page">
	<jsp:attribute name="header">
		<h1>Profile of ${profile.name} ${profile.surname}</h1>
		<c:if test="${user.admin && profile.admin == false}">
			<a href="promote?profileId=${profile.id}"><button
					class="btn btn-success" type="button">Promote to Admin</button></a>
		</c:if>
		<c:if test="${user.admin && profile.admin}">
			<div class="alert alert-info">This User is an Admin</div>
		</c:if>
	</jsp:attribute>
	<jsp:body>
	<div class="container">
					<div class="span3" style="float: left" >
								<img class="profile_img"
					src="${pageContext.request.contextPath}/images/profile?user=${user.id}"
					width="220px" height="330px" />
								<p>${profile.birthdate}</p>
								<p>${profile.email}</p>
								<p>${profile.website}</p>
								<p>${profile.location}</p>
					</div>
					<div class="span5" style="float: left" >

								<div style="float: left">
									<p>${profile.minibio}</p>
								</div>
								
								<div style="float: left">
								
									<p>${profile.description}</p>
								
								</div>
					</div>
					<div class="span4" style="float: left" >
								<c:forEach var="hrp" items="${providedList}">
									<p>${hrr.subject }</p>
								</c:forEach>
					
							<div>
								<c:forEach var="hrr" items="${receivedList}">
									<p>${hrr.subject }</p>
								</c:forEach>
							</div>
							<c:if test="${user.id != profile.id }">
								<ul>
								<c:if test="${showFR}">
										<li><a
								href="friendrequest?asker=${user.id}&receiver=${profile.id}">richiesta di amicizia</a></li>
								</c:if>
								<li><a href="message?">messaggio</a></li>
								<li><a href="helprequest?">helprequest</a></li>
								</ul>
							</c:if>
					
					</div>
	</div>
	</jsp:body>
</t:private-page>