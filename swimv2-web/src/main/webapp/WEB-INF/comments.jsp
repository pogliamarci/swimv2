<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template user="${user}" title="SWIMv2 - Personal Area">
	<jsp:attribute name="header">
		<h1>${hr.subject}</h1>
		<c:if test="${user == hr.sender }">
			<h3> between you and ${hr.receiver.name} ${hr.receiver.surname}</h3>
		</c:if>
		<c:if test="${user == hr.receiver }">
			<h3> between you and ${hr.sender.name} ${hr.sender.surname}</h3>
		</c:if>
		<p><b>Involved Abilities:</b></p>
		<c:forEach var="ab" items="${hr.abilities}">
			<span class="badge badge-info">${ab.name}</span>
		</c:forEach>
	</jsp:attribute>
	<jsp:body>
	
	<div class="container">
	<c:choose>
		<c:when test="${error == 'ClosedRequest'}">
			<div class="alert alert-error">You can't comment on this request because it's closed! Open another help request or send a message, if you need to contact the other person.</div>
		</c:when>
		<c:otherwise>
		<div class="row">
			<div class="span9">
				<div class="row">
				<c:forEach var="c" items="${comments}">
					<c:choose>
						<c:when test="${user == c.sender}">
							<div class="span6 pull-right"><t:comment comment="${c}" /></div>
						</c:when>
						<c:otherwise>
							<div class="span6 pull-left"><t:comment comment="${c}" /></div>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</div>
				
				<hr>
				<c:if test="${isopen}">
					<form name="newcomment" method="post" action="comment">
						<label>Add a new comment</label>
						<input type="hidden" name="hr_id" value="${hr.id }">
						<textarea class="input-block-level" name="comment" rows="4" cols="50" maxlength="255"></textarea>
						<input type="submit" class="btn" value="Send">
					</form>
				</c:if>		
			</div>
			<div class="span3">
				<c:if test="${user == hr.receiver}">
					<t:providing-hr hr="${hr}" />
					<c:if test="${hr.status == 'ZOMBIE'}">
						<t:feedbackform hr="${hr}" role="helper" />
					</c:if>
				</c:if>
				<c:if test="${user == hr.sender}">
					<t:asking-hr hr="${hr}" />
					<c:if test="${hr.status == 'ACCEPTED'}">
						<t:feedbackform hr="${hr}" role="asker" />
					</c:if>
				</c:if>
			</div>
		</div>
		</c:otherwise>
	</c:choose>
	</div>
	
	</jsp:body>
</t:template>