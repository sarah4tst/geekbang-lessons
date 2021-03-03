<head>
<jsp:directive.include
	file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
<title>My Home Page</title>
</head>
<body>
	<div class="container-lg">

   <c:choose>
        <c:when test="${!empty requestScope['Login_Name']}">
            Hello, Welcome <b>${requestScope["Login_Name"]}</b>
        </c:when>
        <c:otherwise>
            <a class="btn" href="/login">登录</a>
            <a class="btn" href="/register">注册</a>
        </c:otherwise>
   </c:choose>

  <a href="/">Back to Homepage</a>
  <br/>
  <c:if test="${!empty requestScope['Message']}">
      <div class="alert alert-info " role="alert">
          <c:out value="${requestScope['Message']}"/>
       </div>
  </c:if>


  <c:if test="${!empty requestScope['Error_Message']}">
  <div class="alert alert-error "role="alert">
      <c:out value="${requestScope['Error_Message']}"/>
    </div>
  </c:if>

	</div>
</body>
