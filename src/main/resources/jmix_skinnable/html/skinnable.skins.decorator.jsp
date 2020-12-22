<%@ taglib uri="http://www.jahia.org/tags/jcr" prefix="jcr" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>


<c:set var="actionUrl" value="${url.base}${currentNode.path}.decorateNode.do" />

<template:addResources>
    <script type="text/javascript">
        $(document).ready(function() { 
        	$("#setBg_${currentNode.identifier}").click(function(){
        		var bgColor = $("#setBgVal_${currentNode.identifier}").val();
        	    $.ajax({
        	       url : '${actionUrl}',
        	       type : 'post',
        	       dataType : 'json',
        	       data: { bgColor: bgColor},
        	       success : function(response){
						console.log("result " + response.succes);
        	       },
        	       error : function(result, statut, error){
         				console.log("error");
        	       },
        	       complete : function(result, statut){
          				console.log("complete");
						window.location.replace("${renderContext.mainResource.node.url}");
        	       }
        	    });
        	});
		});
    </script>
</template:addResources>

<jcr:nodeProperty name="bgColor" node="${currentNode}" var="bgColor"/>

<c:if test="${jcr:hasPermission(currentNode, 'decorate-view-button') }">
	<div>
		<c:choose>
			<c:when test="${renderContext.liveMode}">
				<input id="setBgVal_${currentNode.identifier}" type="text" value="${bgColor.string}"/> 
				<button id="setBg_${currentNode.identifier}">Set Background Color</button>		
			</c:when>
			<c:otherwise>
				Decorator is only available in Live Mode.
			</c:otherwise>
		</c:choose>
		
	</div>
</c:if>
<div style="background-color: ${bgColor.string} ">
	${wrappedContent}
</div>